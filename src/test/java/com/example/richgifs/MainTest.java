package com.example.richgifs;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class MainTest
{
    @Test
    void mainTest()
    {
        RequestController controller = new RequestController();
        String response = controller.getJSONResponse("RUB");
        Assert.hasText(response);
    }

    /*void mockTest() throws IOException
    {
        String sampleEx = "", sampleAll = "";
        ObjectMapper JSONMapper = new ObjectMapper();
        sampleAll = Files.readString(Path.of("samples/allCurSample.txt"));
        sampleEx = Files.readString(Path.of("samples/todaySample.txt"));

        Mockito.when(controller.exchangeFeignClient.getStatistic(Mockito.anyString(), Mockito.anyString())).thenReturn(sampleEx);
        Mockito.when(controller.exchangeFeignClient.getAllCurrencies()).thenReturn(sampleAll);

        var response = controller.getJSONResponse("EUR");
        JsonNode parsed = JSONMapper.readValue(response, JsonNode.class);
        Assert.hasText(parsed.get("data").toString());
    }*/
}
