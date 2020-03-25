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
        socket服务：使用socket 或者iostream或者reader/writer做存根。demo中使用读写器。
        netty服务：使用channel 做存根。

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
    离线、失败消息处理：
        消息要储存、失败重试
        使用内存、使用redis或者mysql    

#### mockqq 测试：
    开启服务、客户端1、客户端2
    (1)登录:
        客户端1： a&xx&login
        客户端2： b&xx&login

    (2)发送消息：
        客户端1：a&b&hello
        客户端b：b&a&hello

#### mockqqnetty:
        (1)服务端    
            //客户端channel 存根
            public static final Map<String ,Channel> cMap=new HashMap();

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                接收消息
                登录消息==>记录用户channel
                其他消息==>转发消息
            }
        (2)客户端

            while(true){
                Scanner sc=new Scanner(System.in);
                ByteBuf message = Unpooled.buffer(1024);
                System.out.print("client>>");  			      
                String nextLine = sc.nextLine(); //stdin.input
                message.writeBytes(nextLine.getBytes(CharsetUtil.UTF_8));
                ch.writeAndFlush(message);
            }
        
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                输出服务消息【服务器转发其他用户发过来的消息】      
            }
### 4.同步阻塞IO和Nio
    <pre>
    netty：https://www.w3cschool.cn/essential_netty_in_action/essential_netty_in_action-y24z289f.html
    https://www.cnblogs.com/luoxn28/p/11810710.html

    netty 
    服务端：
        boot.group(bossGroup, workerGroup)  //指定master组和工作组
        .channel(NioServerSocketChannel.class)
        .localAddress(Config.PORT) //设置绑定的端口
        .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new EchoServerHandler());//消息处理器，可以有多个
            }
        });
   
    服务处理器：
        继承 ChannelInboundHandlerAdapter

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
           读取msg消息
           处理【echo 里面是加上时间前缀返回】
           ctx 再写消息
        }
    
    客户端：
        b.group(group) //指定工作组
        .channel(NioSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY, true)
        .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                //p.addLast(new LoggingHandler(LogLevel.INFO));
                p.addLast(new EchoClientHandler());//指定处理器
            }
        });
        ChannelFuture f = b.connect(Config.HOST_ADDRESS, Config.PORT).sync();//连接指定地址服务

    客户端处理器：
        继承 ChannelInboundHandlerAdapter

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
           读取msg消息
           处理【读取stdin作为消息】
           ctx 再写消息
        }
    </pre>


4.使用场景--rpc
    反射
    java 序列化
    动态代理

    https://www.cnblogs.com/zyl2016/p/9875593.html
    https://www.cnblogs.com/zyl2016/p/9875593.html
    
5.注册中心    
