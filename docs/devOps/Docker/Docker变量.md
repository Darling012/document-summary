# Docker 变量

##### 总结
<font color=#81B300>总结的不好</font>
1. `env` 和 `arg` 都可以被定义在 `dockerfile` 中，然后被传入或覆盖
2. `arg` 只参与构建阶段，通过 `docker build --build-arg` 传入
3. `env` 构建运行都参与
	1. 在运行阶段是通过 `docker run -e` 进行覆盖
	2. 但构建阶段是通过在 `dockerfile` 定义的默认值参与
	3. 所以在构建阶段要想修改 `env` 的值，就要通过 `env` 引用 `arg`，再通过传入 `arg` 值间接实现
	4. 因为 env 在 build 的时候可以被保存下来，所以通过联合使用 ARG 和 ENV, 可以在 build 的时候传递参数，并在运行时仍旧可用
4. 
## dokcerfile
### ARG 参数变量
在 `dockerfile` 中定义
```yaml
ARG TOMCAT_MAJOR
RUN wget -O ...tomcat-$TOMCAT_MAJOR...
```
若没有设置默认值，在构建时通过命令传入
`docker build --build-arg TOMCAT_MAJOR=8 `

### ENV 环境变量
在 `dockerfile` 中定义
```yaml
ENV TOMCAT_MAJOR 8
RUN wget -O ...tomcat-$TOMCAT_MAJOR...
```
在容器运行时进行覆盖
`docker run -e TOMCAT_MAJOR=9`

1. `env` 和 `arg` 用法一样，都是直接替换 `RUN` 指令参数中的内容
2. `arg` 只参与构建过程，`env` 不仅参与构建，还参与容器运行时，其实质就是定义系统环境变量
3. `env` 不是在构建指令中传入，而是在 `dockerfile` 中定义，又由于在容器运行时依然有效，所以可以通过 `docker run -e` 对其进行覆盖
4. 两者同名时，`env` 会覆盖 `arg` 的值


