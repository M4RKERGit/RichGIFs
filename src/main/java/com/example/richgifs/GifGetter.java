package com.example.richgifs;

import com.example.richgifs.pesponse.Response;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

public class GifGetter
{
    private final String apiKey;
    private final Logger logger = new Logger("[GETTER]");
    private final ObjectMapper JSONMapper;

    public GifGetter(String key)
    {
        this.apiKey = key;
        JSONMapper = new ObjectMapper();
        JSONMapper.enable(SerializationFeature.INDENT_OUTPUT);
        JSONMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String getRandomGifUrl(String searchRequest, int limit)
    {
        String searchURL = "https://api.giphy.com/v1/gifs/search?api_key=" + apiKey + "&q=" + searchRequest
                +"&limit=" + limit + "&offset=0&rating=g&lang=en";
        String output;
        Response toRet = null;
        try
        {
            HttpURLConnection conTar = (HttpURLConnection) new URL(searchURL).openConnection();
            conTar.connect();
            Scanner rec = new Scanner(conTar.getInputStream());
            output = rec.nextLine();
            conTar.disconnect();
        }
        catch (Exception e)
        {
            logger.createLog("Failed to connect");
            return "";
        }

        try {toRet = JSONMapper.readValue(output, Response.class);}
        catch (IOException e) {e.printStackTrace(); logger.createLog("Wrapping failed");}
        if (toRet != null)
        {
            Random random = new Random();
            return toRet.getData()[random.nextInt(toRet.getData().length)].getUrl();
        }
        return "";
    }
}
