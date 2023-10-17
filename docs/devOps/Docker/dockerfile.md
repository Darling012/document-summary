##### 
变量指令
1. arg 
2. env
## 指令
##### from
1. Dockerfile 中的第一条指令必须是 FROM 指令
2. 在 Dockerfile 中可以多次出现 FROM 指令

##### run、cmd、entrypoint 区别与选择
1. [Dockerfile RUN，CMD，ENTRYPOINT命令区别 - 简书](https://www.jianshu.com/p/f0a0f6a43907)
2. [Docker 中 RUN、CMD 与 ENTRYPOINT 的区别_云计算-Security的博客-CSDN博客](https://blog.csdn.net/IT_ZRS/article/details/124707747)
==run==
1. 构建时
2. 用于向控制台发送命令的指令
3. 在构建时，Docker 就会执行这些命令，并将它们对文件系统的修改记录下来，形成镜像的变化
==cmd==
1. 运行时
2. 在容器启动时会根据镜像所定义的一条命令来启动容器中进程号为 1 的进程。而这个命令的定义，就是通过 Dockerfile 中的 ENTRYPOINT 和 CMD 实现的
3. dockerfile 中只能包含一个 CMD 指令，如果存在多个，只有最后一个 CMD 生效
4. [Dockerfile CMD指令](https://blog.csdn.net/securitit/article/details/109526413)
==entrypoint==
1. ENTRYPOINT 指令的优先级高于 CMD 指令
2. 当 ENTRYPOINT 和 CMD 同时在镜像中被指定时，CMD 里的内容会作为 ENTRYPOINT 的参数，两者拼接之后，才是最终执行的命令。
3. **ENTRYPOINT 指令主要用于对容器进行一些初始化，而 CMD 指令则用于真正定义容器中主程序的启动命令**

##### add copy
[Best practices for writing Dockerfiles](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/#add-or-copy)







## reference
1. [如何编写最佳的Dockerfile](https://blog.fundebug.com/2017/05/15/write-excellent-dockerfile/)