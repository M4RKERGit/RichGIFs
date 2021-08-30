package com.example.richgifs;

import com.example.richgifs.api.ResponseMaker;
import com.example.richgifs.feign.ExchangeFeignClient;
import com.example.richgifs.feign.GifsFeignClient;
import com.example.richgifs.tools.Additional;
import com.example.richgifs.tools.Configuration;
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
    public void checkKeys() //проверяем, что с конфигом все хорошо
    {
        Assert.notNull(Configuration.getExchangeKey());
        Assert.notNull(Configuration.getGifKey());
        Assert.notNull(Configuration.getBaseCurrency());
        Assert.notNull(Configuration.getEqualityGifURL());
        Assert.notNull(Configuration.getFaultGifURL());
        //Assert.isTrue(Configuration.getDaysBefore() != 0);    //думаю, можно позволить работу с нулем, мало ли
    }

    @Test
    public void allCurrencies() throws IOException  //проверяем обращение в /all
    {
        String sampleAll = Files.readString(Path.of("samples/allCurSample.txt"));
        Mockito.when(controller.exchangeFeignClient.getAllCurrencies()).thenReturn(sampleAll);
        //здесь и далее - подмена возвращаемых значений на семплы через Mockito
        Assert.notNull(controller.exchangeFeignClient.getAllCurrencies());
    }

    @Test
    public void responseFromSamples() throws IOException
    {
        controller.setTodayString(Files.readString(Path.of("samples/todaySample.txt")));
        controller.setYesterdayString(Files.readString(Path.of("samples/yesterdaySample.txt")));
        controller.setRichGifs(Files.readString(Path.of("samples/gifSample.txt")));
        controller.setBrokeGifs(Files.readString(Path.of("samples/gifSample.txt")));

        String today = Additional.getGreenwichDate(0);
        String yesterday = Additional.getGreenwichDate(-1);

        Mockito.when(controller.exchangeFeignClient.getStatistic(Mockito.anyString(), Mockito.eq(today))).thenReturn(controller.getTodayString());
        Mockito.when(controller.exchangeFeignClient.getStatistic(Mockito.anyString(), Mockito.eq(yesterday))).thenReturn(controller.getYesterdayString());

        Mockito.when(controller.gifsFeignClient.getGIF(Mockito.anyString(), Mockito.anyString())).thenReturn(controller.getRichGifs());

        String result = controller.getJSONResponse("CZK");
        Assert.notNull(result);
        System.out.println("TEST RESULT:\n" + result);
    }

    @Test
    public void wrongCurrency() throws IOException
    {
        controller.setTodayString(Files.readString(Path.of("samples/todaySample.txt")));
        controller.setYesterdayString(Files.readString(Path.of("samples/yesterdaySample.txt")));
        controller.setRichGifs(Files.readString(Path.of("samples/gifSample.txt")));
        controller.setBrokeGifs(Files.readString(Path.of("samples/gifSample.txt")));

        String today = Additional.getGreenwichDate(0);
        String yesterday = Additional.getGreenwichDate(-1);

        Mockito.when(controller.exchangeFeignClient.getStatistic(Mockito.anyString(), Mockito.eq(today))).thenReturn(controller.getTodayString());
        Mockito.when(controller.exchangeFeignClient.getStatistic(Mockito.anyString(), Mockito.eq(yesterday))).thenReturn(controller.getYesterdayString());

        String result = controller.getJSONResponse("AAAAAA");
        Assert.notNull(result);
        Assert.isTrue(ResponseMaker.getFaultResponse().equals(result));
        System.out.println("INVALID CURRENCY TEST RESULT:\n" + result);
    }
}