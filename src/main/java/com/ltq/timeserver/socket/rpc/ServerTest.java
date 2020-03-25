package com.ltq.timeserver.socket.rpc;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import com.ltq.timeserver.socket.Config;
public class ServerTest {
    public static void main(String[] args) {
        //注册服务
        RpcHandler.register(Hello.class, HelloImpl.class);
        //启动服务
        try {
            ServerSocket serverSocket = new ServerSocket();
            SocketAddress endpoint = new InetSocketAddress(Config.PORT);
            serverSocket.bind(endpoint);
            while(true){
                Socket socket = serverSocket.accept();
                // 客户端request==>报文==>socket[i,o]
                //==>报文==>request对象
                //==>代理请求方法==>result==>response==>报文
                //==>socket[i,o]==>报文==>客户端对象
                new Thread(new RpcHandler(socket)).start();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        }
    }
}