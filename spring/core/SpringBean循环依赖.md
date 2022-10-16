# SpringBean 循环依赖
[spring bean作用域](springbean作用域.md#springbean作用域)
#### 依赖
1. 强依赖
	1. 构造函数
	2. 无法调节
2. 弱依赖
3.
[Spring中的循环依赖](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247492540&idx=2&sn=a09274b0a49013c3fec6ff34e2a3d10c)
[spring依赖注入](spring依赖注入.md)
##### 什么是循环依赖
1. A 注入 A
2. A 注入 B，B 注入 A
##### 什么情况的循环依赖可以被处理
1. Bean 必须是单例
2. 注入方式不能全是构造器注入，先注入的不能是构造器

### Spring 如何解决循环依赖
[Spring 如何解决循环依赖的问题](https://my.oschina.net/zhangxufeng/blog/3096394)
一个完整的对象包括两部分
1. 对象实例化
2. 对象属性实例化
在 spring 中对象实例化通过反射实现，对象属性实例化在之后完成。
##### Spring 创建 Bean 过程
1. 实例化，`AbstractAutowireCapableBeanFactory#createBeanInstance`,new 一个对象
2. 属性注入，`AbstractAutowireCapableBeanFactory#populateBean`, 填充属性
3. 初始化，`AbstractAutowireCapableBeanFactory#initializeBean`, 执行 aware 接口中的方法，完成 AOP 代理
Ps: [spring bean 的实例化包含了 jvm 对一个类的初始化->实例化过程](https://blog.csdn.net/qq_34869990/article/details/108554013)
#### 三个 map
1. singletonObjects
	1. 缓存实例化、注入、初始化完成的单例 bean
2. singletonFactory
	1. 缓存实例化完成的 bean
3. earlySingletonObjects
	1. 缓存 bean 的创建工厂 ObjectFactory 对象，以便于后面创建代理对象

##### earlySingletonObjects 为什么存放 ObjectFactory 对象而不是实例对象？
为了对实例对象进行增强，AOP

##### 流程
1. 从 singletonObjects 获取不到 A，创建 A
2. 放入earlySingletonObjects
3. A 依赖 B, 从 singletonObjects 获取不到 B
4. 创建 B 放入earlySingletonObjects
5. B 依赖 A, 从 singletonObjects 获取到 A, 并添加到singletonFactory
6. B 依赖注入 A 成功，完成初始化，放入singletonObjects
7. A 依赖注入 B 成功，完成初始化，放入singletonObjects
singletonFactory 的作用？
从 earlySingletonObjects 获取到的是 ObjectFactory 对象，它每次创建的对象可能不一样。
#### 案例
1. 两个作用域为原型的对象互相注入
	1. 可以，在 refresh（）-》finishBeanFactoryInitialization-》preInstantiateSingletons 中，只有非抽象、单例、非懒加载的类才能被提前初始 bean
	2. 假如一个单例 bean 参与进来，那就解决不了，因为没有用到缓存 map，每次都是新的对象。
2. 构造器注入
	1. 未使用缓存 map, 解决不了
	2. 可加@lazy 变相解决
	3. [SpringBoot构造器注入循环依赖及解决](https://blog.csdn.net/Revivedsun/article/details/84642316)
3. 具有@Async 修饰方法
	1. 不一定
	2. 初始化完成后会比对 singletonFactory 中对象与原始对象是否相等
	3. 默认情况下，spring是按照文件完整路径递归查找的，按路径+文件名排序，排在前面的先加载。
	4. [spring：我是如何解决循环依赖的？](https://mp.weixin.qq.com/s/fX4ze5YuLI5cqVaZhxq1tw)
	5. [@Async注解使用不当引发的spring循环依赖思考_个人文章 - SegmentFault 思否](https://segmentfault.com/a/1190000021217176
4. @DependsOn 修饰的属性注入方式的依赖注入
	1. 属性注入本可以通过三个 map 解决
	2. `AbstractBeanFactory`类的`doGetBean`会检查dependsOn的实例有没有循环依赖，如果有循环依赖则抛异常。
5. [一个非典型Spring循环依赖的问题分析](https://blog.mythsman.com/post/5d838c7c2db8a452e9b7082c/)



##### Reference
1.  [Spring 循环依赖的三种方式](https://mp.weixin.qq.com/s?__biz=MzUzMTA2NTU2Ng==&mid=2247487943&idx=2&sn=da1fbf42ff8125f0af129d06dd5e1954)
2.  [图解Spring解决循环依赖♻️ - 掘金](https://juejin.cn/post/6844904122160775176)

[Spring构造器注入循环依赖的解决方案及原理探索](https://www.jianshu.com/p/a178d84d2bcb)
Spring 构造器注入循环依赖的解决方案是@Lazy，其基本思路是：对于强依赖的对象，一开始并不注入对象本身，而是注入其代理对象，以便顺利完成实例的构造，形成一个完整的对象，这样与其它应用层对象就不会形成互相依赖的关系；当需要调用真实对象的方法时，通过 TargetSource 去拿到真实的对象 DefaultListableBeanFactory 的 doResolveDependency ，然后通过反射完成调用

##### Setter注入或者Field注入的循环依赖问题
三个 Map (earlySingletonObjects、singletonFactories、singletonObjects)
实际上并没有层次递进的关系，同一个对象同一时刻只会存在一个 Map 当中，如果想将对象放入另一个 Map，需要将对象从其余的 Map 中移除。因此不称呼三级缓存而是三个 map。









