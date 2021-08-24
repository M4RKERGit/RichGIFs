package com.example.richgifs;

import com.example.richgifs.getters.ExchangeGetter;
import com.example.richgifs.getters.GifGetter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
public class RequestController
{
    private static final Logger logger = new Logger("[CTRL]");
    private final GifGetter gifGetter;
    private final ExchangeGetter exchangeGetter;

    public RequestController()
    {
        exchangeGetter = new ExchangeGetter("cd70bc2edaa64dfea4eba9b2c89b168f");
        gifGetter = new GifGetter("BB0iyFH5ZFJt6lkaJ2avFdxQDNx0WvzA");
    }

    @GetMapping("/")
    public ModelAndView index()
    {
        HashMap<String, String> model = new HashMap<>();
        logger.createLog("Visited index at %s" + Additional.getCurrentTime());
        return new ModelAndView("index.html", model);
    }

    @GetMapping("/getgif")
    public String getGif()
    {
        return this.gifGetter.getRandomGifUrl("rich", 25);
    }

    @GetMapping("/getex")
    public String getEx()
    {
        return this.exchangeGetter.getExchange();
    }
}
