## rpc
[[ribbon]]+resttemplate
[[fegin]] (默认集成 [[ribbon]])
1. [微服务之间的最佳调用方式](https://mp.weixin.qq.com/s?__biz=MzU0MzQ5MDA0Mw==&mid=2247488428&idx=2&sn=bd688553c82af1f597e5adfef666b996&chksm=fb0bf938cc7c702e6b1af4d18b279520dcdf7a21bf73d7482aabc1bf068f674c662b7f491e34&mpshare=1&scene=1&srcid=&sharer_sharetime=1580145600204&sharer_shareid=07754c1336c3524bfffedc4dc59111b6&key=1de96bf14ac83757a9c10aa802bee067fce6b42c902237f3ef5eb70fbbacf01cb54182438ae253d413a0b182f70b8f240fd86a78a253d8434f5f97162691f59ebe7ea39accdc5957595938cf48ffa27b&ascene=1&uin=MTY5NjI3ODY2MQ%3D%3D&devicetype=Windows+10&version=62070158&lang=zh_CN&exportkey=AY7m%2FDd68vPir5gLzpgMPA0%3D&pass_ticket=w0h%2FGWHBYwvP7E%2BIIOeqS7OZUHTXM1M%2B1MRno83Zh0Q1s56m5uIB9otCBPvTvaUT)
2. 业务代码中rpc 
```java
        GenericResponse response = null;
        try {
            response = HttpClint.invoke();
        } catch (Exception exception) {
            // httpClient调用产生的异常
            log.error(exception);
            handlerErrorAndThrowExcetption(exception);
        }
        // 调用成功返回结果
        if (response.code != 200) {
            handlerErrorAndThrowExcetption();
        }
        Pojo pojo = response.getData();
```

按[一个请求的过程](一个请求的过程.md#一个请求的过程)，rpc 调用方与提供方需要关注处理的点为
1. [幂等](幂等.md#幂等)
2. [签名模块的解签解密](加密加签.md#加密加签)
3. [数据业务逻辑校验](数据业务逻辑校验.md#数据业务逻辑校验)
4. [响应体及状态码设计](响应体及状态码设计.md#响应体及状态码设计)
5. [重试](单体/重试.md#RPC%20retry)

#####  hystrix、[fegin](fegin.md)、[ribbon](ribbon.md)
1. [SpringCloud Feign整合Hystrix实现服务降级、熔断、hystrix、ribbon超时时间问题_鸢尾の的博客-CSDN博客_feign关闭熔断](https://blog.csdn.net/weixin_45248492/article/details/123404397)
2. [Feign Ribbon Hystrix 三者关系 | 史上最全, 深度解析 - 疯狂创客圈 - 博客园](https://www.cnblogs.com/crazymakercircle/p/11664812.html)
3. [Feign、Ribbon、Hystrix三者超时时间配置](https://blog.csdn.net/qq_40250122/article/details/118030329)
