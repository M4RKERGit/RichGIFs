package com.example.richgifs.tools;

import lombok.Getter;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Configuration  //класс для хранения конфигурации нашего сервиса из config.properties
{
    private static final Logger logger = new Logger("[" + Configuration.class.getSimpleName() + "]");
    private static final Properties properties = new Properties();
    @Getter
    private static final String exchangeKey, gifKey;   //ключи для валют и gif
    @Getter
    private static final String faultGifURL, equalityGifURL;    //ссылки на гифки, выдаваемые при фейле/равенстве валют
    @Getter
    private static final String baseCurrency;   //базовая валюта, относительно которой смотрятся остальные курсы
    @Getter
    private static final int daysBefore;    //число, на сколько дней назад надо брать курс для сравнения с нынешним, берется по модулю

    static
    {
        try {properties.load(new FileReader("config.properties"));}
        catch (IOException e) {logger.createLog("Error reading properties file"); System.exit(0);}
        exchangeKey = properties.getProperty("exchangeKey");
        gifKey = properties.getProperty("gifsKey");
        faultGifURL = properties.getProperty("faultGifURL");
        equalityGifURL = properties.getProperty("equalityGifURL");
        baseCurrency = properties.getProperty("baseCurrency");
        daysBefore = Math.abs(Integer.parseInt(properties.getProperty("daysBefore")));
    }
}
