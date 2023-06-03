package com.v01d24.callinfo.registry;

import androidx.annotation.NonNull;

public class PhoneNumberRegistryFactory {
    public static @NonNull PhoneNumberRegistry createDefault() {
        return new CFPhoneNumberRegistry();
    }
}
