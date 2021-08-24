package com.example.richgifs.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

@Getter @Setter
public class Comparator
{
    private final static Logger logger = new Logger("[" + Comparator.class.getSimpleName() + "]");
    private Properties properties = new Properties();
    private final String exchangeKey, gifKey;

    public Comparator()
    {
        try {properties.load(new FileReader("config.properties"));}
        catch (IOException e) {logger.createLog("Error reading properties"); System.exit(0);}
        exchangeKey = properties.getProperty("exchangeKey");
        gifKey = properties.getProperty("gifsKey");
    }

    public boolean compareExchanges(String today, String yesterday, String currency)
    {
        ObjectMapper JSONMapper = new ObjectMapper();
        float valueToday = 0, valueYesterday = 0;

        try {valueToday = Float.parseFloat(JSONMapper.readValue(today, JsonNode.class).findValuesAsText(currency).get(0));}
        catch (IOException e) {logger.createLog("Error getting today exchange");}

        try {valueYesterday = Float.parseFloat(JSONMapper.readValue(yesterday, JsonNode.class).findValuesAsText(currency).get(0));}
        catch (IOException e) {logger.createLog("Error getting yesterday exchange");}

        return valueToday > valueYesterday;
    }

    public String findGif(String output)
    {
        ObjectMapper JSONMapper = new ObjectMapper();
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
}
