# 主从同步
redis 支持主从同步和从从同步。
##### 增量同步
1. 同步的是指令流，主节点将修改性指令记录到本地的内存 buffer 中，是个**定长环形数组**repl_baklog。
2. 主节点异步将 buffer 中指令同步到从节点
3. 从节点一边执行同步过来的指令一边向主节点反馈同步到哪里的偏移量
##### 快照同步
1. 主库将所有内存数据快照到磁盘 [RDB](redis持久化.md#RDB)，然后传送给从节点
2. 从节点接受完快照文件后清空内存数据，全量加载
3. 从节点加载完成后再进行增量同步
4. 快照同步的过程中主节点的复制 buffer 又被写满，再次发起快照同步，死循环。

无盘复制：主节点不生成磁盘文件，直接将快照通过 socket 发送到从节点。
wait 指令：redis 的复制是异步的，wait 指令将异步变同步。

##### reference
1.  [Redis持久化方式RDB和AOF以及Redis集群介绍_Penda xx的博客-CSDN博客_redis集群aof](https://blog.csdn.net/Doreamonx/article/details/125642133)
2. 