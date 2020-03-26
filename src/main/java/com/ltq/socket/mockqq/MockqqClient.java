package com.ltq.socket.mockqq;

import java.io.IOException;
import java.net.Socket;

import com.ltq.socket.Config;

public class MockqqClient {
    public static void main(String[] args) {
        try {

            Socket socket = new Socket(Config.HOST_ADDRESS, Config.PORT);
            new MockqqClientHandler(socket).handler();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
