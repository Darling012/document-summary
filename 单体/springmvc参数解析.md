## controller 接收参数
[SpringMVC请求参数接收总结 - 简书](https://www.jianshu.com/p/5f6abd08ee08)
1. @PathVariable
	1. PathVariableMapMethodArgumentResolver
2. @PathParam
3. @RequestParam
	1. get
	2. post
4. @RequestBoby
	1. RequestResponseBodyMethodProcessor

RequestParamMethodArgumentResolver
1. @RequestParam 注解的参数
2. MultipartFile 类型参数
3. Simple 类型 (如 long、int) 参数


HandlerMethodArgumentResolverComposite，里面装载了所有启用的 HandlerMethodArgumentResolver 子类。而 HandlerMethodArgumentResolver 子类在解析参数的时候使用到 HttpMessageConverter，而 HandlerMethodArgumentResolver 子类到底依赖什么 HttpMessageConverter 实例实际上是由请求头中的 ContentType


[Spring MVC 类型转换器（Converter） - emanlee - 博客园](https://www.cnblogs.com/emanlee/p/15150081.html)