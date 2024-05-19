package ru.netology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    @Test
    void loggerWrite() {
        File newFiles = new File("testFile.txt");
        String strTest = "test";
        try{
            if (newFiles.createNewFile()) {
                new Logger().loggerWrite(newFiles, strTest);
                BufferedReader reader = new BufferedReader(new FileReader(newFiles));
                String str = reader.readLine();
                Assertions.assertEquals(strTest,str);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}