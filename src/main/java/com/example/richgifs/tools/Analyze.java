package com.example.richgifs.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Random;

public class Analyze    //набор утилит для анализа ответов от API
{
    private static final Logger logger = new Logger("[" + Analyze.class.getSimpleName().toUpperCase() + "]");
    private static final ObjectMapper JSONMapper = new ObjectMapper();

    public static String parseGif(String output)    //метод выбора рандомной гифки, возвращает ссылку на неё
    {
        try
        {
            Random random = new Random();
            JsonNode parsed = JSONMapper.readValue(output, JsonNode.class);
            int bound = parsed.get("data").size();
            return parsed.get("data").get((random.nextInt(bound))).get("url").asText();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            logger.createLog("Error parsing GIF string");
            return Configuration.getFaultGifURL();  //кидаем дефолтную гифку с Йодой
        }
    }

    public static int compareExchanges(float today, float yesterday, String currency)
    //метод сравнения двух курсов валюты, возвращает -1/0/1 в случае, если первая меньше/равны/вторая меньше
    {
        logger.createLog("Today: " + today + " | Yesterday: " + yesterday + " | Currency: " + currency);
        return Float.compare(today, yesterday);
    }
}