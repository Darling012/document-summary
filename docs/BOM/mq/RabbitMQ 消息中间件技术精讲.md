# RabbitMQ 消息中间件技术精讲
第1章 课程介绍 试看3 节 | 32分钟
本章首先让大家彻底明白为什么学习RabbitMQ，通过本课程的学习具体收获有哪些？课程内容具体安排与学习建议，然后为大家简单介绍下业界主流消息中间件有哪些，各自适用场景等。（专为没有RabbitMQ基础的同学提供免费入门课程：https://www.imooc.com/learn/1042）...

收起列表
图文：
1-1 课前必读（不看会错过一个亿）
视频：
1-2 课程导学 (16:32)
试看
视频：
1-3 业界主流消息中间件介绍 (14:52)
第2章 低门槛，入门RabbitMQ核心概念 试看16 节 | 161分钟
本章首先为大家讲解互联网大厂为什么选择RabbitMQ? RabbitMQ的高性能之道是如何做到的？什么是AMPQ高级协议？AMPQ核心概念是什么？RabbitMQ整体架构模型是什么样子的？RabbitMQ消息是如何流转的？RabbitMQ安装与使用命令行与管控台，RabbitMQ消息生产与消费，RabbitMQ交换机详解，RabbitMQ队列、绑定、虚拟主机、消息等...

