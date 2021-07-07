package netty_examples;

import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class NettyClient {

    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        Scanner in = new Scanner(System.in);
        while (true) {
            String message = in.nextLine();
            if (message.equals("exit")) break;
            client.sendMessage(message);
        }
    }

    private SocketChannel channel;

    public NettyClient() {
        new Thread(() -> {
           EventLoopGroup worker = new NioEventLoopGroup();
           try {
               Bootstrap bootstrap = new Bootstrap();
               bootstrap.group(worker)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) {
                                channel = socketChannel;
                                System.out.println("Connected!");
                                socketChannel.pipeline().addLast(new StringDecoder(),
                                                                 new StringEncoder(),
                                                                 new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) {
                                        System.out.println(s);
                                    }
                                });
                            }
                        });
               ChannelFuture future = bootstrap.connect("localhost", 8189).sync();
               future.channel().closeFuture().sync();
           } catch (Exception e) {
               e.printStackTrace();
           } finally {
               worker.shutdownGracefully();
           }
        }).start();
    }

    public void sendMessage(String message) {
        channel.writeAndFlush(message);
    }
}
