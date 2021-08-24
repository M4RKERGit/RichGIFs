package com.example.richgifs;

import com.example.richgifs.feign.ExchangeFeignClient;
import com.example.richgifs.feign.GifsFeignClient;
import com.example.richgifs.tools.Additional;
import com.example.richgifs.tools.Comparator;
import com.example.richgifs.tools.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
public class RequestController
{
    private static final Logger logger = new Logger("[" + RequestController.class.getSimpleName() + "]");
    private static final Comparator comparator = new Comparator();

    @Autowired
    ExchangeFeignClient exchangeFeignClient;

    @Autowired
    GifsFeignClient gifsFeignClient;

    public RequestController()
    {

    }

    @GetMapping("/")
    public ModelAndView index()
    {
        HashMap<String, String> model = new HashMap<>();
        logger.createLog("Visited index at %s" + Additional.getCurrentTime());
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
}
