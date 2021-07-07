package netty_examples.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class MainHandler extends SimpleChannelInboundHandler<String> {

    private static final List<Channel> channels = new ArrayList<>();
    private static int clientNumber = 1;
    private String name;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client was connected");
        channels.add(ctx.channel());
        name = "Client # " + clientNumber;
        clientNumber++;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) {
        System.out.println("Delivered message: " + s);
        String send = String.format("[%s]: %s", name, s);
        for (Channel channel : channels) {
            channel.writeAndFlush(send);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client was disconnected");
        channels.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
