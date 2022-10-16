# web统一异常处理
##### 需要做
1. 对捕获的异常进行封装，放入trancId、spanID；打印完整异常链
2. 在advice中能够从异常中获取trancId和spanId然后放入统一返回体
3. 异常时获取当前参数
##### 自定义异常处理器
1. HandlerExceptionResolver#resolveException ^2blsnu
2. 注解
3. 当两种方式都实现时，HandlerExceptionResolver要先于ControllerAdvice执行
4. 404等，继承AbstractErrorController、ResponseEntityExceptionHandler
##### reference
1. [全局异常处理](https://blog.csdn.net/havedream_one/article/details/87461303)
2. [Spring Boot 中自定义异常处理](https://blog.csdn.net/u013360850/article/details/93101903)
##### 匹配哪一个？
1. [Springboot多个异常处理类catch顺序](https://blog.csdn.net/qq_34988540/article/details/86664000)
##### 统一异常处理源码流程
[spring mvc 异常处理流程](spring%20mvc%20异常处理流程.md#spring%20mvc%20源码异常处理流程)



## spring mvc 源码异常处理流程

1. ExceptionHandlerMethodResolver： 此缓存Map存放了@ControllerAdvice中所有注解了@ExceptionHandler的方法，其中@ExceptionHandler的value也就是Exception做为Key，值为当前Method
2. exceptionHandlerAdviceCache： key为标注了@ControllerAdvice的类，value为ExceptionHandlerMethodResolver
3. SpringMVC通过HandlerExceptionResolver的resolveException调用实现类的实际实现方法doResolveException
4. doResolveException方法实际调用ExceptionHandlerExceptionResolver的doResolveHandlerMethodException方法。
5. 根据类型进行匹配，匹配不到getCause（）再匹配，匹配多个排序后取第一个
##### reference
1.  [优雅的处理你的 Java 异常](https://my.oschina.net/c5ms/blog/1827907)
1.  [SpringBoot 处理异常的几种常见姿势](https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247485568&idx=2&sn=c5ba880fd0c5d82e39531fa42cb036ac&chksm=cea2474bf9d5ce5dcbc6a5f6580198fdce4bc92ef577579183a729cb5d1430e4994720d59b34&token=1924773784&lang=zh_CN#rd)
1.  [掌握 Spring 之异常处理](https://juejin.cn/post/6844903846544670734#heading-2)