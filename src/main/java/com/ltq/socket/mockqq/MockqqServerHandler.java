package com.ltq.socket.mockqq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MockqqServerHandler implements Runnable{
    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    
    final BlockingQueue<Message> queue = new ArrayBlockingQueue(10);//消息队列
    final Map<String,BufferedWriter> wMap=new HashMap();//存储用户输出流
    
    public void run() {
        // 消费消息
        Message msg;
        try {
            while (true) {
                msg = queue.take();//消费者
                System.out.println("server get msg:"+msg.getString());
                BufferedWriter writer=wMap.get(msg.to);  
                if(writer==null){
                	System.out.println("用户"+msg.to+"不在线");
//                	queue.put(msg); //消息重试
                	continue;
                }

                System.out.println("用户"+msg.to+"在线");
                writer.write("from server:"+msg.getString()+"\n");
                writer.flush();           
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen(final Socket socket) {   
        new Thread(new Runnable(){        
            public void run() {
                //监听用户消息
                try {
                   BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                   final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                   while(true){
                	   String str = bufferedReader.readLine();
                       System.out.println("客户端:"+str);
                       Message m=new Message(str);
                       if(m.content.equals("login")){
                    	   wMap.put(m.from, bufferedWriter);
                    	   System.out.println("用户"+m.from+"登录");                    	   
                       }
                       queue.put(m); //生产者
                   }                   
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
