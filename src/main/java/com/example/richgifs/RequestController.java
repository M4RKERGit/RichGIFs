package com.example.richgifs;

import com.example.richgifs.API.APIResponse;
import com.example.richgifs.feign.ExchangeFeignClient;
import com.example.richgifs.feign.GifsFeignClient;
import com.example.richgifs.tools.Additional;
import com.example.richgifs.tools.Comparator;
import com.example.richgifs.tools.Logger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    private static final Comparator comparator = new Comparator();
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
        var today = exchangeFeignClient.getStatistic(currency,
                comparator.getExchangeKey(), Additional.getDate(0));
        var yesterday = exchangeFeignClient.getStatistic(currency,
                comparator.getExchangeKey(), Additional.getDate(-1));
        if (comparator.compareExchanges(today, yesterday, currency)) return comparator.findGif(gifsFeignClient.getGIF(comparator.getGifKey(), "rich"));
        else return comparator.findGif(gifsFeignClient.getGIF(comparator.getGifKey(), "broke"));
    }

    @GetMapping("/api/{currency}")
    public String getJSONResponse(@PathVariable String currency)
    {
        return "{\"headerMsg\" : \"Курс USD к доллару на 2021-08-24 20:38:51\", \"firstCourse\" : 1.0, \"secondCourse\" : 1.0, \"gifURL\" : \"https://giphy.com/gifs/the-office-michael-scott-graduation-Qa5dsjQjlCqOY\"}";
        /*float today = 0, yesterday = 0;
        String headerMsg = "Курс " + currency + " к доллару на " + Additional.getCurrentTime();
        var todaystring = exchangeFeignClient.getStatistic(currency, comparator.getExchangeKey(), Additional.getDate(0));

        try {today = Float.parseFloat(JSONMapper.readValue(todaystring, JsonNode.class).findValuesAsText(currency).get(0));}
        catch (IOException e) {logger.createLog("Can't get exchange");}

        APIResponse response = new APIResponse(headerMsg, today, yesterday, getFeign(currency));
        try {return JSONMapper.writeValueAsString(response);}
        catch (JsonProcessingException e) {logger.createLog("JSONMapper error"); return "Request error";}*/
    }
}


