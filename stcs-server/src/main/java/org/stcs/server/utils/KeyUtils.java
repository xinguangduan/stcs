package org.stcs.server.utils;


import java.util.UUID;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class KeyUtils {

    public static String generateMessageId() {
        return UUID.randomUUID().toString();
    }

    public static String generateUID() {
        return UUID.randomUUID().toString();
    }
}
