# web统一异常处理
##### 需要做
1. 对捕获的异常进行封装，放入trancId、spanID；打印完整异常链
2. 在advice中能够从异常中获取trancId和spanId然后放入统一返回体
3. 异常时获取当前参数
##### 自定义异常处理器
1. HandlerExceptionResolver#resolveException
2. 注解
3. 当两种方式都实现时，HandlerExceptionResolver要先于ControllerAdvice执行
4. 404等，继承AbstractErrorController、ResponseEntityExceptionHandler
##### reference
1. [全局异常处理](https://blog.csdn.net/havedream_one/article/details/87461303)
2. [Spring Boot 中自定义异常处理](https://blog.csdn.net/u013360850/article/details/93101903)

##### 匹配哪一个？
1. [Springboot多个异常处理类catch顺序](https://blog.csdn.net/qq_34988540/article/details/86664000)

##### 统一异常处理源码流程
![spring mvc 源码异常处理流程](spring异常体系.md#spring%20mvc%20源码异常处理流程)

