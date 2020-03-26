package com.ltq.socket.time;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServerHandler {
    public static final String DATE_FORMAT="yyyy-MM-dd hh:mm:ss";
    public static void handler(Socket socket) {
        try {
            SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT);
            System.out.println(Thread.currentThread()+"连接");   
            String str=null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //输出打印
            str = bufferedReader.readLine();
            System.out.println("客户端说：" + str);
            Date date=new Date();
            bufferedWriter.write("现在是北京时间:"+format.format(date));
            bufferedWriter.write("\n");
            bufferedWriter.flush();

            bufferedReader.close();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}