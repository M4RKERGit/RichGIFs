package com.example.richgifs.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "Exchange", url = "https://openexchangerates.org/api/")
public interface ExchangeFeignClient    //интерфейс Feign для обращения к валютам
{
    @GetMapping("historical/{date}.json?app_id={apiKey}")
    String getStatistic(@PathVariable String apiKey, @PathVariable String date); //получение курса на выбранную дату

    @GetMapping("currencies.json")
    String getAllCurrencies();   //получение списка всех валют (заменено в JS)
}