package com.example.richgifs;

import com.example.richgifs.api.ResponseMaker;
import com.example.richgifs.feign.ExchangeFeignClient;
import com.example.richgifs.feign.GifsFeignClient;
import com.example.richgifs.tools.Additional;
import com.example.richgifs.tools.Configuration;
import com.example.richgifs.tools.Logger;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@RestController
@Getter
public class RequestController  //класс главного REST-контроллера
{
    private final Logger logger = new Logger("[" + RequestController.class.getSimpleName().toUpperCase() + "]");
    //используем своеобразный "кеш", чтобы API отвечал быстрее
    //первый рефреш будет в @PostConstruct (экономим запросы нашего бесплатного плана)
    //особенно эффективно с гифками, так как там надо парсить просто огромный JSON
    @Setter //сеттер для тестов, для постановки семплов
    private String todayString = "", yesterdayString = "", allCurrencies = "", richGifs = "", brokeGifs = "";

    @Autowired
    ExchangeFeignClient exchangeFeignClient;    //клиенты Feign для обращения к внешним API

    @Autowired
    GifsFeignClient gifsFeignClient;

    @PostConstruct
    private void firstRefresh()
    {
        refreshCache();
    }

    @GetMapping("/")
    public ModelAndView index() //отображение веб-интерфейса API
    {
        logger.createLog("Visited index at " + Additional.getCurrentLocalTime());
        return new ModelAndView("index.html", new HashMap<>());
    }

    @GetMapping(value = "/all", produces = "application/json")  //получение списка валют, можно и JSом, но так получится монолитнее
    public String allCurrencies() {return allCurrencies;}

    @GetMapping("/gif/{currency}")
    public String onlyGIF(@PathVariable String currency)   //получение только ссылки на гифку
    {
        return "<iframe id=\"resultGif\" src=\"" + ResponseMaker.throwGif(todayString, yesterdayString, currency,
                brokeGifs, richGifs) + "\" width=\"720\" height=\"420\" frameBorder=\"0\"></iframe>";
    }

    @GetMapping(value = "/api/{currency}", produces = "application/json")
    public String getJSONResponse(@PathVariable String currency)    //метод для работы с веб-интерфейсом
    {
        return ResponseMaker.createJSON(todayString, yesterdayString, currency, brokeGifs, richGifs);
    }

    @Scheduled(fixedRate = 3600000) //запрос к внешнему API за курсами, бесплатный план обновляется раз в час
    public void refreshCache()
    {
        logger.createLog("Refreshing...");
        int indent = Configuration.getDaysBefore();
        todayString = exchangeFeignClient.getStatistic(Configuration.getExchangeKey(), Additional.getGreenwichDate(0));
        yesterdayString = exchangeFeignClient.getStatistic(Configuration.getExchangeKey(), Additional.getGreenwichDate(indent));
        allCurrencies = exchangeFeignClient.getAllCurrencies();
        richGifs = gifsFeignClient.getGIF(Configuration.getGifKey(), "rich");
        brokeGifs = gifsFeignClient.getGIF(Configuration.getGifKey(), "broke");
    }
}