### reference
[Docker ARG，ENV和.env-完整指南 - 知乎](https://zhuanlan.zhihu.com/p/152957068)
==. env 文件==
1. `.env` 文件仅用在 `docker-compose.yml` 文件时的 `预处理步骤` 中, 即将 `yam里的${param}` 替换为  `同文件夹下.env` 文件中的值
2.  配合 Docker Compose 和 Docker Stack 使用，完全是 `docker-compose.yml` 的使用
3. `docker-compose config` 查看替换后的文件
4. 主机上的环境变量能够覆盖 `.env` 中的值(也就是说 `.env` 能读取环境变量？)

==arg==
1. `--arg` 仅在构建镜像阶段使用，用于替换 `dockerfile` 文件中定义的变量
2. 可以通过 `docker build --build-arg` 传入，以及 ` docker-compose.yml ` 定义 ` args 块 ` 传入。
3. 运行时容器访问不到 `arg` 类型变量，这也适用于 CMD 和 ENTRYPOINT 指令。只是告诉容器在默认情况下应该运行什么。

==env==
1. 相比 `arg` ，`env` 可以在容器中访问
2. 在构建镜像时，唯一能提供的是 `arg` 值，不能直接提供 `env` 值，要通过 `arg` 来设置 `env` 的值（[应该说的是 `docker build` 不能传入](Docker变量.md#^fk7h6a)）
3. `env` 值从哪来？
	1. 可以通过硬编码在 `dockerfile` 中
	2. 命令行传入
	3. `docker-compose.yml`
	4. 主机环境变量
	5. `env_file`
4. `env` 值的优先级，高的会覆盖低的：容器化应用程序集的东西，来自单一环境条目的值，来自 env_file (s)的值，最后是 Dockerfile 默认值。

==env_file==
1. 即用于 `env` 变量读取，统一放到 `env_file` 里
2. `docker run` 和 `docker-compose.yml` 都可以引入 `env_file` 文件
	1. `docker run --env-file=env_file_name alpine env`
	2.  `docker-compose.yml` 中的 `env_file` 标签或者 `run -e`

![](Pasted%20image%2020230330101358.png)

[在docker镜像中加入环境变量 ](https://mp.weixin.qq.com/s?__biz=MzUyNzk4MjA5NQ==&mid=2247483786&idx=1&sn=873e9b1e17be0ab91f927904541d18fe)
1. 直接用 `docker` 命令启动（意思应该是相当于给容器设置环境变量，[即便没有定义](Docker变量.md#^r1t6fg)，也可以传入，只不过这样也没有意义）
	1. `docker run --env VARIABLE=VALUE image:tag`
2. 使用 `dockerfile` 的 `ARG` 和 `ENV` 变量
	1. ARG 只在构建 docker 镜像时有效（dockerfile 的 RUN 指令等）
	2. 在启动容器后无效，但可以通过配合 `ENV` 解决 ^fk7h6a
		1. `ARG buildtime_variable=default_value        # if not set default_value buildtime_variable would be set ''`
		2. `ENV env_var_name=$buildtime_variable`
		3. `docker build --build-arg buildtime_variable=other_value --tag image:tag`
		4. 因为 `env` 不能在 `docker build` 构建指令中指定， 所以先用 `env` 变量引用 `arg`，再用 `docker build --build-arg` 间接传入
		6. <sub>没明白这么做的意义在哪，build 指令实现了传入环境变量？那 run 的时候传入不可以么？</sub>
3. 多阶段构建
	1. 但是有时我们只是临时需要环境变量或文件，最后的镜像是不需要的这些变量的，设置 ARG 和 ENV 值就会在 Docker 镜像中留下痕迹，比如保密信息等。多阶段构建可以用来去掉包含保密信息的镜像。


[使用环境变量、arg参数，config声明式配置docker服务 - 简书](https://www.jianshu.com/p/a471d859051a)
==arg==
1. arg 可以在docker文件中声明参数，被声明的参数可以被用在声明后的语句中。
2. arg 是编译时变量，仅用于编译阶段；如果一个 docker 文件中由多个编译阶段，每个阶段应该由自己的 arg 参数
3. 声明 arg 的时候如果没有设置默认值，就需要使用 `--build-arg <varname>=<value>` 方式在 build 的时候提供值。
4. arg 是 build 阶段的变量，Image 创建后将不会存在，也就是说不能用于容器的运行。
	1. 通过“docker history” 方便的查看 build 时使用的 arg
==env==
1. env 与 arg 对比，它是运行时变量，env 变量在其声明以后就可以使用，并且在 build 的时候会保存下来；在容器运行的时候可以继续访问。
2. evn 可以在声明时赋值，也可以在运行时赋值，在运行时使用-e 参数赋值， `docker run -e` 

Arg 与 ENV 在<font color=red>命令行赋值</font>时有这样显著的区别
1. `arg` 未声明，`build` 时传递会报错，`env` 不会，可在 `run` 中声明新的 `env` 变量 ^r1t6fg
2. 编译时声明 arg 需要指定值，负责出错。但是 env 不需要，如果没有给一个 env 变量赋值，该变量会被认为是来自当前 host 的变量。

ARG 和 ENV 可以被联合使用，因为 env 在 build 的时候可以被保存下来，ARG 定义的参数在构建后就消失了，所以通过[联合使用 ARG 和 ENV](Docker变量.md#^fk7h6a), 可以在 build 的时候传递参数，并在运行时仍旧可用。


==env 参数来源==
1. 显示传参
	1.  `docker run -e` 
	2.  ` docker-compose.yml ` 定义 ` environment 块 ` 
2. 宿主机系统变量
	1. 通过不提供赋值的  `docker run -e` 和` docker-compose.yml ` 定义 ` environment 块 ` 
3.  `env_file`

==环境变量替换==
在 dockerfile 中环境变量替换支持以下的指令：

==. env 文件==
1. .env 文件仅试用于 docker-compose 或 docker-stack，它只跟 docker-compose.yaml 文件有关系，它运行 docker-compose.yaml 时，会主动从.env 找寻需要替换的环境变量

==config 文件==
1. 环境变量文件外（.env, env-file），docker swarm 支持在image镜像以外定义配置属性，这些配置属性被保存到config文件中，它可以由docker config 命令来创建。



## docker-compose 环境变量
[Compose中的环境变量\_Docker中文网](https://dockerdocs.cn/compose/environment-variables/)

[Docker Compose 引用环境变量 - sparkdev - 博客园](https://www.cnblogs.com/sparkdev/p/9826520.html)
1. Compose CLI 与环境变量
	1. docker-compose command-line 能够识别 这些[名称](https://docs.docker.com/compose/environment-variables/envvars/)的环境变量
2. 在 compose file 中引用环境变量
	1. `export` 配置当前 shell环境变量
	2. `.env` 文件
3. 把环境变量传递给容器
	1. `environment` 块
	2. `export` 配置当前 shell 环境变量
4. 使用文件为容器设置多个环境变量
	1. `env_file`
5. 环境变量优先级
	1. docker-compose.yml
	2. 在当前 shell 中 export 的环境变量值
	3. `env_file`
	4. Dockerfile 中定义的值
	5. Variable is not defined

##### chatgpt
Docker 使用环境变量的优先级如下： 
1. shell 命令行中的变量 
2. 环境级别的变量（通过 export 定义的环境变量） 
3. 环境变量文件 .env 中的键值对
4. compose 文件中指定的 env_file 文件中的环境变量键值对 
5. compose 文件中指定的 environment 键值对 
6. 服务器操作系统级别定义的环境变量 
7. 默认值
##### bing
Docker Compose 在使用环境变量时，有以下优先级顺序（由高到低）： 
1. shell 命令行中的变量（使用 -e 选项指定） 
2. 环境级别的变量（通过 export 定义的环境变量） 
3. shell 环境变量中定义的键值对 
4. compose 文件中指定的 environment 键值对 
5. compose 文件中指定的 env_file 文件中的环境变量键值对 
6. 环境变量文件 .env 中的键值对 
7. 服务器操作系统级别定义的环境变量 
 如果您在不同的地方定义了相同的变量，那么优先级高的会覆盖优先级低的。例如，如果您在 .env 文件和 shell 环境中都定义了 HOST_SECRET 变量，那么 Docker Compose 会使用 shell 环境中的值，而忽略 .env 文件中的值。 

⬆⬆⬆俩回答还不一样，实践出真知⬆⬆⬆

[Docker 学习系列6 Docker Compose 中的环境变量使用注意事项 - 简书](https://www.jianshu.com/p/e88eb397e480)
[后端 - 更优雅的配置：docker/运维/业务中的环境变量 - 个人文章 - SegmentFault 思否](https://segmentfault.com/a/1190000023655147)