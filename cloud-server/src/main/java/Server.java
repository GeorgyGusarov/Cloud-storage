import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8189);
        Socket socket = serverSocket.accept();
        System.out.println("Client accepted");

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        int length = in.readInt();
        System.out.println("fileLength: " + length);
        String fileName = in.readUTF();

        File file = new File(fileName);
        if (!file.exists()) file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);
        for (int i = 0; i < length; i++) {
            fos.write(in.read());
        }
        fos.close();
        out.writeUTF("File: " + fileName + ", downloaded!");
    }
}
