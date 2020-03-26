package com.ltq.socket.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ltq.socket.Config;

public class EchoServer {
    public static void main(String[] args) {
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        try {
            ServerSocket serverSocket = new ServerSocket();
            SocketAddress endpoint = new InetSocketAddress(Config.PORT);
            serverSocket.bind(endpoint);
            while(true){
                Socket socket = serverSocket.accept();
                //开线程
                //new Thread(new ServerHandler(socket)).start();
                //线程池
                newCachedThreadPool.submit(new ServerHandler(socket));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        }
    
    }
}
