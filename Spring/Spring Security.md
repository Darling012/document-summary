# Spring Security
Spring Security 是 Spring 采用 AOP 思想，基于 Servlet 过滤器实现的安全框架


## reference
==专栏==
[spring-security_nrsc的博客-CSDN博客](https://blog.csdn.net/nrsc272420199/category_9090165.html)
[Spring Security 源码分析 | 伤神的博客](https://www.shangyang.me/categories/%E8%AE%A1%E7%AE%97%E6%9C%BA%E7%A7%91%E5%AD%A6%E4%B8%8E%E6%8A%80%E6%9C%AF/Spring/Security/)


1. [Spring Security的@PreAythorize、@PostAuthorize、@PreFilter 和@PostFilter_诚o的博客-CSDN博客_@prefilter](https://blog.csdn.net/qq_22771739/article/details/86540319)
6.  [SpringSecurity + JWT](https://mp.weixin.qq.com/s?__biz=MzU0MzQ5MDA0Mw==&mid=2247492165&idx=2&sn=e5663d3262d723a4676e3f9f9af80bdb)
7. [ Spring Security 从入门到实战](https://mp.weixin.qq.com/s?__biz=MzUzMTA2NTU2Ng==&mid=2247493456&idx=1&sn=147d47807c2dce7ffb7f36cf43f8d134)
8. [Spring-Gateway 与 Spring-Security 在前后端分离项目中的实践](https://mp.weixin.qq.com/s/8o7lwM2U4GJSx69wET4ejw)

[一文看懂Spring Security](https://mp.weixin.qq.com/s/RPT06-VZ1iRiRgfKt5wo_g)
Spring Security 是 Spring 采用 AOP 思想，基于 Servlet 过滤器实现的安全框架

==主要的过滤器链==
1. SecurityContextPersistenceFilter
	1. 在 session 中保存或更新一个 SecurityContext（threadLocal），存储了当前用户认证及授权信息。
2. WebAsyncManagerIntegrationFilter
	1. 用于集成 SecurityContext 到 Spring 异步执行的 WebAsyncManager
3. HeaderWriterFilter
	1. 向请求头 Header 中添加相应的信息。
4. CsrfFilter
	1. Spring Security 会对所有 POST 请求验证是否包含系统生成的 CSRF 的 Token 信息，如果不包含则报错，起到防止 CSRF 共计的效果。
5. LogoutFilter
	1. 匹配 URL 为 /logout 的请求，实现用户退出清除认证信息（ps 是否是清除 SecurityContext？）
6. UsernamePasswordAuthenticationFilter
	1. 认证操作，默认匹配 URL 为 /login 且必须为 POST 请求
7. DefaultLoginPageGeneratingFilter
	1. 默认的登录页面，该过滤器会生成一个默认的认证页面
8. DefaultLogoutPageGeneratingFilter
	1. 默认的登出页面，该过滤器会生成一个默认的登出页面。
9. BasicAuthenticationFilter
	1. 过滤器会自动解析 HTTP 请求头中名字为 Authentication，且以 BASIC 开头的头部信息
	2. `Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==`
10. RequestCacheAwareFilter
	1. 通过 HttpSessionRequestCache 内部维护一个 RequestCache，用于缓存 HttpServletRequest
11. SecurityContextHolderAwareRequestFilter
	1. 针对于 ServletRequest 进行一次包装，使 Request 具有更多的 API。
12. AnonymousAuthenticationFilter
	1. 在 SecurityContextHolder 中认证信息为空的时候，会创建一个匿名的用户存入到 SecurityContextHolder 中，Spring Security 为了兼容未登录的访问，使用匿名的身份走了一套认证的流程。
13. SessionManagementFilter
	1. 限制一个用户开启多个会话的数量
14. ExceptionTranslationFilter
	1. 转换过滤器链中出现的异常，在过滤器链的最后。
15. FilterSecurityInterceptor
	1. 获取所配置资源的访问授权信息，根据 SecurityContextHolder 中存储的用户信息决定是否有权限访问。


[SpringBoot 集成 Spring Security 自定义认证逻辑备忘_独坐一隅的博客-CSDN博客](https://blog.csdn.net/nangongyanya/article/details/82150848)
==流程==
1. Spring Security 将会通过其内置的拦截器对 URL 进行拦截，以此来管理登录验证和用户权限验证。
2. 当用户登陆时，会被 AuthenticationProcessingFilter 拦截
3. 调用 AuthenticationManager 的实现类来验证用户信息
4. 而 AuthenticationManager 是通过调用 AuthenticationProvider 的实现类来获取用户验证信息
5. 在 AuthenticationProvider 的实现类中将获取到的用户验证信息与通过 UserDetailsService 的实现类根据用户名称获取到的结果（UserDetails 的实现类）进行匹配验证
	1. `AuthenticationProvider#authenticate(Authentication authentication)` 返回 Authentication
	2. `UserDetailsService#loadUserByUsername(String username)` 返回 UserDetails
6. 如果验证通过后会将用户的权限信息封装一个当前登录用户信息对象 放到 Spring Security 的全局缓存 SecurityContextHolder 中，以备后面访问资源时使用。

因此当我们自定义用户认证逻辑的时候，我们需要编写 AuthenticationProvider、UserDetailsService 和 UserDetails 的实现类


[详解 Spring Security 基于表单登录的认证模式](https://mp.weixin.qq.com/s?__biz=MzAwMjk5Mjk3Mw==&mid=2247492327&idx=1&sn=cb32971ab935ebb09bd66188a8c7215d)
![600|600](Pasted%20image%2020230321140134.png)


[SpringBoot整合Spring Security过滤器链加载执行流程源码分析\_51CTO博客\_spring security 过滤器](https://blog.51cto.com/xiongmaoit/6127205)
