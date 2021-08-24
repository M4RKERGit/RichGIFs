package com.example.richgifs.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Additional
{
    private static final Logger logger = new Logger("[" + Additional.class.getSimpleName() + "]");

    public static String getCurrentTime()
    {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String getDate(int indent)
    {
        LocalDateTime dateTime;
        if (indent < 0) {dateTime = LocalDateTime.now().minusDays(Math.abs(indent));}
        else dateTime = LocalDateTime.now();
        String toRet = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        logger.createLog(toRet);
        return toRet;
    }
}
