# HA
#### 概念

**高可用性**（英语：high availability，缩写为 HA）。
$$
A= MTBF/(MTBF+MTTR)
$$
A（可用性），MTBF(平均故障间隔)，MDT(平均修复时间)，MTTF （Mean Time To Failure，平均无故障时间）


[探活与数据同步](探活与数据同步.md)

1. [keepalived高可用](keepalived高可用.md)
优点：简单，基本不需要业务层面做任何事情，就可以实现高可用，主备容灾。而且容灾的宕机时间也比较短。
缺点：也是简单，因为VRRP、主备切换都没有什么复杂的逻辑，所以无法应对某些特殊场景，比如主备通信链路出问题，会导致脑裂。同时，keepalived也不容易做负载均衡。

2. zookeeper
优点：可以支持高可用，负载均衡。本身是个分布式的服务。
缺点：跟业务结合的比较紧密。需要在业务代码中写好ZK使用的逻辑，比如注册名字。拉取名字对应的服务地址等。

##### reference
1. [keepalived和zookeeper对比](https://blog.csdn.net/vtopqx/article/details/79066703)



[redis集群](redis集群.md)
[mysql分布式](mysql分布式.md)


[异地多活](https://mp.weixin.qq.com/s/T6mMDdtTfBuIiEowCpqu6Q)