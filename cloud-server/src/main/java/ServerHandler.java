import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private String cloudStoragePath = "C:\\Programming\\Cloud storage\\cloud-server\\cloud_storage";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof FileMessage) {
                FileMessage fileMessage = (FileMessage) msg;
                Files.write(Paths.get(cloudStoragePath + fileMessage.getFileName()), fileMessage.getData(), StandardOpenOption.CREATE);
                updateCloudListView(ctx);
            }
            if (msg instanceof DownloadRequest) {
                DownloadRequest downloadRequest = (DownloadRequest) msg;
                if (Files.exists(Paths.get(cloudStoragePath + downloadRequest.getFileName()))) {
                    FileMessage fileMessage = new FileMessage(Paths.get(cloudStoragePath + downloadRequest.getFileName()));
                    ctx.writeAndFlush(fileMessage);
                }
            }
            if (msg instanceof UpdateCloudMessage) {
                updateCloudListView(ctx);
            }
            if (msg instanceof DeleteRequest) {
                DeleteRequest deleteRequest = (DeleteRequest) msg;
                Files.delete(Paths.get(cloudStoragePath + deleteRequest.getFileName()));
                updateCloudListView(ctx);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void updateCloudListView(ChannelHandlerContext ctx) {
        try {
            ArrayList<String> serverFileList = new ArrayList<>();
            Files.list(Paths.get(cloudStoragePath))
                    .map(path -> path.getFileName()
                    .toString())
                    .forEach(serverFileList::add);
            ctx.writeAndFlush(new UpdateCloudMessage(serverFileList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
