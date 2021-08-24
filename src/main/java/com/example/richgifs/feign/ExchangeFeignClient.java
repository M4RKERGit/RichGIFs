package com.example.richgifs.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "Exchange", url = "https://openexchangerates.org/api/")
public interface ExchangeFeignClient
{
    @GetMapping("historical/{date}.json?app_id={apiKey}")
    public String getStatistic(@PathVariable String apiKey, @PathVariable String date);

    @GetMapping("currencies.json")
    public String getAllCurrencies();
}