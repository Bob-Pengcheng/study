package Netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * @author baopc@tuya.com
 * @date 2020/1/14
 */
public class NettyClient2 {
    public static void main(String[] args) throws  Exception{

        //1. 创建一个线程组
        EventLoopGroup group=new NioEventLoopGroup();
        //2. 创建客户端的启动助手，完成相关配置
        Bootstrap b=new Bootstrap();
        b.group(group)  //3. 设置线程组
                .channel(NioSocketChannel.class)  //4. 设置客户端通道的实现类
                .handler(new ChannelInitializer<SocketChannel>() {  //5. 创建一个通道初始化对象
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyClientHandler()); //6.往Pipeline链中添加自定义的handler
                    }
                });
        System.out.println("......Client is  ready......");

        //7.启动客户端去连接服务器端  connect方法是异步的   sync方法是同步阻塞的
        ChannelFuture cf=b.connect("localhost",9999).sync();

        //8.关闭连接(异步非阻塞)
        cf.channel().closeFuture().sync();

    }
}

class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //通道就绪事件
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("Client:"+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("老板，还钱吧",CharsetUtil.UTF_8));
    }

    //读取数据事件
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf buf=(ByteBuf) msg;
        System.out.println("服务器端发来的消息："+buf.toString(CharsetUtil.UTF_8));
    }

}
