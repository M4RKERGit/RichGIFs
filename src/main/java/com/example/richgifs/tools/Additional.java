package com.example.richgifs.tools;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Additional
{
    private static final Logger logger = new Logger("[" + Additional.class.getSimpleName().toUpperCase() + "]");

    public static String getCurrentTime()
    {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String getDate(int indent)
    {
        ZonedDateTime dateTime; //использую время по Гринвичу, т.к. в промежутке от 12 до 3 ночи сервис даёт сбой из-за даты "из будущего"
        if (indent < 0) {dateTime = ZonedDateTime.now(ZoneOffset.UTC).minusDays(Math.abs(indent));}
        else dateTime = ZonedDateTime.now(ZoneOffset.UTC);
        String toRet = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        logger.createLog(toRet);
        return toRet;
    }
}
