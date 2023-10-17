## 优化排查故障
便易程度： jvm 自带命令 -> jvm 自带工具  -> arthas



[Java 性能分析与优化导读 | 未读代码](https://www.wdbyte.com/java/performance/)
##### jmx
[JMX 监控和管理 Java 程序 | 未读代码](https://www.wdbyte.com/java/jmx/)

##### cms优化

### jvm 自带命令及工具
《深入理解 Java 虚拟机》第 4 章
[JVM自带性能调优工具（jps,jstack,jmap,jhat,jstat,hprof) - Josh_Persistence - ITeye博客](https://www.iteye.com/blog/josh-persistence-2161848)
1.  jps (Java Virtual Machine Process Status Tool) 
	1. 主要用来输出 JVM 中运行的进程状态信息
2. jstack
	1. 主要用来查看某个 Java 进程内的线程堆栈信息
	2.  jstack 可以定位到线程堆栈，根据堆栈信息我们可以定位到具体代码
3.  jmap（Memory Map）和 jhat（Java Heap Analysis Tool）
	1. jmap 导出堆内存，然后使用 jhat 来进行分析
4. jstat（JVM 统计监测工具）
	1. 看看各个区内存和 GC 的情况
5. hprof（Heap/CPU Profiling Tool）
	1.  hprof 能够展现 CPU 使用率，统计堆内存使用情况。
6. [JVisualVM](https://mp.weixin.qq.com/s?__biz=MzAxNjM2MTk0Ng==&mid=2247492143&idx=1&sn=3dd58095736551582c864729ff2230e5)
	1. VisualVM 对运行中的 Java 应用提供了可视化的信息展示, 它是很多工具的整合包，整合了 JConsole,jstat,jinfo,jstack 以及 jmap
	2. [JVM性能调优监控工具专题二：VisualVM基本篇之监控JVM内存，CPU，线程 - Josh_Persistence - ITeye博客](https://www.iteye.com/blog/josh-persistence-2233445)
	3. [JVM性能调优监控工具专题二：VisualVM基本篇之远程监控，监控Tomcat - Josh_Persistence - ITeye博客](https://www.iteye.com/blog/josh-persistence-2233459)
	4. [JVM性能调优监控工具专题三：VisualVM基本篇之快照分析、监控GC、Eclipse集成 - Josh_Persistence - ITeye博客](https://www.iteye.com/blog/josh-persistence-2233787)

#### 案例
《深入理解 Java 虚拟机》第 5 章
[线上服务出现OOM](https://mp.weixin.qq.com/s/G-GqY17yo1fpdItJieY2FA)
[jvm调优的几种场景 - 我是满意吖 - 博客园](https://www.cnblogs.com/spareyaya/p/13174003.html)
###### cpu 占用过高
两种情况
1. 业务流量短暂过大，如搞活动，不算故障
2. cpu 占用率长期过高，如死循环，需要解决
排查步骤
1. 用 top 命令查看 cpu 占用过高的进程，获得 pid
	1. linux 下，top 命令获得进程号 pid 和 jps 工具获得的 vmid 是相同的
2. 用 top -Hp pid 查看线程情况
3. 把线程号转为 16 进制
4. 用 jstack 工具查看线程栈情况
	1. jstack 进程号 | grep 16 进制线程号 -A 10
###### 死锁
1. jps 查看 java 进程
2. jstack 查看死锁问题
###### 内存泄露
1. 加运行参数 `-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=heap.bin`
	1. 发生OOM时把堆内存信息dump出来
2. MAT

生产往往内存快照很大，dump 到本地内存不够大无法分析，另外一种处理方法
1. 用 jps 定位到进程号
2. 用jstat分析gc活动情况
3. 用 jmap 工具 dump 出内存快照

最该采用 [用 arthas 解决](https://www.cnblogs.com/spareyaya/p/13177513.html)

[JVM 线上故障排查基本操作 - 简书](https://www.jianshu.com/p/bca5a49db4b7)

## arthas
[arthas](arthas.md)
## reference
4. [Java 逃逸分析](https://mp.weixin.qq.com/s?__biz=MzI3ODcxMzQzMw==&mid=2247489776&idx=1&sn=74a93cea618aec7ff5af173f9b6a0626)
8. [性能分析原则](https://mp.weixin.qq.com/s/VEup6GmcZvCDqnQUyd_dvA)
9. 


