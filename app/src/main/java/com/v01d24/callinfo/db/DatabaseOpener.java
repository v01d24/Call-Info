package com.v01d24.callinfo.db;

import android.content.Context;

import androidx.annotation.NonNull;

public class DatabaseOpener {
    private final DatabaseHelper dbHelper;

    public DatabaseOpener(@NonNull Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public @NonNull Database openReadable() {
        return new Database(dbHelper.getReadableDatabase());
    }

    public @NonNull Database openWritable() {
        return new Database(dbHelper.getWritableDatabase());
    }
}
