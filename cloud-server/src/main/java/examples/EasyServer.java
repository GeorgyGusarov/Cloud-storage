package examples;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class EasyServer {
    public static void main(String[] args) {
        System.out.println("Waiting for client to connect...");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8189);
            Socket socket = serverSocket.accept();
            System.out.println("Client " + serverSocket.getLocalPort() + " accepted");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String fileName = in.readUTF();
            long length = in.readLong();
            System.out.println("fileLength: " + length + " Mb.");

            File file = new File(fileName);
            if (!file.exists()) file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[30_000];
            for (long i = 0; i < length; i++) fos.write(in.read());

            fos.close();
            out.writeUTF("File: " + fileName + ", downloaded!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
