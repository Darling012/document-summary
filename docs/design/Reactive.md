# Reactive


《Java异步编程实战》第5章基于反应式编程实现异步编程

1. JDK8Stream提供了流式对数据集合的处理，带来了命令式编程向声明式编程的改变
2. CompletableFuture+Stream的异步编程属于声明式编程，不需要自己创建线程池（某些时候并不算好处）
3. Reactive Programming是一种设计数据流和变化传播的异步编程范式。在命令式中a=b+c，b加c的值赋给a后，b和c变了a不会变，但在响应式中，a会跟着变。
4. JDK8Stream是基于拉的，数据只能消费一次，缺少与时间有关的操作、延迟操作以及不能自定义业务线程池。
5. 遵循Java的Reactive API的RxJava和Reactor库就有了用武之地，相比CompletableFuture+Stream更简单丰富
6. Reactive 编程思想实现的反应式库（例如 reactor 、叫ava ）旨在解决JVM 上“经典”异步编程方法的缺点并提供了更多的特性
7. Reactive Streams 是一个规范（在 Java 9 中也采用），用于定义具有回压的异步组件之间的交互
8. Spring 团队认为 Reactor 是 Spring WebFlux 的首选 Reactive 库。
9. WebFlux 要求 Reactor 作为核心依赖但它可以通过 Reactive Streams 与其他反应库（比如 RxJava ）进行交互操作

[Spring 5的Servlet和反应式技术栈解析\_Java\_Rossen Stoyanchev\_InfoQ精选文章](https://www.infoq.cn/article/servlet-and-reactive-stacks-spring-framework-5)

## RSocket
[Reactive & RSocket介绍 - 掘金](https://juejin.cn/post/7145233384137031710)

[RSocket——Http协议的替代者 - 掘金](https://juejin.cn/post/6844903958314500103)
[RSocket 学习(一)：初探 - 简书](https://www.jianshu.com/p/618fd4a13fa6)
[RSocket 学习(二)：HTTP VS WebSocket VS RSocket - 简书](https://www.jianshu.com/p/227a4c9b5d6f)

### Nacos中的应用

## Spring WebFlux

1. Netty，Undertow 异步容器
2. 支持 NIO api 的 servlet3.1 tommcat、jetty
### spring  cloud gateway
getway web handler ->FilteringWebHandler

GateWay Handler Mapping->RoutePredicateHandlerMapping

1. [Spring WebFlux 要革了谁的命？](https://mp.weixin.qq.com/s?__biz=MzAxOTc0NzExNg==&mid=2665515772&idx=1&sn=205b10cfb2241cfe1b16c7f832b48197)
2. [响应式Spring的道法术器（Spring WebFlux 教程）](https://blog.csdn.net/get_set/article/details/79466657)
3. [Spring Webflux源码阅读 - 文集 - 简书](https://www.jianshu.com/nb/18514645)
4. [Springboot 2.0---WebFlux请求处理流程](https://www.jianshu.com/p/0ac921cf829e)
5. [WebFlux 与 Web MVC 对比](https://mp.weixin.qq.com/s?__biz=MzAwMTk4NjM1MA==&mid=2247489738&idx=1&sn=08839ac5b3fb412d6a9f93defb345cbd)
6. [Spring Cloud Gateway-ServerWebExchange核心方法与请求或者响应内容的修改 - 掘金](https://juejin.cn/post/6844903846469189645)

