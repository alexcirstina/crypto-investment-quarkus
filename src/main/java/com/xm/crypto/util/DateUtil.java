package com.xm.crypto.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class DateUtil {

    private DateUtil(){}

    public static LocalDateTime stringToLocalDateTime(String timestamp){
        return LocalDateTime.ofInstant(Instant
                .ofEpochMilli(Long.parseLong(timestamp)), ZoneId.systemDefault());
    }


}
