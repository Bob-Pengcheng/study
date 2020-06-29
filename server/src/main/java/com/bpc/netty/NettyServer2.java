package com.bpc.netty;

/**
 * @author baopc@tuya.com
 * @date 2020/1/14
 */
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

public class NettyServer2 {
    public static void main(String[] args) throws Exception{

        //1. 创建一个线程组：接收客户端连接
        EventLoopGroup bossGroup =new NioEventLoopGroup();
        //2. 创建一个线程组：处理网络操作
        EventLoopGroup workerGroup =new NioEventLoopGroup();
        //3. 创建服务器端启动助手来配置参数
        ServerBootstrap b=new ServerBootstrap();
        b.group(bossGroup,workerGroup) //4.设置两个线程组
                .channel(NioServerSocketChannel.class) //5.使用NioServerSocketChannel作为服务器端通道的实现
                .option(ChannelOption.SO_BACKLOG,128) //6.设置线程队列中等待连接的个数
                .childOption(ChannelOption.SO_KEEPALIVE,true) //7.保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {  //8. 创建一个通道初始化对象
                    @Override
                    public void initChannel(SocketChannel sc){   //9. 往Pipeline链中添加自定义的handler类
                        sc.pipeline().addLast(new NettyServerHandler());
                    }
                });
        System.out.println("......Server is ready......");
        ChannelFuture cf=b.bind(9999).sync();  //10. 绑定端口 bind方法是异步的  sync方法是同步阻塞的
        System.out.println("......Server is starting......");

        //11. 关闭通道，关闭线程组
        cf.channel().closeFuture().sync(); //异步
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}

//服务器端的业务处理类
class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据事件
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        System.out.println("Server:"+ctx);
        ByteBuf buf=(ByteBuf) msg;
        System.out.println("客户端发来的消息："+buf.toString(CharsetUtil.UTF_8));
    }

    //数据读取完毕事件
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.copiedBuffer("就是没钱",CharsetUtil.UTF_8));
    }

    //异常发生事件
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable t){
        ctx.close();
    }


}