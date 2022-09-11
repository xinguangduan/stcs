package org.stcs.server.utils;


import java.util.UUID;

public final class KeyUtils {

    private KeyUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateMessageId() {
        return UUID.randomUUID().toString();
    }

    public static String generateUID() {
        return UUID.randomUUID().toString();
    }
}
