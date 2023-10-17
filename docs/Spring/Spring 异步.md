# Spring 异步

[Spring MVC的异步模式（Callable、WebAsyncTask、DeferredResult](https://blog.csdn.net/f641385712/article/details/88692534)
1. 这里说的异步对于前端是无感知的，也就是并不是立马返回前端。
2. Spring MVC 3.2 之后支持异步请求，能够在 controller 中返回一个 `Callable` 或者 `DeferredResult`。
3. 并不建议直接使用 Callable ，而是使用 Spring 提供的 `WebAsyncTask` 代替，它包装了 Callable，功能更强大些
4. `DeferredResult` 返回的时候实际结果可能没有生成，实际的结果可能会在另外的线程里面设置到 `DeferredResult` 中去。
[Spring MVC的异步模式（ResponseBodyEmitter、SseEmitter、StreamingResponseBody） 高级使用篇](https://blog.csdn.net/f641385712/article/details/88710676)
1. 

[SpringMVC之异步分析Callable,WebAsyncTask,DeferredResult - 简书](https://www.jianshu.com/p/42c49902782d)
1. `spring MVC3.2` 之后支持异步请求，能够在 `controller` 中返回一个 `Callable` 或者 `DeferredResult`
2. `WebAsyncTask` 在 `Callable` 的基础上进行了包装，提供了更强大的功能，比如：处理超时回调、错误回调、完成回调等。  
3. `DeferredResult` 使用方式与 `Callable` 类似，重点在于跨线程之间的通信。`@Async` 也是替换 `Runable` 的一种方式，可以代替我们自己创建线程。而且适用的范围更广，并不局限于 `Controller` 层，而可以是任何层的方法上。
4. `Servlet3.0` 提供了 `AsyncContext` 支持异步处理。`Spring DeferredResult` 在 `AsyncContext` 进行了优化，实现了更简单的异步的实现。  
5. `Callable` 是并发编程提供的支持有返回值的异步处理方式。 
  
## 这里异步的含义


![WEB即时通讯](WEB即时通讯.md#连接与线程)

跟 ![BIONIOAIO](BIONIOAIO.md#^ny0odi) 类似，这里的异步也是让服务端提高效率

### Servlet

[Tomcat是如何实现异步Servlet的 - 掘金](https://juejin.cn/post/6844903965801316359)
1. 异步 Servlet 的内部原理
	1. `req.startAsync()` 只是保存了一个异步上下文，同时设置一些基础信息，比如 `Timeout`,顺便提一下，这里设置的默认超时时间是**30 S**，如果你的异步处理逻辑超过**30 S**, 此时执行 `ctx.complete()` 就会抛出 IllegalStateException 异常。
	2. 
2. **为什么说 Spring Boot 的@EnableAsync 注解不是异步 Servlet**
	1. 从业务层面来说，确实是异步编程，但是有一个问题，抛开业务的并行处理来说，针对整个请求来说，并不是异步的，也就是说不能立即释放 Tomcat 的线程，从而不能达到异步 Servlet 的效果。
3. 聊聊异步 Servlet 的使用场景
	1. 异步 Servlet 提高了系统的吞吐量，可以接受更多的请求。假设 web 系统中 Tomcat 的线程不够用了，大量请求在等待，而此时 Web 系统应用层面的优化已经不能再优化了，也就是无法缩短业务逻辑的响应时间了，这个时候，如果想让减少用户的等待时间，提高吞吐量，可以尝试下使用异步 Servlet。

[Servlet3中的AsyncContext异步和多线程异步有什么区别 - ITeye问答](https://www.iteye.com/problems/101332)
1. 使用 AsyncContext 的时候，对于浏览器来说，他们是同步在等待输出的
2. 但是对于服务器端来说，处理此请求的线程并没有卡在那里等待，则是把当前的处理转为线程池处理了
3. 关键就在于线程池，服务器端会起一个线程池去服务那些需要异步处理的请求
4. 而如果你自己每次请求去起一个线程处理的话，这就有可能会耗大量的线程。

[Spring MVC Async Processing](https://github.com/chanjarster/web-async-learn/blob/master/spring-mvc-async-processing/README.md)
1. 本文讲到的所有特性皆是基于 Servlet 3.0 Async Processing 的，不是基于 Servlet 3.1 Async IO 的。
2. Callable
3. DeferredResult
4. ListenableFuture or CompletableFuture/CompletionStage
5. ResponseBodyEmitter
6. SseEmitter
7. 

[Spring MVC Async IO](https://github.com/chanjarster/web-async-learn/blob/master/spring-mvc-async-io/README.md)
1. Spring MVC并没有提供对Servlet 3.1 Async IO的直接支持，本文介绍一些在Spring MVC中使用到Servlet 3.1 Async Processing的方法

[SpringBoot的四种异步处理，写这篇文章，我自己先学到了 - 程序新视界](https://mp.weixin.qq.com/s/2PdSH2el6CLMw61QMtwInw)
1. 异步请求与同步请求
2. Servlet 3.0 中的异步 `AsyncContext`
3. 基于 Spring 实现异步请求
	1. 基于 Callable 实现
		1. Spring MVC 开启副线程处理业务 (将 Callable 提交到 TaskExecutor)；
		2. DispatcherServlet 和所有的 Filter 退出 Web 容器的线程，但是 response 保持打开状态；
		3. Callable 返回结果，SpringMVC 将原始请求重新派发给容器 (再重新请求一次/email)，恢复之前的处理；
		4. DispatcherServlet 重新被调用，将结果返回给用户；
	2. 基于 WebAsyncTask 实现
		1. Spring 提供的 WebAsyncTask 是对 Callable 的包装，提供了更强大的功能，比如：处理超时回调、错误回调、完成回调等。
	3. 基于 DeferredResult 实现
		1. controller 返回一个 DeferredResult，把它保存到内存里或者 List 里面（供后续访问）；
		2. Spring MVC 调用 request.startAsync ()，开启异步处理；与此同时将 DispatcherServlet 里的拦截器、Filter 等等都马上退出主线程，但是 response 仍然保持打开的状态；
		3. 应用通过另外一个线程（可能是 MQ 消息、定时任务等）给 DeferredResult setResult值 。然后 SpringMVC 会把这个请求再次派发给 servlet 容器；
		4. DispatcherServlet 再次被调用，然后处理后续的标准流程；
4. SpringBoot 中的异步实现
	1. @EnableAsync 用于开启 SpringBoot 支持异步的功能
	2. @Async用于方法上，标记该方法为异步处理方法。

[007-优化web请求三-异步调用【WebAsyncTask】 - bjlhx15 - 博客园](https://www.cnblogs.com/bjlhx/p/10444814.html)
1. Servlet 3.0 异步请求运作机制的部分原理
2. Callable 的异步请求被处理时所发生的事件
	1. Controller 返回 Callable 
	2. **Spring MVC 调用 request. startAsync（）**并将 Callable 提交给 TaskExecutor，以便在单独的线程中进行处理。 
	3. 同时 DispatcherServlet 和所有 Filter 都退出 Servlet 容器线程，但响应仍保持打开状态。  
	4. 最终，Callable 生成一个结果，Spring MVC 将请求调度回 Servlet 容器以完成处理。  
	5. 再次调用 DispatcherServlet，并使用来自 Callable 的异步生成的返回值继续处理。
3. Callable、DeferredResult、WebAsyncTask、Async 对比
	1. Async 不释放容器线程


[Servelt3 异步请求](https://mp.weixin.qq.com/s/aengS21vh0gqmqhSMAbeVA)
1. SpringMVC 3.2 基于 Servelt 3 提供有两种异步方式
	1. `DeferredResult`
	2.  `Callable`
[Tomcat](Tomcat.md#异步%20Servlet)

[浅谈spring servlet异步编程 - 知乎](https://zhuanlan.zhihu.com/p/612546966)
##### spring 的异步到底指的是什么
1.  真正释放 tomcat 工作 线程的
	1. servlet 3 异步上下文`DeferredResult`
	2. `Callable` 以及其升级版 `WebAsyncTask`
		1. `Spring MVC calls request.startAsync() `，但没看到具体源码
2. 不释放 tomcat 线程的业务异步
	1. `@EnableAsync+@Async`
## DeferredResult

[DeferredResult 如何实现长轮询？](https://segmentfault.com/a/1190000042332319)

[DeferredResult——异步请求处理_程序员Sunny的博客-CSDN博客_deferredresult](https://blog.csdn.net/m0_37595562/article/details/81013909)

[Long Poll 长轮询 \_阿洋 blog](http://xinzhuxiansheng.com/articleDetail/26)
[Spring Boot 使用DeferredResult实现长轮询 - 掘金](https://juejin.cn/post/6950164619872698405)

##### 长轮询
[Spring长轮询DeferredResult简单用法以及SpringMVC对于后置结果处理](https://www.cnblogs.com/qlqwjy/p/17034943.html)

###### 完整的从长轮询概念到异步实现长轮询
[1. 什么是轮询、长轮询、长连接](https://mp.weixin.qq.com/s?__biz=MzA4ODIyMzEwMg==&mid=2447536690&idx=1&sn=df30a28f4f74dadefec2e4b67accb2b6)
[2. Spring Boot使用Servlet居然也可以实现长轮询](https://mp.weixin.qq.com/s?__biz=MzA4ODIyMzEwMg==&mid=2447536702&idx=1&sn=10749a4955d2139a010b60dae01a6101)
1. Servlet 3.0 的AsyncContext
[3. Spring Boot使用Spring DeferredResult实现长轮询](https://mp.weixin.qq.com/s?__biz=MzA4ODIyMzEwMg==&mid=2447536721&idx=1&sn=774d53d28fdcbb86b54d398ce20aed6d)
1. 举了 DeferredResult 的小例子
[4. Spring Boot使用Callable和WebAsyncTask实现长轮询](https://mp.weixin.qq.com/s?__biz=MzA4ODIyMzEwMg==&mid=2447536740&idx=1&sn=dff1a429cf8effed549366e3a45e391e)
1. 多种长轮询方案
[5. DeferredResult是Spring对Servlet异步处理的包装吗？](https://mp.weixin.qq.com/s?__biz=MzA4ODIyMzEwMg==&mid=2447536763&idx=1&sn=905fcdbb4404edeab344e837b2e6926d)
1. 源码分析了 DeferredResult 是对 Servlet 3.0 的 AsyncContext 的包装


## @EnableAsync+@Async

##### 既然不会释放 servlet 线程，那适用场景是什么呢？
不需要响应等待的任务，比如发短信、同步log

[What are the differences between a tomcat thread and a thread started by the async annotation in spring boot?](https://stackoverflow.com/questions/71371203/what-are-the-differences-between-a-tomcat-thread-and-a-thread-started-by-the-asy)



![单体脚手架](单体脚手架.md#异步执行)

[【小家Spring】Spring异步处理@Async的使用以及原理、源码分析（@EnableAsync）-腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1497604)

[@Async默认线程池问题](https://mp.weixin.qq.com/s/ukAXF2kvlZmecO7BhHrbWA)
 1. spring 默认使用（看着并不是 SimpleAsyncTaskExecutor，奇怪。），corePoolSize=8，阻塞队列采用了无界队列 LinkedBlockingQueue
 2. 超出 8 个的任务会放到无界队列里，任务过多就会造成 OOM。
 3. 所以需要自定义 ThreadPoolTaskExecutor
 4. 使用 springboot， 配置类 TaskExecutionAutoConfiguration 提供了默认的 Executor
 5. 假如没有 springboot，spring 默认使用 SimpleAsyncTaskExecutor（跟上边这不不一样了）
 6. SimpleAsyncTaskExecutor 类设计的非常操蛋，因为它每执行一次，都会创建一个单独的线程，根本没有共用线程池。比如你的 TPS 是1000，异步执行了任务，那么你每秒将会生成1000个线程！
ps: 经测试，springboot 下 确实是 SimpleAsyncTaskExecutor。上边的不知道说的啥
pps: 集合下边这篇文章，应该就是 SimpleAsyncTaskExecutor 的实例，因为线程池是先核心线程，再队列，最后最大线程，由于用的无界队列，所以来一个建一个。
[别问了，我真的不喜欢这个注解](https://mp.weixin.qq.com/s?__biz=Mzg3NjU3NTkwMQ==&mid=2247522594&idx=1&sn=0ad582443ed8723d8060ce00924f4456&scene=21)


 [如何在SpringBoot中使用异步方法优化Service逻辑提高接口响应速度?\_springboot异步接口响应\_jinchange的博客-CSDN博客](https://blog.csdn.net/weixin_43441509/article/details/119855613/)

### @Async 失效
[Spring Aop 失效](Spring%20AOP.md#Spring自调用问题)


## reference
[GitHub - chanjarster/web-async-learn: Java web开发async机制学习](https://github.com/chanjarster/web-async-learn)
《Java异步编程实战》- 第四章讲解Spring框架中提供的异步执行能力，包含Spring中如何对TaskExecutor进行的抽象，以及如何使用注解@Async实现异步编程，以及其内部实现原理；

