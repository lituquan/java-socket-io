package com.ltq.socket.time;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import com.ltq.socket.Config;

public class TimeServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket();
            SocketAddress endpoint = new InetSocketAddress(Config.PORT);
            serverSocket.bind(endpoint);
            while(true){
                Socket socket = serverSocket.accept();
                TimeServerHandler.handler(socket);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        }
    
    }
}

