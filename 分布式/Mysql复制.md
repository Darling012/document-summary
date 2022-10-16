# Mysql复制
依赖 [binlog](mysql锁.md#binlog) 逻辑日志
[三种复制模式](三高mysql.md#复制有哪些类型)
1. 异步
2. 半同步
	1. 有损 slave 丢失最后一批提交数据
	2. 无损 5.7 开始解决
3. 组复制
减少主从复制故障引进
4. GTID 复制增强模式（5.6 之后新增）

##### 演进
1. 在2000年，MySQL 3.23.15版本引入了Replication
2. 在2002年，MySQL 4.0.2版本将Slave端event读取和执行独立成两个线程(IO线程和SQL线程)，同时引入了relay log。
3. 在 2010 年 MySQL 5.5 版本之前，一直采用的是这种异步复制的方式。主库事务执行不会管备库的同步进度，如果备库落后，主库崩溃，则会丢失数据。
4. 在 5.5 中以插件形式引入了半同步复制，主库在应答客户端提交的事务前需要至少保证一个从库接收并写入 relay log。
5. 在 2016 年，MySQL 在 5.7.17 中引入了一个全新的技术，称之为 InnoDB Group Replication。

## binlog
![mysql日志](mysql日志.md#binlog)
## 异步复制
##### 步骤[主从复制配置实战](三高mysql.md#主从复制配置实战)
1. 主库将数据更改记录到 binlog 中，通知 dump 线程发送新 binlog
2. 备库将主库的 binlog 复制到 relaylog 中
3. 备库读取 relaylog 回放脚本
## 半同步复制

## 组复制

## GTID 增强复制模式
1. 以往是 logfile+ log_pos 确认同步 binlog 的写入位置
2. GTID 就是 server_uuid: gno 组成的键值对
	1. server_uuid: 节点的 uuid
	2. Gno: 事务流水号，回滚之后进行回收
##### reference
1. [binlog的GTID模式基础及基于GITD的数据恢复](https://www.css3er.com/p/260.html)
2. [一文看懂MySQL的异步复制、全同步复制与半同步复制-51CTO.COM](https://www.51cto.com/article/606556.html)


## 主备延迟如何处理
##### 为什么存在主备延迟
1. relaylog 重放耗时大
2. 备库性能一般比主库差
3. 备库往往承担数据分析工作，对性能要求更高
4. 主库长事务未提交
##### 解决
5.6 以后并行复制
##### 原理
主从同步的时候，从库在获取主库 binlog 并保存为 relaylog 后，把重放 relaylog 的任务另外分配给叫做 worker 的线程执行。sql 线程则执行分配 relaylog 的任务。
##### 类型
1. 按表
2. 按行
3. 按事务
