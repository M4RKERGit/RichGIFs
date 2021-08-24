package com.example.richgifs.getters;

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
        String toRet = null;
        String output = connectAndGet(searchURL);
        try {return JSONMapper.readValue(output, JsonNode.class).findValuesAsText("RUB").get(0);}
        catch (IOException e)
        {e.printStackTrace();}
        return "";
    }
}
