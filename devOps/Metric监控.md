[toc]

### spring boot admin

1. spring boot actuator提供探测信息
2. Spring Boot Admin分为服务端（spring-boot-admin-server）和客户端（spring-boot-admin-client）两个组件：
   - spring-boot-admin-server通过采集actuator端点数据显示在spring-boot-admin-ui上，已知的端点几乎都有进行采集。
   - spring-boot-admin-client是对Actuator的封装，提供应用系统的性能监控数据。此外，还可以通过spring-boot-admin动态切换日志级别、导出日志、导出heapdump、监控各项性能指标等。

##### 集成Prometheus

1. https://juejin.cn/post/6844903725148930061
2. https://blog.51cto.com/u_15103030/2649057
3. https://www.jianshu.com/p/89660c621663

### Prometheus+grafana

#### 角色分配：

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
   1. https://juejin.cn/post/6969764486701924383
3. 配置报表8919 893 4074 1860

##### refrence

1. https://juejin.cn/post/6969764486701924383
2. https://www.cnblogs.com/namedgx/p/14919857.html
3. https://blog.csdn.net/qq_34556414/article/details/113107269

### Prometheus

数据采集方式：

1. exporter 的 pull 机制，是指应用本身暴露能获取监控指标能力的接口，或者特定能采集应用数据的 exporter，然后 Prometheus server 通过访问 exporter 获取到监控数据信息，这种方式是主流的使用方式，也就是 pull 模式。
2. 通过 Push Gateway 的 push 机制，是用户主动将数据推送到 Push Gateway 组件，然后 Prometheus server 通过 Push Gateway  获取到数据，这种是比较传统的 push 模式。（这不还是pull）

### 监控

#### node-exporter

Exporter是Prometheus的指标数据收集组件。它负责从目标Jobs收集数据，并把收集到的数据转换为Prometheus支持的时序数据格式。 和传统的指标数据收集组件不同的是，他只负责收集，并不向Server端发送数据，而是**等待Prometheus Server 主动抓取**，node-exporter 默认的抓取url地址：http://ip:9100/metrics 另外，如果因为环境原因，网络不可达的场景，Prometheus可以使用**Pushgateway**这个组件推送node-exporter的指标数据到远端Prometheus

1. https://blog.csdn.net/qq_34556414/article/details/113107269
2. https://segmentfault.com/a/1190000017959127

#### 进程监控

1. https://segmentfault.com/a/1190000039263952
2. https://www.jianshu.com/p/6a7511004766

