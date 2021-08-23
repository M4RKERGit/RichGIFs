package com.example.richgifs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Logger
{
    private final String modulePrefix;
    private static int logCount = 0;
    private static final String startTime = Additional.getCurrentTime();

    public String getModulePrefix() {return modulePrefix;}
    public static int getLogCount() {return logCount;}
    public static String getStartTime() {return startTime;}

    public Logger(String prefix)
    {
        this.modulePrefix = prefix;
    }

    public void createLog(String message)
    {
        String log = Additional.getCurrentTime() + ' ' + this.modulePrefix + ' ' + message;
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