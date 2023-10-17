## 第 8 章服务容错-Sentinel
### 8-1 雪崩效应
```
file:///Volumes/resources/cloud/大目-Spring Cloud Alibaba从入门到进阶/第8章服务容错-Sentinel/8-1雪崩效应.mp4
```
[01:58](file:///Volumes/resources/cloud/%E5%A4%A7%E7%9B%AE-Spring%20Cloud%20Alibaba%E4%BB%8E%E5%85%A5%E9%97%A8%E5%88%B0%E8%BF%9B%E9%98%B6/%E7%AC%AC8%E7%AB%A0%E6%9C%8D%E5%8A%A1%E5%AE%B9%E9%94%99-Sentinel/8-1%E9%9B%AA%E5%B4%A9%E6%95%88%E5%BA%94.mp4#t=118.660597)
基础服务故障导致上层服务故障并且不断放大的过程叫做
cascading failure、 级联失效、 级联故障、  雪崩效应
### 8-2 常见容错方案
```
file:///Volumes/resources/cloud/大目-Spring Cloud Alibaba从入门到进阶/第8章服务容错-Sentinel/8-2常见容错方案.mp4
```

1. 超时
2. 限流
3. 舱壁模式 
	1. [02:04](file:///Volumes/resources/cloud/%E5%A4%A7%E7%9B%AE-Spring%20Cloud%20Alibaba%E4%BB%8E%E5%85%A5%E9%97%A8%E5%88%B0%E8%BF%9B%E9%98%B6/%E7%AC%AC8%E7%AB%A0%E6%9C%8D%E5%8A%A1%E5%AE%B9%E9%94%99-Sentinel/8-2%E5%B8%B8%E8%A7%81%E5%AE%B9%E9%94%99%E6%96%B9%E6%A1%88.mp4#t=124.203727)
	2. 某个 controller 有独立的线程池
4. 断路器模式
	1. [05:00](file:///Volumes/resources/cloud/%E5%A4%A7%E7%9B%AE-Spring%20Cloud%20Alibaba%E4%BB%8E%E5%85%A5%E9%97%A8%E5%88%B0%E8%BF%9B%E9%98%B6/%E7%AC%AC8%E7%AB%A0%E6%9C%8D%E5%8A%A1%E5%AE%B9%E9%94%99-Sentinel/8-2%E5%B8%B8%E8%A7%81%E5%AE%B9%E9%94%99%E6%96%B9%E6%A1%88.mp4#t=300.467706)
	2. 时间段内达到设定的阈值就跳闸，过会尝试，再恢复 #断路器三态切换
	3. ![](Pasted%20image%2020221023204306.png)

##### 服务容错的思想
[07:39](file:///Volumes/resources/cloud/%E5%A4%A7%E7%9B%AE-Spring%20Cloud%20Alibaba%E4%BB%8E%E5%85%A5%E9%97%A8%E5%88%B0%E8%BF%9B%E9%98%B6/%E7%AC%AC8%E7%AB%A0%E6%9C%8D%E5%8A%A1%E5%AE%B9%E9%94%99-Sentinel/8-2%E5%B8%B8%E8%A7%81%E5%AE%B9%E9%94%99%E6%96%B9%E6%A1%88.mp4#t=459.228955)
超时：只要释放够快，就不容易瘫痪
限流：只吃自己饭量的饭
舱壁模式：不把鸡蛋放在一个篮子里
断路器：监控+开关

## 第 11 章微服务的用户认证与授权
##### client->nginx->tomcat 集群-> sessionStore
1. sessionStore 挂了就全完了
2. 迁移的话所有地址都要改
3. 性能容量瓶颈
改进：
粘性会话：相同 ip 请求，nginx 转发到相同的 Tomcat 实例，也就无需 sessionStore 了。缺点是，断网重连，ip 变了，路由到别的 Tomcat 就要重新登陆


##### client（携带 token、session）->nginx->tomcat 集群
无状态：服务器不保存用户状态，客户浏览器保存，服务器解密校验 token。
token 一旦颁发，就还难控制。token、session 都做校验，session 服务端保存。

1. 处处安全方案： oauth2、spring cloud security、keycloak
2. 外部无状态，内部有状态
	1. client (sessionId、token) -> api 网关 -> 负载均衡 -> 微服务集群
	2. 请求到达微服务集群后
		1. 微服务：解密 token
		2. 传统服务: 拿 sessionID 去 sessionStore 验证
	3. 可以慢慢从传统架构向微服务架构重构
3. 网关认证授权，内部裸奔
	1. 在 API 网关直接解密 token，然后把数据（userid、name 等）向后边微服务明文传递
	2. 网关责任重
4. 内部裸奔改进
	1. 网关转发 token，每个微服务解密 token，密钥泄露风险，借助配置方案解决

授权访问控制模型：ACL、RBAC、ABAC、基于时间/规则
jwt:RFC7519