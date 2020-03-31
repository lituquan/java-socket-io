package com.ltq.socket.rpc.netty;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.ltq.socket.rpc.code.Request;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {
	private static final HashMap<String, Class<?>> serviceRegistry = new HashMap();

	public static void register(Class serviceInterface, Class impl) {
		System.out.println(serviceInterface.getName());
		serviceRegistry.put(serviceInterface.getName(), impl);
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("client commom in");
	}
	public Object BytetoArray (byte[] obj) {      
        try {        
        	ByteArrayInputStream bos = new ByteArrayInputStream(obj);      
        	ObjectInputStream oos = new ObjectInputStream(bos);         
            Object readObject = oos.readObject();                   
            oos.close();         
            bos.close();     
            return readObject; 
        } catch (Exception ex) {        
            ex.printStackTrace();   
        }      
        return null;
    }  
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		System.out.println("123");
		// 读报文
		ByteBuf in=(ByteBuf) msg;
		int length=in.readInt();
        byte[] data = new byte[length];
        in.readBytes(data);
		Request req = (Request) BytetoArray(data);
		// 执行方法
		String serviceName = req.getClassName();
		String methodName = req.getMethodName();
		Class<?>[] parameterTypes = (Class<?>[]) req.getTypeParameters();
		Object[] arguments = (Object[]) req.getParameters();
		System.out.println(req);
		// 代理方法
		Class serviceClass = serviceRegistry.get(serviceName);
		try {
			if (serviceClass == null) {
				throw new ClassNotFoundException(serviceName + " not found");
			}
			Method method = serviceClass.getMethod(methodName, parameterTypes);
			Object result = method.invoke(serviceClass.newInstance(), arguments);
			// 写报文
	        System.out.println(result);
	        in=Unpooled.buffer(4096);	        
	        in.writeBytes(toByteArray(result));
	        ctx.writeAndFlush(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public byte[] toByteArray (Object obj) {      
        byte[] bytes = null;      
        ByteArrayOutputStream bos = new ByteArrayOutputStream();      
        try {        
            ObjectOutputStream oos = new ObjectOutputStream(bos);         
            oos.writeObject(obj);        
            oos.flush();         
            bytes = bos.toByteArray ();      
            oos.close();         
            bos.close();        
        } catch (IOException ex) {        
            ex.printStackTrace();   
        }      
        return bytes;    
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