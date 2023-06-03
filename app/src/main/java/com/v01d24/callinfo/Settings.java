package com.v01d24.callinfo;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private static final String PREFERENCES_NAME = "com.v01d24.callinfo.PREFERENCE_FILE_KEY";
    private static final String KEY_SCAM_CALLS_REJECTION_ENABLED = "SCAM_CALLS_REJECTION_ENABLED";

    private final SharedPreferences preferences;

    public Settings(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean isScamCallsRejectionEnabled() {
        return preferences.getBoolean(KEY_SCAM_CALLS_REJECTION_ENABLED, false);
    }

    public void setScamCallsRejectionEnabled(boolean enabled) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_SCAM_CALLS_REJECTION_ENABLED, enabled);
        editor.apply();
    }
}
