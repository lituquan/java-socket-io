package com.ltq.socket.rpc.test;

import com.ltq.socket.Config;
import com.ltq.socket.rpc.netty.NettyClientProxy;

public class NettyRpcClient {
	public static void main(String[] args) {
		NettyClientProxy proxy=new NettyClientProxy(Config.HOST_ADDRESS, Config.PORT);
		Hello clientIntance = (Hello) proxy.getClientIntance(Hello.class);
		System.out.println(clientIntance.add(100, 50));
	}
}
