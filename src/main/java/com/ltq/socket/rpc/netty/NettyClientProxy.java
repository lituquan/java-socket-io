package com.ltq.socket.rpc.netty;

import com.ltq.socket.rpc.code.Request;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;


public class NettyClientProxy<T> implements InvocationHandler {
    private RpcClientHandler ch;
  
    public NettyClientProxy(String ip, int port) {
        try {
            this.ch = new RpcClientHandler(ip, port);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //反射的原理：生成继承proxy的子类字节码,传递一个InvocationHandler对象
    //serviceInterface 调用方法时,
    public T getClientIntance(Class<T> serviceInterface){    
        return (T) Proxy.newProxyInstance (serviceInterface.getClassLoader(),
        		new Class<?>[]{serviceInterface},
        		this);
    }
	public byte[] toByteArray (Object obj) {      
        byte[] bytes = null;      
        ByteArrayOutputStream bos = new ByteArrayOutputStream();      
        try {        
            ObjectOutputStream oos = new ObjectOutputStream(bos);         
            oos.writeObject(obj);        
            oos.flush();         
            bytes = bos.toByteArray ();      
            oos.close();         
            bos.close();        
        } catch (IOException ex) {        
            ex.printStackTrace();   
        }      
        return bytes;    
    }   
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request req=new Request();
        req.setMessageId(UUID.randomUUID().toString());
        req.setClassName(method.getDeclaringClass().getName());
        req.setMethodName(method.getName());
        req.setParameters(args);
        req.setTypeParameters(method.getParameterTypes());
            
        return  ch.send(req);
    }

}