package com.ltq.socket.mockqq.netty;

import java.util.HashMap;
import java.util.Map;

import com.ltq.socket.mockqq.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    public static final String DATE_FORMAT="yyyy-MM-dd hh:mm:ss";
    public static final Map<String ,Channel> cMap=new HashMap();
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        String msgStr=in.toString(CharsetUtil.UTF_8);
        System.out.println("客户端:"+msgStr);
        Message m=new Message(msgStr); 
        
        in=Unpooled.buffer(1024);
        if(m.content.equals("login")){
            cMap.put(m.from, ctx.channel());
            channelGroup.add(ctx.channel());
            System.out.println("用户"+m.from+"登录"); 
            return;                   	   
        }else{

            Channel writer=cMap.get(m.to);
            if(writer==null){
                System.out.println("用户"+m.to+"不在线");
                //queue.put(msg); //消息重试
                return;
            }
            msgStr="from server:"+msgStr;
            System.out.println(msgStr);
            in.writeBytes(msgStr.getBytes());
            writer.writeAndFlush(in);//
        }
          

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