# SpringBean 生命周期
##### 流程
[Spring Bean的生命周期（非常详细） - Chandler Qian - 博客园](https://www.cnblogs.com/zrtqsk/p/3735273.html)
[深究Spring中Bean的生命周期](https://mp.weixin.qq.com/s?__biz=MzI4Njc5NjM1NQ==&mid=2247488494&idx=1&sn=1ad8b2b4313b173b431a4e4943423f04)
1. 实例化 BeanFactoryPostProcessor 实现类
2. 执行 BeanFactoryPostProcessor 的 postProcessBeanFactory 方法
3. 实例化 BeanPostProcessor 实现类
4. 实例化 <font color=#13C6C3>InstantiationAwareBeanPostProcessor</font>Adapter 实现类
5. 执行 <font color=#13C6C3>InstantiationAwareBeanPostProcessor</font> 的 postProcessBeforeInstantiation 方法
6. 执行 bean 的构造器
7. 执行 <font color=#13C6C3>InstantiationAwareBeanPostProcessor</font> 的 postProcessPropertyValues 方法
8. 为 bean 注入属性
9. 调用 BeanNameAware 的 setBeanName 方法
10. 调用 BeanFactoryAware 的 setBeanFactory 方法
11. 执行 BeanPostProcessor 的 postProcessBeforeInstantiation 方法
12. 调用 InitializingBean 的 afterPropertiesSet 方法
13. 调用 `<bean>` 的 init-method 属性指定的初始化方法
14. 执行 BeanPostProcessor 的 postProcessAfterInstantiation 方法
15. 执行 <font color=#13C6C3>InstantiationAwareBeanPostProcessor</font> 的postProcessAfterInstantiation
16. 容器初始化成功，程序执行，业务逻辑调用，销毁容器
17. 调用 DisposableBean 的 destory 方法
18. 调用`<bean>` 的 destroy-method 属性指定的初始化方法


[请别再问Spring Bean的生命周期了！](https://www.jianshu.com/p/1dec08d290c1)
##### 四个阶段
1. 实例化
2. 属性赋值
3. 初始化
4. 销毁
在这四步之间穿插各种扩展点，其中初始化和销毁可自定义。
主要逻辑都在 doCreate () 方法中按顺序调用以下三个方法
1. createBeanInstance () 
	1. 实例化阶段
2. populateBean () 
	1. 属性赋值阶段
3. initializeBean () 
	1. 初始化阶段
4. ConfigurableApplicationContext#close()
	1. 销毁在容器关闭时调用
##### 常用扩展点
1. 影响多个 bean
	1. InstantiationAwareBeanPostProcessor
		1. 作用于实例化前后
		2. postProcessBeforeInstantiation
		3. postProcessAfterInstantiation
		4. 继承了BeanPostProcessor
		5. [postProcessBeforeInstantiation调用点](SpringBean生命周期.md#^lfo0j6),postProcessBeforeInstantiation 在 doCreateBean 之前调用，也就是在 bean 实例化之前调用的。
		7. [postProcessAfterInstantiation调用点](SpringBean生命周期.md#^z2nrmv), 在 populateBean（）属性赋值方法内，在真正赋值操作前，返回 false 可以阻断赋值阶段，spring aop在此 替换对象。
	2. BeanPostProcessor
		1. 作用于初始化前后
		2. postProcessBeforeInstantiation
		3. postProcessAfterInstantiation
2. 只调用一次的接口
	1. Aware 类型接口
		1. 作用是拿到 spring 容器的一些资源，基本见名知意，[调用时机](SpringBean生命周期.md#^rev8sy)
		2. 在初始化阶段方法 initializeBean () 的 invokeAwareMethods 方法内调用
			1. BeanNameAware
			2. BeanClassLoaderAware
			3. BeanFactoryAware
		3. 在初始化阶段方法 initializeBean () 的 applyBeanPostProcessorsBeforeInitialization 方法内调用
			1. EnvironmentAware
			2. EmbeddedValueResolverAware 
			3. ApplicationContextAware（ResourceLoaderAware\ApplicationEventPublisherAware\MessageSourceAware）返回值实质上都是当前的ApplicationContext对象
			4. 调用方式与 Bean××Aware 不同，通过 BeanPostProcessor 实现
	2. 生命周期接口
		1. InitializingBean
			1. 对应生命周期初始化阶段，
		2. DisposableBean
			1. 对应生命周期的销毁阶段
			2. 以ConfigurableApplicationContext#close()方法作为入口
			3. 实现是通过循环取所有实现了DisposableBean接口的Bean然后调用其destroy()方法 

##### 各种接口方法分类
Bean的完整生命周期经历了各种方法调用，这些方法可以划分为以下几类：
1. Bean 自身的方法：这个包括了 Bean 本身调用的方法和通过配置文件中 `<bean>` 的 init-method 和 destroy-method 指定的方法
2. Bean 级生命周期接口方法：这个包括了 BeanNameAware、BeanFactoryAware、InitializingBean 和 DisposableBean 这些接口的方法
3. 容器级生命周期接口方法：这个包括了 InstantiationAwareBeanPostProcessor 和 BeanPostProcessor 这两个接口实现，一般称它们的实现类为“后处理器”。
4. 工厂后处理器接口方法：这个包括了 AspectJWeavingEnabler, ConfigurationClassPostProcessor, CustomAutowireConfigure 等等非常有用的工厂后处理器接口的方法。工厂后处理器也是容器级的。在应用上下文装配配置文件之后立即调用。

##### BeanPostProcessor 注册时机与执行顺序
BeanPostProcessor 也会注册为 Bean，那么 Spring 是如何保证 BeanPostProcessor 在我们的业务 Bean 之前初始化完成呢？
在 refrence () 中，Spring 是先执行 registerBeanPostProcessors () 进行 BeanPostProcessors 的注册，然后再执行 finishBeanFactoryInitialization 初始化我们的单例非懒加载的 Bean。
每个 BeanPostProcessor 都影响多个 Bean，其执行顺序至关重要，如何控制
1. PriorityOrdered是一等公民，首先被执行，PriorityOrdered公民之间通过接口返回值排序
2. Ordered是二等公民，然后执行，Ordered公民之间通过接口返回值排序
3. 都没有实现是三等公民，最后执行

##### jvm 规范中类的初始化与实例化和 spirng 中 bean 的实例化与初始化的区别
1. [JVM类 和 spring Bean 的实例化 和 初始化区别以及顺序_秋楓_Lance的博客-CSDN博客_spring初始化和实例化的区别](https://blog.csdn.net/qq_34869990/article/details/108554013)
2. [[jvm规范中类的初始化与实例化]和[spirng中bean的实例化与初始化]的区别_chuixue24的博客-CSDN博客_bean的实例化和初始化的区别](https://blog.csdn.net/chuixue24/article/details/103696096)






## refrence
7. [@PostConstruct注解是Spring提供的](https://mp.weixin.qq.com/s?__biz=MzI0NDAzMzIyNQ==&mid=2654071225&idx=1&sn=c65be97b46e94a9790eb6821de3e4af1)
9. [Spring源码----Spring的Bean生命周期流程图及代码解释 - 简书](https://www.jianshu.com/p/70b935f2b3fe)
10. [Bean加载流程概览](https://mp.weixin.qq.com/s?__biz=MzAxOTQxOTc5NQ==&mid=2650499766&idx=1&sn=686d6a24251916b5c7ae82a1a2696b52)
11. [天天用 Spring，bean 实例化原理你懂吗？ - Java技术栈 - 博客园](https://www.cnblogs.com/javastack/p/13431216.html)


[bean 实例化原理](https://mp.weixin.qq.com/s?__biz=MzI3ODcxMzQzMw==&mid=2247502636&idx=2&sn=4cc270e2b49f13d289fc0b3362a56902)
1. 在实例化 bean 之前 BeanDefinition 里已经有了所有需要实例化时用到的元数据。接下来是选择实例化方法及策略
2. 实例化方法有两大类，spring 默认的实例化方法是无参构造函数实例化。
	1. 工厂方法
		1. 静态工厂方法
		2. 实例工厂方法
	2. 构造函数方法实例化
		1. 无参构造函数实例化（默认）
		2. 有参构造函数实例化
3. 实例化策略
	1. 反射
	2. cglib
4. 工厂方法的实例化手段没有选择实例化策略，直接使用反射实现。
5. 实例化策略都是针对构造函数实例化而言的。
6. 具体选择哪种策略是根据 BeanDefinition 里的定义决定的。





postProcessBeforeInstantiation调用点 ^lfo0j6
```java
@Override
    protected Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
            throws BeanCreationException {

        try {
            // Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
            // postProcessBeforeInstantiation方法调用点，这里就不跟进了，
            // 有兴趣的同学可以自己看下，就是for循环调用所有的InstantiationAwareBeanPostProcessor
            Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
            if (bean != null) {
                return bean;
            }
        }
        
        try {   
            // 上文提到的doCreateBean方法，可以看到
            // postProcessBeforeInstantiation方法在创建Bean之前调用
            Object beanInstance = doCreateBean(beanName, mbdToUse, args);
            if (logger.isTraceEnabled()) {
                logger.trace("Finished creating instance of bean '" + beanName + "'");
            }
            return beanInstance;
        }
        
    }
```

postProcessAfterInstantiation调用点 ^z2nrmv
```java
protected void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) {

   // Give any InstantiationAwareBeanPostProcessors the opportunity to modify the
   // state of the bean before properties are set. This can be used, for example,
   // to support styles of field injection.
   boolean continueWithPropertyPopulation = true;
    // InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation()
    // 方法作为属性赋值的前置检查条件，在属性赋值之前执行，能够影响是否进行属性赋值！
   if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
      for (BeanPostProcessor bp : getBeanPostProcessors()) {
         if (bp instanceof InstantiationAwareBeanPostProcessor) {
            InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
            if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
               continueWithPropertyPopulation = false;
               break;
            }
         }
      }
   }

   // 忽略后续的属性赋值操作代码
}
```
Aware 调用时机 ^rev8sy
```java
    // 见名知意，初始化阶段调用的方法
    protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {

        // 这里调用的是Group1中的三个Bean开头的Aware
        invokeAwareMethods(beanName, bean);

        Object wrappedBean = bean;
        
        // 这里调用的是Group2中的几个Aware，
        // 而实质上这里就是前面所说的BeanPostProcessor的调用点！
        // 也就是说与Group1中的Aware不同，这里是通过BeanPostProcessor（ApplicationContextAwareProcessor）实现的。
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
        // 下文即将介绍的InitializingBean调用点
        invokeInitMethods(beanName, wrappedBean, mbd);
        // BeanPostProcessor的另一个调用点
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

        return wrappedBean;
    }
```




[掌握Spring中11个扩展点](https://mp.weixin.qq.com/s/zXRIpRoSiokerCVaiZLqKw)

[不贴代码，Spring的Bean实例化过程应该是怎样的？ - 掘金](https://juejin.cn/post/6929672218322731022)