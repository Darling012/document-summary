# mysql 高可用

##### 名词
1. 故障切换：手动
2. 故障转移：自动；即当活动的服务或应用意外终止时，快速启用**冗余**或备用的服务器、系统、硬件或者网络接替它们工作。
3. 故障恢复：是在计划内或计划外中断解决后**切换回主站点**的过程
4. 容灾与备份：系统高可用技术的组成部分。
	1. 容灾：为了在遭遇灾害时系统正常运行
	2. [备份](mysql高可用.md#备份)：为了应对遭灾时的数据丢失问题

[mysql 的复制、备份与高可用具体关联是什么](https://blog.csdn.net/tcy83/article/details/115453583)
	1. 复制是备份的一种方式，复制基于 binlog, binlog 备份是增量备份的一种。
	2. 高可用的本质是冗余，复制提供了冗余
	3. 高可用设计一般采用集群模式，底层采用数据库复制机制实现数据同步
	4. 高可用方案的 MySQL Replication 就是主从复制，出现故障要手动切换从库。
	5. 备份是保证数据完整性和可靠性，不保证系统可用性
	6.  感觉 mysql 的高可用，就是复制+failover (主主架构没有切换)

## MySQL备份
[MySQL备份](MySQL备份.md#MySQL%20备份)

## Mysql 复制
[MySQL复制](MySQL复制.md#Mysql%20复制)

## 容灾 HA

1. MMM
	1. 谷歌基于 MySQL Replication 基础上扩展优化，不支持新特性，长期没有更新
2. MHA
	1. 自动故障切换，自身不具备故障恢复，某方面算 MMM 进化
3. Galera Cluster
	1. MariaDB Galera Cluster： MariaDB 公司
	2. Percona XtraDB Cluster：Percona，故障自动切换，恢复自动加入集群，无需设置主从，强一致性。
5. MGR：
	1. mysql5.7, 2016 年 12 月
6. MIC：
	1. MySQL 官方在 2017 年 4 月推出了一套完整的、高可用的解决方案，MIC 主要由三部分组成，分别是：MySQL Shell、MySQL Router、MySQL Group Replication (MGR)。
##### Reference
1. [重庆云链数智软件科技有限公司](http://www.bdatacloud.cn/solution/solution_2.html)
2. [【科普】简述常见数据库高可用方案 - 时光与我恰经过 - 博客园](https://www.cnblogs.com/timePasser-leoli/p/12566256.html)
3. [mysql5.7集群方案对比_老狗狐狸的博客-CSDN博客_mysql集群方案对比](https://blog.csdn.net/laogouhuli/article/details/92591386)
4. [mysql pxc集群 原理 （图解+秒懂+史上最全）_架构师-尼恩的博客-CSDN博客_pxc集群](https://blog.csdn.net/crazymakercircle/article/details/120521986)
5. [MySQL MGR技术介绍与集群部署 | 浅时光博客](https://www.dqzboy.com/5098.html)
6. [MySQL+MGR 单主模式和多主模式的集群环境 - 部署手册 (Centos7.5)](https://www.cnblogs.com/kevingrace/p/10470226.html)
7. 

### mysql 数据库架构

1. 主从架构
	1. 传统主从复制、异步复制
	2. 基于 GTID 的主从复制
	3. 主主复制
	4. 级联复制
	5. 多源复制
	6. 延迟复制
	7. 半同步复制
2. 集群架构
	1. MGR (MySQL Group Replication)
	2. PXC (Percona XtraDB Cluster)
	3. MGC (MySQL Galera Cluster)
	4. MNC (MySQL NDB Cluster，有时候也称为 MySQL Cluster)
	5. MySQL + 共享存储方案
	6. MySQL + DRBD 方案
3. 分布式架构
	1. 基于分布式事务的数据库，如 Google Cloud Spanner 和 TiDB
	2. 基于分布式存储的数据库，如极数云舟的 ArkDB、Aurora、PolarDB

中间件
1. KeepAlive、Heartbeat、HAProxy
2. MMM、MHA
3. Orchestrator、Raft
4. 极数云舟的 ArkSentinel
5. Zookeeper、Consul、etcd
##### reference
1. [Fetching Title#03ny](https://www.cnblogs.com/tesla-turing/p/12036839.html)

## reference

[数据库高可用架构设计，看这篇就够了！-架构](http://www.uml.org.cn/zjjs/2021102221.asp)
1. 系统要达到高可用，一定要做好软硬件的冗余，消除单点故障（SPOF single point of failure）
2. 冗余是高可用的基础
3. 还要做好故障转移（Failover）的处理。也就是在最短的时间内发现故障，然后把业务切换到冗余的资源上。

数据复制本质上是数据同步，mysql 复制类型：
1. 异步
2. 半同步
	1. 有损半同步
	2. 无损
3. 多源
4. 延迟

高可用架构方案
1.  

容灾方案
高可用用于处理宕机问题，服务器宕机，机房断网宕机，城市网络宕机，综合考虑，高可用就成了一种容灾处理机制。机房内容灾，同城和跨城容灾。

数据核对
高可用基于复制技术，数据核对解决复制出错情况。


[三高Mysql - 搭建“三高”架构之扩展与切换 - 掘金](https://juejin.cn/post/7084802980402167816)