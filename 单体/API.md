# API

[加密加签](加密加签.md)
[restful](restful.md)
[响应体及状态码设计](响应体及状态码设计.md)
1. [API 设计最佳实践](https://mp.weixin.qq.com/s/QIwMg1dYUpBZ6PTrtwaw1Q)
2. [https://mp.weixin.qq.com/s/mv2beuGE_7_wveDAZE9Mxw](https://mp.weixin.qq.com/s/mv2beuGE_7_wveDAZE9Mxw)


[GET和POST两种基本请求方法的区别 - 在途中# - 博客园](https://www.cnblogs.com/logsharing/p/8448446.html)




1.  老师布置作业写妈妈和写亲人区别
    1.  老师相当于对外提供接口，让学生接入，相当于方法的入参，写妈妈就很具体就没考虑到单亲家庭学生，写亲人，就比较抽象，学生就有更多的选择
    2.  知秋讲代码跟生活结合，不知道有没有讲过此类案例
2.  晓风轻规范：入参尽量抽象，返回值尽量具体


[如何健壮你的后端服务？](https://mp.weixin.qq.com/s/g2Hpk8AAcLs_a1p2kISj2A)
1. 怀疑第三方
	1. 有兜底的，制定好业务降级方案
	2. 快速失败原则，设置超时时间
	3. 适当保护第三方，慎重选择重试机制
2. 防备使用方
	1. 设计一个好的 API, 避免误用
	2. 流量控制
	3. 做好自己
		1. 单一职责原则
		2. 控制资源的使用
			1. CPU 资源怎么限制
			2. 内存资源怎么限制
			3. 网络资源怎么限制
			4. 磁盘资源怎么限制
		3. 避免单点

## APIDOC
### openAPI
[OpenAPI规范系列教程 - OpenAPI简介 - 简书](https://www.jianshu.com/p/a65d638f0315)

#### yapi
[顶尖 API 文档管理工具 (YAPI) - 简书](https://www.jianshu.com/p/a97d2efb23c5)
[API统一管理平台-YApi - 掘金](https://juejin.cn/post/6844903918145634312)
