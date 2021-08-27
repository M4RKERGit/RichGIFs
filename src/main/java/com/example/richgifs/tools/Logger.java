package com.example.richgifs.tools;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Getter
public class Logger //класс для логгирования информации о запуске/выключении/исключениях в программе
{
    private final String modulePrefix;
    private static int logCount = 0;
    private static final String startTime = Additional.getCurrentLocalTime();

    public Logger(String prefix) //логгер создается в каждом классе, имеющем методы, с определенным префиксом формата [ИМЯ_КЛАССА]
    {
        this.modulePrefix = prefix;
    }

    public void createLog(String message)
    {
        String log = Additional.getCurrentLocalTime() + ' ' + this.modulePrefix + ' ' + message;
        try
        {
            if (!Files.exists(Path.of("log.txt")))
            {
                createLog("Log file wasn't found, creating a new one, path: " + Files.createFile(Path.of("log.txt")));
            }
            Files.write(Path.of("log.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
            System.out.println(log);
            logCount++;
        }
        catch (IOException e)
        {
            System.out.println("Logging error");
            e.printStackTrace();
        }
    }
}