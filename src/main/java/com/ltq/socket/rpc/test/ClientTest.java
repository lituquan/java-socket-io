package com.ltq.socket.rpc.test;

import com.ltq.socket.Config;
import com.ltq.socket.rpc.ClientProxy;

public class ClientTest {
    public static void main(String[] args) {
    	ClientProxy cp=new ClientProxy(Config.HOST_ADDRESS, Config.PORT);    	
        Hello hello= (Hello)cp.getClientIntance(Hello.class);
        System.out.println( hello.add(1,2));
    }
}