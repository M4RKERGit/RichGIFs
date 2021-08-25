package com.example.richgifs.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Random;

public class Analyze
{
    private static final Logger logger = new Logger("[" + Analyze.class.getSimpleName() + "]");
    private static final ObjectMapper JSONMapper = new ObjectMapper();

    public static String parseGif(String output)
    {
        try
        {
            Random random = new Random();
            JsonNode parsed = JSONMapper.readValue(output, JsonNode.class);
            int index = parsed.get("data").size();
            return parsed.get("data").get((random.nextInt(index))).get("url").asText();
        }
        catch (IOException e) {e.printStackTrace();}
        return "";
    }

    public static int compareExchanges(String today, String yesterday, String currency)
    {
        float valueToday = 0f, valueYesterday = 0f;

        try {valueToday = Float.parseFloat(JSONMapper.readValue(today, JsonNode.class).findValuesAsText(currency).get(0));}
        catch (IOException e) {logger.createLog("Error getting today exchange");}

        try {valueYesterday = Float.parseFloat(JSONMapper.readValue(yesterday, JsonNode.class).findValuesAsText(currency).get(0));}
        catch (IOException e) {logger.createLog("Error getting yesterday exchange");}

        logger.createLog("Today:" + valueToday + "\tYesterday: " + valueYesterday);
        return Float.compare(valueToday, valueYesterday);
    }
}
