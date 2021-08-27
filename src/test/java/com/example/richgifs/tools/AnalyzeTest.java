package com.example.richgifs.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

class AnalyzeTest   //unit-тесты функций анализатора с семплами, проходят
{
    String inputGif = Files.readString(Path.of("samples/gifSample.txt"));
    String inputExT = Files.readString(Path.of("samples/todaySample.txt"));
    String inputExY = Files.readString(Path.of("samples/yesterdaySample.txt"));
    
    AnalyzeTest() throws IOException {}

    @Test
    void currencySample() throws JsonProcessingException
    {
        ObjectMapper JSONMapper = new ObjectMapper();
        float today = Float.parseFloat(JSONMapper.readValue(inputExT, JsonNode.class).findValuesAsText("CZK").get(0));
        float yesterday = Float.parseFloat(JSONMapper.readValue(inputExY, JsonNode.class).findValuesAsText("CZK").get(0));
        Assert.isTrue(1 == Analyze.compareExchanges(today, yesterday, "RUB"));
    }

    @Test
    void parseGifNonEmpty()
    {
        String result = Analyze.parseGif(inputGif);
        Assert.notEmpty(Collections.singleton(result));
    }
    @Test
    void parseGifContainsURL()
    {
        String result = Analyze.parseGif(inputGif);
        Assert.isTrue(result.contains("https://giphy.com/gifs/"));
    }
    @Test
    void parseGifCorrectResponse() throws IOException
    {
        String result = Analyze.parseGif(inputGif);
        URL url = new URL(result);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("HEAD");
        Assert.isTrue(huc.getResponseCode() == 200);
    }
}