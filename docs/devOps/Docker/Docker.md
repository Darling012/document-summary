# Docker
## 常见问题及解决
1. 容器内没有 ping curl 等命令以及怎么用宿主机上安装的命令
	1. [nsenter：利用宿主机上的 ping、curl 等命令](https://segmentfault.com/a/1190000039660549)
2. docker run 转 docker compose 工具
	1. [Composerize](https://www.composerize.com/)
3. docker-compose 转 helm 工具
	1. 利用 kompose-windows-amd64.exe 转换 dockerCompose 文件到 k8s 部署文件
4. 像用 docker 启动 jenkins, jenkins 里又用 docker 的 `docker in docker` 情况
	1. [Jenkins docker部署jenkins，共享宿主机docker资源](https://blog.csdn.net/weixin_40123451/article/details/113105290)
	2. `unix /var/run/docker.sock: connect: permission denied` 问题，在 `docker-compose` 中增加 `group_add` 项增加权限。
5. 离线部署 docker 容器
	1. [Docker - 实现本地镜像的导出、导入（export、import、save、load）](https://www.hangge.com/blog/cache/detail_2411.html)
		1. save 会保存每一层
		2. save 的时候用 `image:tag`，不要用镜像 id，不然 load 的时候没名字和 tag，还要再改
		3. export 用来制作基础镜像， save 用来做镜像迁移比如离线环境
6. 怎么查看 docker run 启动的容器 启动时 命令
	1. [如何查看一个运行容器的docker run启动参数\_PostgreSQL运维技术的博客-CSDN博客](https://blog.csdn.net/qq_35462323/article/details/101607062)
7. dockerfile-maven-plugin
	1. [构建不成功，clean一下。](https://zhuanlan.zhihu.com/p/90122357)
8. [鹤壁 docker-compose 启动报端口占用问题](Docker.md#鹤壁%20docker-compose%20启动报端口占用问题)
9. 怎么删除状态为 dead 的容器
	1. [docker 中删除dead状态的容器\_whatday的博客-CSDN博客](https://blog.csdn.net/whatday/article/details/102680659)
10. 

### 鹤壁 docker-compose 启动报端口占用问题
##### 背景
mysql 启动不了，错误信息 `driver failed programming external connectivity on endpoint mysql 3306`，edu 项目容器都连接到了 `edu-network`
##### 快速解决
因为是主机 3306 端口占用冲突，所以不再映射出来，通过 `edu-network` 实现联通。
##### 重启导致新问题
1.  `docker-compose up service-exam` 报错
	1. 信息为 `container is marked for removal and cannot be started`
2. `docker ps -a` 查看 `service-exam` 状态
	1. 状态为 `removal in progress`
3. 通过 `docker rm -f service-exam` 强力删除
	1. 返回结果为 `removal of container service-exam is alerady in progress`
4. 通过 `systemctl stop docker`
	1. 且 gpt 建议等待 5 分钟以上
5. 重启后再次执行 `docker ps -a` 未列出 `service-exam` 及之前状态为 `removal in progress` 的其他容器
6. 手动启动 `service-exam` 等容器，并等待后，正常启动。
##### 排查
1. [docker端口映射或启动容器时报错 driver failed programming external connectivity on endpoint quirky\_allen\_whatday的博客-CSDN博客](https://blog.csdn.net/whatday/article/details/86762264)
	1. 说是 iptables 映射问题，执行 `systemctl restart docker` 会重建，但是其信息包含了 `iptables` 字段，鹤壁没有。
2. 由于是鹤壁环境，在尽快解决后，不能再次试验为什么端口没有占用，但启动就会报占用


#### 删除状态为 removal in progress 的容器
鹤壁再次出现问题后用上面方法不再起作用。






[Java 容器化指北 - Kovacs](https://mritd.com/2022/11/08/java-containerization-guide)