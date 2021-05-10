import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        File file = new File("C:\\Programming\\Cloud storage\\cloud-client\\src\\main\\resources\\pepe.jpg");
        //System.out.println(file.exists());

        try (Socket socket = new Socket("localhost", 8189);) {
            System.out.println("Connected to server");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF("pepe.jpg");
            out.writeLong(file.length());

            FileInputStream fis = new FileInputStream(file);
            int x;
            while ((x = fis.read()) != -1) {
                out.write(x);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
