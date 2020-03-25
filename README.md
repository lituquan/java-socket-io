https://www.cnblogs.com/linkenpark/p/11289018.html

### 1.时间服务
    服务在线
    客户端获取时间就结束
    
    服务端
        serversocket开启：
            bind:绑定端口
            accept:监听接受客户端,返回一个socket。为了保持服务，循环监听。
            由于是同步的，所以顺序处理用户，只能一个用户在线。

### 2.复读机服务
    客户端和服务聊天

    多人在线：
        开线程可以异步处理多个用户，一次accpet可以连接一个客户端，绑定一个socket。
        开线程：
            (1)实现Runnable接口
            (2)继承Thread类
        线程池：
            submit 提交Runnable实现类对象

    客户端持续在线：
        【循环监听】用户输入Scanner(Sysytem.io)和服务端的输出流

### 3."模拟qq"
    上面的模式是服务端直接返回消息给客户端，假如要客户端和客户端通信，可以模拟实现qq。

    A:B你好，我是A。 ==> 
        服务端将消息转发给B,所以服务端会记录用户存根，将消息发给对应的人。

    //客户端:socket连接-->输入消息【主线程】-->发送消息
                      -->开线程接收服务端返回的消息  
            所以，每个客户端需要2个线程。          

    //服务端:socket监听-->accept用户连接【主线程】    
                    -->开线程接收消息,转发消息【消息存在消息队列】【每个用户一个线程】
                    -->开线程转发用户消息【从消息队列获取消息】
            所以服务端有2+n个线程。(n为客户端数)        

    //消息格式：
        a&b&login  
    //堵塞队列
        https://www.cnblogs.com/tonyspark/p/3722013.html                  
    离线消息处理：
        消息要储存 
        使用内存、使用redis或者mysql    

3.同步阻塞IO和Nio
    <pre>
    netty：https://www.w3cschool.cn/essential_netty_in_action/essential_netty_in_action-y24z289f.html
    </pre>
4.使用场景--rpc
    反射生成代理

5.注册中心    
