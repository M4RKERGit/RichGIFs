package com.example.richgifs.getters;

import com.example.richgifs.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class Getter
{
    protected String apiKey;
    protected Logger logger = new Logger("[" + getClass().getSimpleName() + "]");
    protected ObjectMapper JSONMapper;

    public String connectAndGet(String searchURL)
    {
        String output;
        HttpURLConnection conTar = null;
        try
        {
            conTar = (HttpURLConnection) new URL(searchURL).openConnection();   //гораздо более актуальный способ сливать большие ответы
            conTar.connect();
            InputStream is = conTar.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null)
            {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            conTar.disconnect();
            output = response.toString();
        }
        catch (Exception e)
        {
            logger.createLog("Failed to connect");
            return "";
        }
        logger.createLog("Connection closed");
        return output;
    }
}
