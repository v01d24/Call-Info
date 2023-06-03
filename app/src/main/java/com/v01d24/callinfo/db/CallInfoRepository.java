package com.v01d24.callinfo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.v01d24.callinfo.PhoneNumberScore;

import java.util.ArrayList;
import java.util.List;

public class CallInfoRepository {

    private final SQLiteDatabase db;

    public CallInfoRepository(@NonNull SQLiteDatabase db) {
        this.db = db;
    }

    public void save(@NonNull CallInfo call) {
        ContentValues values = new ContentValues();
        values.put(CallInfoTable.COLUMN_PHONE_NUMBER, call.getPhoneNumber());
        values.put(CallInfoTable.COLUMN_SCORE, serializeScore(call.getScore()));
        values.put(CallInfoTable.COLUMN_SUMMARY, call.getSummary());
        values.put(CallInfoTable.COLUMN_TIMESTAMP, call.getTimestamp());

        long callId = call.getId();
        if (callId == 0) {
            long id = db.insert(CallInfoTable.NAME, null, values);
            call.setId(id);
        }
        else {
            String selection = CallInfoTable.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(callId) };
            db.update(
                    CallInfoTable.NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }

    public @NonNull List<CallInfo> getRecent(int limit) {
        String[] projection = {
                CallInfoTable.COLUMN_ID,
                CallInfoTable.COLUMN_PHONE_NUMBER,
                CallInfoTable.COLUMN_SCORE,
                CallInfoTable.COLUMN_SUMMARY,
                CallInfoTable.COLUMN_TIMESTAMP
        };
        String sortOrder = CallInfoTable.COLUMN_ID + " DESC";
        Cursor cursor = db.query(
                CallInfoTable.NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder,
                String.valueOf(limit)
        );
        int idIndex = cursor.getColumnIndexOrThrow(CallInfoTable.COLUMN_ID);
        int phoneNumberIndex = cursor.getColumnIndexOrThrow(CallInfoTable.COLUMN_PHONE_NUMBER);
        int scoreIndex = cursor.getColumnIndexOrThrow(CallInfoTable.COLUMN_SCORE);
        int summaryIndex = cursor.getColumnIndexOrThrow(CallInfoTable.COLUMN_SUMMARY);
        int timestampIndex = cursor.getColumnIndexOrThrow(CallInfoTable.COLUMN_TIMESTAMP);
        ArrayList<CallInfo> calls = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(idIndex);
            String phoneNumber = cursor.getString(phoneNumberIndex);
            PhoneNumberScore score = parseScore(cursor.getString(scoreIndex));
            String summary = cursor.getString(summaryIndex);
            long timestamp = cursor.getLong(timestampIndex);
            CallInfo callInfo = new CallInfo(id, phoneNumber, score, summary, timestamp);
            calls.add(callInfo);
        }
        cursor.close();
        return calls;
    }

    public void deleteOld(long olderThanTimestamp) {
        String selection = CallInfoTable.COLUMN_TIMESTAMP + " < ?";
        String[] selectionArgs = { String.valueOf(olderThanTimestamp) };
        db.delete(CallInfoTable.NAME, selection, selectionArgs);
    }

    private @Nullable String serializeScore(@Nullable PhoneNumberScore score) {
        if (score == null) {
            return null;
        }
        return score.toString();
    }

    private @Nullable PhoneNumberScore parseScore(@Nullable String score) {
        if (score == null) {
            return null;
        }
        return PhoneNumberScore.valueOf(score);
    }
}
