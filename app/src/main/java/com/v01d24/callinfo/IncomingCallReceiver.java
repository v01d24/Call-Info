package com.v01d24.callinfo;

import static android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class IncomingCallReceiver extends BroadcastReceiver {

    private static String lastState = TelephonyManager.EXTRA_STATE_IDLE;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_PHONE_STATE_CHANGED.equals(action)) {
            handleStateChanged(context, intent);
        }
    }

    private void handleStateChanged(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(lastState) &&
            TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            CallInfoService.requestPhoneNumberLookup(context, phoneNumber);
        }
        IncomingCallReceiver.lastState = state;
    }
}
