package com.polimi.childcare.client.ui.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils
{
    private static DateTimeFormatter shotDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String dateToStringWithPattern(String pattern, Date date)
    {
        return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
    }

    public static String dateToShortString(Date date)
    {
        return shotDateFormat.format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
    }
}
