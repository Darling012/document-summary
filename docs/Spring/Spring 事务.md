# Spring 事务
##### 问题
1. [三问Spring事务：解决什么问题？如何解决？存在什么问题？](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247491203&idx=2&sn=4a2d80eaa1a6193cbe85ae0b529e686d&)
2. 事务需要 AOP，但是没有显式加 `@EnableAspectJAutoProxy`
	1. [使springAOP生效不一定要加@EnableAspectJAutoProxy注解](https://segmentfault.com/a/1190000042350576)
	2. spring 添加 `@EnableTransactionManagement`（显式开启事务，SpringBoot 默认开启，可省略）
	3. [声明式事务与AOP | TONY'S TOY BLOG](https://www.tony-bro.com/posts/1345748359/index.html)
	4. [@EnableTransactionManagement开启事务管理时，@EnableAspectJAutoProxy必须开启吗_Niklauson的博客-CSDN博客](https://blog.csdn.net/Niklauson/article/details/89963887)
	5. [Springboot源码分析之@Transactional - 掘金](https://juejin.cn/post/6844903925766684679)
		1. ![](Pasted%20image%2020221109111121.png)
		2.	@EnableAspectJAutoProxy 和@EnableTransactionManagement 优先级？
	1. ps: springboot 约定大于配置，减少了很多配置过程，但相应的，不熟悉各个 startter 就会出现为什么我没有引入或者加 xx, 但也可以实现什么效果的情况，就有可能某个 startter

[事务](事务.md)

## spring 事务原理
[Spring AOP](Spring%20AOP.md)
[@Transactional实现原理](https://blog.csdn.net/qq_20597727/article/details/84868035)
1. 首先定义切点。spring 为我们定义了以 @Transactional 注解为植入点的切点，这样才能知道@Transactional 注解标注的方法需要被代理。
2. 在 spring 的  [Bean 的初始化过程中](SpringBean生命周期.md#四个阶段)，就需要对实例化的 bean 进行代理，并且生成代理对象。
3. 生成代理对象的代理逻辑中，进行方法调用时，需要先获取切面逻辑，@Transactional 注解的切面逻辑类似于@Around，在 spring 中是实现一种类似代理逻辑。
![](Pasted%20image%2020221028155949.png)
![](Pasted%20image%2020221028155956.png)


##### reference
2. [Spring 下，关于动态数据源的事务问题的探讨 - 青石路 - 博客园](https://www.cnblogs.com/youzhibing/p/12671004.html)
5. [Spring事务总结](https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247486851&idx=1&sn=e249e4724781655278f751fd21c8b0e5)
6. [Spring事务管理（详解+实例）_Trigl的博客-CSDN博客](https://blog.csdn.net/trigl/article/details/50968079)
8. [深入理解 Spring 事务原理](https://mp.weixin.qq.com/s?__biz=MzU0MzQ5MDA0Mw==&mid=2247489742&idx=2&sn=419ad1540abacba56242cbffee019d37)
9. [一文带你深入理解 Spring 事务原理](https://mp.weixin.qq.com/s?__biz=MzU0MzQ5MDA0Mw==&mid=2247489742&idx=2&sn=419ad1540abacba56242cbffee019d37)


## Spring 事务失效
1.  [Spring 事务失效？](https://mp.weixin.qq.com/s/mms25yg_HPRXY4UhC1_lvg)
2. [踩坑! spring事务,非事务方法与事务方法执行相互调用_西风一任秋的博客-CSDN博客_事务方法调用非事务方法](https://blog.csdn.net/m0_38027656/article/details/84190949)
3. [事务中的异常不也抛出了，为什么没catch到而回滚？](https://mp.weixin.qq.com/s/w2Oabjy0HR5bqvPy-sDA0g)
4. [Spring事务的那些坑9](https://mp.weixin.qq.com/s?__biz=MzAwNjkxNzgxNg==&mid=2247489843&idx=2&sn=b35e76735b22b58ea424d3d2336bafa9)
5. [嵌套事务、挂起事务，Spring 事务机制有什么奥秘？](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247491561&idx=2&sn=45234fa4537d3f51a5efc59e0d754496)
6. [不建议使用@Transactional事务](https://mp.weixin.qq.com/s?__biz=MzI3NzE0NjcwMg==&mid=2650144638&idx=1&sn=d6eb65e75e834fcead698069298e846b)
7. [@Transactional(rollbackFor=Exception.class)的使用)](https://blog.csdn.net/Mint6/article/details/78363761)
8. [一个@Transaction哪里来这么多坑？](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247492409&idx=2&sn=6d495dfdc9b307c1c94e7f5b808aba46)
10. [Spring事务自调用失效原因和Spring AOP原理 - 简书](https://www.jianshu.com/p/9ccab5a00bac)


## 七大传播特性是什么？
spring 在当前线程内，处理多个数据库操作方法事务时所做的一种事务应用策略
#### Reference
1. [8000字 | 32 张图 | 一文搞懂事务+隔离级别+阻塞+死锁](https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247491641&idx=2&sn=c425d69936050c3963c52894112074d6&chksm=cea1aff2f9d626e4748f46ef4d0d41862296827a0297c98b1838aab7a2c63a13d673c6968a0d&mpshare=1&scene=1&srcid=0907vb4Oc8H0OnTOFrnSz1S3&sharer_sharetime=1599444114430&sharer_shareid=07754c1336c3524bfffedc4dc59111b6&key=590f90317dcde6d77276a67be4aa9f355e8fd09f6ce7455ae38318dd7ae135c86535691b0cc10ef1aa2ed6de4fd4a170837ac697738d8e751ef82fac541667f91862e9629cd67ae03c28827a05b32e4360919b96908f08ab124985ee8118ad2be66ea6f2a6ab1ed5db36e3d43ddeea0e5c66268e0a6dca187c0a7d5e53157482&ascene=1&uin=MTY5NjI3ODY2MQ%3D%3D&devicetype=Windows+10&version=62080079&lang=zh_CN&exportkey=AQGJANLVlCI61z6wOmt8o3Y%3D&pass_ticket=YY13S1MqsesGwRIKWOpqH4o63QMQRoax9pZLuVkff24rHKCEG4s4pQjrm4DtkwSf&wx_header=0)


## reference
[Spring事务“套路”面试](https://mp.weixin.qq.com/s?__biz=MzU0MzQ5MDA0Mw==&mid=2247487496&idx=3&sn=654d6af54ae61069920a65804baccd13)
1. 编程式事务、声明式事务
2. spring 代理：jdk 动态代理和 cglib
3. 只有 pulic 方法支持事务

 [Spring事务实现原理](https://mp.weixin.qq.com/s/wGUuMxc0GZZBUHBBaUf07w)
 原生的事务管理，比如 jdbc
##### spring 
 1. 事务管理器 PlatformTransactionManager
	 1. 受限于数据源，如 jdbc 对应的为 DatasourceTransactionManager
	 2. 只规定了事务的基本操作
		 1. 创建事务
		 2. 提价事务
		 3. 回滚事务
 2. 事务状态 TransactionStatus
	 1. 包含了事务对象，并且存储了事务的状态。
	 2. `TransactionStatus = PlatformTransactionManager.getTransaction()`
3. 事务属性的定义 TransactionDefinition
	1. 事务的传播等级和隔离级别定义在这

##### 编程式事务
使用 `TransactionTemplate` 和 `TransactionCallback` 这两个偏向用户层的接口。
##### AOP
AOP 实现机制
1. 依赖动态代理的方式达到对代理类增强的目的：Proxy-based
2. 字节码增强的方式达到增强的目的：Weaving-based
[spring 声明 AOP 方式也有两种](https://blog.csdn.net/u011983531/article/details/70504281)
1. Aspect
2. Advisor
事务是通过 Advisor 声明 AOP 的。
##### 动态代理
1. 目标类
	1. 被 Advisor 的 Pointcut 匹配(classFliter 匹配或是 methodMatches)到的类。
2. 代理类
	1. 在运行时直接创建，通过 jdk 或 cglib
	2. 在  [Bean 的初始化阶段](SpringBean生命周期.md#四个阶段)进行代理，在 `BeanPostProcessor.postProcessorAfterInitialization()` 中。
	3. 动态代理也有可能在实例化之前直接创建代理，这种情况发生在 `InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation()` 中，此时的实例化过程不再是我们上文介绍的通过简单反射创建对象。
##### spring 事务源码




 