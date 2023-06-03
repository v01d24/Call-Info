package com.v01d24.callinfo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "call_info.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + CallInfoTable.NAME + " (" +
                        CallInfoTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        CallInfoTable.COLUMN_PHONE_NUMBER + " TEXT," +
                        CallInfoTable.COLUMN_SCORE + " TEXT," +
                        CallInfoTable.COLUMN_SUMMARY + " TEXT," +
                        CallInfoTable.COLUMN_TIMESTAMP + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CallInfoTable.NAME);
        onCreate(sqLiteDatabase);
    }
}
