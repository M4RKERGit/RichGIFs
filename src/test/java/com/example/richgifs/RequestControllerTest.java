package com.example.richgifs;

import com.example.richgifs.feign.ExchangeFeignClient;
import com.example.richgifs.feign.GifsFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RequestControllerTest  //тест контроллера через MockBean, падает (argument "content" is null)
{
    @MockBean
    public ExchangeFeignClient exchangeFeignClient;

    @MockBean
    public GifsFeignClient gifsFeignClient;

    @Autowired
    public RequestController controller;

    @Test
    public void mainTest()
    {
        Assert.notNull(controller);
        System.out.println(controller.getConfiguration().getExchangeKey());
        System.out.println(controller.getConfiguration().getGifKey());
        //System.out.println("TEST RESULT: " + controller.getJSONResponse("EUR"));
    }
}