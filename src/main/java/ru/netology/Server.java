package ru.netology;
import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;

class ServerSomthing extends Thread {
    private Socket socket; // сокет
    private BufferedReader in; // читаем из сокета
    private BufferedWriter out; // записываем в сокет


    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        if (!Story.story.isEmpty()){
            Server.story.printStory(out); // передача последних 10 сообщений
        }
        start(); // вызываем run()
    }
    @Override
    public void run() {
        String word;
        LocalDate date = LocalDate.now();

        try {
            word = in.readLine();

            try {
                out.write(word + "\n");
                out.flush();
            } catch (IOException ignored) {}
            try {
                while (true) {
                    word = in.readLine();
                    if(word.equals("exit")) {

                        Server.logger.loggerWrite(Server.file, "[" + date + "] " + word + "\n");
                        this.exit();
                        break;
                    }
                    Server.logger.loggerWrite(Server.file, "[" + date + "] " + word + "\n");
                    Server.story.addStoryEl(word);
                    for (ServerSomthing vr : Server.serverList) {
                        vr.send(word); // отсылаем сообщения от клиента всем другим клиентам
                    }
                }
            } catch (NullPointerException ignored) {}


        } catch (IOException e) {
            this.exit();
        }
    }
    // отсылка сообщения
    public void send(String msg) {
        try {
            if (!msg.isEmpty()) {
                out.write(msg + "\n");
                out.flush();
            }
        } catch (IOException ignored) {}

    }

    private void exit() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerSomthing vr : Server.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}
class Story {
    public static LinkedList<String> story = new LinkedList<>();
    public void addStoryEl(String el) {
        if (story.size() >= 10) {
            story.removeFirst();
            story.add(el);
        } else {
            story.add(el);
        }
    }
    public void printStory(BufferedWriter writer) {
            try {
                writer.write("История сообщений:" + "\n");
                for (String vr : story) {
                    writer.write(vr + "\n");
                }
                writer.write("/...." + "\n");
                writer.flush();
            } catch (IOException ignored) {}
    }
}

public class Server {
    public static LinkedList<ServerSomthing> serverList = new LinkedList<>(); // список всех нитей - экземпляров
    public static Story story;
    public static File file = new File("serverLog.log");
    public static Logger logger = Logger.getInstance();

    public static void main(String[] args) throws IOException {
        int port = new PortReader().getPort("settings.txt");
        ServerSocket server = new ServerSocket(port);
        story = new Story();
        System.out.println("Server Started");
        Date time = new Date();
        logger.loggerWrite(file, "[" + time + "] " + "Server Started \n");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerSomthing(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}