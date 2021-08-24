package com.example.richgifs.API;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class APIResponse
{
    private final String headerMsg;
    private final float firstCourse;
    private final String gifURL;

    public APIResponse(String headerMsg, float firstCourse, float secondCourse, String gifURL)
    {
        this.firstCourse = firstCourse;
        this.gifURL = gifURL;
        this.headerMsg = headerMsg;
    }
}
