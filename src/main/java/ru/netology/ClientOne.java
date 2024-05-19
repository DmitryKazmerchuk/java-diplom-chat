package ru.netology;

import java.io.IOException;

public class ClientOne {
    public static void main(String[] args) throws IOException {
        new ClientLogic("localhost");
    }
}