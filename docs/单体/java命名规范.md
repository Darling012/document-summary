# java 命名规范

[Java 命名规范](https://mp.weixin.qq.com/s/DMPfd_F1bHLT87mIlA4r5w)
## pojo 命名

并不能统一，比如 vo 有的是 view object 有的是 value object。所以讨论的价值仅限于团队内，或公司有明确规范团队内项目有明确分层保证应用的情况，不然团队团队之间、第三方与自身都有可能定义不一致。

在项目不够大，或者不成体系的时候不如直接用 request、response、entity、param、result 这种见名知意的后缀。降低沟通交流成本。

[浅析 VO、DTO、DO、PO 的概念、区别和用处！](https://mp.weixin.qq.com/s?__biz=MzU0OTk3ODQ3Ng==&mid=2247487387&idx=1&sn=f2b201590eeea936008031b1b19558dd)
1. vo
	1. view object: 视图对象。
2. 



[领域驱动设计系列文章（2）——浅析VO、DTO、DO、PO的概念、区别和用处 - Cat Qi - 博客园](https://www.cnblogs.com/qixuejia/p/4390086.html)

[PO,VO,DAO,BO,POJO 之间的区别你懂吗？](https://mp.weixin.qq.com/s/Iqe9CAnYB_9mjY6kqwE7rA)
1. vo 
	1. value object: 值对象，通常用于业务层之间的数据传递，由 new 创建，由 gc 回收
2. po
	1. persistant object: 持久层对象。对应数据库中表的字段。VO 和 PO 都是属性加上属性的 get 和 set 方法；表面看没什么不同，但代表的含义是完全不同的。
3. dto
	1. data transfer object：数据传输对象。
	2. 表里面有十几个字段：id,name,gender(M/F),age,conmpanyId(如001)...
	3. 页面需要展示四个字段：name,gender(男/女),age,conmpanyName(如今日头条股份有限公司)。
	4. DTO由此产生，一是能提高数据传输的速度（减少了传输字段），二能隐藏后端表结构。
4. bo
	1. business object：业务对象
	2. BO 把业务逻辑封装为一个对象。我理解是 PO 的组合，比如投保人是一个 PO，被保险人是一个 PO，险种信息是一个 PO 等等，他们组合起来是第一张保单的 BO
5. pojo
	1. plain ordinary java object：简单无规则 java 对象。
	2. 纯的传统意义的 java 对象，最基本的 Java Bean 只有属性加上属性的 get 和 set 方法。可以转化为 PO、DTO、VO；比如 POJO 在传输过程中就是 DTO。
6. dao
	1. data access object：数据访问对象。
	2. 主要用来封装对数据的访问，注意，是对数据的访问，不是对数据库的访问。