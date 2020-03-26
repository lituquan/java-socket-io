package com.ltq.socket.mockqq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MockqqClientHandler {
    private Socket socket = null;

    public MockqqClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void handler() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));           
            Scanner sc=new Scanner(System.in);
            new Thread(new ReaderMessage(socket)).start();            
            while (true) {                
                System.out.print("client>>>");
                String result =  sc.nextLine() ;
                bufferedWriter.write(result+"\n");
                bufferedWriter.flush();
            }
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }

    }

}

class ReaderMessage implements Runnable{
    BufferedReader bufferedReader = null;
    public ReaderMessage(Socket socket){
        try {            
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            this.bufferedReader=bufferedReader;
        } catch (Exception e) {
            //TODO: handle exception
        }
    
    }
    public void run() {
        String str = null;
        try {
            while(true){
                str = bufferedReader.readLine();
                System.out.println(str);	
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
