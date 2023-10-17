# Spring Bean

## spring bean 作用域
1. singleton
	1. 一个 beanFactory 有且仅有一个实例
	2. 主要通过 beanDefinition 中的 isSingleton ()
2. prototype
	1. 原型作用域，每次 DI 和 DL 注入都会生成新的 Bean 对象
	2. spring 没有办法管理其完整生命周期，也没有办法记录实例的存在。一旦 DI 或 DL 完成后就与 spring 脱钩，只有它的 beanDefinition。销毁方法不会执行，可以利用 BeanPostProcessor 进行清扫工作。
3. request
	1. 将 spring bean 存储在 servletRequest 的上下文中
4. session
	1. 将 spring bean 存储在 session 中
5. application
	1. 将 spring bean 存储在 servletContext 中

其中
1. singleton 和 prototype 可能同时存在
2. request、session、application 主要应用在服务端页面渲染中，如 jsp、模版引擎

## Spring DI
1. 构造器注入
2. 属性注入
3. setter 方法注入
4. 
[2014-03-10 Spring的学习(2)------依赖注入和自动扫描Bean - 炉台的个人空间 - OSCHINA - 中文开源技术交流社区](https://my.oschina.net/codeWatching/blog/206824)
[原始方式：spring四种依赖注入方式 |注解方式：Spring零配置通过注解实现Bean依赖注入... - 豆奶特](https://www.dounaite.com/article/62c1e6f4f4ab41be486aba92.html)
##### ObjectProvider
1. [Spring Boot 注解之ObjectProvider源码追踪 - 掘金](https://juejin.cn/post/6844904009556312077)
2. [Spring杂谈 | 什么是ObjectFactory？什么是ObjectProvider？ - 腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1656165)

##### 注解区别
[@Autowired、@Resource和@Inject的区别 - 掘金](https://juejin.cn/post/6844904158252761102)
[@Autowire和@Resource使用的区别在哪?](https://mp.weixin.qq.com/s/-EZL0ERsfDGkN0L-FsQ3uw)

##### 注入方式选择
1. [为什么IDEA不推荐你使用@Autowired ？](https://mp.weixin.qq.com/s/nSP6DKNLyL_5FmSbd_NywQ)
2. [Spring官方为什么建议构造器注入？](https://mp.weixin.qq.com/s/I6cjOXjlhvM5lv0v7O6kLA)
3. [【Spring】浅谈spring为什么推荐使用构造器注入 - 腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1126880)

##### 备忘点
1. [Spring内部类注入时构造函数踩坑](https://www.jianshu.com/p/7a662ea860be)


[spring中竟然有12种定义bean的方法](https://mp.weixin.qq.com/s/-gFK9X9j1rvl3vKqTUEDmA)


## Spring IOC

1. [Spring bean依赖注入、bean的装配及相关注解 - 风一样的码农 - 博客园](https://www.cnblogs.com/chenpi/p/6222595.html)
2. [SpringIOC初始化过程--详解 - 小毛毛--专注后端 - 博客园](https://www.cnblogs.com/zgwjava/p/10839732.html)
3. [IOC控制反转前的处理](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247486449&idx=1&sn=00e3bb161f6ca5f19f1043b036a1d8b2)
4. [Spring IOC 容器源码分析系列文章导读_慕课手记](https://www.imooc.com/article/31807)
5. [Spring IOC 容器源码分析_Javadoop](https://javadoop.com/post/spring-ioc)


 [面向切面编程](https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650246879&idx=1&sn=a5da5e3fc05778117674f65aaed40106)
 1. 什么是 IOC
	 1. inversion of control 控制反转，面向对象的设计原则，用来降低耦合度。 

[彻底理解SpringIOC、DI-这篇文章就够了 - 掘金](https://juejin.cn/post/6844903715602694152)
Spring 在初始化 Singleton 的时候大致可以分几步，初始化——设值——销毁，循环依赖的场景下只有 A——B——A 这样的顺序，但在并发的场景下，每一步在执行时，都有可能调用 getBean 方法，而单例的 Bean 需要保证只有一个 instance，那么 Spring 就是通过这些个缓存外加对象锁去解决这类问题，同时也可以省去不必要的重复操作。