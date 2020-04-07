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
    java 序列化、对象流
    动态代理
    tcp/socket

    服务端：
        (1)开端口接收连接
            RpcServerStater start
        (2)监听输入流:
            RpcHandler 线程run:
              读报文=>接口、方法、参数、参数类型【反序列化】
        (3)反射Method调用invoke执行远程对象的方法  
              反射invoke得到result=>写报文【序列化】          
        (4)写入输出流 

    客户端：
        (1)连接
        (2)获取代理接口的对象【动态代理】
        (3)调用方法,发送请求           
        (4)接收响应
            接口、方法、参数、参数类型=>写报文【序列化】
            读报文=>result对象【反序列化】

    测试：
        服务端设置端口，【注册】服务接口和远程对象，开启服务。
        客户端连接，获取接口【代理】对象，执行方法。

    https://www.cnblogs.com/zyl2016/p/9875593.html
   
  netty rpc 
    https://gitee.com/huangyong/rpc 
    https://www.cnblogs.com/zeroone/p/8490930.html

    客户端->proxy.invoke->客户端req 编码 ->传输->服务端req解码->处理请求
    ->服务端response 编码->传输->客户端response解码->结果返回

    编码解码要用到序列化和反序列化
    传输报文【字节长度,字节】
    
5.注册中心   
    (1)服务注册
        当服务掉线了,要通知切换地址【服务上下线】
    (2)服务发现


zk:(1)配置中心、命名服务 (2)服务上下线 (3)软负载均衡 (4)分布式锁
    https://www.jianshu.com/p/74e4d91a1886

redis:(1)缓存 (2)排行榜
    https://www.cnblogs.com/shiqi17/p/9581752.html    