package com.ltq.socket.rpc.test;

import com.ltq.socket.Config;
import com.ltq.socket.rpc.netty.NettyRpcServer;
import com.ltq.socket.rpc.netty.RpcServerHandler;


//客户端request==>报文==>socket[i,o]==>报文==>服务端请求方法==>result==>报文
//==>socket[i,o]==>报文==>客户端对象
public class NettyServerTest {
    public static void main(String[] args) {
        //注册服务1
    	RpcServerHandler.register(Hello.class, HelloImpl.class);
        //注册服务2
    	RpcServerHandler.register(Counter.class, CounterImpl.class);
        //启动服务
        new NettyRpcServer(Config.PORT).start();
        
    }
}