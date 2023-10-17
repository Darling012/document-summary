
# Spring Validation

##### 关系  
1. jsr303：Java API 规范 (JSR303) 定义了 Bean 校验的标准 validation-api，但没有提供实现。   
2. hibernate validation： 对 jsr303 进行了实现，提供了校验注解如@Email、@Length 等。  
3. Spring Validation：是对 hibernate validation 的二次封装，用于支持 spring mvc 参数自动校验。  
4. [JSR303/JSR-349，hibernate validation，spring validation 之间的关系](https://blog.51cto.com/u_15050718/3675487)

## spring 方法级别的参数校验实现原理
[Spring方法级别数据校验](https://blog.csdn.net/f641385712/article/details/97402946)
###### 方法级别
1. 方法级别的入参有可能是各种平铺的参数、也可能是一个或者多个对象

###### MethodValidationPostProcessor
1. 是 spring 提供基于方法实现 jsr 校验的核心处理器
2. 对入参、返回值其约束作用
3. 类上必须标注 `@Validated`
4. 是个普通的 `BeanPostProcessor`，为 Bean 创建的代理的时机是 `postProcessAfterInitialization()`，也就是在 Bean 完成[初始化](SpringBean生命周期.md#流程)后有必要的话用一个代理对象返回进而交给 Spring 容器管理~（同 `@Aysnc`）

##### 流程
1. MethodValidationPostProcessor 动态注册 aop 切面
	1. 为所有 `@Validated` 标注的 Bean 创建切面  
	2. 创建 Advisor 进行增强
	3. 创建 Advice，本质就是一个方法拦截器 
2. 然后使用 MethodValidationInterceptor 对切点方法织入增强，**执行校验逻辑**
	1. 无需增强直接跳过 
	2. 获取分组信息 
	3. 方法入参校验 Hibernate Validator, 有异常直接抛出
	4. 执行自己写的业务方法
	5. 对返回值做校验 Hibernate Validator，有异常直接抛出

## spring mvc 对 javaBean 验证的支持
[controller对JavaBean的校验](https://blog.csdn.net/f641385712/article/details/97621783)

`Controller` 提供的使用 `@Valid` 便捷校验 `JavaBean` 的原理，和 [Spring对方法级别数据校验](Spring%20Validation.md#spring%20方法级别的参数校验实现原理)支持的原理是有很大差异的（可类比 `Spring MVC` 拦截器和 `Spring AOP` 的差异区别

##### 对@RequestBody 进行 validate原理
1. RequestResponseBodyMethodProcessor 用于处理@RequestBody 标注的参数以及返回值
2. 在其 resolveArgument ()-->validateIfApplicable (binder, parameter)  执行数据校验
3. 获取请求参数注解，比如@RequestBody、@Valid、@Validated
4. 先尝试获取@Validated 注解，如果直接标注了@Validated，那么直接开启校验。如果没有，那么判断参数前是否有 Valid 起头的注解。
5. 调用 Hibernate Validator 执行真正的校验。  

##### 对@RequestParam pojo
1.  ModelAttributeMethodProcessor 处理 get 请求使用 @RequestParam 标注 接口用 pojo 接的请求
2.  在其.... 同上

###### 使用
1. 参数前标注@Validated 或@Valid（基于原理 4），**controller 可不标注**（但要兼容平铺参数，所以必须加@Validated ）。 
2. 入参处不标注，**在 pojo 的 class 标注不起作用**，源码是取入参处注解进行操作 `MethodParameter#getParameterAnnotations（）`。
3. 对于 pojo A 嵌套 pojo B 的，要在 A 的 B 属性上添加  [@Valid](Spring%20Validation.md#Valid和%20Validated) 满足嵌套校验
5. 校验失败抛出MethodArgumentNotValidException异常，http状态码400。  
6. 不能对返回值校验 

## 解决 spring mvc 不支持平铺参数校验
采用 [Spring对方法级别数据校验](Spring%20Validation.md#spring%20方法级别的参数校验实现原理)的能力
1. 必须在 Controller 类上标注@Validated 注解，并在入参上声明约束注解。
2. @Valid 不起作用，因为用的 spring 提供的能力，@Valid 为 jsr 注解
3. 在入参处加@Validated 不起作用
4. 校验失败，会抛出 ConstraintViolationException 异常。
5. 入参返回值都可校验 

## 总结 
##### springmvc 中 validate 应用场景
请求参数分为如下两种形式：  
1. POST、PUT 请求，使用 requestBody 传递参数
	1. [采用 mvc 自身对 Javabean 的校验处理](Spring%20Validation.md#spring%20mvc%20对%20javaBean%20验证的支持)
2. GET 请求，使用 requestParam/PathVariable 传递参数
	1. 采用 [Spring对方法级别数据校验](Spring%20Validation.md#spring%20方法级别的参数校验实现原理)的能力
	2. 使用 requestParam 注解，接口采用 pojo 接收情况，[采用 mvc 自身对 Javabean 的校验处理](Spring%20Validation.md#spring%20mvc%20对%20javaBean%20验证的支持)

1. controller 必须加@Validated ，满足 get 请求平铺参数 场景
2. get 请求传递 pojo 的要在 pojo 前加 valid 开头注解。
3. 对于@requestBody 参数前必须加 valid 开头注解，满足校验。
4. 嵌套校验要在所嵌套 对象上加@Valid
5. 有接口继承情况，校验注解要写在接口层，[不然报错](https://blog.csdn.net/f641385712/article/details/97402946)

##### @Valid和@Validated
在Controller中校验方法参数时，使用@Valid和@Validated并无特殊差异（若不需要分组校验的话）:@Valid：标准JSR-303规范的标记型注解，用来标记验证属性和方法返回值，进行级联和递归校验@Validated：Spring的注解，是标准JSR-303的一个变种（补充），提供了一个分组功能，可以在入参验证时，根据不同的分组采用不同的验证机制  
方法级别：
1. @Validated 注解可以用于类级别，用于支持 [Spring 进行方法级别的参数校验](Spring%20Validation.md#spring%20方法级别的参数校验实现原理)。
2. @Valid 可以用在属性级别约束，用来表示级联校验。
3. @Validated 只能用在类、方法和参数上
4. @Valid 可用于方法、字段、构造器和参数上
5. [@Validated和@Valid校验参数、级联属性、List - 掘金](https://juejin.cn/post/6844903974257049608)
6. [参数验证 @Validated 和 @Valid 的区别](https://mp.weixin.qq.com/s?__biz=MzAxODcyNjEzNQ==&mid=2247498335&idx=2&sn=d7901cc16b121cfe9db1c76a9daa345f)
7. 

##### spring 关于 null 的注解
 [spring 关于 null 的注解](https://mp.weixin.qq.com/s?__biz=MzI4Njc5NjM1NQ==&mid=2247488766&idx=1&sn=3dd91caba2f15db453852cbdf37eddbd)
-  `@Nullable`: 用于指定参数、返回值或者字段可以作为 null 的注释。
-   `@NonNull`: 与上述注释相反，表明指定参数、返回值或者字段不允许为null。(不需要@NonNullApi和@NonNullFields适用的参数/返回值和字段)
-   `@NonNullApi`: 包级别的注释声明非null作为参数和返回值。
-   `@NonNullFields`:包级别的注释声明字段默认非空

##### refefence
1. [Spring Validation最佳实践及其实现原理，参数校验没那么简单！ - 掘金](https://juejin.cn/post/6856541106626363399)
4. [分组序列@GroupSequenceProvider、@GroupSequence控制数据校验顺序，解决多字段联合逻辑校验问题【享学Spring MVC】](https://blog.csdn.net/f641385712/article/details/99725482)


##### ps
MethodValidationPostProcessor 和@async 的处理器 AsyncAnnotationBeanPostProcessor 类似，都是继承自AbstractBeanFactoryAwareAdvisingPostProcessor[Spring 异步](Spring%20异步.md)
