package com.ltq.socket.time;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class TimeClientHandler {
    private Socket socket = null;

    public TimeClientHandler(Socket socket) {
        this.socket=socket;
    }

    public void handler() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println(Thread.currentThread()+"client");
            bufferedWriter.write("现在几点?"+"\n");
            bufferedWriter.flush();

            String str=null;
            str=bufferedReader.readLine();
            System.out.println(str);
            
            bufferedReader.close();
            bufferedWriter.close();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }

    }
}
