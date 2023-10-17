1. [一个考虑转行的程序猿的博客_MysqL_51CTO博客](https://blog.51cto.com/u_15230485/category20)
2. [mysql专栏 - 懒时小窝的专栏 - 掘金](https://juejin.cn/column/7001840919171301390)

# 第 1 章 课程介绍
#### 应该怎么学 mysql
1. 从理论到实战[01:36](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC1%E7%AB%A0%20%E8%AF%BE%E7%A8%8B%E4%BB%8B%E7%BB%8D/1-1%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4#t=96.366834)
	1. 从数据表的逻辑结构，到优化数据表的性能
	2. 从数据查询原理，到改善慢 sql 性能
	3. 从事务原理，到优化事务执行效率
2. 从单点到集群[01:57](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC1%E7%AB%A0%20%E8%AF%BE%E7%A8%8B%E4%BB%8B%E7%BB%8D/1-1%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4#t=117.471486)
3. 从现在到未来[02:21](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC1%E7%AB%A0%20%E8%AF%BE%E7%A8%8B%E4%BB%8B%E7%BB%8D/1-1%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4#t=141.332745)
	1. 5.0 到 8.0
	2. 单体到分布式
	3. **技术原理到技术趋势**
#### 什么是三高 [03:02](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC1%E7%AB%A0%20%E8%AF%BE%E7%A8%8B%E4%BB%8B%E7%BB%8D/1-1%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4#t=182.837093)

高并发：同时处理事务数高
高性能： 事务、sql 的执行效率高
高可用：系统可用时间高

#### 为什么不直接讲三高 
三高只是目的，不是手段，手段有：
1. 复制[03:53](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC1%E7%AB%A0%20%E8%AF%BE%E7%A8%8B%E4%BB%8B%E7%BB%8D/1-1%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4#t=233.188006)
	1. 目的：数据冗余
	2. 手段：binlog 传送
	3. 收获：并发量提升、可用性提升
	4. 问题：占用更多的硬件资源
2. 扩展
	1. 目的：扩展数据库容量，性能、数据
	2. 手段：数据分片分库、分表
	3. 收获：性能、并发量的提升
	4. 问题：可能降低可用性
3. 切换[05:08](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC1%E7%AB%A0%20%E8%AF%BE%E7%A8%8B%E4%BB%8B%E7%BB%8D/1-1%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4#t=308.074204)
	1. 目的：提高可用性
	2. 手段：主从身份切换
	3. 收获：并发量的提升
	4. 问题：丢失切换时期的数据

#### 三高的实现[05:44](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC1%E7%AB%A0%20%E8%AF%BE%E7%A8%8B%E4%BB%8B%E7%BB%8D/1-1%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4#t=344.626744)
高并发：通过复制和扩展，将数据分散至多节点
高性能：复制提升速度，扩展提升容量
高可用：节点间身份切换保证随时可用

##### 如何提升单点的性能 [06:45](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC1%E7%AB%A0%20%E8%AF%BE%E7%A8%8B%E4%BB%8B%E7%BB%8D/1-1%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4#t=405.577444)
建表、查询、更新三方面

##### 未来数据库发展趋势 [08:26](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC1%E7%AB%A0%20%E8%AF%BE%E7%A8%8B%E4%BB%8B%E7%BB%8D/1-1%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4#t=506.134459)
# 第 2 章 环境搭建
# 第 3 章 一个 SQL 语句如何执行
本章介绍MySQL的各个组成部分与架构设计思路，演示MySQL的网络连接方法及图解一次更新SQL的执行过程等，建立MySQL清晰的架构体系，深入理解MySQL Server层+存储引擎层的架构体系，并理解存储引擎插件化的好处。
["三高"Mysql - Mysql的基础结构了解 - 掘金](https://juejin.cn/post/7076457248888717348)

## 3-1 为什么需要数据库
#### 有了电子表格，为什么还要数据库 
[01:47](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC3%E7%AB%A0%20%E4%B8%80%E4%B8%AASQL%E8%AF%AD%E5%8F%A5%E5%A6%82%E4%BD%95%E6%89%A7%E8%A1%8C/3-1%20%E4%B8%BA%E4%BB%80%E4%B9%88%E9%9C%80%E8%A6%81%E6%95%B0%E6%8D%AE%E5%BA%93.mp4#t=107.867163)

## 3-2 数据库软件的典型架构是怎样的
#### 软件架构
[01:28](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC3%E7%AB%A0%20%E4%B8%80%E4%B8%AASQL%E8%AF%AD%E5%8F%A5%E5%A6%82%E4%BD%95%E6%89%A7%E8%A1%8C/3-2%20%E6%95%B0%E6%8D%AE%E5%BA%93%E8%BD%AF%E4%BB%B6%E7%9A%84%E5%85%B8%E5%9E%8B%E6%9E%B6%E6%9E%84%E6%98%AF%E6%80%8E%E6%A0%B7%E7%9A%84.mp4#t=88.458218)
1. 分层架构 如 mvc、微服务、k8s
2. 事件驱动架构 如 mq
3. 管道-过滤器架构，如责任链[06:43](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC3%E7%AB%A0%20%E4%B8%80%E4%B8%AASQL%E8%AF%AD%E5%8F%A5%E5%A6%82%E4%BD%95%E6%89%A7%E8%A1%8C/3-2%20%E6%95%B0%E6%8D%AE%E5%BA%93%E8%BD%AF%E4%BB%B6%E7%9A%84%E5%85%B8%E5%9E%8B%E6%9E%B6%E6%9E%84%E6%98%AF%E6%80%8E%E6%A0%B7%E7%9A%84.mp4#t=403.14814)
4. 微核架构，靠插件，如 vscode，obsidian[08:56](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC3%E7%AB%A0%20%E4%B8%80%E4%B8%AASQL%E8%AF%AD%E5%8F%A5%E5%A6%82%E4%BD%95%E6%89%A7%E8%A1%8C/3-2%20%E6%95%B0%E6%8D%AE%E5%BA%93%E8%BD%AF%E4%BB%B6%E7%9A%84%E5%85%B8%E5%9E%8B%E6%9E%B6%E6%9E%84%E6%98%AF%E6%80%8E%E6%A0%B7%E7%9A%84.mp4#t=536.449973)

## 3-3 MySQL软件架构是怎样的
[01:34](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC3%E7%AB%A0%20%E4%B8%80%E4%B8%AASQL%E8%AF%AD%E5%8F%A5%E5%A6%82%E4%BD%95%E6%89%A7%E8%A1%8C/3-3%20MySQL%E8%BD%AF%E4%BB%B6%E6%9E%B6%E6%9E%84%E6%98%AF%E6%80%8E%E6%A0%B7%E7%9A%84.mp4#t=94.961246)
客户端 (jdbc，navicat) -> 连接器 -> 缓存（8.0 去掉，不推荐用)-> 分析器-> 优化 -> 执行 -> 引擎 -> 文件系统
#### 用了哪些架构
[05:25](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC3%E7%AB%A0%20%E4%B8%80%E4%B8%AASQL%E8%AF%AD%E5%8F%A5%E5%A6%82%E4%BD%95%E6%89%A7%E8%A1%8C/3-3%20MySQL%E8%BD%AF%E4%BB%B6%E6%9E%B6%E6%9E%84%E6%98%AF%E6%80%8E%E6%A0%B7%E7%9A%84.mp4#t=325.668136)

## 3-4 客户端怎样连接 MySQL 数据库（上)
#### 四种连接方式
[00:45](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC3%E7%AB%A0%20%E4%B8%80%E4%B8%AASQL%E8%AF%AD%E5%8F%A5%E5%A6%82%E4%BD%95%E6%89%A7%E8%A1%8C/3-4%20%E5%AE%A2%E6%88%B7%E7%AB%AF%E6%80%8E%E6%A0%B7%E8%BF%9E%E6%8E%A5MySQL%E6%95%B0%E6%8D%AE%E5%BA%93%EF%BC%88%E4%B8%8A%EF%BC%89.mp4#t=45.608864)
1. Tcp/ip 连接
2. 命名管道
3. 共享内存
4. unix 域套接字

## 3-5 客户端怎样连接 MySQL 数据库（下)
#### 后三种连接方式介绍

## 3-6 一个 SQL 语句是怎样执行的
[00:36](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC3%E7%AB%A0%20%E4%B8%80%E4%B8%AASQL%E8%AF%AD%E5%8F%A5%E5%A6%82%E4%BD%95%E6%89%A7%E8%A1%8C/3-6%20%E4%B8%80%E4%B8%AASQL%E8%AF%AD%E5%8F%A5%E6%98%AF%E6%80%8E%E6%A0%B7%E6%89%A7%E8%A1%8C%E7%9A%84.mp4#t=36.948748)

# 第 4 章 如何建表更符合业务
本章详细学习InnoDB的数据表底层原理，详细阐述B+树与逻辑存储结构，掌握如何建立高效索引、如何优化数据表结构。
[三高Mysql - Inndb存储引擎和索引介绍 - 掘金](https://juejin.cn/post/7076809925720014862)

## 4-1 什么叫索引组织表
#### 索引组织表 index organized table
[01:27](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-1%20%E4%BB%80%E4%B9%88%E5%8F%AB%E7%B4%A2%E5%BC%95%E7%BB%84%E7%BB%87%E8%A1%A8.mp4#t=87.270362)
1. 由索引组织起来的表
2. 在 innoDB 中，表都是根据主键顺序组织存放的
#### 索引
[02:39](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-1%20%E4%BB%80%E4%B9%88%E5%8F%AB%E7%B4%A2%E5%BC%95%E7%BB%84%E7%BB%87%E8%A1%A8.mp4#t=159.114843)
1. 索引是数据库中 **对某一列或几列** 的 **值** 进行预排序的 数据结构
2. 可以理解为数据的**目录**
3. innodb 中，主键是一个特殊的索引字段

#### 主键
[03:22](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-1%20%E4%BB%80%E4%B9%88%E5%8F%AB%E7%B4%A2%E5%BC%95%E7%BB%84%E7%BB%87%E8%A1%A8.mp4#t=202.024814)
1. 在 innodb 中，每个表都有一个主键
2. 未指定，若表中有一个非空唯一索引，则为主键
3. 若多个非空唯一索引，选择第一个定义的
4. 若没有非空唯一索引，则隐式自动创建一个 6 字节的指针作为主键

#### 演示哪个是唯一索引
[06:11](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-1%20%E4%BB%80%E4%B9%88%E5%8F%AB%E7%B4%A2%E5%BC%95%E7%BB%84%E7%BB%87%E8%A1%A8.mp4#t=371.33531)

## 4-2 B+树的B是什么意思-1

#### 主流索引查找算法
[00:56](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-2%20B+%E6%A0%91%E7%9A%84B%E6%98%AF%E4%BB%80%E4%B9%88%E6%84%8F%E6%80%9D-1.mp4#t=56.320727)
线性查找 `O(n)` ，a->b->c，一个一个往后找，数据结构是链表或者数组
二分查找 `O（logn）` , 必须要知道终点在哪，磁盘有碎片，数据结构是链表或者数组
二叉树查找 O (logn)，有可能退化成链表线性查找
平衡二叉树（不退化为线性查找，每个节点都是一个数据，磁盘利用率低） -> Btree（也有平衡问题） -> B+ tree（Btree+线性查找）
##### 原理
[03:24](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-2%20B+%E6%A0%91%E7%9A%84B%E6%98%AF%E4%BB%80%E4%B9%88%E6%84%8F%E6%80%9D-1.mp4#t=204.622656)
数据可视化网站： `http://cs.usfca.edu/~galles/visualization/algorithms.html`
###### 线性查找
[03:46](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-2%20B+%E6%A0%91%E7%9A%84B%E6%98%AF%E4%BB%80%E4%B9%88%E6%84%8F%E6%80%9D-1.mp4#t=226.133304)
第一个数据开始逐个匹配，数据结构是链表或者数组，
###### 二分查找
[05:34](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-2%20B+%E6%A0%91%E7%9A%84B%E6%98%AF%E4%BB%80%E4%B9%88%E6%84%8F%E6%80%9D-1.mp4#t=334.451826)
先找头尾，比对中点位置
假如都是同样的值，这种场景该用什么数据结构，什么查找算法？
###### 二叉树查找
[08:22](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-2%20B+%E6%A0%91%E7%9A%84B%E6%98%AF%E4%BB%80%E4%B9%88%E6%84%8F%E6%80%9D-1.mp4#t=502.796168)
左节点比中节点数值小，右节点比中节点数值大
从根节点开始搜索，大则找右子树。
一直插入递增的数值，则退化为链表，线性查找
###### 平衡二叉树 AVL 
通过左旋或者右旋来 平衡  二叉树 ，不会退化成链表
1. 插入、删除、改都会左旋、右旋保证平衡
2. 查找与二叉树相同
3. **一个节点只有一条数据，数据量太低，硬盘每一次写都有最小值**
## 4-3 B+树的 B 是什么意思-2
###### B tree
[03:18](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-3%20B+%E6%A0%91%E7%9A%84B%E6%98%AF%E4%BB%80%E4%B9%88%E6%84%8F%E6%80%9D-2.mp4#t=198.235892)
1. 改善 AVL 单个节点数据量太低，单个节点多条数据，多个（指针 + 数据）， 多叉树， 单个节点为线性查找
2. 是线性数据结构（单个节点）和树的结合
3. 不需要旋转就能保证树的平衡，降低了树的高度
4. 范围查找跨节点情况，[08:43](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-3%20B+%E6%A0%91%E7%9A%84B%E6%98%AF%E4%BB%80%E4%B9%88%E6%84%8F%E6%80%9D-2.mp4#t=523.221212)，效率低，每个元素都要从根节点开始查找

###### B+ tree
[09:01](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-3%20B+%E6%A0%91%E7%9A%84B%E6%98%AF%E4%BB%80%E4%B9%88%E6%84%8F%E6%80%9D-2.mp4#t=541.885122)
1. 解决范围查找，找到第一个节点后就可以接着往后找（基于 3）
2. 叶子节点存放数据，上边枝干只有指针，作为索引
3. 叶子节点之间加指针成为链表
4. 由 Btree 发展而来，所有数据放在叶子节点形成一个线性表

##### 总结
[13:28](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-3%20B+%E6%A0%91%E7%9A%84B%E6%98%AF%E4%BB%80%E4%B9%88%E6%84%8F%E6%80%9D-2.mp4#t=808.090957)
B+ tree 是目前最主流的数据库索引算法
B+tree 由线性表、二叉树、B tree 发展而来，集成了线性表、AVL 的优势

## 4-4 为什么说 InnoDB 索引即数据
##### B+ tree 索引
[00:24](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-4%20%E4%B8%BA%E4%BB%80%E4%B9%88%E8%AF%B4InnoDB%E7%B4%A2%E5%BC%95%E5%8D%B3%E6%95%B0%E6%8D%AE.mp4#t=24.625129)
1. Innodb 使用 B+tree 作为索引的数据结构
2. B+ tree 高度一般为 2-4 层
3. innodb 索引分为聚簇索引（主索引）和辅助索引（另外加的提升性能）

##### 聚簇索引
[02:24](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-4%20%E4%B8%BA%E4%BB%80%E4%B9%88%E8%AF%B4InnoDB%E7%B4%A2%E5%BC%95%E5%8D%B3%E6%95%B0%E6%8D%AE.mp4#t=144.862615)
1. Innodb 把数据放在主索引里，所以叫 聚簇索引
2. 根据表的 [主键](三高mysql.md#主键) 构造一个 b+ tree
3. 叶子节点存放行数据，而不是指针

###### innodb 里 b+ tree 的实现
 [04:25](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-4%20%E4%B8%BA%E4%BB%80%E4%B9%88%E8%AF%B4InnoDB%E7%B4%A2%E5%BC%95%E5%8D%B3%E6%95%B0%E6%8D%AE.mp4#t=265.63923)
1. 单个节点有 上限指针、下限指针、页号。枝干节点存数据存 主键范围（主键大于 x 的 在 page 4），叶子节点存原始数据（主键 id、其他数据）

##### 辅助索引
[12:45](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-4%20%E4%B8%BA%E4%BB%80%E4%B9%88%E8%AF%B4InnoDB%E7%B4%A2%E5%BC%95%E5%8D%B3%E6%95%B0%E6%8D%AE.mp4#t=765.275397)
1. 也是一个 B+ tree[15:21](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-4%20%E4%B8%BA%E4%BB%80%E4%B9%88%E8%AF%B4InnoDB%E7%B4%A2%E5%BC%95%E5%8D%B3%E6%95%B0%E6%8D%AE.mp4#t=921.083446)
2. 叶子节点不包含 行数据
3. 叶子节点记录了 行数据的主键，枝干节点存放的是 作为索引的具体数据原始值 (大于值为 y 的主键在 page 4)，叶子节点存放的是（值 y, 主键 ID）
[14:11](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-4%20%E4%B8%BA%E4%BB%80%E4%B9%88%E8%AF%B4InnoDB%E7%B4%A2%E5%BC%95%E5%8D%B3%E6%95%B0%E6%8D%AE.mp4#t=851.951161)

##### 总结
[16:47](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-4%20%E4%B8%BA%E4%BB%80%E4%B9%88%E8%AF%B4InnoDB%E7%B4%A2%E5%BC%95%E5%8D%B3%E6%95%B0%E6%8D%AE.mp4#t=1007.04995)
1. 分为聚簇索引和辅助索引
2. 同层 b+ tree 之间，为双向链表
3. 在 b+ tree 节点之内，数据条目之间为单向链表

## 4-5 InnoDB 数据表是如何存储的
##### Innodb 逻辑存储结构
[00:52](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-5%20InnoDB%E6%95%B0%E6%8D%AE%E8%A1%A8%E6%98%AF%E5%A6%82%E4%BD%95%E5%AD%98%E5%82%A8%E7%9A%84.mp4#t=52.028668)
1. 表空间 tablespace idb 文件
	1. 段 segment
		1. 区 extent
			1. 页 page (上限指针、下限指针、row)
				1. 行 row (事务 ID，回滚指针，Col1... n)
					1. Col  真正的行数据
						1. 

###### 表空间
[02:52](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-5%20InnoDB%E6%95%B0%E6%8D%AE%E8%A1%A8%E6%98%AF%E5%A6%82%E4%BD%95%E5%AD%98%E5%82%A8%E7%9A%84.mp4#t=172.436422)
1. 表空间指的是数据表在硬盘上的存储空间
2. 默认所有表的数据都存在共享表空间
3. 每个表也可以独占表空间（ibd 文件），奔溃救援恢复比较方便

###### 段
[04:18](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-5%20InnoDB%E6%95%B0%E6%8D%AE%E8%A1%A8%E6%98%AF%E5%A6%82%E4%BD%95%E5%AD%98%E5%82%A8%E7%9A%84.mp4#t=258.22106)
1. 数据段：B+ tree 存放叶子节点的段
2. 索引段：B+ tree 非叶子节点
3. innodb，段自动管理

###### 区
[05:35](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-5%20InnoDB%E6%95%B0%E6%8D%AE%E8%A1%A8%E6%98%AF%E5%A6%82%E4%BD%95%E5%AD%98%E5%82%A8%E7%9A%84.mp4#t=335.081841)
1. 64 个 page, 每个页默认 16kb，`16*64=1mb`
2. 区由连续页组成，默认大小为 1mb
3. 一次从磁盘申请 4-5 个区

###### 页
[06:52](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-5%20InnoDB%E6%95%B0%E6%8D%AE%E8%A1%A8%E6%98%AF%E5%A6%82%E4%BD%95%E5%AD%98%E5%82%A8%E7%9A%84.mp4#t=412.760723)
1. B+tree 的一个节点
2. innodb 磁盘读写的最小逻辑单位，默认 16kb ^svn8s3
3. 页的大小充分考虑硬盘的最小单元[08:23](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-5%20InnoDB%E6%95%B0%E6%8D%AE%E8%A1%A8%E6%98%AF%E5%A6%82%E4%BD%95%E5%AD%98%E5%82%A8%E7%9A%84.mp4#t=503.984502)
4. 页怎么组成的 B+tree [08:41](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-5%20InnoDB%E6%95%B0%E6%8D%AE%E8%A1%A8%E6%98%AF%E5%A6%82%E4%BD%95%E5%AD%98%E5%82%A8%E7%9A%84.mp4#t=521.802447)

##### 总结
[10:47](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC4%E7%AB%A0%20%E5%A6%82%E4%BD%95%E5%BB%BA%E8%A1%A8%E6%9B%B4%E7%AC%A6%E5%90%88%E4%B8%9A%E5%8A%A1/4-5%20InnoDB%E6%95%B0%E6%8D%AE%E8%A1%A8%E6%98%AF%E5%A6%82%E4%BD%95%E5%AD%98%E5%82%A8%E7%9A%84.mp4#t=647.195001)
1. innodb 的页就是 b+ 的一个节点
2. innodb 的页是自身逻辑概念，与硬件无关

##### 思考
1. 为什么页不能太大或太小


# 第 5 章 怎么查询速度更快
本章讲解MySQL排序、随机、联合索引等原理。在丰富的实战场景下学习分析慢SQL语句的思路，并学习如何多角度优化慢SQL性能。
[三高Mysql - Mysql索引和查询优化讲解（偏理论部分） - 掘金](https://juejin.cn/post/7083080770478145543)
[三高Mysql - Mysql索引和查询优化（偏实战部分） - 掘金](https://juejin.cn/post/7083355154744868900)
# 第 6 章 如何处理数据更新
本章讲解InnoDB日志、锁和事务的原理，并讲透MVCC原理、间隙锁原理。并学习如何解决事务场景下的慢事务、死锁等问题。
# 第 7 章 ORM 框架原理
本章介绍 ORM 框架原理，了解 ORM 的一般架构和中间层设计思路以及 ORM 框架使用过程中如何规避 SQL 缺陷问题。

# 第 8 章 怎么给数据上保险
本章学习不同的备份种类，mysqldump和ibbackup的备份逻辑与使用方法等，使同学对MySQL备份有全局认识，可以通过所学知识为公司的数据库设计备份恢复方案了。
["三高"Mysql - Mysql备份概览 - 掘金](https://juejin.cn/post/7083744759428317192)
## 8-1 数据库有哪些种类的备份
##### 数据库有哪些种类的备份 
状态、格式、内容
备份方式没有优劣，只是用途不同
1. [备份数据库时状态](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-1%20%E6%95%B0%E6%8D%AE%E5%BA%93%E6%9C%89%E5%93%AA%E4%BA%9B%E7%A7%8D%E7%B1%BB%E7%9A%84%E5%A4%87%E4%BB%BD.mp4#t=160.020202)
	1. 热备：正在运行中
	2. 温备：数据库只读
	3. 冷备：完全停止后备份
2. 备份文件的格式
	1. 逻辑备份：输出文本或 sql 语句
	2. 物理备份（裸文件）：备份数据库底层文件
3. 备份的内容
	1. 完全备份
	2. 增量备份
	3. 日志备份：binlog 备份，增量备份的一种，一般增量备份是逻辑备份，日志备份指的是物理备份增量备份


## 8-2 如何使用 OUTFILE 命令进行备份
##### 如何使用 outfile 命令进行备份
[01:18](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-2%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8OUTFILE%E5%91%BD%E4%BB%A4%E8%BF%9B%E8%A1%8C%E5%A4%87%E4%BB%BD.mp4#t=78.137417)
1. Mysql 原生的 sql 指令
2. 最原始的逻辑备份方式
3. 备份的功能和效果取决于如何写 sql 语句

Outfile 怎么保证一致性？[09:43](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-2%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8OUTFILE%E5%91%BD%E4%BB%A4%E8%BF%9B%E8%A1%8C%E5%A4%87%E4%BB%BD.mp4#t=583.626931)
1. mysql 默认可重复读，mvcc
2. 在备份的时候，开启事务
3. 再逐个备份各个表
缺陷
1. 输出文本简略
2. 很难进行还原，往往用来简单的导出数据
## 8-3 如何使用 mysqldump 进行备份
##### 如何使用mysqldump进行备份
Outfile 如何进行改进？[02:17](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-3%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump%E8%BF%9B%E8%A1%8C%E5%A4%87%E4%BB%BD.mp4#t=137.074213)
1. 自动执行每个表的命令
2. 自动开启事务
3. 输出 insert 语句，可以用于还原

mysqldump[04:12](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-3%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump%E8%BF%9B%E8%A1%8C%E5%A4%87%E4%BB%BD.mp4#t=252.216304)
1. mysql server 自带
2. 输出内容为 sql 语句，平衡了阅读与还原
3. sql 语句占用空间小

原理[06:51](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-3%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump%E8%BF%9B%E8%A1%8C%E5%A4%87%E4%BB%BD.mp4#t=411.383481)
1. 执行时 sql 加 sql_no_cache，不进行缓存
2. --single-transation


[使用](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-3%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump%E8%BF%9B%E8%A1%8C%E5%A4%87%E4%BB%BD.mp4#t=434.290344)

缺点：
1. 导出逻辑数据，备份较慢
2. 还原需要执行 sql，速度也比较慢

## 8-4 如何使用 mysqldump+binlog 增量备份
##### 如何使用[mysqldump](如何使用mysqldump进行备份.md#如何使用mysqldump进行备份)+binlog增量备份
[思路](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-4%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump+binlog%E5%A2%9E%E9%87%8F%E5%A4%87%E4%BB%BD.mp4#t=436.852572)
1. Mysqldump 就是把当前数据库数据 select 出来后导出
2. binlog 记录了 MySQL 数据的变化
3. Binlog 与存储引擎无关
4. Mysqldump 全量备份后，用 Binlog 做增量备份
5. Mysqldump 全量备份时，切换新的 binlog 文件
6. 从零还原时，采用全量还原+binlog 还原
###### 操作
[全量](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-4%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump+binlog%E5%A2%9E%E9%87%8F%E5%A4%87%E4%BB%BD.mp4#t=776.771854)
1. --flush-logs:
2. --master-data=2:
[增量](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-4%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump+binlog%E5%A2%9E%E9%87%8F%E5%A4%87%E4%BB%BD.mp4#t=921.793833)
1. 切换 binlog 文件

[还原](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-4%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump+binlog%E5%A2%9E%E9%87%8F%E5%A4%87%E4%BB%BD.mp4#t=997.979894)



# 第 9 章 搭建“三高”架构的基础
本章讲解MySQL主备复制的原理、配置方法、binlog格式对主备复制的影响等。本章还研究了备库延迟的原因以及备库延迟的改善方法。最后讲解了MySQL最简单的高可用架构：主主架构。
[三高Mysql - 搭建“三高”架构之复制 - 掘金](https://juejin.cn/post/7084243950244446239)
## 9-1 三高架构之路
什么是三高
1. 高并发：同时处理的事务数高
2. 高性能： 事务 sql 的执行速度高
3. 高可用：系统可用时间高

三高是目的不是手段

手段：
1. [复制](三高mysql.md#9-2%20复制有哪些类型)
2. 扩展
3. 切换

[复制](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC9%E7%AB%A0%20%E6%90%AD%E5%BB%BA%E2%80%9C%E4%B8%89%E9%AB%98%E2%80%9D%E6%9E%B6%E6%9E%84%E7%9A%84%E5%9F%BA%E7%A1%80/9-1%20%E4%B8%89%E9%AB%98%E6%9E%B6%E6%9E%84%E4%B9%8B%E8%B7%AF.mp4#t=252.183109)

[扩展](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC9%E7%AB%A0%20%E6%90%AD%E5%BB%BA%E2%80%9C%E4%B8%89%E9%AB%98%E2%80%9D%E6%9E%B6%E6%9E%84%E7%9A%84%E5%9F%BA%E7%A1%80/9-1%20%E4%B8%89%E9%AB%98%E6%9E%B6%E6%9E%84%E4%B9%8B%E8%B7%AF.mp4#t=316.733481)

[切换](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC9%E7%AB%A0%20%E6%90%AD%E5%BB%BA%E2%80%9C%E4%B8%89%E9%AB%98%E2%80%9D%E6%9E%B6%E6%9E%84%E7%9A%84%E5%9F%BA%E7%A1%80/9-1%20%E4%B8%89%E9%AB%98%E6%9E%B6%E6%9E%84%E4%B9%8B%E8%B7%AF.mp4#t=381.023481)

三高的实现
[08:10](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC9%E7%AB%A0%20%E6%90%AD%E5%BB%BA%E2%80%9C%E4%B8%89%E9%AB%98%E2%80%9D%E6%9E%B6%E6%9E%84%E7%9A%84%E5%9F%BA%E7%A1%80/9-1%20%E4%B8%89%E9%AB%98%E6%9E%B6%E6%9E%84%E4%B9%8B%E8%B7%AF.mp4#t=490.209375)

## 9-2 复制有哪些类型
复制的基本原理[02:38](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC9%E7%AB%A0%20%E6%90%AD%E5%BB%BA%E2%80%9C%E4%B8%89%E9%AB%98%E2%80%9D%E6%9E%B6%E6%9E%84%E7%9A%84%E5%9F%BA%E7%A1%80/9-2%E5%A4%8D%E5%88%B6%E6%9C%89%E5%93%AA%E4%BA%9B%E7%B1%BB%E5%9E%8B.mp4#t=158.295064)
1. 数据库打开 binlog 后数据修改就会有 binlog 产生
2. 主库 dump_thread 会将 binLog 发送
3. 从库的 io_thread 接收 binlog，保存为 relay log
4. 从库 sql_thread 读取、重放 relaylog。
5. 从库会再产生 binlog

##### 复制的类型
根据复制同步的类型
1. 异步复制[07:36](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC9%E7%AB%A0%20%E6%90%AD%E5%BB%BA%E2%80%9C%E4%B8%89%E9%AB%98%E2%80%9D%E6%9E%B6%E6%9E%84%E7%9A%84%E5%9F%BA%E7%A1%80/9-2%E5%A4%8D%E5%88%B6%E6%9C%89%E5%93%AA%E4%BA%9B%E7%B1%BB%E5%9E%8B.mp4#t=456.428155)
	1. 不能保证日志被传送到从库
2. 半同步复制[09:30](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC9%E7%AB%A0%20%E6%90%AD%E5%BB%BA%E2%80%9C%E4%B8%89%E9%AB%98%E2%80%9D%E6%9E%B6%E6%9E%84%E7%9A%84%E5%9F%BA%E7%A1%80/9-2%E5%A4%8D%E5%88%B6%E6%9C%89%E5%93%AA%E4%BA%9B%E7%B1%BB%E5%9E%8B.mp4#t=570.865667)
	1. 主库等待从库变成 relaylog 后主库再 commit
	2. 主库事务是否提交完成取决于是否传送到从库变为 relaylog
	3. 主库可以配置超时时间脱钩，切换为异步复制，默认 10 秒
3. 组复制
	1. 数据库节点不再区分主从
	2. 节点 A 执行前先询问其他节点能否执行
	3. 强一致性 Paxos 算法

## 9-3 主从复制配置实战
命令中 mysqld 是数据库，mysql 是客户端
# 第 10 章 数据库高并发、高性能的基本保证--如何解决数据库超大容量不够问题
本章学习MySQL的可拓展性（容量和性能的拓展），首先介绍分区表的原理、特点和注意事项。然后学习分库分表的原理，重点学习MyCat的升级版：dble的安装与使用。让大家对MySQL分库分表的分类、目的、手段等有清晰的认识。
[三高Mysql - 搭建“三高”架构之扩展与切换 - 掘金](https://juejin.cn/post/7084802980402167816#heading-9)
# 第 11 章 数据库高可用和身份切换的关键--如何解决数据库经常宕机问题
本章学习MySQL实现高可用的意义、原理和实现方案。学习MySQL主备切换的隐患以及处理方法。讲解高并发、高性能、高可用MySQL集群：DRDS集群的原理和架构。
[如何实现Mysql架构中的主从切换，高可用架构的神器MHA！_51CTO博客_mysql主从架构原理](https://blog.51cto.com/u_15230485/4541028)

# 第 12 章 未来的数据库什么样
本章讲解未来新型数据库的发展趋势，主要讲解MySQL8.0新特性、阿里POLARDB、谷歌Spanner数据库、TiDB、阿里OceanBase等NewSQL数据库。
[三高Mysql - Mysql特性和未来发展 - 掘金](https://juejin.cn/post/7084586748742729735)







