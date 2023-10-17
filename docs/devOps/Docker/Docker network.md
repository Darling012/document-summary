# Docker 网络
## 问题

1. docker 创建的 ` bridge` 类型网络会在宿主机创建一个 ` br ` 开头虚拟网卡，当容器连接到此网络时，又会创建一个 `veth` 开头的虚拟网络接口。那么 `ifconfig` 和 `ip addr` 命令列出来的都是哪些东西？


## 操作

==查看 docker 网络信息操作==
1. 通过 `docker network inspect yapi_default` 查看
	1. `ID` 为 `12122381e1c`, 对应宿主机中网卡 `br-12122381e1c8`
	2. 子网掩码 `172.27.0.0/16`, 网关 `172.27.0.1` 以及连接在此网络上的容器 IP `172.27.0.2/16` 等信息
	3. [yapi_default信息](Pasted%20image%2020230215132826.png)
2. 进入 `yapi-web` 容器内，执行 `ip addr` 查看
	1. `inet addr:172.27.0.2 Bcast:172.27.255.255 Mask:255.255.0.0`
	2. [容器内部信息](Pasted%20image%2020230215132805.png)
3. 宿主机执行 `ip addr` 查看
	1. `yapi_default` 网桥对应网卡 `br-12122381e1c8`，信息`inet 172.27.0.1/16 brd 172.27.255.255 scope global br-12122381e1c8`
	2. `veth` 宿主机接口 `vethb4e34e3@if58`，包含信息`br-12122381e1c8`
	3. [网卡br-12122381e1c8](Pasted%20image%2020230215133353.png)
4. 通过 `brctl show` 命令查看 虚拟网桥下挂载的 `veth` 对

