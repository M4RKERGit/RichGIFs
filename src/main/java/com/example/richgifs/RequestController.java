package com.example.richgifs;

import com.example.richgifs.API.APIResponse;
import com.example.richgifs.feign.ExchangeFeignClient;
import com.example.richgifs.feign.GifsFeignClient;
import com.example.richgifs.tools.Additional;
import com.example.richgifs.tools.Analyze;
import com.example.richgifs.tools.Configuration;
import com.example.richgifs.tools.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
@Getter
public class RequestController  //класс главного REST-контроллера
{
    private final Logger logger = new Logger("[" + RequestController.class.getSimpleName().toUpperCase() + "]");
    private final Configuration configuration = new Configuration();
    private final ObjectMapper JSONMapper = new ObjectMapper();
    //используем своеобразный "кеш", чтобы API отвечал быстрее
    //первый рефреш будет при входе в любой endpoint нашего API (экономим запросы нашего бесплатного плана)
    //особенно эффективно с гифками, так как там надо парсить просто огромный JSON
    @Setter
    private String todayString = "", yesterdayString = "", allCurrencies = "", richGifs = "", brokeGifs = "";

    @Autowired
    ExchangeFeignClient exchangeFeignClient;    //клиенты Feign для обращения к внешним API

    @Autowired
    GifsFeignClient gifsFeignClient;

    public RequestController() {JSONMapper.enable(SerializationFeature.INDENT_OUTPUT);}  //хуманизируем ответ отступами

    @GetMapping("/")
    public ModelAndView index() //отображение веб-интерфейса API
    {
        if (todayString.isEmpty() || yesterdayString.isEmpty()) refreshCourses();
        HashMap<String, String> model = new HashMap<>();    //модель не используется
        logger.createLog("Visited index at " + Additional.getCurrentLocalTime());
        return new ModelAndView("index.html", model);
    }

    @GetMapping(value = "/all", produces = "application/json")  //получение списка валют, можно и JSом, но так получится монолитнее
    public String allCurrencies()
    {
        if (todayString.isEmpty() || yesterdayString.isEmpty()) refreshCourses();
        return allCurrencies;
    }

    @GetMapping("/gif/{currency}")
    public String onlyGIF(@PathVariable String currency)   //получение только ссылки на гифку
    {
        if (todayString.isEmpty() || yesterdayString.isEmpty()) refreshCourses();
        float today = 0, yesterday = 0;
        String toRet;
        String base = configuration.getBaseCurrency();
        try
        {
            //распарсили курсы во float
            today = 1/(Float.parseFloat(JSONMapper.readValue(todayString, JsonNode.class).findValuesAsText(currency).get(0))
                    /Float.parseFloat(JSONMapper.readValue(todayString, JsonNode.class).findValuesAsText(base).get(0)));
            yesterday = 1/(Float.parseFloat(JSONMapper.readValue(yesterdayString, JsonNode.class).findValuesAsText(currency).get(0))
                    /Float.parseFloat(JSONMapper.readValue(yesterdayString, JsonNode.class).findValuesAsText(base).get(0)));
        }
        catch (Exception e)   //если такая валюта не найдена, кидает дефолтный ответ с гифкой Йоды
        {
            e.printStackTrace();
            logger.createLog("Can't get exchange");
            return "<iframe id=\"resultGif\" src=\"" + configuration.getFaultGifURL() + "\" width=\"720\" height=\"420\" frameBorder=\"0\"></iframe>";
        }
        var compared = (Analyze.compareExchanges(today, yesterday, currency));
        logger.createLog("Compared: " + compared);
        if (compared > 0) toRet = Analyze.parseGif(gifsFeignClient.getGIF(configuration.getGifKey(), "rich"));
        else if (compared == 0) toRet = configuration.getEqualityGifURL();
        else toRet = Analyze.parseGif(gifsFeignClient.getGIF(configuration.getGifKey(), "broke"));
        return "<iframe id=\"resultGif\" src=\"" + Additional.makeEmbed(toRet) + "\" width=\"720\" height=\"420\" frameBorder=\"0\"></iframe>";
    }

    @GetMapping(value = "/api/{currency}", produces = "application/json")
    public String getJSONResponse(@PathVariable String currency)    //метод для работы с веб-интерфейсом
    {
        //return "{\"headerMsg\" : \"Курс RUB к USD на 2021-08-25 05:25:18\", \"course\" : 73.8542, \"courseYesterday\" : 74.2789, \"gifURL\" : \"https://giphy.com/gifs/insatiable-netflix-angie-1ppudqsvJAWPa63iLU\"}";
        if (todayString.isEmpty() || yesterdayString.isEmpty()) refreshCourses();
        float today = 0, yesterday = 0;
        String gifURL;
        String base = configuration.getBaseCurrency();
        String headerMsg = "Курс " + currency + " к " + base + " на " + Additional.getCurrentLocalTime();

        try
        {
            //распарсили курсы во float
            today = 1/(Float.parseFloat(JSONMapper.readValue(todayString, JsonNode.class).findValuesAsText(currency).get(0))
                    /Float.parseFloat(JSONMapper.readValue(todayString, JsonNode.class).findValuesAsText(base).get(0)));
            yesterday = 1/(Float.parseFloat(JSONMapper.readValue(yesterdayString, JsonNode.class).findValuesAsText(currency).get(0))
                    /Float.parseFloat(JSONMapper.readValue(yesterdayString, JsonNode.class).findValuesAsText(base).get(0)));
        }

        catch (Exception e)   //если такая валюта не найдена, кидаем дефолтный ответ с гифкой Йоды
        {
            e.printStackTrace();
            logger.createLog("Can't get exchange");
            APIResponse faultResponse = new APIResponse("Извини, но я не нашел такой валюты: " + currency,
                    0f, 0f, configuration.getFaultGifURL());
            try {return JSONMapper.writeValueAsString(faultResponse);}
            catch (JsonProcessingException jsonProcessingException) {logger.createLog("Failed to make FAULT json response");}
        }

        var compared = (Analyze.compareExchanges(today, yesterday, currency));
        logger.createLog("Compared:" + today + "/" + yesterday);
        if (compared > 0) gifURL = Analyze.parseGif(brokeGifs);
        else if (compared == 0) gifURL = configuration.getEqualityGifURL();
        else gifURL = Analyze.parseGif(richGifs);
        //сравнили курсы и по возвращаемому значению определили, с каким запросом нам нужна гифка

        APIResponse response = new APIResponse(headerMsg, today, yesterday, Additional.makeEmbed(gifURL));    //сформировали ответ для JS-скрипта
        try {return JSONMapper.writeValueAsString(response);}
        catch (JsonProcessingException e)
        {
            logger.createLog("JSONMapper error");
            e.printStackTrace();
            return "Request error";
        }
    }

    @Scheduled(fixedRate = 3600000) //запрос к внешнему API за курсами, бесплатный план обновляется раз в час
    public void refreshCourses()
    {
        todayString = exchangeFeignClient.getStatistic(configuration.getExchangeKey(), Additional.getGreenwichDate(0));
        yesterdayString = exchangeFeignClient.getStatistic(configuration.getExchangeKey(), Additional.getGreenwichDate(-1));
        allCurrencies = exchangeFeignClient.getAllCurrencies();
        richGifs = gifsFeignClient.getGIF(configuration.getGifKey(), "rich");
        brokeGifs = gifsFeignClient.getGIF(configuration.getGifKey(), "broke");
    }
}