package com.ltq.timeserver.socket.echo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler{
    private Socket socket = null;

    public ClientHandler(Socket socket) {
        this.socket=socket;
    }

    public void handler() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println(Thread.currentThread()+"client");
            Scanner sc=new Scanner(System.in);
            while (true) {                
                System.out.print("client>>>");
                String result =  sc.nextLine() ;
                bufferedWriter.write(result+"\n");
                bufferedWriter.flush();

                String str=null;
                str=bufferedReader.readLine();
                System.out.println(str);
            }
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }

    }

}
