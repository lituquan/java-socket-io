package com.ltq.socket.echo.netty;

import java.util.Scanner;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Scanner sc=new Scanner(System.in);
        ByteBuf message = Unpooled.buffer(1024);      
        message.writeBytes("client common in".getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(message); //发到服务器,这里还有个作用，发出第一个消息，才能接受消息进入channelRead
    }
 
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(((ByteBuf) msg).toString(CharsetUtil.UTF_8));  
        Scanner sc=new Scanner(System.in);
        ByteBuf message = Unpooled.buffer(1024);
        System.out.println("client");
        String nextLine = sc.nextLine(); //stdin.input
        message.writeBytes(nextLine.getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(message); //发到服务器     
    }
 
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // channelsMap.put(new User(100,"张三"), ctx.channel());
    }
}