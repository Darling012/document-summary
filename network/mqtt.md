# mqtt

## mqtt over websocket
1. mqtt 本身和 [websocket](websocket.md) 没啥关系，但是 broker 可以处理 websocket 里的 mqtt 包
2. 使用 ws 建立连接，然后在 ws 通道上，使用 mqtt 协议进行通信
3. [Mqtt相关知识 - kerwin cui - 博客园](https://www.cnblogs.com/kerwincui/p/15366048.html)
4. [MQTT和Websocket的区别是什么？ - 知乎](https://www.zhihu.com/question/21816631) 

mqttx 客户端，在选择协议时，支持 ws:// mqtt://两种，区别是什么？
[使用浏览器连接emqx是不是就跟mqtt协议没关系了？ - EMQX - EMQ 问答社区](https://askemq.com/t/topic/2395)
1. mqtt://  客户端、emqx 都使用 mqtt 协议，tcp 发送，根据 mqtt 协议解析内容
2. ws://    客户端根据 mqtt 协议 组装报文，再通过 websocket 组装，再通过 tcp 发送
 >  mqtt <-> websocket <->tcp <-> ip <-> physical
## broker选择
[EMQX 与 RabbitMQ 消息服务器 MQTT 性能对比（上） | EMQ](https://www.emqx.com/zh/blog/emqx-or-rabbitmq-part-1)
[EMQX 与 RabbitMQ 消息服务器 MQTT 性能对比（下） | EMQ](https://www.emqx.com/zh/blog/emqx-or-rabbitmq-part-2)