# sentinel
1. [Spring Cloud Gateway 整合 sentinel 实现流控熔断](https://mp.weixin.qq.com/s/gKpTJqvtwXVKQa0wbLzUEQ)
2. [Sentinel原理与实战-逐码](https://mp.weixin.qq.com/mp/homepage?__biz=MzA3MTQ2MDgyOQ==&hid=3&sn=e16a90e7e5271a937af691ca1f1dbf42)

[Sentinel 17 问](https://mp.weixin.qq.com/s/JBX3M-LrNwCoGl4Xzcg18Q)
### 什么是 sentinel
以流量为切入点，从流量控制、熔断降级、系统负载保护服务稳定性
### sentinel 和 hystrix 区别与选择
[Sentinel vs Hystrix 限流对比，到底怎么选？ - Java技术栈 - 博客园](https://www.cnblogs.com/javastack/p/16544564.html)
### 如何接入 sentinel 服务台
1. yaml 添加配置即可，猜测利用了端点监控
2. 懒加载模式，访问一次后才被监控
### 流量控制如何配置
监控 qps 或并发线程数等指标
`com.alibaba.csp.sentinel.slots.block.flow.FlowRule`
1. 资源名 resource
	1.  限流作用的对象
2. 限流阈值 count
	1. 
3. 限流阈值类型 grade
	1. 
4. 流控针对的调用来源 limitApp
	1. 
5. 流控模式 strategy  ^75jy23
	1. 直接拒绝
	2. 关联
		1. A 关联 B，一旦 B 达到阈值，则 A 被限流  [网关做限流的问题](spring%20cloud微服务安全实战.md#网关做限流的问题%20网关做限流的问题)
	3. 链路
		1. 只记录指定链路上的流量（指定资源从入口资源进来的流量，如果达到阈值，就可以限流）
6. 流控效果 controlBehavior
	1. 快速失败，抛出 `FlowException`
	2. warm up
		1. 预热/冷启动
		2. 令牌桶算法
	3. 排队等待
		1. 漏桶算法

### 降级规则如何配置
三种熔断策略
1. 平均响应时间
2. 异常比例
3. 异常数
### 热点参数如何限流    #热点数据
1. 利用 LRU 策略统计热点参数，利用令牌桶算法进行参数级别控制
2. 只能针对 qps
### 系统自适应如何限流
1. 热点参数、普通流量限流都是针对的某个接口
2. 系统自适应限流针对的是整个系统的入口流量
### 如何自定义限流返回的异常信息
`@SentinelResource`
### 如何对异常进行降级处理
`@SentinelResource`
### 限流规则如何持久化
