#### 使用 
[Arthas如何在生产环境捉“虫”](https://mp.weixin.qq.com/s?__biz=MzIzMzgxOTQ5NA==&mid=2247493275&idx=3&sn=2d64c851e05730122b8a0fb66d3ad0b1)
1.  `wget https://alibaba.github.io/arthas/arthas-boot.jar `
2. 启动Arthas的 arthas-boot.jar
3. dashboard命令
	1. 可以查看线程，内存，GC，以及 Runtime 信息
4.  jad 
	1. 反编译查看线上代码
5. watch
	1. 查看函数的执行信息
6. tt
	1. 定位异常调用
7. trace 
	1. 查看调用链路
8. redefine 
	1. 实现热部署
	2. [jad/ mc/ redefine热更一条龙](https://hengyun.tech/arthas-online-hotswap/)
[trace、watch](https://mp.weixin.qq.com/s?__biz=MzI0NzEyODIyOA==&mid=2247484883&idx=1&sn=1d6017c6824a7782ff6a20fe2bf7f088)

##### arthas idea plugin 辅助构建命令
1. [arthas idea plugin，构建各种命令，然后复制到服务器执行](https://github.com/WangJi92/arthas-idea-plugin)
2. [arthas idea plugin 使用文档](https://www.yuque.com/arthas-idea-plugin/help/pe6i45)

#### docker 内 Springboot 服务怎么使用arthas 
arthas 依赖 jvm 提供的命令行工具和类库，但为了让镜像体积更小，往往底层镜像为 精简的 jre。
[聊聊部署在docker容器里面的springboot项目如何启用arthas - 腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1822681)
1.  进入容器内部执行相应命令
	1. curl -O https://arthas.aliyun.com/arthas-boot.jar
	2.  java -jar arthas-boot.jar
2. 把arthas安装到基础镜像
	1. k12 中 skywalking 集成到了镜像中
3. 使用 arthas-spring-boot-starter

#### springbootadmin 集成 arthas
1. [Spring Boot Admin 集成诊断利器 Arthas 实践]( https://mp.weixin.qq.com/s/ldXaO5HCX8_uCYnOxHu20Q )
2.  [SpringBoot Admin2.0集成Arthas实践 · Issue #1736 · alibaba/arthas · GitHub](https://github.com/alibaba/arthas/issues/1736)


#### [Arthas 实现原理 ]( https://www.51cto.com/article/709823.html )
[字节码](字节码.md)
Arthas 如何做到无需重启 attach 到 JVM (ASM + Instrument 处理流程)：
目标类 class bytes -> ClassReader解析 -> ClassVisitor增强修改字节码 -> ClassWriter生成增强后的 class bytes -> 通过Instrument解析加载为新的Class.


##### 其他产品
1. [GitHub - qunarcorp/bistoury: Bistoury是去哪儿网的java应用生产问题诊断工具，提供了一站式的问题诊断方案](https://github.com/qunarcorp/bistoury)
2. [vjtools/README.md at master · vipshop/vjtools · GitHub](https://github.com/vipshop/vjtools/blob/master/vjtop/README.md)

##### 扩展
1. [Arthas 实践分享Rainbond](https://mp.weixin.qq.com/s/ukgqZPVJ4L0v1d2dbEOMgw)
2. [用 Arthas 定位 Spring Boot 接口的超时问题，让应用起飞～ - 掘金](https://juejin.cn/post/7140462361759973384)