## mysql

service - > mycat -> mha mysql集群

1. mha可故障切换；可通知mycat
2. 或者mycat探活
3. mycat单点问题怎么解决？
   1. 如何负载：haproxy
   2. 数据同步：zk

自写agent探活方案：

代码侵入，需改动已有代码。

service -> 自写agent+dynamicDataSource -> mysql(主从复制) 











