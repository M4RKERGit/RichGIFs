package com.example.richgifs;

import com.example.richgifs.tools.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RichGiFsApplication
{
    private static final Logger logger = new Logger("[" + RichGiFsApplication.class.getSimpleName().toUpperCase() + "]");
    public static void main(String[] args)
    {
        logger.createLog("RichGIFs launched");
        SpringApplication.run(RichGiFsApplication.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {logger.createLog("Disabling RichGIFs");}));
    }
}
