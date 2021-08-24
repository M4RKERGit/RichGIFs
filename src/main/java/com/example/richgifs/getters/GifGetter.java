package com.example.richgifs.getters;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Random;

public class GifGetter extends Getter
{
    public GifGetter(String key)
    {
        this.apiKey = key;
        JSONMapper = new ObjectMapper();
        JSONMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String getRandomGifUrl(String searchRequest, int limit)
    {
        String searchURL = "https://api.giphy.com/v1/gifs/search?api_key=" + apiKey + "&q=" + searchRequest
                +"&limit=" + limit + "&offset=0&rating=g&lang=en";
        String output = connectAndGet(searchURL);
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
