# mysql 高可用
##### 名词
1. 故障切换：手动
2. 故障转移：自动；即当活动的服务或应用意外终止时，快速启用**冗余**或备用的服务器、系统、硬件或者网络接替它们工作。
3. 故障恢复：是在计划内或计划外中断解决后**切换回主站点**的过程
4. 容灾与备份：系统高可用技术的组成部分。
	1. 容灾：为了在遭遇灾害时系统正常运行
	2. [备份](mysql高可用.md#备份)：为了应对遭灾时的数据丢失问题


## MySQL备份
![mysql备份](mysql备份.md#MySQL%20备份)

## Mysql 复制
![Mysql复制](Mysql复制.md#Mysql复制)


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
6. [Fetching Title#4gt6](https://www.cnblogs.com/kevingrace/p/10470226.html)
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




## AI 项目
1. service - > mycat -> mha mysql 集群
	1. mha 可故障切换；可通知 mycat
	2. 或者 mycat 探活
	3. mycat 单点问题怎么解决？
	   1. 如何负载：haproxy
	   2. 数据同步：zk
	4. [SpringBoot +MySQL+mycat 实现读写分离+故障转移_islibin6666的博客-CSDN博客](https://blog.csdn.net/weixin_38938840/article/details/103474507)
2. 自写 agent 探活方案：
	1. 代码侵入，需改动已有代码。
	2. service -> 自写 agent+dynamicDataSource -> mysql (主从复制) 
    3.  [动手实现MySQL读写分离and故障转移 - 掘金](https://juejin.cn/post/6844904013272449037#heading-24)












