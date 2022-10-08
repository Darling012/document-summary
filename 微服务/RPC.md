## rpc

1. [微服务之间的最佳调用方式](https://mp.weixin.qq.com/s?__biz=MzU0MzQ5MDA0Mw==&mid=2247488428&idx=2&sn=bd688553c82af1f597e5adfef666b996&chksm=fb0bf938cc7c702e6b1af4d18b279520dcdf7a21bf73d7482aabc1bf068f674c662b7f491e34&mpshare=1&scene=1&srcid=&sharer_sharetime=1580145600204&sharer_shareid=07754c1336c3524bfffedc4dc59111b6&key=1de96bf14ac83757a9c10aa802bee067fce6b42c902237f3ef5eb70fbbacf01cb54182438ae253d413a0b182f70b8f240fd86a78a253d8434f5f97162691f59ebe7ea39accdc5957595938cf48ffa27b&ascene=1&uin=MTY5NjI3ODY2MQ%3D%3D&devicetype=Windows+10&version=62070158&lang=zh_CN&exportkey=AY7m%2FDd68vPir5gLzpgMPA0%3D&pass_ticket=w0h%2FGWHBYwvP7E%2BIIOeqS7OZUHTXM1M%2B1MRno83Zh0Q1s56m5uIB9otCBPvTvaUT)
2. 

```java
        GenericResponse response = null;
        try {
            response = HttpClint.invoke();
        } catch (Exception exception) {
            // httpClient调用产生的异常
            log.error(exception);
            handlerErrorAndThrowExcetption(exception);
        }
        // 调用成功返回结果
        if (response.code != 200) {
            handlerErrorAndThrowExcetption();
        }
        Pojo pojo = response.getData();
```

**能否用布尔值代替void作为返回值？**ps:正如在没有异常之前用-1,0当返回值表示异常

https://www.zhihu.com/question/321451061

不行

1. 语义混淆，假如单指程序是否成功运行，那false的时候，就会包含多种情况，不明确。
2. 对于确实不需要返回的函数它是过度设计了，对于 false 不能明确失败原因的场景，还是要引入异常或其他机制，而代码逻辑因此要分散到返回值和异常两个不同的执行路径，无谓增加圈复杂度。那还不如统一用异常好了，或者类似 Go 的做法，返回 （result, error)。
3. 就是函数的调用方只知道是否成功，但是并不知道失败的原因是什么。所以 Go 和 Lua 的惯用手法除了布尔值表示成功失败以外，还会有个错误原因的额外返回值。WIN32 API则要么提供`GetLastError` ，要么用的`HRESULT` 代替布尔值来提供异常原因。
4. 测试单元的方法 明确要求是void的
5. 如果确定函数内不会有问题或意外发生的，那返回true或false没意义，如果会有意外发生的，那true或false不足够携带所有信息

#### 讨论Rpc情况

既然不能用布尔值代替，那么void函数执行失败应该抛出异常。在多模块rpc情况下，就是当前模块抛出异常，统一捕获后封装进响应体。调用模块判断响应体状态码，若失败则抛出异常。即上边模板所示流程。

1. ##### rpc调用异常

   先讨论一下第一步httpClent的调用产生的异常

   1. 若不trycatch rpc调用方法，不捕获异常，那么异常会直接向外抛出，advice统一异常处理是可以捕获到的。
   2. 配合ribbion、熔断后，以及fegin自定义ErrorDecoder配置，可捕获掉调用异常。

2. ##### 业务异常

   第二步判断调用结果中的业务是否有异常

3. ##### rpc异常在哪里捕获处理？

   1. 调用方处理：可以灵活自定义异常msg，补充进模块信息，细分是哪个业务错误。
   2. fegin等统一处理：因为配置大概率是对整个项目起作用，所以不能补充诸如 谁调用了谁  这种异常信息。

##### 项目：

1. k12中业务异常http状态码为非200，会进入fegin的ErrorDecoder
2. ErrorDecoder通过swich case httpStatus后，重新抛出了重试、token过期异常，在swich default尝试解析返回体判断有没有code msg，没有抛出FeignRequestException，有则构建抛出内部服务调用异常,且try catch default 抛出Exception进行最大的兜底，最后交由advice统一异常处理捕获。
3. k12在自定义异常设计中，业务异常用httpStatus非200标识，且后边的业务系统都至少经过一层fegin调用。
4. AIVideo项目ErrorDecoder，对httpStatus>=400 && <=500的都统一抛出DeepBlueSysException，其他状态码返回feign.FeignException.errorStatus(methodKey, response);，并@SneakyThrows兜底。
5. AIVideo的业务错误httpStatus为200，不会进入fegin错误处理，所以在fegin调用后要判断业务状态码

