package com.ltq.socket.rpc.test;

import com.ltq.socket.Config;
import com.ltq.socket.rpc.RpcHandler;
import com.ltq.socket.rpc.RpcServerStater;


//客户端request==>报文==>socket[i,o]==>报文==>服务端请求方法==>result==>报文
//==>socket[i,o]==>报文==>客户端对象
public class ServerTest {
    public static void main(String[] args) {
        //注册服务
        RpcHandler.register(Hello.class, HelloImpl.class);
        //启动服务
        new RpcServerStater(Config.PORT).start();
    }
}