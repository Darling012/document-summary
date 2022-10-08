# Mysql日志
## redo log 
是InnoDB引擎层的日志，用来记录事务操作引起数据的变化，记录的是数据页的物理修改。
事务日志，Innodb 提供，固定大小。分四块，环形结构。
1. write position ：当前记录位置
2. checkpoint：当前要擦除的位置，擦除记录前要把记录更新到数据文件。 什么数据文件？
## binlog
逻辑日志，简单理解为记录的sql语句，通过追加的方式写入。
#### 使用场景
1. 主从复制
2. 数据恢复
##### 三种格式
1. statement 记录sql原文 update T set update_time=now() where id=1，但now()是获取当前系统时间，就导致主从不一致
2. row 记录sql + 具体数据  占用空间大
3. Mixed 混合，有可能数据不一致就row，没有就statemnet
### redo log 和bin log  差异
1. redolog是物理日志，只记录结果，在某页修改了了啥
2. Binlog 是逻辑日志，记录的是逻辑性操作，比如给 ID=1 这一行的 c 字段加 1 
3. redolog在事务开始后不断写入，binlog只在提交事务后才写入
### uodolog
逻辑日志
##### Reference
1. [MySQL三大日志(binlog、redo log和undo log)详解](https://github.com/Snailclimb/JavaGuide/blob/main/docs/database/mysql/mysql-logs.md)
2. [谈谈传说中的redo log是什么？有啥用？](https://www.cnblogs.com/ZhuChangwu/p/14096575.html)