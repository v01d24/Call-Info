package com.v01d24.callinfo.db;

import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

public class Database {
    private final SQLiteDatabase db;

    public Database(@NonNull SQLiteDatabase db) {
        this.db = db;
    }

    public @NonNull CallInfoRepository getCallInfoRepository() {
        return new CallInfoRepository(db);
    }
}
