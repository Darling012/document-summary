
# WEB即时通讯


##### 方式
1. 短轮询
	1. 客户端定时不断发起请求
2. 长轮询
	1. 客户端发起请求后服务端阻塞请求，要么数据变更后服务端返回结果，要么服务端超时后返回客户端从新请求。
3. [comet](https://www.cnblogs.com/JoannaQ/p/3434758.html)
	1. 本质也是轮询，分为长轮询和流技术
	2. 轮询和 comet 都是基于请求问答模式，每次都浪费了一定流量在头信息上
	3. 流技术
		1. 基于 Iframe 及 htmlfile 的流（streaming）方式
		2. 客户端使用一个隐藏窗口与服务端建立 HTTP 长连接，服务端不断更新状态以保持长连接存活。
4. SSE
	1. html 5 单向，服务器推送
	2. 仅支持get 请求
5. [websocket](websocket.md)
6. mqtt
7. grpc-web

##### 关于 comet
不再考虑。
在 comet 技术相关的文章里，会提到基于 http 长连接。但这个长连接跟 http1.1 的长连接不是一回事，是说的一个 [http 连接不断开](http://www.52im.net/thread-224-1-1.html)，即长轮询。但是看基于流技术的图，目测一次请求响应了多组数据。在  [Comet技术详解](http://www.52im.net/thread-334-1-1.html)里提到开发这种 `长连接`，多个 frame 打开不能为每个都建立 http `长连接`，因为 `HTTP 1.1 规范中规定，客户端不应该与服务器端建立超过两个的 HTTP 连接`，在设计上考虑让多个 frame 的更新共用一个长连接，并举了 IE 打开三个窗口下载文件，第三个会阻塞问题。
![http](http.md#浏览器限制同源请求并发%20tcp%20连接数问题)



##### 对服务端的影响
长连接长期占用客户端和服务端一个连接资源
1.  [03 轮询与长连接：如何解决消息的实时到达问题？.md](https://learn.lianglianglee.com/%E4%B8%93%E6%A0%8F/%E5%8D%B3%E6%97%B6%E6%B6%88%E6%81%AF%E6%8A%80%E6%9C%AF%E5%89%96%E6%9E%90%E4%B8%8E%E5%AE%9E%E6%88%98/03%20%E8%BD%AE%E8%AF%A2%E4%B8%8E%E9%95%BF%E8%BF%9E%E6%8E%A5%EF%BC%9A%E5%A6%82%E4%BD%95%E8%A7%A3%E5%86%B3%E6%B6%88%E6%81%AF%E7%9A%84%E5%AE%9E%E6%97%B6%E5%88%B0%E8%BE%BE%E9%97%AE%E9%A2%98%EF%BC%9F.md)

##### 长连接和长轮询
http 的长连接是在 tcp 的三次握手四次挥手之间有多次请求响应。
[HTTP - 长连接 & 短连接 & 长轮询 & 短轮询 & 心跳机制](https://developer.aliyun.com/article/933937)
###### 第一组概念
1. 轮询
2. 短轮询
	1. 有无数据都立即响应
3. 长轮询
	1. 服务端保持连接，发送消息后连接断开
4. 短连接
	1. 响应后断开, 配合轮询来进行新旧数据的更替
5. 长连接
	1. 保持连接，发送消息后连接不断开，除非超时
6. 长短连接和长短轮询
	1. 短轮询即每一次请求立即返回
	2. 长轮询是服务端没有数据更新则挂起连接，有数据再返回，再进入循环周期
	3. 长短轮询的**理想实现**都应当基于长连接，否则若是循环周期太短，那么服务器的荷载会相当重；
7.  心跳机制
	1. 心跳超时认为连接断开
8.  .......。
9. 

## 方案
##### 场景
1. 报警等事件推送
	1. 不断连
2. 私信收发
	1. 不断连

##### 怎么选
[[译] WebSockets 与长轮询的较量 - 掘金](https://juejin.cn/post/6844903871710494733)

websocket 相对于 http 的优势是在网络传输中，websocket 用一个 tcp 实现全双工，而基于 http 的长轮询和 SSE 会重新发起 http。
对于服务器来说，参考 [tomcat maxThreads、maxConnections、acceptCount 的理解](Spring%20Servlet%20Tomcat.md#一个%20SpringBoot%20项目能处理多少请求？)，websockt、SSE 和长轮询都会占用一个连接，即 tcp 三次握手后，都受 maxConnections 限制，但不占用工作线程。

[SSE技术详解：使用 HTTP 做服务端数据推送应用的技术 - 古兰精 - 博客园](https://www.cnblogs.com/goloving/p/9196066.html)
1. 选择长轮询还是常规轮询？
	1. 我们可以从带宽占用的角度分析，如果一个程序数据更新太过频繁，假设每秒 2 次更新，如果使用长轮询的话每分钟要发送 120 次 HTTP 请求。如果使用常规轮询，每 5 秒发送一次请求的话， 一分钟才 20 次，从这里看，常规轮询更占有优势。
2. 在 SSE 的草案中提到，"text/event-stream" 的 MIME 类型传输应当在静置 15 秒后自动断开。在实际的项目中也会有这个机制，但是断开的时间没有被列入标准中。 ^be9by1

###### SSE 是基于 http 长连接的轮询，那和短轮询和长轮询差别在哪？
1. 区别就在于：一个TCP连接可建立1次或多次http请求响应，一次请求响应又可包含一次或多次服务端发送数据
2. 短轮询需要重复 tcp 三次握手四次挥手
3. 长轮询一次请求只能获取一个消息
4. sse 一次请求，服务端可以发送多组数据。且可设置永不超时，即不会断开重连或者重新发起请求，造成创建多次请求退化成轮询。
5. sse 存在超时时间时，就会出现在一个 http 长连接上，多次请求。
6. 为了减少 tcp 三四资源消耗，短、长轮询都应该基于长连接，sse 本身基于长连接。
7. 从长连接角度，且受 tomcat 一个长连接默认最多支持100个请求和浏览器同源最大 tcp 连接数限制：
	1. 短轮询消耗一次请求响应。
	2. 长轮询消耗一次请求响应。但服务端 hold 住，不超时情况下有可能获取到数据
	3. sse 可设置不超时，只用一次请求响应，获取多组数据。
	4. sse 存在超时，每次断开重来都消耗一次请求响应 (一次请求响应也可发送多组数据)。
8. 根据 [SSE 是基于 http 长连接的轮询 ](WEB即时通讯.md#SSE%20是基于%20http%20长连接的轮询) 测试，SseEmitter的超时时间为单次请求响应的，在Tomcat限制的100次请求响应后，会发起重连。

[长连接/websocket/SSE等主流服务器推送技术比较](https://mp.weixin.qq.com/s/unagcn33kfCnMrlI-Oslig)
1. sse 与长轮询机制类似，**区别是每个连接不只发送一个消息**。客户端发送一个请求，服务端保持这个连接直到有新消息发送回客户端，仍然保持着连接，这样连接就可以消息的再次发送，由服务器单向发送给客户端。
2. 服务端 hold 住一个长轮询连接 (http 也是基于 tcp 啊)的代价 (再加上 keepalive 的支持) 应该不会比 hold 住一个 websocket 长连接 大太多吧？
	1. 没有 websocket 代价高，但 websocket 需要改造协议。

###### sse 和 websokcet 除性能外的优势
1. stomp over websocket
	1. 内置心跳检测
	2. 内置 ack、nack、receipt
	3. 内置事务控制
2. sse
	1. 断线重连
	2. 

### 长轮询
spring 利用 [DeferredResult](Spring%20异步.md#DeferredResult) 实现长轮询

### SSE
SSE 的基本思想是，服务器使用流信息向服务器推送信息。严格地说，http 协议无法做到服务器主动推送信息。但是，有一种变通方法，就是服务器向客户端声明，接下来要发送的是[流信息](https://www.cnblogs.com/superlizhao/p/13992807.html)。也就是说，发送的不是一次性的数 据包，而是一个数据流，会连续不断地发送过来。这时，客户端不会关闭连接，会一直等着服务器发过来的新的数据流，视频播放就是这样的例子。

##### SSE 是基于 http 长连接的轮询 

![WEB即时通讯](WEB即时通讯.md#^be9by1)
以及 [SSE 浏览器默认如果3秒内没有发送任何信息，则开始重连。](https://www.jianshu.com/p/b1f00785799c)，就导致了类似[长轮询](WEB即时通讯.md#长轮询)的模式，假如一直没消息，超时后就会断开连接再重连，并不减少网络资源占用。但是在[代码案例](https://juejin.cn/post/7122014462181113887) 中 spring 提供的 `new SseEmitter(0L); 0表示永不过期`，目测不会新建请求。 ^1vbfg8
###### 有了一个疑问，`new SseEmitter(); ` 里的参数控制的是一次请求的超时时间，还是一次连接？
[代码案例](https://juejin.cn/post/7122014462181113887)的例子写的不好，设置超时事件后不能成功验证猜想。[结合这个例子](https://blog.csdn.net/m0_50596083/article/details/124600126)
经测试超时时间为一次请求的超时时间，超时后会重新发起请求，受 tomcat 限制100 次后重新建立 tcp 连接。


##### 连接与线程
tomcat 对于 DeferredResult 的长轮询会释放连接线程，那么 SSE 是基于长连接的，是不是一直持有这个线程？websocekt 呢？

[这篇](https://juejin.cn/post/6844903552666566670)里使用了异步，这是不是就切换到 spring 或者说我们自定义的线程上了，而不再是 tomcat 处理 servlet 的线程了，是不是就释放了 tomcat 的线程？

综合[这里](https://stackoverflow.com/questions/31768349/how-does-server-sent-events-work)提到要使用异步 API ` AsyncContext actx = req.startAsync();` 并且针对这个问题
1. What is actually happening on the server? In normal scenarios, tomcat creates a thread to handle every request. What is happening now?
2. If use NIO connector which is default in Tomcat 8.0.X, within the whole processing cycle HTTP I/O about a request won't hold a thread. If use BIO a thread will be hold until the whole processing cycle completes. All threads are from a thread pool, tomcat won't create a thread for each request.

然后这三篇文章
1. [Tomcat NIO(18)-服务端事件SSE](https://mp.weixin.qq.com/s/YcTEvWL-0C41857uVyfbvA) 
	1. 对于 tomcat 来说，一个长连接默认最多支持100个请求，所以当请求多于100个的时候，一定关闭当前连接，然后由 SSE 再次建立另一个长连接。
	2. SSE 其本质是浏览器发送一个请求到服务端建立一个长连接，在这个长连接基础上客户端多次发送 http 请求，来轮询服务端是否有数据需要返回给浏览器。所以 SSE 是建立在浏览器和服务端长连接基础上的多次 http 轮询请求，但是 SSE 支持断开重连(由浏览器自己实现)
2. [Tomcat NIO(19)-开启异步](https://mp.weixin.qq.com/s?__biz=MzI0MDE3MjAzMg==&mid=2648393727&idx=1&sn=75fbbd6d4e4128336d90cc7308510470)
	1. 即 ` AsyncContext actx = req.startAsync();`
3. [Tomcat NIO(20)-异步任务运行](https://mp.weixin.qq.com/s?__biz=MzI0MDE3MjAzMg==&mid=2648393728&idx=1&sn=357c0ffea8399c4ef2e0ecf8c63b0f59)
	1. 在 tomcat 原生异步实现的 API 中，任务是占用了 io 线程的。我们并不建议这样做，因为 io 线程是 servlet 的运行线程，所以当大量异步任务开启的时候势必会占用 io 线程池中的大量资源。从而影响 servlet 请求的运行，进而影响了服务器的吞吐率。
	2. 所以在这种情况下我们建议引入业务线程池，将异步任务在业务线程池中运行，得到结果，设置响应，结束异步。这样释放 io 线程，避免影响服务器吞吐率
跟上边对起来了。重点是 [Tomcat的NIO](Tomcat.md#NIO) ^mjemwl

#### SseEmitter
[SseEmitter可以在一次请求中返回多条数据,而DeferredResult只能返回一条](https://www.cnblogs.com/jun1019/p/10886079.html)

#### webflux
[利用 Server-Sent Events (SSE) 技术, 仿 ChatGPT, 实现对话内容动态输出 - 知源笔记](https://zhiyuanbiji.cn/notes/497dfabc44ab27acf00b422aafd125d4)
[详解 Spring 5 Server-Sent Events (三) WebFlux | LeFer](https://www.lefer.cn/posts/30624/)

### websocket
[websocket](websocket.md)

### http2
只能主动推静态资源

## reference
[web通讯的四种方式，短轮询、长轮询(comet)、长连接(SSE)、WebSocket - qiqi715 - 博客园](https://www.cnblogs.com/qiqi715/p/13138589.html)
1. 短轮询
	1. 即普通 ajax 请求
	2. 请求端控制间隔
2. 长轮询
	1. 服务端接收到 ajax 请求后 hold 住连接，直到有消息才响应并关闭连接
	2. 服务端有数据返回决定间隔
3. http 长连接
	1. 多个 http 请求共用一个 tcp 连接
	2. http 1.1 在请求头和响应头中 `connection: keep-alive`
	3. 在响应头中 `keep-live：timeout=30,max=5`，timeout 是两次 http 请求保持的时间 (s), , max 是这个 tcp 连接最多为几个 http 请求重用
		1. 经测试，会返回 timeout, 不会返回 max。`server.tomcat.max-keep-alive-requests=20;server.tomcat.keep-alive-timeout=30000;`
		2. 但这个字段的约束力并不强，通信的双方可能并不会遵守，所以不太常见
4. websocket

[新手入门贴：史上最全Web端即时通讯技术原理详解-网页端IM开发/专项技术区 - 即时通讯开发者社区!](http://www.52im.net/thread-338-1-1.html)
1. 轮询（polling）
	1.  客户端以设定时间周期性向服务器发送 ajax 请求，频繁查询是否有新的数据改动。
2. 长轮询（long-polling）
3. 基于 http-stream 通信
	1. 基于 XHR 对象的 streaming 方式
	2. 基于 iframe 的数据流
	3. 基于 htmlfile 的数据流通信
4. SSE（服务器推送事件（Server-sent Events）

[Web端即时通讯技术盘点：短轮询、Comet、Websocket、SSE - 腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1194073)
1. ajax 短轮询
2. comet
	1. ajax 长轮询
	2. Iframe 及 htmlfile 的流（http streaming）
3. websocket
4. html 5 的 SSE

[短轮询、长轮询、SSE、WebSocket](https://mp.weixin.qq.com/s?__biz=MzUzMjM5ODk5Nw==&mid=2247486867&idx=1&sn=31c5be08f334bdd196d9ffe20c2d1853)
1. 客户端轮询：传统意义上的短轮询（Short Polling）；
2. 服务器端轮询：长轮询（Long Polling）；
3. 单向服务器推送：Server-Sent Events（SSE）；
	1. SSE的本质其实就是一个HTTP的长连接，只不过它给客户端发送的不是一次性的数据包，而是一个stream流，格式为_text/event-stream_。所以客户端不会关闭连接，会一直等着服务器发过来的新的数据流，视频播放就是这样的例子。
4. 全双工通信：WebSocket。

[我有 7种 实现web实时消息推送的方案，7种！ - 掘金](https://juejin.cn/post/7122014462181113887) 
[有完整代码案例](https://github.com/Darling012/springboot-realtime-data)
1. js 定时器短轮询
2. 长轮询，前端阻塞，后端用 `DeferredResult`
3. `iframe流`，在前后端建立长连接
4. SSE
5. mqtt
6. websocket
7. 第三方

 [Web端即时通讯](https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzUzMjM5ODk5Nw==&action=getalbum&album_id=2035725814863822851&scene=173&from_msgid=2247488189&from_itemidx=1&count=3&nolastread=1#wechat_redirect)


[SSE技术详解：一种全新的HTML5服务器推送事件技术-网页端IM开发/专项技术区 - 即时通讯开发者社区!](http://www.52im.net/thread-335-1-1.html)