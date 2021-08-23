package com.example.richgifs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Additional
{
    public static String getCurrentTime()
    {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
