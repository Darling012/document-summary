# MyBatis-Plus 批量操作

#### 三种插入方式
1. 每次执行一条，循环执行insert into user(id, name, age) value (1, "a", 17)；
2. 批量执行多条insert into user(id, name, age) value (1, "a", 17)；
3. 执行一条sql插入多条数据insert into user(id, name, age) values (1, "a", 17), (2,"b", 18)；
##### reference
 [MyBatis 批量插入数据的 3 种方法！](https://segmentfault.com/a/1190000040781858)
 1.  循环单次插入
 2. MP 批量插入功能
	 1. 从上述源码可以看出，MP 是将要执行的数据分成 N 份，每份 1000 条，每满 1000 条就会执行一次批量插入
 3. 原生批量插入功能（性能最高）
	 1. 依靠 MyBatis 中的 foreach 标签，将数据拼接成一条原生的 insert 语句一次性执行的

[MyBatis批量插入几千条数据慎用foreach_huanghanqian的博客-CSDN博客](https://blog.csdn.net/huanghanqian/article/details/83177178/)
1. 如果MyBatis需要进行批量插入，推荐使用 ExecutorType. BATCH 的插入方式
2. 如果非要使用 `<foreach>` 的插入的话，需要将每次插入的记录控制在 20~50 左右


#### 三个配置参数
1. rewriteBatchedStatements=true：批量将数据传给mysql
2. allowMultiQueries=true：允许一次性执行多条sql
3. max_allowed_packet：单条sql长度限制
##### refrence
1. [mybatis的批处理(效率)之rewriteBatchedStatements和allowMultiQueries](https://blog.csdn.net/qq_44413835/article/details/117113156)
2. [mysql开启批量执行sql参数allowMultiQueries与批处理 rewriteBatchedStatements=true(加速batchUpdate)](https://blog.csdn.net/weixin_43944305/article/details/106824519)

### MyBatis-Plus批量操作
1. IService的saveBatch在配置参数后为方式2否则为方式1
   1. [MySQL的JDBC连接的url中要加rewriteBatchedStatements参数，并保证5.1.13以上版本的驱动，才能实现高性能的批量插入。 · Issue #3902 · baomidou/mybatis-plus · GitHub](https://github.com/baomidou/mybatis-plus/issues/3902)
2. [实现方式3](https://blog.csdn.net/qq_35549286/article/details/113603176)
   1. [Mybatis Plus 实现批量插入](https://blog.csdn.net/j1231230/article/details/111386018)
3. [自己实现方式3](https://blog.csdn.net/qq_28025423/article/details/115680196)
   1. [Mybatis Plus 自定义批量更新和插入方法](https://blog.csdn.net/JonKee/article/details/119772797)
   2. [优化mybatis-plus的批量插入功能](https://www.modb.pro/db/88140)
4. 补充
   1. [mybatis puls 批量插入方法的建议](https://github.com/baomidou/mybatis-plus/issues/2456)
   2. [mybatis plus saveBatch批量插入较慢](https://github.com/baomidou/mybatis-plus/issues/2786)
##### 两种方式效率对比
1. [MyBatis-Plus 批处理有坑，我教你改造](https://juejin.cn/post/6939777685170159629)
2. [mybatisplus的saveOrUpdateBatch会生成一条查询sql](https://github.com/baomidou/mybatis-plus/issues/1406)

### 在执行批量操作时加事务锁超时
 [MySQL事务锁等待超时 Lock wait timeout exceeded; try restarting transaction](https://juejin.cn/post/6844904078749728782)
 1. `Lock wait timeout exceeded`：后提交的事务等待前面处理的事务释放锁，但是在等待的时候超过了mysql的锁等待时间
 2. `Dead Lock`：两个事务互相等待对方释放相同资源的锁，从而造成的死循环，就会引发这个异常
 3. `innodb_lock_wait_timeout`：innodb的dml操作的行级锁的等待时间
 4. `lock_wait_timeout`：数据结构ddl操作的锁的等待时间
##### rewriteBatchedStatements参数
1. [mysql 批量插入优化之rewriteBatchedStatements](https://www.cnblogs.com/grefr/p/6088000.html)
2. [MySQL 和 JDBC 与 rewriteBatchedStatements=true](https://www.learnfk.com/question/mysql/26307760.html)
3. [rewriteBatchedStatements实现原理](https://www.jianshu.com/p/7eb8eec78b9a)
4. [MySQL之rewriteBatchedStatements](https://www.jianshu.com/p/04d3d235cb9f)
5. [jdbc mysql设置rewriteBatchedStatements参数实现高性能批量处理 executeBatch返回值问题](https://blog.csdn.net/chuangxin/article/details/83447387)
#### 逻辑删除与唯一索引冲突
1. [Mybatis-Plus中逻辑删除与唯一索引冲突的解决方案](https://baobao555.tech/archives/39)
2. [逻辑删除和唯一索引](https://chsm1998.github.io/2020/08/29/logical-deletion-and-unique-index/) 
##### 其他
1. [MySQL批量SQL插入性能优化](https://segmentfault.com/a/1190000008890065)
2. [MySql数据库Update批量更新与批量更新多条记录的不同值实现方法](https://cloud.tencent.com/developer/article/1886324)
3. [MySQL面试常问：一条语句提交后，数据库都做了什么？](https://www.51cto.com/article/675364.html)
4. [SpringBoot 高效批量插入万级数据\_hughmu的博客-CSDN博客](https://blog.csdn.net/weixin_44030143/article/details/130825037)