package java_nio_examples;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class ChannelTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("FileTest.txt", "rw");
        FileChannel channel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(5);
        for (int i = 0; i < 5; i++) {
            buffer.put((byte) (65 + i));
        }
        System.out.println(Arrays.toString(buffer.array()));
        buffer.flip();

        for (int i = 0; i < buffer.limit(); i++) {
            System.out.print((char) buffer.get());
        }
        System.out.println();

        ByteBuffer dst = ByteBuffer.allocate(15);
        channel.read(dst);
        dst.flip();
        while (dst.hasRemaining()) System.out.print((char) dst.get());
    }
}
