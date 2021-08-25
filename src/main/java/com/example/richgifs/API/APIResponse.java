package com.example.richgifs.API;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class APIResponse
{
    private final String headerMsg;
    private final float course, courseYesterday;
    private final String gifURL;

    public APIResponse(String headerMsg, float course, float courseYesterday, String gifURL)
    {
        this.course = course;
        this.courseYesterday = courseYesterday;
        this.gifURL = gifURL;
        this.headerMsg = headerMsg;
    }
}
