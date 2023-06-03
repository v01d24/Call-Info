package com.v01d24.callinfo.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.v01d24.callinfo.PhoneNumberScore;

public class CallInfo {
    private long id;
    private final String phoneNumber;
    private PhoneNumberScore score;
    private String summary;
    private final long timestamp;

    public CallInfo(@NonNull String phoneNumber, long timestamp) {
        this.id = 0;
        this.phoneNumber = phoneNumber;
        this.timestamp = timestamp;
    }

    public CallInfo(long id,
                    @NonNull String phoneNumber,
                    @Nullable PhoneNumberScore score,
                    @Nullable String summary,
                    long timestamp) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.score = score;
        this.summary = summary;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NonNull String getPhoneNumber() {
        return phoneNumber;
    }

    public @Nullable PhoneNumberScore getScore() {
        return score;
    }

    public void setScore(@Nullable PhoneNumberScore score) {
        this.score = score;
    }

    public @Nullable String getSummary() {
        return summary;
    }

    public void setSummary(@Nullable String summary) {
        this.summary = summary;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
