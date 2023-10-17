# token

## 问题
1. oracle openJDK 19 对于 `.signWith(SignatureAlgorithm.HS512, secret)` 的失败
	1. [Fetching Title#n387](https://blog.csdn.net/u010748421/article/details/107363925)
2. 

[JWT体系](https://mp.weixin.qq.com/s/mJqUE_EIEFDUi10dy4yghQ)
1. jose: json object signing and encryption
	1. jwa
	2. jwk
	3. jwt
		1. jws: 通常所说的 jwt
		2. jwe


## JWT
只防篡改，不保密
##### 结构
1. header (头部)：
	1. json 对象，描述 jwt 的元数据
	2. alg：签名算法类型，默认 HMAC SHA256 (HS256)
	3. typ: token 类型，统一 JWT
3. payload (载荷)：
	1. json 对象，自定义字段
4. signature (签名)：
	1. 对前两部分 base64 编码后用"."连接起来再用算法加密



5. [JWT VS OAuth2](https://mp.weixin.qq.com/s/9UZAsBhBfe6JCUzhXNURFw)
6. [JWT-Json Web Token使用详解及注意事项 - 程序新视界](https://www.choupangxia.com/2019/11/20/jwt-json-web-token/)
7. [基于token的多平台身份认证架构设计 - 一点一滴的Beer - 博客园](https://www.cnblogs.com/beer/p/6029861.html)


## 存在哪
1：基础款，后端接口拦截判断是否 token 过期，如果过期返回 401，没过期继续业务请求，前端请求拦截器内加判断；  
2：基础款 Plus，后端返回 token 之后返回过期时间，前端保存在本地，请求之前先判断是否过期，如过期直接提示，中断请求。

[Token一般存放在哪里？ - 掘金](https://juejin.cn/post/6922782392390746125)



[chrome 浏览器的清空缓存功能为啥不清 localstorage - V2EX](https://www.v2ex.com/t/914660)
[localStorage，sessionStorage存储限制是多大？ · Issue #179 · FrankKai/FrankKai.github.io · GitHub](https://github.com/FrankKai/FrankKai.github.io/issues/179)
