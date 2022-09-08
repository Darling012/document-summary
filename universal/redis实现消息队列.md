# Overview

​	火灾项目中有应用延迟队列场景，包括预警信息超时处理和预警消息时间窗口内聚合。考虑不增加其他组件及对消息可靠性要求不是特别高，所以应用了redis实现延迟队列。后期又考虑使用 redis实现消息解耦，最后采用mqtt，并新增组件emqx。

**注意点**

1. 利用Redis 过期回调实现延迟队列想当**不可靠**！Redis不能确保key在指定时间被删除,在 [Timing of expired events ](https://redis.io/topics/notifications#timing-of-expired-events) 中 , 明确的说明了 "Basically `expired` events **are generated when the Redis server deletes the key** and not when the time to live theoretically reaches the value of zero." 
2. redis PubSub已经被stream替代了。



我们平时习惯于使用 Rabbitmq 和 Kafka 作为消息队列中间件，来给应用程序之间增加异步消息传递功能。这两个中间件都是专业的消息队列中间件，特性之多超出了大多数人的理解能力。

使用过 Rabbitmq 的同学知道它使用起来有多复杂，发消息之前要创建 Exchange，再创建 Queue，还要将 Queue 和 Exchange 通过某种规则绑定起来，发消息的时候要指定 routing-key，还要控制头部信息。消费者在消费消息之前也要进行上面一系列的繁琐过程。但是绝大多数情况下，虽然我们的消息队列只有一组消费者，但还是需要经历上面这些繁琐的过程。

有了 Redis，它就可以让我们解脱出来，对于那些只有一组消费者的消息队列，使用 Redis 就可以非常轻松的搞定。Redis 的消息队列不是专业的消息队列，它没有非常多的高级特性，**没有 ack 保证**，如果对消息的可靠性有着极致的追求，那么它就不适合使用。

# 异步消息队列

Redis 的 list(列表) 数据结构常用来作为异步消息队列使用，使用`rpush/lpush`操作入队列，使用`lpop 和 rpop`来出队列。



![image](http://10.16.32.202:9000/projectimg/1618150700483-ceeb87bb-204f-42b4-baa7-3cd4bc4fe3f3.png)
rpush 和 lpop 结合使用还可以使用 lpush 和 rpop 结合使用，效果是一样的。这里不再赘述。

# **延时队列**

延时队列可以通过 Redis 的 zset(有序列表) 来实现。我们将消息序列化成一个字符串作为 zset 的`value`，这个消息的到期处理时间作为`score`，然后用多个线程轮询 zset 获取到期的任务进行处理，多个线程是为了保障可用性，万一挂了一个线程还有其它线程可以继续处理。因为有多个线程，所以需要考虑并发争抢任务，确保任务不能被多次执行。

**Redis 的 zrem 方法是多线程多进程争抢任务的关键，它的返回值决定了当前实例有没有抢到任务**，因为 loop 方法可能会被多个线程、多个进程调用，同一个任务可能会被多个进程线程抢到，通过 zrem 来决定唯一的属主。

同时，我们要注意一定要对 handle_msg 进行异常捕获，**避免因为个别任务处理问题导致循环异常退出**。还要注意要保证扫描zset的线程久活。

```java
import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import redis.clients.jedis.Jedis;

public class RedisDelayingQueue<T> {

  static class TaskItem<T> {
    public String id;
    public T msg;
  }

  // fastjson 序列化对象中存在 generic 类型时，需要使用 TypeReference
  private Type TaskType = new TypeReference<TaskItem<T>>() {
  }.getType();

  private Jedis jedis;
  private String queueKey;

  public RedisDelayingQueue(Jedis jedis, String queueKey) {
    this.jedis = jedis;
    this.queueKey = queueKey;
  }

  public void delay(T msg) {
    TaskItem<T> task = new TaskItem<T>();
    task.id = UUID.randomUUID().toString(); // 分配唯一的 uuid
    task.msg = msg;
    String s = JSON.toJSONString(task); // fastjson 序列化
    jedis.zadd(queueKey, System.currentTimeMillis() + 5000, s); // 塞入延时队列 ,5s 后再试
  }

  public void loop() {
    while (!Thread.interrupted()) {
      // 只取一条
      Set<String> values = jedis.zrangeByScore(queueKey, 0, System.currentTimeMillis(), 0, 1);
      if (values.isEmpty()) {
        try {
          Thread.sleep(500); // 歇会继续
        } catch (InterruptedException e) {
          break;
        }
        continue;
      }
      String s = values.iterator().next();
      if (jedis.zrem(queueKey, s) > 0) { // 抢到了
        TaskItem<T> task = JSON.parseObject(s, TaskType); // fastjson 反序列化
        this.handleMsg(task.msg);
      }
    }
  }

  public void handleMsg(T msg) {
    System.out.println(msg);
  }

  public static void main(String[] args) {
    Jedis jedis = new Jedis();
    RedisDelayingQueue<String> queue = new RedisDelayingQueue<>(jedis, "q-demo");
    Thread producer = new Thread() {

      public void run() {
        for (int i = 0; i < 10; i++) {
          queue.delay("codehole" + i);
        }
      }

    };
    Thread consumer = new Thread() {

      public void run() {
        queue.loop();
      }

    };
    producer.start();
    consumer.start();
    try {
      producer.join();
      Thread.sleep(6000);
      consumer.interrupt();
      consumer.join();
    } catch (InterruptedException e) {
    }
  }
}
```

 **Redis 消息队列的不足之处，那就是它不支持消息的多播机制**。



![image](http://10.16.32.202:9000/projectimg/1618149786397-d7a45466-4d4c-46d2-ac81-26322b503ef8.png)



## PubSub

为了支持消息多播，Redis 不能再依赖于那 5 种基本数据类型了。它单独使用了一个模块来支持消息多播，这个模块的名字叫着 PubSub，也就是 PublisherSubscriber，发布者订阅者模型。

## PubSub 缺点

PubSub 的生产者传递过来一个消息，Redis 会直接找到相应的消费者传递过去。如果一个消费者都没有，那么消息直接丢弃。如果开始有三个消费者，一个消费者突然挂掉了，生产者会继续发送消息，另外两个消费者可以持续收到消息。但是挂掉的消费者重新连上的时候，这断连期间生产者发送的消息，对于这个消费者来说就是彻底丢失了。

如果 Redis 停机重启，PubSub 的消息是不会持久化的，毕竟 Redis 宕机就相当于一个消费者都没有，所有的消息直接被丢弃。

正是因为 PubSub 有这些缺点，它几乎找不到合适的应用场景。所以 Redis 的作者单独开启了一个项目 Disque 专门用来做多播消息队列。该项目目前没有成熟，一直长期处于 Beta 版本，但是相应的客户端 sdk 已经非常丰富了，就待 Redis 作者临门一脚发布一个 Release 版本。

# **Stream**

Redis5.0 最大的新特性就是多出了一个数据结构 **Stream**，它是一个新的强大的支持多播的可持久化的消息队列，作者坦言 Redis Stream 狠狠地借鉴了 Kafka 的设计。

![image](http://10.16.32.202:9000/projectimg/1618150069579-a1738878-00a2-4412-a904-fd2f71ec1008.png)

Redis Stream 的结构如上图所示，它有一个消息链表，将所有加入的消息都串起来，每个消息都有一个唯一的 ID 和对应的内容。消息是持久化的，Redis 重启后，内容还在。

每个 Stream 都有唯一的名称，它就是 Redis 的 key，在我们首次使用`xadd`指令追加消息时自动创建。

每个 Stream 都可以挂多个消费组，每个消费组会有个游标`last_delivered_id`在 Stream 数组之上往前移动，表示当前消费组已经消费到哪条消息了。每个消费组都有一个 Stream 内唯一的名称，消费组不会自动创建，它需要单独的指令`xgroup create`进行创建，需要指定从 Stream 的某个消息 ID 开始消费，这个 ID 用来初始化`last_delivered_id`变量。

每个消费组 (Consumer Group) 的状态都是独立的，相互不受影响。也就是说同一份 Stream 内部的消息会被每个消费组都消费到。

同一个消费组 (Consumer Group) 可以挂接多个消费者 (Consumer)，这些消费者之间是竞争关系，任意一个消费者读取了消息都会使游标`last_delivered_id`往前移动。每个消费者有一个组内唯一名称。

消费者 (Consumer) 内部会有个状态变量`pending_ids`，它记录了当前已经被客户端读取的消息，但是还没有 ack。如果客户端没有 ack，这个变量里面的消息 ID 会越来越多，一旦某个消息被 ack，它就开始减少。这个 pending_ids 变量在 Redis 官方被称之为`PEL`，也就是`Pending Entries List`，这是一个很核心的数据结构，它用来确保客户端至少消费了消息一次，而不会在网络传输的中途丢失了没处理。

## **消息 ID**

消息 ID 的形式是`timestampInMillis-sequence`，例如`1527846880572-5`，它表示当前的消息在毫米时间戳`1527846880572`时产生，并且是该毫秒内产生的第 5 条消息。消息 ID 可以由服务器自动生成，也可以由客户端自己指定，但是形式必须是`整数-整数`，而且必须是后面加入的消息的 ID 要大于前面的消息 ID。

## **消息内容**

消息内容就是键值对，形如 hash 结构的键值对，这没什么特别之处。

## **增删改查**

1. `xadd` 追加消息
2. `xdel` 删除消息，这里的删除仅仅是设置了标志位，不影响消息总长度
3. `xrange` 获取消息列表，会自动过滤已经删除的消息
4. `xlen` 消息长度
5. `del` 删除 Stream

## **独立消费**

我们可以在不定义消费组的情况下进行 Stream 消息的独立消费，当 Stream 没有新消息时，甚至可以阻塞等待。Redis 设计了一个单独的消费指令`xread`，可以将 Stream 当成普通的消息队列 (list) 来使用。使用 `xread` 时，我们可以完全忽略消费组 (Consumer Group) 的存在，就好比 Stream 就是一个普通的列表 (list)。

```
# 从 Stream 头部读取两条消息
127.0.0.1:6379> xread count 2 streams codehole 0-0
1) 1) "codehole"
   2) 1) 1) 1527851486781-0
         2) 1) "name"
            2) "laoqian"
            3) "age"
            4) "30"
      2) 1) 1527851493405-0
         2) 1) "name"
            2) "yurui"
            3) "age"
            4) "29"
# 从 Stream 尾部读取一条消息，毫无疑问，这里不会返回任何消息
127.0.0.1:6379> xread count 1 streams codehole $
(nil)
# 从尾部阻塞等待新消息到来，下面的指令会堵住，直到新消息到来
127.0.0.1:6379> xread block 0 count 1 streams codehole $
# 我们从新打开一个窗口，在这个窗口往 Stream 里塞消息
127.0.0.1:6379> xadd codehole * name youming age 60
1527852774092-0
# 再切换到前面的窗口，我们可以看到阻塞解除了，返回了新的消息内容
# 而且还显示了一个等待时间，这里我们等待了 93s
127.0.0.1:6379> xread block 0 count 1 streams codehole $
1) 1) "codehole"
   2) 1) 1) 1527852774092-0
         2) 1) "name"
            2) "youming"
            3) "age"
            4) "60"
(93.11s)
```

客户端如果想要使用 `xread` 进行顺序消费，一定要记住当前消费到哪里了，也就是返回的消息 ID。下次继续调用 `xread` 时，将上次返回的最后一个消息 ID 作为参数传递进去，就可以继续消费后续的消息。

block 0 表示永远阻塞，直到消息到来，block 1000 表示阻塞 1s，如果 1s 内没有任何消息到来，就返回 `nil`。

```
127.0.0.1:6379> xread block 1000 count 1 streams codehole $
(nil)
(1.07s)
```

## **创建消费组**



![image](http://10.16.32.202:9000/projectimg/1618150069692-556d28b1-c454-4351-8a98-34808851941f.png)

Stream 通过`xgroup create`指令创建消费组 (Consumer Group)，需要传递起始消息 ID 参数用来初始化`last_delivered_id`变量。

```
#  表示从头开始消费
127.0.0.1:6379> xgroup create codehole cg1 0-0
OK
# $ 表示从尾部开始消费，只接受新消息，当前 Stream 消息会全部忽略
127.0.0.1:6379> xgroup create codehole cg2 $
OK
# 获取 Stream 信息
127.0.0.1:6379> xinfo stream codehole
 1) length
 2) (integer) 3  # 共 3 个消息
 3) radix-tree-keys
 4) (integer) 1
 5) radix-tree-nodes
 6) (integer) 2
 7) groups
 8) (integer) 2  # 两个消费组
 9) first-entry  # 第一个消息
10) 1) 1527851486781-0
    2) 1) "name"
       2) "laoqian"
       3) "age"
       4) "30"
11) last-entry  # 最后一个消息
12) 1) 1527851498956-0
    2) 1) "name"
       2) "xiaoqian"
       3) "age"
       4) "1"
# 获取 Stream 的消费组信息
127.0.0.1:6379> xinfo groups codehole
1) 1) name
   2) "cg1"
   3) consumers
   4) (integer) 0  # 该消费组还没有消费者
   5) pending
   6) (integer) 0  # 该消费组没有正在处理的消息
2) 1) name
   2) "cg2"
   3) consumers  # 该消费组还没有消费者
   4) (integer) 0
   5) pending
   6) (integer) 0  # 该消费组没有正在处理的消息
```

## **消费**

Stream 提供了 `xreadgroup` 指令可以进行消费组的组内消费，需要提供消费组名称、消费者名称和起始消息 ID。它同 `xread` 一样，也可以阻塞等待新消息。读到新消息后，对应的消息 ID 就会进入消费者的 PEL(正在处理的消息) 结构里，客户端处理完毕后使用 `xack` 指令通知服务器，本条消息已经处理完毕，该消息 ID 就会从 PEL 中移除。

```
# > 号表示从当前消费组的 last_delivered_id 后面开始读
# 每当消费者读取一条消息，last_delivered_id 变量就会前进
127.0.0.1:6379> xreadgroup GROUP cg1 c1 count 1 streams codehole >
1) 1) "codehole"
   2) 1) 1) 1527851486781-0
         2) 1) "name"
            2) "laoqian"
            3) "age"
            4) "30"
127.0.0.1:6379> xreadgroup GROUP cg1 c1 count 1 streams codehole >
1) 1) "codehole"
   2) 1) 1) 1527851493405-0
         2) 1) "name"
            2) "yurui"
            3) "age"
            4) "29"
127.0.0.1:6379> xreadgroup GROUP cg1 c1 count 2 streams codehole >
1) 1) "codehole"
   2) 1) 1) 1527851498956-0
         2) 1) "name"
            2) "xiaoqian"
            3) "age"
            4) "1"
      2) 1) 1527852774092-0
         2) 1) "name"
            2) "youming"
            3) "age"
            4) "60"
# 再继续读取，就没有新消息了
127.0.0.1:6379> xreadgroup GROUP cg1 c1 count 1 streams codehole >
(nil)
# 那就阻塞等待吧
127.0.0.1:6379> xreadgroup GROUP cg1 c1 block 0 count 1 streams codehole >
# 开启另一个窗口，往里塞消息
127.0.0.1:6379> xadd codehole * name lanying age 61
1527854062442-0
# 回到前一个窗口，发现阻塞解除，收到新消息了
127.0.0.1:6379> xreadgroup GROUP cg1 c1 block 0 count 1 streams codehole >
1) 1) "codehole"
   2) 1) 1) 1527854062442-0
         2) 1) "name"
            2) "lanying"
            3) "age"
            4) "61"
(36.54s)
# 观察消费组信息
127.0.0.1:6379> xinfo groups codehole
1) 1) name
   2) "cg1"
   3) consumers
   4) (integer) 1  # 一个消费者
   5) pending
   6) (integer) 5  # 共 5 条正在处理的信息还有没有 ack
2) 1) name
   2) "cg2"
   3) consumers
   4) (integer) 0  # 消费组 cg2 没有任何变化，因为前面我们一直在操纵 cg1
   5) pending
   6) (integer) 0
# 如果同一个消费组有多个消费者，我们可以通过 xinfo consumers 指令观察每个消费者的状态
127.0.0.1:6379> xinfo consumers codehole cg1  # 目前还有 1 个消费者
1) 1) name
   2) "c1"
   3) pending
   4) (integer) 5  # 共 5 条待处理消息
   5) idle
   6) (integer) 418715  # 空闲了多长时间 ms 没有读取消息了
# 接下来我们 ack 一条消息
127.0.0.1:6379> xack codehole cg1 1527851486781-0
(integer) 1
127.0.0.1:6379> xinfo consumers codehole cg1
1) 1) name
   2) "c1"
   3) pending
   4) (integer) 4  # 变成了 5 条
   5) idle
   6) (integer) 668504
# 下面 ack 所有消息
127.0.0.1:6379> xack codehole cg1 1527851493405-0 1527851498956-0 1527852774092-0 1527854062442-0
(integer) 4
127.0.0.1:6379> xinfo consumers codehole cg1
1) 1) name
   2) "c1"
   3) pending
   4) (integer) 0  # pel 空了
   5) idle
   6) (integer) 745505
```

## **Stream 消息太多怎么办?**

要是消息积累太多，Stream 的链表岂不是很长，内容会不会爆掉?`xdel` 指令又不会删除消息，它只是给消息做了个标志位。

Redis 自然考虑到了这一点，所以它提供了一个定长 Stream 功能。在 `xadd` 的指令提供一个定长长度 `maxlen`，就可以将老的消息干掉，确保最多不超过指定长度。

```
127.0.0.1:6379> xlen codehole
(integer) 5
127.0.0.1:6379> xadd codehole maxlen 3 * name xiaorui age 1
1527855160273-0
127.0.0.1:6379> xlen codehole
(integer) 3
```

我们看到 Stream 的长度被砍掉了。如果 Stream 在未来可以提供按时间戳清理消息的规则那就更加完美了，但是目前还没有。

## **消息如果忘记 ACK 会怎样?**

Stream 在每个消费者结构中保存了正在处理中的消息 ID 列表 PEL，如果消费者收到了消息处理完了但是没有回复 ack，就会导致 PEL 列表不断增长，如果有很多消费组的话，那么这个 PEL 占用的内存就会放大。

![image](http://10.16.32.202:9000/projectimg/1618150069777-a26a6f5a-8a7e-4114-9de2-fd875a23da48.png)



## **PEL 如何避免消息丢失?**

在客户消费者读取 Stream 消息时，Redis 服务器将消息回复给客户端的过程中，客户端突然断开了连接，消息就丢失了。但是 PEL 里已经保存了发出去的消息 ID。待客户端重新连上之后，可以再次收到 PEL 中的消息 ID 列表。不过此时 `xreadgroup` 的起始消息 ID 不能为参数>，而必须是任意有效的消息 ID，一般将参数设为 0-0，表示读取所有的 PEL 消息以及自`last_delivered_id`之后的新消息。

## **Stream 的高可用**

Stream 的高可用是建立主从复制基础上的，它和其它数据结构的复制机制没有区别，也就是说在 Sentinel 和 Cluster 集群环境下 Stream 是可以支持高可用的。不过鉴于 Redis 的指令复制是异步的，在 `failover` 发生时，Redis 可能会丢失极小部分数据，这点 Redis 的其它数据结构也是一样的。

## **分区 Partition**

Redis 的服务器没有原生支持分区能力，如果想要使用分区，那就需要分配多个 Stream，然后在客户端使用一定的策略来生产消息到不同的 Stream。你也许会认为 Kafka 要先进很多，它是原生支持 Partition 的。关于这一点，我并不认同。记得 Kafka 的客户端也存在 HashStrategy 么，因为它也是通过客户端的 hash 算法来将不同的消息塞入不同分区的。

另外,Kafka 还支持动态增加分区数量的能力，但是这种调整能力也是很蹩脚的，它不会把之前已经存在的内容进行 rehash，不会重新分区历史数据。这种简单的动态调整的能力 Redis Stream 通过增加新的 Stream 就可以做到。

## 小结

Stream 的消费模型借鉴了 Kafka 的消费分组的概念，它弥补了 Redis Pub/Sub 不能持久化消息的缺陷。但是它又不同于 kafka，Kafka 的消息可以分 partition，而 Stream 不行。如果非要分 parition 的话，得在客户端做，提供不同的 Stream 名称，对消息进行 hash 取模来选择往哪个 Stream 里塞。

# Reference

1. [请勿过度依赖Redis的过期监听](https://juejin.cn/post/6844904158227595271) 
2. [SpringBoot实现监听redis key失效事件](https://www.jianshu.com/p/106f0eae07c8)
3. [Redis 做延迟消息队列](https://mp.weixin.qq.com/s/_Dkj-DsbQ7B0J1GYzJ8Zeg)
4. [Redis 深度历险：核心原理与应用实践](https://juejin.cn/book/6844733724618129422/section)

