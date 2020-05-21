package com.ltq.socket.rpc.test;

import com.ltq.rpc.register.ServiceRegistry;
import com.ltq.rpc.register.zookeeper.ZooKeeperServiceDiscovery;
import com.ltq.socket.Config;
import com.ltq.socket.rpc.netty.NettyClientProxy;
/*
 *
 * 组合spring:
 * 	注册中心可以在spring中加载,代理类可以直接在spring中生成
 * 对应dubbo 中的：
 * 	提供注册中心地址,@Reference可以根据接口名获取代理
 * 
 * 服务更新和服务下线通知
 * 服务降级
 * 服务限流
 */
public class NettyRpcClient {
	public static void main(String[] args) {
		//连接注册中心
		String zkAddress="127.0.0.1:2181";
		ServiceRegistry serviceRegistry0=new ZooKeeperServiceDiscovery(zkAddress);
		//服务发现：假设当前只有一个服务地址  ip：port
		//这里可以做客户端负载均衡
		String discover = serviceRegistry0.discover(Hello.class.getName());
		
		//服务代理对象
		NettyClientProxy proxy=new NettyClientProxy(discover);
		Hello clientIntance = (Hello) proxy.getClientIntance(Hello.class);
				
		//服务调用
		System.out.println(clientIntance.add(100, 50));
	}
}
