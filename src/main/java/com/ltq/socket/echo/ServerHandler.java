package com.ltq.socket.echo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerHandler implements Runnable{
    private Socket socket=null;
    public static final String DATE_FORMAT="yyyy-MM-dd hh:mm:ss";
    public ServerHandler(Socket socket){
        this.socket=socket;
    }

    public void run() {

        try {
            SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT);
            System.out.println(Thread.currentThread()+"连接");
            System.out.println(Thread.currentThread()+"等待客户端");          
            String str=null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            //通过while循环不断读取信息，
            while (true) {
                //输出打印
                str = bufferedReader.readLine();
                System.out.println("客户端说：" + str);
                Date date=new Date();
                bufferedWriter.write(format.format(date)+":"+str);
                bufferedWriter.write("\n");
                bufferedWriter.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
