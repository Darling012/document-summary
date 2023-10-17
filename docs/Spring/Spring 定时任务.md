# Spring 定时任务

## SpringTask
[深入浅出Spring task定时任务](https://blog.csdn.net/u011116672/article/details/52517247)

[Spring Schedule实现方式](https://pdai.tech/md/spring/springboot/springboot-x-task-spring-task-timer.html)
	1. 处理异常
	2. 处理超时

##### springboot 定时器默认单线程问题
springtask 默认单线程串行执行，可通过配置 SchedulingConfigurer 实现并行任务（ThreadPoolTaskScheduler，可注入的线程池为 XXXScheduler），即不同任务方法用不同线程，解决不能同时执行不同任务。
加上[@Async](Spring%20异步.md#Async)实现并行异步任务（另起线程执行）解决单个任务需要间隔三秒，但执行了五秒，上个任务影响下个任务问题。只加@Async也能实现并行。  
  
即ThreadPoolTaskScheduler解决了调用定时任务时，当存在多个任务，提供了不同线程。  
[@Async](Spring%20异步.md#Async) 是异步执行，无论各个任务，还是单个任务不同循环，都是另起线程，所以实现了并行。要注意极端情况线程池没线程可用。  
1. springtask 是通过配置 `SchedulingConfigurer` 注入自定义 ` ThreadPoolTaskScheduler `
2. @Async 是自定义`ThreadPoolTaskExecutor`

存在在异步线程中获取不到 request 情况，因为通过@async 注解的方法，会被 springboot 丢到线程池中去执行，就等于开启了新的线程； 但是 RequestContextHolder 使用 ThreadLocal 保存 request 实例的，那么如果在新的线程中肯定会获取不到 request 的。建议在调用异步方法的时候，将 request 作为参数传递到异步方法中  

[Springboot定时器多线程解决多个定时器冲突问题](https://blog.csdn.net/cssnnd/article/details/108328942)


#### 对任务监控
![Metric监控](Metric监控.md#spring%20boot%20admin%20spring%20boot%20actuator)
##### 自定义注解输出日志
利用 [AOP](AOP.md) 日志输出。

#### 动态增删任务
[Spring Boot 实现动态定时任务 | 蒋先森のBlog](https://jlj98.top/springboot-dynamic-job/)
1. 仅修改任务周期
	1. 实现 `SchedulingConfigurer` 方法，重写 `configureTasks` 方法，重新制定 `Trigger`。
	2. `scheduledTaskRegistrar.addTriggerTask`，它只接收两个参数，分别是调度任务实例(Runable 实例)，Trigger 实例。如果想修改定时任务的时间，其实修改的就是这里的 `nextExecutionTime`，返回下次执行时间。
	3. 这种方法存在一种缺陷，就是修改周期后，需要下一次执行后才能生效
2. 动态提交任务并修改任务执行周期
	1. `ThreadPoolTaskScheduler` 通过 `schedule(Runnable task, Trigger trigger)` 提交了一个新的任务。
[spring boot实现动态增删启停定时任务 - 简书](https://www.jianshu.com/p/0f68936393fd)
1. 原理跟上边一样，更加完整
2. 包括从数据库读取，启停等
3. 通过传入对象名，方法名，参数名反射调用，感觉没啥用
[SpringBoot定时任务动态管理通用解决方案](https://blog.csdn.net/qq_34886352/article/details/106494637)
1. 也用到了反射，比上个还复杂


## Quartz
[基础quartz实现方式](https://pdai.tech/md/spring/springboot/springboot-x-task-quartz-timer.html)

[Springboot整合quartz实现定时任务的动态加载 - 掘金](https://juejin.cn/post/6921270284360089614)
1. springboot-summary 中已集成测试
2. 
[quartz-lite-starter: 基于Quartz的可视化任务系统，SpringBoot-Starter版本](https://gitee.com/leiguoqing/quartz-lite-starter)

### 分布式 Quartz
[分布式quartz cluster方式](https://pdai.tech/md/spring/springboot/springboot-x-task-quartz-cluster-timer.html) 

## reference
1. [千万不要把Request传递到异步线程里面](https://mp.weixin.qq.com/s/eu6cpP7rpjZvf4-nl3K7uw)
2. [Spring Async 最佳实践(3)：完结篇\_51CTO博客\_spring @async](https://blog.51cto.com/u_15127686/2832738)
3. [spring boot 接口，启用异步线程，spring request过期问题_springboot 异步request失效](https://blog.csdn.net/jiao_zg/article/details/121030592)
4. [ HttpServletRequest和@Async搭配使用出现的问题](https://blog.csdn.net/wzy_168/article/details/109182301)

