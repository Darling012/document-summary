# geteway
核心功能 路由转发，还能做统一的[[熔断]]、[[限流]]、[认证](oauth2.md)、[日志监控](日志分析.md)、解决[[跨域]]等

1. spring cloud gateway
2. netflix zuul
3. nginx 
4. kong 基于 nginx
5. ali tengine

2. [一文带你搞懂API网关](https://github.com/aCoder2013/blog/issues/35)
3. [第一章 微服务网关 - 入门 - 赵计刚 - 博客园](https://www.cnblogs.com/java-zhao/p/6716059.html)

## spring cloud gateway 原理
### filter
1. pre 类型
	1. 在请求转发到微服务前，对请求进行拦截和修改，如参数校验、权限校验、流量监控、日志输出、协议转换等
2. post 类型
	1. 微服务处理完成后返回响应给网关，可再次处理，如[修改响应内容或响应头](gateway.md#spring%20cloud%20gateway%20修改响应体)，日志输出、流量监控等
##### 按范围分
1. 全局过滤器 GlobalFilter 
	1. loadBalancerClient Filter：
		1. 整合 ribbion 实现负载均衡
	2. NettyRoutingFilter 
		1. 使用 netty 的 httpclient 转发 http、https 请求
	3. websocket routing filter 
		1. 使用 spring web socket 转发 websocket 请求
	4. 本地 forward、监控指标等
2. 局部过滤器 GatewayFilter
##### reference
1.  [Spring Cloud Gateway 过滤器执行顺序原理分析_抓手的博客-CSDN博客](https://blog.csdn.net/Anenan/article/details/114691488)
2. [深入理解 Spring Cloud Gateway 的原理](https://mp.weixin.qq.com/s?__biz=MzAwMjI0ODk0NA==&mid=2451964154&idx=1&sn=202154c1206936d8eaa72e617a2ef6ad&chksm=8d1ff965ba6870732290b202e2f97c85f9c8c6f84f8d33e57dca63085f5eefc9aba05e0ef523)

### 请求流程
[webflux](webflux.md)
1. [Spring Cloud Gateway-ServerWebExchange核心方法与请求或者响应内容的修改 - 掘金](https://juejin.cn/post/6844903846469189645)
2. [Spring-Cloud-Gateway之请求处理流程 - 简书](https://www.jianshu.com/p/c40a757fad01)

##### netty http 客户端
SpringCloud gateway 在实现服务路由并请求的具体过程是在 **org.springframework.cloud.gateway.filter.NettyRoutingFilter** 的过滤器中，该过滤器封装了具体的请求参数，以及根据路由规则请求的对应服务，然后根据 HttpClient 进行微服务之间的请求； 该 httpClient 类是 用netty 封装的 客户端，其包路径为 ： **reactor.netty.http.client.HttpClient ;**
1. [SpringCloud gateway自定义请求的 httpClient - 香吧香 - 博客园](https://www.cnblogs.com/zjdxr-up/p/16530423.html)

### 使用
1. [快速搭建一个网关服务，动态路由、鉴权看完就会（含流程图） - 掘金](https://juejin.cn/post/7004756545741258765)
2. [响应前过早关闭连接问题解决](https://mp.weixin.qq.com/s/xdg9wt1HMkzay6SGEKDoeQ)
3. [Spring Cloud Gateway系列【8】基于注册中心Nacos的动态路由案例及加载执行流程源码分析_云烟成雨TD的博客-CSDN博客](https://blog.csdn.net/qq_43437874/article/details/121631097)
##### spring cloud gateway 修改响应体
1. [Spring Cloud Gateway 修改响应数据 - 掘金](https://juejin.cn/post/7038779004169486343)
##### spring cloud gateway [[熔断]] [[降级]]
1. CircuitBreaker 过滤器
	1. [第五章 微服务网关Spring Cloud Gateway - 掘金](https://juejin.cn/post/6994380263983693854)
2. 集成hystrix
	1. [Spring cloud gateway 详解和配置使用（文章较长）_荡漾-的博客-CSDN博客_springgateway](https://blog.csdn.net/qq_38380025/article/details/102968559)

##### spring cloud gateway [[限流]]
1. RequestRateLimiterGatewayFilterFactory
	1. [第五章 微服务网关Spring Cloud Gateway - 掘金](https://juejin.cn/post/6994380263983693854#heading-18)
##### spring cloud gateway [负载均衡](负载均衡.md)
1. ribbon 阻塞式 LoadBalancerClientFilter
	1. 默认使用，gateway 建议使用 reactive 的
2. ReactiveLoadBalancerClientFilter 的LoadBalancer
	1. 
两者在 route 中配置 lb 方式一样
1. [Spring Cloud Gateway网关两种负载均衡_yaoshengting的博客-CSDN博客_springgateway负载均衡](https://blog.csdn.net/ystyaoshengting/article/details/119650231)
2. [Spring Cloud Gateway负载均衡](https://www.jianshu.com/p/9623f9fb160b)
6. [Ribbon+OpenFeign客户端+Gateway](https://blog.csdn.net/muhaokai/article/details/118697415)

##### 其他网关
1. [网关Zuul](https://mp.weixin.qq.com/s?__biz=MzU0MzQ5MDA0Mw==&mid=2247489653&idx=1&sn=b2ed7b657b67c147483571ae01cb9aae)
	1. `1.x` 版本是阻塞，`2.x` 基于 netty 非阻塞
2. [网关Soul](https://mp.weixin.qq.com/s?__biz=MzU0MzQ5MDA0Mw==&mid=2247489653&idx=1&sn=b2ed7b657b67c147483571ae01cb9aae)

