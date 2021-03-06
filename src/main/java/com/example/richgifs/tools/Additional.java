package com.example.richgifs.tools;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Additional //класс с набором микро-утилит, вспомогательные инструменты
{
    private static final Logger logger = new Logger("[" + Additional.class.getSimpleName().toUpperCase() + "]");

    public static String getCurrentLocalTime()  //получение текущего времени для логгирования
    {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String getGreenwichDate(int indent)   //получение даты по Гринвичу для запроса к валютам
    {
        ZonedDateTime dateTime; //использую время по Гринвичу, т.к. в промежутке от 12 до 3 ночи сервис даёт сбой из-за даты "из будущего"
        if (indent != 0) {dateTime = ZonedDateTime.now(ZoneOffset.UTC).minusDays(Math.abs(indent));} //определяем, нужна дата текущая или вчерашняя
        else dateTime = ZonedDateTime.now(ZoneOffset.UTC);
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String makeEmbed(String url)  //превращаем ссылку на гифку во "вставляемую" на сайт
    {
        String[] splitted;
        if (url.contains("-")) splitted = url.split("-");
        else splitted = url.split("/");
        return "https://giphy.com/embed/" + splitted[splitted.length - 1];
    }

    public static String constructHeader(int indent)    //составление заголовка с учетом падежа
    {
        if (indent == 1) return "Вчера курс составлял:";
        if (indent == 2) return "Позавчера курс составял:";
        String base = " назад курс составлял:";
        if (indent > 10 && indent < 20) return indent + " дней" + base;
        return switch (indent % 10)
        {
            case (1) -> indent + " день" + base;
            case (2), (3), (4) -> indent + " дня" + base;
            default -> indent + " дней" + base;
        };
    }
}
