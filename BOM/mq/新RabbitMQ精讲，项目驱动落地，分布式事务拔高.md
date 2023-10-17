# RabbitMQ精讲，项目驱动落地，分布式事务拔高
## 第1章 思想纲领
这里，我们来统一我们的愿景，了解一下课程目标、设计思路以及简介。 掌握正确的学习方法，几种学习路线
1-1 为什么做这门课？做课思路是什么样？
1-2 本门课程最好的学习路线是什么？
1-3 怎么最大效率的进行技术交流？

## 第2章 树立清晰的RabbitMQ初印象
从0开始，以生活中的案例来引入。重新认识什么是消息中间件，理解RabbitMQ的灵魂：AMQP协议，以及它的心脏：Exchange的原理。掌握RabbitMQ的快速安装和管理工具。树立对RabbitMQ的初印象。
2-1 从找小jj买咖啡理解消息中间件
2-2 主流消息中间件怎么选？
2-3 RabbitMQ高性能的原因
2-4 RabbitMQ的灵魂——AMQP协议
2-5 RabbitMQ的心脏——Exchange解析
2-6 RabbitMQ快速安装
2-7 视觉直观感受——管理工具概览
2-8 更常用的网页端管理工具
2-9 基本功——命令行管理
2-10 本章总结

## 第3章 利用RabbitMQ基本用法，开发项目
学会基本的命令行操作以后，要继续学习如何在项目中实用。本章将带领大家利用RabbitMQ的几种Exchange，完成项目开发。掌握巩固RabbitMQ基本的客户端使用。并且在讲解过程中，讲授多个开发小技巧
3-1 RabbitMQ消息交换的关键是什么？
3-2 需求分析与架构设计
3-3 数据库设计与项目搭建
3-4 利用Direct开发餐厅和骑手微服务
3-5 设计工程涉及的数据结构
3-6 dao层开发
3-7 队列和交换机绑定
3-8 下单并商家微服务发消息
3-9 收到消息更新订单状态
3-10 给骑手微服务发送消息
3-11 开发商家微服务
3-12 利用Direct接收消息
3-13 完善骑手微服务
3-14 订单向结算服务发送消息
3-15 利用Fanout完善结算微服务
3-16 四个微服务联调
3-17 利用Topic开发积分微服务
3-18 目前的项目不足之处分析
3-19 实际开发中经验及小结

## 第4章 利用RabbitMQ高级特性，完善项目的可靠性
目前的项目只是最基础的把RabbitMQ用起来，要想项目更加健壮，还需要完善消息的可靠性。本章结合RabbitMQ的高级用法，从发送方、消费方和RabbitMQ自身来完善目前的项目，一并讲述更多的高级特性。
### 4-1 如何保证消息可靠性
> file:///Volumes/resources/mq/039- -新RabbitMQ精讲，项目驱动落地，分布式事务拔高/第4章 利用RabbitMQ高级特性，完善项目的可靠性/4-1 如何保证消息可靠性.mp4

