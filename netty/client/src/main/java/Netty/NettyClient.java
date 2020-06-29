package Netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.util.Date;

public class NettyClient {

    public static void main(String[] args){

        String host = "localhost";
        int port = 6666;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();

            b.group(workerGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
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
}

class TimerClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("receive data from server:" + buf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Bob, This is Alice", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
