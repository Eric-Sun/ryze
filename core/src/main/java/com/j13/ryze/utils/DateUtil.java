package com.j13.ryze.utils;

import java.text.SimpleDateFormat;

public class DateUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String format(long time) {
        return sdf.format(time);
    }
}
