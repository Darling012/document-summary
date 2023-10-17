# TCP
==什么是 tcp==
面向连接 可靠的 字节流 传输 
## 头部报文
1. source port
2. destination port
3. sequence number
	1. 序列号
	2. 分割的数据段在原始数据包的位置
4. Acknowledgment Numbe
	1. 确认序列号
	2. 接收确认端所期望收到的下一序列号，且只有标志位 ack 为 1 时有效
5. tcp flag 标志位
	1.  ACK（Acknowledge Character），确认字符
	2. SYN（Synchronize Sequence Numbers），同步序列编号；
	3. FIN
	4. URG
	5. PSH
	6. RST
		1. 当 RST=1，表明 TCP 连接中出现严重差错，必须释放连接，然后再重新建立连接；

[就是要你懂TCP--握手和挥手 | plantegg](https://plantegg.github.io/2017/06/02/%E5%B0%B1%E6%98%AF%E8%A6%81%E4%BD%A0%E6%87%82TCP--%E8%BF%9E%E6%8E%A5%E5%92%8C%E6%8F%A1%E6%89%8B/)
## 三次握手
要确认双方都具备
1. 发送数据能力
2. 接收数据能力
A->B 发数据，B 收到，证明 A 有发送能力
B->A 发数据，A 收到，证明 B 有接收和发送的能力
A->B 再发数据，证明 A 有接收的能力
[两张动图-彻底明白TCP的三次握手与四次挥手](https://blog.csdn.net/qzcsu/article/details/72861891)
==为什么不是两次==
没法确定客户端接收的能力。
1. 假如客户端发送后一个包后，这个包在网络里滞留
2. tcp 以为丢包，重传，然后两次握手建立连接
3. 然后发完数据后，断开连接，这时第一个包到达服务端，按两次握手，服务端会建立连接，但客户端其实已经关闭了。
==为什么至少三次==
1. 防止重复连接
2. [同步初始化序列化](https://mp.weixin.qq.com/s/ZLXzvnaUq3n8kDHmIVcdKw)
==流程==
1. 最开始客户端和服务端都是 `close` 状态，然后服务端先主动开始监听某个端口，服务端处于 `listen` 状态
2. 然后客户端主动发起连接，发送 `SYN`，此时客户端变为 `SYN-send` 状态
3. 服务端接收到信息后<sub>（客发）</sub>返回 `SYN` 和 `ACK`，此时服务端变为 `SYN-rcvd` 状态
4. 客户端接收到消息后<sub>（服收发）</sub>，再次发送 `ACK` 到服务端，此时客户端变为 `established` 已确认状态
5. 服务端收到 `ACK` 后<sub>（客收）</sub>，也变为 `established` 已确认状态
[TCP三次握手中SYN，ACK，seq ack的含义 - 陈木 - 博客园](https://www.cnblogs.com/muyi23333/articles/13841268.html)

## 四次挥手

###### 为什么 TIME_WAIT 等待的时间是 2MSL？
[4.1 TCP 三次握手与四次挥手面试题 | 小林coding](https://www.xiaolincoding.com/network/3_tcp/tcp_interview.html#%E4%B8%BA%E4%BB%80%E4%B9%88-time-wait-%E7%AD%89%E5%BE%85%E7%9A%84%E6%97%B6%E9%97%B4%E6%98%AF-2msl)

`last_ack` 超时重发是多久以及其他超时时间怎么定义的？
[TCP三次握手及四次挥手过程中的异常处理](https://blog.csdn.net/ArtAndLife/article/details/120004631)
`当服务端收到客户端的第二次挥手的FIN报文后`，第二次是是 ACK 报文啊!



[图解TCP、UDP，流量控制，拥塞控制，一次看懂](https://mp.weixin.qq.com/s/I-mwnyEBB_Uj4hxugsvEZw)
[TCP 三次握手](https://mp.weixin.qq.com/s/sqkYBM-4l4qFFPkjY_zCJA)
[TCP为什么是四次挥手，而不是三次？ - 知乎](https://www.zhihu.com/question/63264012)
1. 主动方发送 `fin` 报文给被动方，表示主动方不再发送数据，但主动方还可以接收数据
2. 被动方收到后向主动方发送 `ack` 报文，表示知道主动方想断开连接。主动方收到后就不再发送 `fin` 报文，且被动方有可能还有报文需要发送给主动方。
3. 被动方发完剩下的报文后，发送 `fin` 报文给主动方，被动方进去 `last_ack` 超时等待状态
4. 主动方收到后发送 `ack` 报文进行中断确认，被动方收到就会直接释放连接。

TCP 的关键是，要在不可靠网络中建立可靠的数据传输。
为了适应物理设备单次数据包传输上限，引入了分批传输，是单来了有序传输的问题。为了实现有序传输，引入了 seq。为了实现可靠传输，引入了重传。为了尽可能减少重传，引入了 ACK。为了保证重传最高不可太多，引入了重试次数。

[ TCP 三次握手过程](https://mp.weixin.qq.com/s/5VXhL0dTFcWNyfQ7-7NBEg)
[TCP 四次分手过程](https://mp.weixin.qq.com/s/CrnaB1R3hkfQdBvD8OyqXw)
[(建议收藏)TCP协议灵魂之问，巩固你的网路底层基础 - 掘金](https://juejin.cn/post/6844904070889603085#heading-51)

[Wireshark抓包：详解TCP四次挥手报文内容](https://mp.weixin.qq.com/s/Gc3Po3wIfV1lMjnYvsU1OA)

## KeepAlive
###### tcp 的 Keepalive 的 保活机制和 tcp 长连接关系
TCP 本身并没有长短连接的区别
[Tcp长连接和keepalive - 简书](https://www.jianshu.com/p/0c4de17dd6bc)
[采坑之 TCP 长连接和 KeepAlive 心跳机制](https://whoosy.cn/2020/04/28/%E8%AE%A1%E7%AE%97%E6%9C%BA%E7%BD%91%E7%BB%9C/tcp%E9%95%BF%E9%93%BE%E6%8E%A5%E5%92%8Ckeepalive%E5%BF%83%E8%B7%B3%E6%9C%BA%E5%88%B6/)

[关于TCP连接的Keepalive和reset | plantegg](https://plantegg.github.io/2018/08/26/%E5%85%B3%E4%BA%8ETCP%E8%BF%9E%E6%8E%A5%E7%9A%84KeepAlive%E5%92%8Creset/)

### 关于心跳

为什么 TCP keepalive 不能替代应用层心跳？心跳除了说明应用程序还活着（进程还在，网络通畅)，更重要的是表明应用程序还能正常工作。而 TCP keepalive 由操作系统负责探查，即便进程死锁或阻塞，操作系统也会如常收发 TCP keepalive 消息。对方无法得知这一异常。---《Linux多线程服务端编程》

[不要使用TCP的KeepAlive功能\_liuyuan185442111的博客-CSDN博客](https://blog.csdn.net/liuyuan185442111/article/details/117516101)
1. keepalive 只能检测连接是否存活，不能检测连接是否可用。
2. 如果TCP连接中的另一方突然断网，我们并不知道连接断开，此时发送数据失败会进行重传，由于重传包的优先级要高于keepalive的数据包，因此keepalive的数据包无法发送出去。

[Java socket API: How to tell if a connection has been closed? - Stack Overflow](https://stackoverflow.com/questions/10240694/java-socket-api-how-to-tell-if-a-connection-has-been-closed)
[关于TCP连接的Keepalive和reset | plantegg](https://plantegg.github.io/2018/08/26/%E5%85%B3%E4%BA%8ETCP%E8%BF%9E%E6%8E%A5%E7%9A%84KeepAlive%E5%92%8Creset/)

[TCP中已有SO_KEEPALIVE选项，为什么还要在应用层加入心跳包机制?? - 知乎](https://www.zhihu.com/question/40602902)

[TCP Keep-Alive 和 应用层探活 - 简书](https://www.jianshu.com/p/00aec37b6be8) 关联极客时间课程

## 全连接队列和半连接队列

[就是要你懂TCP--半连接队列和全连接队列 | plantegg](https://plantegg.github.io/2017/06/07/%E5%B0%B1%E6%98%AF%E8%A6%81%E4%BD%A0%E6%87%82TCP--%E5%8D%8A%E8%BF%9E%E6%8E%A5%E9%98%9F%E5%88%97%E5%92%8C%E5%85%A8%E8%BF%9E%E6%8E%A5%E9%98%9F%E5%88%97/)


## TCP 重传、滑动窗口、流量控制、拥塞控制
[4.2 TCP 重传、滑动窗口、流量控制、拥塞控制 | 小林coding](https://www.xiaolincoding.com/network/3_tcp/tcp_feature.html#%E9%87%8D%E4%BC%A0%E6%9C%BA%E5%88%B6)
1. 重传机制
	1. 超时重传
		1. 定时器超时重传
	2. 快速重传
		1. 发送方收到接收方未接到包的ACK消息，收到三次后就会在超时前重传，但存在重传一个还是所有的问题
	3. SACK方法
		1. 选择性确认，即在 TCP 头部「选项」字段里加一个 `SACK` 的东西，它**可以将已收到的数据的信息发送给「发送方」**，这样发送方就可以知道哪些数据收到了，哪些数据没收到，知道了这些信息，就可以**只重传丢失的数据**。
	4. Duplicate SACK
		1. 主要**使用了 SACK 来告诉「发送方」有哪些数据被重复接收了。**
2. 滑动窗口
	1. 窗口大小就是指**无需等待确认应答，而可以继续发送数据的最大值**。窗口的实现实际上是操作系统开辟的一个缓存空间，发送方主机在等到确认应答返回之前，必须在缓冲区中保留已发送的数据。如果按期收到确认应答，此时数据就可以从缓存区清除。
3. 流量控制
	1. 背压。
4. 拥塞控制
## reference
[《TCP/IP详解 卷1：协议》在线阅读版（全网唯一） - 即时通讯开发者社区!](http://www.52im.net/topic-tcpipvol1.html)