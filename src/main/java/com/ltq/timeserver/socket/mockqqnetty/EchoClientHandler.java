package com.ltq.timeserver.socket.mockqqnetty;

import java.util.Scanner;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    	// final Channel ch=ctx.channel();
    	// new Thread(){
    	// 	public void run(){
    	// 		while(true){
  		// 		  Scanner sc=new Scanner(System.in);
  		// 	      ByteBuf message = Unpooled.buffer(1024);
  		// 	      System.out.print("client>>");  			      
  		// 	      String nextLine = sc.nextLine(); //stdin.input
  		// 	      message.writeBytes(nextLine.getBytes(CharsetUtil.UTF_8));
  		// 	      ch.writeAndFlush(message);
	  	// 		}
    			
    	// 	}
    	// }.start();
    }
 
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(((ByteBuf) msg).toString(CharsetUtil.UTF_8));        
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
    }
}