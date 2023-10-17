# BIONIOAIO

## I/O 访问技术 
零拷贝（Zero Copy）是指计算机执行操作时，CPU 不需要先将数据从某处内存复制到另外一个特定区域。
IO 技术的演进就是减少 cpu 切换和数据拷贝。

1. 缓冲 IO (Buffered I/O)
	1. 读：外部➡内核 Page Cache ➡用户缓冲区
	2. 写：用户缓冲区➡socket 缓冲区➡外部
2. 直接内存访问DMA（Direct Memory Access）
	1. 在 cpu 和硬件之间加一层，cpu 发起请求后立马返回执行其他操作
	2. CPU 不再参与「将数据从磁盘控制器缓冲区搬运到内核空间」的工作，这部分工作全程由 DMA 完成
3. 直接 IO (Direct I/O) 
	1. 用户态直接 I/O 使得应用进程或运行在用户态（user space）下的库函数直接访问硬件设备，数据直接跨过内核进行传输，内核在数据传输过程除了进行必要的虚拟存储配置工作之外，不参与任何其他工作，这种方式能够直接绕过内核，极大提高了性能。
4. 内存映射 (Memory-Mapped, mmap)
	1. `mmap()` 系统调用函数会直接把内核缓冲区里的数据「**映射**」到用户空间，这样，操作系统内核与用户空间就不需要再进行任何的数据拷贝操作。
5. 零拷贝 (Zero Copy)
	1. sendfile
	2. sendfile + SG-DMA
	3. splice

![](Pasted%20image%2020230830095126.png)

