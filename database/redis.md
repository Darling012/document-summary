问题：
1. Redis 的客户端怎么选择？
	1. [Redis 客户端 Jedis、lettuce 和 Redisson 对比 - 程序员自由之路 - 博客园](https://www.cnblogs.com/54chensongxia/p/13815761.html)
2. Redisson 支持的延迟队列
3. 

# redis基础
1. [[Redis的最常被问到知识点总结](https://www.cnblogs.com/Young111/p/11518346.html)](https://www.cnblogs.com/Young111/p/11518346.html)
2. [Redis 内存满了怎么办？](https://mp.weixin.qq.com/s?__biz=MzAxMjEwMzQ5MA==&mid=2448888602&idx=1&sn=db0d9c28817d29a7cdab3df4b2960703)
3. [Redis数据结构与内部编码](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247492009&idx=1&sn=bfa52aea9a115b4f1a9b89a8a5c96b3c)
4. [深度解析Redis线程模型设计原理](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247492679&idx=2&sn=04f78cfdbbed4427ec22395ded83f1a6)
5. [Redis 入门指南](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247487211&idx=2&sn=0b444d329a76a9134fc6748c3a2b1d1e)
##### Redis 客户端
1. Spring boot 1.0 默认 Jedis, 阻塞 io, 同步调用
2. Spring boot 2.0 默认 lettuce
3. Redisson 提供很多分布式操作服务，如分布式锁、分布式集合、延迟队列，
4. Redisson 在 Redis 的基础上实现的 Java 驻内存数据网格（In-Memory Data Grid）。
5. 采用 lettuce + Redisson
6. 

##### Reference
1. [对于Redis中设置了过期时间的Key，你需要知道这些内容 - 掘金](https://juejin.cn/post/6844903929625444360)
2. [面试官问：Redis 内存满了怎么办？我想不到！](https://mp.weixin.qq.com/s?__biz=MzAxMjEwMzQ5MA==&mid=2448888602&idx=1&sn=db0d9c28817d29a7cdab3df4b2960703)