package com.v01d24.callinfo.usecases;

import android.util.Log;

import androidx.annotation.NonNull;

import com.v01d24.callinfo.Notificator;
import com.v01d24.callinfo.Phone;
import com.v01d24.callinfo.PhoneNumberScore;
import com.v01d24.callinfo.db.CallInfo;
import com.v01d24.callinfo.db.CallInfoRepository;
import com.v01d24.callinfo.registry.PhoneNumberInfo;
import com.v01d24.callinfo.registry.PhoneNumberRegistry;
import com.v01d24.callinfo.registry.RegistryException;

public class IncomingCall {
    private static final String TAG = IncomingCall.class.getName();

    private static final String[] NUMBERS_WHITELIST = {};

    private static final long WEEK = 7 * 24 * 60 * 60 * 1000;
    private static final int RETRIES = 3;

    private final String phoneNumber;
    private final Phone phone;
    private final CallInfoRepository callInfoRepository;
    private final PhoneNumberRegistry phoneNumberRegistry;
    private final Notificator notificator;

    private final boolean isScamCallsRejectionEnabled;

    public IncomingCall(@NonNull String phoneNumber,
                        @NonNull Phone phone,
                        @NonNull CallInfoRepository callInfoRepository,
                        @NonNull PhoneNumberRegistry phoneNumberRegistry,
                        @NonNull Notificator notificator,
                        boolean isScamCallsRejectionEnabled) {
        this.phoneNumber = phoneNumber;
        this.phone = phone;
        this.callInfoRepository = callInfoRepository;
        this.phoneNumberRegistry = phoneNumberRegistry;
        this.notificator = notificator;
        this.isScamCallsRejectionEnabled = isScamCallsRejectionEnabled;
    }

    public void run() {
        if (isWhitelisted(phoneNumber)) {
            Log.e(TAG, "Number is whitelisted!!!");
            return;
        }
        if (phone.isKnownNumber(phoneNumber)) {
            Log.e(TAG, "Number was found in phone book");
            return;
        }
        long timestamp = System.currentTimeMillis();
        CallInfo callInfo = new CallInfo(phoneNumber, timestamp);
        callInfoRepository.save(callInfo);
        try {
            PhoneNumberInfo info = getPhoneNumberInfo(phoneNumber);
            Log.e(TAG, "Number score: " + info.score);
            Log.e(TAG, "Number summary: " + info.summary);
            callInfo.setScore(info.score);
            callInfo.setSummary(info.summary);
            callInfoRepository.save(callInfo);
            notificator.createIncomingCallNotification(phoneNumber, info);
            callInfoRepository.deleteOld(timestamp - WEEK);
            if (PhoneNumberScore.NEGATIVE.equals(info.score) && isScamCallsRejectionEnabled) {
                Log.e(TAG, "Reject scam call");
                phone.endCall();
            }
        } catch (RegistryException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private boolean isWhitelisted(String phoneNumber) {
        for (String whitelistNumber: NUMBERS_WHITELIST) {
            if (phoneNumber.equals(whitelistNumber)) {
                return true;
            }
        }
        return false;
    }

    private PhoneNumberInfo getPhoneNumberInfo(String phoneNumber) throws RegistryException {
        RegistryException lastException = null;
        for (int i = 0; i < RETRIES; i++) {
            try {
                return phoneNumberRegistry.lookup(phoneNumber);
            } catch (RegistryException e) {
                lastException = e;
            }
            try {
                Thread.sleep(1000 * (i + 1));
            } catch (InterruptedException ignored) {}
        }
        throw lastException;
    }
}
