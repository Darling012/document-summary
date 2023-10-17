# MySQL 备份

## 问题
1. 逻辑备份的工具原理需要对所有表加读锁，那么还适合热备吗？或者只适合在读库热备？
2. 全量备份采用冷备还是热备? 
	1. 应该先考虑具不具备冷备的条件。
3. 数据迁移
	1. [数据迁移工具，用这8种](https://mp.weixin.qq.com/s/bz2PbFkUT1jcmP8e4MJs2Q)


## 总结

1. 复制是实现备份的一种，复制基于 binlog，备份中有 binlog 备份。
2. mysqldump 用于逻辑备份，xtrabackup 用于物理备份
3. mysql 自带的Outfile 命令是最原始的逻辑备份方式，也是 mysql 的关键字
4. mysqldump 可以看做 outfile 的扩展。
5. 一般物理备份是直接拷文件，所以不能热备，因为会有新数据。但xtrabackup 可以实现热备。
6. 物理备份和逻辑备份都可实现全量备份
7. 增量备份通过 binlog 实现。
8. 备库是为了故障转移，数据一直和主库一致，备份是按时间节点备份数据，可回退

## mysql 复制

[复制与备份关系](https://blog.csdn.net/m0_37628958/article/details/105311388)
[MySQL复制](MySQL复制.md#Mysql复制)是备份基础，是实现备份的一种方式。

##### 备份和备库的区别
备库就好像一个替身，在主库出现问题的时候可以快速切换，但有的问题并不能通过备库来解决，比如主库上发生了误操作把数据删除了，这个操作会同步到和备库，导致备库的数据也被删除；备份是就是对同一数据库数据的多次备份，在出现问题的时候可以用来恢复数据，就好像时间旅行，可以回到过去，取回历史上存在的数据。但是数据恢复的周期可能会比较长。备份和备库两者互相补充，是数据库容灾最基本的两种手段。

##  MySQL 备份分类

==存储引擎所支持的形式==
MyISAM：温备，不支持热备  
InnoDB：都支持  

1. 完整备份：备份所有数据
2. [增量备份](如何使用mysqldump+binlog增量备份.md#如何使用mysqldump%20binlog增量备份)：仅备份上次完整备份或增量备份以来变化的数据 
3. 差异备份：仅备份上次完整备份以来变化的数据  
4. 热备份：正常运行备份，数据库读写不受影响（mysqldump-->innodb）
5. 温备份：数据库仅可以执行读操作（mysqldump-->myisam）
6. 冷备份：离线备份，读写都不可用，数据库停机无法进行任何操作  
7. 逻辑备份：将 sql 语句导出文本文件中（mysqldump），可阅读，与存储引擎无关
8. 物理备份：裸文件，将数据库底层文件拷贝（xtrabackup、mysqlhotcopy），不可阅读，与存储引擎有关
9. 日志备份：[binlog](MySQL日志.md#binlog) 备份

![数据库有哪些种类的备份](三高mysql.md#数据库有哪些种类的备份)

1. 按颗粒度分
	1. 全量备份
		1. 对整个数据库进行备份，根据备份内容不同，可以分为 [逻辑备份](MySQL备份.md#^o7qhqg)、[物理备份](MySQL备份.md#^yoi33q)两种形式
	2. 增量备份
		1. 对日志文件 binlog 进行备份
		2. [canal](https://mp.weixin.qq.com/s/57kNk7BPQTRTfuiyPvW9rQ)
		3. [go-mysql-transfer](https://blog.51cto.com/u_9928699/2893264)
		4. [分布式CDC框架Debezium](https://mp.weixin.qq.com/s/RbICmCM9REIoDFEZP28fGA)
	3. 差异备份
2. 按备份文件格式分
	1. 物理备份  ^yoi33q
		1. 速度快，但只能针对一整个数据库实例
		2. 备份文件为frm文件、ibd文件、binlog文件、redo log文件等
	2. 逻辑备份 ^o7qhqg
		1. 逻辑内容，即 sql
3. 是否影响可用性
	1. 热备份
	2. 冷备份
	3. 温备份

## 备份策略

1. mydumper 逻辑备份 和 XtraBackup 物理备份都需要独立安装，可通过 docker 解决
2. 冷备只能选择物理备份，因为逻辑备份要 MySQL 服务开着。
3. 

备份策略：全量备份 + [增量备份](三高mysql.md#8-4%20如何使用%20mysqldump%20binlog%20增量备份)。实现基于时间点的恢复。
全量备份记录这个备份对应的时间点位，某个 GTID 位置，增量备份就可以重放这个点位后的日志。
1. 一周一次全量备份
	1. 备份文件多份做容灾
	2. 备份文件校验
2. 



[理解Mysql备份与恢复](https://mp.weixin.qq.com/s?__biz=MzU4MTg3OTA3MQ==&mid=2247484028&idx=1&sn=19c3e2cea07c0b1aff6ea021d2b4fe82)
==物理 vs 逻辑==
1. 物理备份操作简单恢复速度快，但占用空间大，不能保证数据百分百可用，需配合逻辑备份
2. 数据量大会耗时，只能温备和热备对数据库性能有影响。（没有完整保存浮点精度？）
==备份思想==
1. 生产环境物理备份是必须的，选择 XtraBackup 或 LVM 的文件快照方式
2. 防止物理备份文件损坏，定时逻辑备份也是需要的，建议二进制增量逻辑备份方式

[MySQL中如何选择合适的备份策略和备份工具](https://blog.csdn.net/weixin_48612224/article/details/121016736)
==总结==
1. 尽量选择物理备份
2. 使用逻辑备份尽量选择多线程
3. 尽量增加备份频率，缩短备份周期

## 工具使用

[MySQL 常用备份工具流程解析](https://juejin.cn/post/7065566178625880101)
1. 逻辑备份
	1. mysqldump 等
2. 物理备份
	1. mysqlbackup、XtraBackup 等
3. binlog 备份
	1. [mysqlbinlog 命令](https://blog.csdn.net/line_on_database/article/details/115487650)

这里先说一下 binlog 备份，它只是把 binlog 又复制了一份，并且需要在逻辑备份或者物理备份的基础上才能进行数据恢复，无法单独进行数据恢复。

==mysqldump==
核心是对每个表进行 select 然后转化成相应的 insert 语句。备份流程大致如下
1. 对某个库下<font color=#81B300>所有表</font>加读锁；
2. 循环备份表数据；
3. 释放读锁。
4. 重复以上三个步骤。

要注意备份出来的 sql 文件会对现有数据有什么影响。

### 物理备份工具
1. [XtraBackup 物理备份](MySQL备份.md#XtraBackup%20物理备份)
	1. 直接备份 Innodb 的数据文件，支持全量、增量、热备份
2. mysqlbackup
	1. MySQL 企业级备份工具（ MySQL Enterprise Backup ），适用于 MySQL 企业版
3. Clone Plugin
	1. MySQL 8.0.17 引入的克隆插件。初衷是为了方便 Group Replication 添加新的节点。有了 Clone Plugin，我们也能很方便的搭建一个从库，无需借助其它备份工具。

三者的实现原理基本相同，都是在备份的过程中，拷贝物理文件和 redo log ，最后，再利用 InnoDB Crash Recovery，将物理文件恢复到备份结束时的一致性状态。

### 逻辑备份工具
1. [Outfile 命令](三高mysql.md#8-2%20如何使用%20OUTFILE%20命令进行备份)
	1. SELECT ... INTO OUTFILESQL 命令，可将表记录直接导出到文件中。
2. [mysqldump 逻辑备份](三高mysql.md#8-3%20如何使用%20mysqldump%20进行备份)
	1. 官方提供，支持逻辑、热、全量备份；单线程。
3. [mysqlpump 逻辑备份](MySQL备份.md#mysqlpump%20逻辑备份)
	1. mysql 5.7 引入，官方提供，多个表多线程并行备份，但大于 1 个线程不能构建一致性备份。单表过大还是慢。
4. [ mysqldumper 逻辑备份](MySQL备份.md#mysqldumper%20逻辑备份)
	1. 开源，支持一致性备份，支持单表分片多线程备份，多线程恢复等。
5. [MySQL 官方出品，比 mydumper 更快的多线程逻辑备份工具-MySQL Shell Dump & Load - iVictor - 博客园](https://www.cnblogs.com/ivictor/p/16326495.html)
	1. MySQL Shell 8.0.21 引入了一个工具-util.dumpInstance ()，可实现行级别的并行备份。这个工具对备份实例和恢复实例的版本有要求：备份实例 >= 5.6，恢复实例 >= 5.7。
==对比==
1. 导出
	1. outfile 只是个命令，outfile 和 MySQL shell dump 导出 csv 格式文件，其他三个是以 `insert` 语句保存备份结果（mysqldump 命令还可以生成 CSV、其他分隔文本或 XML 格式的输出。）
	2. 三个及 shell dump 本质上都是通过 SELECT * FROM TABLE 的方式备份数据，只不过在此基础上，通过全局读锁 + REPEATABLE READ 事务隔离级别，实现了数据库的一致性备份。
2. 恢复
	1. 各个工具对应的恢复工具也不一样。具体来说，mysqldump、mysqlpump 对应的恢复工具是 mysql 客户端，所以是单线程恢复。
	2. mydumper 对应的恢复工具是 myloader，支持多线程恢复。
	3. util.dumpInstance ()对应的恢复工具是 util.loadDump ()，该工具实际调用的是 LOAD DATA LOCAL INFILE 命令，支持多线程恢复。
	4. SELECT ... INTO OUTFILE 对应的恢复命令是 LOAD DATA。

[Mysqldump vs Mysqlpump vs Mydumper](https://mydbops.wordpress.com/2019/03/26/mysqldump%E2%80%8B-vs-mysqlpump-vs-mydumper/)


### mysqlpump 逻辑备份
[MySQL 5.7 新备份工具mysqlpump 使用说明 - 运维小结 - 散尽浮华 - 博客园](https://www.cnblogs.com/kevingrace/p/9760185.html)
[MySQL并行导入导出工具——mysqlpump\_Hehuyi\_In的博客-CSDN博客](https://blog.csdn.net/Hehuyi_In/article/details/102981945)

```bash
 mysqlpump --single-transaction --set-gtid-purged=OFF --parallel-schemas=2:kevin --parallel-schemas=4:dbt3 -B kevin dbt3 -p123456 > /tmp/backup.sql

mysqlpump -uroot -p123456 --set-gtid-purged=off -B adp > /tmp/abp-bak.sql
```

| 参数                 | 含义                               |
| -------------------- | ---------------------------------- |
| --single-transaction |                                    |
| --set-gtid-purged    |                                    |
| --parallel-schemas   | 指定并行备份的库，多个库用逗号分隔 |
|           -B           |   -B, --databases                                 |

### mysqldumper 逻辑备份

chatgpt真是个好东西，google没找到镜像使用方法，chatgpt回答了。
```sh
docker run --name mydumper --rm  -v /data/mydumper:/mydump mydumper/mydumper mydumper -u root -p P@ssw0rd -h 192.168.215.54 -P 3316 -B test -o /mydump
```

会导出4个文件
1. metadata
	1. 记录了备份数据库在备份时间点的二进制日志文件名，日志的写入位置，如果是在从库进行备份，还会记录备份时同步至从库的二进制日志文件及写入位置
2. database-schema-create.sql 数据库创建文件
3. database.table-schema.sql 表结构文件  
4. database.table.sql 表数据文件

[MySQL 工具之mysqldumper介绍 - 简书](https://www.jianshu.com/p/65f33c7eb630)
[GitHub - mydumper/mydumper: Official MyDumper project](https://github.com/mydumper/mydumper)
[MySQL mydumper使用方法详解\_富士康质检员张全蛋的博客-CSDN博客](https://blog.csdn.net/qq_34556414/article/details/106884371)

### XtraBackup 物理备份
==mysql底层文件类型==
InnoDB 的存储文件有两个，后缀名分别是.frm和.idb，[其中.frm是表的定义文件，而.idb是数据文件](https://blog.csdn.net/ThreeAspects/article/details/108913587)

[定时任务+xtrabackup镜像](https://github.com/fanxcv/xtrabackup-docker) 主要看看shell定时任务，官方有镜像。

[Run Percona XtraBackup in a Docker container - Percona XtraBackup](https://docs.percona.com/percona-xtrabackup/8.0/installation/docker.html)

[Mysql的备份神器xtrabackup](https://blog.51cto.com/u_15230485/4371058)


## reference
["三高"Mysql - Mysql备份概览 - 掘金](https://juejin.cn/post/7083744759428317192)
