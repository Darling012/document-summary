
[https://mp.weixin.qq.com/s/TtEdt7ylunkX9OlVs92qEw](https://mp.weixin.qq.com/s/TtEdt7ylunkX9OlVs92qEw)
### 软件工程全生命周期工具：
1. 项目管理
   1.  jira+confluence需求分析、任务分配、风险管控、bug追踪、文档管理、积累复盘
2. 开发
   1. API管理：yapi
   2. 自动化测试：
   3. 配置管理：nacos
   4. 服务发现：nacos
   5. 负载均衡、容错：nginx、gateway、ribbon、hystrix
   6. 安全与认证：oAuth2、keycloak、spring security
3. CI/CD
   1. 持续集成：git、maven、docker、jenkins
   2. 持续交付: docker-compose、docker swarm、k8s（rancher+helm）
   3. 制品库：jcr+
4. 服务监控
   1. [日志监控](日志分析)：elk、filebeat
   2. [APM](APM.md)：skywalking、zipkin
   3. [Metric监控](Metric监控)：spring-actuator、Prometheus+Alertmanager+Grafana
   4. 自愈、自动伸缩、调度与发布：k8s

**我们需要devOps做什么？**

研发效能提升。让开发更专注于开发。减少测试部署时诸如环境配置、参数等不必要的问题及沟通。

**为什么要选择统一化的CI/CD?**
https://www.toutiao.com/article/6779098800825827852/

**我们要做什么**？

符合开发场景的最佳实践。




##### APM
[微服务架构之「 监控系统 」 - 不止思考 - 博客园](https://www.cnblogs.com/jsjwk/p/10899175.html)


[Metric](Metric监控) 的特点是，它是可累加的：他们具有原子性，每个都是一个逻辑计量单元，或者一个时间段内的柱状图。 例如：队列的当前深度可以被定义为一个计量单元，在写入或读取时被更新统计; 输入 HTTP 请求的数量可以被定义为一个计数器，用于简单累加; 请求的执行时间可以被定义为一个柱状图，在指定时间片上更新和统计汇总。

[Logging](日志分析) 的特点是，它描述一些离散的(不连续的)事件。 例如：应用通过一个滚动的文件输出 Debug 或 Error 信息，并通过日志收集系统，存储到 Elasticsearch 中; 审批明细信息通过 [Kafka](http://www.oneapm.com/ci/kafka.html "Kafka")，存储到数据库(BigTable)中; 又或者，特定请求的元数据信息，从服务请求中剥离出来，发送给一个异常收集服务，如 NewRelic。

[Tracing](APM.md) 的最大特点就是，它在单次请求的范围内，处理信息。 任何的数据、元数据信息都被绑定到系统中的单个事务上。 例如：一次调用远程服务的 RPC 执行过程;一次实际的 SQL 查询语句;一次 HTTP 请求的业务性 ID。

[APM 介绍与实现\_Javadoop](https://javadoop.com/post/apm)


[Tracing 与 Metrics 的邂逅，提供更强大的 APM 能力](https://toutiao.io/posts/zmd6d9m/preview)
[微服务应用性能如何？APM监控工具来告诉你！ - 掘金](https://juejin.cn/post/6882530961406296077)
[APM介绍与主流监控工具对比 - 简书](https://www.jianshu.com/p/4b756ee173fa)