### mybatisPlus批量操作

1. 单条sql，批量提交
   1. 需要开启数据库批量提交rewriteBatchedStatements=true
   2. mybatisplus的saveOrUpdateBatch会生成一条查询sql
   3. https://www.jianshu.com/p/7eb8eec78b9a
   4. https://blog.csdn.net/qq271859852/article/details/79562262
   5. https://www.jianshu.com/p/04d3d235cb9f
   6. https://github.com/baomidou/mybatis-plus/issues/2456
   7. https://github.com/baomidou/mybatis-plus/issues/2786
2. 大sql更新
   1. insert into user(id, name, age) values (1, "a", 17), (2,"b", 18)
      1. https://cloud.tencent.com/developer/article/1886324
   2. 数据量过大或字段过多造成拼接sql过长问题
      1. 改参数

##### mySql执行以上两种sql效率分析

1. https://segmentfault.com/a/1190000008890065

#### 逻辑删除与唯一索引冲突

https://baobao555.tech/archives/39

https://chsm1998.github.io/2020/08/29/logical-deletion-and-unique-index/