##### 发送方
[01:01](file:///Volumes/resources/mq/039-%20-%E6%96%B0RabbitMQ%E7%B2%BE%E8%AE%B2%EF%BC%8C%E9%A1%B9%E7%9B%AE%E9%A9%B1%E5%8A%A8%E8%90%BD%E5%9C%B0%EF%BC%8C%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%8B%94%E9%AB%98/%E7%AC%AC4%E7%AB%A0%20%E5%88%A9%E7%94%A8RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7%EF%BC%8C%E5%AE%8C%E5%96%84%E9%A1%B9%E7%9B%AE%E7%9A%84%E5%8F%AF%E9%9D%A0%E6%80%A7/4-1%20%E5%A6%82%E4%BD%95%E4%BF%9D%E8%AF%81%E6%B6%88%E6%81%AF%E5%8F%AF%E9%9D%A0%E6%80%A7.mp4#t=61.966684)
1. 发送端确认机制，确认消息成功发送到 mq 并处理
2. 消息返回机制，若没有匹配到目标队列，会通知发送方

##### 消费方
[01:18](file:///Volumes/resources/mq/039-%20-%E6%96%B0RabbitMQ%E7%B2%BE%E8%AE%B2%EF%BC%8C%E9%A1%B9%E7%9B%AE%E9%A9%B1%E5%8A%A8%E8%90%BD%E5%9C%B0%EF%BC%8C%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%8B%94%E9%AB%98/%E7%AC%AC4%E7%AB%A0%20%E5%88%A9%E7%94%A8RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7%EF%BC%8C%E5%AE%8C%E5%96%84%E9%A1%B9%E7%9B%AE%E7%9A%84%E5%8F%AF%E9%9D%A0%E6%80%A7/4-1%20%E5%A6%82%E4%BD%95%E4%BF%9D%E8%AF%81%E6%B6%88%E6%81%AF%E5%8F%AF%E9%9D%A0%E6%80%A7.mp4#t=78.531361)
1. 消费端确认机制，确认消息没有发生处理异常
2. 消费端限流机制，限制消息推送速度，保障接收端服务稳定

rabbitmq 自身
[02:10](file:///Volumes/resources/mq/039-%20-%E6%96%B0RabbitMQ%E7%B2%BE%E8%AE%B2%EF%BC%8C%E9%A1%B9%E7%9B%AE%E9%A9%B1%E5%8A%A8%E8%90%BD%E5%9C%B0%EF%BC%8C%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%8B%94%E9%AB%98/%E7%AC%AC4%E7%AB%A0%20%E5%88%A9%E7%94%A8RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7%EF%BC%8C%E5%AE%8C%E5%96%84%E9%A1%B9%E7%9B%AE%E7%9A%84%E5%8F%AF%E9%9D%A0%E6%80%A7/4-1%20%E5%A6%82%E4%BD%95%E4%BF%9D%E8%AF%81%E6%B6%88%E6%81%AF%E5%8F%AF%E9%9D%A0%E6%80%A7.mp4#t=130.285828)
1. 消息过期时间，防止消息大量积压
2. 私信队列，收集过期消息

### 4-2 发送端确认机制原理
> file:///Volumes/resources/mq/039- -新RabbitMQ精讲，项目驱动落地，分布式事务拔高/第4章 利用RabbitMQ高级特性，完善项目的可靠性/4-2 发送端确认机制原理.mp4

##### 三种确认机制
[01:55](file:///Volumes/resources/mq/039-%20-%E6%96%B0RabbitMQ%E7%B2%BE%E8%AE%B2%EF%BC%8C%E9%A1%B9%E7%9B%AE%E9%A9%B1%E5%8A%A8%E8%90%BD%E5%9C%B0%EF%BC%8C%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%8B%94%E9%AB%98/%E7%AC%AC4%E7%AB%A0%20%E5%88%A9%E7%94%A8RabbitMQ%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7%EF%BC%8C%E5%AE%8C%E5%96%84%E9%A1%B9%E7%9B%AE%E7%9A%84%E5%8F%AF%E9%9D%A0%E6%80%A7/4-2%20%E5%8F%91%E9%80%81%E7%AB%AF%E7%A1%AE%E8%AE%A4%E6%9C%BA%E5%88%B6%E5%8E%9F%E7%90%86.mp4#t=115.833149)
1. 单条同步确认机制
2. 多条同步确认机制
3. 异步确认机制

### 4-3 实现多条同步确认和异步确认机制
### 4-4 消息返回机制
### 4-5 消费端确认机制
### 4-6 实现重回队列
### 4-7 消费端限流机制
### 4-8 消息过期机制
### 4-9 死信队列
### 4-10 目前项目的不足之处分析
### 4-11 实际开发中的经验及小结

## 第5章 RabbitMQ与SpringBoot适配，利用工具类简化项目
上述项目是基于SpringBoot直接进行开发的，并没有针对RabbitMQ进行适配，因此出现了一些不足：例如需要手动建立链接、监听消息等。本章就带你解读源码，利用SpringBoot的RabbitMQ的适配工具来解决上述问题。
5-1 引入SpringBoot的重要性
5-2 利用RabbitAdmin快速配置Rabbit服务
5-3 简化配置Rabbit服务的流程
5-4 解析涉及到的RabbitAdmin源码
5-5 利用RabbitTemplate快速消息发送
5-6 改造发送端确认和消息返回
5-7 SimpleMessageListenerContainer
5-8 深入源码探究核心原理
5-9 利用MessageListenerAdapter自定义消息监听
5-10 利用MessageConverter高效处理消息
5-11 利用RabbitListener快速实现消息处理器（上）
5-12 利用RabbitListener快速实现消息处理器（下）
5-13 实际中的开发经验及小结

## 第6章 RabbitMQ集群入门——手动搭建集群并容灾实验
之前的项目还是在单体上，先要集群搭建起来。本章了解RabbitMQ的镜像队列，并借此学习RabbitMQ高可用集群搭建方法，动手搭建高可用集群，并进行容灾实验。学习Shovel与Federation两种集群间通信方式，并动手搭建。
### 6-1 RabbitMQ使用集群的好处
6-2 RabbitMQ集群架构拓扑
6-3 搭建RabbitMQ集群
6-4 RabbitMQ镜像队列
6-5 RabbitMQ怎么实现高可用?
6-6 HAproxy+Keepalived 高可用集群搭建
6-7 RabbitMQ集群间通信原理
6-8 实际开发中的经验和本章小结

## 第7章 RabbitMQ集群高可用
K8s已成为公认的高可用方案，本章学习使用Kubernetes部署RabbitMQ高可用集群。并且分析集群网络分区的危害，讲解判断方法和处理方法，进行RabbitMQ状态监控方法实战。
### 7-1 优化 RabbitMQ 集群



### 7-2 理解Docker架构和原理
### 7-3 搭建Docker环境
### 7-4 利用 DockerCompose 搭建 RabbitMQ 集群
> file:///Volumes/resources/mq/039- -新RabbitMQ精讲，项目驱动落地，分布式事务拔高/第7章 RabbitMQ集群高可用/7-4 利用DockerCompose搭建RabbitMQ集群.mp4

##### 分布式框架理论 CAP
[00:51](file:///Volumes/resources/mq/039-%20-%E6%96%B0RabbitMQ%E7%B2%BE%E8%AE%B2%EF%BC%8C%E9%A1%B9%E7%9B%AE%E9%A9%B1%E5%8A%A8%E8%90%BD%E5%9C%B0%EF%BC%8C%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%8B%94%E9%AB%98/%E7%AC%AC8%E7%AB%A0%20%E5%9F%BA%E4%BA%8ERabbitMQ%E5%BC%80%E5%8F%91%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%A1%86%E6%9E%B6/8-2%20%E5%88%86%E5%B8%83%E5%BC%8F%E6%A1%86%E6%9E%B6%E8%AE%BE%E8%AE%A1.mp4#t=51.858473)
##### base 理论
[02:49](file:///Volumes/resources/mq/039-%20-%E6%96%B0RabbitMQ%E7%B2%BE%E8%AE%B2%EF%BC%8C%E9%A1%B9%E7%9B%AE%E9%A9%B1%E5%8A%A8%E8%90%BD%E5%9C%B0%EF%BC%8C%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%8B%94%E9%AB%98/%E7%AC%AC8%E7%AB%A0%20%E5%9F%BA%E4%BA%8ERabbitMQ%E5%BC%80%E5%8F%91%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%A1%86%E6%9E%B6/8-2%20%E5%88%86%E5%B8%83%E5%BC%8F%E6%A1%86%E6%9E%B6%E8%AE%BE%E8%AE%A1.mp4#t=169.35825)
##### 分布式事务的取舍
[04:58](file:///Volumes/resources/mq/039-%20-%E6%96%B0RabbitMQ%E7%B2%BE%E8%AE%B2%EF%BC%8C%E9%A1%B9%E7%9B%AE%E9%A9%B1%E5%8A%A8%E8%90%BD%E5%9C%B0%EF%BC%8C%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%8B%94%E9%AB%98/%E7%AC%AC8%E7%AB%A0%20%E5%9F%BA%E4%BA%8ERabbitMQ%E5%BC%80%E5%8F%91%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%A1%86%E6%9E%B6/8-2%20%E5%88%86%E5%B8%83%E5%BC%8F%E6%A1%86%E6%9E%B6%E8%AE%BE%E8%AE%A1.mp4#t=298.792503)
##### 分布式事务框架设计
[06:29](file:///Volumes/resources/mq/039-%20-%E6%96%B0RabbitMQ%E7%B2%BE%E8%AE%B2%EF%BC%8C%E9%A1%B9%E7%9B%AE%E9%A9%B1%E5%8A%A8%E8%90%BD%E5%9C%B0%EF%BC%8C%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%8B%94%E9%AB%98/%E7%AC%AC8%E7%AB%A0%20%E5%9F%BA%E4%BA%8ERabbitMQ%E5%BC%80%E5%8F%91%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%A1%86%E6%9E%B6/8-2%20%E5%88%86%E5%B8%83%E5%BC%8F%E6%A1%86%E6%9E%B6%E8%AE%BE%E8%AE%A1.mp4#t=389.394933)
1. 发送失败重试
2. 消费失败重试
3. 死信队列
4. 重试的消息入库[08:26](file:///Volumes/resources/mq/039-%20-%E6%96%B0RabbitMQ%E7%B2%BE%E8%AE%B2%EF%BC%8C%E9%A1%B9%E7%9B%AE%E9%A9%B1%E5%8A%A8%E8%90%BD%E5%9C%B0%EF%BC%8C%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%8B%94%E9%AB%98/%E7%AC%AC8%E7%AB%A0%20%E5%9F%BA%E4%BA%8ERabbitMQ%E5%BC%80%E5%8F%91%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%A1%86%E6%9E%B6/8-2%20%E5%88%86%E5%B8%83%E5%BC%8F%E6%A1%86%E6%9E%B6%E8%AE%BE%E8%AE%A1.mp4#t=506.952573)








### 7-5 理解Kubernetes原理
### 7-6 使用K8s搭建高可用集群
### 7-7 分析集群网络分区的意义与风险
### 7-8 集群网络分区处理方法
### 7-9 RabbitMQ状态监控
### 7-10 目前项目不足之处分析
### 7-11 实际开发过程中的经验及小结

## 第8章 基于RabbitMQ开发分布式事务框架
RabbitMQ集群本身已经健壮，但事务还没有完善，面临分布式特有的问题，本章从单节点事务引出分布式事务概念，带你学会开发SpringBoot插件。并介绍ACID、CAP理论
### 8-1 事务为什么要“分布式化”
> file:///Volumes/resources/mq/039- -新RabbitMQ精讲，项目驱动落地，分布式事务拔高/第8章 基于RabbitMQ开发分布式事务框架/8-1 事务为什么要&ldquo;分布式化&rdquo;.mp4


### 8-2 分布式框架设计
> file:///Volumes/resources/mq/039- -新RabbitMQ精讲，项目驱动落地，分布式事务拔高/第8章 基于RabbitMQ开发分布式事务框架/8-2 分布式框架设计.mp4


### 8-3 分布式事务框架搭建
### 8-4 数据库相关开发
### 8-5 消息发送失败的重试流程分析
### 8-6 实现消息重发功能
### 8-7 消息回调逻辑实现
### 8-8 实现消息定时重发
### 8-9 消息消费失败重试流程分析
### 8-10 改造监听消息代码
### 8-11 死信消息告警
### 8-12 框架打包并引入微服务项目
### 8-13 实际开发中的经验小结
