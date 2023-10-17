# 分布式 ID 特点
1. 全局唯一
2. 高并发
3. 高可用
## [实现方案](file:///Volumes/resources/%E5%88%86%E5%B8%83%E5%BC%8F/320%E3%80%81%E7%8E%A9%E8%BD%ACN%E7%A7%8D%E4%BC%81%E4%B8%9A%E7%BA%A7%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88%EF%BC%8C%E7%AC%91%E5%82%B2%E5%88%86%E5%B8%83%E5%BC%8F%E5%BC%80%E5%8F%91%EF%BC%88%E5%AE%8C%E7%BB%93%EF%BC%89/%E7%AC%AC2%E7%AB%A0%20%E6%9E%81%E9%80%9F%E4%B8%8A%E6%89%8B%E5%88%86%E5%B8%83%E5%BC%8FID/2-1%20%E5%88%86%E5%B8%83%E5%BC%8FID-%E7%90%86%E8%AE%BA%E9%83%A8%E5%88%86.mp4#t=634.609602)
1. uuid
	1. 不能生成递增有序地数字，造成索引效率下降
	2. 空间占用较高
	3. [ULID - 一种比UUID更好的方案_pushiqiang的博客-CSDN博客_除了uuid](https://blog.csdn.net/pushiqiang/article/details/117365290)
2. 数据库主键自增
	1. 并发性能不高，受限于数据库性能
	2. 分库分表，需改造，较复杂
	3. 自增：数据量泄露（时间段前后的 ID 相减）
	4. 号段模式
3. redis 自增
	1. 数据丢失
	2. 自增：数据量泄露
4. [雪花算法](file:///Volumes/resources/%E5%88%86%E5%B8%83%E5%BC%8F/320%E3%80%81%E7%8E%A9%E8%BD%ACN%E7%A7%8D%E4%BC%81%E4%B8%9A%E7%BA%A7%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88%EF%BC%8C%E7%AC%91%E5%82%B2%E5%88%86%E5%B8%83%E5%BC%8F%E5%BC%80%E5%8F%91%EF%BC%88%E5%AE%8C%E7%BB%93%EF%BC%89/%E7%AC%AC2%E7%AB%A0%20%E6%9E%81%E9%80%9F%E4%B8%8A%E6%89%8B%E5%88%86%E5%B8%83%E5%BC%8FID/2-2%20%E5%88%86%E5%B8%83%E5%BC%8FID%E7%90%86%E8%AE%BA-%E9%9B%AA%E8%8A%B1%E7%AE%97%E6%B3%95.mp4#t=364.029684)
	1. 时钟回拨
	2. 不依赖外部组件
	3. 1bit 符合位 + 41bit 时间戳位 +1 0bit 工作进程位 (区域+服务器标识) + 12bit 序列号位
方案总结
1. 号段模式
2. 雪花算法

## [开源组件](file:///Volumes/resources/%E5%88%86%E5%B8%83%E5%BC%8F/320%E3%80%81%E7%8E%A9%E8%BD%ACN%E7%A7%8D%E4%BC%81%E4%B8%9A%E7%BA%A7%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88%EF%BC%8C%E7%AC%91%E5%82%B2%E5%88%86%E5%B8%83%E5%BC%8F%E5%BC%80%E5%8F%91%EF%BC%88%E5%AE%8C%E7%BB%93%EF%BC%89/%E7%AC%AC2%E7%AB%A0%20%E6%9E%81%E9%80%9F%E4%B8%8A%E6%89%8B%E5%88%86%E5%B8%83%E5%BC%8FID/2-3%20%E5%88%86%E5%B8%83%E5%BC%8FID-%E5%BC%80%E6%BA%90%E7%BB%84%E4%BB%B6%E4%BB%8B%E7%BB%8D.mp4#t=935.727596)
1. 百度 uid-generator
	1. 只支持雪花算法
2. 滴滴 tinyid
	1. 只支持号段
	2. 支持多 DB，高可用，Java-client
3. 美团 leaf
	1. 支持雪花、号段
	2. 提供了一个 springboot
	3. 上传服务器标识到 zk

##### reference
1. [分布式系统唯一ID生成方案汇总](https://www.cnblogs.com/haoxinyue/p/5208136.html)