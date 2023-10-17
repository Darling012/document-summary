# redis 基础
##### 六种底层数据结构
1. 简单动态字符串 
2. 双向链表 linkedlist
3. 压缩列表 ziplist
4. 哈希表 hashtable
5. 跳表 skiplist
6. 整数数组 intset

##### 五种数据类型
1. 字符串 string
	1. 
2. 散列 hash
	1. hashtable、ziplist
3. 列表 list
	1. linkedlist、ziplist
4. 集合 set
	1. hashtable、intset
5. 有序集合 zset
	1. skiplist、ziplist
额外
1. 位图 bitmap ^ntx6uf
	1. 本质字符串
2. 超小内存唯一值计数 hyperloglog
	1. 本质字符串
3. 地理信息定义 geo
	1. 本质 set

[Redis进阶 - 数据结构：底层数据结构详解 | Java 全栈知识体系](https://pdai.tech/md/db/nosql-redis/db-redis-x-redis-ds.html)
##### reference
1. [[Redis的最常被问到知识点总结](https://www.cnblogs.com/Young111/p/11518346.html)](https://www.cnblogs.com/Young111/p/11518346.html)
2. [Redis 内存满了怎么办？](https://mp.weixin.qq.com/s?__biz=MzAxMjEwMzQ5MA==&mid=2448888602&idx=1&sn=db0d9c28817d29a7cdab3df4b2960703)
3. [Redis数据结构与内部编码](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247492009&idx=1&sn=bfa52aea9a115b4f1a9b89a8a5c96b3c)
4. [深度解析Redis线程模型设计原理](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247492679&idx=2&sn=04f78cfdbbed4427ec22395ded83f1a6)
5. [Redis 入门指南](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247487211&idx=2&sn=0b444d329a76a9134fc6748c3a2b1d1e
1. [对于Redis中设置了过期时间的Key，你需要知道这些内容 - 掘金](https://juejin.cn/post/6844903929625444360)
2. [面试官问：Redis 内存满了怎么办？我想不到！](https://mp.weixin.qq.com/s?__biz=MzAxMjEwMzQ5MA==&mid=2448888602&idx=1&sn=db0d9c28817d29a7cdab3df4b2960703)



[渐进式 rehash · Redis 设计与实现（第二版） · 看云](https://www.kancloud.cn/kancloud/redisbook/63842)