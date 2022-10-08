![redis](redis.md#redis基础)
# redis 集群
### 主从同步
redis 支持主从同步和从从同步。
##### 增量同步
1. 同步的是指令流，主节点将修改性指令记录到本地的内存 buffer 中，是个**定长环形数组**。
2. 主节点异步将 buffer 中指令同步到从节点
3. 从节点一边执行同步过来的指令一边向主节点反馈同步到哪里的偏移量
##### 快照同步
1. 主库将所有内存数据快照到磁盘，然后传送给从节点
2. 从节点接受完快照文件后清空内存数据，全量加载
3. 从节点加载完成后再进行增量同步
4. 快照同步的过程中主节点的复制 buffer 又被写满，再次发起快照同步，死循环。

无盘复制：主节点不生成磁盘文件，直接将快照通过 socket 发送到从节点。
wait 指令：redis 的复制是异步的，wait 指令将异步变同步。


### sentinel 哨兵
主节点挂掉后自动进行主从切换。

### cluster 集群
从节点挂掉后

1. [Redis生产环境架构选型方案对比](https://mp.weixin.qq.com/s/z_JCqor-iBuLYxao6OkRsQ)
2. [Redis分片原理](https://mp.weixin.qq.com/s?__biz=MzAwNjkxNzgxNg==&mid=2247489024&idx=2&sn=68c1bcb2d8992278b07861edffcf6f90)
3. [redis单例、主从模式、sentinel以及集群的配置方式及优缺点对比 - charming丶的个人空间 - OSCHINA - 中文开源技术交流社区](https://my.oschina.net/zhangxufeng/blog/905611)
4. [你管这破玩意叫哨兵？](https://mp.weixin.qq.com/s?__biz=Mzk0MjE3NDE0Ng==&mid=2247495300&idx=1&sn=7a7f201d9a09d7b485b23a862869885f)
5. https://github.com/dunwu/db-tutorial
