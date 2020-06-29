package com.bpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * @author yd
 * @date 2020/1/8 2:52 下午
 */

class MyChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("receive msg:" + buf.toString(CharsetUtil.UTF_8));
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Alice. This is Bob", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws  Exception{
        cause.printStackTrace();
        ctx.close();
    }
}

public class NettyServer {

    public static void main(String[] args) throws Exception{
        EventLoopGroup bossEventLoop = new NioEventLoopGroup();
        EventLoopGroup workerEventLoop = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();

        try{
            b.group(bossEventLoop,workerEventLoop)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel sh){
                            sh.pipeline().addLast(new MyChannelHandler());
                        }
                    });

            ChannelFuture future = b.bind("localhost",6666).sync();

            future.channel().closeFuture().sync();
        } finally {
            bossEventLoop.shutdownGracefully();
            workerEventLoop.shutdownGracefully();
        }


    }
}
