# Spring Servlet Tomcat

## 在哪里加载的 `servlet、filter、listener`
[SpringBoot内置Servlet容器源码分析 | HealeJean的梦想博客](https://blog.healerjean.com/springboot/2020/06/18/SpringBoot%E5%86%85%E7%BD%AEServlet%E5%AE%B9%E5%99%A8%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%90/)
1. 在 `getSelfInitializer()` 方法里，调用到 `getServletContextInitializerBeans()` 
2. 加载
	1. 调用 `addServletContextInitializerBean` 从 `spring 容器` 中获取 `ServletRegistrationBean`、`FilterRegistrationBean`、`ServletListenerRegistrationBean`
	2. 调用 `addAdaptableBeans`，从 `beanFactory` 获取所有 `Servlet.class` 和 `Filter.class` 类型的 Bean，然后通过 `ServletRegistrationBeanAdapter` 和 `FilterRegistrationBeanAdapter` 两个适配器将 `Servlet.class` 和 `Filter.class` 封装成 RegistrationBean
[Java 类在 Tomcat 中是如何加载的？](https://mp.weixin.qq.com/s?__biz=MzI3ODcxMzQzMw==&mid=2247490906&idx=3&sn=5cdca5f70553470e63668bb696c90167)

## 一个 SpringBoot 项目能处理多少请求？

### maxThreads、maxConnections、acceptCount 的理解

###### BIO
1. tcp 三次握手后进入 accpet 队列，acceptCount 决定了这个队列长度
2. Acceptor 线程死循环
	1. 先判断当前连接数是否大于 maxConnections
	2. 再调用 `SocketChannel=serverSocket.accept()`,成功后将获得 SocketChannel 对象封装在一个 PollerEvent 对象中，并将 PollerEvent 对象压入 Poller 的 Queue 里，这是个典型的生产者 - 消费者模式，Acceptor 与 Poller 线程之间通过 Queue 通信。
3. maxThreads 则是真正并发处理的 socket 数。

[深度解读Tomcat中的NIO模型 - 简书](https://www.jianshu.com/p/76ff17bc6dea)
![](Pasted%20image%2020230825172041.png)

[这篇](https://www.cnblogs.com/thisiswhy/p/17559808.html)是讲的最大工作线程，maxThreads，即同时有 200 个线程处理请求
1. JDK 的线程池，是先使用核心线程数配置，接着使用队列长度，最后再使用最大线程配置。
2. Tomcat 的线程池，就是先使用核心线程数配置，再使用最大线程配置，最后才使用队列长度。
[我们来拉扯一下Spring Boot 最大连接数吧？](https://mp.weixin.qq.com/s/Q1Ap91PtEEUoUGmnxOijpQ)
[图解系列 图解Spring Boot 最大连接数及最大并发数\_一个springboot能支持多少并发\_lakernote的博客-CSDN博客](https://laker.blog.csdn.net/article/details/130957301)

[这篇](https://zhuanlan.zhihu.com/p/609235701)说的是最大连接数 maxConnections+acceptCount 
1. ~~完成三次握手的是 maxConnections~~
2. ~~maxConnections 满了后进入 acceptCount~~
3.  ![200|200](Pasted%20image%2020230824180130.png) ![200|200](Pasted%20image%2020230824180140.png)

[Tomcat的acceptCount、maxConnections与maxThreads参数的含义和关系 - 掘金](https://juejin.cn/post/6997300458461069325)
1. acceptCount、maxConnections 实际上是 tcp 层面的参数
2. 三次握手成功，OS 会把连接从 SYN 队列中移除，并创建一个完整的连接，随后将该连接加入 Accept 队列（Accept Queue）中，Accept 队列是一个全连接队列（因为此时已经真正的建立了 TCP 连接），等待被上层应用程序取走的连接。**当应用程序进程调用 accept ()系统从 accept 队列中获取已经建立成功的连接套接字之后，这个 socket 将移除 Accept 队列。**
3. tomcat 使用 Acceptor 线程专门负责从 Accept 队列中取出 connection，当当前socket连接超过maxConnections的时候，Acceptor线程自己会阻塞等待，等连接降下去之后，才去处理Accept队列的下一个连接)。

[详解tomcat的连接数与线程池 - 编程迷思 - 博客园](https://www.cnblogs.com/kismetv/p/7806063.html)
1. Acceptor 接收 socket 后，不是直接使用 Worker 中的线程处理请求，而是先将请求发送给了 Poller，而 Poller 是实现 NIO 的关键。Acceptor 向 Poller 发送请求通过队列实现，使用了典型的生产者-消费者模式。在 Poller 中，维护了一个 Selector 对象；当 Poller 从队列中取出 socket 后，注册到该 Selector 中；然后通过遍历 Selector，找出其中可读的 socket，并使用 Worker 中的线程处理相应请求。与 BIO 类似，Worker 也可以被自定义的线程池代替。
2. 如何查看服务器中的连接数和线程数。

[tomcat连接数相关的配置 | KL's blog](https://qsli.github.io/2017/04/05/tomcat-connection/)
1. tomcat 的 Acceptor 线程会不停的从系统的全连接队列里去拿对应的 socket 连接，直到达到了 maxConnections 的值。
2. 之后 Acceptor 会阻塞在那里，直到处理的连接小于 maxConnections 的值。如果一直阻塞的话，就会在系统的 tcp 连接队列中阻塞，这个队列的长度是 acceptCount 控制的，默认是 100。如果仍然处理不过来，系统可能就会丢掉一些建立的连接了。
3. 所以，大致可以估计下最多能处理的连接数：acceptCount + maxConnection

[tomcat 的最大连接数\_tomcat连接数\_quliuwuyiz的博客-CSDN博客](https://blog.csdn.net/quliuwuyiz/article/details/79979031)
```java
while (running) {
    ...    
    //if we have reached max connections, wait
    countUpOrAwaitConnection(); //计数+1，达到最大值则等待
 
    ...
    // Accept the next incoming connection from the server socket
    socket = serverSock.accept();
 
    ...
    processSocket(socket);
 
    ...
    countDownConnection(); //计数-1
    closeSocket(socket);
}
```

[从源码角度分析Tomcat的acceptCount、maxConnections、maxThreads参数 - 知乎](https://zhuanlan.zhihu.com/p/451975059)


[你们服务最大的并发量是多少？](https://cloud.tencent.com/developer/article/1890140)
1. 默认设置中，Tomcat 的最大线程数 200，最大连接数 10000
2. 并发量指的是连接数，还是线程数？
	1. 连接数。
3. 200 个线程如何处理 10000 条连接？
	1. BIO
		1. 一个线程只处理一个 Socket 连接
	2. NIO
		1. 一个线程处理多个 Socket 连接。由于 HTTP 请求不会太耗时，而且多个连接一般不会同时来消息，所以一个线程处理多个连接没有太大问题。

[SpringBoot是接到一个http请求就开启一个线程处理吗？](https://mp.weixin.qq.com/s/Itq8G5wEwkEgvW-8HwxWqw)
###### 一个人的三篇文章补充了点信息，但不多且不清晰
1. [Chrome浏览器限制](https://mp.weixin.qq.com/s?__biz=MzI0MDEzODc5MA==&mid=2247484019&idx=1&sn=d1357eb1838696745198a1d75c2ac0f0) WebSocket 的最大连接数就是 256
	1. websocket 也是建立在 tcp 基础上，tcp 限制是 6 个啊，为啥 websocket 是 256？
	2. claude 回答默认 6 个，可以通过修改实验特性 `increase websocket connection limit` 提高到 256
2. [从源码剖析SpringBoot中Tomcat的默认最大连接数](https://mp.weixin.qq.com/s/CmLpfCvE3M1tPAUcIPC-vQ)
	1. 从源码找到 maxConnections 配置
3. [SpringBoot默认200个线程对于Websocket长连接够用吗？（一）](https://mp.weixin.qq.com/s/WZIGE0wj6yFwmvA3x5XD8Q)
	1. 配置了 maxThreads=1，解释了 Http11NioProtocol
	2. 那么一个线程能最多处理多少个请求呢？如果处理的不多的话，默认200个线程不是还是不够用吗？
4. 并没有第二篇解答

###### accpet 队列和 accept 函数
![](https://image.cnxct.com/2015/06/tcp-sync-queue-and-accept-queue-small.jpg)



## tomcat in springboot
[Spring Boot 内置与外置Servlet容器讲解（六）\_开源项目LibraPlatform的博客-CSDN博客](https://blog.csdn.net/qq_33524158/article/details/79749755)
[SpringBoot源码分析之内置Servlet容器 | Format's Notes](https://fangjian0423.github.io/2017/05/22/springboot-embedded-servlet-container/)
[Tomcat在SpringBoot中是如何启动的 - 掘金](https://juejin.cn/post/6844903910784630792)
[Spring Boot如何加载tomcat | TechLiving](http://loveruby.top/2018/03/21/Spring%20Boot%E5%A6%82%E4%BD%95%E5%8A%A0%E8%BD%BDtomcat/)

[在使用 SpringMVC 时，Spring 容器是如何与 Servlet 容器进行交互的？](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247489479&idx=2&sn=e834aa2c829670384f78e1dc972c7c50)
## 请求流程
[谈谈 Tomcat 请求处理流程](https://github.com/c-rainstorm/blog/tree/master/tomcat)

[Tomcat处理请求流程 - wansw - 博客园](https://www.cnblogs.com/wansw/p/10244039.html)

[一个请求的过程](一个请求的过程.md)
## reference
[Spring Boot Servlet\_catoop的博客-CSDN博客](https://blog.csdn.net/catoop/article/details/50501686)

[从Spring到SpringBoot构建WEB MVC核心配置详解 - jimisun - 博客园](https://www.cnblogs.com/jimisun/p/10084461.html)

[Spring Filter深度解析 - 简书](https://www.jianshu.com/p/61b248d5b87a)
