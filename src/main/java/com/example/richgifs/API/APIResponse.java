package com.example.richgifs.API;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class APIResponse    //класс описания ответа от API, для формирования ответа в контроллере
{
    private final String headerMsg; //заголовок сообщения на сайте
    private final float course, courseYesterday;    //курсы валюты
    private final String gifURL;    //ссылка на GIF, обрабатывается в embed на конечной машине

    public APIResponse(String headerMsg, float course, float courseYesterday, String gifURL)
    {
        this.course = course;
        this.courseYesterday = courseYesterday;
        this.gifURL = gifURL;
        this.headerMsg = headerMsg;
    }
}
