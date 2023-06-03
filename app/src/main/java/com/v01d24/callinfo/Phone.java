package com.v01d24.callinfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Phone {

    private static final String TAG = Phone.class.getSimpleName();

    private final Context context;

    public Phone(Context context) {
        this.context = context;
    }

    public boolean isKnownNumber(String phoneNumber) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber)
        );
        Cursor cursor = resolver.query(
                uri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                null, null, null
        );

        boolean numberFound = false;
        if (cursor != null) {
            numberFound = cursor.getCount() > 0;
            cursor.close();
        }
        return numberFound;
    }

    public void endCall() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("service call phone 5 \n");
            } catch (Exception exc) {
                Log.e(TAG, exc.getMessage());
            }
        });
    }
}
