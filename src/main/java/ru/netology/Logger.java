package ru.netology;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class Logger {
    private static Logger INSTANCE = null;
    Logger() {
    }
    public static Logger getInstance() {
        if (INSTANCE == null) {
            synchronized (Logger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Logger();
                }
            }
        }
        return INSTANCE;
    }

    public void loggerWrite(File tempFiles, String log) {
        try (FileOutputStream fos = new FileOutputStream(tempFiles, true);) {
            byte[] bytes = log.toString().getBytes();
            fos.write(bytes, 0, bytes.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}