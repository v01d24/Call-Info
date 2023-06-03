package com.v01d24.callinfo;

import androidx.annotation.NonNull;

public class PhoneNumberParser {
    public static @NonNull String parse(@NonNull String rawPhoneNumber) {
        String phoneNumber = rawPhoneNumber.replaceAll("[^\\d.+]", "");
        if (phoneNumber.length() == 11 && phoneNumber.startsWith("8")) {
            phoneNumber = "+7" + phoneNumber.substring(1);
        }
        else if (phoneNumber.length() == 10 && phoneNumber.startsWith("9")) {
            phoneNumber = "+7" + phoneNumber;
        }
        return phoneNumber;
    }
}
