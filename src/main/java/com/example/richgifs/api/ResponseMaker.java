package com.example.richgifs.api;

import com.example.richgifs.tools.Additional;
import com.example.richgifs.tools.Analyze;
import com.example.richgifs.tools.Configuration;
import com.example.richgifs.tools.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;

public class ResponseMaker  //класс составления ответа на запросы, вынесен из контроллера
{
    //шаблон для ответа в случае внутренней ошибки. Может быть вызвана прямым обращением к API в обход веб-интерфейса или тем, что
    //иногда не все валюты, которые получаются по запросу /all присутствуют в отчете по датам, может, несвоевременно обновляются на внешнем API
    @Getter
    private static final String faultResponse = "{\"headerMsg\": \"Извини, но я не нашел такой валюты\"," +
            " \"secondHeader\": \"Может быть, ее не успели проиндексировать\", \"course\": 0, \"courseYesterday\": 0}";
    private static final Logger logger = new Logger("[" + ResponseMaker.class.getSimpleName().toUpperCase() + "]");
    private static final ObjectMapper JSONMapper = new ObjectMapper();

    static{JSONMapper.enable(SerializationFeature.INDENT_OUTPUT);}

    public static String createJSON(String todayString, String yesterdayString, String currency, String brokeGifs, String richGifs)
    {
        float today, yesterday;
        String gifURL;
        String base = Configuration.getBaseCurrency();
        String headerMsg = "Курс " + currency + " к " + base + " на " + Additional.getCurrentLocalTime();
        String secondHeader = Additional.constructHeader(Configuration.getDaysBefore());
        try
        {
            gifURL = throwGif(todayString, yesterdayString, currency, brokeGifs, richGifs);
            today = 1/(Float.parseFloat(JSONMapper.readValue(todayString, JsonNode.class).findValuesAsText(currency).get(0))
                    /Float.parseFloat(JSONMapper.readValue(todayString, JsonNode.class).findValuesAsText(base).get(0)));
            yesterday = 1/(Float.parseFloat(JSONMapper.readValue(yesterdayString, JsonNode.class).findValuesAsText(currency).get(0))
                    /Float.parseFloat(JSONMapper.readValue(yesterdayString, JsonNode.class).findValuesAsText(base).get(0)));
        }
        catch (Exception e)   //если такая валюта не найдена, кидает дефолтный ответ с гифкой Йоды
        {
            e.printStackTrace();
            logger.createLog("Can't get exchange");
            return faultResponse;
        }

        APIResponse response = new APIResponse(headerMsg, secondHeader, today, yesterday, Additional.makeEmbed(gifURL));    //сформировали ответ для JS-скрипта
        try {return JSONMapper.writeValueAsString(response);}
        catch (JsonProcessingException e)
        {
            logger.createLog("JSONMapper error");
            e.printStackTrace();
            return "Request error";
        }
    }

    public static String throwGif(String todayString, String yesterdayString, String currency, String brokeGifs, String richGifs)
    {
        float today, yesterday;
        String toRet;
        String base = Configuration.getBaseCurrency();
        try
        {
            today = 1/(Float.parseFloat(JSONMapper.readValue(todayString, JsonNode.class).findValuesAsText(currency).get(0))
                    /Float.parseFloat(JSONMapper.readValue(todayString, JsonNode.class).findValuesAsText(base).get(0)));
            yesterday = 1/(Float.parseFloat(JSONMapper.readValue(yesterdayString, JsonNode.class).findValuesAsText(currency).get(0))
                    /Float.parseFloat(JSONMapper.readValue(yesterdayString, JsonNode.class).findValuesAsText(base).get(0)));
            //распарсили курсы во float
        }
        catch (Exception e)
        {
            logger.createLog("Float parsing failed");
            return Configuration.getFaultGifURL();
        }
        var compared = (Analyze.compareExchanges(today, yesterday, currency));
        if (compared > 0) toRet = Analyze.parseGif(richGifs);
        else if (compared == 0) toRet = Configuration.getEqualityGifURL();
        else toRet = Analyze.parseGif(brokeGifs);
        return Additional.makeEmbed(toRet);
    }
}
