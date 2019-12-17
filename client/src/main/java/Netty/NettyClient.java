package Netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.ByteBuffer;
import java.util.Date;

public class NettyClient {

    public static void main(String[] args){

        String host = "localhost";

        int port = 6666;

        EventLoopGroup workerGroup = new NioEventLoopGroup(1);

        try{
            Bootstrap b = new Bootstrap();

            b.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(new TimerClientHandler());
                        }
                    });

            ChannelFuture f = b.connect(host,port).sync();

            f.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            workerGroup.shutdownGracefully();
        }
    }

    static class TimerClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg){
            ByteBuf m = (ByteBuf) msg;
            try{
                long time = (m.readUnsignedInt() - 2208988800L) * 1000L;
                System.out.println(new Date(time));
                ctx.close();
            } finally {
                m.release();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
            cause.printStackTrace();
            ctx.close();
        }
    }
}
