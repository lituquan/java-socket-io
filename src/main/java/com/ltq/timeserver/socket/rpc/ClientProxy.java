package com.ltq.timeserver.socket.rpc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class ClientProxy<T> implements InvocationHandler {

    private Class<T> serviceInterface;
    private Socket socket;
        
    ObjectInputStream in = null;
    ObjectOutputStream out = null;

    public ClientProxy(Class<T> serviceInterface, String ip, int port) {
        this.serviceInterface = serviceInterface;
        try {
            this.socket = new Socket(ip, port);            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public T getClientIntance(){
        return (T) Proxy.newProxyInstance (serviceInterface.getClassLoader(),new Class<?>[]{serviceInterface},this);
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {    
        out=new ObjectOutputStream(socket.getOutputStream());      
        out.writeUTF(serviceInterface.getName());
        out.writeUTF(method.getName());
        out.writeObject(method.getParameterTypes());
        out.writeObject(args);       
        out.flush();
        System.out.println("wait for server");

        in=new ObjectInputStream(socket.getInputStream());
        return in.readObject();
    }

}