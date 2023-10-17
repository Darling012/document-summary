# Nacos 怎么实现自动刷新

[Nacos配置中心原理](https://mp.weixin.qq.com/s?__biz=MzA3MTQ2MDgyOQ==&mid=2247484130&idx=1&sn=17b309de7a84389346d0abb1f0e7e46b)
1. ConfigService
	1. 从示例代码中的 ConfigService 找到实现 NacosConfigServer
	2. NacosConfigServer
		1. 主要实例化两个对象
		2. ServerHttpAgent
		3. ClientWorker
			1. 将 agent 维护在内部
			2. 创建两个线程池；第一个线程池只有一个线程执行每 10 秒的 checkConfigInfo () 方法；第二个线程池做长轮询
			
checkConfigInfo () 做了什么？
取出一批任务，交由线程池去执行 LongPollingRunnable，每个任务都有 taskId。

LongPollingRunnable 做了什么？
1.  检查本地配置信息
2.  获取服务端的配置信息然后更新到本地

1.  本地检查
取出该 taskID 相关的 CacheData 进行本地配置检查和监听器 md5 检查。
    本地检查是为了服务容错，服务端挂了可以从本地文件系统获取相关的配置信息，checkLoaclConfig
md5 checkListenerMd5

2.  服务端检查
通过 checkUpdateDataIds () 方法从服务端获取那些值发生了变化的 dataId 列表
getServerConfig 方法，根据 dataId 到服务端获取最新的配置信息，接着将最新的配置信息保存到 CacheData 中
最后调用 CacheData 的 checkListenerMd5 方法
最最后在 finally 中又重新通过 executorService 提交了本任务。

addListener
最终被放入到 CacheData 中

CacheData
dataId，group，content，taskId 这些跟配置相关的属性，还有两个比较重要的属性：listeners、md5
listeners 是该 CacheData 所关联的所有 listener，不过不是保存的原始的 Listener 对象，而是包装后的 ManagerListenerWrap 对象，该对象除了持有 Listener 对象，还持有了一个 lastCallMd5 属性。
另外一个属性 md5 就是根据当前对象的 content 计算出来的 md5 值。

Listeners 是什么时候触发回调方法 receiveConfigInfo
cacheData. checkListenerMd5 () 中，检查 CacheData 当前的 md5 与 CacheData 持有的所有 Listener 中保存的 md5 的值是否一致，如果不一致，就执行 safeNotifyListener，在这个方法中获取最新的配置信息，调用 Listener 的回调方法，将最新的配置信息作为参数传入，这样 Listener 的使用者就能接收到变更后的配置信息了，最后更新 ListenerWrap 的 md5 值

md5 何时变更
LongPollingRunnable 所执行的任务中，在获取服务端发生变更的配置信息时，将最新的 content 数据写入了 CacheData。可以看到是在长轮询的任务中，当服务端配置信息发生变更时，客户端将最新的数据获取下来之后，保存在了 CacheData 中，同时更新了该 CacheData 的 md5 值，所以当下次执行 checkListenerMd5 方法时，就会发现当前 listener 所持有的 md5 值已经和 CacheData 的 md5 值不一样了，也就意味着服务端的配置信息发生改变了，这时就需要将最新的数据通知给 Listener 的持有者。
###### 总结
1. Nacos 服务端创建了相关的配置项后，客户端就可以进行监听了。
2. 客户端是通过一个定时任务来检查自己监听的配置项的数据
3. 一旦服务端的数据发生变化时，客户端将会获取到最新的数据
4. 并将最新的数据保存在一个 CacheData 对象中，然后会重新计算 CacheData 的 md5 属性的值，此时就会对该 CacheData 所绑定的 Listener 触发 receiveConfigInfo 回调
5. 考虑到服务端故障的问题，客户端将最新数据获取后会保存在本地的 snapshot 文件中，以后会优先从文件中获取配置信息的值。



[Spring Cloud Alibaba Nacos 原理：推+拉打造客户端配置信息的实时更新](https://mp.weixin.qq.com/s?__biz=MzA3MTQ2MDgyOQ==&mid=2247484140&idx=1&sn=88a4c388bfb97b30d00e173296732604)
1. 客户端设置 http 请求超时时间为 30 秒的长轮询
2. 使用了`AsyncContext`


[（nacos源码系列）springBoot下实现http请求的异步长轮询—AsyncContext - 简书](https://www.jianshu.com/p/0e968ad2a5fa)