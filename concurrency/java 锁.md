# Java 锁
##### 问题  

1. [Thread.sleep()、Object.wait()、Condition.await()、LockSupport.park()的区别?](https://pdai.tech/md/java/thread/java-thread-x-lock-LockSupport.html#%E6%9B%B4%E6%B7%B1%E5%85%A5%E7%9A%84%E7%90%86%E8%A7%A3)  
2. [Java并发面试题及答案](https://mp.weixin.qq.com/s?__biz=MzIzMzgxOTQ5NA==&mid=2247488001&idx=1&sn=e9d80b1128681de5bb603f0068a2d4db)  
3.   
## 理论基础  
  
#### 并发  
  
同时拥有两个或者多个线程，如果程序在单核处理器上运行，多个线程交替得换入或者换出内存，这些线程是同时“存在”的，每个线程都处于执行过程中的某个状态，如果运行在多核处理器上，此时，程序中的每个线程都将分配到一个处理器核上，因此可以同时运行.  
  
#### 高并发  
  
高并发（High Concurrency）是互联网分布式系统架构设计中必须考虑的因素之一，他通常是指，通过设计保证系统能够同时并行处理很多请求。  
  
#### 对比  
  
>谈并发时：多个线程操作相同的资源，保证线程安全，合理利用资源  
>谈高并发时：服务能同时处理很多请求（如12306的抢票，天猫双十一的秒杀活动，这会导致系统在短时间内执行大量的操作，  
   如对资源的请求，数据库的访问），提高程序性能（如果高并发处理不好，不光会导致用户体验不好，还可能会使服务器宕机，出现OOM等）  
  
##### refrence   
1. [并发与并行的区别](https://www.cnblogs.com/bughui/p/7422214.html)  
1. [线程、进程、多线程、多进程和多任务有啥关系?](https://mp.weixin.qq.com/s/H_-faCIW4OqoHXqBlnzWPw)  
1. [进程、线程、进程池、进程三态、🍨同步、异步、🍦并发、并行、串行](https://www.cnblogs.com/songhaixing/p/13799827.html)  

  
并发三要素  
  
1. 可见性，[cpu缓存](JMM.md#^lftr0e)引起  
2. 原子性，分时复用引起  
3. 有序性，重排序引起  
4. [并发编程：原子性、可见性和竞态条件与复合操作](https://mp.weixin.qq.com/s?__biz=MzAxOTQxOTc5NQ==&mid=2650499817&idx=1&sn=6ae767f81c3682580112a4b745236f46)  
  
## 关键字  
  
### synchronized  
  
 作用主要有三个：  
  
1. 确保线程互斥的访问同步代码  
2. 保证共享变量的修改能够及时可见  
3. 有效解决重排序问题  
  
从语法上讲，Synchronized 总共有三种用法：  
  
1. 修饰普通方法  
2. 修饰静态方法  
3. 修饰代码块  
  
##### 修饰普通方法  
  
执行方法前要获取**同一个对象**上的锁，锁是当前实例对象。调用方法前会检查常量池中ACC_SYNCHRONIZED 是否被设置。  
  
##### 修饰静态方法  
  
本质上是对类的同步，调用的时候需要获取**同一个类上** Monitor（每个类只对应一个 Class 对象），锁是当前类的class对象  
  
##### 修饰代码块  
  
锁是**括号里面的对象**，利用monitorenter 和monitorexit指令实现  
  
##### refrence  
  
1. [Java并发编程：Synchronized及其实现原理](https://www.cnblogs.com/paddix/p/5367116.html)  
  
2. [御姐带你深入理解synchronized的实现原理](https://mp.weixin.qq.com/s?__biz=MzU4Njc1MTU2Mw==&mid=2247483769&idx=1&sn=d28a030b8499fc5959d101d7d24af12f)  
  
3. [浅谈多线程之锁的机制](https://blog.csdn.net/z1035075390/article/details/54929909)  
  
  
## JUC原子类  
  
### CAS  
  
CPU原子指令，基于硬件平台汇编指令，JVM封装调用。  
  
##### 问题  
  
1. ABA问题：加版本号解决，AtomicStampedReference解决  
2. 循环时间长开销大  
  
### Unsafe  
  
内部使用自旋的方式进行CAS（调用native方法）更新(while循环进行CAS更新，如果更新失败，则循环再次重试)。只能jvm内部使用unsafe类。  
  
### 原子类  
  
AtomicInteger 等13个原子类（基本类型、数组、引用类型、字段）  
  
1. volatile保证线程的可见性  
2. CAS 保证数据更新的原子性  
  
##### refrence  
  
1. [性能优化之使用LongAdder替换AtomicLong](https://juejin.cn/post/6921595303460241415)  
2.   
## JUC锁  
  
### LockSupport  
  
创建锁和其他同步类的基本线程阻塞原语。LockSupport的核心函数都是基于Unsafe类中定义的park和unpark函数。  
  
1. [AQS阻塞唤醒工具LockSupport](http://geyifan.cn/2017/03/19/locksupport-utilities/)  
  
### AQS  
  
  
  
### 分类  
  
1. 线程要不要锁住同步资源  
   1. 锁住：悲观锁  
   2. 不锁住：乐观锁  
2. 按不按请求锁时间早晚排队  
   1. 排队：公平锁  
   2. 先尝试插队再排队： 非公平锁，避免唤醒带来的空档期。猜测是获得cpu时间片的线程来尝试取锁。  
3. 多个线程能不能共享一把锁  
   1. 能： 共享锁，是一种乐观锁，ReadWriteLock  
   2. 不能：独占锁、排他锁、互斥锁，是一种悲观锁，ReentrantLock  
4. 再次获取自己已经获得的锁是否阻塞  
   1. 不阻塞： 可重入锁、递归锁，ReentrantLock和synchronized都是可重入锁  
   2. 阻塞： 不可重入  
5. 获取锁失败后，要不要阻塞挂起  
   1. 不阻塞：自旋锁、适应性自旋锁  
   2. 阻塞：非自旋锁  
6. 等锁是否可中断  
   1. 可以：可中断锁 lock  
   2. 不可以：不可中断锁 synchronized  
7. 针对synchronized的状态  
   1. 无锁-》偏向锁-》轻量级锁-》重锁  
    8.  
  
##### 悲观锁与乐观锁  
  
1. 乐观锁与悲观锁是一种广义上的概念，体现了看待线程同步的不同角度。在Java和数据库中都有此概念对应的实际应用。  
  
2. Java中悲观锁实现：synchronized关键字和Lock的实现类都是悲观锁。  
  
3. Java中乐观锁实现：在Java中是通过使用无锁编程来实现，Java原子类中的递增操作就通过CAS自旋实现的。  
  
4. **悲观锁适合写操作多的场景**，先加锁可以保证写操作时数据正确。  
  
5. **乐观锁适合读操作多的场景**，不加锁的特点能够使其读操作的性能大幅提升。  
  
6. git 乐观锁形式  
  
7.  
  
##### refrence  
  
1. [漫话：如何给女朋友解释什么是共享锁和排他锁](https://mp.weixin.qq.com/s/DRcwDgUMGwYC8Hr-mMnM_A)  
2. [漫话：如何给女朋友介绍什么是死锁](https://mp.weixin.qq.com/s/26-D_gFF2RCieJJ5qAqipA)  
3. [Java 中各种锁的介绍](https://mp.weixin.qq.com/s?__biz=MzAwMDczMjMwOQ==&mid=2247483971&idx=2&sn=efeee4f2aff2661fbeb0c0772f8cefd9)  
4. [ Java并发 - Java中所有的锁](https://pdai.tech/md/java/thread/java-thread-x-lock-all.html)  
5. [到底什么是重入锁，拜托，一次搞清楚！](https://mp.weixin.qq.com/s?__biz=MzI3ODcxMzQzMw==&mid=2247489881&idx=1&sn=fd30734494272ec71ea9d77e2a2d2b00)  
6. [synchronized 的锁膨胀过程](https://mp.weixin.qq.com/s?__biz=MzI5NTYwNDQxNA==&mid=2247485139&idx=1&sn=3e2a0f56907b8bf90597ad6846cfa846)  
7. [Java 对象头信息分析和三种锁的性能对比](https://mp.weixin.qq.com/s/gaqEjwa13T-IVeGrykqCqA)  
8. [共享锁、排他锁、互斥锁、悲观锁、乐观锁、行锁、表锁、页面锁、不可重复读、丢失修改、读脏数据](https://blog.csdn.net/weixin_36634753/article/details/90815755)  
  
### Lock  
  
#### ReentrantLock  
  
默认非公平锁  
  
1. try前获取锁，finally里释放锁！  
2. Lock不会像synchronized一样，异常的时候自动释放锁，所以最佳实践是，finally中释放锁，以便保证发生异常的时候锁一定被释放  
3. 用tryLock来避免死锁，lock()方法不能被中断，一旦陷入死锁，lock()就会永久等待.trylock()用来尝试获得锁，如果当期锁没有被其他线程占用，则返回true，否则FALSE，可以用这个结果来决定程序后续行为。trylock(time)超时就放弃  
4. lock.lockInterruptibly()，相当于tryLock(time)把超时时间设置为无限，在等锁过程中可以被中断  
5. [Java多线程之ReentrantLock与Condition](https://www.cnblogs.com/xiaoxi/p/7651360.html)  
  
#### ReentrantReadWriteLock  
  
1. 和ReentrantLock没关系  
2. 不管是ReadLock还是WriteLock都支持Interrupt，语义与ReentrantLock一致。  
3. WriteLock支持Condition并且与ReentrantLock语义一致，而ReadLock则不能使用Condition，否则抛出UnsupportedOperationException异常。   
4.   
> 场景：线程2、4在读，3想写，拿不到锁进入等待队列，5不在队列，现在要读  
>  
> 要么：5插队 和2、4一起读，效率高但容易造成饥饿  
>  
> 要么：5排到3后边， 避免饥饿   ReentrantReadWriteLock的选择  
  
公平锁：AQS有了等待节点后读、写都要排队  
  
非公平锁：  
  
1. 写锁可以随时插队，但获取不到锁，会进入队列排队  
2. 读锁只在等待队列头节点不是写锁时可插队  
  
  
1. [轻松掌握java读写锁(ReentrantReadWriteLock)的实现原理](https://blog.csdn.net/yanyan19880509/article/details/52435135)  
2. [Java并发（7）- 你真的了解ReentrantReadWriteLock吗？](https://www.cnblogs.com/konck/p/9473615.html)  
  
锁降级  
  
持有写锁可以降级为读锁  
  
### Synchronized与Lock  
  
Synchronized缺点：  
  
1. 试图获取锁不能设定超时、不能中断一个正在试图获得锁的线程，只有执行完毕或者异常结束才能释放锁, 无法主动释放，死锁。  
2. 每个锁仅有一个单一的条件(某个对象)，相对而言，读写锁更加灵活  
3. 无法知道是否成功获得锁  
  
##### refrence  
  
1. [Java提供了synchronized，为什么还要提供Lock呢？](https://mp.weixin.qq.com/s?__biz=MzAwNjkxNzgxNg==&mid=2247488848&idx=2&sn=9fffb71d0936b7ea538b062e295ae583)  
2. [Synchronized与Lock](https://pdai.tech/md/java/thread/java-thread-x-key-synchronized.html#synchronized%E4%B8%8Elock)  
  
## JUC集合  
  
1. [Java多线程总结之聊一聊Queue](https://www.iteye.com/blog/hellosure-1126541)  
2. [14个Java并发容器](https://blog.csdn.net/Design407/article/details/100084673)  
3.  
  
## 线程  
  
1. 操作系统支持什么样的线程模型取决于虚拟机映射，对Java程序透明  
2. ```java.lang.Thread#setPriority(int newPriority)```并不一定能与系统中的优先级匹配，所以可以理解为```建议优先级```  
  
#### 状态  
  
##### new  
  
刚创建的线程，尚未调用start()时  
  
##### runnable  
  
在调用start()后进入，可以认为有俩子状态  
  
1. ready： 在被线程调度器调度后进入running  
2. running: 即run()里的代码正在被cpu执行，当Thread#yield()后进入ready  
  
##### wating  
  
无限期等待，不会被分配cpu执行时间，被显式唤醒后进入runnable。以下方法让线程进入wating状态  
  
1. 不带超时的Object#wait()，等另一个线程调用对象上Object#notify()或者Object#notifyAll()  
2. 不带超时的LockSupport.park()，等另一个线程调用LockSupport.unpart(thread)  
3. 不带超时的Thread#join() ，阻塞当期线程直到join的线程终结  
  
##### time wating  
  
有限期等待，不会被分配cpu执行时间，但不需要显示唤醒，超时后会被vm唤醒。  
  
##### blocked  
  
阻塞等待获取监视器，不会被分配cpu执行时间，有两种情况进入阻塞  
  
1. 等锁  
2. 进入同步代码后，  
  
##### terminated  
  
终结，一个线程实例只能调用一次run()方法，run方法执行后，线程生命周期结束。  
  
#### 上下文切换  
  
即线程在runnable和非runnable之间切换时，要保存与恢复cpu寄存器程序计数器等信息。  
  
额外的开销  
  
1. 保存与恢复上下文  
2. 对线程进行调度的cpu时间开销  
3. cpu缓存失效  
  
##### refrence  
  
1. [Java线程生命周期与状态切换](https://mp.weixin.qq.com/s?__biz=MzAwMDczMjMwOQ==&mid=2247484099&idx=1&sn=5d8cf7ff7b0f9274cc5fd79a3edf1e42)  
2. [Java线程状态之细节回顾](https://mp.weixin.qq.com/s?__biz=MzAxNjk4ODE4OQ==&mid=2247486023&idx=2&sn=e832c2da5ea748c0940803b46805ea0b)  
3. [线程间到底共享了哪些进程资源？](https://mp.weixin.qq.com/s/L7-CypKf25bLVzIeStB-sA)  
  
#### 什么情况下线程会让出CPU 与 锁  
  
只有runnable到running时才会占用cpu时间片，其他都会出让cpu时间片。  
  
1. Thread.sleep()；让出cpu，在超时后cpu继续执行。假若是在同步代码块中，不会释放锁。  
2. Thread.yield(); 建议性让出后会再次参与竞争  
3. Object.wait()、condition.await()让出cpu 释放锁  
4. LockSupport.park() 让出cpu,释放锁资源实际上是在Condition的await()方法中实现  
   1. Condition.await()底层  
      1. 把当前线程添加到条件队列中  
      2. “完全”释放锁，也就是让state状态变量变为0  
      3. 调用LockSupport.park()阻塞当前线程  
  
5. notify()和notifyall()不会真正释放锁，要等到同步代码块执行完后才释放锁  
  
区别：  
  
1. wait()和notify()、notifyAll() 是java.lang.Object的方法，用于锁机制。wait等待其实是对象monitor，所有类都继承Object，每个对象都内置monitor对象。  
2. Thread.sleep(long) 和Thread.yield()都是Thread类的静态方法，跟锁没关系。  
3. join,本质是让调用线程wait()在当前线程对象实例上？让线程并行变串行。  
4. park/unpark与wati/notify都提供阻塞唤醒的功能，用做线程间同步，不过两者 的粒度不同，park/unpark作用在线程上，而wait/notify作用在对象上，二者没有交集。Object的wait/notify使用前必须获取对象的监视器，而park/unpark不需要。  
  
##### refrence  
  
1. [什么情况下线程会让出CPU](https://blog.csdn.net/xiaozhu0301/article/details/96881830)  
2. [sleep、yield、wait、join的区别](https://www.cnblogs.com/noteless/p/10443446.html)  
3. [ sleep、yield、join 的用法及 sleep与wait区别](https://mp.weixin.qq.com/s?__biz=MzAxNjk4ODE4OQ==&mid=2247488457&idx=2&sn=622393a5858864a4f6af99f89d0080d2)  
4. [线程间的协作(wait/notify/sleep/yield/join)](https://www.cnblogs.com/paddix/p/5381958.html)  
5. [Wait/Notify通知机制解析](https://juejin.cn/post/6844903520437551111)  
  
##### refrence  
  
1. [Java线程生命周期与状态切换](https://mp.weixin.qq.com/s?__biz=MzAwMDczMjMwOQ==&mid=2247484099&idx=1&sn=5d8cf7ff7b0f9274cc5fd79a3edf1e42)  
1. [Java总结篇系列：Java多线程（一）](https://www.cnblogs.com/lwbqqyumidi/p/3804883.html)  
1. [如何保证线程安全](https://blog.csdn.net/qq_26545305/article/details/79516610)  
1. [为什么 Java 线程没有 Running 状态](https://mp.weixin.qq.com/s?__biz=MzUzMTA2NTU2Ng==&mid=2247487565&idx=1&sn=067bf9ddb8f9db90c2bb34f09d443491)  
1. [线程休眠只会用 Thread.sleep？](https://mp.weixin.qq.com/s?__biz=MzI3ODcxMzQzMw==&mid=2247490616&idx=2&sn=531b29636b90cd73642b987e1e31fbad)  
  
## 控制并发流程  
  
1. [Semaphore自白：限流器用我就对了！](https://mp.weixin.qq.com/s?__biz=MzA5NDg3MjAwMQ==&mid=2457112913&idx=1&sn=bf88925730209956bc155273db9d29ef)  
2.  
  
## 多线程的通信  
  
1. [多线程的通信](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247487399&idx=3&sn=ff752d76ed35346a82fa29a448ba5b1b)