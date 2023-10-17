# Java异常体系
## 异常类
**Throwable**
1. **Error** 
   1. 非程序错误；Java 运行时内部错误和资源耗尽错误。一般指 jvm 错误，无法恢复或不能捕获，程序无法处理。
2. **Exception**
   1. **RuntimeException**  代码问题；程序错误导致的异常；
   2. **非运行时异常**           程序没有问题，如I/O错误导致的异常；
**非受检异常：** 派生于 Error 类或 RuntimeException 类的所有异常
**受检异常**：其他所有异常
1. [Java异常类的组织结构](https://mp.weixin.qq.com/s/xbopgxZ5BEDdSvwO9ad9Xg)

##### 异常链
#####  try-catch-finally
1.  [try{}catch的隐藏(如何优雅的实现异常块)](https://www.cnblogs.com/liruilong/p/13403963.html)
1.  [try-catch-finally中的4个巨坑](https://mp.weixin.qq.com/s?__biz=Mzg3MjA4MTExMw==&mid=2247500490&idx=1&sn=ffb3f82a4236bc9c292d2bee965711d1&chksm=cef6317cf981b86a9fad4fb69761ec0a2a85187e4c25e1c9cc544c69af14976afd29ad4b4925&mpshare=1&scene=1&srcid=0321dYYFIwps3lqfq1QMVj0e&sharer_sharetime=1616338399063&sharer_shareid=07754c1336c3524bfffedc4dc59111b6#rd)
##### reference
1. [Java运行时异常和非运行时异常](https://blog.csdn.net/huhui_cs/article/details/38817791)
2. [Java异常知识汇总](https://blinkfox.github.io/2018/10/28/hou-duan/java/java-yi-chang-zhi-shi-hui-zong/)
3. [Java异常面试题](https://blog.csdn.net/ThinkWon/article/details/101681073)
4. [Java异常经典14问](https://mp.weixin.qq.com/s/hWeEWlMFP51gKL7A9AgdJw)

## java异常传播
1. [Java中的异常传播(一)](http://imyida.com/java-exceptions_1.html)
##### 父子异常
1. [java 父子异常](https://www.cnblogs.com/iamzhoug37/p/4403357.html)

#### Java 线程池异常
[Java 线程池异常](threadPool.md#线程池异常处理)


#### lambda 异常处理