##### reference

1. [**Spring Cloud Feign--全局异常处理--方法/实例**](https://blog.51cto.com/knifeedge/5139865) void 情况？

2. [Feign自定义ErrorDecoder错误时返回统一结构](https://blog.csdn.net/new9xgh/article/details/107934862)

3. [原生Feign的解码器Decoder、ErrorDecoder](https://cloud.tencent.com/developer/article/1588501)

4. [feign和ribbon的异常捕捉](https://www.cnblogs.com/chuliang/p/13100531.html)

5. [SpringCloud组件OpenFeign——将服务端详细异常信息返回给客户端](https://blog.csdn.net/m0_47503416/article/details/122089913)

6. [feign服务端出异常客户端处理的方法](https://www.cnblogs.com/lori/p/11157394.html)

## HTTPClient

1. [HTTP超时、重复请求必见坑点及解决方案](https://mp.weixin.qq.com/s?__biz=MzAwNjkxNzgxNg==&mid=2247489656&idx=2&sn=4a4e08d448fd29eb52e99ecc6e6baffd&chksm=9b0743afac70cab9f95cdd18b910bd26faa021264e9a66866161e47bde4fd6afee21f32abbe7&mpshare=1&scene=1&srcid=12070SJCGoYxHUAsg1nXb09x&sharer_sharetime=1607354508881&sharer_shareid=07754c1336c3524bfffedc4dc59111b6&key=54253d61e149ca80533ac1405a13d8e7d764e680d4a02fec40aee220453e90346662968d918c250ce7ce9d8494b702efb83f9a69af0f0444c60171880273cbb1dfd968cb79fc02986ac70be139a931f86b3d87ee1f92720e667a61f3ff9c61cbbbb74b4e35e65c2ab9082efd1d1720983c4c63d464d223bcab215e5eb4cfc94a&ascene=1&uin=MTY5NjI3ODY2MQ%3D%3D&devicetype=Windows+10&version=62080079&lang=zh_CN&exportkey=AacUOt3UCGkSWRrl9x6%2FzEw%3D&pass_ticket=ohX3vsEs9B4U%2FeDpPdBg6umBNMGxbFS1FmZsoIQqhvoKetmakeEg0RLTwkql3aUX&wx_header=0)
2. 

## Fegin

##### 问题：

1. 

1.  全局异常处理
2. get请求多参数

##### reference

1. https://juejin.cn/column/7087106428485238791
2. https://www.modb.pro/db/109223
3. http://docs.springcloud.cn/user-guide/feign/

### 配置

### @FeignClient各参数

1. [那天晚上和@FeignClient注解的深度交流](https://juejin.cn/post/6844904039595917319)

##### 超时与重试

1. [聊聊openfeign的超时和重试](https://cloud.tencent.com/developer/article/1749558)

#### 日志

1. [Feign 的细粒度与全局 日志打印](https://blog.csdn.net/Tiny_Demon/article/details/124717616)

##### 丢失请求头

1.  [Feign 调用丢失Header的解决方案](https://www.cnblogs.com/huanchupkblog/p/11895979.html)

### Fegin的继承

1. [使用Feign的一些问题以及如何解决？](https://mp.weixin.qq.com/s/yzGWfxBsRbeDdDFYXQHNvg)

### 调用过程

1.  [Spring Cloud Feign 调用过程分析](https://www.cnblogs.com/rickiyang/p/11802487.html)

### 常见问题

多参数请求、上传文件、form表单、分页

1. [Feign常见问题总结](https://www.imooc.com/article/289005)
2. [Spring Cloud Feign 复杂用法：文件上传+复杂参数传递【官方方案】](https://hicode.club/articles/2018/12/25/1550590735080.html)
3. [Feign远程调用传递对象参数 并 返回自定义分页数据完整过程](https://blog.csdn.net/qq_36068521/article/details/102565751)
4. [如何使用Feign构造多参数的请求](https://www.imooc.com/article/289000)
5. [RequestParam，RequestBody，SpringQueryMap](https://www.jianshu.com/p/9d1d770e22b0)




### 加密加签

##### 问题

1. 如何保证数据在传输过程中的安全性
2. 数据已经到达服务器端，服务器端如何识别数据，如何不被攻击

##### 问题1解决

1. https
2. 非对称加密
