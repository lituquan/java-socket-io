package com.ltq.timeserver.socket.mockqqnetty;

import java.util.Scanner;

import com.ltq.timeserver.socket.Config;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class EchoClient {
    public static void main(String[] args) {
 
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    //p.addLast(new LoggingHandler(LogLevel.INFO));
                    p.addLast(new EchoClientHandler());
                }
            });
     
            // Start the client.
            ChannelFuture f = b.connect(Config.HOST_ADDRESS, Config.PORT).sync();
            while(true){
                Scanner sc=new Scanner(System.in);
                ByteBuf message = Unpooled.buffer(1024);
                System.out.print("client>>");  			      
                String nextLine = sc.nextLine(); //stdin.input
                message.writeBytes(nextLine.getBytes(CharsetUtil.UTF_8));
                f.channel().writeAndFlush(message);
                
            }
            //f.channel().closeFuture().sync();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
     
}