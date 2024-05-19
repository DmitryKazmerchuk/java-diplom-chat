package ru.netology;

import java.io.*;

public class PortReader {



    public int getPort(String path) throws IOException {
        int port = -1;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            port = Integer.parseInt(reader.readLine());
        } catch (FileNotFoundException e) {
            System.out.println("Файл settings.txt не найден");
        }
        return port;
    }
}
