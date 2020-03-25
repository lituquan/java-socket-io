package com.ltq.timeserver.socket.rpc;

import com.ltq.timeserver.socket.Config;

public class ClientTest {
    public static void main(String[] args) {
        Hello hello= (Hello) new ClientProxy(Hello.class, Config.HOST_ADDRESS, Config.PORT).getClientIntance();
        System.out.println( hello.add(1,2));
    }
}