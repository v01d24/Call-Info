package com.v01d24.callinfo.registry;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.net.MalformedURLException;

public interface PhoneNumberRegistry {
    @NonNull
    Uri getWebPageUri(@NonNull String phoneNumber);
    @NonNull
    PhoneNumberInfo lookup(@NonNull String phoneNumber) throws RegistryException;
}
