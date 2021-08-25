package com.example.richgifs;

import com.example.richgifs.API.APIResponse;
import com.example.richgifs.feign.ExchangeFeignClient;
import com.example.richgifs.feign.GifsFeignClient;
import com.example.richgifs.tools.Additional;
import com.example.richgifs.tools.Analyze;
import com.example.richgifs.tools.Configuration;
import com.example.richgifs.tools.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;

@RestController
public class RequestController
{
    private static final Logger logger = new Logger("[" + RequestController.class.getSimpleName() + "]");
    private final Configuration configuration = new Configuration();
    private static final ObjectMapper JSONMapper = new ObjectMapper();

    @Autowired
    ExchangeFeignClient exchangeFeignClient;

    @Autowired
    GifsFeignClient gifsFeignClient;

    public RequestController()
    {
        JSONMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @GetMapping("/")
    public ModelAndView index()
    {
        HashMap<String, String> model = new HashMap<>();
        logger.createLog("Visited index at " + Additional.getCurrentTime());
        return new ModelAndView("index.html", model);
    }

    @GetMapping("/feign/{currency}")
    public String getFeign(@PathVariable String currency)
    {
        var today = exchangeFeignClient.getStatistic(configuration.getExchangeKey(), Additional.getDate(0));
        var yesterday = exchangeFeignClient.getStatistic(configuration.getExchangeKey(), Additional.getDate(-1));
        var compared = (Analyze.compareExchanges(today, yesterday, currency));
        logger.createLog("Compared: " + compared);
        if (compared > 0) return Analyze.parseGif(gifsFeignClient.getGIF(configuration.getGifKey(), "rich"));
        if (compared == 0) return Analyze.parseGif(gifsFeignClient.getGIF(configuration.getGifKey(), "stabile"));
        else return Analyze.parseGif(gifsFeignClient.getGIF(configuration.getGifKey(), "broke"));
    }

    @GetMapping("/api/{currency}")
    public String getJSONResponse(@PathVariable String currency)
    {
        //return "{\"headerMsg\" : \"Курс RUB к доллару на 2021-08-24 20:38:51\", \"course\" : 73.8960, \"gifURL\" : \"https://giphy.com/gifs/the-office-michael-scott-graduation-Qa5dsjQjlCqOY\"}";
        float today = 0, yesterday = 0;
        String gifURL;
        String headerMsg = "Курс " + currency + " к USD на " + Additional.getCurrentTime();
        var todayString = exchangeFeignClient.getStatistic(configuration.getExchangeKey(), Additional.getDate(0));
        var yesterdayString = exchangeFeignClient.getStatistic(configuration.getExchangeKey(), Additional.getDate(-5));
        try
        {
            today = Float.parseFloat(JSONMapper.readValue(todayString, JsonNode.class).findValuesAsText(currency).get(0));
            yesterday = Float.parseFloat(JSONMapper.readValue(yesterdayString, JsonNode.class).findValuesAsText(currency).get(0));
        }
        catch (IOException e) {logger.createLog("Can't get exchange");}

        var compared = (Analyze.compareExchanges(todayString, yesterdayString, currency));
        if (compared > 0) gifURL = Analyze.parseGif(gifsFeignClient.getGIF(configuration.getGifKey(), "rich"));
        else if (compared == 0) gifURL = Analyze.parseGif(gifsFeignClient.getGIF(configuration.getGifKey(), "stabile"));
        else gifURL = Analyze.parseGif(gifsFeignClient.getGIF(configuration.getGifKey(), "broke"));

        APIResponse response = new APIResponse(headerMsg, today, yesterday, gifURL);
        try {return JSONMapper.writeValueAsString(response);}
        catch (JsonProcessingException e) {logger.createLog("JSONMapper error"); return "Request error";}
    }
}