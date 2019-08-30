package com.j13.ryze.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String format(long time) {
        return sdf.format(time);
    }

    public static String getFormatDate(Date date, String timeFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        return sdf.format(date);
    }

    public static Date addDay(Date date, int addDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, addDay);
        return c.getTime();
    }
}
