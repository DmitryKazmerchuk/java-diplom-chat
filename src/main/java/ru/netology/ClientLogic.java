package ru.netology;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class ClientLogic {

    private Socket socket; // сокет
    private BufferedReader in; // читаем из сокета
    private BufferedWriter out; // поток чтения в сокет
    private BufferedReader inputUser; // читаем с консоли
    private String addr; // host клиента
    private String nickname; // никнейм
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;


    public ClientLogic(String addr) throws IOException {
        this.addr = addr;
        int port = new PortReader().getPort("settings.txt");

        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Ошибка создания сокета");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.nickname(); // вводим никнейм
            new ReadMsg().start(); // читаем сообщения из сокета
            new WriteMsg().start(); // пишем сообщения в сокет с консоли
        } catch (IOException e) {
            ClientLogic.this.exit();  // закрываем сокет в случае ошибки
        }
    }

    // создаём никнейм
    private void nickname() {
        System.out.print("Здравствуйте! Введите Ваш никнейм: ");
        try {
            nickname = inputUser.readLine();
            out.write(" " + "\n");
            out.flush();
        } catch (IOException ignored) {
        }
    }

    // закрываем сокет
    private void exit() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {
        }
    }

    // читаем сообщения с сервера
    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String str;
            try {
                while (true) {
                    str = in.readLine();
                    if (str.equals("exit")) {
                        ClientLogic.this.exit();
                        break;
                    }
                    System.out.println(str); // пишем сообщение с сервера в консоль
                }
            } catch (IOException e) {
                ClientLogic.this.exit();
            }
        }
    }

    // отправляем сообщение с консоли на сервер
    public class WriteMsg extends Thread {
        File file = new File("clientLog.log");
        Logger logger = Logger.getInstance();
        @Override
        public void run() {
            while (true) {
                String userWord;
                try {
                    time = new Date();
                    dt1 = new SimpleDateFormat("HH:mm:ss");
                    dtime = dt1.format(time);
                    userWord = inputUser.readLine();
                    if (userWord.equals("exit")) {
                        out.write("exit" + "\n");
                        logger.loggerWrite(file, "[" + time + "] " + nickname + ": " + userWord + "\n"); // пишем в clientLog.log
                        ClientLogic.this.exit();
                        break;
                    } else {
                        out.write("[" + dtime + "] " + nickname + ": " + userWord + "\n"); // отправляем на сервер
                        logger.loggerWrite(file, "[" + time + "] " + nickname + ": " + userWord + "\n"); // пишем в clientLog.log
                    }
                    out.flush();
                } catch (IOException e) {
                    ClientLogic.this.exit(); // выходим по исключению
                }

            }
        }
    }
}