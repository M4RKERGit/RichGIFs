package com.example.richgifs.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "GIFs", url = "https://api.giphy.com/v1/gifs/search")
public interface GifsFeignClient    //интерфейс Feign для получения гифок по запросу
{
    @GetMapping("?q={request}&api_key={apiKey}&limit=25")
    public String getGIF(@PathVariable String apiKey, @PathVariable String request);
}