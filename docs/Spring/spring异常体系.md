# spring 异常体系
1. 定时任务
2. @Async
   1.  [Spring @Async的异常处理](https://www.cnblogs.com/jpfss/p/10272085.html)
3. spring提供的线程池
4. [spring mvc](web统一异常处理.md)
5. 
## spring mvc 源码异常处理流程
1. ExceptionHandlerMethodResolver： 此缓存 Map 存放了@ControllerAdvice 中所有注解了@ExceptionHandler 的方法，其中@ExceptionHandler 的 value 也就是 Exception 做为 Key，值为当前 Method
2. exceptionHandlerAdviceCache： key 为标注了@ControllerAdvice 的类，value 为 ExceptionHandlerMethodResolver
3. SpringMVC 通过 HandlerExceptionResolver 的 resolveException 调用实现类的实际实现方法 doResolveException
4. doResolveException 方法实际调用 ExceptionHandlerExceptionResolver 的 doResolveHandlerMethodException 方法。
5. 根据类型进行匹配，匹配不到 getCause（）再匹配，匹配多个排序后取第一个






##### reference
1.  [优雅的处理你的 Java 异常](https://my.oschina.net/c5ms/blog/1827907)
1.  [SpringBoot 处理异常的几种常见姿势](https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247485568&idx=2&sn=c5ba880fd0c5d82e39531fa42cb036ac&chksm=cea2474bf9d5ce5dcbc6a5f6580198fdce4bc92ef577579183a729cb5d1430e4994720d59b34&token=1924773784&lang=zh_CN#rd)
1.  [掌握 Spring 之异常处理](https://juejin.cn/post/6844903846544670734#heading-2)
1. [SpringMVC异常处理机制详解附带源码分析](https://www.cnblogs.com/fangjian0423/p/springMVC-exception-analysis.html)
2. [SpringMVC 异常处理体系深入分析](https://juejin.cn/post/6951317320845230116)
3. [浅谈springboot异常处理机制](https://blog.csdn.net/u013194072/article/details/79044286)
4. [Spring MVC源码(四) ----- 统一异常处理原理解析](https://www.cnblogs.com/java-chen-hao/p/11190659.html)