# Tomcat

## 连接

### 连接数

![Spring Servlet Tomcat](Spring%20Servlet%20Tomcat.md#一个%20SpringBoot%20项目能处理多少请求？)

### keepAlive 长连接
##### 问题
1. http 1.1, 一个请求响应后会关闭一个 tcp 连接吗? 什么决定关闭一个 maxConnections 的连接？
	1. [keepalive连接复用对tomcat线程池的影响 - 简书](https://www.jianshu.com/p/de9610142791)
		1. tomcat 的默认 maxKeepaliveTimeout 是 60 s，另外一个控制长连接能保持多久的参数是 maxKeepAliveRequests 默认是 100，可以通过调试客户端程序配合 netstat 命令查看 tcp 连接，**当启动一个客户端向服务端发送 100 次请求之前或发送完一个请求之后的 60 秒内，客户端与服务端之间的连接一直是同一个**
	2. [HTTP keep-alive 二三事 | 三点水](https://lotabout.me/2019/Things-about-keepalive/)
		1. 在一些 TPS/QPS 很高的 REST 服务中，如果使用的是短连接（即没有开启 keep-alive），则很可能发生**客户端端口被占满**的情形。这是由于短时间内会创建大量 TCP 连接，而在 TCP 四次挥手结束后，客户端的端口会处于 TIME_WAIT 一段时间(`2*MSL`)，这期间端口不会被释放，从而导致端口被占满。这种情况下最好使用长连接。
		2. 服务端被保留的连接，超时时间之后，会在 `NioEndpoint#Poller#timeout` 方法中被关闭
2. keepalive 避免客户端主动断开端口占满情况，避免了服务端主动断开 TIME_WAIT 数量，在 wireshark 抓包中也发现，chrome 和 postman 多个 tcp 连接的端口是不一样的。为什么呢？
	1. ![200|200](Pasted%20image%2020230824170722.png)![200|200](Pasted%20image%2020230824170744.png)


[Tomcat NIO(15)-长连接](https://mp.weixin.qq.com/s?__biz=MzI0MDE3MjAzMg==&mid=2648393722&idx=1&sn=f6299b1915fd9833532d01008e829c32)
- tomcat 默认就是开启长连接的。
- 对于 http 1.0 协议不使用长连接。
- 如果请求头中 Connection 的值为 keep-alive 则使用长连接，为 close 则关闭 socket 不使用长连接。
- tomcat 每个长连接默认支持 100 个请求，如果超过则关闭 socket 停止当前长连接，不过在后续新的连接里还是继续支持长连接。
- 对于每个长连接 tomcat 会在[以前文章](http://mp.weixin.qq.com/s?__biz=MzI0MDE3MjAzMg==&mid=2648393626&idx=1&sn=36cf6e3db8d31920a908afa8ce70bd38&chksm=f1310af5c64683e3b7875f73a1a46ee0e667ad7c4507b4ea74f2468c8645eefdb0c903f9702c&scene=21#wechat_redirect)介绍的 poller 线程中检查是否有读写超时，默认读写超时时间均为 1 分钟，如果 1 分钟之内没有读写操作，那么关闭 socket 停止当前长连接。
- 对于上面没有读写操作关闭长连接的情况不仅仅适用于 http 协议，还适用于其它基于 tcp 的协议，因为这个是基于原始 socket 的检测，例如 websocket 协议。 只是对于 websocket 协议来说服务器设置的默认读写超时时间为-1，即不会超时，所以实现了该协议的长连接。当然对于 websocket 协议来说本身也有 ping/pong 定义来实现 keeplive 。

[浅析keepalive在Tomcat中的实现原理（之一）](https://mp.weixin.qq.com/s/zbAf1Sebr2gaKJb1yYXLXQ)
1. TIME_WAIT
	1. 使用 http keep-alive，可以**减少服务端 TIME_WAIT 数量** (因为由服务端 httpd 守护进程主动关闭连接)。道理很简单，相较而言，启用 keep-alive，建立的 tcp 连接更少了，自然要被关闭的 tcp 连接也相应更少了
	2. 就是 TIME_WAIT 可以保证 TCP 的实现可靠的，而 keepalive 的设置可以让服务器端主动关闭减少，从而减少了这个状态，这样就会极大的提升服务器的吞吐量。
[浅析keepalive在Tomcat中的实现原理（之二）](https://mp.weixin.qq.com/s/2slLAkxqAXqEJMVyx29ivA)

[http 的 keep-alive 和 TCP 的 Keepalive](http.md#http%20的%20keep-alive%20和%20TCP%20的%20Keepalive%20TCP%20md%20KeepAlive)


[Tomcat对keep-alive的实现逻辑](https://mp.weixin.qq.com/s/M1Ge1wX_7bhxDdRhRHjBUQ)
1. 对于keep-alive情况下处理下一次请求，NIO跟APR类似，线程不会一直阻塞在socket上。对于header的处理，NIO也同样不会阻塞，只有在body的读取时，NIO采取模拟阻塞的方式
2. 线程并不是阻塞在原生的 IO 方法上，而是 `NioBlockingSelector.read` 方法上，这个方法从名字就可以看出它用 NIO 实现的阻塞式 selector (里面的 read 和 write 方法注释也有明确说明)；相当于通过锁的方式来模拟阻塞方式，正如之前表格里红色字体突出的。
3. 为什么 NIO 在读取 body 时要模拟阻塞？
	1. 跟[异步 Servlet t](Tomcat.md#异步%20Servlet)关联起来了
4. tomcat 的 NIO 完全可以以非阻塞方式处理 IO，为什么在读取 body 部分时要模拟阻塞呢？这是因为 servlet 规范里定义了 `ServletInputStream` 在读数据时是阻塞模式，这里相关的争论可以 google
5. 在servlet3.0里引入了异步，但仅针对传统IO，对应用来说仍有很多限制，所以servlet3.1又引入了非阻塞IO，但这要tomcat8才提供了。


## 架构
[聊聊Tomcat的架构设计](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247486483&idx=2&sn=88bb307db66077492425591a1f307c6b)

[初探Tomcat的架构设计](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247489328&idx=2&sn=182472f9368d646bf49bd10074695c84)

[Tomcat 架构原理解析到架构设计借鉴](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247491380&idx=2&sn=3a0465a4274545aeb2fa7e3b2df1839f)

[Tomcat源码分析 | 一文详解生命周期机制Lifecycle](https://mp.weixin.qq.com/s/5P2dloZS8RHRJtyJqc8q9w)
[当Tomcat遇上Netty](https://mp.weixin.qq.com/s?__biz=Mzg3MjA4MTExMw==&mid=2247489458&idx=2&sn=b2a047493c9e46a124eb97da72a411a3)
[手写4个mini版的tomcat！](https://mp.weixin.qq.com/s/gu49D6O4LhcoZlHoOg5A8A)

## NIO

### 异步 Servlet
##### 理解
1. 3.0 的 AsyncContext 将处理 request、response 的 tomcat 线程 与业务线程分离。
2. 3.1非阻塞 I/O 解决的是读写 request、response。

[Servlet3异步原理与实践](https://mp.weixin.qq.com/s/9nfKMOJRxKL756EHdL4reQ)
1. 接收到 request 请求之后，由 tomcat 工作线程从 HttpServletRequest 中获得一个异步上下文 AsyncContext 对象，
2. 然后由 tomcat 工作线程把 AsyncContext 对象传递给业务处理线程，
3. 同时 tomcat 工作线程归还到工作线程池，这一步就是异步开始。
4. 在业务处理线程中完成业务逻辑的处理，生成 response 返回给客户端。
5. 在 Servlet 3.0 中虽然处理请求可以实现异步，但是 InputStream 和 OutputStream 的 IO 操作还是阻塞的，当数据量大的 request body 或者 response body 的时候，就会导致不必要的等待。
6. 区分 Tomcat NIO Connector ,Servlet 3.0 Async, Spring MVC Async 的关系
7. 根据 Servlet 3.1 规范中的描述”非阻塞 IO 仅对在 Servlet 中的异步处理请求有效，否则，当调用 ServletInputStream. setReadListener 或 ServletOutputStream. setWriteListener 方法时将抛出 IllegalStateException“。可以说 Servlet 3 的非阻塞 IO 是对 Servlet3异步的增强。


[关于Spring DeferredResult的作用和Servlet 3的异步处理请求 | Zollty's Blog](http://blog.zollty.com/b/archive/about-spring-deferredresult-and-servlet3-async-request.html)
1. 从线程池取到的线程 wait 阻塞后，是不会从线程池释放的，也就是说，wait 到期或者被唤醒时，仍然是原来那个线程获得资源继续执行。线程 wait 之后，不会回归到线程池被被再次使用，wait 结束后也不会换其他线程来继续执行任务。 ^kj7f2g

[Servlet 3.0/3.1 中的异步处理 - 无知者云 - 博客园](https://www.cnblogs.com/davenkin/p/async-servlet.html)
1. 在 Servlet 3.0 中，我们可以从 HttpServletRequest 对象中获得一个 AsyncContext 对象，该对象构成了异步处理的上下文，Request 和 Response 对象都可从中获取。
2. AsyncContext 可以从当前线程传给另外的线程，并在新的线程中完成对请求的处理并返回结果给客户端，初始线程便可以还回给容器线程池以处理更多的请求。
3. 如此，通过将请求从一个线程传给另一个线程处理的过程便构成了 Servlet 3.0 中的异步处理。
4. 先通过 request.startAsync ()获取到该请求对应的 AsyncContext，然后调用 AsyncContext 的 start ()方法进行异步处理，处理完毕后需要调用 complete ()方法告知 Servlet 容器。start ()方法会向 Servlet 容器另外申请一个新的线程（可以是从 Servlet 容器中已有的主线程池获取，也可以另外维护一个线程池，不同容器实现可能不一样），然后在这个新的线程中继续处理请求，而原先的线程将被回收到主线程池中。事实上，这种方式对性能的改进不大，因为如果新的线程和初始线程共享同一个线程池的话，相当于闲置下了一个线程，但同时又占用了另一个线程。
	1. 按[这篇](WEB即时通讯.md#^mjemwl)说法，tomcat 应该是使用了自己的同一个处理线程池

[2.13 Servlet3.1新增的非阻塞式IO | 蓝蓝站点](https://lanlan2017.github.io/JavaReadingNotes/8dcbd8d1/)
1. 非阻塞 IO 代码示例种，并未开启新线程，且提到了这样 `Servlet` 就可以继续向下执行, 不会因为 `IO` 阻塞线程。
2. 那么这效果岂不是跟 `AsyncContext+自定义线程` 一样？
	1. 不一样，无论是 Tomcat 的线程池还是自定义，读取 IO 都阻塞，非阻塞 IO 是解决这个问题的。

[1.异步Servlet - 简书](https://www.jianshu.com/p/7de94c4fcadd)
1. 业务线程与容器线程独立，**但是对外输入输出过程依然是同步的**，如果接收和发送大量数据且网络较慢，容器线程还是会 block 在 IO 处理上。
	1. 跟上边文章的图对应上了
2. 虽然 servlet 3.0 的执行过程可以是异步的了，但是从 request 读取和向 response 写入的过程，依然是同步的 IO。所以在此基础上，servlet 3.1 引入了 non-blockingio，提供 WriteListener、ReadListener 方式，以解决“容器线程可能被慢网络阻塞”的问题。
3. ReadListener 和 WriteListener 会在 IO 准备好时，被容器线程调用;
4. 示例代码展示的是从 request 读，是 **对外**，而 `AsyncContext+自定义线程` 感觉是 **对内**。
5.  使用SpringMVC的方式使用Servlet3.x的异步

[Java技术域中的异步编程](https://chinalhr.github.io/post/java-asyncprogram/)
1. Servlet 3.0 规范让 Servlet 的执行变为了异步，但是其 IO 还是阻塞式的（从 ServletInputStream 中读取请求体时是阻塞的）。
2. 为此在Servlet3.1规范中提供了非阻塞IO处理方式，（当内核支持）Servlet3.1允许我们在ServletInputStream上通过函数setReadListener注册一个监听器，该监听器在发现内核有数据时才会进行回调处理函数。


[[tomcat]源码简析 异步/非阻塞和请求构成 ]( https://www.cnblogs.com/mianteno/p/10780257.html )
1. tomcat 8 底层已经默认使用 NIO 了，不是已经是 IO 非阻塞了吗，怎么又说 servlet 3.1 解决了非阻塞。 
	1. 由于在Http11Processor中已经利用输入缓冲装置将消息行和消息头解析到request对象中了，在开发者自定义的servlet中request.getInputStream().read实际是在读取消息body。
	2. tomcat 中 NIO 解决的 IO 阻塞是针对消息行和消息头而言的，对于消息体的读取仍然是 IO 阻塞的。
	3. 那么 servlet 3.1 又是如何解决这个问题的呢？
		1. 在开启了异步的场合，当Has all of the request body been read的场合，则触发开发者添加的读取监听器ReadListener中的onAllDataRead方法。
2. 关于异步，如果开发者在 serlvet 中开一个业务线程来实现，也算异步，为什么 3.0 还提供了一个组件来解决，那么这种方式和开发者自己开个线程去执行在表现上又有什么差异，该组件在 tomcat 底层又是如何流转的。  
	1. 开发者在自定义的 servlet 中开启了一个线程来执行业务，但是由于响应结果是依赖该业务的输出结果的，那么必须在在自定义的 serlvet 中阻塞等待业务线程的输出，这个期间 tomcat 的线程是始终得不到释放的。但是如果我们改用 tomcat 的开启异步，情况就完全不一样了，在业务线程执行期间，tomcat 线程已经释放了，可以去执行其他请求了。等到业务线程执行完毕，同步输出响应结果。
	
[Tomcat-BIO与NIO的区别 - 知乎](https://zhuanlan.zhihu.com/p/400163446)

[Servlet 3.1 Async IO](https://github.com/chanjarster/web-async-learn/blob/master/servlet-async-io/README.md)
1. 当容器发现可以读取到新数据的时候，再分配一个 Http thread 去读 InputStream

[Tomcat中的Servlet异步是怎么实现的？](https://mp.weixin.qq.com/s/Mx5sWYpKoOHDVswto-NMVg)
1. 从 tomcat 源码分析了异步 Servlet 的实现

[Servlet3.1的非阻塞ReadListener](https://mp.weixin.qq.com/s/6_icL-Fs0YbQUPB6fJGVJw)

### 请求响应解析

#### 读写请求体

[Tomcat7 request line(请求行)源码解析 - 简书](https://www.jianshu.com/p/62b0318a9b24)
1. tomcat buffer 关系
2. SocketBuffer


[Tomcat7 Header 解析 源码分析 - 简书](https://www.jianshu.com/p/b36acecc8ec8)
[Tomcat http Body 解析源码分析 - 简书](https://www.jianshu.com/p/d8a2bc7d3c21)
[Tomcat 写操作原理分析--上篇 - 简书](https://www.jianshu.com/p/34d02ef0ebab)

[Tomcat源码篇-解析请求体 - 简书](https://www.jianshu.com/p/c15285eb78b7)
#### TomcatNIO 阻塞读请求体、阻塞写请求头请求体

[keepalive连接复用对tomcat线程池的影响 - 简书](https://www.jianshu.com/p/de9610142791)
1. tomcat NIO 模式下，worker 线程在一次 request 的读写过程中是 blocking 的，但是一次 request 读写完成之后，等待下次 request 是 unblocking 的。
2. 等待下一次从 socket 连接过来的请求的时候是把 socket 注册到 Poller 的 selector 上的，这个时候它是非阻塞的，而读一个具体的 request body 的时候，如果 body 未读完则使用 CountDownLatch 阻塞当前线程等待 BlockPoller 通知继续读。
3. Poller 每次提交给 worker 线程池的 task 类是 SocketProcessor，名字起的有些误导人，事实上提交给线程池处理的不是“一个连接”而应该理解为“一次请求”，同一个连接上可以有多个请求，每个请求可能会分配给不同的 worker 线程去处理，但是每个请求的 body 是始终由一个线程处理的。

以及这里说的，那么就是说一个请求体未读完会阻塞当前线程，等能读时当前线程再重新读。**这里的线程应该是 Tomcat 的 IO 线程，那自定义线程能优化吗？**
![Tomcat](Tomcat.md#^kj7f2g)

[Tomcat 线程模型详解 - Mr·Zh](https://zzcoder.cn/2020/08/30/Tomcat-%E7%BA%BF%E7%A8%8B%E6%A8%A1%E5%9E%8B%E8%AF%A6%E8%A7%A3/)
1. NIO 模式
	1. Read Request Body/Write Response Headers and Body 是阻塞的
	2. Read Request Headers/Wait for next Request 是非阻塞的
2. 既然使用 NIO 模式为什么还要阻塞的读写数据呢？
	1. 因为 Tomcat 是 Servlet 容器，Read Request Body/Write Response Headers and Body 是由 ServletInputStream 和 ServletOutputStream 定义的，而在 Servlet 3.1 之前的规范中 ServletInputStream 和 ServletOutputStream 是阻塞的。
3. 
4. Tomcat 的 I/O 读写和业务阻塞均在同个 Servlet 线程，这在业务阻塞时间较长时很容易出现 Tomcat 的 Worker 线程池任务堆积（默认最大 200 个线程），无法支撑高并发场景。而在 Servlet 3.0 开始支持了 Servlet 异步接口
5. 通过异步模式可以将 I/O 读写和业务阻塞分离以达到复用 Worker 线程的作用。但是此时 InputStream 和 OutputStream 的 I/O 操作仍然是阻塞的，因此无论将 I/O 操作放在 Servlet 线程还是自定义线程，这总是会导致对应线程无用的等待，因此在 Servlet 3.1 开始支持非阻塞 I/O，通过非阻塞 I/O 才解决了读写时线程资源浪费的问题。
	1. **这里的非阻塞 IO 指的是什么？跟 Tomcat NIO 啥关系？
	2. `无论将 I/O 操作放在 Servlet 线程还是自定义线程` 和 `内核有数据时才会进行回调处理函数` 说明，读取请求体的操作放在哪个线程池都没关系，非阻塞 IO 主要解决了当前读写线程不阻塞。
6. 结合异步模式和非阻塞 I/O，Tomcat 就真正既解决了“一个连接一个请求”的问题，又解决高并发下业务阻塞会影响到读写线程的问题
![WEB即时通讯](WEB即时通讯.md#^mjemwl)
==思考==
这里说的自定义业务线程池应该就是上边 `通过异步模式可以将 I/O 读写和业务阻塞分离以达到复用 Worker 线程的作用`，但是 IO 读写仍是阻塞。那么还是有疑问，结合下边 `为什么 3.0 还提供了一个组件来解决`，也就是说没有异步时，即便我们自定义业务线程去执行操作，那么对于 Tomcat 的 IO 线程依然是阻塞的，等着业务线程的结果然后 IO 线程去响应。也就是 `AsyncContext` 解决了释放 IO 线程的问题。那么在加入 `servlet3.1的非阻塞IO` 后，又是什么情况呢？这个非阻塞 IO 是说的 Tomcat 的 IO 线程吗？~~再结合下边这篇文章，那么非阻塞 IO 是不是就是，tomcat 工作线程获得 AsyncContext 对象给业务线程后，就释放了。然后业务线程 IO 完成后，通过 `asyncContext.complete()` 通知了 tomcat 线程？~~划掉的说的其实还是异步，跟阻塞没关系。也就是现在搞不清， 这个异步是针对哪个线程说的？通过《Java 异步编程实战》第六章讲解 Web Servlet 的异步非阻塞处理 ,终于搞清楚了这个困扰两天的问题。还是书本系统啊。

[这篇](https://mp.weixin.qq.com/s/B_lS3KwiZZ7dOyqyZOsQ1A) [1](http://8.129.87.238/%E6%89%8B%E6%8A%8A%E6%89%8B%E5%B8%A6%E4%BD%A0%E6%90%AD%E5%BB%BA%E7%A7%92%E6%9D%80%E7%B3%BB%E7%BB%9F/06-%E9%9B%B7%E4%BB%A4%E9%A3%8E%E8%A1%8C%EF%BC%9A%E6%80%A7%E8%83%BD%E8%B0%83%E4%BC%98%E6%9B%B4%E4%B8%8A%E4%B8%80%E5%B1%82%E6%A5%BC%20%283%E8%AE%B2%29/12%EF%BD%9C%E9%AB%98%E6%80%A7%E8%83%BD%E4%BC%98%E5%8C%96%EF%BC%9A%E5%8D%95%E6%9C%BAJava%E6%9E%81%E8%87%B4%E4%BC%98%E5%8C%96.html)
1. Tomcat NIO 或 AIO 的概念是针对请求的接收来说，而 Servlet 的异步非阻塞主要是针对请求的处理，已经是到了 Tomcat 线程池那里了。
2. Servlet 3.0 之前，Tomcat 线程在执行自定义 Servlet 时，如果过程中发生了 IO，那么 Tomcat 线程只能在那等着结果，这时线程是被挂起的，如果被挂起的多了，自然会影响对其他请求的处理。
3. 所以在 Servlet 3.0 之后，支持在这种情况下将这种等待的任务交给一个自定义的业务线程池去做，这样 Tomcat 线程可以很快地回到线程池，处理其他请求。而业务线程在执行完业务逻辑以后，通过调用指定的方法，告诉 Tomcat 线程池接下来可以将业务线程执行的结果返回给调用方，这样就实现了同步转异步的效果。
4. 这样做的好处，可能对提高系统的吞吐量有一定帮助，但从 JVM 层面来说，并没有减少工作量。业务线程在执行任务遇到 IO 时，依然会阻塞，现在只是由业务线程池代替了 Tomcat 线程池做了最耗时的那部分工作，这样也许可以将原来的 200 个 Tomcat 线程，拆分成 20 个 Tomcat 线程、180 个业务线程来配合工作。这里原生 Servlet 以及 SpringMVC 对异步功能支持的测试代码，你可以看 GitHub 代码库中的 AsyncServlet 类和 TestAsyncController 类
5. Servlet 3.1 的非阻塞，这块简单来说，就是针对请求消息体的读取，这是个 IO 过程，以前是阻塞式读取，现在支持非阻塞读取了。实现的大致原理就是在读取数据时，新增一个监听事件，在读取完成后由 Tomcat 线程执行回调。

此外，还有一个疑问，既然 tomcat 8 已经使用了 `servlet3.1的非阻塞IO`，<font color=red size=5>那为什么读写请求体还是阻塞？</font>
[这篇](https://mp.weixin.qq.com/s/Lq7BqbQF0g2geoCrt_98Eg)提到了，但没有说明 tomcat 版本，感觉是对 servlet 3.1 之前的解释。

[源码分析tomcat BIO NIO NIO2 的 Read Request Headers及Read Request Body 为 Non Blocking/Blocking状态原因\_const伐伐的博客-CSDN博客](https://blog.csdn.net/u013905744/article/details/110675057)
1. NIO 的 Read Request Headers 之所以是非阻塞的，是因为在应用层，通过 poller 线程操作（I/O 多路复用机制），当 socket 可读，就让 exec 线程通过同步非阻塞的 while 循环方式不断地读取，放到 heap byte buffer 中，解析，直到将请求行和请求头都解析出来，其是非阻塞的
2. 为什么 Read Request Body 是 Blocking 方式的呢？
	1. Servlet 规范规定了对 HTTP Body 的读写是阻塞的
	2. “Tomcat 和 Jetty 在解析 HTTP 协议数据时， 都采取了延迟解析的策略，HTTP 的请求体（HTTP Body）直到用的时候才解析。也就是说，当 Tomcat 调用 Servlet 的 service 方法时，只是读取了和解析了 HTTP 请求头，并没有读取 HTTP 请求体。”
	3. 当读取请求体的时候，request buffer有8k大小，是循环读取、覆盖的方式，同样，responseBuffer 也是8k大小，也是循环写入、覆盖的方式，所以其是读写是阻塞的。

[Tomcat NIO线程模型与IO方式分析 - 简书](https://www.jianshu.com/p/ad20dbeac912)
1. Tomcat nio 读 request body 与写 response 都是阻塞的
2. 根据 servlet 规范需要使用从 Request 和 Response 两个类获取流来进行读写，而 `ServletInputStream` 和 `ServletOutputStream` 根据 servlet 规范是要求阻塞读写的。
3. 比如在 Request 处理时，工作线程读请求 line 和 header 的时候是非阻塞的，而读 request body 是阻塞的。而由于 accept 的 socket 设置 blocking false，所以要找到一个办法去让工作线程阻塞的去处理非阻塞的 socket。
4. NioBlockingSelector 和 BlockPoller，前者提供读写方法、如果一次没读写完则阻塞在一个读写锁上等待通知就绪，后者内部是 selector 和轮询线程、负责 epoll 出来当前读写就绪的连接。当读写就绪时，就会打开连接上的读写锁（CountDownLatch 实现），让阻塞在锁上的线程继续读写

##### TomcatNIO 模拟阻塞读写请求体与 Servlet 3.1 非阻塞 IO 的关联是什么
[Tomcat NIO(11)-请求数据读取](https://mp.weixin.qq.com/s?__biz=MzI0MDE3MjAzMg==&mid=2648393718&idx=1&sn=c18a44c601c1d02c2012b5ccb92bfcfa)
[Tomcat NIO(12)-响应数据写入](https://mp.weixin.qq.com/s?__biz=MzI0MDE3MjAzMg==&mid=2648393719&idx=1&sn=cd50572858fc045586339d881398a353)
经代码验证，在自定义的异步 Servlet 下，声明 listener 后可以触发非阻塞读取请求体。
1. 读操作是在 `CoyoteInputStream#checkNonBlockingRead()调用coyoteRequest.getReadListener() == null` 来判断
	1. `NioSocketWrapper#read()` → `NioSocketWrapper#fillReadBuffer(boolean block, ByteBuffer buffer)`
2. 写操作 `Http11OutputBuffer#isBlocking()` 是通过 `response.getWriteListener() == null` 来判断
3. `SocketWrapperBase#write(boolean block, byte[] buf, int off, int len)`
4. 我们在自己写的 Servlet 中添加了监听器，所以是非阻塞读写

[Tomcat阅读笔记 - HttpServlet InputStream流从哪读取来 - 知乎](https://zhuanlan.zhihu.com/p/601300961)
[Tomcat阅读笔记 - 为什么说Tomcat在NIO模式下的body读取是模拟阻塞读取 - 知乎](https://zhuanlan.zhihu.com/p/601551426)
头疼，验证不了 CoyoteInputStream 只是一种读取情况，也没再复现出来，奇怪。
不知道 CoyoteInputStream 是针对哪种请求格式的解析。

###### ==再次 debug 代码，终于摸索到==
1. 在 `Http11Processor#service` 中，先处理请求头等信息
2. 然后调用了 `getAdapter().service(request, response);` 进入 `CoyoteAdapter#service()`
3. 然后执行 `connector.getService().getContainer().getPipeline().getFirst().invoke(request, response)` 进入 servlet 方法内部
4. 假如是异步请求，那么会直接继续向下，在读取请求体注册监听器后，会接着返回，继续执行 `CoyoteAdaper#service()` ，判断 `request.isAsync()`，然后获取监听器，判断是否完成读取，然后完成请求
5. 假如是同步请求，则进去 servlet 后直接发生读
6. 读写请求体，最终都是调用到 `NioEndpoint中的NioSocketWrapper#fillReadBuffer(boolean block, ByteBuffer buffer)`
7. **假如是阻塞读写，且读不到数据时，则会 `registerReadInterest();`，也就是模拟阻塞读的逻辑**
8. 假如是非阻塞，则直接读

[知秋大佬-----Reactor-Netty与Spring WebFlux解读 之 第一章 为什么需要Spring WebFlux](https://zhuanlan.zhihu.com/p/389308521)
1. 因为客户端与服务器底层的通信都是通过 Socket 进行的，如果进入服务器（客户端写入 Socket）的数据阻塞或因为网络问题数据流传输的速度比服务器读取的速度慢，则尝试读取此数据的服务器线程必须等待该数据。
2. 另一方面，如果从服务器往 ServletOutputStream 写入响应数据的速度很慢，则客户端线程必须等待。
3. 在这两种情况下，服务器线程都执行都会因这种传统的 I/O（用于请求/响应）阻塞。同时，对于数据量大的请求体或者响应体，阻塞 IO 也将导致不必要的等待。换句话说，在 Servlet 3.0 中，只有请求处理部分变为异步，而用于服务请求和响应的 I/O 并不是异步的。要知道，Servlet 线程中会有一个过滤器链对请求和响应进行处理，在过滤器链的处理过程中会对请求和响应产生 I/O 操作，如果线程阻塞的数量足够多，这将导致线程饥饿，并影响性能。
4. 为此，在 Servlet 3.1 中引入了非阻塞 IO，通过在 HttpServletRequest 和 HttpServletResponse 中分别添加 ReadListener 和 WriterListener 方法，可以让我们做到：只有在 IO 数据满足一定条件时（比如读取/写入已经数据准备好），再进行后续的操作。
5. Servlet 3.1中引入的WriteListener。 WriteListener有一个onWritePossible方法的接口，该方法由Servlet容器调用。 通过ServletOutputStream的isReady来检查是否可以写入**NIO Socket的缓冲区**。 万一返回true，则在Servlet容器上调用执行onWritePossible方法，否则在Socket可用于写的时候，Servlet容器会调用此监听器方法。


### 源码
[深度解读Tomcat中的NIO模型 - 简书](https://www.jianshu.com/p/76ff17bc6dea)
1. Tomcat 的 NIO 是基于 I/O 复用来实现的
2. 有个时序图

[Tomcat 中的 NIO 源码分析\_Javadoop](https://javadoop.com/post/tomcat-nio)
1. 源码分析了流程

[Tomcat NIO](https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzI0MDE3MjAzMg==&action=getalbum&album_id=2123520319532400647)

[14 NioEndpoint组件：Tomcat如何实现非阻塞IO？](http://learn.lianglianglee.com/%E4%B8%93%E6%A0%8F/%E6%B7%B1%E5%85%A5%E6%8B%86%E8%A7%A3Tomcat%20%20Jetty/14%20NioEndpoint%E7%BB%84%E4%BB%B6%EF%BC%9ATomcat%E5%A6%82%E4%BD%95%E5%AE%9E%E7%8E%B0%E9%9D%9E%E9%98%BB%E5%A1%9EI_O%EF%BC%9F.md) 

[Reactor线程模型](BIONIOAIO.md#Reactor)

[tomcat堆栈中10大常见线程详解](https://blog.csdn.net/smart_an/article/details/106592347)

## reference
[tomcat内核\_超人汪小建(seaboat)的博客-CSDN博客](https://blog.csdn.net/wangyangzhizhou/category_2133839.html)
[预流 的个人主页 - 文章 - 掘金](https://juejin.cn/user/3104676567070968/posts)

```
|-- bin 执行脚本目录；
|   |-- bootstrap.jar tomcat启动时所依赖的一个类，在启动tomcat时会发现Using CLASSPATH: 是加载的这个类；
|   |-- catalina-tasks.xml 定义tomcat载入的库文件，类文件；
|   |-- catalina.bat
|   |-- catalina.sh tomcat单个实例在Linux平台上的启动/关闭脚本；
|   |-- commons-daemon-native.tar.gz jsvc工具，可以使tomcat以守护进程方式运行，需单独编译安装；
|   |-- commons-daemon.jar jsvc工具所依赖的java类；
|   |-- configtest.bat
|   |-- configtest.sh tomcat检查配置文件语法是否正确的Linux平台脚本；
|   |-- cpappend.bat
|   |-- daemon.sh tomcat已守护进程方式运行时的，启动，停止脚本；
|   |-- digest.bat
|   |-- digest.sh
|   |-- setclasspath.bat
|   |-- setclasspath.sh
|   |-- shutdown.bat
|   |-- shutdown.sh tomcat服务在Linux平台下关闭脚本；
|   |-- startup.bat
|   |-- startup.sh tomcat服务在Linux平台下启动脚本；
|   |-- tomcat-juli.jar
|   |-- tomcat-native.tar.gz 使tomcat可以使用apache的apr运行库，以增强tomcat的性能需单独编译安装；
|   |-- tool-wrapper.bat
|   |-- tool-wrapper.sh
|   |-- version.bat
|   |-- version.sh 查看tomcat以及JVM的版本信息；
|-- conf 顾名思义，配置文件目录；
|   |-- catalina.policy Java相关的安全策略配置文件，在系统资源级别上提供访问控制的能力，比如：配置tomcat对文件系统中目录或文件的读、写执行等权限，及对一些内存，session等的管理权限；
|   |-- catalina.properties Tomcat内部package的定义及访问相关的控制，也包括对通过类装载器装载的内容的控制；Tomcat在启动时会事先读取此文件的相关设置；
|   |-- context.xml tomcat的默认context容器，所有host的默认配置信息；
|   |-- logging.properties Tomcat通过自己内部实现的JAVA日志记录器来记录操作相关的日志，此文件即为日志记录器相关的配置信息，可以用来定义日志记录的组件级别以及日志文件的存在位置等；
|   |-- server.xml tomcat的主配置文件，包含Service, Connector, Engine, Realm, Valve, Hosts主组件的相关配置信息；
|   |-- tomcat-users.xml Realm认证时用到的相关角色、用户和密码等信息；Tomcat自带的manager默认情况下会用到此文件；在Tomcat中添加/删除用户，为用户指定角色等将通过编辑此文件实现；
|   |-- web.xml 为不同的Tomcat配置的web应用设置缺省值的文件，遵循Servlet规范标准的配置文件，用于配置servlet，并为所有的Web应用程序提供包括MIME映射等默认配置信息；
|-- lib 运行需要的库文件（JARS），包含被Tomcat使用的各种各样的jar文件。在Linux/UNIX上，任何这个目录中的文件将被附加到Tomcat的classpath中；
|-- logs 日志文件默认存放目录；
|   |-- localhost_access_log.2013-09-18.txt 访问日志；
|   |-- localhost.2013-09-18.log 错误和其它日志；
|   |-- manager.2013-09-18.log 管理日志；
|   |-- catalina.2013-09-18.log Tomcat启动或关闭日志文件；
|-- temp 临时文件存放目录；
|   |-- safeToDelete.tmp
|-- webapps tomcat默认存放应用程序的目录，好比apache的默认网页存放路径是/var/www/html一样；
|   |-- docs tomcat文档；
|   |-- examples tomcat自带的一个独立的web应用程序例子；
|   |-- host-manager tomcat的主机管理应用程序；
|   |   |-- META-INF 整个应用程序的入口，用来描述jar文件的信息；
|   |   |   |-- context.xml 当前应用程序的context容器配置，它会覆盖tomcat/conf/context.xml中的配置；
|   |   |-- WEB-INF 用于存放当前应用程序的私有资源；
|   |   |   |-- classes 用于存放当前应用程序所需要的class文件；
|   |   |   |-- lib 用于存放当前应用程序所需要的jar文件；
|   |   |   |-- web.xml 当前应用程序的部署描述符文件，定义应用程序所要加载的servlet类，以及该程序是如何部署的；
|   |-- manager tomcat的管理应用程序；
|   |-- ROOT 指tomcat的应用程序的根，如果应用程序部署在ROOT中，则可直接通过http://ip:port 访问到；
|-- work 用于存放JSP应用程序在部署时编译后产生的class文件；
```


[https://mp.weixin.qq.com/s/kgcr7zdcRlJNHG84ODgHDw](https://mp.weixin.qq.com/s/kgcr7zdcRlJNHG84ODgHDw)