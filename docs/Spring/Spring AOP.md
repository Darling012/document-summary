# Spring aop


spring aop 在哪里为哪些 bean 生成代理类进行 aop
[Spring AOP中如何为Bean创建代理？](https://blog.51cto.com/u_11966318/5377709)
AbstractAutoProxyCreator 的 [postProcessAfterInstantiation](SpringBean生命周期.md#^z2nrmv) 方法中我们可以看到其会尝试对 Bean 进行代理。
[[十一]Spring AOP - 代理入口类 | 叶良辰の学习笔记](https://yangzhiwen911.github.io/zh/spring/%5B11%5Daop%E5%85%A5%E5%8F%A3%E5%8F%8Aaop%E4%B8%AD%E7%9A%84%E5%90%84%E7%A7%8Dadvice%E5%92%8Cadvisor.html#%E6%98%AF%E5%90%A6%E7%94%9F%E6%88%90%E4%BB%A3%E7%90%86)
[spring 代理](spring%20代理.md)


## AOP 失效
### Spring自调用问题

[完整剖析SpringAOP的自调用](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247488888&idx=2&sn=d2e427bddfce50859a9175cd5c91d1f5)    
1. [AOP的自调用问题_hankXusLying的博客-CSDN博客_aop 自调用](https://blog.csdn.net/weixin_42146026/article/details/112346768)
2. AOP 与 spring AOP
3. 解决自调用问题
	1. 注入代理 bean 到自己，使用@Lazy 解决循环依赖
	2. `AopContext.currentProxy()` 获取当前代理对象
	3. 直接使用 AspectJ

![Spring 事务](Spring%20事务.md#spring%20事务失效)
## reference
1. 
7. [从 Spring 集成 MyBatis 到浅析 Java 动态代理](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247487429&idx=1&sn=fc27ae265af62e8b58f98e74165f3818)
9. [彻底剖析清楚Spring AOP](https://mp.weixin.qq.com/s/-nTo8Y8V1plK4WanODsXCQ)



[SpringAOP实现原理与CGLIB应用 - 叶落](https://www.yelcat.cc/index.php/archives/965/)
1. AOP 面向对象的补充，应用于处理具有横切性质的系统级服务。
2. AOP 存在的价值
3. 使用 AspectJ 的编译时增强进行 AOP
4. 使用 Spring AOP
5. Spring AOP 原理剖析


[关于 Spring AOP (AspectJ) 你该知晓的一切_zejian_的博客-CSDN博客](https://blog.csdn.net/javazejian/article/details/56267036)
1. OOP、POP、AOP


[Spring AOP是什么？你都拿它做什么？](https://mp.weixin.qq.com/s?__biz=MzAxOTQxOTc5NQ==&mid=2650499734&idx=1&sn=ee5262ed17da1fbc9da8c3312414277f)
1. 代理模式
2. 静态代理
3. 动态代理
4. spring aop
	1. 如果有接口，则使用 JDK 代理，反之使用 CGLIB
	2. 如果目标类没有实现接口，且 class 为 final 修饰的，则不能进行 Spring AOP 编程

[Spring AOP](https://mp.weixin.qq.com/s?__biz=MzUzMTA2NTU2Ng==&mid=2247488651&idx=2&sn=b4e4098df34b66c881d2d47aaf6c69fb)
切面、连接点等

[spring @EnableAspectJAutoProxy背后的那些事(spring AOP源码赏析) - 在山的那边 - 博客园](https://www.cnblogs.com/foreveravalon/p/8653832.html)

