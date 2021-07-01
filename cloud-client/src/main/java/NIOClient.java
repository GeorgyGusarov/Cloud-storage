import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NIOClient {
    static boolean exit = false;

    public static void main(String[] args) {
        try {
            SocketChannel channel = SocketChannel.open(new InetSocketAddress(8189));
            Scanner in = new Scanner(System.in);
            channel.configureBlocking(false);
            ByteBuffer data = ByteBuffer.allocate(256);
            new Thread(() -> {
                while (!exit) {
                    if (channel.isConnected()) {
                        try {
                            int read = 0;
                            StringBuilder message = new StringBuilder();
                            data.clear();
                            boolean flag = false;
                            while ((read = channel.read(data)) > 0) {
                                data.flip();
                                byte[] bytes = new byte[data.limit()];
                                data.get(bytes);
                                message.append(new String(bytes));
                                data.clear();
                                flag = true;
                            }
                            if (flag) {
                                if (message.length() > 0) {
                                    System.out.println(message);
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            }).start();

            while (true) {
                String msg = in.nextLine();
                channel.write(ByteBuffer.wrap(msg.getBytes()));
                if (msg.equals("quit")) {
                    exit = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
