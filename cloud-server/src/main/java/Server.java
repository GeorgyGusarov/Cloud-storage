import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8189);
            Socket socket = serverSocket.accept();
            System.out.println("Client accepted");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            long length = in.readLong();
            System.out.println("fileLength: " + length);
            String fileName = in.readUTF();

            File file = new File(fileName);
            if (!file.exists()) file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            for (long i = 0; i < length; i++) {
                fos.write(in.read());
                if (i % 1000 == 0) System.out.println(10000 + " b downloaded.");
            }
            fos.close();
            out.writeUTF("File: " + fileName + ", downloaded!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
