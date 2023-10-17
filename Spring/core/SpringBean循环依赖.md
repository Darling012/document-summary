# SpringBean 循环依赖


## Spring 中存在的循环依赖现象
#### 依赖
1. 强依赖
	1. 构造函数
	2. 无法调节
2. 弱依赖

### 什么是循环依赖


1. 两个作用域为原型的对象互相注入
	1. 可以，在 refresh（）-》finishBeanFactoryInitialization-》preInstantiateSingletons 中，只有非抽象、非懒加载、单例的类才能被提前初始 bean
	2. 假如一个单例 bean 参与进来，那就解决不了，因为没有用到缓存 map，每次都是新的对象。
2. 构造器注入
	1. 未使用缓存 map, 解决不了
	2. 可加@lazy 变相解决
3. 具有@Async 修饰方法
	1. 不一定
	2. 初始化完成后会比对 singletonFactory 中对象与原始对象是否相等
	3. 默认情况下，spring 是按照文件完整路径递归查找的，按路径+文件名排序，排在前面的先加载。
	4. [spring：我是如何解决循环依赖的？]
	5. [@Async 注解使用不当引发的 spring 循环依赖思考_个人文章 - SegmentFault 思否]( https://segmentfault.com/a/1190000021217176
4. @DependsOn 修饰的属性注入方式的依赖注入
	1. 属性注入本可以通过三个 map 解决
	2. `AbstractBeanFactory` 类的 `doGetBean` 会检查 dependsOn 的实例有没有循环依赖，如果有循环依赖则抛异常。
5. [一个非典型Spring循环依赖的问题分析](https://blog.mythsman.com/post/5d838c7c2db8a452e9b7082c/)




#### Setter 注入或者 Field 注入的循环依赖问题

#### 构造器注入
[Spring构造器注入循环依赖的解决方案及原理探索](https://www.jianshu.com/p/a178d84d2bcb)
Spring 构造器注入循环依赖的解决方案是@Lazy，其基本思路是：对于强依赖的对象，一开始并不注入对象本身，而是注入其代理对象，以便顺利完成实例的构造，形成一个完整的对象，这样与其它应用层对象就不会形成互相依赖的关系；当需要调用真实对象的方法时，通过 TargetSource 去拿到真实的对象 DefaultListableBeanFactory 的 doResolveDependency ，然后通过反射完成调用

[SpringBoot构造器注入循环依赖及解决](https://blog.csdn.net/Revivedsun/article/details/84642316)


## Spring 如何解决循环依赖






#### 三个 map

1. singletonObjects
	1. 缓存实例化、注入、初始化完成的单例 bean
2. earlySingletonObjects
	1. 缓存实例化完成的 bean，未进行属性注入及初始化
3. singletonFactory
	1. 缓存 bean 的创建工厂 ObjectFactory 对象，以便于后面创建代理对象。第二个 map 从这个工厂获取对象

三个 Map (earlySingletonObjects、singletonFactories、singletonObjects)
实际上并没有层次递进的关系，同一个对象同一时刻只会存在一个 Map 当中，如果想将对象放入另一个 Map，需要将对象从其余的 Map 中移除。因此不称呼三级缓存而是三个 map。



##### [流程](https://mp.weixin.qq.com/s/fX4ze5YuLI5cqVaZhxq1tw)
![](Pasted%20image%2020221027174539.png)

##### 为什么是三个map
1. 如果只有两个 map，当 bean 被代理，那么返回的是早期对象，不是代理对象。
2. singletonFactory 的作用？
	1. 从 earlySingletonObjects 获取到的是 ObjectFactory 对象，它每次创建的对象可能不一样。
3. singletonFactory 为什么存放 ObjectFactory 对象而不是实例对象？
	1. 为了对实例对象进行增强，AOP

## Reference
 [Spring 循环依赖的三种方式](https://mp.weixin.qq.com/s?__biz=MzUzMTA2NTU2Ng==&mid=2247487943&idx=2&sn=da1fbf42ff8125f0af129d06dd5e1954)
1. 构造器参数循环依赖
2. setter 方式单例，默认方式
3. setter 方式原型，prototype
	1. 


[Spring中的循环依赖](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247492540&idx=2&sn=a09274b0a49013c3fec6ff34e2a3d10c)
1. 什么是循环依赖？
	1. A 注入 A
	2. A 注入 B，B 注入 A
2. 什么情况下循环依赖可以被处理？
	1. 出现循环依赖的Bean必须要是单例
	2. 注入方式不能全是构造器注入，[先注入的不能是构造器](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247492483&idx=2&sn=38a897c91452945dec976bd4a9583cc8)
3. Spring 是如何解决的循环依赖？
	1. 简单的循环依赖（没有 AOP）
		1. Spring 按照 `order` 顺序进行创建 bean
		2. 创建 bean 分三步
			1. 实例化，`AbstractAutowireCapableBeanFactory#createBeanInstance`,new 一个对象
			2. 属性注入，`AbstractAutowireCapableBeanFactory#populateBean`, 填充属性
			3. 初始化，`AbstractAutowireCapableBeanFactory#initializeBean`, 执行 aware 接口中的方法，完成 AOP 代理
			4. ps：[spring bean 的实例化包含了 jvm 对一个类的初始化->实例化过程](https://blog.csdn.net/qq_34869990/article/details/108554013)
			5. ps: [Spring 创建 Bean 过程](SpringBean生命周期.md#SpringBean%20生命周期)
		3. 循环依赖处理过程
			1. 创建 bean 会 `getSingleton(beanName)`，然后又调 `getSingleton(beanName, true)`，尝试去三个 map 取
				1. singletonObjects：缓存实例化、注入、初始化完成的单例 bean
				2. earlySingletonObjects：缓存实例化完成的 bean，未进行属性注入及初始化
				3. singletonFactory：缓存 bean 的创建工厂 ObjectFactory 对象，以便于后面创建代理对象。第二个 map 从这个工厂获取对象
			2. 因为第一次创建 A，所以都取不到，会进入另外一个重载方法 `getSingleton(beanName, singletonFactory)`
				1. 在完成 Bean 的实例化后，属性注入之前 Spring 将 Bean 包装成一个工厂添加进了三级缓存中
				2. #todo
	2. 结合了 AOP 的循环依赖


[图解Spring解决循环依赖♻️ - 掘金](https://juejin.cn/post/6844904122160775176)
1. **原型**(Prototype)的场景是**不支持**循环依赖的，通常会走到 `AbstractBeanFactory` 类中下面的判断，抛出异常。
	1. `if (isPrototypeCurrentlyInCreation(beanName)) {     throw new BeanCurrentlyInCreationException(beanName);   }`


[Spring 如何解决循环依赖](https://mp.weixin.qq.com/s/djVu6SJjfYj4y_LNW-SKyw)
![](Pasted%20image%2020230921141907.png)
1. 从源码讲了完整的过程，很细
2. 为什么要有三级缓存
	1. 一级缓存 `singletonObjects`，结构是 `Map<String, Object>`，它就是一个单例池，将初始化好的对象放到里面，给其它线程使用，如果没有第一级缓存，程序不能保证 Spring 的单例属性。
	2. 三级缓存 `singletonFactories`，结构是 `Map<String, ObjectFactory<?>>`，主要作用是存放半成品的单例 Bean，目的是为了“打破循环”。
		1. ObjectFactory判断是否要 AOP，决定返回原对象还是代理对象
		2. 那么二级缓存就是存放 ObjectFactory 生成的对象的
3. 能去掉二级缓存么
	1. 不能，不把 ObjectFactory 生成的对象的对象缓存起来，那么有多个地方依赖此对象，ObjectFactory 就会生成多次






[Spring 如何解决循环依赖的问题](https://my.oschina.net/zhangxufeng/blog/3096394) 
一个完整的对象包括两部分
1. 对象实例化
2. 对象属性实例化
在 spring 中对象实例化通过反射实现，对象属性实例化在之后完成。