[9.1 什么是零拷贝？ | 小林coding](https://www.xiaolincoding.com/os/8_network_system/zero_copy.html)
1. 传统 IO
	1. 用户进程一直阻塞
	2. cpu 发起 IO 请求给磁盘后阻塞等待, 然后两阶段拷贝数据
	3. `read()` 读从用户态切到内核，数据拷贝完再从内核切回用户
2. DMA
	1. cpu 发起 IO 请求给 DMA，然后干别的
	2. DMA 发 IO 请求给磁盘，并将数据拷贝到内核缓冲区
	3. cpu 只需要从内核拷贝到用户，不再参与从磁盘到内核
	4. 用户进程一直阻塞，两次切换
3. 基于以上的传输文件需要从磁盘到网卡需要 先 `read()` 再 `write()`
	1. 四次切换，四次拷贝
	2. 调用 `read()` 从用户切换到内核第一次 1，调用完再切回去执行别的第二次， `write()` 一样也两次
	3. 读操作磁盘缓冲区到 pageCache DMA 拷贝 1 次，内核到用户 cpu 拷贝一次
	4. 写操作用户到内核 cpu 拷贝一次，内核到 socketCache DMA 拷贝一次
4. 怎么减少切换和拷贝呢？
	1. mmap + write
		1. mmap 直接将 pageCache 映射到用户空间
		2. 所以 cpu 在内核空间直接从 pageCache 拷贝到 socketCache
		3. 四次切换，三次拷贝
	2. sendfile
		1. 代替 `read()` 和 `write()`，减少一次系统调用即两次切换
		2. 同样支持 cpu 在内核空间直接从 pageCache 拷贝到 socketCache
		3. 两次切换，三次拷贝
	3. sendfile 更进一步，假如网卡支持 SG-DMA
		1. 磁盘 DMA 拷贝到 pageCache
		2. SG-DMA 从 pageCache 拷贝到网卡
		3. 两次切换，两次拷贝。且两次拷贝不需要 cpu ，都是 DMA 传输
5. 案例
	1. Kafka 最终调用 Java NIO 库里的 `transferTo`，如果 linux 支持，则最终调用 `sendfile()`
	2. nginx 的 `sendfile on` 配置
		1. on, 使用 `sendfile()`，2 切2 拷
		2. off, 使用 `read()+write()`，4 切 4 拷
		3. /Volumes/resources/nginx/63-Nginx 核心知识 100 讲/第五章：Nginx 的系统层性能优化 (18 讲)/134 丨 零拷贝与 gzip_static 模块. mp 4


[深入剖析Linux IO原理和几种零拷贝机制的实现 - 知乎](https://zhuanlan.zhihu.com/p/83398714)


[Linux IO体系、零拷贝和虚拟内存关系的重新思考\_男孩NoBad的博客-CSDN博客](https://blog.csdn.net/lixiaodong037/article/details/114434041)
[21.你管这破玩意叫mmap？ - 《计算机底层的秘密》 - 书栈网 · BookStack](https://www.bookstack.cn/read/webxiaohua-gitbook/21.-ni-guan-zhe-po-wan-yi-jiao-mmap.md)
[22.彻底理解零拷贝 - 《计算机底层的秘密》 - 书栈网 · BookStack](https://www.bookstack.cn/read/webxiaohua-gitbook/22.-che-di-li-jie-ling-kao-bei.md)

[04 | 零拷贝：如何高效地传输文件？](https://leeshengis.com/archives/232676)

## 与 IO 模型关系

![](Pasted%20image%2020230830100118.png)

[IO的多路复用和零拷贝冲突么？ - 知乎](https://www.zhihu.com/question/412600983/answer/2363402250)
[高效IO之零拷贝技术-腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1771790)

[linux 下 mmap、sendfile](https://blog.csdn.net/LiushaoMr/article/details/104833463)
1. 有了mmap之后，用户态发起select调用不需要传递fds，当产生新的fd时，只需要将fd放入红黑树中，内核直接遍历红黑树中的fd发现有人数据到达了，就fd放入链表中。用户态直接用链表中的fd进行读取。
[Epoll到底用没用到mmap之一 - 知乎](https://zhuanlan.zhihu.com/p/342466197)
##### 总结
IO 操作分两块，发起 IO 请求，实际的 IO 读写。前者的优化就是 IO 模型，从线程状态和通信方式演进，后者即 IO 访问技术，从减少 cpu 切换和数据拷贝演进，两者都是操作系统层面技术。
在此基础上，Java 利用不同的底层技术结合更丰富 IO 线程模型比如 Reactor 模式，发展 NIOAIO。并且像 netty、tomcat 又是建立在 JavaNIO 的基础上。

## 同步、异步、阻塞、非阻塞

同步和异步是被调用方的响应方式，是立马响应还是等待业务完成再响应。
阻塞和非阻塞是调用方是否需要等待，是调用后可做别的事还是要等待响应后才能做别的。

[📔【操作系统】详解阻塞、非阻塞、同步、异步](https://imageslr.com/2019/block-non-blocking-sync-async.html)这篇文章及 reference 里提供的区分这四个词的思路对，一个是操作系统层面，再一个是开发语言层面，开发语言对系统调用有了封装，最主要的是，讨论 IO 模型的优化加上线程时，就不知道是针对谁说的了。

[并发模式 · 异步网络模型](https://xieshren.gitbooks.io/io/content/bing-fa-mo-shi.html)
1. 并发模式中，“同步”指程序按照代码顺序执行，“异步”指程序依赖事件驱动
2. I/O 模型中，“同步”、“异步”用来区分 I/O 操作的方式，是主动通过 I/O 操作拿到结果，还是异步返回结果。

[这次，让我们捋清：同步、异步、阻塞、非阻塞](https://mp.weixin.qq.com/s/bIJQZlbKPxrABMItiog7DQ)
1. 同步和异步
	1. 当前线程是否需要等待方法调用执行完毕
2. 阻塞&非阻塞
	1. 数未就绪时线程是否被阻塞挂起，让出 cpu
3. 所以阻塞和同步看起来都是等，但是本质上它们不一样，同步的时候可没有让出 CPU。
4. 明确 I/O 操作有两个步骤
	1. 发起 I/O 请求
	2. 实际 I/O 读写，即数据从内核缓存拷贝到用户空间
5. 阻塞 I/O 和非阻塞 I/O
	1. 就是第一步，用户线程发起 I/O 请求后，数据没就绪时，让出 cpu 就是阻塞，不让继续执行别的业务就是非阻塞
6. 同步 I/O 和异步 I/O
	1. 就是第二步，数据从内核到用户空间拷贝时，用户线程等着拷完就是同步，不用等，后续收到通知就是异步。
7. IO 场景下的同步和异步其实说的就是内核的实现，一种用户线程等，一种不等内核用回调通知。

## IO 模型

[还在分不清各种IO模型？](https://mp.weixin.qq.com/s/h5goFjIvJd9N5ptCumKEUw)
1. linux IO模型
2. Java中的IO模型

[为什么网络 I/O 会被阻塞？](https://mp.weixin.qq.com/s/BH1H-JUhKqgaAjaIx5k0Fw)
1. I/O 到底是什么?
	1. 磁盘 I/O 指的是硬盘和内存之间的输入输出
	2. 网络 I/O 指的是网卡与内存之间的输入输出
	3. 为什么都要跟内存交互呢?
	4. 指令最终是由 CPU 执行的，再因为速度差距，所以是内存
	5. 总结下：I/O 就是指内存与外部设备之间的交互（数据拷贝）。
2. 为什么网络 I/O 会被阻塞？
	1. 因为建连和通信涉及到的 accept、connect、read、write 这几个方法都可能会发生阻塞。
3. 由于阻塞，起初是多建线程，但会 C10K, 后来，就有了
4. 非阻塞套接字，然后 I/O 多路复用、信号驱动 I/O、异步 I/O。

[让我们，从头到尾，通透I/O模型](https://mp.weixin.qq.com/s/MPscJNF4uuLW4geg8yJ7fA)
1. 在 UNIX 系统下，一共有五种 I/O 模型
2. 内核态和用户态
	1. 从设备拷贝到内核空间（在内核中进行内存的分配回收，磁盘文件读写，网络数据读写等危险操作）
	2. 从内核空间拷贝到用户空间供用户程序访问
3. IO 模型的不同，就是这两个拷贝实现的差异
4. 同步阻塞 I/O
	1. 用户线程调用 read 获取网络数据（发起 IO 操作）
	2. 没有数据可读都一直阻塞着等
	3. 有数据网卡拷贝到内核，内核拷贝到用户空间
	4. 等网卡有数据，和两次拷贝都阻塞
5. 同步非阻塞 I/O
	1. 没有数据的时候用户线程直接返回，可以先干点别的
	2. 但是要一直调用 read，看有没有数据来，要是没别的活干就等于一直系统调用 read
6. I/O 多路复用
	1. 不再是每个线程都自己问，而是多了个 select
	2. 用一个 select 线程查看多个连接数据是否可读，如果有则让线程来读。
	3. 但是读的过程，即内核到用户空间，用户线程还是阻塞的
7. 信号驱动式 I/O
	1. 优化了 select 轮询查看每个连接是否有数据就绪
	2. 让内核主动通知，但是用户线程读还是阻塞
	3. 为什么市面上用的都是 I/O 多路复用而不是信号驱动?
		1. 因为 tcp socket 可以产生 7 种信号事件
		2. udp 没那么多，可以
8. 异步 I/O
	1. 用户线程调用 `aio_read`，然后内核将数据从内核拷贝到用户空间，再调用回调通知用户线程，来实现真正的非阻塞 I/O
	3. 但是 Linux 对异步 I/O 的支持不足
	4.  Tomcat 都实现了 AIO 的实现类，实际上底层实现是用 epoll 模拟实现的


##### recvfrom 函数
[从 recvfrom 看 BIO 和 NIO 的区别 | gorden5566](https://gorden5566.com/post/1092.html)
1. 用 c 代码演示了 recvfrom 函数 BIO 和 NIO 的区别

[关于那些容易搞混的网络io函数](https://liuguangxuan.top/%E5%85%B3%E4%BA%8E%E9%82%A3%E4%BA%9B%E5%AE%B9%E6%98%93%E6%90%9E%E6%B7%B7%E7%9A%84%E7%BD%91%E7%BB%9Cio%E5%87%BD%E6%95%B0/)

[Linux下，write/read，recv/send， recvfrom/sendto的区别\_sendto和write](https://blog.csdn.net/sea_snow/article/details/112260750)
1. read/wirte 是通用的文件描述符操作；
2. recv/send 通常应用于 TCP；
3. recvfrom/sendto通常应用于UDP。
### select、poll、epoll
将用户调用 read 不断轮询是否有可读交由操作系统。

[I/O多路复用技术（multiplexing）是什么？ - 知乎](https://www.zhihu.com/question/28594409)

[你管这破玩意叫 IO 多路复用？](https://mp.weixin.qq.com/s/YdIdoZ_yusVWza1PU7lWaw)
1. 阻塞 IO 
	1. accept 和 read 都阻塞
	2. 可以优化一下，就是一个线程专管 accept, 连接上后就另起一个线程去 read
	3. 主线程没有阻塞在 read 上，轮询 accept，但来一个连接就得创建一个线程，这个线程阻塞在 read 上等着能读
2. 非阻塞 IO
	1. 要想让子线程不阻塞在 read 上，改造一下 read，不能读的时候返回错误码。
	2. 于是，子线程不断的轮询调用 read，造成资源浪费
3. IO 多路复用
	1. 以上有俩浪费，一个是每个连接都一个线程，另一个是每个线程都调用 read
	2. 优化 ，每个连接 accept 后放入一个数组，然后轮询这个数组
	3. 并且，将这个过程交给内核，也就是 select、poll、epoll
4. 但是，我们还得轮询 select、poll、epoll 返回的数组，做读取操作

[9.2 I/O 多路复用：select/poll/epoll | 小林coding](https://www.xiaolincoding.com/os/8_network_system/selete_poll_epoll.html)
1. select 实现多路复用的方式是，将已连接的 Socket 都放到一个**文件描述符集合**，然后调用 select 函数将文件描述符集合**拷贝**到内核里，让内核来检查是否有网络事件产生，检查的方式很粗暴，就是通过**遍历**文件描述符集合的方式，当检查到有事件产生后，将此 Socket 标记为可读或可写， 接着再把整个文件描述符集合**拷贝**回用户态里，然后用户态还需要再通过**遍历**的方法找到可读或可写的 Socket，然后再对其处理。
2. poll 不再用 BitsMap 来存储所关注的文件描述符，取而代之用动态数组，以链表形式来组织，突破了 select 的文件描述符个数限制，当然还会受到系统文件描述符限制。
3. 但是 poll 和 select 并没有太大的本质区别，**都是使用「线性结构」存储进程关注的 Socket 集合，因此都需要遍历文件描述符集合来找到可读或可写的 Socket，时间复杂度为 O(n)，而且也需要在用户态与内核态之间拷贝文件描述符集合**，这种方式随着并发数上来，性能的损耗会呈指数级增长。
4. epoll 是解决 C10K 问题的利器，通过两个方面解决了 select/poll 的问题。
	1. epoll 在内核里使用「红黑树」来关注进程所有待检测的 Socket，红黑树是个高效的数据结构，增删改一般时间复杂度是 O(logn)，通过对这棵黑红树的管理，不需要像 select/poll 在每次操作时都传入整个 Socket 集合，减少了内核和用户空间大量的数据拷贝和内存分配。
	2. epoll 使用事件驱动的机制，内核里维护了一个「链表」来记录就绪事件，只将有事件发生的 Socket 集合传递给应用程序，不需要像 select/poll 那样轮询扫描整个集合（包含有和无事件的 Socket ），大大提高了检测的效率。

[一文搞懂I/O多路复用及其技术 - 尹瑞星 - 博客园](https://www.cnblogs.com/yrxing/p/14143644.html)
1. I/O 模型：阻塞、非阻塞、同步、异步
2. 进程模型：单进程、多进程、多线程
3. ​ 我们所说的 I/O 模型是指网络 I/O 模型, 就是服务端如何管理连接，如何请求连接的措施，是用一个进程管理一个连接（PPC），还是一个线程管理一个连接 (TPC)，亦或者一个线程管理多个连接 (Reactor)。
4. 因此 IO 多路复用中多路就是多个 TCP 连接（或多个 Channel），复用就是指复用一个或少量线程，理解起来就是多个网路 IO 复用一个或少量线程来处理这些连接。
5. 同步阻塞 IO
	1. 用户线程通过系统调用发起 io 操作，由用户空间转到内核空间，阻塞等待数据。
6. 同步非阻塞 IO
	1. 同步基础上，[socket](socket.md) 设置为 NonBlock，用户线程发起 io 操作后立即返回。
	2. 但是未读到任何数据，且为了读到数据，要不断的轮询，消耗了大量 cpu 资源
7. IO 多路复用 ^3 zimba
	1. IO 多路复用使用两个系统调用（select/poll/epoll 和 recvfrom），blocking IO 只调用了 recvfrom
	2. select/poll/epoll 核心是可以同时处理多个 connection，**而不是更快，所以连接数不高的话，性能不一定比多线程+阻塞 IO 好**。
	3. select 是内核提供的多路分离函数，使用它可以避免同步非阻塞 IO 中轮询等待问题。
	4. 过程
		1. 用户将 io 操作的 socket 添加到 select，然后阻塞等待 select 系统调用返回。
		2. 当数据到达，socket 被激活，select 返回，用户发起 read 请求。
	5. 相比同步阻塞 io，用户可以在一个线程内处理多个 socket 的 io 请求。而在同步阻塞模型中，需要用多线程才能达到这个目的。
	6. **所以 IO 多路复用设计目的其实不是为了快，而是为了解决线程/进程数量过多对服务器开销造成的压力** ^ny0odi
	7. 但每个 io 请求的过程还是阻塞的，在 select 函数上阻塞。
		1. ps: 说的不清楚
	8. reactor
		1. **用户线程注册事件处理器之后可以继续执行做其他的工作（异步）**，而 Reactor 线程负责调用内核的 select 函数检查 socket 状态。当有 socket 被激活时，则通知相应的用户线程（或执行用户线程的回调函数），执行 handel_envent 进行数据的读取、处理工作
			1. ps: 说的不清楚
	9. 由于 select 函数是阻塞的，因此多路 IO 复用模型就被称为异步阻塞 IO 模型
8. 异步非阻塞 IO
	1. ​ 在 IO 多路复用模型中，事件循环文件句柄的状态事件通知给用户线程，由用户线程自行读取数据、处理数据。而异步 IO 中，当用户线程收到通知时候，数据已经被内核读取完毕，并放在了用户线程指定的缓冲区内，内核在 IO 完成后通知用户线程直接使用就行了。因此这种模型需要操作系统更强的支持，把 read 操作从用户线程转移到了内核。
9. select、poll、epoll
	1. select，poll，epoll 都是 IO 多路复用的机制。I/O 多路复用就是通过一种机制，一个进程可以监视多个描述符（socket），一旦某个描述符就绪（一般是读就绪或者写就绪），能够通知程序进行相应的读写操作。虽说 IO 多路复用被称为异步阻塞 IO，但 select，poll，epoll 本质上都是同步 IO，因为它们都需要在读写事件就绪后自己负责进行读写，也就是说这个读写过程是阻塞的，而真正意义上的异步 IO 无需自己负责进行读写。

[一文彻底理解IO多路复用](https://mp.weixin.qq.com/s/9g8FfNKhTL1F8dR08HRuow)

##### I/O 多路复用同步非阻塞
1.  [同步](https://www.jianshu.com/p/b9a25358e3ff)是指 BIO 、NIO、IO 复用、以及信号驱动 IO,  由于第二阶段的数据复制过程, 调用线程都会被阻塞, 因此都属于同步 IO。
2.  [非阻塞](https://imageslr.com/2020/02/27/select-poll-epoll.html)是指第一个过程遍历文件描述符要不能阻塞，不然就卡在一个上了。
3. [I/O 复用模型](https://www.jianshu.com/p/76ff17bc6dea)，是同步非阻塞，这里的非阻塞是指 I/O 读写，对应的是 recvfrom 操作，因为数据报文已经准备好，无需阻塞。说它是同步，是因为，这个执行是在一个线程里面执行的。有时候，还会说它又是阻塞的，实际上是指阻塞在 select 上面，必须等到读就绪、写就绪等网络事件。有时候我们又说 I/O 复用是多路复用，这里的多路是指 N 个连接，每一个连接对应一个 channel，或者说多路就是多个 channel。复用，是指多个连接复用了一个线程或者少量线程 。
	1. 非阻塞 recvfrom 可以立即返回


[多路复用一样会阻塞用户线程，那它和同步阻塞有什么区别? - 知乎](https://www.zhihu.com/question/456131257) [2](https://mp.weixin.qq.com/s/IMGZihQrBGuAE1rffN3sXg)
1. 对于监听用户请求的线程来说都会阻塞等待，但降低了服务器的资源成本，多路复用一个线程就够了，同步阻塞一个请求一个

## IO 线程模型

1. 传统 IO
	1. 池化
	2. IO 多路复用
2. Reactor
3. Proactor
### Reactor

[填坑Reactor模型和Netty线程模型](https://mp.weixin.qq.com/s/PrquEWiFgoeMIQU5UDgxLA)
1. 传统阻塞 IO 模型的不足
	1. 每个连接都需要独立线程处理，当并发数大时，创建线程数多，占用资源
	2. 采用阻塞 IO 模型，连接建立后，若当前线程没有数据可读，线程会阻塞在读操作上，造成资源浪费
2. 针对传统阻塞 IO 模型的两个问题，可以采用如下的方案
	1. 基于池化思想，避免为每个连接创建线程，连接完成后将业务处理交给线程池处理
	2. 基于IO复用模型，多个连接共用同一个阻塞对象，不用等待所有的连接。遍历到有新数据可以处理时，操作系统会通知程序，线程跳出阻塞状态，进行业务逻辑处理

[五分钟快速理解 Reactor 模型](https://mp.weixin.qq.com/s/7CvrYsuLSFF7eCAQ3svihw)
1. 传统阻塞 IO 模型
	1. 特点
		1. 采用阻塞式 IO
		2. 每个连接都需要一个线程完成数据 read->业务处理->send 完整操作
	2. 问题
		1. 并发数大，需要创建大量的线程，系统资源占用大
		2. 连接建立后，假如当前线程没有数据可读，则线程阻塞在 read 操作上，造成资源浪费
2.  解决
	1. 基于 IO 复用模型，多个连接共用一个阻塞对象
	2. 基于线程池优化连接后的业务处理
3. IO 复用+线程池，就是 reactor 模式的基本设计思想，IO 多了复用统一监听事件，收到后事件后分发（dispatch）
4. reactor 模式的两个关键组成
	1. reactor，reactor 在一个单独的线程中运行，负责监听和分发事件
	2. handler，处理执行 IO 事件
5. 根据 reactor 数量和处理资源线程池线程数量不同，分为三种典型实现
	1. 单 Reactor 单线程
		1. 都在一个线程内完成
		2. handler 会完成 read->业务处理->send 完整操作
	2. 单 Reactor 多线程
		1. hander 不再负责业务处理，交给 worker 线程池
	3. 主从 Reactor 多线程
		1. reactor 主线程 mainReactor 对象通过 监听 select，acceptor 建立连接事件
		2. mainReactor 将连接分配给 reactor 子线程 subReactor 进行处理
6. 在 reactor 中，实际的读写操作都需要应用程序同步操作，将 IO 操作改为异步
7. proactor 模型
	1. 基于异步 IO
8. 两者相比
	1. Reactor 是在事件发生时就通知处理线程，读写都在线程中完成，也就是得同步等待
	2. proactor 是在事件发生时由内核完成异步 IO 操作，IO 返回后回调应用程序处理业务
9. proactor 模型缺点

[Reactor模型与Proactor模型 - 掘金](https://juejin.cn/post/6844903830765731848)
1. I/O 操作得经历两个过程： 
	1. 读存储设备数据到内核缓存 
	2. 从内核缓存读数据到用户空间
	3. 1操作比2操作慢的多，因为去磁盘寻址啊等操作比较慢。
	4. 我们平日里针对 I/O 场景下说的阻塞 I/O、非阻塞 I/O 指的就是1操作是否阻塞，也就是会立即返回一个状态值，还是会等待存储设备数据读取到内核缓存后在返回所需的数据。
	5.  而平日里说的同步 I/O、异步 I/O 指的是2操作是否会阻塞。
2. *上面这段话应该就是之前看过的*
	1. 针对 IO 场景应该说的就是 select、poll、epoll
	2. 平日所说的同步应该是针对业务线程说的，怎么通知业务线程。说的不确切，以阻塞是让出 cpu 为解释的话，同步的时候业务线程有可能不让出 cpu, 一直轮询
3. Reactor 模型
	1. 单 Reactor 单线程
		1. select 监听事件，事件来了 reactor 就分发，建立请求的事件就分配 acceptor, 然后 acceptor 创建 handler 处理，别的事件则分配之前的 handler 处理
		2. 没有利用多核，也没有多线程抢占资源。
		3. redis 采用，适用于业务处理极快的情况。
	2. 单 Reactor 多线程
		1. 业务不够快时，具体业务不再有 handler 处理，而是交给业务线程池。handler 只负责 read 数据，将数据给业务线程，业务线程处理完后将结果给 handler，handler send 给客户端。
		2. 缺点是多线程抢资源问题，再就是单个 Reactor 监听响应的性能瓶颈。
	3. 多 Reactor 多线程
		1. mainReactor 只接受新连接，连接进来就给 acceptor。
		2. acceptor 再分配给 subReactor，subReactor 创建 handler 处理连接。
		3. 之后就由 subReactor 来 select 监听响应这个请求。
4. Proactor 模型
	1. 我们不必等待I/O数据准备好也就是内核缓存已经读数据到用户空间。这一切都有内核来帮我们搞定，数据准备好了之后就通知Proactor，然后Proactor就调用相应的Handler进行业务处理。相对于Reactor省去了遍历事件通知队列selector 的代价

[彻底搞懂Reactor模型和Proactor模型](https://mp.weixin.qq.com/s/51YyfWk4WAJdvpcCcBgn2w)
1. Reactor 模型用于同步 I/O，而 Proactor 模型运用于异步 I/O 操作
2. 无论是 Reactor 模型还是 Proactor 模型，对于支持多连接的服务器，一般可以总结为 2 种 fd 和 3 种事件
	1. 2 种 fd
	2. 3 种事件
3. Reactor 模型
	1. 单 Reactor 单线程模型
	2. 单 Reactor 多线程模型
	3. 主从 Reactor 多线程模型
4. Proactor 模型
5. 常见架构的进程/线程模型
	1. Netty 采用的是主从线程模型
	2. Tomcat 支持四种接收请求的处理方式：BIO、NIO、APR 和 AIO
	3. Nginx 采用的是多进程（单线程）&多路 IO 复用模型。
	4. redis 单 Reactor 单进程模型


[高性能网络编程之 Reactor 网络模型（彻底搞懂） - 掘金](https://juejin.cn/post/7092436770519777311)

[Reactor 模型｜为啥 Redis 单线程模型也能效率这么高？ | 不忘痴心 砥砺前行](http://hxz.ink/2021/09/11/reactor-pattern/)

##### Reactor 模型中的 selector 和 I/O 多路复用 select、poll、epoll 的关系
[对 Reactor 始终隔了一层纱？](https://mp.weixin.qq.com/s/eKPIl5GH-PdHgsrhChLoXQ)
Reactor 是服务端在网络编程时的一个编程模式，主要由一个基于 *Selector (底层是 select/poll/epoll)* 的死循环线程，也称为 Reactor 线程。将 I/O 操作抽象成不同的事件，每个事件都配置对应的回调函数，由 Selector 监听连接上事件的发生，再进行分发调用相应的回调函数进行事件的处理。
###### 在看多路复用的理解种，I/O 多路复用是监测 I/O 事件，Reactor 当中的 selector 有连接事件，那岂不是I/O 多路复用也能监测连接事件了？
按下边评论的理解是：select、poll、epoll 文件描述符是监测是否有变化，sokcet 连接也是文件 IO。

[9.3 高性能网络模式：Reactor 和 Proactor | 小林coding](https://xiaolincoding.com/os/8_network_system/reactor.html#%E6%BC%94%E8%BF%9B) 的评论
select 的基本功能是以 I/O 多路复用思想，去监听 socket fd 是否有变化。在完整的 TCP 请求-响应过程中，select 发挥两个作用：
1. 客户端与服务端需要先“建立连接”，这一步是在服务端监听自身的 IP 和端口，这个套接字是 `监听socket` ，监听有无客户端来建立连接；监听到有客户端建立连接后，完成三次握手之后，select 会把建立好的连接返回给程序（此时返回的套接字是 `已连接的socket` ）；
2. 接着服务端需要接收数据并响应数据，此时需要再次使用 select 来监听 `已连接的socket` 上是否有客户端传来数据（这个数据是真正的 request data），如果数据到达的话，select 返回相应的 `已连接的socket` 给程序，由程序读取数据→业务处理→发送数据；
[select 函数用法](https://github.com/lhy12315/easyserverdev/blob/master/%E7%BD%91%E7%BB%9C%E9%80%9A%E4%BF%A1%E5%9F%BA%E7%A1%80%E9%87%8D%E9%9A%BE%E7%82%B9%E8%A7%A3%E6%9E%90%2004%20%EF%BC%9Aselect%20%E5%87%BD%E6%95%B0%E7%94%A8%E6%B3%95.md) 是能监听的。[1](https://www.cnblogs.com/skyfsm/p/7079458.html) [2](https://doc.embedfire.com/linux/imx6/base/zh/latest/system_programing/socket_io.html)

[socket connect的问题\_socketconnect\_flymachine的博客-CSDN博客](https://blog.csdn.net/flymachine/article/details/7530915)
[为什么有监听socket和连接socket,为什么产生两个socket\_FantanLee的博客-CSDN博客](https://blog.csdn.net/lw_jack/article/details/113248295)
##### Reactor 模型和 JavaNIO 关系
[Java NIO——Reactor模式 - 简书](https://www.jianshu.com/p/2759a2374ed4)
1. Java 的 NIO 模式的 Selector 网络通讯，其实就是一个简单的 Reactor 模型。可以说是 Reactor 模型的朴素原型。

[Java NIO Reactor网络编程模型的深度理解 - 掘金](https://juejin.cn/post/7014672477968138277)
1. Java并发编程之父Doug Lea早在多年之前就对Reactor模型进行了详尽的阐述： nio包中的Selector就是基于最简单的Reactor模型实现的
## Java

[Java之IO,BIO,NIO,AIO知多少？](https://blog.csdn.net/huangwenyi1010/article/details/75577091?ref=myread)

[对于BIO/NIO/AIO，你还只停留在烧开水的水平吗？](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247486694&idx=1&sn=5e85b28051413fc23bb890636cd18a50)
在代码中，main 方法所在的主线程拥有多路复用器并开启了一个主机端口进行通信，所有的客户端连接都会被注册到主线程所在的多路复用器，通过轮询 `while(true){}` 不断检测多路复用器上所有连接的状态，也就是 selectedKey 提供的 API。发现请求有效，就开启一个线程进行处理，无效的请求，就不需要创建线程进行处理。

[Java IO 体系、线程模型大总结](http://learn.lianglianglee.com/%E6%96%87%E7%AB%A0/Java%20IO%20%E4%BD%93%E7%B3%BB%E3%80%81%E7%BA%BF%E7%A8%8B%E6%A8%A1%E5%9E%8B%E5%A4%A7%E6%80%BB%E7%BB%93.md)

### JavaIO 访问
[Java NIO - 零拷贝实现 | Java 全栈知识体系](https://pdai.tech/md/java/io/java-io-nio-zerocopy.html)
1. 在 Java NIO 中的通道（Channel）就相当于操作系统的内核空间（kernel space）的缓冲区
2. 缓冲区（Buffer）对应的相当于操作系统的用户空间（user space）中的用户缓冲区（user buffer）
3. `MappedByteBuffer` 基于 `mmap`
4. `DirectByteBuffer` 是 MappedByteBuffer 的具体实现类，除了允许分配操作系统的直接内存以外，DirectByteBuffer 本身也具有文件内存映射的功能
5. `FileChannel` 利用 `sendfile()`

[Java 两种zero-copy零拷贝技术mmap和sendfile的介绍 - 掘金](https://juejin.cn/post/7016498891365302302)
1. Java 对 `sendfile()` 的支持就是 NIO 中的 `FileChannel.transferTo()` 或者 `transferFrom()`。
2. mmap（Memory Mapped Files）是一种零拷贝技术，学名内存映射文件，Java 中的实现就是 `MappedByteBuffer`，通过 `channel#map` 方法得到。

[第12讲 | Java有几种文件拷贝方式？哪一种最高效？ | JUST DO IT](https://leeshengis.com/archives/8393)

[理解 MappedByteBuffer 及其实现类 DirectByteBuffer-CSDN博客](https://blog.csdn.net/yzh_1346983557/article/details/119760911)
##### 基于 Java 的 tomcat、netty、rocketmq 和 kafka

[零拷贝技术及在Java中应用 - 掘金](https://juejin.cn/post/7110568992049692703)

|系统调用|CPU 拷贝|DMA 拷贝|上下文切换|
|--- |--- |--- |--- |--- |
|传统 I/O|read+write|2|2|4|
|mmap|mmap+write|1|2|4|
|sendfile|sendfile|1|2|2|
|sendfile+gather|sendfile|0|2|2|
|splice|splice|0|2|0|

[Tomcat中的ArpEndpoint(再论零拷贝与SendFile） - Moonshoterr - 博客园](https://www.cnblogs.com/moonyaoo/p/13047445.html)

[16 IO 加速：与众不同的 Netty 零拷贝技术](https://learn.lianglianglee.com/%E4%B8%93%E6%A0%8F/Netty%20%E6%A0%B8%E5%BF%83%E5%8E%9F%E7%90%86%E5%89%96%E6%9E%90%E4%B8%8E%20RPC%20%E5%AE%9E%E8%B7%B5-%E5%AE%8C/16%20%20IO%20%E5%8A%A0%E9%80%9F%EF%BC%9A%E4%B8%8E%E4%BC%97%E4%B8%8D%E5%90%8C%E7%9A%84%20Netty%20%E9%9B%B6%E6%8B%B7%E8%B4%9D%E6%8A%80%E6%9C%AF.md)

/Volumes/resources/netty/Netty 核心原理剖析与 RPC 实践/16 | IO 加速：与众不同的 Netty 零拷贝技术. md
/Volumes/resources/圣思园/netty/38_NIO 堆外内存与零拷贝深入讲解. mp 4
/Volumes/resources/圣思园/netty/49_零拷贝深入剖析及用户空间与内核空间切换方式. mp 4
### JavaNIO
[Java NIO：Buffer、Channel 和 Selector\_Javadoop](https://javadoop.com/post/java-nio)
[Java 非阻塞 IO 和异步 IO\_Javadoop](https://javadoop.com/post/nio-and-aio)

[Java NIO - IO多路复用详解 | Java 全栈知识体系](https://pdai.tech/md/java/io/java-io-nio-select-epoll.html)
1. 传统 IO 模型
	1. 主要是一个 Server 对接 N 个客户端，在客户端连接之后，为每个客户端都分配一个执行线程。
2. Reactor 事件驱动模型
	1. 在 Reactor 模型中，主要有四个角色：client，Reactor，Acceptor 和 Handler。这里 Acceptor 会不断地接收 client 的连接，然后将接收到的连接交由 Reactor 进行分发，最后有具体的 Handler 进行处理。
3. Reactor 模型----业务处理与 IO 分离
	1. 这里在单线程 Reactor 模型的基础上提出了使用线程池的方式处理业务操作的模型。
4. Reactor 模型----并发读写
	1. 使用线程池进行网络读写，而仅仅只使用一个线程专门接收客户端连接

[关于Java NIO的『一切』 | 夕阳下的奔跑](http://xintq.net/2017/06/12/everything-about-java-nio/)
##### Java NIO 的 selector 和 I/O 多路复用 select、poll、epoll 的关系
[BIO/NIO/多路复用/Selector/select/poll/epoll](https://blog.csdn.net/weixin_44273302/article/details/115269745)
selector 多路复用器算是对 linux 下的 select/poll/epoll 进行封装，selector 可以有多种实现，linux 系统下默认使用 epoll 的实现方式。

[彻底搞懂NIO效率高的原理](https://mp.weixin.qq.com/s?__biz=MzUyNzgyNzAwNg==&mid=2247483929&idx=1&sn=ed536aee5ac7f898fc5e640785769fd4&scene=21#wechat_redirect)
1. Java NIO 主要由以下三个核心部分组成：
	1. - Channel
	2. - Buffer
	3. - Selector
2. Java NIO 根据操作系统不同， 针对 NIO 中的 Selector 有不同的实现
	1. JDK 在 Linux 已经默认使用 epoll 方式，但是 JDK 的 epoll 采用的是水平触发，所以 Netty 自 4.0.16 起, Netty 为 Linux 通过 JNI 的方式提供了 native socket transport。Netty 重新实现了 epoll 机制
### JavaAIO

[Java 非阻塞 IO 和异步 IO\_Javadoop](https://javadoop.com/post/nio-and-aio)
1. `AsynchronousFileChannel`
2. `AsynchronousServerSocketChannel` 对应的是非阻塞 IO 的 `ServerSocketChannel`

[到底什么是Java AIO？为什么Netty会移除AIO？一文搞懂AIO的本质](http://www.52im.net/thread-4283-1-1.html)
1. Java IO 是哪一种模式，需要有个参考系，才能定义它是同步还是异步，要针对 IO 读写操作这件事来理解。
	1. 发起 IO 读写的线程（调用 read 和 write 的线程），和实际操作 IO 读写的线程，如果是同一个线程，就称之为同步，否则是异步。
	2. 🤔嗯..，感觉不如从 linux IO 模型定义来的准确，毕竟假如发起 IO 操作的线程空闲的话，是不是也有可能被选中执行 IO 读写。
2. 监听回调的本质是什么？
3. Java AIO 的本质是什么？


## netty

[[netty]]
## tomcat

[Tomcat](Tomcat.md)
## reference

[怎样理解阻塞非阻塞与同步异步的区别？ - 知乎](https://www.zhihu.com/question/19732473)
同步与异步是说被调用者通知调用者的方式
阻塞与非阻塞是说调用者是否一直等待结果不干别的，程序在等待调用结果（消息，返回值）时的状态
[什么是IO中的阻塞、非阻塞、同步、异步](https://mp.weixin.qq.com/s/LgC43CL8TqkvbIwkJmVHrQ)


[Java中BIO,NIO,AIO的理解](https://blog.csdn.net/itismelzp/article/details/50886009)
[一篇讲透全网最高频的Java NIO面试考点汇总](https://mp.weixin.qq.com/s?__biz=MzIzMzgxOTQ5NA==&mid=2247488079&idx=1&sn=c7bc82ddd41e70278765dcc1842842bf&chksm=e8fe8e46df89075041bcafa78d3410743eeaa585c27f4f826bf4104c0afbd17f487827fa787c&mpshare=1&scene=23&srcid=&sharer_sharetime=1565408592570&sharer_shareid=07754c1336c3524bfffedc4dc59111b6#rd)


[如何给女朋友解释什么是BIO、NIO和AIO？](https://mp.weixin.qq.com/s?__biz=Mzg3MjA4MTExMw==&mid=2247485960&idx=1&sn=83d418c498c2d6df102bd227c9e5c7ff&chksm=cef5f9bef98270a8e5cbd23280fb211362c7ff17dcf8894df71ba42692348c8f99e101a3c35d&mpshare=1&scene=23&srcid=#rd)


[聊聊Netty那些事儿之从内核角度看IO模型](https://mp.weixin.qq.com/s?__biz=Mzg2MzU3Mjc3Ng==&mid=2247483737&idx=1&sn=7ef3afbb54289c6e839eed724bb8a9d6)  #值得细看 


[NIO、EPOLL和协程](https://plantegg.github.io/2019/07/31/NIO%E5%92%8CEpoll/)
没显示的[[协程]]图。在[协程](协程.md#python%20的协程)下参考的文章里也有
![800|800](https://static001.geekbang.org/resource/image/e4/57/e47ec54ff370cbda4528e285e3378857.jpg)

1. [如何正确理解线程机制中常见的 IO 模型](如何正确理解线程机制中常见的%20IO%20模型.md)
2. [详解磁盘IO、网络IO、零拷贝IO、BIO、NIO、AIO、IO多路复用(select、poll、epoll) | Layne's Blog](https://wxler.github.io/2021/02/19/134758/)
3.  [虚拟内存 & I/O & 零拷贝总结 - 知乎](https://zhuanlan.zhihu.com/p/565012118)