package com.v01d24.callinfo.registry;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.v01d24.callinfo.PhoneNumberScore;

public class PhoneNumberInfo {
    public final PhoneNumberScore score;
    public final String summary;
    public final Uri webPageUri;

    public PhoneNumberInfo(@NonNull PhoneNumberScore score,
                           @NonNull String summary,
                           @NonNull Uri webPageUri) {
        this.score = score;
        this.summary = summary;
        this.webPageUri = webPageUri;
    }
}
