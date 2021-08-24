package com.example.richgifs.tools;

import com.example.richgifs.getters.ExchangeGetter;
import com.example.richgifs.getters.GifGetter;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Comparator
{
    private final Logger logger = new Logger("[" + getClass().getSimpleName() + "]");
    public final GifGetter gifGetter;
    public final ExchangeGetter exchangeGetter;
    private Properties properties = new Properties();

    public Comparator()
    {
        try
        {
            properties.load(new FileReader("config.properties"));
        }
        catch (IOException e) {logger.createLog("Error reading properties"); System.exit(0);}
        exchangeGetter = new ExchangeGetter(properties.getProperty("exchangeKey"));
        gifGetter = new GifGetter(properties.getProperty("gifsKey"));
    }

    public String compareAndRespond()
    {
        float today = Float.parseFloat(exchangeGetter.getExchange());
        float yesterday = Float.parseFloat(exchangeGetter.getYesterdayExchange());
        logger.createLog(today + "/" + yesterday);
        if (today > yesterday) return gifGetter.getRandomGifUrl("rich", 25);
        else return gifGetter.getRandomGifUrl("broke", 25);
    }
}
