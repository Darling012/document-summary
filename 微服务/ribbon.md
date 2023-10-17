springcloud 一般使用 ribbon 作为客户端负载均衡工具，一般不单独使用，结合 restTemplate 或 [Feign](Feign.md)。
#### 如何获取注册中心的服务实例
1. ribbon 提供出接口 ServerList，注册中心实现。
2. ribbon 在初始化时向注册中心获取服务列表
3. 根据不同的 IPing 实现，向获取到的服务列表  串行  发送 ping，以此判断服务的可用性
4. 当服务可用性发生变化，重新拉取或更新服务列表
5. 对 ServerList 服务进行 IRule 负载均衡策略

##### 源码流程：
1. RibbonLoadBalancerClient
	1. 负责负载均衡的请求处理
2. ILoadBalancer
	1. 定义了一系列负载均衡算法，相当于路由的作用
	2. 默认实现类ZoneAwareLoadBalancer
3. allServerList
	1. 从注册中心获取的服务实例
	2. upServerList 代表了健康实例
`RibbonLoadBalancerClient#getLoadBalancer()` 
1. 从 ribbon 上下文中获取名称为 ribbon-produce，类型为 ILoadBalance 的 Spring Bean
2. 以 ILoadBalance 默认实现ZoneAwareLoadBalancer
	1. ZoneAwareLoadBalancer 继承 DynamicServerListLoadBalancer 继承BaseLoadBalancer
	2. BaseLoadBalancer 主要
		1. IPing 服务探测
			1. 每隔 10 秒会去 Ping 服务地址，如果返回状态不是 200，那么默认该实例下线
			2. Ribbon 客户端内置的扫描，默认每隔 30 秒去拉取 Nacos 也就是注册中心的服务实例，如果已下线实例会在客户端缓存中剔除
		2. IRule 负载均衡
	3. ....... 然后到了获取服务列表数据
		1. serverListImpl.getUpdatedListOfServers();
		2. serverListImpl 实现自 ServerList，NacosServerList

##### 负载均衡规则
1. [基于nacos注册中心的ribbon定制规则](https://www.cnblogs.com/nijunyang/p/14141028.html)

##### ribbon 灰度发布
2. [nacos+ribbon+feign+gateway设计实现灰度方案 - 盛开的太阳 - 博客园](https://www.cnblogs.com/ITPower/p/13353248.html)
3. [基于ribbon实现灰度发布_最爱奶油花生的博客-CSDN博客_ribbon灰度发布](https://blog.csdn.net/abcd930704/article/details/124141939)


## Reference
1. [SpringCloud Ribbon 负载均衡原理](https://www.modb.pro/db/109222)
2. [图解+源码讲解 Ribbon 如何获取注册中心的实例](https://juejin.cn/post/7085520967547486221)
3. [SpringCloud Nacos + Ribbon 调用服务的 2 种方法！](https://cloud.tencent.com/developer/article/1998100)
4. [nacos基础(12):nacos服务发现之Spring Cloud服务协作流程](https://itcn.blog/p/554281380.html)
5. [Nacos下 Ribbon 原理分析](https://blog.csdn.net/LarrYFinal/article/details/120942122)
6. [Ribbon 原理研究](https://juejin.cn/column/7085157997072089119) 

