package examples;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.Iterator;

public class NIOServer implements Runnable {
    public static void main(String[] args) {
        new Thread(new NIOServer()).start();
    }

    private ServerSocketChannel srv;
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(256);
    private static int count = 1;

    @Override
    public void run() {
        try {
            srv = ServerSocketChannel.open();
            srv.socket().bind(new InetSocketAddress(8189));
            srv.configureBlocking(false);
            selector = Selector.open();
            srv.register(selector, SelectionKey.OP_ACCEPT);

            SelectionKey sKey;
            Iterator<SelectionKey> iterator;

            System.out.println("Server started on port 8189");

            while (srv.isOpen()) {
                selector.select();
                iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    sKey = iterator.next();
                    iterator.remove();
                    if (sKey.isAcceptable()) {
                        handleAccept(sKey);
                    }
                    if (sKey.isReadable()) {
                        handleRead(sKey);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * принимает соединение и зарегистрировать на канале чтение
     */
    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        count++;
        String userName = "Client # " + count;
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ, userName);
        System.out.println("New client was connected!");
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        StringBuilder message = new StringBuilder();
        buffer.clear();
        int read = 0; // прочитано
        if (channel.isConnected()) {
            while ((read = channel.read(buffer)) > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                message.append(new String(bytes));
                buffer.clear();
            }
            String msg;
            if (read < 0) {
                msg = key.attachment() + " leave the chat";
                channel.close();
            } else {
                if (message.toString().equals("quit")) {
                    msg = key.attachment() + " leave the chat";
                    channel.close();
                } else {
                    msg = key.attachment() + ": " + message.toString();
                }
            }
            System.out.println(msg);
            broadcastMessage(msg);
        }
    }

    private void broadcastMessage(String message) throws IOException {
        ByteBuffer msg = ByteBuffer.wrap(message.getBytes());
        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                ((SocketChannel) key.channel()).write(msg);
                msg.rewind();
            }
        }
    }
}
