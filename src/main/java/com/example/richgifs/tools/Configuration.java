package com.example.richgifs.tools;

import lombok.Getter;
import lombok.Setter;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Getter
@Setter
public class Configuration  //класс для хранения конфигурации нашего сервиса из config.properties
{
    private final Logger logger = new Logger("[" + getClass().getSimpleName() + "]");
    private static Properties properties = new Properties();
    private final String exchangeKey, gifKey;   //ключи для валют и gif
    private final String faultGifURL;
    private final String equalityGifURL;
    private final String baseCurrency;

    public Configuration()
    {
        try {properties.load(new FileReader("config.properties"));}
        catch (IOException e) {logger.createLog("Error reading properties file"); System.exit(0);}
        this.exchangeKey = properties.getProperty("exchangeKey");
        this.gifKey = properties.getProperty("gifsKey");
        this.faultGifURL = properties.getProperty("faultGifURL");
        this.equalityGifURL = properties.getProperty("equalityGifURL");
        this.baseCurrency = properties.getProperty("baseCurrency");
    }
}
