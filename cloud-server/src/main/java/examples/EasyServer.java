package examples;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

public class EasyServer {

    public static void main(String[] args) {
        System.out.println("Server is online, waiting for client to connect...");
        new EasyServer().start();
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String com = scanner.next();
                if (com.equals("/quit")) {
                    stop();
                    break;
                }
            }
        }).start();
//        ServerSocket serverSocket;
//        try {
//            serverSocket = new ServerSocket(8189);
//            Socket socket = serverSocket.accept();
//            System.out.println("Client " + serverSocket.getLocalPort() + " accepted");
//
//            DataInputStream in = new DataInputStream(socket.getInputStream());
//            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//
//            String fileName = in.readUTF();
//            long length = in.readLong();
//            System.out.println("fileLength: " + length + " Mb.");
//
//            File file = new File(fileName);
//            if (!file.exists()) file.createNewFile();
//
//            FileOutputStream fos = new FileOutputStream(file);
//            for (long i = 0; i < length; i++) fos.write(in.read());
//
//            fos.close();
//            out.writeUTF("File: " + fileName + ", downloaded!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static boolean isRunning = true;
    public void setRunning(boolean running) {
        isRunning = running;
    }

    public ConcurrentLinkedDeque<FileHandler> clients; // храним очередь клиентов
    public ConcurrentLinkedDeque<FileHandler> getClients() {
        return clients;
    }
    public void removeFromClientsList(FileHandler handler) {
        clients.remove(handler);
    }

    public static void stop() {
        isRunning = false;
    }

    public void start() {
        clients = new ConcurrentLinkedDeque<>();
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (isRunning) {
                Socket socket = serverSocket.accept();
                FileHandler connection = new FileHandler(this, socket);
                new Thread(connection).start();
                clients.add(connection);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
