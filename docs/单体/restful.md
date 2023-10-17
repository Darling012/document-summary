# restful
web 服务器：[Nginx](Nginx.md)、apache 只能解析静态资源
应用服务器： [tomcat](devOps/tomcat.md)、jetty 等 servlet 容器, netty
部署时，web 服务器暴露端口外网访问，应用服务器只能内网访问。nginx 部署证书，外网使用 https 访问，并且只开放 443 和 80 端口，其他关闭，防止黑客扫描。

1. 接口数据即显示，前端仅做渲染逻辑处理。（前端做，使用客户浏览器资源）
2. 渲染逻辑禁止跨多个接口调用
3. 前端关注交互，渲染逻辑，尽量避免业务逻辑处理。
4. 响应 json 应简单，避免多级 json。
5. 布尔值用 0、1 表示
6. 日期、long 用 string，多端可用时间戳，大前端自己转换
7. 对外的 ID 字段使用字符串类型，不要使用自增的数字，不要用 long，会超长，js 报错
8. 基本类型使用包装类型
##### reference
1. [RESTful API 最佳实践 - 阮一峰的网络日志](https://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html)
2. [RESTful API 中 四种操作对应说明 及 常用HTTP 状态码的定义](https://blog.csdn.net/qq_39028239/article/details/79893399)
3. [Site Unreachable](https://github.com/aisuhua/restful-api-design-references)
4. [https://mp.weixin.qq.com/s?__biz=Mzg3MjA4MTExMw==&mid=2247486344&idx=1&sn=f8d1e356e25198820e593a72469a12cf](https://mp.weixin.qq.com/s?__biz=Mzg3MjA4MTExMw==&mid=2247486344&idx=1&sn=f8d1e356e25198820e593a72469a12cf)
5. [RESTFUL URL命名原则_360linker的博客-CSDN博客_restful url 大小写](https://blog.csdn.net/belalds/article/details/80060296)
6. [https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247485984&idx=1&sn=e5262817fe46a29b7e0f7e537d4c6cb7](https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247485984&idx=1&sn=e5262817fe46a29b7e0f7e537d4c6cb7)
7. [https://mp.weixin.qq.com/s?__biz=MzI3ODcxMzQzMw==&mid=2247486775&idx=2&sn=870da6b84065df709d292724b6f2244f](https://mp.weixin.qq.com/s?__biz=MzI3ODcxMzQzMw==&mid=2247486775&idx=2&sn=870da6b84065df709d292724b6f2244f)
8. [https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247488585&idx=1&sn=3291a2952ea8073ceb5e2ff2838f47d8](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247488585&idx=1&sn=3291a2952ea8073ceb5e2ff2838f47d8)
9. [https://mp.weixin.qq.com/s?__biz=MzAwMjk5Mjk3Mw==&mid=2247492149&idx=1&sn=dcf314f5ce0f78f0f6dddb2a3ffdb3f6](https://mp.weixin.qq.com/s?__biz=MzAwMjk5Mjk3Mw==&mid=2247492149&idx=1&sn=dcf314f5ce0f78f0f6dddb2a3ffdb3f6)
10. [https://mp.weixin.qq.com/s/Ta-nt7YH01AbwU45aUPaKg](https://mp.weixin.qq.com/s/Ta-nt7YH01AbwU45aUPaKg)

## 前后端分离

1. [https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247486417&idx=1&sn=9f2c3848bf71d7ed5101bd8aefd54ec0](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247486417&idx=1&sn=9f2c3848bf71d7ed5101bd8aefd54ec0)
2. [https://www.jianshu.com/p/c81008b68350](https://www.jianshu.com/p/c81008b68350)
3. [https://mp.weixin.qq.com/s?__biz=MzAxNjk4ODE4OQ==&mid=2247485680&idx=1&sn=e6cf06f0bde86b057791c356d2084bbf](https://mp.weixin.qq.com/s?__biz=MzAxNjk4ODE4OQ==&mid=2247485680&idx=1&sn=e6cf06f0bde86b057791c356d2084bbf)
4. [https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247491760&idx=4&sn=1baf40eb3f10352f814b94cef1e5a009](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247491760&idx=4&sn=1baf40eb3f10352f814b94cef1e5a009)
5. [node.js - 前后端分离实践（一）_个人文章 - SegmentFault 思否](https://segmentfault.com/a/1190000009329474?_ea=2038402)
6. [一篇来自前端同学对后端接口的吐槽 - 掘金](https://juejin.cn/post/6844903861841313806)
7. [https://mp.weixin.qq.com/s?__biz=MzAxOTQxOTc5NQ==&mid=2650500370&idx=1&sn=e5ea384e441b07f67611d5c905e3d9c5](https://mp.weixin.qq.com/s?__biz=MzAxOTQxOTc5NQ==&mid=2650500370&idx=1&sn=e5ea384e441b07f67611d5c905e3d9c5)
8. [大家前后端规范是怎么制定的呢？ - V2EX](https://www.v2ex.com/t/445812)
9. [https://mp.weixin.qq.com/s/_OOEKk-S12W4KVkjOtDlvw](https://mp.weixin.qq.com/s/_OOEKk-S12W4KVkjOtDlvw)
10. [https://mp.weixin.qq.com/s/yMcHukjENIdAJg6-ysxT0w](https://mp.weixin.qq.com/s/yMcHukjENIdAJg6-ysxT0w)
11. [https://mp.weixin.qq.com/s/R81PWSolr6r0Rllpfv3F9Q](https://mp.weixin.qq.com/s/R81PWSolr6r0Rllpfv3F9Q)
12. [Site Unreachable](http://codeburst.io/best-practices-api-design-61d4697d17ff)

[关于pojo中的null与默认值](关于pojo中的null与默认值.md)
