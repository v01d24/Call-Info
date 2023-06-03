package com.v01d24.callinfo.registry;

public class RegistryException extends Exception {
    public RegistryException(String message) {
        super(message);
    }

    public RegistryException(String message, Throwable cause) {
        super(message, cause);
    }
}