收起列表
视频：
2-1 本章导航 (02:24)
视频：
2-2 哪些互联网大厂在使用RabbitMQ,为什么？ (04:20)
试看
视频：
2-3 RabbitMQ高性能的原因 (02:01)
视频：
2-4 AMQP高级消息队列协议与模型 (03:55)
视频：
2-5 AMQP核心概念讲解 (07:16)
视频：
2-6 RabbitMQ整体架构与消息流转 (03:13)
视频：
2-7 RabbitMQ环境安装-1 (15:44)
视频：
2-8 RabbitMQ环境安装-2 (10:36)
视频：
2-9 命令行与管理台结合讲解 (27:04)
视频：
2-10 生产者消费者模型构建-1 (12:15)
视频：
2-11 生产者消费者模型构建-2 (18:10)
视频：
2-12 交换机详解-1 (16:09)
视频：
2-13 交换机详解-2 (11:36)
视频：
2-14 交换机详解-3 (07:18)
视频：
2-15 绑定、队列、消息、虚拟主机详解 (15:23)
视频：
2-16 本章小结 (03:09)
## 第 3 章 渐进式，深入 RabbitMQ 高级特性
消息如何保障 100% 的投递成功 ？幂等性概念详解，在海量订单产生的业务高峰期，如何避免消息的重复消费问题？Confirm 确认消息、Return 返回消息，自定义消费者，消息的 ACK 与重回队列，消息的限流，TTL 消息，死信队列等
[https://mp.weixin.qq.com/s/iUsjGgpD1HsEU9AkJBw3RQ](https://mp.weixin.qq.com/s/iUsjGgpD1HsEU9AkJBw3RQ)
### 3-1 本章导航 (03:06)
[00:23](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_1-2.mp4#t=23.97234)
本章主要为大家讲解 RabbitMQ 的高级特性和实际场景应用，包括
1. 百分百投递
2. 幂等性
3. 解决重复消费
4. confirm 确认消息，return 返回消息
5. 自定义消费者
6. 消息的 ack 与 重回队列
7. 消息限流
8. TTL 消息
9. 死信队列
### 3-2 消息如何保障 100% 的投递成功方案-1 (15:38) 
[03:14](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_1-2.mp4#t=194.757927)
##### 什么是生产端的可靠性投递？
[04:20](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_1-2.mp4#t=260.998906)
1. 保障消息的成功发出
2. 保障 mq 节点的成功接收
3. 发送端接收到 mq 节点（broker）确认应答
4. 完善的消息进行补偿机制

##### 生产端-可靠性投递
[06:20](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_1-2.mp4#t=380.359874)
###### 解决方案
1. 消息落库，对消息进行状态标记[08:45](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_1-2.mp4#t=525.81746)
	1. 业务消息入库、消息入库
	2. 发送消息
	3. broker 发送 confirm 消息
	4. 更新消息状态
	6. 轮询发送失败的、超时的进行最大努力重发
### 3-3 消息如何保障 100% 的投递成功方案-2 (12:14)
[01:49](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_3.mp4#t=109.800327)
比起 1 来减少 消息入库 操作
2. 消息的延迟投递，做二次确认，回调检查
	1. 业务消息入库后，
	2. 向 mq 发送
		1. 发送消息到业务 broker
		2. 发送延迟投递消息到 callback broker，时间段后 mq 才会收到
	3. 消费者消费消息
	4. 消费者发送 confirm 消息到业务 broker
	5. callback broker 也会监听 confirm 消息
	6. callback broker 收到 confirm 消息后入 callback 消息库
	7. callback broker 收到 延迟投递 的消息后去 callback 消息库检查是否已成功投递
	8. 若没有则 rpc biz 服务 进行重发

###### 追求性能[11:39](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_3.mp4#t=699.909644)

方案 1 是两次入库操作，方案 2 是异步 callback 入库进行补偿

### 3-4 幂等性概念及业界主流解决方案 (14:02)
##### 幂等性概念
[00:17](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_4.mp4#t=17.227442)
多次执行的结果是相同的。
##### 消费端-幂等性保障
在海量订单产生的业务高峰期，如何避免消息的重复消费问题？[04:22](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_4.mp4#t=262.907732)
###### 主流幂等性操作方案
1. 唯一 ID+ 指纹码 (时间戳、业务规则) 机制，利用数据库主键去重
	1. [05:07](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_4.mp4#t=307.674136)
2. 利用 redis原子性
	1. [11:46](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_4.mp4#t=706.854977)
	2. 是否需要进行数据库落库（此处指的是业务数据），需要的话怎么保证双写一致性
	3. 如果不落库，如何设置定时同步策略

### 3-5 Confirm确认消息详解 (19:38)
##### confirm 消息确认机制
[00:29](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_5.mp4#t=29.827239)
1. 生产者发送消息到 broker
2. broker 收到后发送 confirm 消息
3. 生产者异步监听 confirm 消息
###### confirm 确认消息实现
[01:57](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_5.mp4#t=117.522525)

### 3-6 Return返回消息详解 (18:49)
[00:13](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_6.mp4#t=13.796279)
生产端用于处理一些不可路由的消息


### 3-7 自定义消费者使用 (08:50)
视频：
### 3-8 消费端的限流策略-1 (06:55)
##### 什么是消费端的限流
[00:55](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_8-9.mp4#t=55.370295)
服务器上有上万条未处理消息，打开某个客户端后无法同时处理这么多数据。
##### QOS 服务质量保证
[03:00](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_8-9.mp4#t=180.179251)
在非自动确认消息的前提下，如果一定数目的消息未被确认前，不进行消费

### 3-9 消费端的限流策略-2 (08:37)
>file:///Volumes/resources/mq/30.RabbitMQ消息中间件技术精讲/第3章 渐进式，深入RabbitMQ高级特性/3_8-9.mp4

### 3-10 消费端 ACK 与重回队列机制 (15:16)
>file:///Volumes/resources/mq/30.RabbitMQ消息中间件技术精讲/第3章  渐进式，深入 RabbitMQ 高级特性/3_10. mp4

##### 消费端的手工 ACK 和 NACK
[01:21](file:///Volumes/resources/mq/30.RabbitMQ%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E6%8A%80%E6%9C%AF%E7%B2%BE%E8%AE%B2/%E7%AC%AC3%E7%AB%A0%20%E6%B8%90%E8%BF%9B%E5%BC%8F%EF%BC%8C%E6%B7%B1%E5%85%A5RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7/3_10.mp4#t=81.362411)



### 3-11 TTL消息详解 (09:20)
视频：
### 3-12 死信队列详解-1 (06:47)
视频：
### 3-13 死信队列详解-2 (11:35)
视频：
### 3-14 本章小结 (03:14)
## 第4章 手把手，整合RabbitMQ&Spring家族19 节
本章为大家讲解RabbitMQ如何与Spring系的框架体系进行整合（RabbitMQ整合Spring AMQP实战，RabbitMQ整合Spring Boot实战 ，RabbitMQ整合Spring Cloud实战），涉及实际工作中需要注意的细节点，与最佳实战应用，通过本章的学习，学员能够掌握RabbitMQ的实战整合能力，直接应用到具体的工作中！ ...

收起列表
视频：
### 4-1 本章导航 (02:48)
视频：
### 4-2 SpringAMQP用户管理组件-RabbitAdmin应用-1 (15:15)
视频：
### 4-3 SpringAMQP用户管理组件-RabbitAdmin应用-2 (18:23)
视频：
### 4-4 SpringAMQP用户管理组件-RabbitAdmin源码分析 (04:41)
视频：
### 4-5 SpringAMQP-RabbitMQ声明式配置使用 (05:28)
视频：
### 4-6 SpringAMQP消息模板组件-RabbitTemplate实战 (14:55)
视频：
### 4-7 SpringAMQP消息容器-SimpleMessageListenerContainer详解 (15:50)
视频：
### 4-8 SpringAMQP消息适配器-MessageListenerAdapter使用-1 (15:59)
视频：
### 4-9 SpringAMQP消息适配器-MessageListenerAdapter使用-2 (07:23)
视频：
4-10 SpringAMQP消息转换器-MessageConverter讲解-1 (16:17)
视频：
4-11 SpringAMQP消息转换器-MessageConverter讲解-2 (13:26)
视频：
4-12 RabbitMQ与SpringBoot2.0整合实战-基本配置 (07:28)
视频：
4-13 RabbitMQ与SpringBoot2.0整合实战-1 (05:18)
视频：
4-14 RabbitMQ与SpringBoot2.0整合实战-2 (18:18)
视频：
4-15 RabbitMQ与SpringBoot2.0整合实战-3 (17:05)
视频：
4-16 RabbitMQ与SpringBoot2.0整合实战-4 (16:33)
视频：
4-17 RabbitMQ与Spring Cloud Stream整合实战-1 (07:12)
视频：
4-18 RabbitMQ与Spring Cloud Stream整合实战-2 (16:00)
视频：
4-19 本章小结 (02:53)
## 第5章 高可靠，构建RabbitMQ集群架构13 节
本章为大家讲解RabbitMQ集群架构的各种姿势，以及从零到一带大家构建高可靠性的RabbitMQ集群架构（Haproxy + Keepalived），并分享包括对集群的运维、故障恢复方案以及延迟队列插件应用等

收起列表
视频：
5-1 本章导航 (01:38)
视频：
5-2 RabbitMQ集群架构模式-主备模式（Warren） (06:20)
视频：
5-3 RabbitMQ集群架构模式-远程模式（Shovel） (05:30)
视频：
5-4 RabbitMQ集群架构模式-镜像模式（Mirror） (04:01)
视频：
5-5 RabbitMQ集群架构模式-多活模式（Federation） (07:09)
视频：
5-6 RabbitMQ集群镜像队列构建实现可靠性存储 (18:05)
视频：
5-7 RabbitMQ集群整合负载均衡基础组件HaProxy (17:33)
视频：
5-8 RabbitMQ集群整合高可用组件KeepAlived-1 (12:23)
视频：
5-9 RabbitMQ集群整合高可用组件KeepAlived-2 (17:32)
视频：
5-10 RabbitMQ集群配置文件详解 (05:31)
视频：
5-11 RabbitMQ集群恢复与故障转移的5种解决方案 (10:38)
视频：
5-12 RabbitMQ集群延迟队列插件应用 (18:13)
视频：
5-13 本章小结 (02:56)
## 第6章 追前沿，领略SET化架构衍化与设计7 节
本章主要为大家带来一线互联网实现消息中间件多集群的实际落地方案与架构设计思路讲解，涉及目前互联网架构里非常经典的多活，单元化的理念，更有效的提升服务的可靠性与稳定性。

收起列表
视频：
6-1 本章导航 (01:04)
视频：
6-2 BAT、TMD大厂单元化架构设计衍变之路分享 (15:41)
视频：
6-3 SET化架构设计策略(异地多活架构) (17:36)
视频：
6-4 SET化架构设计原则 (03:35)
视频：
6-5 SET化消息中间件架构实现-1 (08:18)
视频：
6-6 SET化消息中间件架构实现-2 (18:58)
视频：
6-7 本章小结 (01:49)
第7章 学大厂，拓展基础组件封装思路7 节 | 56分钟
本章节，我们希望和大家分享互联网大厂的基础组件架构封装思路，其中涉及到消息发送的多模式化、消息的高性能序列化、消息的异步化、连接的缓存容器、消息的可靠性投递、补偿策略、消息的幂等解决方案

收起列表
视频：
7-1 本章导航 (01:53)
视频：
7-2 一线大厂的MQ组件实现思路和架构设计思路 (11:09)
视频：
7-3 基础MQ消息组件设计思路-1（迅速，确认，批量，延迟） (10:52)
视频：
7-4 基础MQ消息组件设计思路-2（顺序） (12:06)
视频：
7-5 基础MQ消息组件设计思路-3(事务) (10:12)
视频：
7-6 消息幂等性保障-消息路由规则架构设计思路 (05:51)
视频：
7-7 本章小结 (02:55)
第8章 课程总结1 节 | 9分钟
本章带大家回顾课程总体的收获，并希望大家都能来课程问答区与老师就学习过程中的问题进行进一步的交流。

收起列表
视频：
8-1 课程总结 (08:25)
第9章 RocketMQ核心技术精讲与高并发抗压实战试听10 节 | 79分钟
本章为大家开通《RocketMQ核心技术精讲与高并发抗压实战》课程的试学内容，初衷是希望能降低大家学习二期的选择风险，让大家少走冤枉路，少花冤枉钱，祝大家学习愉快！

收起列表
视频：
9-1 课程导学 (13:37)
视频：
9-2 本章导航 (01:24)
视频：
9-3 RocketMQ整体认知 (08:01)
视频：
9-4 RocketMQ概念模型 (03:48)
视频：
9-5 RocketMQ源码包编译 (09:31)
视频：
9-6 RocketMQ源码包结构说明 (04:58)
视频：
9-7 RocketMQ环境搭建-1 (15:24)
视频：
9-8 RocketMQ环境搭建-2 (08:30)
视频：
9-9 RocketMQ控制台使用介绍 (11:11)
视频：
9-10 本章小结 (01:52)