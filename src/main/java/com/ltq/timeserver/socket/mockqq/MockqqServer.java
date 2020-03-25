package com.ltq.timeserver.socket.mockqq;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import com.ltq.timeserver.socket.Config;


public class MockqqServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket();
            SocketAddress endpoint = new InetSocketAddress(Config.PORT);
            serverSocket.bind(endpoint);
            MockqqServerHandler mockqqServerHandler = new MockqqServerHandler();
            new Thread(mockqqServerHandler).start();//处理用户消息
            while(true){
                Socket socket = serverSocket.accept(); //监听用户连接                          
                mockqqServerHandler.listen(socket);//监听用户消息
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        }
    
    }
}

