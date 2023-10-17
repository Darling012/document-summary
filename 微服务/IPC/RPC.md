# rpc

##### IPC、RPC、RMI 关系
IPC inter-process communication 进程间通信
RPC（Remote Procedure Call Protocol）远程过程调用协议
RMI （Java Remote Method Invocation） Java 远程方法调用
[gRPC](https://grpc.io/)  Google Remote Procedure Call
[IPC和RPC的区别\_二仪式的博客-CSDN博客\_ipc rpc](https://blog.csdn.net/u013894427/article/details/82789837)
有两种类型的进程间通信 (IPC)。
　　本地过程调用 (LPC) LPC 用在多任务操作系统中，使得同时运行的任务能互相会话。这些任务共享内存空间使任务同步和互相发送信息。
　　远程过程调用 (RPC) RPC 类似于 LPC，只是在网上工作。RPC 开始是出现在 Sun 微系统公司和 HP 公司的运行 UNIX 操作系统的计算机中。


##### restful 和 RPC 和 gRpc
[gRPC系列(一) 什么是RPC？ - 知乎](https://zhuanlan.zhihu.com/p/148139089)
[什么是gRPC？gRPC VS REST - 华为](https://info.support.huawei.com/info-finder/encyclopedia/zh/gRPC.html)

[直观讲解--RPC调用和HTTP调用的区别\_rpc与http的区别\_浮生忆梦的博客-CSDN博客](https://blog.csdn.net/m0_38110132/article/details/81481454)
[3.8 既然有 HTTP 协议，为什么还要有 RPC？ | 小林coding](https://www.xiaolincoding.com/network/2_http/http_rpc.html)
## restful
1.  [HTTPClient](HTTPClient.md)
	1. [java实现HTTP请求的三种方式 - 请叫我西毒 - 博客园](https://www.cnblogs.com/hhhshct/p/8523697.html)
2.  [[ribbon]] +resttemplate
	1. [RestTemplate实践（及遇到的问题） - duanxz - 博客园](https://www.cnblogs.com/duanxz/p/3510622.html)
3.  [[Feign]] (默认集成 [[ribbon]])
4. [spring-boot项目整合Retrofit最佳实践，最优雅的HTTP客户端工具！ - 掘金](https://juejin.cn/post/6854573211426750472)
5. [forest](https://gitee.com/dromara/forest)

##### 业务代码中 rpc 

按[一个请求的过程](一个请求的过程.md#一个请求的过程)，rpc 调用方与提供方需要关注处理的点为
1. [幂等](幂等.md#幂等)
2. [签名模块的解签解密](加密加签.md#加密加签)
3. [数据业务逻辑校验](数据业务逻辑校验.md#数据业务逻辑校验)
4. [响应体及状态码设计](响应体及状态码设计.md#响应体及状态码设计)
5. [重试](单体/重试.md#RPC%20retry)
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


[微服务之间的最佳调用方式_倚天码农的博客-CSDN博客_微服务调用](https://blog.csdn.net/weixin_38748858/article/details/101062272)
## grpc
[gRPC](gRPC.md)
## dubbo
[Dubbo 的 8000 字图文详解](https://mp.weixin.qq.com/s?__biz=MzU0OTk3ODQ3Ng==&mid=2247486693&idx=1&sn=9910e97735cea0ab525eeb802b7fb5ee)

## 原理

[Https协议与HttpClient的实现 - kingszelda - 博客园](https://www.cnblogs.com/kingszelda/p/9029735.html)

[RPC实现以及相关学习 · 语雀](https://www.yuque.com/xavior.wx/point/rpc-practice)
[Netty RPC的简易DEMO](https://mp.weixin.qq.com/s?__biz=MzAwMDczMjMwOQ==&mid=2247483688&idx=1&sn=96f589f8a9153d50fcbf834f0cd25ec8)
[你应该知道的RPC原理 - zhanlijun - 博客园](https://www.cnblogs.com/LBSer/p/4853234.html)
[整天跟微服务打交道，你不会连RPC都不知道吧？](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247488064&idx=1&sn=5b6b29701f3a338a06c575596060e0ab)
[徒手撸一个简单的RPC框架 - 掘金](https://juejin.cn/post/6844903764445364232)
[Dubbo创始人当年花10分钟撸的RPC骨架。](https://mp.weixin.qq.com/s?__biz=MzIzMzgxOTQ5NA==&mid=2247488273&idx=1&sn=020c57a0deba8814fd5e8fb7adf6f15c)

