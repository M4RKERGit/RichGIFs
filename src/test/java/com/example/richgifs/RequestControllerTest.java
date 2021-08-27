package com.example.richgifs;

import com.example.richgifs.feign.ExchangeFeignClient;
import com.example.richgifs.feign.GifsFeignClient;
import com.example.richgifs.tools.Additional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RequestControllerTest  //тест контроллера через MockBean и подмену ответов Feign на семплы, проходит
{
    @MockBean
    public ExchangeFeignClient exchangeFeignClient;

    @MockBean
    public GifsFeignClient gifsFeignClient;

    @Autowired
    public RequestController controller;

    @Test
    public void controllerNotNull()
    {
        Assert.notNull(controller);
    }

    @Test
    public void checkKeys()
    {
        Assert.notNull(controller.getConfiguration().getExchangeKey());
        Assert.notNull(controller.getConfiguration().getGifKey());
    }

    @Test
    public void allCurrencies() throws IOException
    {
        String sampleAll = Files.readString(Path.of("samples/allCurSample.txt"));
        Mockito.when(controller.exchangeFeignClient.getAllCurrencies()).thenReturn(sampleAll);
        Assert.notNull(controller.exchangeFeignClient.getAllCurrencies());
    }

    @Test
    public void responseFromSamples() throws IOException
    {
        String sampleExT = Files.readString(Path.of("samples/todaySample.txt"));
        String sampleExY = Files.readString(Path.of("samples/yesterdaySample.txt"));
        String sampleGif = Files.readString(Path.of("samples/gifSample.txt"));

        String today = Additional.getGreenwichDate(0);
        String yesterday = Additional.getGreenwichDate(-1);

        Mockito.when(controller.exchangeFeignClient.getStatistic(Mockito.anyString(), Mockito.eq(today))).thenReturn(sampleExT);
        Mockito.when(controller.exchangeFeignClient.getStatistic(Mockito.anyString(), Mockito.eq(yesterday))).thenReturn(sampleExY);

        Mockito.when(controller.gifsFeignClient.getGIF(Mockito.anyString(), Mockito.anyString())).thenReturn(sampleGif);

        String result = controller.getJSONResponse("CZK");
        Assert.notNull(result);
        System.out.println("TEST RESULT:\n" + result);
    }

    @Test
    public void wrongCurrency() throws IOException
    {
        String sampleExT = Files.readString(Path.of("samples/todaySample.txt"));
        String sampleExY = Files.readString(Path.of("samples/yesterdaySample.txt"));

        String today = Additional.getGreenwichDate(0);
        String yesterday = Additional.getGreenwichDate(-1);

        Mockito.when(controller.exchangeFeignClient.getStatistic(Mockito.anyString(), Mockito.eq(today))).thenReturn(sampleExT);
        Mockito.when(controller.exchangeFeignClient.getStatistic(Mockito.anyString(), Mockito.eq(yesterday))).thenReturn(sampleExY);

        String result = controller.getJSONResponse("AAAAAA");
        Assert.notNull(result);
        System.out.println("INVALID CURRENCY TEST RESULT:\n" + result);
    }
}