### mybatisPlus批量操作

1. [mybatis-plus 关于savebatch,saveorupdatebatch遇到的坑及解决办法](https://blog.csdn.net/qq_28025423/article/details/115680196)
2. [mybatis-plus批量插入InsertBatchSomeColumn](https://blog.csdn.net/qq_35549286/article/details/113603176)
3. [JDBC的URL设置allowMultiQueries的原因](https://www.cnblogs.com/jeffen/p/6038261.html)
4. [mysql开启批量执行sql参数allowMultiQueries与批处理 rewriteBatchedStatements=true(加速batchUpdate)](https://blog.csdn.net/weixin_43944305/article/details/106824519)
5. [Mybatis Plus 自定义批量更新和插入方法](https://blog.csdn.net/JonKee/article/details/119772797)
6. [mybatis的批处理(效率)之rewriteBatchedStatements和allowMultiQueries](https://blog.csdn.net/qq_44413835/article/details/117113156)
7. [MyBatis 批量插入数据的 3 种方法！](https://segmentfault.com/a/1190000040781858)

8. [MyBatis-Plus 批处理有坑，我教你改造](https://juejin.cn/post/6939777685170159629)

9. [优化mybatis-plus的批量插入功能](https://www.modb.pro/db/88140)

10. 大批量更新数据mysql**批量更新的四种方法** https://blog.csdn.net/u014520745/article/details/52416002

11. https://www.jianshu.com/p/f1633e7e5739

12. https://blog.csdn.net/j1231230/article/details/111386018

13. https://github.com/baomidou/mybatis-plus/issues/1406

14. https://blog.csdn.net/weixin_45369440/article/details/116044771?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_antiscanv2&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_antiscanv2&utm_relevant_index=1



锁超时

1. [MySQL事务锁等待超时 Lock wait timeout exceeded; try restarting transaction](https://juejin.cn/post/6844904078749728782)
2. 





1. 单条sql，批量提交
   1. 需要开启数据库批量提交rewriteBatchedStatements=true

   2. mybatisplus的saveOrUpdateBatch会生成一条查询sql

   3. [mysql 批量插入优化之rewriteBatchedStatements](https://www.cnblogs.com/grefr/p/6088000.html)

   4. [MySQL 和 JDBC 与 rewriteBatchedStatements=true](https://www.learnfk.com/question/mysql/26307760.html)

   5. [rewriteBatchedStatements实现原理](https://www.jianshu.com/p/7eb8eec78b9a)

   6. [MySQL之rewriteBatchedStatements](https://www.jianshu.com/p/04d3d235cb9f)

   7. [jdbc mysql设置rewriteBatchedStatements参数实现高性能批量处理 executeBatch返回值问题](https://blog.csdn.net/chuangxin/article/details/83447387)

   8. [mybatis puls 批量插入方法的建议](https://github.com/baomidou/mybatis-plus/issues/2456)

   9. [mybatis plus saveBatch批量插入较慢](https://github.com/baomidou/mybatis-plus/issues/2786)

2. 大sql更新
   1. insert into user(id, name, age) values (1, "a", 17), (2,"b", 18)
      1. [MySql数据库Update批量更新与批量更新多条记录的不同值实现方法](https://cloud.tencent.com/developer/article/1886324)
   2. 数据量过大或字段过多造成拼接sql过长问题
      1. SQL语句是有长度限制，通过max_allowed_packet参数修改

##### mySql执行以上两种sql效率分析

1. [MySQL批量SQL插入性能优化](https://segmentfault.com/a/1190000008890065)

#### 逻辑删除与唯一索引冲突

1. [Mybatis-Plus中逻辑删除与唯一索引冲突的解决方案](https://baobao555.tech/archives/39)
2. [逻辑删除和唯一索引](https://chsm1998.github.io/2020/08/29/logical-deletion-and-unique-index/)