==Docker 容器访问[宿主机网络](https://jingsam.github.io/2018/10/16/host-in-docker.html) ==
1. `host` 网络模式
2. `bridge`
	1.  Linux 使用 `docker0` 虚拟网卡 IP
	2. windows、mac 没有 `docker0` 虚拟网卡 ，使用 `host.docker.internal` 这个特殊的 DNS 名称来解析宿主机 IP

## linux 网络
==veth pair 和 linux bridge==
[Linux VETH 和 Bridge](https://gobomb.github.io/post/learning-linux-veth-and-bridge/)
1. VETH（virtual Ethernet）：是 Linux 内核支持的一种虚拟网络设备，表示一对虚拟的网络接口，VETH 对的两端可以处于不同的网络命名空间，所以可以用来做主机和容器之间的网络通信。
2. Bridge：Bridge 类似于交换机，用来做二层的交换。可以将其他网络设备挂在 Bridge 上面，当有数据到达时，Bridge 会根据报文中的 MAC 信息进行广播、转发或丢弃。

> [二层网络三层网络理解 - 简书](https://www.jianshu.com/p/a540c16f5b5c)
> 二层网络是数据链路层，交换器用网卡 mac 通信
> 三层网络是网络层，路由器用 IP 地址通信
> 四层是传输层，tcp、udp、socket

[Linux 虚拟网络设备 veth-pair 详解](https://www.cnblogs.com/bakari/p/10613710.html)
1. `veth-pair` 是一对虚拟网络接口，都是成对出现，一端互相连接，一端连接网络协议栈
2. 充当桥梁角色连接虚拟网略设备，比如两个 namespace 之间，docker 容器之间，bridge、ovs 之间
两个 namespace 之间建立连接方式：
1. 用一对 `veth-pair` 直接将两个 namespace 连接
2. birdge
	1. Linux Bridge 相当于一台交换机，用两对 `veth-pair` 将两个 namespace 连接到 bridge 上
3. ovs
	1. ovs 是第三方开源的 Birdge，同上

==iptables==
[iptables（一）基础概念、filter表常用语法规则 - flag\_HW - 博客园](https://www.cnblogs.com/flags-blog/p/15463554.html)
netfilter/iptables（简称为iptables）组成Linux平台下的包过滤防火墙，与大多数的Linux软件一样，这个包过滤防火墙是免费的，它可以代替昂贵的商业防火墙解决方案，完成封包过滤、封包重定向和网络地址转换（NAT）等功能

==ifconfig 和 ip addr 命令==
[ifconfig 各项信息](https://einverne.github.io/post/2017/10/ifconfig.html)
[ip addr 命令](https://blog.csdn.net/qq_33330687/article/details/99118865)
[ip addr详解](https://www.cnblogs.com/wqbin/p/11065587.html)
网卡中的net 是 ip 地址，能看到子网掩码
网卡中的ink/ether 这个是 Mac 地址，一个网卡的物理地址，16 进制 6byte  全球唯一

## docker 网络 

[Docker的四种网络类型](https://cloud.tencent.com/developer/article/1693450)
[docker容器网络配置 - 悬溺· - 博客园](https://www.cnblogs.com/wg123/p/16204289.html)
[花了三天时间终于搞懂 Docker 网络了](https://cloud.tencent.com/developer/article/1747307)
[Docker容器网络 - Alone-林 - 博客园](https://www.cnblogs.com/Alone-8712/p/16572283.html)

### bridge 类型网络

1. 安装 docker 时创建的默认 `docker 网桥`，用 `docker network ls` 查看其名称是 `bridge`，宿主机上用 ` ifconfig` 查看，是 ` docker0` 虚拟网卡
2. 当启动新容器不指定网络时，默认都连接到 `dokcer` 网桥上，每个容器都会再创建一个 `veth ` 开头的虚拟网卡
3. 容器会通过 `DHCP` 获取一个与 `docker0` 同网段的 IP 地址

### 自己实现 docker 网络
[Docker单机网络上](https://mp.weixin.qq.com/s?__biz=MzU0NDc4NjA0MQ==&mid=2247484841&idx=1&sn=ae67491f268932fd4a5239423b6aac78)
[Docker单机网络下](https://blog.51cto.com/u_15069479/3319973)

## docker network 命令
docker network 所有子命令如下：

- docker network create
- docker network connect
- docker network ls
- docker network rm
- docker network disconnect
- docker network inspect

### 创建网络

在安装 Docker Engine 时会自动创建一个默认的 bridge 网络 `docker0`。
此外，还可以创建自己的 `bridge` 网络或 `overlay` 网络。

`bridge` 网络依附于运行 Docker Engine 的单台主机上，而 `overlay` 网络能够覆盖运行各自 Docker Engine 的多主机环境中。

创建 `bridge` 网络比较简单如下：

```
 # 不指定网络驱动时默认创建的bridge网络
 docker network create simple-network
 # 查看网络内部信息
 docker network inspect simple-network
 # 应用到容器时，可进入容器内部使用ifconfig查看容器的网络详情
```

但是创建一个 `overlay` 网络就需要一些前提条件（具体操作请参考 `Docker容器网络` 相关内容）：

- key-value store（Engine 支持 Consul、Etcd 和 ZooKeeper 等分布式存储的 key-value store）
- 集群中所有主机已经连接到 key-value store
- swarm 集群中每个主机都配置了下面的 daemon 参数
- –cluster-store
- –cluster-store-opt
- –cluster-advertise
然后创建 `overlay` 网络：

```
# 创建网络时，使用参数`-d`指定驱动类型为overlay
docker network create -d overlay my-multihost-network
```

就使用 `--subnet` 选项创建子网而言，`bridge` 网络只能指定一个子网，而 `overlay` 网络支持多个子网。

在 bridge 和 overlay 网络驱动下创建的网络可以指定不同的参数，具体请参考：[https://docs.docker.com/engine/userguide/networking/work-with-networks/](https://docs.docker.com/engine/userguide/networking/work-with-networks/)

### 连接容器

创建三个容器，分别前两个使用默认网络启动容器，第三个使用自定义 bridge 网络启动。
然后再将第二个容器添加到自定义网络。这三个容器的网络情况如下

- 第一个容器：只有默认的 docker 0
- 第二个容器：属于两个网络——docker 0、自定义网络
- 第三个容器：只属于自定义网络

说明：通过容器启动指定的网络会覆盖默认 bridge 网络 docker 0。

```
# 创建三个容器 conTainer1,container2,container3
docker run -itd --name=container1 busybox
docker run -itd --name=container2 busybox
# 创建网络mynet
docker network create -d bridge --subnet 172.25.0.0/16 mynet
# 将容器containerr2连接到新建网络mynet
docker network connect mynet container2
# 使用mynet网络来容器container3
docker run --net=mynet --ip=172.25.3.3 -itd --name=container3 busybox

# 查看这三个容器的网络情况
docker network inspect container1 # docker0
docker network inspect container2 # docker0, mynet
docker network inspect container3 # mynet
```

#### 默认网络与自定义 bridge 网络的差异

默认网络 docker 0：网络中所有主机间只能用 IP 相互访问。通过 `--link` 选项创建的容器可以对链接的容器名 (container-name) 作为 hostname 进行直接访问。
自定义网络 (bridge)：网络中所有主机除 ip 访问外，还可以直接用容器名 (container-name) 作为 hostname 相互访问。

```
# 进入container2内部
docker attach container2
ping -w 4 container3 # 可访问
ping -w 4 container1 # 不可访问
ping -w 4 172.17.0.2 # 可访问container1的IP
# Ctrl+P+Q退出容器，让container2以守护进程运行
```

#### 默认网络与自定义 bridge 网络在容器连接的差别

在默认网络中使用 link（legency link），有如下功能：

- 使用容器名作为 hostname
- link 容器时指定 alias: `--link=<Container-Name>:<Alias>`
- 配合 `--icc=false` 隔离性，实现容器间的安全连接
- 环境变量注入

自定义网络中使用 docker net 提供如下功能：

- 使用 DNS 实现自动化的名称解析
- 一个网络提供容器的安全隔离环境
- 动态地 attach 与 detach 到多个网络
- 支持与 `--link` 选项一起使用，为链接的容器提供别名（可以是尚不存在链接容器，与默认容器中–link 使用的最大差别）

默认网络中的 link 是静态的，不允许链接容器重启，而自定义网络下的 link 是动态的，支持链接容器重启（以及 IP 变化）
**因此，使用**`**--link**`**时链接的容器，在默认网络中必须提前创建好，而自定义网络下不必预先建好。**

使用 `docker network connetct` 将容器连接到新网络中时，用参数 `--link` 链接相同的容器时，可以指定不同的别名，它们是针对不同网络的。

```
# 运行容器使用自定义网络，同时使用--link链接尚不存在的container5容器
docker run --net=mynet -itd --name=container4 --link container5:c5 busybox
# 创建容器container5
docker run --net=mynet -itd --name=container5 --link container4:c4 busybox
# 虽然是相同容器，但是在不同的网络环境连接中可以不同的alias链接
docker network connect --link container5:foo local_alias container4
docker network connect --link container4:bar local_alias container5
```

#### 指定容器在网络范围的别名（Network-scoped alias）

Network-scoped alias 就是指定容器在可被同一网络范围内的其他容器访问的别名。
不同于 link 别名的是，link 别名是由链接容器的使用者提供的，只有它自己可使用；
而指定网络范围内别名，是由容器提供给网络中其它容器使用的。

Network-scoped alias：同一网络中的多个容器可以指定相同的别名，在使用的当然只有第一个指定别名的容器才生效，
只有当第一个容器关闭时，指定相同别名的第二个容器的别名才会开始生效。

```
docker run --net=mynet -itd --name=container6 --net-alias app busybox
docker network connect --alias scoped-app local_alias container6
docker run --net=isolated_nw -itd --name=container7 --net-alias app busybox
docker network connect --alias scoped-app local_alias container7
# 在container4中
docker attach container4
ping app # 访问container6的IP
# 从container4中以守护进程运行退出：Ctrl+P+Q
docker stop container6
docker attach container4
ping app # 访问的container7的IP
```

#### 断开网络与移除网络

```
# 容器从mynet网络中断开（它将无法再网络中的容器container3通讯）
docker network disconnect mynet container2
# 测试与容器container3失败
docker attach container2
ping contianer3 # 访问失败
```

在多主机的网络环境中，在将容器用已移除的容器名称连接到网络中时会出现 `container already connected to network` 的错误，
这时需要将新容器强制移除 `docker rm -f`，重新运行并连接到网络中。

**移除网络**要求网络中所有的容器关闭或断开与此网络的连接时，才能够使用移除命令：

```
# 断开最后一个连接到mynet网络的容器
docker network disconnet mynet container3
# 移除网络
docker network rm mynet
```

## reference

 [Docker容器网络通信机制](https://baobao555.tech/archives/36)
1. docker 启动就会在主机创建一个 `docker` 虚拟网桥，即 `bridge` 看做软件交换机
2. 创建 docker 容器时，同时会创建一对 `veth pair` 接口（当把数据发送到一个接口，另一个就会收到）
3. 其中一端接口在容器内即 `eth0`，另一端在宿主机本地并挂载到 `docker网桥`，名称以 `veth` 开头




[docker容器ping不通宿主机与外网问题排查及解决](https://blog.csdn.net/qq_35641923/article/details/121549499)
[Docker容器网络-实现篇 - Mr\_Zack - 博客园](https://www.cnblogs.com/sally-zhou/p/13463016.html)

[docker小技巧（一） 使用iptables 映射docker容器端口\_51CTO博客\_docker容器配置端口映射](https://blog.51cto.com/u_14205795/4561316)
[容器网络——如何为docker添加网卡？ | 没有理想的人不伤心](https://typesafe.cn/posts/how-to-add-port-for-docker/)


[解决docker不能绑定静态的外网固定ip的问题 – 峰云就她了](https://xiaorui.cc/archives/1488)
[使用iptables管理docker容器做端口映射网络 – 峰云就她了](https://xiaorui.cc/archives/1502)