package com.example.richgifs;

import com.example.richgifs.getters.ExchangeGetter;
import com.example.richgifs.getters.GifGetter;
import com.example.richgifs.tools.Additional;
import com.example.richgifs.tools.Comparator;
import com.example.richgifs.tools.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
public class RequestController
{
    private static final Logger logger = new Logger("[" + RequestController.class.getSimpleName() + "]");
    private static final Comparator comparator = new Comparator();

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

    @GetMapping("/getgif")
    public String getGif()
    {
        return comparator.gifGetter.getRandomGifUrl("rich", 25);
    }

    @GetMapping("/getex")
    public String getEx()
    {
        return comparator.exchangeGetter.getExchange();
    }

    @GetMapping("/task")
    public String doTask(){return comparator.compareAndRespond();}
}
