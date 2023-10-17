# threadPool

##### 问题
1. 怎么监测线程



## 线程池参数
==核心线程数 corePoolSize==
1. [cpu密集型](https://bbs.huaweicloud.com/blogs/344722)：cpu核心数1-2倍
2. [io密集型](https://juejin.cn/post/7027610930431131685)：cpu核心数*（1+平均等待时间/平均工作时间）
==最大线程数 maxPoolSize==
==线程保活时间 keepAliveTime==
==时间单位==
==工作队列 BlockingQueue [queue](concurrency/queue.md#queue) ==
1. ArrayBlockingQueue（有界队列）是一个用数组实现的有界阻塞队列，按FIFO排序量。
2. [LinkedBlockingQueue](https://mp.weixin.qq.com/s/BTVDPHVWjBiq9mj3mvT_Vw)（可设置容量队列）基于链表结构的阻塞队列，按 FIFO 排序任务，容量可以选择进行设置，不设置的话，**将是一个无边界的阻塞队列**，最大长度为 Integer. MAX_VALUE，吞吐量通常要高于 ArrayBlockingQuene；<font color= #F36208 >newFixedThreadPool</font>线程池使用了这个队列
3. DelayQueue（延迟队列）是一个任务定时周期的延迟执行的队列。根据指定的执行时间从小到大排序，否则根据插入到队列的先后排序。<font color=#F36208>newScheduledThreadPool</font>线程池使用了这个队列。
4. PriorityBlockingQueue（优先级队列）是具有优先级的无界阻塞队列；
5. SynchronousQueue（同步队列）直接交换，一个不存储元素的阻塞队列，每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于LinkedBlockingQuene，<font color=#F36208>newCachedThreadPool</font>线程池使用了这个队列。

==线程工厂类 ThreadFactory==
新的线程默认使用 Executors. defaultThreadFactory () 创建，创建出来的线程都在同一个线程组，拥有同样的 NORM_PRIORITY 优先级且不是守护线程。如果自己指定 ThreadFactory, 就可以改变线程名、线程组、优先级、是否守护线程等

==拒绝策略 RejectdExecutionHandler==
什么时候会拒绝？
1. executer关闭后
2. 最大线程数和有限队列饱和
策略：
1. jdk四种
   1. AbortPolicy(丢弃任务并抛出RejectedExecutionException异常)
   2. DiscardPolicy(直接丢弃任务，但不抛出异常)
   3. DiscardOldestPolicy（丢弃队列里最老的任务，将当前这个任务继续提交给线程池，重复此过程）
   4. CallerRunsPolicy（谁提交的任务谁执行)
2. 第三方
   1. dubbo
   2. netty
   3. activemq
##### reference
1. [JAVA线程池学习以及队列拒绝策略](https://mp.weixin.qq.com/s?__biz=MzAxOTQxOTc5NQ==&mid=2650499812&idx=1&sn=db6624d264099226c9610f9f04cf3c74)
2. [java线程池ThreadPoolExecutor八种拒绝策略浅析](http://www.kailing.pub/article/index/arcid/255.html)
3. [一步一步分析 RejectedExecutionException 异常](https://my.oschina.net/dabird/blog/1524273)

## 线程池原理
###### [jdk](https://mp.weixin.qq.com/s?__biz=MzAxOTQxOTc5NQ==&mid=2650499788&idx=1&sn=be62aac8d3ac431e9e93fe633ca10f9b) 提供的线程池
1.  FixedThreadPool，问题：固定线程池核心数等于最大数，队列为LinkedBlockingQueue，队列未设置大小无界，所以会OOM
2.  SingleThreadExecutor，是一个线程数为1的FixedThreadPool，队列未设置大小无界，所以会OOM
3.  CachedThreadPool：无界线程池，可自动回收多于线程。直接交换对列，进来任务直接交给线程执行。最大线程数为整形最大值，在超出默认时间后，会回收，默认60秒。因为  线程数  没有限制也会导致oom。
4.  ScheduledThreadPool：定期、周期执行任务，未设置大小无界，所以会OOM
5.  1.8 workStealingPool适用有子任务场景

##### 为什么用线程池？
软件中的“池”，可以理解为计划经济，控制资源总量。
1. 反复创建线程开销大
2. 过多线程占用资源大
reference
1. [使用线程池 ThreadPoolExecutor](https://segmentfault.com/a/1190000007925310)

##### 为什么要手动创建线程池？
根据不同业务场景，硬件资源调研后确认名字，线程数等。

##### 为什么要[手动关闭](https://blog.csdn.net/f641385712/article/details/82021919)线程池？
局部线程池手动关闭，防止内存泄漏。像数据库连接池持续使用的就不用。

##### 如何停止线程池
1. Shutdown 开始结束, 调用后拒绝新任务，等现有任务结束后结束池。
2. isShutdown 判断是否进入停止状态，即是否调用了 Shutdown 方法。
3. isTerminated 是否真的结束了。
4. awaitTermination 检测线程在传入时间内是否结束
5. ShutdownNow，立即暴力结束，给线程发送中断，返回队列中任务
[如何正确地停止一个线程？ - 王晓符 - 博客园](https://www.cnblogs.com/greta/p/5624839.html)
##### 优雅销毁
[优雅地关闭线程池\_线程池setwait关闭\_Sunday\_ding的博客-CSDN博客](https://blog.csdn.net/a1058926697/article/details/103652001)
1. setWaitForTasksToCompleteOnShutdown(true): 该方法用来设置 线程池关闭 的时候 等待 所有任务都完成后，再继续 销毁 其他的 Bean，这样这些 异步任务 的 销毁 就会先于 数据库连接池对象 的销毁
2. setAwaitTerminationSeconds(60): 该方法用来设置线程池中 任务的等待时间，如果超过这个时间还没有销毁就 强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
##### 添加线程规则
1. 线程池初始化后，默认没有任何线程。
2. 来任务后，创建线程。如果线程数小于corePoolSize，即使其他工作线程处于空闲状态，也会创建一个新线程执行新任务。
3. 如果线程数等于或大于 corePoolSize 但小于 maxPoolSize，则将任务放入队列。
4. 如果队列已满，并且线程数小于maxPoolSize，则创建一个新线程执行任务。
5. 如果队列已满，并且线程数大于或等于maxPoolSize，则拒绝该任务。

##### 回收线程规则
在线程池处理完任务后，会根据设置的 keepAliveTime 来回收核心线程数 corePoolSize 之外的线程，同时若设置了 allowCoreThreadTimeOut 值为 true，也会对核心线程进行回收。
1. [线程池中多余的线程是如何回收的？](https://mp.weixin.qq.com/s/kj4bL5UAUWuxKqEoAfDDiw)
2. [ThreadPoolExecutor是怎么回收线程的](https://juejin.cn/post/6922069411981426702)
3. [简单分析ThreadPoolExecutor回收工作线程的原理](https://www.cnblogs.com/kingsleylam/p/11241625.html)

##### 线程如何实现复用
[Java 线程池中的线程复用是如何实现的？](https://cloud.tencent.com/developer/article/1652672)
[简单分析ThreadPoolExecutor回收工作线程的原理 - kingsleylam - 博客园](https://www.cnblogs.com/kingsleylam/%20p/11241625.html)

##### 钩子方法

## 如何配置线程池
线程池使用面临的核心的问题在于：线程池的参数并不好配置。即便经过谨慎评估仍不能保证一次计算出来合适的参数，所以换个方向，让参数调整变得简单，可动态配置。根据不同的业务场景配置不同的线程池，让线程池可监测，动态化参数配置使得我们配置每个线程池更合理的参数。



[如何合理地估算线程池大小？](https://www.cnblogs.com/cjsblog/p/9068886.html) [1](https://mp.weixin.qq.com/s/sLf3x7B7eX6QAIMtHtZzfg)
1. 将问题转为TPS问题，像怎么设计线程池，可以每秒处理20个事务？
2. 第一种就是简单的计算，比如一个线程处理一个事务4秒，那一秒20个事务就得80个线程（但是感觉不能4个线程处理一个事务）
3. 第二种，N为cpu总数
	1. CPU密集型，线程池大小设置为N+1
	2. 计算密集型，2N+1
4. 还有个最佳线程数目 = （（线程等待时间+线程CPU时间）/线程CPU时间 ）* CPU数目
5. 又有个“Dark Magic”估算方法，提供了Java代码，比如期望CPU利用率是多少，任务队列大小。

像上面这种事前估算，不好处理的点在于，即便我们能估算清楚这个业务下的线程池合理参数，但是不能保证这台服务器只有这一个线程池。当存在多个线程池时，都会抢cpu，单个估算的再准确，也会互相影响。所以，需要换个思路。可以在系统跑起来后，动态的调整。当然还是不能完全的避免不同线程池的竞争关系，但是也可以设计策略优先级。

### 监控线程池
[高可用可伸缩微服务架构](高可用可伸缩微服务架构.md#第5章%20微服务稳定性保证的常用手段)第5章提到了使用一个调度线程池对业务线程池进行监控。
之前单体脚手架中抄了个VisiableThreadPoolTaskExecutor。


#### 基于 [spring boot actuator](Metric监控.md#spring%20boot%20admin%20spring%20boot%20actuator) 页面监控

[基于Actuator的可修改配置的线程池监控 - 掘金](https://juejin.cn/post/6902051279154249735)
[基于SpringBoot集成线程池，实现线程的池的动态监控（超级详细，建议收藏） - 跟着Mic学架构 - 博客园](https://www.cnblogs.com/mic112/p/15424574.html)

### 动态化配置线程池
[Java线程池实现原理及其在美团业务中的实践](https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html)
==动态化线程池==
核心设计包括三个方面
1. 简化线程池配置，构造参数有8个，但最核心的三个应该是corePoolSize、maximumPoolSize，workQueue。实际应用的并发场景主要两种
	1. 并行执行子任务，这就应该使用同步队列，没有任务缓存下来，直接执行
	2. 并行执行大批次任务，这就使用有界队列缓冲大批任务。
2. 能够监控线程池状态，让开发知道该怎么调整
3. 核心参数动态修改，外部化配置
JDK允许线程池使用方通过ThreadPoolExecutor的实例来动态设置线程池的核心策略，并且内部会处理好当前状态做到平滑修改。
也提供了读取到当前线程池的运行状态以及参数的方法。

 [如何设置线程池参数？美团给出了一个让面试官虎躯一震的回答。](https://segmentfault.com/a/1190000022353812) 
 1. 动态更新的工作原理是什么？
 2. 动态设置的注意点有哪些？
 3. 如何动态指定队列长度？
	 1. JDK并没有提供，且LinkedBlockingQueue的 capacity 是被 final 修饰。所以要自己实现个队列可以修改capacity，但是要保证线程安全。
 4. 

[基于Spring Boot的线程池监控方案 - 掘金](https://juejin.cn/post/7074579161201229838)

[Hippo4j动态线程池监控接入 - chen 的个人博客](https://www.dczzs.com/articles/2023/04/12/1681288036887.html)
[GitHub - opengoofy/hippo4j:.](https://github.com/opengoofy/hippo4j)
## 线程池异常处理
==缘由：==
子线程抛出异常，父线程 catch 不到。使用线程池时， 任务执行代码为 `ThreadPoolExecutor#runWorker`，其中的 task 执行被 try catch 起来，异常传递到 afterExecute (task, ex)，但其并没有实现。所以我们无法感知，这样做保证了提交的任务出错不影响其他任务及当前线程。
==如何避免：==
1. 提交任务时捕获，不抛给线程池，任务子线程自己处理。
2. 抛给线程池，统一处理
==总结==
直接 catch
	1. 所有任务都要 catch，不存在受检异常也要 catch。在子线程中处理掉异常。
线程池处理
1. submit
       1. FutureTask的run方法中，调用了callable对象的call方法,如果代码(Runnable)抛出异常，会被捕获并且把这个异常保存下来。在调用get方法时，会将保存的异常重新抛出。
2. execute 
      1. 重写ThreadPoolExecutor#afterExecute(Runnable r, Throwable t)方法。submit的task.run把异常吃掉了，不会有异常进入此方法。
      2. 实现Thread.UncaughtExceptionHandler接口，重写uncaughtException方法，在使用线程时将此handler置入。针对于execute()方式。
      3. 继承ThreadGroup,重写重写uncaughtException方法，同2.针对于execute()方式。
      4. 针对于submit（），异常会被放入Future,catch起future.get()处理。
##### reference
1. [Java线程池异常处理的正确姿势](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247488240&idx=2&sn=93071cb6607fb3423ba5667856e24d0b&chksm=9bf4a245ac832b5379dbb3dddaa70da33c1177674fabadf25d4ab96691479b611f72d7f6466c&mpshare=1&scene=23&srcid=&sharer_sharetime=1566098202685&sharer_shareid=07754c1336c3524bfffedc4dc59111b6#rd)
2. [Java线程池异常处理方案](https://www.jianshu.com/p/30e488f4e021?u_atoken=50bfa341-1393-4db1-a7a0-603614e89dc6&u_asession=0174HUWCLqjCy4PCfwqS-I72V3TuywMd5LgAZBWSxlsZbkQ3Dx-wR4ttcUQbb3CmrRX0KNBwm7Lovlpxjd_P_q4JsKWYrT3W_NKPr8w6oU7K-f_cCnieCFEZ5JNG6i3UumzdjoMV1y19BFQvaXcOyBfmBkFo3NEHBv0PZUm6pbxQU&u_asig=05TrrStJKDQyRL6Pe0d0BBueNneiSmsnS7rxUvTsCEKOpJHc08uXe4voEYLTwcUF6tfc7WcqheaVo7AdGbW7KZ--lPm0jm8V1C55FitYnswSLEj8dHXACGNZSV-xzbOpUPBeBcPdWKDR4n2HfURFDKQhGqj4d3uBeC_WPItX4G0-b9JS7q8ZD7Xtz2Ly-b0kmuyAKRFSVJkkdwVUnyHAIJzcHtJit-VFdCnbY5KweLLertSQwo05UTBpNRodm4X3N-qBR97QLsOYcZJeUxi-_JXu3h9VXwMyh6PgyDIVSG1W_NFH0JW6h5FE4GaVS3IyNGfGzAYnzq8c-CZpY9VVcZzt8Awox9DcLS4ydNV-HrHa0dNKujjw4YNlUpc8u2tTMWmWspDxyAEEo4kbsryBKb9Q&u_aref=Pqk4TWAhg%2Bpmp02PiiVPn5CoyAc%3D)
3. [Java捕获线程异常的几种方式](https://blog.csdn.net/pange1991/article/details/82115437)
1.  [线程池异常处理详解，一文搞懂！](https://www.cnblogs.com/ncy1/articles/11629933.html)
2. [线程池中线程抛了异常，该如何处理？](https://mp.weixin.qq.com/s/N-u4eDjAYOromeNnPGAxyQ)

## 自定义线程池
1. [亲缘性线程池，这是什么鬼？](https://zhailuxu.github.io/java/2020/08/01/KeyAffinityExecutor.html)



## reference
1. [图解 | 你管这破玩意叫线程池？](https://mp.weixin.qq.com/s/70u3d3c6VavgteGttIBoWA)
2. [Java并发编程：线程池的使用](https://www.cnblogs.com/dolphin0520/p/3932921.html)
3. [Java线程池解析](https://juejin.cn/post/6844903889678893063)
4. [线程池没你想的那么简单](https://juejin.cn/post/6844903847018659854)
5. [关于线程池，这些“根儿上”的东西你不能不知道！](https://mp.weixin.qq.com/s?__biz=MzU0OTk3ODQ3Ng==&mid=2247485579&idx=1&sn=2a85c8c3edcad6a7d92d92af866ba819)
6. [HikariCP-SpringBoot默认的数据库连接池](https://mp.weixin.qq.com/s/GPIGWceZJ6C-utJC3Z_kMA)



