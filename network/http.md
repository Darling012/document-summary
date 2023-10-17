# http
##### 问题
1. 服务端怎么配合 http2 的并行请求？

##### 无线通信的三种通信方式
1. 单工： 数据传输只允许在一个方向上的传输，只能一方来发送数据，另一方来接收数据并发送。
2. 半双工：数据传输允许两个方向上的传输，但是同一时间内，只可以有一方发送或接受消息。
3. 全双工：同时可进行双向传输。例如：`websocket`
###### http协议是什么工作模式呢
1. `http1.0`：单工。因为是短连接，客户端发起请求之后，服务端处理完请求并收到客户端的响应后即断开连接。
2. `http1.1`：半双工。默认开启长连接`keep-alive`，开启一个连接可发送多个请求。
3. `http2.0`：全双工，允许服务端主动向客户端发送静态数据。

![序列化与编解码](序列化与编解码.md#前置知识)

## http 1.x

##### 浏览器限制同源请求并发 tcp 连接数问题

[HTTP/1.x 的响应](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Connection_management_in_HTTP_1.x)都是序列化的，想要服务器更快做出响应就要多建立连接，而无限制建立连接会触发服务器 DoS 保护的风险。
[HTTP/1.1 标准推荐的是 2](https://blog.51cto.com/u_15072763/4030621)， [不同浏览器限制是不一样的。](https://juejin.cn/post/6844904192633487367)比如 chrome 下载静态资源，6 个请求在 6 个连接上一组，可以通过域名分片优化。
此问题同[前端请求大量数据场景](大数据量同步.md#大数据量同步)。 ^oj1loi

[使用HTTP/2.0解决浏览器限制同源HTTP/1.x连接并发个数的问题 - 知乎](https://zhuanlan.zhihu.com/p/77803705)

### keep-alive
1. 1.0 是可以手动开启的
2. 1.1 默认开启
3. [HTTP/1.0](https://blog.51cto.com/liuyu/279164) keep-alive 长连接，[HTTP/1.0 中是默认关闭的](https://juejin.cn/post/6989985247836241957)，可以通过 `Connection: keep-alive;` 开启 ，HTTP/1.1 默认是开启的，无论加没加 `Connection: keep-alive;`

[HTTP长连接和短连接 - WhyWin - 博客园](https://www.cnblogs.com/0201zcr/p/4694945.html)
1. http 的长连接和短连接都是基于 tcp 的长连接和短连接
2. tcp 短连接
	1. client 和 server 在一次读写后都可发起 close 操作，一般是 client 发起
3. tcp 长连接
	1. 在一次读写操作后，server 保留半开放连接，在超时后向 client 发送探测报文
4. 长短连接优缺点
5. 怎么选择

##### http 的 keep-alive 和 [TCP 的 Keepalive](TCP.md#KeepAlive)

[HTTP 长连接和 TCP 长连接有区别？](https://mp.weixin.qq.com/s/6Az2B2gUJYW7od4cLDqOxg)
http 的 keep-alive 是由**应用层（用户态）** 实现的，称为 HTTP 长连接
1. http 是请求-应答模式，基于 tcp 传输协议实现
2. http 短连接就是：tcp 三次握手建立连接-》请求-》响应-》tcp 四次挥手断开连接
3. http 加入 keep-alive 的长连接就是一次 tcp 连接，多次请求响应
4. 以及实现了 http 流水线技术，即一次性发送多个请求，服务器按顺序响应。
5. 为了避免 http 长连接只发送一次请求后就闲置，加入超时机制 `keepalive_timeout`
TCP 的 Keepalive，是由 **TCP 层（内核态）** 实现的，称为 TCP 保活机制
1. TCP 保活机制可以在双方没有数据交互的情况，通过探测报文，来确定对方的 TCP 连接是否存活
2. 这个工作是在内核完成的

[TCP（HTTP）长连接和短连接区别](https://www.cnblogs.com/JCpeng/p/15158795.html)
提到了 tcp 的 Keepalive 的保活机制和 tcp 长连接关系
1. tcp 短连接
	1. 一般都是 client 先发起 close 操作
2. 长连接
	1. 不出现异常情况或由用户（应用层）主动关闭就不会关


[KeepAlive详解 - 简书](https://www.jianshu.com/p/9fe2c140fa52)
tcp的keepalive是在ESTABLISH状态的时候，双方如何检测连接的可用行。而http的keep-alive说的是如何避免进行重复的TCP三次握手和四次挥手的环节。
[TCP和HTTP中的KeepAlive机制总结](https://xie.infoq.cn/article/398b82c2b4300f928108ac605) 提到 `keepalive` 请求头是 `hop-by-hop` 类型，即 从浏览器到 nginx 再到 tomcat, 只在当前请求头有效。

###### wireshark 抓包分析
**postman**
![](Pasted%20image%2020230817164720.png)

**idea 的 restfultool**
![](Pasted%20image%2020230817162843.png)

**chrome**
![](Pasted%20image%2020230817164443.png)

其中，idea 没有心跳包，修改 postman 和 chrome 请求头 `Connection:close` 后不会出现心跳包。

==几个问题：==
1. [postman ](https://blog.csdn.net/J_YongBoy/article/details/125743871) 和 [chrome](https://blog.csdn.net/weixin_43275277/article/details/119858417) 会建立两个 tcp 连接，idea 的 restfultool 不会。
2. postman 在三次握手和四次挥手之间会有心跳包，但 chrome 咋在四次挥手后还有啊？
	1. 测了一下 edge, 也是在三四之间，估计 chrome 问题
	2. [claude回答的](Pasted%20image%2020230824172155.png)
3. 为什么 http 打开 keep-alive 后，会影响 tcp 出现 Keepalive 心跳包？
4. 以及为啥这心跳包的频率跟[操作系统默认值](https://kibazen.cn/tcp-zhong-de-keepalive-ji-zhi/)不一样

[这篇](https://blog.csdn.net/w903328615/article/details/124516618)说到 34 问题，但没解答，并且，按照已有认识，tcp 的 keep-alive 是 ESTABLISH 阶段，而 http 是复用连接，确实没啥关系啊。
[这篇给了个思路](https://blog.isayme.org/posts/issues-4/)因为要复用连接，所以 tcp 发送探测报文检测是否正常？
[这里](https://www.lixueduan.com/posts/network/06-http-flow/)提到，为了防止连接被关闭，客户端会自动发送 Keep-Alive 请求来保持连接。
[这篇](https://hengyun.tech/why-we-need-heartbeat/)证明了一个正常的 socket 连接，心跳会受 tcp 三个参数影响
[这篇](https://zhuanlan.zhihu.com/p/224595048)说每隔1秒，发生一对 TCP 请求的来回，用来维护 TCP 连接的可用性。保证和等待该 TCP 连接被复用。

综上我觉得，既然 http 连接复用，就得保证断开前连接正常，而 tcp 的三个参数粒度太大了，所以开启 http 长连接后，应该会影响 tcp 探测包的发送频率。并且是客户端实现的心跳。也就是
==应用层心跳==
[这篇](https://www.jianshu.com/p/73cc4482cd52)提到，有了 TCP 提供了 KeepAlive 机制, 为什么还需要应用层的心跳保活？但还是没有具体指明两个 keepalive 的关系。
[这篇](https://zhuanlan.zhihu.com/p/542380273)提到，tcp 长短连接通过使用应用层心跳包或 tcp 自带的 KeepAlive 保活机制以及 close 函数的运行时机来控制。
结合 [TCP的keepalive](TCP.md#关于心跳) 只能检测连接存活，不能检测是否可用。

加之，chrome 和 postman 发送心跳的频率不一样。
那么问题就转化为，不同客户端，http1.1 的 keepalive 怎么实现的心跳？

用 axios 做了下测试，跟 chrome 一致。不能排除是不是打开网页点击按钮发送请求的原因。
用 apache httpclient 发送请求，不会出现心跳。

嗯，下一步就是，http 客户端怎么保证长连接可用？
[用netty实现心跳](https://www.cnblogs.com/superfj/p/9153776.html)，但是是 `[PSH，ACK]` 指令，并不是 `[TCP Keep-Alive]`。

![](Pasted%20image%2020230818160701.png)
看来 postman 和 chrome 类似 `claude` 回答的第一种情况。
但又引出另外一个问题，既然 tcp 的 keepalive 存在弊端，为什么 postman 和 chrome 还采用？

[Chrome对TCP连接的保活机制](https://blog.chionlab.moe/2016/11/07/tcp-keepalive-on-chrome/)
1. Chrome 对于可复用的 TCP 连接，采用的保活机制是 TCP 层（传输层）自带的 Keepalive 机制，通过 TCP Keepalive 探测包的方式实现，而不是在七层报文上自成协议来传输其它数据。
2. 通过使用TCP Keepalive机制，来避免污染七层（应用层）的传输数据。

[HTTP keep-alive、TCP Keep-Alive、心跳检测，傻傻分不清？](https://blog.csdn.net/sslulu520/article/details/117980443)
1. Chrome 限制的是 6 个 tcp 连接
2. Chrome 浏览器会通过 TCP keep-alive 来检测连接
3. TCP Keep-Alive 的功能完全可以通过应用层的心跳检测功能来实现，并且能够更加灵活，因为 TCP Keep-Alive 功能还要依赖操作系统的设置，而应用层协议完全可以自己实现。它们本质上都是做了连接有效性检测的事。所以它们只是做的事情相同，本身并没有什么关系。



### 队头阻塞
队头阻塞是 http 请求应答模式导致的，与长短连接无关。半双工只能一发一收，就形成了一个先进先出的串行队列。当队首请求消耗大量时间，那后续请求就会阻塞。
1. 进行优化就是并行请求，建立并发连接。RFC 2616 规范限制 2 个，但浏览器一般限制 6-8 个。RFC 7230 取消了 2 个的限制。
2. 还可以进行域名分片，这样每个子域名都可以建立 6 个连接。但每个连接还是受队头阻塞影响。
3. pipeline 并没有浏览器实现，所以没啥用


### Range

[基于 HTTP Range 实现视频分片快速播放！ - luch的博客](https://www.quanzhan.co/archives/572)
[视频播放、断点续传、多线程下载实现基础：Range](https://segmentfault.com/a/1190000018451249)


![大文件上传](大文件上传.md#HTTP%20Range)

## http 2.0
[你不知道的 Http 2.0 - 掘金](https://juejin.cn/post/7199106370826125372)
1. http 2.0 多路复用下，数据被拆分成一帧一帧进行无序传输，快接口的数据帧和慢接口的数据帧，会在流上同时传输。
2. 本来加载快的资源，可能存在等待加载慢的资源的现象。
3. 这里用"可能"，以及自己的疑问，虽然是多个接口用的同一个连接，即便每个接口数据都被分帧了，但快接口数据也有可能早就全部返回。
4. 并且 http 2 有个流优先级的概念
![](Pasted%20image%2020230831155722.png)
### 性能优化
[假如HTTP/2已经普及](https://jelly.jd.com/article/6006b1035b6c6a01506c87aa)

[从页面加载到数据请求，前端页面性能优化实践分享 - 葡萄城技术博客](https://www.grapecity.com.cn/blogs/spreadjs-front-end-page-performance-optimization-practice-sharing)

[HTTP/2：新的机遇与挑战](https://imququ.com/post/http2-new-opportunities-and-challenges.html)

[从浏览器如何下载资源来浅析HTTP2的新特性 - 古兰精 - 博客园](https://www.cnblogs.com/goloving/p/14685877.html)

[HTTP 请求之合并与拆分技术详解](https://cloud.tencent.com/developer/article/1837260)
对比了前端加载页面，页面包含图片、css、js 不同协议的耗时
1. HTTP/1.1 合并 VS 拆分
2. HTTP/1.1 VS HTTP/2 并发请求
3. HTTP/2 合并 VS 拆分
4. 浏览器并发 HTTP/2 请求数（大量（300 个图） VS 少量（30 个图））时，其他请求的耗时

[合并HTTP请求 vs 并行HTTP请求，到底谁更快？](https://segmentfault.com/a/1190000015665465)
1. DNS解析(T1) -> 建立TCP连接(T2) -> 发送请求(T3) -> 等待服务器返回首字节（TTFB）(T4) -> 接收数据(T5)。

[Web 开发者的 HTTP/2 性能优化指南 - 掘金](https://juejin.cn/post/6844903427126853645)
[HTTP/2 与 WEB 性能优化（一） | JerryQu 的小站](https://imququ.com/post/http2-and-wpo-1.html)
[HTTP/2 与 WEB 性能优化（二） | JerryQu 的小站](https://imququ.com/post/http2-and-wpo-2.html)
### 使用
[HTTP2.0学习 与 Nginx和Tomcat配置HTTP2.0 - 自由早晚乱余生 - 博客园](https://www.cnblogs.com/operationhome/p/12577540.html)
[Spring Boot 启用 http2 协议 | wangdaye'blog](https://www.wangdaye.net/archives/s-p-r-i-n-g--b-o-o-t--qi-yong--h-t-t-p-2--xie-yi)

## http 3
[HTTP/3 来了 ！HTTP/2 还没怎么用起来呢，先一起扫个盲吧！](https://mp.weixin.qq.com/s?__biz=MzAxODcyNjEzNQ==&mid=2247493552&idx=1&sn=d114406d45483f006bd0fca26ac8aca2)
[从HTTP到HTTP/3的发展简史](https://mp.weixin.qq.com/s/ROiUE9qUEo7rYdOME0c2Rg)

## https

[https](https.md)
##### https 和 http.x 的关系
[node.js - What is difference between httpS and http/2? - Stack Overflow](https://stackoverflow.com/questions/53488601/what-is-difference-between-https-and-http-2)
HTTP/2, like HTTP/1.1, is available over unencrypted (http://) and encrypted (https://) channels but web browsers only support it over HTTPS, where it is decided whether to use HTTP/1.1 or HTTP/2 as part of the HTTPS negotiation at the start of the connection.

file:///Volumes/resources/go/228.Vue+Go 前端后端一体化 企业级微服务网关项目~11/第5章 实时通讯 websocket+安全加密 https【向 nubility 出发~】/5-4 严守秘密 - 一章读懂 https、http2、http1.1之间区别.mp4
1. https 和 http 的区别
2. http 1.1 和 http 2 的区别
	1. http/2 采用二进制格式，http 1.1 是文本格式
	2. http/2 使用**一个连接**可实现多路复用
	3. http/2 使用报头压缩
	4. http/2 push 机制
3. http 2 和 https 的关系
	1. 启用 https 不一定使用 http 2
	2. 但使用 http 2 必须启用 https (浏览器强制)
4. http 2 设计目标
	1. 

## reference
[HTTP五连问](https://mp.weixin.qq.com/s?__biz=MzIzMzgxOTQ5NA==&mid=2247488791&idx=2&sn=8b5b9f17a63223960878f70164fada8e&chk)
1. 现代浏览器在与服务器建立了一个 TCP 连接后是否会在一个 HTTP 请求完成后断开？什么情况下会断开？
	1. 1.0 会断开
	2. 1.1 增加了 `Connection: keep-alive` 后，tcp 不会断开没有 ssl 开销
	3. 1.1 默认开启长连接，只有请求头中带有 `Connection: close` 才会关闭连接
2. 一个 TCP 连接可以对应几个 HTTP 请求？
	1. 1.1 后多个
3. 一个 TCP 连接中 HTTP 请求发送可以一起发送么（比如一起发三个请求，再三个响应一起接收）？
	1. 1.1 发送多个请求是串行的，即请求响应，请求响应。
	2. 1.1 规定了 Pipelining，即一个长连接中可以发送多个请求，不用等待响应，但是服务器必须按照收到的请求顺序发送响应。
		1. 浏览器默认关闭，因为存在比如第一个到达服务器的请求处理时间长，那其他请求要阻塞。
	3. http2 提供了 Multiplexing，可以在同一个 tcp 连接中并行请求。
	4. 1.1 时代提高页面加载效率
		1. 建立多个 tcp 连接，但应该存在[浏览器限制同一 Host HTTP/1.x 建立tcp连接数的问题](WEB即时通讯.md#浏览器限制同一%20Host%20HTTP%201%20x%20建立tcp连接数的问题)
		2. 维持已建立 tcp 连接，同一个连接上顺序处理多个请求。
4. 为什么有的时候刷新页面不需要重新建立 SSL 连接？
5. 浏览器对同一 Host 建立 TCP 连接到数量有没有限制？
	1. 有，[浏览器限制同一 Host HTTP/1.x 建立tcp连接数的问题](WEB即时通讯.md#浏览器限制同一%20Host%20HTTP%201%20x%20建立tcp连接数的问题)
	2. 如果发现用不了 HTTP2 呢？或者用不了 HTTPS（现实中的 HTTP2 都是在 HTTPS 上实现的，所以也就是只能使用 HTTP/1.1）。那浏览器就会在一个 HOST 上建立多个 TCP 连接，连接数量的最大限制取决于浏览器设置，这些连接会在空闲的时候被浏览器用来发送新的请求，如果所有的连接都正在发送请求呢？那其他的请求就只能等等了。

[51 张图助你彻底掌握 HTTP 协议](https://mp.weixin.qq.com/s/MMxcyENbxHpMnLFGFcVqQQ)


[HTTP 入门到进阶](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247488827&idx=2&sn=db2717933de54bfbe94f6452ff88a246)
[你还在为 HTTP 的这些概念头疼吗？](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247488891&idx=2&sn=75833e3691d4478c3c69227fccb077b0)
[GET和POST两种基本请求方法有什么区别](https://mp.weixin.qq.com/s?__biz=MzU0MzQ5MDA0Mw==&mid=2247493926&idx=2&sn=c735e4b0fd39903862b7bb2ea28bf7d3)

[http协议无状态中的 "状态" 到底指的是什么？！ - 赛艇队长 - 博客园](https://www.cnblogs.com/bellkosmos/p/5237146.html)
[短URL服务的设计以及实现 - 掘金](https://juejin.cn/post/6844903873950269454)


