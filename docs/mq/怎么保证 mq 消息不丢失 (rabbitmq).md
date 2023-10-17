#### 怎么保证 mq 消息不丢失 (rabbitmq)
 1. 生产者丢数据，数据在网络传输丢失
	1. 事务机制, 吞吐量和性能都会下降很多
		1. 在发送前通过 `channel.txSelect` 开启事务，然后发送消息
		2. 如果消息 rabbitmq 未收到，则生产者会收到异常，进行 `channel.txRollback`
		3. 成功则提交事务 `channel.txCommit`
	2. confirm 机制
		1. 在生产者设置，每个发消息都分配一个唯一的 ID
		2. mq 收到会回传一个 ack
		3. 收不到会回调 nack 接口
	3. 两者差异
		1. 事务机制是同步的
		2. [消息确认机制(confirm)以及事务机制(tx)](https://writing-bugs.blog.csdn.net/article/details/102935393)
2. mq 丢数据，消息到 mq 后没有落盘，mq 宕机
	1. 持久化机制，创建 queue 设置为持久化
		1. `boolean durable = true; channel.queueDeclare(queue_name, durable, false, false, null);`
	2. 发送消息设置为持久化
		1. `builder.deliveryMode(2);`
	3. 消息持久化后再给生产者发送 ack 消息
3. 消费端丢数据，接收到消息未消费前 down 机
	1. 关闭自动 ack，消费者一收到消息就会自动发 ack 给 mq
	2. [手动 ack](https://writing-bugs.blog.csdn.net/article/details/103701101)，在消费完后再发 ack。若中间出现问题，mq 会重发。
4. reference
	1. [RabbitMQ生产端可靠性投递（一） - 掘金](https://juejin.cn/post/6997973196716638238)
	2. [深入解析分布式消息队列设计精髓](https://mp.weixin.qq.com/s/7NfpOjnEetbwp7owJ53Z4w)
![RabbitMQ 消息中间件技术精讲](RabbitMQ%20消息中间件技术精讲.md#3-2%20消息如何保障%20100%20的投递成功方案-1%2015%2038)
![RabbitMQ 消息中间件技术精讲](RabbitMQ%20消息中间件技术精讲.md#3-3%20消息如何保障%20100%20的投递成功方案-2%2012%2014)




[2.RabbitMQ 的可靠性消息的发送 - 千里送e毛 - 博客园](https://www.cnblogs.com/qlsem/p/11553266.html)