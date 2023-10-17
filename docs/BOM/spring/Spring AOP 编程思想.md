## 第一章：Spring AOP 总览 (21 讲)

### 01 | 课程介绍


02 | 内容综述
时长 10:58

03 | 知识储备：基础、基础，还是基础！
时长 25:36

04 | AOP 引入：OOP 存在哪些局限性？
时长 10:01

05 | AOP 常见使用场景
时长 05:36
06 | AOP 概念：Aspect、Join Point 和 Advice 等术语应该如何理解？
时长 12:31
07 | Java AOP 设计模式：代理、判断和拦截器模式
时长 02:12
08 | Java AOP 代理模式（Proxy）：Java 静态代理和动态代理的区别是什么？
时长 12:06
09 | Java AOP 判断模式（Predicate）：如何筛选 Join Point？
时长 09:56
10 | Java AOP 拦截器模式（Interceptor）：拦截执行分别代表什么？
时长 19:29
11 | Spring AOP 功能概述：核心特性、编程模型和使用限制
时长 06:47
12 | Spring AOP 编程模型：注解驱动、XML 配置驱动和底层 API
时长 10:48
13 | Spring AOP 设计目标：Spring AOP 与 AOP 框架之间的关系是竞争还是互补？
时长 04:10
14 | Spring AOP Advice 类型：Spring AOP 丰富了哪些 AOP Advice 呢？
时长 04:59
15 | Spring AOP 代理实现：为什么 Spring Framework 选择三种不同 AOP 实现？
时长 06:03
16 | JDK 动态代理：为什么 Proxy. newProxyInstance 会生成新的字节码？
时长 14:58
17 | CGLIB 动态代理：为什么 Java 动态代理无法满足 AOP 的需要？
时长 15:54
18 | AspectJ 代理代理：为什么 Spring 推荐 AspectJ 注解？
时长 06:38
19 | AspectJ 基础：Aspect、Join Points、Pointcuts 和 Advice 语法和特性
时长 13:44
20 | AspectJ 注解驱动：注解能完全替代 AspectJ 语言吗？
时长 03:16
21 | 面试题精选
时长 02:15
## 第二章：Spring AOP 基础 (20 讲)
22 | Spring 核心基础：《小马哥讲 Spring 核心编程思想》还记得多少？
时长 08:38
23 | @AspectJ 注解驱动
时长 11:13
24 | 编程方式创建 @AspectJ 代理
时长 08:06
25 | XML 配置驱动 - 创建 AOP 代理
时长 12:13
26 | 标准代理工厂 API - ProxyFactory
时长 06:46
27 | @AspectJ Pointcut 指令与表达式：为什么 Spring 只能有限支持？
时长 11:18
28 | XML 配置 Pointcut
时长 08:30
29 | API 实现 Pointcut
时长 11:00
30 | @AspectJ 拦截动作：@Around 与@Pointcut 有区别吗？
时长 04:11
31 | XML 配置 Around Advice
时长 03:22
32 | API 实现 Around Advice
时长 04:48
33 | @AspectJ 前置动作：@Before 与@Around 谁优先级执行？
时长 12:21
34 | XML 配置 Before Advice
时长 06:10
35 | API 实现 Before Advice
时长 06:26
36 | @AspectJ 后置动作 - 三种 After Advice 之间的关系？
时长 05:53
37 | XML 配置三种 After Advice
时长 04:06
38 | API 实现三种 After Advice
时长 07:32
39 | 自动动态代理
时长 18:36
40 | 替换 TargetSource
时长 07:24
41 | 面试题精选
时长 03:44
## 第三章：Spring AOP API 设计与实现 (49 讲)
42 | Spring AOP API 整体设计
时长 12:10
43 | 接入点接口 - Joinpoint
时长 07:10
44 | Joinpoint 条件接口 - Pointcut
时长 07:41
45 | Pointcut 操作 - ComposablePointcut
时长 04:48
46 | Pointcut 便利实现
时长 07:58
47 | Pointcut AspectJ 实现 - AspectJExpressionPointcut
时长 08:14
48 | Joinpoint 执行动作接口 - Advice
时长 04:31
49 | Joinpoint Before Advice 标准实现
时长 06:49
50 | Joinpoint Before Advice AspectJ 实现
时长 10:48
51 | Joinpoint After Advice 标准实现
时长 22:36
52 | Joinpoint After Advice AspectJ 实现
时长 17:44
53 | Advice 容器接口 - Advisor
时长 05:37
54 | Pointcut 与 Advice 连接器 - PointcutAdvisor
时长 10:56
55 | Introduction 与 Advice 连接器 - IntroductionAdvisor
时长 16:09
56 | Advisor 的 Interceptor 适配器 - AdvisorAdapter
时长 05:55
57 | AdvisorAdapter 实现
时长 11:12
58 | AOP 代理接口 - AopProxy
时长 06:17
59 | AopProxy 工厂接口与实现
时长 09:26
60 | JDK AopProxy 实现 - JdkDynamicAopProxy
时长 23:13
61 | CGLIB AopProxy 实现 - CglibAopProxy
时长 20:47
62 | AopProxyFactory 配置管理器 - AdvisedSupport
时长 19:09
63 | Advisor 链工厂接口与实现 - AdvisorChainFactory
时长 18:50
64 | 目标对象来源接口与实现 - TargetSource
时长 12:31
65 | 代理对象创建基础类 - ProxyCreatorSupport
时长 07:17
66 | AdvisedSupport 事件监听器 - AdvisedSupportListener
时长 13:50
67 | ProxyCreatorSupport 标准实现 - ProxyFactory
时长 05:49
68 | ProxyCreatorSupport IoC 容器实现 - ProxyFactoryBean
时长 15:24
69 | ProxyCreatorSupport AspectJ 实现 - AspectJProxyFactory
时长 38:15
70 | IoC 容器自动代理抽象 - AbstractAutoProxyCreator
时长 07:29
71 | IoC 容器自动代理标准实现
时长 15:18
72 | IoC 容器自动代理 AspectJ 实现 - AspectJAwareAdvisorAutoProxyCreator
时长 18:44
73 | AOP Infrastructure Bean 接口 - AopInfrastructureBean
时长 11:09
74 | AOP 上下文辅助类 - AopContext
时长 10:10
75 | 代理工厂工具类 - AopProxyUtils
时长 05:13
76 | AOP 工具类 - AopUtils
时长 13:18
77 | AspectJ Enable 模块驱动实现 - @EnableAspectJAutoProxy
时长 16:38
78 | AspectJ XML 配置驱动实现 - <aop: aspectj-autoproxy/>
时长 10:52
79 | AOP 配置 Schema-based 实现 - <aop: config/>
时长 14:49
80 | Aspect Schema-based 实现 - <aop: aspect/>
时长 07:43
81 | Pointcut Schema-based 实现 - <aop: pointcut/>
时长 11:58
82 | Around Advice Schema-based 实现 - <aop: around/>
时长 24:25
83 | Before Advice Schema-based 实现 - <aop: before/>
时长 04:58
84 | After Advice Schema-based 实现 - <aop: after/>
时长 06:45
85 | After Returning Advice Schema-based 实现 - <aop: after-returning/>
时长 05:12
86 | After Throwing Advice Schema-based 实现 - <aop: after-throwing/>
时长 04:35
87 | Adviser Schema-based 实现 - <aop: advisor/>
时长 09:34
88 | Introduction Schema-based 实现 - <aop: declare-parents/>
时长 06:04
89 | 作用域代理 Schema-based 实现 - <aop: scoped-proxy/>
时长 16:24
90 | 面试题精选
时长 13:38
## 第四章：Spring AOP 设计模式 (17 讲)
91 | 抽象工厂模式（Abstract factory）实现
时长 09:05
92 | 构建器模式（Builder）实现
时长 08:19
### 93 | 工厂方法模式（Factory method）实现
时长 05:23
### 94 | 原型模式（Prototype）实现
时长 07:56
### 95 | 单例模式（Singleton）实现
时长 06:44
### 96 | 适配器模式（Adapter）实现
时长 07:39
### 97 | 组合模式（Composite）实现
时长 05:08
### 98 | 装饰器模式（Decorator）实现
时长 07:28
### 99 | 享元模式（Flyweight）实现
时长 05:24
### 100 | 代理模式（Proxy）实现
时长 04:25
### 101 | 模板方法模式（Template Method）实现
时长 06:24
### 102 | 责任链模式（Chain of Responsibility）实现
时长 04:31
### 103 | 观察者模式（Observer）实现
时长 04:20
### 104 | 策略模式（Strategy）实现
时长 03:44
### 105 | 命令模式（Command）实现
时长 05:53
### 106 | 状态模式（State）实现
时长 04:34
### 107 | 面试题精选
时长 08:00
## 第五章：Spring AOP 在 Spring Framework 内部应用 (7 讲)
### 108 | Spring AOP 在 Spring 事件（Events）
时长 25:14
### 109 | Spring AOP 在 Spring 事务（Transactions）理论基础
时长 25:17
### 110 | Spring AOP 在 Spring 事务（Transactions）源码分析
时长 18:58
### 111 | Spring AOP 在 Spring 缓存（Caching）
时长 11:52
### 112 | Spring AOP 在 Spring 本地调度（Scheduling）
时长 13:32
### 113 | 面试题精选
时长 11:01
### 114 | 结束语
时长 27:28