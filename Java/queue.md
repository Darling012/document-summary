# queue

[栈(stack), 堆(heap), 队列(queue) 是什么？](https://limingxie.github.io/basic/stack/)
1. 栈 (stack)
2. 堆 (heap)
	1. 二叉堆，完全二叉树
3. 队列 (queue)
	1. 先进先出: (FIFO, First-In-First-Out) 的原则存储数据。  
	2. 基于数组的队列
	3. 基于链表的队列
	4. 循环队列
	5. 双端队列 (deque，全名double-ended queue)
	6. 优先队列

[吃多了拉就是队列，吃多了吐就是栈](https://mp.weixin.qq.com/s?__biz=MzIyODE5NjUwNQ==&mid=2653315428&idx=1&sn=5bac30dd38f231aa07a81e34b6a40896)
[java集合类——Stack栈类与Queue队列 - 梦之心上 - 博客园](https://www.cnblogs.com/whsa/p/4272717.html)
### 分类  
  
物理结构：  
+ 顺序存储结构：数组  
+ 链式存储结构：链表  
  
逻辑结构：  
+ 线性结构：栈、队列、顺序表  
+ 非线性结构： 树、图  
  
#### queue  




#### refence  

[45张图庖丁解牛18种Queue，你知道几种？](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247495665&idx=3&sn=472d39e331f47abc45f8d573db436b9d)  
![](Pasted%20image%2020231016113636.png)
![](img.png)




[聊聊java中那些各式各样的queue](https://juejin.cn/post/6844904017881989127)  
1. 有界无界
	1. 名字带array基本就是个数组，数组肯定需要指定大小，都有边界。
	2. link一般是链表，只要内存够就可以无界，但像LinkedBlockingQueue还是会指定一个最大值方式内存爆炸。
2. 并发性
	1. 
3. 使用场景
4. ConcurrentLinkedQueue的一个坑
	1. size方法会去遍历链表节点来确定size
5. Disruptor环形队列
6. 分布式消息队列

[Java多线程总结之聊一聊Queue](https://www.iteye.com/blog/hellosure-1126541)  


[Java中的5大队列，你知道几个？](https://mp.weixin.qq.com/s/pvcLcJUBXqS9IS7i0IeOxA)  [博主更多的数据结构和算法博客](https://github.com/vipstone/algorithm)
队列，逻辑结构，就像隧道，先进先出。  
  
1.  按阻塞分类  
	1. 阻塞队列  （队列满 put阻塞  队列空 take阻塞）  
		1. ArrayBlockingQueue  
		2. LinkedBlockingQueue  
		3. PriorityBlockingQueue  
		4. ...
	2. 非阻塞队列    （队列满了以后直接返回错误）  
		1. ConcurrentLinkedQueue   
		2. ConcurrentLinkedDeque
		3. PriorityQueue  
		4. ...
2. 按大小 
	1. 有界（固定大小）  
		1. ArrayBlockingQueue
		2. SynchronousQueue
	2. 无界（没有设定大小，但有默认值Integer. MAX_VALUE）  
3. 按功能
	1. 普通队列
		1. 是指实现了先进先出的基本队列，例如 `ArrayBlockingQueue` 和 `LinkedBlockingQueue`
	2. 双端  
		1. 双端队列（Deque）是指队列的头部和尾部都可以同时入队和出队的数据结构
	3. 优先 
		1. 不是先进先出的，而是优先级高的元素先出队，优先队列是根据二叉堆实现的
	4. 延迟  
		1. 延迟队列（DelayQueue）是基于优先队列 `PriorityQueue` 实现的，它可以看作是一种以时间为度量单位的优先的队列
	5.  `SynchronousQueue`
		1. 它内部没有容器，每次进行 `put()` 数据后（添加数据），必须等待另一个线程拿走数据后才可以再次添加数据





[这个队列的思路真的好，现在它是我简历上的亮点了。](https://mp.weixin.qq.com/s/BTVDPHVWjBiq9mj3mvT_Vw)  


[java基础知识博客](https://github.com/feigeswjtu/java-basics)内有几个自己实现的Java collection