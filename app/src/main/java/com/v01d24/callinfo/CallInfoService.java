package com.v01d24.callinfo;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.v01d24.callinfo.db.CallInfoRepository;
import com.v01d24.callinfo.db.DatabaseOpener;
import com.v01d24.callinfo.registry.PhoneNumberRegistryFactory;
import com.v01d24.callinfo.registry.PhoneNumberRegistry;
import com.v01d24.callinfo.usecases.IncomingCall;

public class CallInfoService extends IntentService {
    private static final String ACTION_PHONE_NUMBER_LOOKUP = "ACTION_PHONE_NUMBER_LOOKUP";
    private static final String EXTRA_PHONE_NUMBER = "EXTRA_PHONE_NUMBER";

    public static void requestPhoneNumberLookup(Context context, String phoneNumber) {
        Intent intent = new Intent(context, CallInfoService.class);
        intent.setAction(ACTION_PHONE_NUMBER_LOOKUP);
        intent.putExtra(EXTRA_PHONE_NUMBER, phoneNumber);
        context.startService(intent);
    }

    public CallInfoService() {
        super("CallInfoService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        if (ACTION_PHONE_NUMBER_LOOKUP.equals(intent.getAction())) {
            String rawNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER);
            handleIncomingCall(PhoneNumberParser.parse(rawNumber));
        }
    }

    private void handleIncomingCall(String phoneNumber) {
        Phone phoneBook = new Phone(this);
        CallInfoRepository repository = new DatabaseOpener(this)
                .openWritable()
                .getCallInfoRepository();
        PhoneNumberRegistry registry = PhoneNumberRegistryFactory.createDefault();
        Notificator notificator = new Notificator(this);
        boolean isScamCallsRejectionEnabled = new Settings(this)
                .isScamCallsRejectionEnabled();
        new IncomingCall(
                phoneNumber, phoneBook, repository, registry, notificator,
                isScamCallsRejectionEnabled
        ).run();
    }
}
