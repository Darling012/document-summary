# gRPC

[一个恋爱小故事告诉你什么是gRPC？！ | Go 技术论坛](https://learnku.com/articles/58641)



[分类: 深入浅出 gRPC | JUST DO IT](https://leeshengis.com/categories/%E6%B7%B1%E5%85%A5%E6%B5%85%E5%87%BA%20gRPC)

## gRPC 快在哪
[grpc-我们为什么要用gRpc？gRpc快在哪里？ - 掘金](https://juejin.cn/post/6969787751084654605)
1. protobuf 比 json快
2. http2.0比http1.1性能高

## 协议
##### 为什么 gRPC 采用 http 2 而不是 tcp
感觉不是从性能角度考虑的而是适用度。
[gRPC 底层的通信框架基于Netty 4.1构建，通过集成Netty的HTTP/2协议栈](https://www.cnblogs.com/moonyaoo/p/12932602.html)，[gRPC是如何与Netty进行融合](https://codingrookieh.github.io/grpc%E4%BB%8E%E5%85%A5%E9%97%A8%E5%88%B0%E6%94%BE%E5%BC%83/2018/09/02/grpc-netty-analysis/)

1. [思考gRPC ：为什么是HTTP/2 | 横云断岭的专栏](https://hengyun.tech/thinking-about-grpc-http2/)
2. [gRPC为什么要使用http作为传输协议？难道要跟xml-http/json-http一决雌雄？ - 知乎](https://www.zhihu.com/question/52670041)
	1. 做 gRPC 时当面问过项目领队这个问题。他的答案是：许多客户端要通过 HTTP 代理来访问网络，gRPC 全部用 HTTP/2 实现，等到代理开始支持 HTTP/2 就能透明转发 gRPC 的数据。不光如此，负责负载均衡、访问控制等等的反向代理都能无缝兼容 gRPC，比起自己设计 wire protocol 的 Thrift，这样做科学不少。

## protobuf
![序列化与编解码](序列化与编解码.md#protobuf)

[思考gRPC ：为什么是protobuf | 横云断岭的专栏](https://hengyun.tech/thinking-about-grpc-protobuf/)



## reference
[记一次 Go 协程泄漏的排查过程](https://taoshu.in/go/go-case-study-of-goroutine-leak.html)
