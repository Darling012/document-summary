# Feign

1. 默认集成了 [[ribbon]]
2. fegin 由 netflix 开发，openFegin 属于 spring cloud 自己研发
#####  [Hystrix](Hystrix.md)、[Feign](Feign.md)、[ribbon](ribbon.md) 关系
ribbon 获取服务地址，fegin 发起实际调用，hystrix 兜底

1. [SpringCloud Feign整合Hystrix实现服务降级、熔断、hystrix、ribbon超时时间问题_鸢尾の的博客-CSDN博客_feign关闭熔断](https://blog.csdn.net/weixin_45248492/article/details/123404397)
2. [Feign Ribbon Hystrix 三者关系 | 史上最全, 深度解析 - 疯狂创客圈 - 博客园](https://www.cnblogs.com/crazymakercircle/p/11664812.html)
3. [Feign、Ribbon、Hystrix三者超时时间配置](https://blog.csdn.net/qq_40250122/article/details/118030329)
## 配置
##### @FeignClient各参数
1. [那天晚上和@FeignClient注解的深度交流](https://juejin.cn/post/6844904039595917319)
##### 超时与重试
1. [聊聊openfeign的超时和重试](https://cloud.tencent.com/developer/article/1749558)
##### 日志
1. [Feign 的细粒度与全局 日志打印](https://blog.csdn.net/Tiny_Demon/article/details/124717616)
##### 丢失请求头
1.  [Feign 调用丢失Header的解决方案](https://www.cnblogs.com/huanchupkblog/p/11895979.html)
##### Fegin 的继承
好处
1. 面向契约
2. 重用
坏处
1. 紧耦合
2. 方法参数映射不被继承
 [使用Feign的一些问题以及如何解决？](https://mp.weixin.qq.com/s/yzGWfxBsRbeDdDFYXQHNvg)

##### fegin 异常处理
1. [**Spring Cloud Feign--全局异常处理--方法/实例**](https://blog.51cto.com/knifeedge/5139865) void 情况？
2. [Feign自定义ErrorDecoder错误时返回统一结构](https://blog.csdn.net/new9xgh/article/details/107934862)
3. [原生Feign的解码器Decoder、ErrorDecoder](https://cloud.tencent.com/developer/article/1588501)
4. [feign和ribbon的异常捕捉](https://www.cnblogs.com/chuliang/p/13100531.html)
5. [SpringCloud组件OpenFeign——将服务端详细异常信息返回给客户端](https://blog.csdn.net/m0_47503416/article/details/122089913)
6. [feign服务端出异常客户端处理的方法](https://www.cnblogs.com/lori/p/11157394.html)

##### 调用过程
1.  [Spring Cloud Feign 调用过程分析](https://www.cnblogs.com/rickiyang/p/11802487.html)
###### [Feign 如何调用Ribbon进行客户端负载均衡](https://blog.csdn.net/qq_23202687/article/details/94408008)
feign. Client 是一个接口，它有两个实现类
1. `feign.Client.Default`
2. `org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient`
`LoadBalancerFeignClient#execute`
1. 封装 RibbonReuqest 请求对象
2. 从容器中获取 Ribbon 请求配置参数信息 IConfigClient
3. 根据 RibbonRequest 和 IConfigClient 构造 FeignLoadBalancer 
	1. FeignLoadBalancer 根据 Ribbon 的 IRule 规则从 Iloadbalancer 获取一个服务实例 server。
	2. FeignLoadBalancer 的 execute根据获取到的服务实例 Server 发起请求调用。然后返回。
##### 常见问题
多参数请求、上传文件、form表单、分页
1. [Feign常见问题总结](https://www.imooc.com/article/289005)
2. [Spring Cloud Feign 复杂用法：文件上传+复杂参数传递【官方方案】](https://hicode.club/articles/2018/12/25/1550590735080.html)
3. [Feign远程调用传递对象参数 并 返回自定义分页数据完整过程](https://blog.csdn.net/qq_36068521/article/details/102565751)
4. [如何使用Feign构造多参数的请求](https://www.imooc.com/article/289000)
5. [RequestParam，RequestBody，SpringQueryMap](https://www.jianshu.com/p/9d1d770e22b0)

[FeignClient源码深度解析 - 分布式编程](https://zthinker.com/archives/feignclient%E6%BA%90%E7%A0%81%E6%B7%B1%E5%BA%A6%E8%A7%A3%E6%9E%90)

#### 项目
##### k12
![k12fegin](66服务器.md#fegin)

## reference
1. [Feign 原理源码详解](https://juejin.cn/column/7087106428485238791) 
2. [SpringCloud OpenFeign 核心原理](https://www.modb.pro/db/109223) 
3. [声明式 REST 客户端：Feign]( http://docs.springcloud.cn/user-guide/feign/ )