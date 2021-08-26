package com.example.richgifs.tools;

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
    AnalyzeTest() throws IOException {}
    @Test
    void compareExchangesTest1()
    {
        String[] first = new String[]{"{\"rates\": {\"AED\": 3.6732}}", "{\"rates\": {\"AED\": 3.9967}}"};
        int f = Analyze.compareExchanges(first[0], first[1], "AED");
        Assert.isTrue(-1 == f);
    }
    @Test
    void compareExchangesTest2()
    {
        String[] second = new String[]{"{\"rates\": {\"AED\": 5.5555}}", "{\"rates\": {\"AED\": 5.5555}}"};
        int s = Analyze.compareExchanges(second[0], second[1], "AED");
        Assert.isTrue(0 == s);
    }
    @Test
    void compareExchangesTest3()
    {
        String[] third = new String[]{"{\"rates\": {\"AED\": 14.6783345}}", "{\"rates\": {\"AED\": 12.90991177}}"};
        int t = Analyze.compareExchanges(third[0], third[1], "AED");
        Assert.isTrue(1 == t);
    }

    String input = Files.readString(Path.of("samples/gifSample.txt"));
    @Test
    void parseGifNonEmpty()
    {
        String result = Analyze.parseGif(input);
        Assert.notEmpty(Collections.singleton(result));
    }
    @Test
    void parseGifContainsURL()
    {
        String result = Analyze.parseGif(input);
        Assert.isTrue(result.contains("https://giphy.com/gifs/"));
    }
    @Test
    void parseGifCorrectResponse() throws IOException
    {
        String result = Analyze.parseGif(input);
        URL url = new URL(result);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("HEAD");
        Assert.isTrue(huc.getResponseCode() == 200);
    }
}