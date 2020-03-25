package com.ltq.timeserver.socket.time;

import java.io.IOException;
import java.net.Socket;

import com.ltq.timeserver.socket.Config;

public class TimeClient {
    public static void main(String[] args) {
        try {

            Socket socket = new Socket(Config.HOST_ADDRESS, Config.PORT);
            new TimeClientHandler(socket).handler();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
