package netty_examples;

import io.netty.bootstrap.*;
import io.netty.channel.*;
import io.netty.channel.nio.*;
import io.netty.channel.socket.*;
import io.netty.channel.socket.nio.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty_examples.handlers.BroadcastHandler;
import netty_examples.handlers.InputHandler;
import netty_examples.handlers.OutputHandler;

public class NettyServer implements Runnable {

    public static void main(String[] args) {
        new Thread(new NettyServer()).start();
    }

    @Override
    public void run() {
        EventLoopGroup auth = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(auth, worker)
                     .channel(NioServerSocketChannel.class)
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel socketChannel) {
                             socketChannel.pipeline().addLast(new StringDecoder(),
                                                              new StringEncoder(),
                                                              new BroadcastHandler());
                         }
                     });
            ChannelFuture future = bootstrap.bind(8189).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
            auth.shutdownGracefully();
        }
    }
}
