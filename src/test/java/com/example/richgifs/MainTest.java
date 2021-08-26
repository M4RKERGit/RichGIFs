package com.example.richgifs;

import com.example.richgifs.feign.ExchangeFeignClient;
import com.example.richgifs.feign.GifsFeignClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTest   //тест с моком внешнего API, не проходит
{
    @MockBean
    public ExchangeFeignClient exchangeFeignClient;

    @MockBean
    public GifsFeignClient gifsFeignClient;

    @Autowired
    public RequestController controller;

    @Test
    void mockTest() throws IOException
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
    }
}
