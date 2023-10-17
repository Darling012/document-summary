##### 问题
1. 一个生产者生产一条数据，多个消费者消费同一条数据案例
	1. [FANOUT模式--随机产生多个队列名称绑定到同一个交换机上](https://www.zhihu.com/question/300082037)
	2. [Topic模式--服务**启动时**生成多个通配符队列](https://blog.csdn.net/weixin_39611937/article/details/112530488)
2. 怎么确认消息到达交换机以及队列
	1. [Springboot——整合Rabbitmq之Confirm和Return详解_专注写bug的博客-CSDN博客_springboot rabbitmq confirm](https://writing-bugs.blog.csdn.net/article/details/123175981)
	2. [《RabbitMQ》如何保证消息的可靠性 - 知乎](https://zhuanlan.zhihu.com/p/167826668)
	3. [rabbitmq消息发送的可靠性机制_車輪の唄的博客-CSDN博客_消息可靠性机制](https://blog.csdn.net/asdfsadfasdfsa/article/details/83751047)




##### rabbitmq
‌‌‌　　[史上最全的RabbitMQ-总结](https://mp.weixin.qq.com/s/iUsjGgpD1HsEU9AkJBw3RQ)

[RabbitMQ四种交换机类型 - 掘金](https://juejin.cn/post/6996134538887839780)
![](Pasted%20image%2020221103092717.png)


[rabbitmq_专注写bug的博客-CSDN博客](https://blog.csdn.net/qq_38322527/category_9451207.html)



##### k12
生产者
1. 
消费者
1. 解析消息
2. 解析成功后执行业务逻辑，手动ack
	1. `channel.basicAck(x.getDeliveryTag(), false);`
3. 解析不成功，进行消息补偿
	1. 判断是否
		1. `channel.basicReject(x.getDeliveryTag(), false);`
		2. 