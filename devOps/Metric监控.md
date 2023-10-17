# Metric监控
##### 作用
1. 定位故障
2. 预警
3. 辅助容量规划
4. 性能调优


先量化再改进

#####  对象及指标
1. 服务器监控
	1. CPU 使用率、内存使用率、磁盘使用率、磁盘读写速度、网络 IO 流量等
	2. 

1. [监控作用、对象、指标](https://mp.weixin.qq.com/s/x_TosBZU3qELoHaRUKw52w)
2. [Spring boot 3.0 可观测性增强](https://mp.weixin.qq.com/s/9gvkBv1f-7Q4RsnmimRGzA)

## spring boot admin + spring boot actuator
1. spring boot actuator 提供探测信息，健康检查，线程信息，JVM 堆信息，指标收集，运行情况监控等
2. Spring Boot Admin分为服务端（spring-boot-admin-server）和客户端（spring-boot-admin-client）两个组件：
   - spring-boot-admin-server通过采集actuator端点数据显示在spring-boot-admin-ui上，已知的端点几乎都有进行采集。
   - spring-boot-admin-client是对Actuator的封装，提供应用系统的性能监控数据。此外，还可以通过spring-boot-admin动态切换日志级别、导出日志、导出heapdump、监控各项性能指标等。
[Spring Boot Admin 参考指南](https://consolelog.gitee.io/docs-spring-boot-admin-docs-chinese/)
[SpringBoot监控 - 集成springboot admin监控工具 | Java 全栈知识体系](https://pdai.tech/md/spring/springboot/springboot-x-monitor-boot-admin.html)

##### JVM监控
[给你的SpringBoot做埋点监控--JVM应用度量框架Micrometer - 云扬四海 - 博客园](https://www.cnblogs.com/yunlongn/p/11343848.html)
##### 集成Prometheus
1. [Spring Boot Actuator详解与深入应用（三）：Prometheus+Grafana应用监控 - 掘金](https://juejin.cn/post/6844903725148930061)
2. [Prometheus + Spring Boot 应用监控_51CTO博客_prometheus监控应用程序](https://blog.51cto.com/u_15103030/2649057)
3. [Spring Boot 微服务应用集成Prometheus + Grafana 实现监控告警 - 简书](https://www.jianshu.com/p/89660c621663)
4. [面试官：聊一聊SpringBoot服务监控机制_双子孤狼的博客-CSDN博客_sprintboot 如何监控http当前连接数](https://blog.csdn.net/zwx900102/article/details/115446997)



## Prometheus+grafana
##### 角色分配：
- [Prometheus](#Prometheus) 采集数据
- Grafana 用于图表展示
- AlertManager 处理由客户端应用程序（如Prometheus server）发送的警报。它负责将重复数据删除，分组和路由到正确的接收者集成，还负责沉默和抑制警报
- redis_exporter 用于收集 redis 的 metrics
- [node-exporter](#node-exporter) 用于收集操作系统和硬件信息的 metrics
- cadvisor 用于收集 docker 的相关 metrics

#### 部署
创建/data/prometheus/prometheus.yml

```yaml
global:
  scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).
# Alertmanager configuration
alerting:
  alertmanagers:
  - static_configs:
    - targets: ['10.10.170.161:9093']
      # - alertmanager:9093

# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
  - "node_down.yml"
  # - "first_rules.yml"
  # - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'prometheus'

    # metrics_path defaults to '/metrics'
    # scheme defaults to 'http'.

    static_configs:
    - targets: ['192.168.215.54:9090']

  - job_name: 'redis'
    static_configs:
     - targets: ['192.168.215.54:9121']
       labels:
         instance: redis

  - job_name: 'node'
    scrape_interval: 8s
    static_configs:
     - targets: ['192.168.215.54:9100']
       labels:
         instance: node

  - job_name: 'cadvisor'
    static_configs:
     - targets: ['192.168.215.54:8088']
     labels:
         instance: cadvisor
```
创建/data/prometheus/docker-compose-prometheus.yml
```yaml
   version: '3.7'
   
   networks:
       monitor:
           driver: bridge
   
   services:
       prometheus:
           image: prom/prometheus
           container_name: prometheus
           hostname: prometheus
           restart: always
           volumes:
               - /data/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
               - /data/prometheus/node_down.yml:/etc/prometheus/node_down.yml
           ports:
               - "9090:9090"
           networks:
               - monitor
           command:
               - '--config.file=/etc/prometheus/prometheus.yml'
               - '--web.enable-lifecycle'
   
       grafana:
           image: grafana/grafana
           container_name: grafana
           hostname: grafana
           restart: always
           ports:
               - "3000:3000"
           networks:
               - monitor
       redis-exporter:
           image: oliver006/redis_exporter
           container_name: redis_exporter
           hostname: redis_exporter
           restart: always
           ports:
               - "9121:9121"
           networks:
               - monitor
           command:
               - '--redis.addr=redis://192.168.215.54:7000'
       node-exporter:
           image: quay.io/prometheus/node-exporter
           container_name: node-exporter
           hostname: node-exporter
           restart: always
           ports:
               - "9100:9100"
           networks:
               - monitor
   
       cadvisor:
           image: google/cadvisor:latest
           container_name: cadvisor
           hostname: cadvisor
           restart: always
           volumes:
               - /:/rootfs:ro
               - /var/run:/var/run:rw
               - /sys:/sys:ro
               - /var/lib/docker/:/var/lib/docker:ro
           ports:
               - "8088:8080"
           networks:
               - monitor
   
```

登录http://192.168.215.54:9090/targets ，如果State都是UP即代表Prometheus工作正常

#### 配置grafana

登录http://192.168.215.54:3000 

1. 默认账号密码admin/admin 54服务器：admin 654321
2. 配置Prometheus
   1. [使用docker-compose搭建Prometheus+Grafana监控系统 - 掘金](https://juejin.cn/post/6969764486701924383)
3. 配置报表8919 893 4074 1860
##### refrence
1. [使用docker-compose搭建Prometheus+Grafana监控系统 - 掘金](https://juejin.cn/post/6969764486701924383)
2. [docker compose部署prometheus+grafana+alertmanager+prometheus-webhook-dingtalk - mrdongdong - 博客园](https://www.cnblogs.com/namedgx/p/14919857.html)
3. [Node Exporter 简介_富士康质检员张全蛋的博客-CSDN博客_node_exporter](https://blog.csdn.net/qq_34556414/article/details/113107269)

### Prometheus

数据采集方式：

1. exporter 的 pull 机制，是指应用本身暴露能获取监控指标能力的接口，或者特定能采集应用数据的 exporter，然后 Prometheus server 通过访问 exporter 获取到监控数据信息，这种方式是主流的使用方式，也就是 pull 模式。
2. 通过 Push Gateway 的 push 机制，是用户主动将数据推送到 Push Gateway 组件，然后 Prometheus server 通过 Push Gateway  获取到数据，这种是比较传统的 push 模式。（这不还是pull）

### 探针

##### node-exporter
Exporter是Prometheus的指标数据收集组件。它负责从目标Jobs收集数据，并把收集到的数据转换为Prometheus支持的时序数据格式。 和传统的指标数据收集组件不同的是，他只负责收集，并不向Server端发送数据，而是**等待Prometheus Server 主动抓取**，node-exporter 默认的抓取url地址：http://ip:9100/metrics 另外，如果因为环境原因，网络不可达的场景，Prometheus可以使用**Pushgateway**这个组件推送node-exporter的指标数据到远端Prometheus

1.  [Node Exporter 简介_富士康质检员张全蛋的博客-CSDN博客_node_exporter](https://blog.csdn.net/qq_34556414/article/details/113107269)
2. [docker - 容器监控实践—node-exporter_个人文章 - SegmentFault 思否](https://segmentfault.com/a/1190000017959127)

##### 进程监控
1. [prometheus监控之进程监控（process-exporter）](https://segmentfault.com/a/1190000039263952)
2. [Prometheus监控进程状态（Process-Exporter）](https://www.jianshu.com/p/6a7511004766)

[进程管理工具](https://cloud.tencent.com/developer/article/1725966)
##### 容器监控 cAdvisor







[QPS、TPS、RT、Load、PV、UV](https://mp.weixin.qq.com/s?__biz=MzU1MzUyMjYzNg==&mid=2247484903&idx=1&sn=69762eab1546712021c192e30e50b5d0)
怎么样来衡量一个应用当前的状态到底是怎么样的？到底需不需要扩容？是需要横向扩容还是进行项目重构？
要看那些指标来判断呢？
1. qps
	1. queries per second
	2. 每秒处理查询次数
2. tps
	1. transactions per second
	2. 每秒处理的事务数
	3. 在针对单接口，TPS可以认为是等价于QPS的，如访问 ‘order. html’ 这个页面而言, 是一个TPS。而访问 ‘order. html’ 页面可能请求了3此服务器（如调用了css、js、order接口），这实际就算产生了三个QPS
3. rt
	1. res (onse time)
	2. 从客户端请求发起到服务端响应结果的时间。
	3. 一般监控三个值，平均 、最大 、最小
4. 并发数
	1. 系统能同时处理的请求的数量
	2. 请求一个 index. html 页面，客户端发起了三个请求（css、js、index 接口）, 那么此时 TPS =1 、QPS =3 、并发数 3。
	3. 即：并发数=QPS* RT 
5. 吞吐量
	1. 每秒承受的用户访问量
	2. 吞吐量和系统 tps、qps、并发数相关，每个参数都有相对极限值，都到极限值此时系统吞吐量也就最大，若某个参数再加大，那吞吐量就会下降。
	3. 所以 QPS（TPS）= 并发数/平均响应时间
6. pv
	1. page view
	2. 每个页面的浏览次数，用户每次刷新就算一次
7. uv
	1. unique visitor
	2. 独立访客数，网站每天的用户访问数
8. load
	1. 系统负载
	2. 此数据指的是 Linux 系统的负载情况，也就是咱们平时所用 Top 命令时，最上面显示的数据信息 ( load average: 0.1, 0.2, 0.5)。此时会显示1分钟、5分钟、15分钟的系统平均 Load，很显然 load average 的值越低，你的系统负荷越小。
	3. 简单的说下这个值应该怎么看，如果你是单核 cpu, 那此值为1的时候就是系统已经满负荷状态了，需要你马上去解决。但实际经验告诉我们，当系统负荷持续大于0.7的时候（也就是70%），就需要你马上来解决问题了，防止进一步恶化。
	4. 为什么需要三个值 load average: 0.1, 0.2, 0.5，其实就是给你个参考。比如只有1分钟的是1，其他俩都是0.1，这表明只是临时突发的现象，问题不大。如果15分钟内，系统负荷都是1或大于1，那表明问题持续存在啊。所以你应该主要观察15分钟的系统负荷