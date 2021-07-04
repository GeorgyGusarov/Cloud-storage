package netty_examples.handlers;

import io.netty.channel.*;
import io.netty.channel.Channel;
import java.util.concurrent.*;

public class BroadcastHandler extends ChannelInboundHandlerAdapter {

    private static ConcurrentLinkedDeque<Channel> channels = new ConcurrentLinkedDeque<>();
    private static int count = 0;

    public BroadcastHandler() {
        super();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("IN: connection open");
        count++;
        String name = "User # " + count;
        channels.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("IN: connection closed");
        channels.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String message = (String) msg;
        System.out.println(msg);
        for (Channel channel : channels) {
            channel.writeAndFlush(message);
        }
    }
}
