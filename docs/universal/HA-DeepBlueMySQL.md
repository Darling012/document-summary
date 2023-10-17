# HA-DeepBlueMySQL

## 思路
机房内[mysql高可用](mysql高可用.md)或者是说容灾包含两部分
1. 备库，可进行故障切换的从数据库，需要利用 [MySQL复制](MySQL复制.md)
2. [备份](MySQL备份.md)，按时间节点将数据备份，防止主从数据库都坏掉后进行数据恢复。更科学的是备份文件与数据库不在同一服务器。

为了防止单点故障，配置 mysql 主从复制，66 服务器 mysql 为主，55 为从。采用 docker 部署，最好是通过 docker swarm 部署到两台机器。
备份采用定时任务+全量备份，全量备份选择物理还是逻辑备份？

## AIVideo 项目高可用思路
1. service - > mycat -> mha mysql 集群
	1. mha 可故障切换；可通知 mycat
	2. 或者 mycat 探活
	3. mycat 单点问题怎么解决？
	   1. 如何负载：haproxy
	   2. 数据同步：zk
	4. [SpringBoot +MySQL+mycat 实现读写分离+故障转移_islibin6666的博客-CSDN博客](https://blog.csdn.net/weixin_38938840/article/details/103474507)
2. 自写 agent 探活方案：
	1. 代码侵入，需改动已有代码。
	2. service -> 自写 agent+dynamicDataSource [动态切换数据源](动态切换数据源.md) -> mysql (主从复制) 
    3.  [动手实现MySQL读写分离and故障转移 - 掘金](https://juejin.cn/post/6844904013272449037#heading-24)

## 测试服务器主从同步
1. [binlog格式](MySQL日志.md#三种格式)有三种，采用 mixed 混合模式
2. 复制模式采用 GTID
3. 若数据库有数据要先同步两个数据库数据

### 配置

```yaml
version: '3.3'
services:
  mysql-master:
    image: mysql:8.0
    container_name: mysql-master
    command:
      --default-authentication-plugin=mysql_native_password
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_general_ci
      --explicit_defaults_for_timestamp=true
      --lower_case_table_names=1
    ports:
      - 3316:3306  
    environment:
      - MYSQL_ROOT_PASSWORD=P@ssw0rd

    volumes:
      - ./conf:/etc/mysql/conf.d
      - ./log:/var/log/mysql
      - ./data:/var/lib/mysql
```


==master.cnf==
```c
[mysqld]

max_connections = 2000
default-time_zone='+8:00'
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION


server_id = 1                   #服务器id，一般为IP地址
binlog-ignore-db=mysql          #复制过滤：也就是指定哪个数据库不用同步（mysql库一般不同步） 

#gtid:
gtid_mode = on                  #开启gtid模式
enforce_gtid_consistency = on   #强制gtid一致性，开启后对于特定create table不被支持

#binlog
log_bin = master-binlog          #开启二进制日志功能，可以随便取，最好有含义（关键就是这里了）
log_slave_updates = on
binlog_format = mixed           #主从复制的格式（mixed,statement,row，默认格式是statement）
binlog_cache_size=1M            #为每个session 分配的内存，在事务过程中用来存储二进制日志的缓存
expire_logs_days=7              #二进制日志自动删除/过期的天数。默认值为0，表示不自动删除。
slave_skip_errors=106           #跳过主从复制中遇到的所有错误或指定类型的错误，避免slave端复制中断。如：1062错误是指一些主键重复，1032错误是因为主从数据库数据不一致


#relay log
skip_slave_start = 1
  
default_authentication_plugin = 'mysql_native_password'  #更改加密方式

```


==slave.cnf==
```c
[mysqld]
#与master.cnf配置相同，注意几个配置要改值#

read_only = on                   #设置只读
```


```sh
# 连接mysql

docker exec -it mysql-slave mysql -uroot -pP@ssw0rd

#查看server_id是否生效

show variables like '%server_id%';

# 查看MASTER状态

SHOW SLAVE STATUS\G

# 从节点使用备份账户连接主节点，开启备份

CHANGE MASTER TO
      master_host='192.168.215.66',
      master_port=3306,
      master_user='replicasName',
      master_password='replicasPasswd',
      master_auto_position=1;
      
CHANGE MASTER TO master_host='192.168.215.66', master_port=3306, master_user='replicasName', master_password='replicasPasswd', master_auto_position=1;
# 启动同步
RESET SLAVE;

START SLAVE;
```
stop alave;


## reference
[docker-compose部署 Mysql 8.0 主从模式基于GTID](https://www.cnblogs.com/bigfairy/p/14772021.html)
概述
1. mysql 主从复制又叫 Replication、AB 复制

[【MySQL】你还不会在Docker下安装MySQL主备吗？ - 掘金](https://juejin.cn/post/7101875752349466661#heading-5)

