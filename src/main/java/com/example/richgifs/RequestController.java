package com.example.richgifs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
public class RequestController
{
    private static final Logger logger = new Logger("[CTRL]");
    private final GifGetter gifGetter;

    public RequestController()
    {
        gifGetter = new GifGetter("BB0iyFH5ZFJt6lkaJ2avFdxQDNx0WvzA");
    }

    @GetMapping("/")
    public ModelAndView index()
    {
        HashMap<String, String> model = new HashMap<>();
        logger.createLog("Visited index at %s" + Additional.getCurrentTime());
        return new ModelAndView(getGif(), model);
    }

    @GetMapping("/getGif")
    public String getGif()
    {
        return this.gifGetter.getRandomGifUrl("rich", 5);
    }
}
