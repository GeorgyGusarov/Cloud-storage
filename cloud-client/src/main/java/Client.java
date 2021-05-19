import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) {
        File file = new File("cloud-client\\src\\main\\resources\\pepe.jpg");
        System.out.println(file.exists());
        // bla bla

        try (Socket socket = new Socket("localhost", 8189);) {
            System.out.println("Connected to server");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF("pepe.jpg");
            out.writeLong(file.length());

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[30_000];
            while ((fis.read(buffer)) != -1) {
                out.write(buffer);
                Arrays.fill(buffer, (byte) 0);
            }
            String callBack = in.readUTF();
            System.out.println(callBack);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
