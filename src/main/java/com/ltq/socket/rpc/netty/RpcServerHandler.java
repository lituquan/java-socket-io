package com.ltq.socket.rpc.netty;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.ltq.socket.rpc.code.Request;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {
	private static final HashMap<String, Class<?>> serviceRegistry = new HashMap();

	public static void register(Class serviceInterface, Class impl) {
		serviceRegistry.put(serviceInterface.getName(), impl);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		// 读报文
		Request req = (Request) msg;
		Object result=handle(req);
		// 写报文
		ctx.writeAndFlush(result).addListener(ChannelFutureListener.CLOSE);

	}
  
	private Object handle(Request req) {
		// 执行方法
		String serviceName = req.getClassName();
		String methodName = req.getMethodName();
		Class<?>[] parameterTypes = (Class<?>[]) req.getTypeParameters();
		Object[] arguments = (Object[]) req.getParameters();
		// 代理方法
		Class serviceClass = serviceRegistry.get(serviceName);
		try {
			if (serviceClass == null) {
				throw new ClassNotFoundException(serviceName + " not found");
			}
			Method method = serviceClass.getMethod(methodName, parameterTypes);
			Object result = method.invoke(serviceClass.newInstance(), arguments);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}