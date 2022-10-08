# mysql锁
[隔离级别与锁的关系](事务.md#事务的隔离级别)
1.  读未提交： 读取数据不加读锁，这样也就不会跟写锁冲突
2.  读已提交： 读操作加读锁，语句执行完立马释放。两条语句各自加、放。
3.  可重复读： 读操作加锁，事务执行完毕以后才释放锁。
4. 序列化：串行读读、读写、写读、写写
### 锁粒度
行级锁：
页级锁：
表级锁：
锁粒度取决于具体存储引擎。InnoDB支持三种粒度。
### InnoDB怎么实现
1. 基于索引实现行锁： 
	select * from tab_with_index where id = 1 for update;
	for update 可以根据条件来完成行锁锁定，并且 ID 是有索引键的列，如果 ID不是索引键那么InnoDB将完成表锁，并发将无从谈起
2. innodb锁算法
	1. Record lock：单个行记录上的锁  
	2. Gap lock：间隙锁，锁定一个范围，不包括记录本身  
	3. Next-key lock：record+gap 锁定一个范围，包含记录本身
##### reference
1. [MySQL锁](https://mp.weixin.qq.com/s?__biz=MzU0MzQ5MDA0Mw==&mid=2247493820&idx=2&sn=176897c44b23ba88760b190556752617)