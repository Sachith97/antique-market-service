package com.sac.antiquemarketservice.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Sachith Harshamal
 * @created 2023-08-18
 */
public final class DateUtil {

    /**
     * @implNote create formatted String from LocalDateTime object
     * @param dateTime: accepting LocalDateTime object
     * @return formatted String
     */
    public static String getStringFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
