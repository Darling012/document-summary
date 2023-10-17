# websocket
##### 问题
1. 
2. 容器（tomcat、undertow）实现的 websokcet 和 spring 实现的以及 netty 实现的有啥区别
3. sockjs 和 soket.io 处于的层级和作用
4. stomp 比着 [Protobuf](https://github.com/protocolbuffers/protobuf) 多做了哪些事情
5. mqtt 和 stomp
6. 区分 http 1.0 和 http 1.1 以及 html 5 出来的 websocket
7. 和 [socket](socket.md) 关系
	1. 没关系。
	2. socket 是对 tcp/udp 的抽象接口。websocket 是应用层协议。
8. 和 [webrtc](webrtc.md) 关系
	1. 也没啥关系，不过 webrtc 可以用 websocket 当信令的通道。

[EasySwoole-WebSocket在线测试工具](http://www.easyswoole.com/wstool.html)

![http](http.md#无线通信的三种通信方式)

## 原理
WebSocket 是为了 HTML5 应用方便与服务器双向通讯而设计的协议，HTTP 握手然后转 TCP 协议，用于取代之前的 Server Push、Comet、长轮询等老旧实现。  
1. 单个 tcp/ip 连接提供全双工通信通道
2. 先使用 http 建立连接，再协商升级到 websockets，然后连接从 http 切换到 websocket
3. 在 HTTP 协议中，是基于 Request/Response 请求响应的**同步**模型，进行交互。在 Websocket 协议中，是基于 Message 消息的**异步**模型，进行交互

1. [WebSocket的故事（一）—— WebSocket的由来 - 掘金](https://juejin.cn/post/6844903655221493774)
2. [看完让你彻底理解 WebSocket 原理，附完整的实战代码（包含前端和后端） - nnngu - 博客园](https://www.cnblogs.com/nnngu/p/9347635.html)
3. [一篇吃透WebSocket](https://mp.weixin.qq.com/s/miYKpUIrcwgOGxztokNyxw)
4. [一文吃透 WebSocket 原理 刚面试完，趁热赶紧整理 - 掘金](https://juejin.cn/post/7020964728386093093)
	1. 虽然HTTP/2也具备服务器推送功能，但HTTP/2只能推送静态资源，无法推送指定的信息。
5. [有了 HTTP 协议，为什么还要有 websocket 协议？](https://mp.weixin.qq.com/s/TtRKkVxS6H-miQ8luQgY1A)

##### websocket 和 http 关系
[WebSocket详解（四）：刨根问底HTTP与WebSocket的关系(上篇)-网页端IM开发/专项技术区 - 即时通讯开发者社区!](http://www.52im.net/thread-1258-1-1.html)

[WebSocket原理与应用](https://mp.weixin.qq.com/s?__biz=MzI3ODU3MzQ2Ng==&mid=2247484415&idx=1&sn=eb4c3696ed799913839c019f60847efc)
1. 都是基于全双工的 tcp，为什么 websocket 是全双工，而 htt 2 之前的不是？
	1. 这篇文章从 socket 和操作系统内核角度解释了为什么支持全双工
	2. 但是没有从操作系统内核角度解释 http
		1. 带来一个新的盲区，内核、io、协议 #知识的荒原 
	3. 并且，到了底层都应该是 socket tcp，还是没明确说明上述问题。
	4. [通过tomcat](Spring%20Servlet%20Tomcat.md#一个%20SpringBoot%20项目能处理多少请求？) 关联起来协议、IO。
2. 提到了更底层的，在物理链路媒介层面决定了全双工通信的可能性
[基于websocket实现《全双工协议》的TS同步调用之Request/Response/Notify 原理与实战\~ - 掘金](https://juejin.cn/post/7000140081293950984?searchId=20230719112547521909CA0457EA3411FD)
这篇里说明了
1. 本质的原因 HTTP1.x 协议是一个请求、响应的模式。在一次请求中，`Respose必定是在Request之后发生的`，请求包与响应包是不可能同时在网络中传输
2. 但是在一个全双工的通信中，消息之间是没有明确的顺序与关联关系的。
	1. 这也是引入 stomp 的原因吧可能


##### websoket 和 [socket](socket.md) 关系
[WebSocket详解（六）：刨根问底WebSocket与Socket的关系-网页端IM开发/专项技术区 - 即时通讯开发者社区!](http://www.52im.net/thread-1273-1-1.html)

##### 为什么能实现长连接
1. [WebSocket 是什么原理？为什么可以实现持久连接？ - 知乎](https://www.zhihu.com/question/20215561)

nginx  单个连接时长对 websocket 长连接的影响`keepalive_timeout`

## 单体
1. javax
	1. tomcat
2. spring
	1. `@EnableWebSocket`
		1. 支持 SockJS
	2. `@EnableWebSocketMessageBroker`
		1. 使用STOMP
3. netty

[【Spring Boot】WebSocket 的 6 种集成方式\_springboot集成websocket\_代码峡谷孙膑的博客-CSDN博客](https://blog.csdn.net/m0_64360721/article/details/125384766)
- `Javax` 是 ` java` 的扩展包 `javax.websocket` 中定义的一套 `WebSocket` 的接口规范
- `WebMVC`
- `WebFlux`
后三种跟spring 并不强绑定
- `Java-WebSocket` 这是一个纯`java`的第三方库，专门用于实现`WebSocket`
- `SocketIO`该库使用的协议是经过自己封装的，支持很多的语言，提供了统一的接口，所以需要使用它提供的`Server`和`Client`来连接
- `Netty`

[Spring WebSocket简析 | Tony's toy blog](https://www.tony-bro.com/posts/3568303861/index.html)
1. 原生 Java API
	1. 这种情况只利用 Spring 的基础能力以及辅助工具类，直接以标准的 java 规范接口（javax. websocket 包下的内容）来编写 websocket 应用，在依赖上仅需要 `spring-boot-starter-websocket`。
2. 单独使用 Spring Websocket
	1. `@EnableWebSocket`
	2. `withSockJS` 提供 SockJS 的支持
3. STOMP On Spring Websocket
	1. `@EnableWebSocketMessageBroker`
	2. 

[绿色记忆:Spring对WebSocket的支持](https://blog.gmem.cc/ws-support-of-spring)
1. Spring 4. x 引入了新的模块 spring-websocket，对 WebSocket 提供了全面的支持，Spring 的 WebSocket 实现遵循 JSR-356（Java WebSocket API），并且添加了一些额外特性。Spring 允许基于 SockJS 协议作为备选传输方案。
2. Spring 4 引入了新的模块 [spring-messaging](spring-messaging.md)，抽象出了 Message、MessageChannel、MessageHandler 等消息架构的基础概念。此模块包含了一些注解，用于将消息映射到方法（类似于 Spring MVC 把 URL 映射到方法）。
##### SockJS
html 5 规范中给了原生的 websocektAPI，但有的浏览器不支持 websocket，这就得切换为 ajax 长轮询、sse 等方案。sockjs 和 socketIO 提供了支持。
1. spring 是 sockjs 推荐的 Java server 实现，也提供了 Java client 实现（client 不该是前端 js？）
2. nodejs 做服务端的话就用 [Socket. iot](websocket.md#Socket%20io)，但也有[engine. io-server-java]( https://link.juejin.cn/?target=https%3A%2F%2Fgithub.com%2Fsocketio%2Fengine.io-server-java " https://github.com/socketio/engine.io-server-java" )或者 [netty-socketio]( https://link.juejin.cn/?target=https%3A%2F%2Fgithub.com%2Fmrniko%2Fnetty-socketio " https://github.com/mrniko/netty-socketio" )
3. 不管你使用哪一种，都必须保证客户端与服务端同时支持。
[SockJS简单介绍](https://blog.csdn.net/John_62/article/details/78208177)
1. SockJS 提供了浏览器兼容性，不支持 WebSocket 时自动将为轮询
2. spring 提供了对 SockJS 的支持 `@EnableWebSocket` 、`...withSockJS();`
3. 心跳消息
	1. SockJS 要求服务器发送心跳，以阻止代理结束连接
	2. spring SockJS 可配置频率，还可以使用 task 调度心跳任务
	3. 当使用 STOMP 时，如果通过协商来交换心跳，SockJS 的心跳将禁用
##### Socket. io
[socket.io简易教程(群聊，发送图片，分组，私聊](https://blog.csdn.net/neuq_zxy/article/details/77531126)
[WebSocket、socket.io、SSE](https://mp.weixin.qq.com/s?__biz=MzUzMjM5ODk5Nw==&mid=2247487964&idx=1&sn=a00af16226070cc183edacf183be2953)
[Netty Socket.IO](https://blogplus.cn/2022/03/12/hou-duan-yu-qian-duan-xiao-xi-tui-song/)

![流媒体](流媒体.md#^7odefl)
##### STOMP
[STOMP原理与应用开发详解](https://mp.weixin.qq.com/s/3Vr8Po-J_9mdcWrM2g7xIw)
1. STOMP 可以用于任何可靠的双向流网络协议，如 TCP 和 WebSocket，虽然 STOMP 是一个面向文本的协议，但消息 payload 可以是文本或二进制。
2. 就像 HTTP 在 TCP 套接字之上添加了请求-响应模型层一样，STOMP 在 WebSocket 之上提供了一个基于帧的线路格式（frame-based wire format）层，用来定义消息的语义。
3. STOMP 协议并不是为 websocket 设计的, 它是属于消息队列的一种协议, 和 amqp, jms 平级。

###### stompjs
Javascript and Typescript Stomp client for Web browsers and node.js apps

[javascript - websocket+sockjs+stompjs详解及实例 - 个人文章 - SegmentFault 思否](https://segmentfault.com/a/1190000017204277)

[StompJS使用文档总结：如何创建stomp客户端、如何连接服务器、心跳机制、如何发送消息、如何订阅和取消订阅、事务、如何调试](https://www.cnblogs.com/goloving/p/10746378.html)
1. STOMP 消息的 `body` 必须为字符串
2. 
[WebSocket 介绍以及配合 STOMP 的使用 | fx-team](https://fx-team.github.io/2018/01/21/websocket%E4%BB%8B%E7%BB%8D%E4%BB%A5%E5%8F%8A%E9%85%8D%E5%90%88Stomp%E7%9A%84%E4%BD%BF%E7%94%A8/)

###### stomp over websocket
[webSocket进阶篇——STOMP Over Websocket - 简书](https://www.jianshu.com/p/32fae52c61f6)

![mqtt over websocket](mqtt.md#mqtt%20over%20websocket)

[WebSocket STOMP | Kindrage's blog](https://win11.ren/2023/01/31/websocket/stomp/)
1. websocket 支持文本和字节两种类型的数据，但是消息内容格式没有规定
2. WebSocket 支持 client 和 server 协商一个子协议来发送消息, 子协议可以更好的约定消息内容的格式.
3. stomp 就可以作为子协议，stomp 也支持文本和字节
4. springboot（spring）支持 [5](https://win11.ren/2023/01/31/websocket/stomp/#fn:5),了 websocket stomp 服务端
	1. 启用基于 WebSocket 的 STOMP `@EnableWebSocketMessageBroker`
5. 前端使用 stompjs [2](https://win11.ren/2023/01/31/websocket/stomp/#fn:2) 支持 stomp v1.0, v1.1, v1.2
6. 同一客户多客户端订阅同一地址时，只向一个客户端发送消息
7. nginx 代理 websocket




[为何Websocket一般会配合STOMP协议？ - 墨天轮](https://www.modb.pro/db/165467) 
1. 使用 http 浏览每次访问后端都要携带 http 的请求头信息，响应要带响应头
2. ws 定义了两种传输信息类型：文本信息和二进制信息。类型虽然被确定，但是他们的传输体是没有规定的，也就是说传输体可以自定义成什么样的数据格式都行
3. 只要客户端和服务端约定好，得到数据后能够按照约定的语义解析数据就好。就好比 Http，约定好了必须有请求头、请求体这类语义，接收到数据后先要解析头信息。
4. 我们可以使用 json 作为传输体格式
5. 就像 HTTP 在 TCP 套接字之上添加了请求-响应模型层一样，STOMP 在 WebSocket 之上提供了一个基于帧的线路格式（frame-based wire format）层，用来定义消息的语义
6. 服务端和客户端按照这种格式去解析数据即可，而且提供的信息也比我们刚刚定义的 JSON 作为传输体丰富，比如 SEND 表示这是一条发送给服务器的信息，数据是“hello queue a”。

[SpringBoot实现STOMP协议下的WebSocket](https://blog.csdn.net/weixin_40693633/article/details/91512632)
1. 对 `WebSocketMessageBrokerConfigurer` 注释较全

[1、SpringBoot+WebSocket+STOMP+VUE实现双通道通信](http://blog.shangsw.com/archives/articles202006261593171340075html)
1. 代码比较全

[springboot中通过stomp方式来处理websocket及token权限鉴权相关](https://www.cnblogs.com/vishun/p/14334142.html)
1. 一些注意点
2. 不推荐再用 sockjs, 以及相关 vue 代码

[Springboot整合WebSocket（基于Stomp）](https://blog.csdn.net/AhangA/article/details/125470930)
1. 使用的这个代码案例，包括原生、stomp、rabbitmq
2. 心跳、ack、事务

[【Java分享客栈】SpringBoot整合WebSocket+Stomp搭建群聊项目 - 程序员济癫 - 博客园](https://www.cnblogs.com/fulongyuanjushi/p/16102628.html)
1. 提到了 springboot 版本问题
2. @MessageMapping 注解对应客户端的 stomp. send ('url')；
	1. 用法一：要么配合@SendTo ("转发的订阅路径")，去掉 messagingTemplate，同时 return msg 来使用，return msg 会去找@SendTo 注解的路径；
	2. 用法二：要么设置成 void，使用 messagingTemplate 来控制转发的订阅路径，且不能 return msg，个人推荐这种。

[Spring Websocket+Stomp 防踩坑实战 - 简书](https://www.jianshu.com/p/d4860176af07)
1. 自定义用户方便在发送消息时，通过用户名来找到当前节点的在线用户数据；
2. 上面网关转发的功能已完成，还存在一个问题就是微服务架构下，多个服务节点，如何给在线用户发送及时消息；这里我们主要用的 redis 的订阅与发布，大家也可以使用**Mq 消息队列**处理；

[Spring WebSocket简析 | Tony's toy blog](https://www.tony-bro.com/posts/3568303861/index.html)
1. 处理流程
2. 参数注释

[websocket与STMOP的比较及使用步骤](https://blog.csdn.net/achenyuan/article/details/80851512)
##### Stomp on Spring WebSocket
`.withSockJS()` 提供了降级为轮询的

[Stomp on Spring WebSocket项目源码分析 | 瓴岳技术团队](https://blog.fintopia.tech/60dc33852078082a378ec5eb/)

##### netty
[十块钱帮我做个视频网站吧，就带弹幕那种？](https://binhao.blog.csdn.net/article/details/112631642)
[自从Java有了Netty，实现Websocket推送消息不再愁，值得收藏 - 知乎](https://zhuanlan.zhihu.com/p/254646641) [「Netty」实战 Springboot + netty +websocket 推送消息（附源码）\_暖风ii的博客-CSDN博客](https://blog.csdn.net/weixin_44912855/article/details/122667977)

## 集群

##### websocket 集群
1. [分布式WebSocket集群解决方案_weixin_34194702的博客-CSDN博客](https://blog.csdn.net/weixin_34194702/article/details/88701309)
3. [使用 Spring Cloud Gateway 替换 Zuul 实现接入 WebSocket 教程](https://www.jianshu.com/p/716e0600c3aa)
4. [一个注解，优雅的实现 WebSocket 集群！](https://mp.weixin.qq.com/s/K3SqYPjaPP5kC-O6Cc3kEA)



## reference
[完全理解TCP/UDP、HTTP长连接、Websocket、SockJS/Socket.IO以及STOMP的区别和联系 - 掘金](https://juejin.cn/post/6844903969408434189)
1. 为什么不适用 http 长连接 comet
	1. http 1.1 中规定，客户端不该与服务端建立超过两个的 http 连接，新连接会阻塞
	2. 对于服务端来说，每个长连接都占用一个用户线程
2. 为什么不直接使用 sokcet 编程
	1. socket 针对 cs，浏览器没法发起 socket 请求
3. SockJS/Socket.IO
4. STOMP over Websocket 是什么？
	1. websocket 建立连接后，用 stomp 协议定义的格式定义内容格式

[VUE+Websocket+Protobuf | hviker](https://hviker.github.io/post/vuewebsocketprotobuf/)
1. 原生的 ws 在由浏览器端向后台发送数据时，后台支持接收的数据类型有 String, ArrayBuffer、Blob、ArrayBufferView，但是借助 stompJS 的实现方式只支持发送 String
2. 经测试，发送 Integer 也是可以接收的。但一般都是经过 json. pase 后都是 str, 也没啥问题。

[消息协议 AMQP 及MQTT ,STOMP，JMS的概念和基本理解 - 呆呆的木鸡 - 博客园](https://www.cnblogs.com/Jyongli/p/15636435.html)
消息协议则是指用于实现消息队列功能时所涉及的协议。按照是否向行业开放消息规范文档，可以将消息协议分为开放协议和私有协议。常见的开放协议有 AMQP, MQTT STOMP,XMPP 等。有些特殊框架（如 Red Kafka ZeroMQ ）根据自身需要未严格遵循 MQ 规范，而是基于 TCP IP 自行封装了 协议，通过网 Socket 接口进行传输，实现了 MQ 的功能。这样的协议可以简单地理解成对双方通信有个约定，比如传过来 衍流数据，其中第几个字节表示什么类似这样的约定。


[芋道 Spring Boot WebSocket 入门](https://www.iocoder.cn/Spring-Boot/WebSocket/?github)
1. [JSR-356](https://www.oracle.com/technical-resources/articles/java/jsr356.html) 规范，定义了 Java 针对 WebSocket 的 API ，即 [Javax WebSocket](https://mvnrepository.com/artifact/javax.websocket) 。提供了 [`@ServerEndpoint`](https://github.com/eclipse-ee4j/websocket-api/blob/master/api/server/src/main/java/javax/websocket/server/ServerEndpoint.java)、 [`@OnOpen`](https://github.com/eclipse-ee4j/websocket-api/blob/master/api/client/src/main/java/javax/websocket/OnOpen.java)、[`@OnMessage`](https://github.com/eclipse-ee4j/websocket-api/blob/master/api/client/src/main/java/javax/websocket/OnMessage.java)、[`@OnClose`](https://github.com/eclipse-ee4j/websocket-api/blob/master/api/client/src/main/java/javax/websocket/OnClose.java)、[`@OnError`](https://github.com/eclipse-ee4j/websocket-api/blob/master/api/client/src/main/java/javax/websocket/OnError.java) 注解。
2. 主流 web 容器都提供了对 jsr-356 的实现，如 tomcat、jetty、undertow 等。
3. 一般在项目中实现 websocket，有以下几种解决方案：
	1. [Tomcat WebSocket](https://www.cnblogs.com/xdp-gacl/p/5193279.html)
	2. [Spring WebSocket](https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/websocket.html)
		1.  内置对 stomp 协议的支持
	3.  [Netty WebSocket](https://netty.io/news/2012/11/15/websocket-enhancement.html)
4. Tomcat WebSocket 使用的是 [Session](https://github.com/eclipse-ee4j/websocket-api/blob/master/api/client/src/main/java/javax/websocket/Session.java) 作为会话，而 Spring WebSocket 使用的是 [WebSocketSession](https://github.com/spring-projects/spring-framework/blob/master/spring-websocket/src/main/java/org/springframework/web/socket/WebSocketSession.java) 作为会话。这两种都需要自己去维护用户 session。
5. 在上述的提供的 Tomcat WebSocket 和 Spring WebSocket 示例中，我们相当于在 WebSocket 实现了自定义的子协议，就是基于 `type` + `body` 的消息结构。而 Spring WebSocket 内置了对 [STOMP](https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/websocket.html#websocket-stomp-overview) 的支持
6. 如何保证消息一定送达给用户？ ^pf6ii8
	1. 基于每一条消息编号 ACK。细看
	2. 基于滑动窗口 ACK。细看
		1. 推拉结合方案，在[分布式消息队列](怎么保证%20 mq%20 消息不丢失%20 (rabbitmq). md)、[配置中心](Nacos%20怎么实现自动刷新.md)、注册中心实现实时的[数据同步](大数据量同步.md)，经常被采用。细看
		2. 并且，采用这种方案的情况下，客户端和服务端不一定需要使用长连接，也可以使用[长轮询](Spring%20异步.md#DeferredResult)所替代。细看

[即时通讯网WebSocket](https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzUzMjM5ODk5Nw==&action=getalbum&album_id=1872065781224505349&scene=173&from_msgid=2247488189&from_itemidx=1&count=3&nolastread=1#wechat_redirect)



[SpringBoot+WebSocket实时监控异常 - Jae1995 - 博客园](https://www.cnblogs.com/jae-tech/p/15409340.html)
1. 一个基于 Java 原生 API 的例子



[XHR，ajax，axios，fetch之间的区别 - 掘金](https://juejin.cn/post/6844904184748195848) ^myodyc
1. XHR
	1. 现代浏览器，最开始与服务器交换数据，都是通过 XMLHttpRequest 对象
2. ajax
	1. 为了方便操作 dom 并避免一些浏览器兼容问题，产生了 jquery， 它里面的 AJAX 请求也兼容了不同的浏览器，可以直接使用. get、. pist。它就是对 XMLHttpRequest 对象的一层封装
3. axios
	1. 基于 promise 的 HTTP 库，可以用在浏览器和 node. js 中。它本质也是对原生 XMLHttpRequest 的封装，只不过它是 Promise 的实现版本，符合最新的 ES 规范。
4. fetch
	1. Fetch API 提供了一个 JavaScript 接口，用于访问和操作 HTTP 管道的部分，例如请求和响应。它还提供了一个全局 fetch ()方法，该方法提供了一种简单，合理的方式来跨网络异步获取资源。
	2. fetch是低层次的API，代替XHR，可以轻松处理各种格式，非文本化格式。






