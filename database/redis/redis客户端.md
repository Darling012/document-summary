##### Redis 客户端
1. Spring boot 1.0 默认 Jedis, 阻塞 io, 同步调用
2. Spring boot 2.0 默认 lettuce
3. Redisson 提供很多分布式操作服务，如分布式锁、分布式集合、延迟队列，
4. Redisson 在 Redis 的基础上实现的 Java 驻内存数据网格（In-Memory Data Grid）。
5. 采用 lettuce + Redisson

##### reference
1. [Redis 客户端 Jedis、lettuce 和 Redisson 对比 - 程序员自由之路 - 博客园](https://www.cnblogs.com/54chensongxia/p/13815761.html)