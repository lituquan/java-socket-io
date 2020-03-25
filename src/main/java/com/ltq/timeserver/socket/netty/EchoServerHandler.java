package com.ltq.timeserver.socket.netty;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    public static final String DATE_FORMAT="yyyy-MM-dd hh:mm:ss";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        String msgStr=in.toString(CharsetUtil.UTF_8);
        System.out.println(msgStr);
        SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT);    
        in=Unpooled.buffer(1024);
        msgStr=format.format(new Date())+":"+msgStr;
        in.writeBytes(msgStr.getBytes());
        ctx.writeAndFlush(in);//
    }
 
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    }
 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}