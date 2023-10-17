# queue
### 分类  
物理结构：  
+ 顺序存储结构：数组  
+ 链式存储结构：链表  
逻辑结构：  
+ 线性结构：栈、队列、顺序表  
+ 非线性结构： 树、图  
#### queue  
队列，逻辑结构，就像隧道，先进先出。  
- ##### 按阻塞分类  
  - 阻塞队列  （队列满 put阻塞  队列空 take阻塞）  
    - ArrayBlockingQueue  
    - LinkedBlockingQueue  
    - PriorityBlockingQueue  
  - 非阻塞队列（队列满了以后直接返回错误）  
    - ConcurrentLinkedQueue   
    - PriorityQueue  
- 按大小  
  - 有界（固定大小）  
    - ArrayBlockingQueue  
    - SynchronousQueue  
  - 无界（没有设定大小，但有默认值Integer.MAX_VALUE）  
    -   
- 功能  
  - 普通  
  - 双端  
  - 优先  
    > 二叉堆实现  
  - 延迟  
  - 其他  
#### refence  
1. [45张图庖丁解牛18种Queue，你知道几种？](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247495665&idx=3&sn=472d39e331f47abc45f8d573db436b9d)  
2. [聊聊java中那些各式各样的queue](https://juejin.cn/post/6844904017881989127)  
3. [Java中的5大队列，你知道几个？](https://mp.weixin.qq.com/s/pvcLcJUBXqS9IS7i0IeOxA)  
4. [这个队列的思路真的好，现在它是我简历上的亮点了。](https://mp.weixin.qq.com/s/BTVDPHVWjBiq9mj3mvT_Vw)  
5. [Java多线程总结之聊一聊Queue](https://www.iteye.com/blog/hellosure-1126541)  
6. https://github.com/vipstone/algorithm  
7. https://github.com/feigeswjtu/java-basics  
  