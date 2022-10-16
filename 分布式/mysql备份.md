## mysql 复制

[Mysql复制](Mysql复制.md#Mysql复制)
[复制与备份关系](https://blog.csdn.net/m0_37628958/article/details/105311388)

## MySQL 备份
[数据库有哪些种类的备份](三高mysql.md#数据库有哪些种类的备份)
[理解Mysql备份与恢复](https://mp.weixin.qq.com/s?__biz=MzU4MTg3OTA3MQ==&mid=2247484028&idx=1&sn=19c3e2cea07c0b1aff6ea021d2b4fe82)
#### 类型
1. 完整备份：备份所有数据
2. [增量备份](如何使用mysqldump+binlog增量备份.md#如何使用mysqldump%20binlog增量备份)：仅备份上次完整备份或增量备份以来变化的数据 
3. 差异备份：仅备份上次完整备份以来变化的数据  
4. 热备份：正常运行备份，数据库读写不受影响（mysqldump-->innodb）
5. 温备份：数据库仅可以执行读操作（mysqldump-->myisam）
6. 冷备份：离线备份，读写都不可用，数据库停机无法进行任何操作  
7. 逻辑备份：将 sql 语句导出文本文件中（mysqldump），可阅读，与存储引擎无关
8. 物理备份：裸文件，将数据库底层文件拷贝（xtrabackup、mysqlhotcopy），不可阅读，与存储引擎有关
9. 日志备份：[binlog](mysql日志.md#binlog) 备份

##### 存储引擎所支持的形式
MyISAM：温备，不支持热备  
InnoDB：都支持  

1. 按颗粒度分
	1. 全量备份
		1. 对整个数据库进行备份，根据备份内容不同，可以分为 [mysql备份](mysql备份.md#^o7qhqg)、[mysql备份](mysql备份.md#^yoi33q)两种形式
	2. 增量备份
		1. 对日志文件 binlog进行备份
		2. [canal](https://mp.weixin.qq.com/s/57kNk7BPQTRTfuiyPvW9rQ)
		3. [go-mysql-transfer](https://blog.51cto.com/u_9928699/2893264)
		4. [分布式CDC框架Debezium](https://mp.weixin.qq.com/s/RbICmCM9REIoDFEZP28fGA)
	3. 差异备份
2. 按备份文件格式分
	1. 物理备份  ^yoi33q
		1. 速度快，但只能针对一整个数据库实例
		2. xtrabackup: 直接备份 Innodb 的数据文件，支持全量、增量、热备份
		3. Clone Plugin： 8.0.17 后推出的插件，
		4. 
	2. 逻辑备份 ^o7qhqg
		1. 逻辑内容，即 sql
		2. mysqldump: 官方提供，支持逻辑、热、全量备份；单线程。
		3. mysqlpump (5.7): 官方提供，多个表多线程并行备份，但大于 1 个线程不能构建一致性备份。单表过大还是慢。
		4. mysqldumper: 开源，支持一致性备份，支持单表分片多线程备份，多线程恢复等。
		5. [Mysqldump vs Mysqlpump vs Mydumper](https://mydbops.wordpress.com/2019/03/26/mysqldump%E2%80%8B-vs-mysqlpump-vs-mydumper/)
3. 是否影响可用性
	1. 热备份
	2. 冷备份
	3. 温备份
##### 备份和备库的区别
备份和备库，这里我们需要注意的是两者之间是有区别的，并不是做了其中之一就行了，两者在功能上还是有些差异的。备库就好像一个替身，在主库出现问题的时候可以快速切换，但有的问题并不能通过备库来解决，比如主库上发生了误操作把数据删除了，这个操作会同步到和备库，导致备库的数据也被删除；备份是就是对同一数据库数据的多次备份，在出现问题的时候可以用来恢复数据，就好像时间旅行，可以回到过去，取回历史上存在的数据。但是数据恢复的周期可能会比较长。备份和备库两者互相补充，是数据库容灾最基本的两种手段。

## 工具
### Outfile 命令
1. mysql 自带命令，同时也是 mysql 预留关键字。
2. 最原始的**逻辑备份**方式
3. 实际不用，只作了解
### mysqldump 逻辑备份
1. 可以看做 outfile 的扩展
2. 
### mysqldumper 逻辑备份
### mysqlpump 逻辑备份
Mysqlpump主要特点  
-  并行备份数据库和数据库中的对象的，加快备份过程。  
- 更好的控制数据库和数据库对象（表，存储过程，用户帐户）的备份。  
- 备份用户账号作为帐户管理语句（CREATE USER，GRANT），而不是直接插入到MySQL的系统数据库。  
- 备份出来直接生成压缩后的备份文件。  
- 备份进度指示（估计值）。  
- 重新加载（还原）备份文件，先建表后插入数据最后建立索引，减少了索引维护开销，加快了还原速度。  
- 备份可以排除或则指定数据库。  
Mysqlpump缺点  
- 只能并行到表级别,如果表特别大,开多线程和单线程是一样的,并行度不如mydumper；  
- 无法获取当前备份对应的binlog位置；  
- MySQL5.7.11之前的版本不要使用,并行导出和single-transaction是互斥的；

### XtraBackup 物理备份

## 实践
备份策略：全量备份 + 增量备份。实现基于时间点的恢复。
全量备份记录这个备份对应的时间点位，某个 GTID 位置，增量备份就可以重放这个点位后的日志。
1. 一周一次全量备份
	1. 备份文件多份做容灾
	2. 备份文件校验
2. 
