package ru.netology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

class PortReaderTest {

    @Test
    void canReadFiles() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("settings.txt"));
            int portRez = Integer.parseInt(reader.readLine());
            int portSample = new PortReader().getPort("settings.txt");
            Assertions.assertEquals(portRez,portSample);
        } catch (FileNotFoundException e) {
            System.out.println("Тест: Файл settings.txt не найден");
        }
    }

}