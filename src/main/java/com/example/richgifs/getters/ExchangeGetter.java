package com.example.richgifs.getters;

import com.example.richgifs.tools.Additional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ExchangeGetter extends Getter
{
    public ExchangeGetter(String key)
    {
        this.apiKey = key;
        this.JSONMapper = new ObjectMapper();
    }

    public String getExchange()
    {
        String searchURL = "https://openexchangerates.org/api/latest.json?app_id=" + this.apiKey;
        String output = connectAndGet(searchURL);
        try {return JSONMapper.readValue(output, JsonNode.class).findValuesAsText("RUB").get(0);}
        catch (IOException e) {logger.createLog("Error getting today exchange");}
        return "";
    }

    public String getYesterdayExchange()
    {
        String searchURL = "https://openexchangerates.org/api/historical/" + Additional.getYesterdayDate() + ".json?app_id=" + this.apiKey;
        String output = connectAndGet(searchURL);
        try {return JSONMapper.readValue(output, JsonNode.class).findValuesAsText("RUB").get(0);}
        catch (IOException e) {logger.createLog("Error getting yesterday exchange");}
        return "";
    }
}
