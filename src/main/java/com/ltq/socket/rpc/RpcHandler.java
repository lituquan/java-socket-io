package com.ltq.socket.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.HashMap;

public class RpcHandler implements Runnable {
    private Socket socket=null;
    private static final HashMap<String, Class<?>> serviceRegistry = new HashMap();

    public static void register(Class serviceInterface, Class impl) {
        serviceRegistry.put(serviceInterface.getName(), impl);
    }
    public RpcHandler(Socket socket){
        this.socket=socket;
    }

    public void run() {

        try {
            //通过while循环不断读取信息，
            while (true) {
                //读报文
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                //转对象
                //接口,方法,参数
                String serviceName = input.readUTF();
                String methodName = input.readUTF();
                System.out.println (methodName);
                Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                Object[] arguments = (Object[]) input.readObject();
                //代理方法
                Class serviceClass = serviceRegistry.get(serviceName);
                if (serviceClass == null) {
                    throw new ClassNotFoundException(serviceName + " not found");
                }
                Method method = serviceClass.getMethod(methodName, parameterTypes);
                Object result = method.invoke(serviceClass.newInstance(), arguments);               
                //转报文
                //写报文
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(result);             
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}

class ServiceInvocation  implements InvocationHandler {

    private Object target;

    public ServiceInvocation(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("判断用户是否有权限进行操作");
        Object obj = method.invoke(target);
        System.out.println("记录用户执行操作的用户信息、更改内容和时间等");
        return obj;
    }
}