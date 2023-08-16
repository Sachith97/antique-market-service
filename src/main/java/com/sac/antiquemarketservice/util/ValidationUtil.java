package com.sac.antiquemarketservice.util;

/**
 * @author Sachith Harshamal
 * @created 2023-08-16
 */
public final class ValidationUtil {

    /**
     * @implNote validate whether the specified String is not null, and has content that is not whitespace
     * @param str: accepting String
     * @return content availability
     */
    public static boolean stringHasContent(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
