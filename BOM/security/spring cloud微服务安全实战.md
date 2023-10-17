[Spring cloud微服务安全实战](https://www.cnblogs.com/lihaoyang/tag/Spring%20Cloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E5%AE%9E%E6%88%98/)
[Spring cloud微服务安全实战 - 标签 - GASA - 博客园](https://www.cnblogs.com/wangjunwei/tag/Spring%20cloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E5%AE%9E%E6%88%98/)
## 第1章 课程导学  
### 1-1 课程导学
```
file:///Volumes/resources/security/Spring Cloud微服务安全实战[qiudaoyu]/第1章 课程导学/1-1课程导学.mp4
```
##### 完整的知识体系  
[01:55](file:///Volumes/resources/security/Spring%20Cloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E5%AE%9E%E6%88%98[qiudaoyu]/%E7%AC%AC1%E7%AB%A0%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6/1-1%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4#t=115.055615)
防攻击、监控、认证、授权、加密、报警、限流、熔断、降级、隔离
##### 系统性思考
[03:19](file:///Volumes/resources/security/Spring%20Cloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E5%AE%9E%E6%88%98[qiudaoyu]/%E7%AC%AC1%E7%AB%A0%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6/1-1%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4#t=199.798759)
回顾、总结、思考、体系
##### 课程大纲
[04:14](file:///Volumes/resources/security/Spring%20Cloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E5%AE%9E%E6%88%98[qiudaoyu]/%E7%AC%AC1%E7%AB%A0%20%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6/1-1%E8%AF%BE%E7%A8%8B%E5%AF%BC%E5%AD%A6.mp4#t=254.638476)
1. 单个 api 安全
2. 微服务场景下 针对网关
3. 深入微服务认证和 sso
4. 微服务之间的，当系统庞大后，上千个微服务，优惠券积分等
5. 微服务可用性，监控和报警
## 第2章 环境搭建
开发工具的介绍及安装，介绍项目代码结构并搭建，基本的依赖和参数设置。
2-1 环境安装
## 第3章 API安全
我们从简单的API场景入手，讲述API安全相关的知识。首先我们会介绍要保证一个API安全都需要考虑哪些问题，然后我们针对这些问题介绍常见的安全机制，我们会针对每种问题和安全机制编写相应的代码，让大家对这些问题和安全机制有一个初步的认识。
### 3-1 API 安全 常见的安全机制
```
file:///Volumes/resources/security/Spring Cloud微服务安全实战[qiudaoyu]/第3章 API安全/3-1API安全常见的安全机制.mp4

[Spring cloud微服务安全实战-课程章节](https://coding.imooc.com/class/chapter/379.html#Anchor)
```
##### API 安全
信息安全、应用安全、网络安全
##### API 安全目标:CIA
confidentiality： 机密性，确保信息只被预期用户访问
integrity: 完整性，防止未授权操作
availability: 可用性，API 总是可用

##### 常见 API 风险 SPTIDE
| 名词                   | 解释     | 解决         |
| ---------------------- | -------- | ------------ |
| spoofing               | 欺骗，伪装成某人    | 通过认证解决 |
| tampering              | 干预，做了不该干的事     | 授权         |
| repudiate              | 否认，拒绝承认做过的事     | 审计         |
| information disclosure | 信息泄露 | 授权、加密     |
| define of service      | 拒绝服务 | 流控         |
| elevation of privilege | 越权，做了不能做的事    | 授权         |

加密的数据--->流控（API 请求过多）--->认证（确保身份）--->审计（记录时间什么人做了啥）--->授权（是否可以执行）--->业务逻辑

### 3-2 第一个 API 及注入攻击防护
```
file:///Volumes/resources/security/Spring Cloud微服务安全实战[qiudaoyu]/第3章 API安全/3-2第一个API及注入攻击防护.mp4
```
使用了用户输入的但是没有校验过得数据来拼装成可执行的指令
[08:34](file:///Volumes/resources/security/Spring%20Cloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E5%AE%9E%E6%88%98[qiudaoyu]/%E7%AC%AC3%E7%AB%A0%20API%E5%AE%89%E5%85%A8/3-2%E7%AC%AC%E4%B8%80%E4%B8%AAAPI%E5%8F%8A%E6%B3%A8%E5%85%A5%E6%94%BB%E5%87%BB%E9%98%B2%E6%8A%A4.mp4#t=514.463364)
#####  sql 注入 拼接字符串 `or 1=1`
[15:14](file:///Volumes/resources/security/Spring%20Cloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E5%AE%9E%E6%88%98[qiudaoyu]/%E7%AC%AC3%E7%AB%A0%20API%E5%AE%89%E5%85%A8/3-2%E7%AC%AC%E4%B8%80%E4%B8%AAAPI%E5%8F%8A%E6%B3%A8%E5%85%A5%E6%94%BB%E5%87%BB%E9%98%B2%E6%8A%A4.mp4#t=914.024137)
1. 传入参数校验
2. 控制 jdbc 登录用户权限
3. 使用 jpa，spring data jpa 做了防注入
4. jdbc 用 ? 占位符避免
5. mybatis mapper. xml 里的 `${}` 是拼接字符串，有风险。`#{}` 使用 `（?）` 绑定可避免

### 3-3 API 安全机制之流控
```
file:///Volumes/resources/security/Spring Cloud微服务安全实战[qiudaoyu]/第3章 API安全/3-3API安全机制之流控.mp4
```
##### 流控
`http.state` = 429, too many request
认证审核会消耗资源，所以流控要放在最前面
##### 实现
[04:31](file:///Volumes/resources/security/Spring%20Cloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E5%AE%9E%E6%88%98[qiudaoyu]/%E7%AC%AC3%E7%AB%A0%20API%E5%AE%89%E5%85%A8/3-3API%E5%AE%89%E5%85%A8%E6%9C%BA%E5%88%B6%E4%B9%8B%E6%B5%81%E6%8E%A7.mp4#t=271.55978)
oncePreRequestFilter  一次请求只通过一次
guava 限流 rateLimitFilter
##### 在哪做流控？
[01:54](file:///Volumes/resources/security/Spring%20Cloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E5%AE%9E%E6%88%98[qiudaoyu]/%E7%AC%AC3%E7%AB%A0%20API%E5%AE%89%E5%85%A8/3-3API%E5%AE%89%E5%85%A8%E6%9C%BA%E5%88%B6%E4%B9%8B%E6%B5%81%E6%8E%A7.mp4#t=114.290425)
1. 集群
	1. 负载均衡
	2. 反向代理
2. 单体服务，自己代码

### 3-4 API安全机制之认证（1）
 认证 401，验证是否合法，不成功要往下走，审计记录，授权决定执不执行。
 登录      获取身份证明，不成功直接返回

### 3-5 API安全机制之认证（2）
##### 3.5 常见问题
jsr 校验，DAO 层也要加，业务处理过程中数据改变
加密
1. md5 是摘要算法，不是加密算法，两个一样的串摘要后是一样的
2. AES，可逆
3. 传过来的密码能不能加密成库中的串比对
4. 盐，采用随机串做盐。用户相关信息做盐，存在哪？
https 访问
1. 保证到达服务之前的安全
	1. 验证双方身份，保证访问的网站肯定是真的
	2. 数据传输   加密
### 3-6 API安全机制之数据校验
##### 3.6 审计
1. 认证之后，授权之前
2. 请求进来时记录，出去时更新
3. 用拦截器实现
4. jpa 审计
### 3-7 API安全机制之数据加密
##### 3.7 授权 403
ACL: 用户《——》权限。 直接绑定这个人能干什么。Linux 用户
RBAC: 引入角色
拦截器实现

### 3-8 API安全机制之Https
session fixcution 攻击，session 固定攻击
### 3-9 API安全机制之审计日志

### 3-10 API安全机制之授权

### 3-11 API安全机制之登录

### 3-12 session固定攻击防护

### 3-13 重构代码

### 3-14 章节小结

## 第4章 微服务网关安全
我们会从简单的API场景过渡到复杂的微服务场景，首先我们会介绍在微服务场景下，相比简单的API场景，我们会面临哪些新的挑战。后我们会介绍一个中小企业中常见的微服务架构，同时也会介绍OAuth2协议，我们会讲一下这个架构和OAuth2协议如何一起解决这些新的挑战。在这一章的后半部分，我们会在网关上做一些开发，演示如何在...

### 4-1 章节概述
```
file:///Volumes/resources/security/Spring Cloud微服务安全实战[qiudaoyu]/第4章 微服务网关安全/4-1章节概述.mp4
```

### 4-2 微服务安全的新挑战
```
file:///Volumes/resources/security/Spring Cloud微服务安全实战[qiudaoyu]/第4章 微服务网关安全/4-2微服务安全的新挑战.mp4
```
##### 微服务面临的安全挑战
[00:09](file:///Volumes/resources/security/Spring%20Cloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E5%AE%9E%E6%88%98[qiudaoyu]/%E7%AC%AC4%E7%AB%A0%20%E5%BE%AE%E6%9C%8D%E5%8A%A1%E7%BD%91%E5%85%B3%E5%AE%89%E5%85%A8/4-2%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E7%9A%84%E6%96%B0%E6%8C%91%E6%88%98.mp4#t=9.371224)
1. 更多的入口点，业务逻辑不再是在单一的进程里
2. 性能问题，RPC
3. 服务间的通讯安全
4. 请求追踪
	1. log 日志 
	2. metrics 指标监控，时间窗口 数字 
	3. tracking
5. 容器化部署导致证书和访问控制问题  #容器化导致与主机信息不一致 、
	1. [微服务网关安全 - 简书](https://www.jianshu.com/p/a2ad42d0a6fe)
6. 共享用户状态
### 4-3 常见的微服务安全整体架构

### 4-4 OAuth2协议与微服务安全
##### 4.4 oauth2 
1. 用户
2. 客户端应用
3. 认证服务器
4. 资源服务器
### 4-5 搭建 OAuth2 认证服务器
通过继承 `AuthorizationServerConfigurerAdapter` 来配置
1. 配置客户端
	1. 让认证服务器知道哪些客户端来申请令牌
	2. `ClientDetailsServiceConfigurer`：客户端的详情服务的配置
2. 配置用户
	1. 校验用户信息是否合法
	2. 给 `AuthorizationServerEndpointsConfigurer` 一个 `AuthenticationManager`
		1. 只有一个 `authenticate` 方法，常见认证方式都已经提供实现，接收一个 `Authentication` 并返回一个封装了认证信息的 `Authentication`
		2. 不同认证方式信息不一样，用户名密码等，此方法验证完更新信息返回。
		3. `AuthenticationManager` 通过 `WebSecurityConfigurerAdapter` 构建，由我们提供两个类
		4. `UserDetailsService` 获取用户信息，`PasswordEncoder` 比对密码
3. 资源服务器
	1. 配置资源服务器来验 token 的规则，必须携带 1 中配置的信息等。
### 4-6 搭建 OAuth2 资源服务器
通过 `ResourceServerConfigurerAdapter` 配置
1. 配置 ID 等自身信息
	1. `ResourceServerSecurityConfigurer`，去 auth 验 token 时携带的自身服务名等信息
2. 资源 url 请求拦截配置
	1. `HttpSecurity` 哪些拦，哪些布兰
3. 怎么去验证令牌
	1. `WebSecurityConfigurerAdapter` 配置
		1. 自定义 `ResourceServerTokenServices`，去哪验 token
		2. 重写 `authenticationManagerBean()` 方法，将 1 注入，让系统用起来

###### 业务中怎么知道 token 是哪个用户？
` @AuthenticationPrincipal` 获取

###### 令牌头前缀
basic、bearer

### 4-7 重构代码以适应真实环境
资源服务器
1. 配置权限表达式
2. 配置 token 转 user
3. 配置持久化，tokenstore

### 4-8 Zuul 网关安全开发（一）
1. 安全处理和业务逻辑耦合，微服务最主要就是解耦
2. 验证服务压力过大
3. 多个服务同时暴露，增加了外部访问的复杂性
 引入网关

### 4-9 Zuul网关安全开发（二）

### 4-10 Zuul网关安全开发（三）

### 4-11 Zuul网关安全开发（四）

## 第5章 微服务身份认证和SSO
我们会学习微服务安全中比较重要的一个话题:身份认证，我会在微服务环境下实现一个前后端完全分离的单点登录(SSO)。在这个过程中，我们会进一步介绍OAuth2协议中的各种授权协议，以及如何使用这些协议达成我们的目标。最终我们会实现两个版本的SSO:基于服务器Session的实现和基于浏览器Cookie的实现。

### 5-1 单点登录基本架构

##### session sso
浏览器-》前端服务器（nginx 换位 node）-》认证
问题：
1. seo 搜索引擎只能识别静态 HTML 资源
2. 页面渲染在浏览器完成，压力大效果不好。

获取 token 后放入前端服务器 session，rpc 时，网关 filter 从 session 中取出 token 存入请求头，登出时使 token 失效。
当前存在
1. 认证服务器 session 的有效期
2. 客户端 session 存了 token 的有效期
3. 还有 token 有效期

1. 控制多长时间需要用户输入用户名密码
2. 登录一次能访问多久的微服务
3. 控制多久跳一次认证服务器

解决三个失效时间冲突：
1. 点击登出，客户端、认证 session 都失效
2. token 短活，拿 refreshToken、clientID、security 去刷新 token，每次先从 session 中取出来判断是否过期
3. refreshToken 过期，重新去认证服务器走流程，又分为两种情况
	1. 认证服务器 session 没过期，走流程时，认证服务器会直接返回
	2. 过期，直接重新登录
4. 刷新 token 时，refreshToken 可能过期，捕获异常，重新登录
juejin.cn/post/6932702419344162823
优点：
1. 安全：所有 token 都存在 session 里，浏览器只有一个 sessionId
2. 可控制高：token 信息存在数据库，登录有 redis，可以主动操作失效
3. 跨域：客户端布在哪个域名下都可以直接跟认证服务器交互

缺点：
1. 复杂度高，过期时间多
2. 性能低

##### cookie sso
客户端拿到 token 后，不是放入 session 而是写入 cookie，refreshToken 也放入 cake
1. 登录状态，当 cookie 里的 refreshToken 失效才会去认证服务器做登录，这样认证服务器 session 有效期可以很短
2. 安全性低，token 存在了浏览器
3. 可控性低，存在客户浏览器里，没法主动失效
4. 跨域：只存二级域名可以做 sso, 使用 Https 保证 cookie 传递

##### 区别
1. 存 session
	1. 客户端 session 失效，就需要登录认证，所以服务器 session 时间要很长来保证 sso
2. 存 cookie
	1. 由 refreshToken 决定能不能访问其他服务




### 5-2 前端页面改造

### 5-3 后端服务改造

### 5-4 请求转发及退出

### 5-5 实现授权码认证流程（1）
```
file:///Volumes/resources/security/Spring Cloud微服务安全实战[qiudaoyu]/第5章 微服务身份认证和SSO/5-5实现授权码认证流程（1）.mp4
```
![](5-5实现授权码认证流程（1）-0001.png)
用户在登录的时候，是把直接跳认证服务器，这样各个服务完全接触不到敏感信息，在认证服务器完成认证
### 5-6 实现授权码认证流程（2）

### 5-7 实现基于session的SSO（客户端应用的Session有效期）

### 5-8 实现基于session的SSO（认证服务器的session有效期）

### 5-9 实现基于session的SSO（Token有效期）

### 5-10 实现基于session的SSO（Token有效期）

### 5-11 实现基于token的SSO（1）

### 5-12 实现基于token的SSO（2）

## 第6章 微服务之间的通讯安全

我们会聚焦于微服务之间的安全。我们会演示如何使用CA分发证书，保证微服务之间的通讯是安全的。我们也会讲解如何使用JWT来确保微服务之间的通讯安全。最后，我们会介绍一下阿里的开源框架Sentinel，以及如何使用Sentinel来实现集中式的微服务流控、熔断和降级管理，以确保微服务的可用性。...

### 6-1 本章概述
##### 网关做限流的问题
A 限 100，B 限 100，A 调用 B , B 就 200 了
身份认证：网关上每个请求都要验证令牌，不安全，之前 username 放在请求头里
授权问题：和限流类似
### 6-2 JWT认证之认证服务改造
JWT 本身不是加密的，用 key 对 token 签名，使用 token 的用同样的可以验签成功，则说明没被篡改
### 6-3 JWT改造之网关和服务改造
不再发校验 token 的请求，直接从 jwt 中读取，从认证服务器获取 key
### 6-4 权限控制改造

### 6-5 jwt改造之日志及错误处理（1）

### 6-6 jwt改造之日志及错误处理（2）

### 6-7 jwt改造总结

### 6-8 sentinel限流实战

### 6-9 sentinel之熔断降级

### 6-10 sentinel之热点和系统规则

### 6-11 sentinel之配置持久化

## 第7章 微服务监控和报警

我们会介绍微服务的监控、追踪和报警。首先我们会介绍如何使用Promethus + Grafana + AlertManger来采集微服务的Metrics信息，并基于这些信息自动报警。其次我们会介绍如何使用 ElasticSearch + FileBeat + Kibana 来采集和查询微服务日志信息。最后我们会介绍一个调用链路追踪工具 pinpoint，用来监控微服务
### 7-1 章节概述
```
file:///Volumes/resources/security/Spring Cloud微服务安全实战[qiudaoyu]/第7章 微服务监控和报警/7-1章节概述.mp4
```
##### 微服务的可见性保证及时发现问题
[01:07](file:///Volumes/resources/security/Spring%20Cloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E5%AE%9E%E6%88%98[qiudaoyu]/%E7%AC%AC7%E7%AB%A0%20%E5%BE%AE%E6%9C%8D%E5%8A%A1%E7%9B%91%E6%8E%A7%E5%92%8C%E6%8A%A5%E8%AD%A6/7-1%E7%AB%A0%E8%8A%82%E6%A6%82%E8%BF%B0.mp4#t=67.064171)
1. 日志监控
	1. 描述离散的不联系的事件信息，用文字表示
2. 指标监控
	1. 能用数字表示的信息，如 cpu 利用率，内存用了多少
3. 调用链监控
	1. 针对单次请求，发生了什么事情，调用了哪些服务，哪个数据库

### 7-2 docker快速入门

### 7-3 prometheus环境搭建
```
file:///Volumes/resources/security/Spring Cloud微服务安全实战[qiudaoyu]/第7章 微服务监控和报警/7-3prometheus环境搭建.mp4
```

##### prometheus
[02:11](file:///Volumes/resources/security/Spring%20Cloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%AE%89%E5%85%A8%E5%AE%9E%E6%88%98[qiudaoyu]/%E7%AC%AC7%E7%AB%A0%20%E5%BE%AE%E6%9C%8D%E5%8A%A1%E7%9B%91%E6%8E%A7%E5%92%8C%E6%8A%A5%E8%AD%A6/7-3prometheus%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA.mp4#t=131.001387)


### 7-4 整合SpringBoot和Prometheus

### 7-5 配置grafana图表及报警

### 7-6 自定义metrics监控指标（1）

### 7-7 自定义metrics监控指标（2）

### 7-8 ELK+SpringBoot环境搭建

### 7-9 自定义日志采集的格式和内容

### 7-10 ELK日志采集架构优化

### 7-11 PinPoint+SpringBoot环境搭建

### 7-12 整合链路追踪和日志监控

### 7-13 章节总结

## 第8章 课程总结
这一章我们会回顾整门课中学到的知识，并对后面的进一步学习指出一些方向
### 8-1 课程总结
