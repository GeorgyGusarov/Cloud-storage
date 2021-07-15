package examples;

import javax.sql.DataSource;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class EasyClient {

    public static DataInputStream in;
    public static DataOutputStream out;
    public static boolean isRunning = true;
    private final static int DEFAULT = -1, UPLOAD = 0, DOWNLOAD = 1;
    private static int currentState;

    public void stop() {
        isRunning = false;
    }

    public void init() {
        currentState = DEFAULT;
        try (Socket socket = new Socket("localhost", 8189)) {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (isRunning) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new EasyClient().init();
        new Thread(() -> {
            try {
                Scanner scanner = new Scanner(System.in);
                while (isRunning) {
                    String command = scanner.next();
                    if (command.equals("/upload")) {
                        out.writeUTF(command);
                        currentState = UPLOAD;
                    }
                    if (command.equals("/download")) {
                        out.writeUTF(command);
                        currentState = DOWNLOAD;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
//        File file = new File("C:\\Programming\\Cloud storage\\cloud-client\\src\\main\\resources\\pepe.jpg");
//        System.out.println(file.exists());
//
//        try (Socket socket = new Socket("localhost", 8189)) {
//            System.out.println("Connected to server");
//
//            DataInputStream in = new DataInputStream(socket.getInputStream());
//            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//
//            out.writeUTF("pepe.jpg");
//            out.writeLong(file.length());
//
//            FileInputStream fis = new FileInputStream(file);
//            byte[] buffer = new byte[30_000];
//            while ((fis.read(buffer)) != -1) {
//                out.write(buffer);
//                Arrays.fill(buffer, (byte) 0);
//            }
//            String callBack = in.readUTF();
//            System.out.println(callBack);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
