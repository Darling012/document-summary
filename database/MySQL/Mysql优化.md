# MySQL优化


## 查询

优化场景 
1. 数据填充优化， n+1 问题
2. 分页优化

### mysql n+1 问题

思路是减少数据库连接，把需要用的数据加载到内存，然后进行封装。
1. [mysql优化之N+1问题_殷桃小狗子的博客-CSDN博客_mysql n+1](https://blog.csdn.net/Aurora_____/article/details/113527905)
2. [为什么强烈建议你不要做联表查询？](https://mp.weixin.qq.com/s/5FdMdBmYxpnd56jcdOi0lw)
##### spring data jpa n+1 问题
[解决Spring Data Jpa懒加载的N+1问题_皓亮君的博客-CSDN博客_jpa 懒加载](https://blog.csdn.net/ming19951224/article/details/123750438)
[Spring-data-jpa n+1问题 - 月满清爵 - 博客园](https://www.cnblogs.com/ymqj520/p/11589934.html)


### mysql 分页查询优化

思路是 hashtable 一样，可以通过计算得知位置，再去查询。在 mysql 场景下就是 主键要递增，可计算出要查询分页数据的 ID，然后用 ID 过滤再分页。
[MySQL分页查询优化 - 悠悠i - 博客园](https://www.cnblogs.com/youyoui/p/7851007.html)
1. 使用子查询优化
2. 使用 ID 限定优化
3. 使用临时表优化
### 深分页
[实战！聊聊如何解决MySQL深分页问题 - 掘金](https://juejin.cn/post/7012016858379321358)


## 索引优化
![MySQL索引](MySQL索引.md#索引优化)


## reference
[MySQL 如何优化大分页查询?](https://mp.weixin.qq.com/s?__biz=MzI5NTYwNDQxNA==&mid=2247485219&idx=1&sn=2df9ef61ae9170c2f0589a37cecd1b9f)
[MySQL 的 order by 优化](https://mp.weixin.qq.com/s?__biz=MzA5NDg3MjAwMQ==&mid=2457103315&idx=1&sn=e8e110a89f616726b6145fe89a7e1f89)
[Mybatis 一对多查询 分页问题 - たかぎ](https://lixingyong.com/2020/08/13/mybatis)
[我必须得告诉大家的 MySQL 优化原理 - 腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/news/223156)
[truncate、delete和drop的6大区别](https://mp.weixin.qq.com/s/3lk3CZ9vCeWVaRIjl9mUUQ)
7. [记住，永远不要在MySQL中使用utf8](https://www.infoq.cn/article/in-mysql-never-use-utf8-use-utf8)
8. [19条效率至少提高3倍的MySQL技巧](https://mp.weixin.qq.com/s?__biz=MzU0MzQ5MDA0Mw==&mid=2247485751&idx=1&sn=edb8fd8d09715cbffe6b051a0fdad9ed)
11. [MySQL慢查询之慢 SQL 定位、日志分析与优化方案](https://mp.weixin.qq.com/s/ok6yylJxalkeB8oyRo6LSg)
12. [SQL优化 21 连击 + 思维导图](https://mp.weixin.qq.com/s/muSBSZx6UJKNpzDQe4UvOQ)
13. [图解 SQL 执行顺序](https://mp.weixin.qq.com/s/n6Z3_dDv-sOHUhSD9HMSCw)
14. [面试官问我MySQL调优，我真的是 - 掘金](https://juejin.cn/post/7017969370206830623)
15. [优化慢SQL](https://mp.weixin.qq.com/s/E5yC_Iss2BWD0SiNFuTtwg)



