# APM
[APM，监控界的扛把子，牛逼！-腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1912101)

![微服务架构设计模式](微服务架构设计模式.md#11%203%20设计可观测的服务)
## 日志监控

[日志分析](日志分析.md#微服务日志)
## 链路追踪
### zipkin

spring cloud sleuth 

```Bash
docker run -d -p 9411:9411 openzipkin/zipkin
```
### skywalking

**注意：等待elasticsearch完全启动之后，再启动oap**

```sh
#### 安装oap
docker run -d --name oap --restart always \
--restart=always \
-e TZ=Asia/Shanghai \
-p 12800:12800 \
-p 11800:11800 \
apache/skywalking-oap-server
```

1. 安装ui 
1. -javaagent:H:\openSource\skywalking-agent\skywalking-agent.jar SW_AGENT_COLLECTOR_BACKEND_SERVICES=192.168.215.54:11800 -javaagent:H:\openSource\skywalking-agent\skywalking-agent.jar -Dskywalking.agent.service_name=heatmap -Dskywalking.collector.backend_service=192.168.215.54:11800

```sh
docker run -d --name skywalking-ui \
--restart=always \
-e TZ=Asia/Shanghai \
-p 8091:8080 \
--link oap:oap \
-e SW_OAP_ADDRESS=oap:12800 \
apache/skywalking-ui
```
```yaml
version: '3'
services:
  skywalking-oap:
    image: apache/skywalking-oap-server:8.3.0-es7
    container_name: skywalking-oap
    restart: always
    ports:
      - 11800:11800
      - 12800:12800
    environment:
      TZ: Asia/Shanghai
      SW_STORAGE: elasticsearch7
      SW_STORAGE_ES_CLUSTER_NODES: elk_elasticsearch:9200
    networks:
       - elk_default
    external_links:
       - elk_elasticsearch


  skywalking-ui:
    image: apache/skywalking-ui:8.3.0
    container_name: skywalking-ui
    restart: always
    depends_on:
      - skywalking-oap
    links:
      - skywalking-oap
    ports:
      - 809:8080
    environment:
      TZ: Asia/Shanghai
      SW_OAP_ADDRESS: skywalking-oap:12800
networks:
  elk_default:
    external: true
```

## 异常监控
### Sentry
[微服务架构统一异常监控Sentry-腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1791697)

## 指标监控
[Metric监控](Metric监控.md#Metric监控)