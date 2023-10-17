# spring 核心编程思想
[小马哥讲Spring核心编程思想_tobebetter9527的博客-CSDN博客](https://blog.csdn.net/qq_39530821/category_10189825.html)

## 第一章：Spring Framework 总览 (12 讲)

01 | 课程介绍
时长 05:40

02 | 内容综述
时长 19:07

03 | 课前准备：学习三件套（工具、代码与大脑）
时长 05:53

04 | 特性总览：核心特性、数据存储、Web 技术、框架整合与测试
时长 11:59

05 | Spring 版本特性：Spring 各个版本引入了哪些新特性？
时长 05:56
06 | Spring 模块化设计：Spring 功能特性如何在不同模块中组织？
时长 08:44
07 | Java 语言特性运用：各种 Java 语法特性是怎样被 Spring 各种版本巧妙运用的？
时长 08:41
08 | JDK API 实践：Spring 怎样取舍 Java I/O、集合、反射、动态代理等 API 的使用？
时长 17:19
09 | Java EE API 整合：为什么 Spring 要与“笨重”的 Java EE 共舞？
时长 09:10

### 10 丨 Spring 编程模型：Spring 实现了哪些编程模型？
```
file:///Volumes/resources/小马哥/Spring核心编程思想/第一章：Spring Framework总览 (12讲)/10丨Spring编程模型：Spring实现了哪些编程模型？.mp4
```
1. 面向对象编程
2. 面向切面编程
3. 面相元数据编程
4. 函数驱动
5. 模块驱动

11 | Spring 核心价值：我们能从 Spring Framework 中学到哪些经验和教训呢？
时长 07:53
12 | 面试题精选
## 第二章：重新认识 IoC (9 讲)
### 13 | IoC 发展简介：你可能对 IoC 有些误会？
时长 06:02
### 14 | IoC 主要实现策略：面试官总问 IoC 和 DI 的区别，他真的理解吗？
时长 04:24
### 15 | IoC 容器的职责：IoC 除了依赖注入，还涵盖哪些职责呢？
时长 06:02
### 16 | 除了 Spring，还有其它的 IOC 容器实现吗？
时长 04:56
### 17 | 传统 IoC 容器实现：JavaBeans 也是 IoC 容器吗？
时长 17:56
### 18 | 轻量级 IoC 容器：如何界定 IoC 容器的“轻重”？
时长 05:17
### 19 | 依赖查找 VS. 依赖注入：为什么 Spring 总会强调后者，而选择性忽视前者？
时长 03:33
### 20 | 构造器注入 VS. Setter 注入：为什么 Spring 官方文档的解读会与作者的初心出现偏差？
时长 13:12
### 21 | 面试题精选


## 第三章：Spring IoC 容器概述 (9 讲)

### 22 丨 Spring IoC 依赖查找：依赖注入还不够吗？依赖查找存在的价值几何？
```
file:///Volumes/resources/小马哥/Spring核心编程思想/第三章：Spring IoC容器概述 (9讲)/22丨Spring IoC依赖查找：依赖注入还不够吗？依赖查找存在的价值几何？.mp4
```
1. 根据 bean 名称查找
	1. 实时查找
	2. 延迟查找  objectFactory
2. 根据 bean 类型查找
	1. 单个bean
	2. 集合 bean (查找到多个 bean)
3. bean 名称+类型
4. 根据 Java 注解
### 23 丨 Spring IoC 依赖注入：Spring 提供了哪些依赖注入模式和类型呢?
```
file:///Volumes/resources/小马哥/Spring核心编程思想/第三章：Spring IoC容器概述 (9讲)/23丨Spring IoC依赖注入：Spring提供了哪些依赖注入模式和类型呢？.mp4
```

1. 根据 bean 名称 `<proper name=''>` 硬编码 手动   bean 按配置配置顺序
2. 根据 bean 类型 auto-write 自动 顺序不可调
	1. 单个
	2. 集合
3. 注入容器内建 bean 对象
	1. 依赖注入和依赖查找 bean 来源不一样
4. 注入非 bean 对象
	1. 内建依赖
5. 注入类型
	1. 实时
	2. 延迟

### 24 丨 Spring IoC 依赖来源：依赖注入和查找的对象来自于哪里？
```
file：///Volumes/resources/小马哥/Spring核心编程思想/第三章：Spring IoC容器概述 (9讲)/24丨Spring IoC依赖来源：依赖注入和查找的对象来自于哪里？.mp4
```
1. 自定义 bean
2. 容器内建 bean 对象，environment
3. 容器内建依赖 baenfactory

### 25 丨 Spring IoC 配置元信息：Spring IoC 有哪些配置元信息？它们的进化过程是怎样的？
```
file:///Volumes/resources/小马哥/Spring核心编程思想/第三章：Spring IoC容器概述 (9讲)/25丨Spring IoC配置元信息：Spring IoC有哪些配置元信息？它们的进化过程是怎样的？.mp4
```
1. bean 定义配置
	1. 基于 xml
	2. grovvyDSL
		1. 基于 properties 文件
		2. 基于 Java 注解
2. IOC 容器配置
	1. 基于 xml
	2. 基于注解
	3. JavaAPI
3. 外部化配置
	1. Java 注解
	2. @Value
### 26 丨 Spring IoC 容器：BeanFactory 和 ApplicationContext 谁才是 Spring IoC 容器？
```
file:///Volumes/resources/小马哥/Spring核心编程思想/第三章：Spring IoC容器概述 (9讲)/26丨Spring IoC容器：BeanFactory和ApplicationContext谁才是Spring IoC容器？.mp4
```
1. beanFactory
2. applicationContext

### 27 丨 Spring 应用上下文：ApplicationContext 除了 IoC 容器角色，还提供哪些特性？
```
file:///Volumes/resources/小马哥/Spring核心编程思想/第三章：Spring IoC容器概述 (9讲)/27丨Spring应用上下文：ApplicationContext除了IoC容器角色，还提供哪些特性？.mp4
```
1. 面向切面 AOP
2. 配置元信息
3. 资源管理 URL classLoading
4. 事件
5. 国际化
6. 注解
7. environment
### 28 | 使用 Spring IoC 容器：选 BeanFactory 还是 ApplicationContext？
1. beanFactory 是 spring 底层的 IOC 容器
2. applicationContext 是具备应用特性的 beanFactory 超集

### 29 | Spring IoC 容器生命周期：IoC 容器启停过程中发生了什么？
1. 启动
2. 运行
3. 停止

### 30 | 面试题精选
##### 什么是 IOC 容器？
##### beanFactory 和 factoryBean 区别？
1. beanFactory 是 IOC 底层容器
2. factoryBean 是创建 bean 的一种方式，实现复杂的初始化逻辑
## 第四章：Spring Bean 基础 (11 讲)
### 31 | 定义 Bean：什么是 BeanDefinition？
时长 03:28
### 32 | BeanDefinition 元信息：除了 Bean 名称和类名，还有哪些 Bean 元信息值得关注？
时长 16:20
### 33 | 命名 Spring Bean：id 和 name 属性命名 Bean，哪个更好？
时长 11:18
34 | Spring Bean 的别名：为什么命名 Bean 还需要别名？
时长 08:17
35 | 注册 Spring Bean：如何将 BeanDefinition 注册到 IoC 容器？
时长 21:36
36 | 实例化 Spring Bean：Bean 实例化的姿势有多少种？
时长 32:21
37 | 初始化 Spring Bean：Bean 初始化有哪些方式？
时长 11:32
38 | 延迟初始化 Spring Bean：延迟初始化的 Bean 会影响依赖注入吗？
时长 06:45
39 | 销毁 Spring Bean： 销毁 Bean 的基本操作有哪些？
时长 09:13
40 | 回收 Spring Bean：Spring IoC 容器管理的 Bean 能够被垃圾回收吗？
时长 05:52
41 | 面试题精选
时长 14:23
第五章：Spring IoC 依赖查找（Dependency Lookup） (9 讲)
42 | 依赖查找的今世前生：Spring IoC 容器从 Java 标准中学到了什么？
时长 10:46
43 | 单一类型依赖查找：如何查找已知名称或类型的 Bean 对象？
时长 11:17
44 | 集合类型依赖查找：如何查找已知类型多个 Bean 集合？
时长 08:21
45 | 层次性依赖查找：依赖查找也有双亲委派？
时长 21:09
46 | 延迟依赖查找：非延迟初始化 Bean 也能实现延迟查找？
时长 11:11
47 | 安全依赖查找
时长 22:04
48 | 内建可查找的依赖：哪些 Spring IoC 容器内建依赖可供查找？
时长 19:25
49 | 依赖查找中的经典异常：Bean 找不到？Bean 不是唯一的？Bean 创建失败？
时长 18:26
### 50 | 面试题精选
##### objectFactory 和 BeanFactory 区别？
objectFactory 仅关注一个或一种类型的依赖查找，靠 BeanFactory 完成查找。
##### BeanFactory.getBean 是否线程安全
##### spring 依赖查找与依赖注入来源上的区别？

## 第六章：Spring IoC 依赖注入（Dependency Injection） (20 讲)
### 51 | 依赖注入的模式和类型：Spring 提供了哪些依赖注入的模式和类型？
```
file:///Volumes/resources/小马哥/Spring核心编程思想/第六章：Spring IoC依赖注入（Dependency Injection） (20讲)/51丨依赖注入的模式和类型：Spring提供了哪些依赖注入的模式和类型？.mp4
```

1. 手动模式
	1. 配置或编程，提前安排注入规则
	2. xml 资源配置元信息
	3. Java 注解配置
	4. API
2. 自动模式
	1. 实现方提供依赖自动关联的方式，按照内建的注入规则
	2. `@Autowrite` 自动绑定
3. 类型
	1. setter 方法
	2. 构造器
	3. 方法
	4. 字段
	5. 接口回调


### 52 | 自动绑定（Autowiring）：为什么 Spring 会引入 Autowiring？
时长 05:28
### 53 | 自动绑定（Autowiring）模式：各种自动绑定模式的使用场景是什么？
时长 04:08
### 54 | 自动绑定（Autowiring）限制和不足：如何理解和挖掘官方文档中深层次的含义？
时长 05:50
### 55 | Setter 方法依赖注入：Setter 注入的原理是什么？
时长 27:53
### 56 | 构造器依赖注入：官方为什么推荐使用构造器注入？
时长 08:37
### 57 | 字段注入：为什么 Spring 官方文档没有单独列举这种注入方式？
时长 08:23
### 58 | 方法注入：方法注入是@Autowired 专利吗？
时长 05:08
### 59 | 接口回调注入：回调注入的使用场景和限制有哪些？
时长 08:12
### 60 | 依赖注入类型选择：各种依赖注入有什么样的使用场景？
时长 02:54
### 61 | 基础类型注入：String 和 Java 原生类型也能注入 Bean 的属性，它们算依赖注入吗？
时长 10:57
### 62 | 集合类型注入：注入 Collection 和 Map 类型的依赖区别？还支持哪些集合类型？
时长 09:03
### 63 | 限定注入：如何限定 Bean 名称注入？如何实现 Bean 逻辑分组注入？
时长 22:20
### 64 | 延迟依赖注入：如何实现延迟执行依赖注入？与延迟依赖查找是类似的吗？
时长 07:09
### 65 | 依赖处理过程：依赖处理时会发生什么？其中与依赖查找的差异在哪？
时长 34:18
### 66 | @Autowired 注入：@Autowired 注入的规则和原理有哪些？
时长 22:30
### 67 | JSR-330 @Inject 注入：@Inject 与@Autowired 的注入原理有怎样的联系？
时长 06:38
### 68 | Java 通用注解注入原理：Spring 是如何实现@Resource 和@EJB 等注解注入的？
时长 13:06
### 69 | 自定义依赖注入注解：如何最简化实现自定义依赖注入注解？
时长 16:10
### 70 | 面试题精选
时长 04:56
## 第七章：Spring IoC 依赖来源（Dependency Sources） (8 讲)
71 | 依赖查找的来源：除容器内建和自定义 Spring Bean 之外，还有其他来源提供依赖查找吗？
时长 12:00
72 | 依赖注入的来源：难道依赖注入的来源与依赖查找的不同吗？
时长 18:54
73 | Spring 容器管理和游离对象：为什么会有管理对象和游离对象？
时长 02:53
74 | Spring Bean Definition 作为依赖来源：Spring Bean 的来源
时长 07:59
75 | 单例对象作为依赖来源：单体对象与普通 Spring Bean 存在哪些差异？
时长 06:15
76 | 非 Spring 容器管理对象作为依赖来源：如何理解 ResolvableDependency？
时长 09:46
77 | 外部化配置作为依赖来源：@Value 是如何将外部化配置注入 Spring Bean 的？
时长 12:39
78 | 面试题精选
时长 07:17
第八章：Spring Bean 作用域（Scopes） (9 讲)
79 | Spring Bean 作用域：为什么 Spring Bean 需要多种作用域？
时长 05:56
80 | "singleton" Bean 作用域：单例 Bean 在当前 Spring 应用真是唯一的吗？
时长 07:05
81 | "prototype" Bean 作用域：原型 Bean 在哪些场景下会创建新的实例？
时长 31:24
82 | "request" Bean 作用域：request Bean 会在每次 HTTP 请求创建新的实例吗？
时长 28:44
83 | "session" Bean 作用域：session Bean 在 Spring MVC 场景下存在哪些局限性？
时长 07:39
84 | "application" Bean 作用域：application Bean 是否真的有必要？
时长 14:47
85 | 自定义 Bean 作用域：设计 Bean 作用域应该注意哪些原则？
时长 20:07
86 | 课外资料：Spring Cloud RefreshScope 是如何控制 Bean 的动态刷新？
时长 07:09
87 | 面试题精选
时长 03:33
第九章：Spring Bean 生命周期（Bean Lifecycle） (18 讲)
88 | Spring Bean 元信息配置阶段：BeanDefinition 配置与扩展
时长 25:04
89 | Spring Bean 元信息解析阶段：BeanDefinition 的解析
时长 13:10
90 | Spring Bean 注册阶段：BeanDefinition 与单体 Bean 注册
时长 08:42
91 | Spring BeanDefinition 合并阶段：BeanDefinition 合并过程是怎样出现的？
时长 30:20
92 | Spring Bean Class 加载阶段：Bean ClassLoader 能够被替换吗?
时长 15:17
93 | Spring Bean 实例化前阶段：Bean 的实例化能否被绕开？
时长 12:40
94 | Spring Bean 实例化阶段：Bean 实例是通过 Java 反射创建吗？
时长 40:55
95 | Spring Bean 实例化后阶段：Bean 实例化后是否一定被是使用吗？
时长 11:30
96 | Spring Bean 属性赋值前阶段：配置后的 PropertyValues 还有机会修改吗？
时长 21:32
97 | Aware 接口回调阶段：众多 Aware 接口回调的顺序是安排的？
时长 19:11
98 | Spring Bean 初始化前阶段：BeanPostProcessor
时长 14:12
99 | Spring Bean 初始化阶段：@PostConstruct、InitializingBean 以及自定义方法
时长 13:15
100 | Spring Bean 初始化后阶段：BeanPostProcessor
时长 04:43
101 | Spring Bean 初始化完成阶段：SmartInitializingSingleton
时长 12:24
102 | Spring Bean 销毁前阶段：DestructionAwareBeanPostProcessor 用在怎样的场景?
时长 14:18
103 | Spring Bean 销毁阶段：@PreDestroy、DisposableBean 以及自定义方法
时长 09:40
104 | Spring Bean 垃圾收集（GC）：何时需要 GC Spring Bean？
时长 05:42
105 | 面试题精选
时长 17:48
第十章：Spring 配置元信息（Configuration Metadata） (17 讲)
106 | Spring 配置元信息：Spring 存在哪些配置元信息？它们分别用在什么场景？
时长 08:57
107 | Spring Bean 配置元信息：BeanDefinition
时长 10:11
108 | Spring Bean 属性元信息：PropertyValues
时长 16:43
109 | Spring 容器配置元信息
时长 22:44
110 | 基于 XML 资源装载 Spring Bean 配置元信息
时长 20:10
111 | 基于 Properties 资源装载 Spring Bean 配置元信息：为什么 Spring 官方不推荐？
时长 26:57
112 | 基于 Java 注解装载 Spring Bean 配置元信息
时长 12:39
113 | Spring Bean 配置元信息底层实现之 XML 资源
时长 12:04
114 | Spring Bean 配置元信息底层实现之 Properties 资源
时长 06:13
115 | Spring Bean 配置元信息底层实现之 Java 注解
时长 12:20
116 | 基于 XML 资源装载 Spring IoC 容器配置元信息
时长 11:17
117 | 基于 Java 注解装载 Spring IoC 容器配置元信息
时长 16:31
118 | 基于 Extensible XML authoring 扩展 Spring XML 元素
时长 33:25
119 | Extensible XML authoring 扩展原理
时长 13:49
120 | 基于 Properties 资源装载外部化配置
时长 11:50
121 | 基于 YAML 资源装载外部化配置
时长 20:52
122 | 面试题
时长 07:18
第十一章：Spring 资源管理（Resources） (11 讲)
123 | 引入动机：为什么 Spring 不使用 Java 标准资源管理，而选择重新发明轮子？
时长 06:16
124 | Java 标准资源管理：Java URL 资源管理存在哪些潜规则？
时长 16:22
125 | Spring 资源接口：Resource 接口有哪些语义？它是否“借鉴”了 SUN 的实现呢？
时长 10:52
126 | Spring 内建 Resource 实现：Spring 框架提供了多少种内建的 Resource 实现呢？
时长 13:29
127 | Spring Resource 接口扩展：Resource 能否支持写入以及字符集编码？
时长 14:00
128 | Spring 资源加载器：为什么说 Spring 应用上下文也是一种 Spring 资源加载器？
时长 07:18
129 | Spring 通配路径资源加载器：如何理解路径通配 Ant 模式？
时长 06:49
130 | Spring 通配路径模式扩展：如何扩展路径匹配的规则？
时长 12:24
131 | 依赖注入 Spring Resource：如何在 XML 和 Java 注解场景注入 Resource 对象？
时长 09:31
132 | 依赖注入 ResourceLoader：除了 ResourceLoaderAware 回调注入，还有哪些注入方法？
时长 09:12
133 | 面试题精选
时长 17:28
第十二章：Spring 国际化（i18n） (9 讲)
134 | Spring 国际化使用场景
时长 04:43
135 | Spring 国际化接口：MessageSource 不是技术的创造者，只是技术的搬运工？
时长 06:43
136 | 层次性 MessageSource：双亲委派不是 ClassLoader 的专利吗？
时长 06:02
137 | Java 国际化标准实现：ResourceBundle 潜规则多？
时长 10:44
138 | Java 文本格式化：MessageFormat 脱离 Spring 场景，能力更强大？
时长 14:44
139 | MessageSource 开箱即用实现：ResourceBundle +MessageFormat 组合拳？
时长 13:32
140 | MessageSource 内建依赖：到底“我”是谁？
时长 07:06
141 | 课外资料：SpringBoot 为什么要新建 MessageSource Bean？
时长 22:01
142 | 面试题精选
时长 43:24
第十三章：Spring 校验（Validation） (7 讲)
143 | Spring 校验使用场景：为什么 Validator 并不只是 Bean 的校验？
时长 03:47
144 | Validator 接口设计：画虎不成反类犬？
时长 06:16
145 | Errors 接口设计：复杂得没有办法理解？
时长 09:49
146 | Errors 文案来源：Spring 国际化充当临时工？
时长 15:52
147 | 自定义 Validator：为什么说 Validator 容易实现，却难以维护？
时长 08:46
148 | Validator 的救赎：如果没有 Bean Validation，Validator 将会在哪里吗？
时长 17:00
149 | 面试题精选
时长 03:54
第十四章：Spring 数据绑定（Data Binding） (9 讲)
150 | Spring 数据绑定使用场景：为什么官方文档描述一笔带过？
时长 05:26
151 | Spring 数据绑定组件：DataBinder
时长 05:23
152 | DataBinder 绑定元数据：PropertyValues 不是 Spring Bean 属性元信息吗？
时长 06:03
153 | DataBinder 绑定控制参数：ignoreUnknownFields 和 ignoreInvalidFields 有什么作用？
时长 21:50
154 | Spring 底层 JavaBeans 替换实现：BeanWrapper 源于 JavaBeans 而高于 JavaBeans？
时长 03:11
155 | BeanWrapper 的使用场景：Spring 数据绑定只是副业？
时长 06:55
156 | 课外资料：标准 JavaBeans 是如何操作属性的
时长 12:17
157 | DataBinder 数据校验：又见 Validator
时长 11:12
158 | 面试题精选
时长 04:18
## 第十五章：Spring 类型转换（Type Conversion） (15 讲)
### 159 | Spring 类型转换的实现：Spring 提供了哪几种类型转换的实现？
时长 05:56
### 160 | 使用场景：Spring 类型转换各自的使用场景以及发展脉络是怎样的？
时长 06:42
### 161 | 基于 JavaBeans 接口的类型转换：Spring 是如何扩展 PropertyEditor 接口实现类型转换的？
时长 10:44
### 162 | Spring 内建 PropertyEditor 扩展：哪些常见类型被 Spring 内建 PropertyEditor 实现？
时长 07:03
### 163 | 自定义 PropertyEditor 扩展：不尝试怎么知道它好不好用？
时长 15:56
### 164 | SpringPropertyEditor 的设计缺陷：为什么基于 PropertyEditor 扩展并不适合作为类型转换？
时长 04:02
### 165 | Spring 3 通用类型转换接口：为什么 Converter 接口设计比 PropertyEditor 更合理？
时长 07:14
### 166 | Spring 内建类型转换器：Spring 的内建类型转换器到底有多丰富？
时长 04:14
### 167 | Converter 接口的局限性：哪种类型转换场景 Converter 无法满足？有什么应对之策？
时长 04:35
### 168 | GenericConverter 接口：为什么 GenericConverter 比 Converter 更通用？
时长 06:15
### 169 | 优化 GenericConverter 接口：为什么 GenericConverter 需要补充条件判断？
时长 04:18
### 170 | 扩展 Spring 类型转换器：为什么最终注册的都是 ConditionalGenericConverter？
时长 21:54
### 171 | 统一类型转换服务：ConversionService 足够通用吗？
时长 09:18
### 172 | ConversionService 作为依赖-能够同时作为依赖查找和依赖注入的来源吗？
时长 15:36
### 173 | 面试题精选
时长 03:56
## 第十六章：Spring 泛型处理（Generic Resolution） (8 讲)
174 | Java 泛型基础：泛型参数信息在擦写后还会存在吗？
时长 12:54
175 | Java 5 类型接口-Type：Java 类型到底是 Type 还是 Class？
时长 14:44
176 | Spring 泛型类型辅助类：GenericTypeResolver
时长 19:23
177 | Spring 泛型集合类型辅助类：GenericCollectionTypeResolver
时长 09:45
178 | Spring 方法参数封装-MethodParameter：不仅仅是方法参数
时长 07:44
179 | Spring 4.2 泛型优化实现-ResolvableType
时长 09:41
180 | ResolvableType 的局限性：形式比人强？
时长 02:35
181 | 面试题精选
时长 03:39
第十七章：Spring 事件（Events） (20 讲)
182 | Java 事件/监听器编程模型：为什么 Java 中没有提供标准实现？
时长 11:29
183 | 面向接口的事件/监听器设计模式：单事件监听和多事件监听怎么选？
时长 04:34
184 | 面向注解的事件/监听器设计模式：便利也会带来伤害？
时长 03:12
185 | Spring 标准事件-ApplicationEvent：为什么不用 EventObject？
时长 02:29
186 | 基于接口的 Spring 事件监听器：ApplicationListener 为什么选择单事件监听模式？
时长 05:32
187 | 基于注解的 Spring 事件监听器：@EventListener 有哪些潜在规则？
时长 09:40
188 | 注册 Spring ApplicationListener：直接注册和间接注册有哪些差异？
时长 03:26
189 | Spring 事件发布器：Spring 4.2 给 ApplicationEventPublisher 带来哪些变化？
时长 06:31
190 | Spring 层次性上下文事件传播：这是一个 Feature 还是一个 Bug？
时长 13:48
191 | Spring 内建事件（Built-in Events）：为什么 ContextStartedEvent 和 ContextStoppedEvent 是鸡肋事件？ - 深入剖析源码，掌握核心编程特性
时长 04:17
192 | Spring 4.2 Payload 事件：为什么说 PayloadApplicationEvent 并非一个良好的设计？
时长 07:24
193 | 自定义 Spring 事件：自定义事件业务用得上吗？
时长 02:59
194 | 依赖注入 ApplicationEventPublisher：事件推送还会引起 Bug？
时长 06:17
195 | 依赖查找 ApplicationEventPublisher：ApplicationEventPublisher 从何而来？
时长 09:18
196 | ApplicationEventPublisher 底层实现：ApplicationEventMulticaster 也是 Java Observable 的延伸？
时长 13:44
197 | 同步和异步 Spring 事件广播：Spring 对J.U.C Executor 接口的理解不够？
时长 20:44
198 | Spring 4.1 事件异常处理：ErrorHandler 使用有怎样的限制？
时长 06:13
199 | Spring 事件/监听器实现原理：面向接口和注解的事件/监听器实现有区别吗？
时长 13:55
200 | 课外资料：Spring Boot 和 Spring Cloud 事件也是 Spring 事件？
时长 06:24
201 | 面试题精选
时长 05:06
第十八章：Spring 注解（Annotations） (12 讲)
202 | Spring 注解驱动编程发展历程
时长 12:15
203 | Spring 核心注解场景分类
时长 05:51
204 | Spring 注解编程模型
时长 02:59
205 | Spring 元注解（Meta-Annotations）
时长 05:28
206 | Spring 模式注解（Stereotype Annotations）
时长 33:57
207 | Spring 组合注解（Composed Annotations）
时长 08:10
208 | Spring 注解属性别名（Attribute Aliases）
时长 14:21
209 | Spring 注解属性覆盖（Attribute Overrides）
时长 10:40
210 | Spring @Enable 模块驱动
时长 14:00
211 | Spring 条件注解
时长 17:09
212 | 课外资料：Spring Boot 和 Spring Cloud 是怎样在 Spring 注解内核上扩展的?
时长 05:18
213 | 面试题精选
时长 11:32
第十九章：Spring Environment 抽象（Environment Abstraction） (16 讲)
214 | 理解 Spring Environment 抽象
时长 05:56
215 | Spring Environment 接口使用场景
时长 04:47
216 | Environment 占位符处理
时长 13:33
217 | 理解条件配置 Spring Profiles
时长 04:49
218 | Spring 4 重构@Profile
时长 02:25
219 | 依赖注入 Environment
时长 07:51
220 | 依赖查找 Environment
时长 07:05
221 | 依赖注入@Value
时长 09:15
222 | Spring 类型转换在 Environment 中的运用
时长 06:24
223 | Spring 类型转换在@Value 中的运用
时长 04:11
224 | Spring 配置属性源 PropertySource
时长 05:55
225 | Spring 內建的配置属性源
时长 03:05
226 | 基于注解扩展 Spring 配置属性源
时长 08:12
227 | 基于 API 扩展 Spring 外部化配置属性源
时长 11:22
228 | 课外资料：Spring 4.1 测试配置属性源-@TestPropertySource
时长 12:20
229 | 面试题精选
时长 03:09
第二十章：Spring 应用上下文生命周期（Container Lifecycle） (21 讲)
230 | Spring 应用上下文启动准备阶段
时长 13:43
231 | BeanFactory 创建阶段
时长 12:36
232 | BeanFactory 准备阶段
时长 14:19
233 | BeanFactory 后置处理阶段
时长 27:33
234 | BeanFactory 注册 BeanpostProcessor 阶段
时长 12:49
235 | 初始化内建 Bean：MessageSource
时长 06:47
236 | 初始化内建 Bean：Spring 事件广播器
时长 03:18
237 | Spring 应用上下文刷新阶段
时长 05:09
238 | Spring 事件监听器注册阶段
时长 05:49
239 | BeanFactory 初始化完成阶段
时长 12:59
240 | Spring 应用上下刷新完成阶段
时长 16:08
241 | Spring 应用上下文启动阶段
时长 03:25
242 | Spring 应用上下文停止阶段
时长 07:41
243 | Spring 应用上下文关闭阶段
时长 19:30
244 | 面试题精选
时长 22:20
245 | 结束语
时长 20:05
加餐 1：为什么说 ObjectFactory 提供的是延迟依赖查找?
时长 17:34
加餐 2 | 依赖查找（注入）的 Bean 会被缓存吗？
时长 20:40
加餐 3 | @Bean 的处理流程是怎样的？
时长 20:06
加餐 4 | BeanFactory 如何处理循环依赖的？
时长 34:21
加餐 5 | MyBatis 与 Spring Framework 是如何集成的？
时长 11:31