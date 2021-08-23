package com.example.richgifs.pesponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response
{
    public SingleGif[] getData() {return data;}
    public void setData(SingleGif[] data) {this.data = data;}
    private SingleGif[] data;
}
