package com.example.richgifs.tools;

import lombok.Getter;
import lombok.Setter;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Getter
@Setter
public class Configuration
{
    private final Logger logger = new Logger("[" + getClass().getSimpleName() + "]");
    private static Properties properties = new Properties();
    private final String exchangeKey, gifKey;

    public Configuration()
    {
        try {properties.load(new FileReader("config.properties"));}
        catch (IOException e) {logger.createLog("Error reading properties"); System.exit(0);}
        this.exchangeKey = properties.getProperty("exchangeKey");
        this.gifKey = properties.getProperty("gifsKey");
    }
}
