# MySQL 日志
##### 问题
1. 区分page cache、Buffer Pool、redo log、redo log buffer

## redo log
物理日志，事务开始之后就产生redo log。
是InnoDB引擎层的日志，用来记录事务操作引起数据的变化，记录的是数据页的物理修改。
让Mysql有了崩溃恢复的能力。

事务日志，Innodb 提供，固定大小。分四块，环形结构。
1. write position ：当前记录位置
2. checkpoint：当前要擦除的位置，擦除记录前要把记录更新到数据文件。 什么数据文件？
Redo log日志文件由两部分组成
1. 重做日志缓冲  内存
2. 重做日志文件  磁盘
##### 有什么作用？
1. mysql为了性能每次修改都是先把数据存到 Buffer poll 再用后台线程轮询同步到磁盘。
2. 每一个sql执行rodoLog buffer都会记录修改后的值
3. 假如一个事务commit后mysql发生宕机，Buffer poll内未同步到磁盘，就可以依靠rodoLog恢复数据。
4. 保证了事务的持久化特性。

##### redo log什么时候刷盘？

##### 既然redolog 也要落盘进行磁盘io, 为什么还用？
1. 因为 `Innodb` 是以  [页](三高mysql.md#^svn8s3) 为单位进行磁盘交互的，而一个事务很可能只修改一个数据页里面的几个字节，这个时候将完整的数据页刷到磁盘的话，太浪费资源了！
2. 一个事务可能涉及修改多个数据页，并且这些数据页在物理上并不连续，使用随机IO写入性能太差！
##### 既然redolog 也不是立即落盘，怎么保证数据不丢？
配合 [Binlog](MySQL日志.md#binlog)

## binlog

逻辑日志，事务提交后记录。简单理解为记录的sql语句，通过追加的方式写入。查询类操作不会记录。
#### 使用场景
1. [主从复制](主从复制配置实战.md)
2. 数据恢复
3. [Binlog的三个业务应用场景 - kingszelda - 博客园](https://www.cnblogs.com/kingszelda/p/8362612.html)
##### 三种格式
1. statement 记录 sql 原文 update T set update_time=now () where id=1，但 now () 是获取当前系统时间，就导致主从不一致。(类似 reidsAOF)
2. row 记录 sql + 具体数据  占用空间大 (类似 reidsAOF 重写)
3. Mixed 混合，有可能数据不一致就 row，没有就 statemnet
### redo log 和 bin log  差异

1. redolog是物理日志，只记录结果，在某页修改了了啥
2. Binlog 是逻辑日志，记录的是逻辑性操作，比如给 ID=1 这一行的 c 字段加 1 
3. redolog 在事务开始后不断写入，binlog 只在提交事务后才写入
## uodo log
逻辑日志
记录数据的**逻辑**变化，为了在发生错误时回滚之前的操作，需要将**之前**的**每次**操作都记录下来，这样在系统宕机或者rollback操作时就可以回滚未commit的操作。
保证了未提交事务的原子性。
##### undo log 落盘时机
[关于Innodb undo log的刷新时机？ - 知乎](https://www.zhihu.com/question/267595935)
##### 其中
1. 每条数据变更操作 insert、update、delete都伴随一条undo log的产生
2. undo log先于 数据 持久化到磁盘
3. 回滚操作就是根据undo log生成逆向sql
## 与事务关系

[面试官：你说熟悉MySQL事务，那来谈谈事务的实现原理吧！](https://blog.51cto.com/u_12302929/3287096)

[为了让你彻底弄懂MySQL事务日志，我通宵肝出了这份图解！](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247491137&idx=2&sn=fd3791d0eee60b26061ed6ace5e6fe94&chksm=9bf4aef4ac8327e256dfe20cbdecc5f88f329acb5c32bd37f64f43260380e4355eed9481012f&mpshare=1&scene=1&srcid=&sharer_sharetime=1591442885634&sharer_shareid=07754c1336c3524bfffedc4dc59111b6&key=17b3559d7137fbade4ba22e2ad79d97a8df65bceeea65da81ce3f759b716f158c5fd4db5a0cf42d3ef1a8c8046684d75ba93d6acea8ef2271ca3115a214e5d2600a24f71152941aa9c036de2e85a3565&ascene=1&uin=MTY5NjI3ODY2MQ%3D%3D&devicetype=Windows+10&version=62080079&lang=zh_CN&exportkey=AX%2FjcsjiH%2BCPf%2F%2F0cbN2524%3D&pass_ticket=es4zZkNhEe3nVxFbxpeJvIMd2DU75YFpTeC60BMByIYwkb3xhzyf0k7mw0datcKs)
## Reference
2. [谈谈传说中的redo log是什么？有啥用？](https://www.cnblogs.com/ZhuChangwu/p/14096575.html)
3. [MySQL知识体系的三驾马车 | plantegg](https://plantegg.github.io/2019/05/26/MySQL%E7%9F%A5%E8%AF%86%E4%BD%93%E7%B3%BB%E7%9A%84%E4%B8%89%E9%A9%BE%E9%A9%AC%E8%BD%A6/)

[MySQL 日志：undo log、redo log、binlog - 掘金](https://juejin.cn/post/7120460698953941029)
==更新语句会涉及到的三种日志==
1. undo log（回滚日志） ：是 Innodb 存储引擎层生成的日志，实现了事务中的原子性，主要用于事务回滚和 MVCC。
2. redo log（重做日志） ：是 Innodb 存储引擎层生成的日志，实现了事务中的持久性，主要用于掉电等故障恢复；
3. binlog （归档日志） ：是 Server 层生成的日志，主要用于数据备份和主从复制；
==为什么需要undo log?==
mysql记录事务更新前的数据到undo log日志文件，当事务发生回滚或者系统崩溃，就利用undo log回滚之前的数据。
mvcc是通过readView+undo log实现的
==为什么需要buffer pool?==
直接从磁盘读写数据慢，InnoDB设计了缓冲池（Buffer Pool）提升性能。
缓存了索引页、数据页、插入缓存页、undo页、自适应哈希索引、锁信息等等

==为什么需要redo log？==
buffer pool是基于内存的，为了防止数据丢失，当更新数据时，innoDB会先将记录写到redo log里，并更新buffer pool，这个更新操作就算完成了。然后后台线程再将buffer pool刷新到磁盘上，也就是WAL技术。
1. 什么是 redo log？
2. 被修改 Undo 页面，需要记录对应 redo log 吗？
3. redo log 和 undo log 区别在哪？
4. redo log 要写到磁盘，数据也要写磁盘，为什么要多此一举？
5. 产生的 redo log 是直接写入磁盘的吗？
6. redo log 什么时候刷盘？
7. redo log 文件写满了怎么办？

==为什么需要 binlog ？==
1. 为什么有了 binlog， 还要有 redo log？
2. redo log 和 binlog 有什么区别？
==主从复制是怎么实现的？==

[听我讲完redo log、binlog原理，面试官老脸一红](https://blog.csdn.net/qq_39390545/article/details/115214802)
1. MySQL日志主要包括六种
	1. 重做日志（redo log）
	2. 回滚日志（undo log）
	3. 归档日志（binlog）
	4. 错误日志（errorlog）
	5. 慢查询日志（slow query log）
	6. 一般查询日志（general log）
	7. 中继日志（relay log）
2. WAL 的全称是 Write-Ahead Logging，它的关键点就是先写日志，再写磁盘
3. 有了 redo log，InnoDB 就可以保证即使数据库发生异常重启，之前提交的记录都不会丢失，这个能力称为 `crash-safe`。
4. 本质上说，crash-safe 就是落盘处理，将数据存储到了磁盘上，断电重启也不会丢失。
5. 两阶段提交
	1. 更新操作先更新缓存中数据，没有的话查出加载到缓存，然后记录修改到redo log（内存或直接落盘），这时redo log处于prepare状态。
	2. 写binlog，并把binlog落盘
	3. 更新change buffer page
	4. 写redo log, 更新事务状态为commit。
6. 日志落盘
	1. innodb_flush_log_at_trx_commit 这个参数设置成 1 的时候，表示每次事务的 redo log 都直接持久化到磁盘。这样可以保证 MySQL 异常重启之后数据不丢失。
	2. sync_binlog 这个参数设置成 1 的时候，表示每次事务的 binlog 都持久化到磁盘。这样可以保证 MySQL 异常重启之后 binlog 不丢失。



[【MySQL】三大日志-binlog、redo log和undo log\_redolog和undolog谁先写\_redstone618的博客-CSDN博客](https://blog.csdn.net/MortShi/article/details/122563026)
1. 


[必须了解的mysql三大日志-binlog、redo log和undo log](https://mp.weixin.qq.com/s?__biz=MzI5NTYwNDQxNA==&mid=2247488468&idx=2&sn=38138e27e92b35eb5fb5834450d23d32)
==binlog==
1. 逻辑日志：可以简单得理解为sql语句
2. 物理日志：MySQL中数据都是保存在数据页中的，物理日志记录的是数据页上的变更；
3. binlog用于记录写入性操作，二进制形式保存在磁盘中。是逻辑日志由Server层记录，任何存储引擎都会记录。
4. binlog通过追加的方式进行写入，通过 ``max_binlog_size`` 控制每个binlog文件的大小
5. 应用于主从复制、数据恢复
6. 在事务提交时才会记录binlog, 此时记录在内存里，binlog刷盘时机
7. mysql通过 `sync_binlog` 参数控制 `biglog` 的刷盘时机，取值范围是 `0-N`
	1. 0：不去强制要求，由系统自行判断何时写入磁盘；
	2. 1：每次 commit 的时候都要将 binlog 写入磁盘；（默认值）
	3. N：每N个事务，才会将 binlog 写入磁盘。
8. binlog日志三种格式
==redo log==
1. 事务的持久性，只要事务提交成功，那么对数据库的修改就永久保存下来
2. 最简单的实现是每次事务提交，将涉及的修改后的数据页都刷新到磁盘中，但是会有两个性能问题
	1. Innodb是以页为单位进行磁盘交互，而一个事务很可能只修改一个数据页的几个字节。
	2. 一个事务可能涉及修改多个数据页，而数据页在物理上是不连续的，IO读写性能较差
3. redo log只记录事务对数据页做了哪些修改，相对页文件而言更小且是顺序读写
4. redo log分两部分
	1. 内存中的日志缓冲 `redo log buffer`
	2. 磁盘上的日志文件 `redo log file`
5. mysql执行DML语句，先写入 `redo log buffer`，后续再一次性将多个记录写入 `redo log file`。先写日志，再写磁盘，就是 `WAL(Write-Ahead Logging)` 技术。
6. 用户空间下的缓冲数据无法直接写入磁盘，需要经过内核空间。`redo log buffer``undo log buffer` 都在用户空间
7. 所以，`redo log buffer` 写入 `redo log file` 实际上是先写入 `OS Buffer` 再通过系统调用 `fsync()` 将其刷到 `redo log file`
8. `mysql` 支持三种将 `redo log buffer` 写入 `redo log file` 的时机，可以通过 `innodb_flush_log_at_trx_commit` 参数配置
	1. 0，事务提交不会将 `redo log buffer` 写入 `OS Buffer`，而是后台每秒写入 `OS Buffer` 并调用 `fsync()` 写到 `redo log file`。
	2. 1，事务每次提交都会 `redo log buffer` 写入 `OS Buffer` 并调用 `fsync()` 写到 `redo log file`
	3. 2，事务每次提交都会 `redo log buffer` 写入 `OS Buffer` ，然后每秒调用 `fsync()` 写到 `redo log file`
9. redo log记录形式
==redo log 和 binlog 区别==
1. 文件大小
2. 实现方式
3. 记录形式
4. 使用场景

[Redologbuff 什么时候刷盘到redologfile](https://www.51cto.com/article/674976.html)
1. InnoDB存储引擎的redo log (重做日志)，MySQL Servce层的 binlog (归档日志)
2. redo log (重做日志)是InnoDB存储引擎独有的，它让MySQL拥有了崩溃恢复能力。
3. 比如MySQL实例挂了或宕机了，重启时，InnoDB存储引擎会使用redo log恢复数据，保证数据的持久性与完整性。
4. MySQL中数据以页为单位，查询一条记录，会从硬盘把一页的数据加载到Buffer Pool中，后续的查找都是先从Buffer Pool中找，命中不了再去硬盘加载，提高IO效率。
5. 更新操作，数据存在Buffer Pool，就直接更新。然后会把"某页记录做了哪些修改"存到 `重做日志缓存（redo log buffer）` 里，然后刷新到 `redo log` 文件中。
6. 那么什么时候刷盘呢？
7. InnoDB为redo log的刷盘策略提供了 `innodb_flush_log_at_trx_commit` 参数，它支持三种策略
	1. 设置为0的时候，表示每次事务提交时不进行刷盘操作
	2. 设置为1的时候，表示每次事务提交时都将进行刷盘操作 (默认值)
	3. 设置为2的时候，表示每次事务提交时都只把redo log buffer内容写入page cache
8. 另外InnoDB存储引擎有一个后台线程，每隔1秒，就会把redo log buffer中的内容写到文件系统缓存 (page cache)，然后调用fsync刷盘。
9. 还有一种情况，当redo log buffer占用的空间即将达到 `innodb_log_buffer_size` 一半的时候，后台线程会主动刷盘。
10. 综合来看
	1. 设置为0，可能会有1s的数据丢失
	2. 设置为1
		1. 事务提交时，redo log就刷新到硬盘了。所以1s内数据不会丢。
		2. 事务执行时宕机，因为事务未提交，所以数据也没问题。（应该会根据undo log 回滚）
	3. 设置为2
		1. 事务提交后，假如宕机，因为没有调用fsync刷盘到硬盘，所以会有1s丢失。
11. 硬盘上的redo log文件是个环状的文件组，写完一个再写下一个
12. 在Buffer Poll中修改完直接刷盘不可以么？为什么还要redo log？
13. 


[MySQL三大日志(binlog、redo log和undo log)详解](https://github.com/Snailclimb/JavaGuide/blob/main/docs/database/mysql/mysql-logs.md)

[详解MySQL的Redo日志与Undo日志](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247489853&idx=2&sn=9f1b2b2535b63e9c5614743694ed5a71)