package com.example.richgifs;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResponseTest   //тест ответа контроллера запущенного приложения, проходит
{
    Response response = RestAssured.when().get("/api/RUB");
    @Test
    void correctResponse()
    {
        response.then().assertThat().statusCode(200)
                .and().body("headerMsg", Matchers.containsString("Курс"))
                .and().body("course", Matchers.greaterThanOrEqualTo(0f))
                .and().body("courseYesterday", Matchers.greaterThanOrEqualTo(0f));
    }

    @Test
    void validGIF() throws IOException
    {
        String result = response.getBody().path("gifURL").toString();
        URL url = new URL(result);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("HEAD");
        Assert.isTrue(huc.getResponseCode() == 200);
    }
}
