# excel导入数据

1. 解析Excel，数据正确性校验
   1. 能正确解析的数据parseSuccessList
   2. 解析失败的数据failList,失败原因
2. parseSuccessList进行业务性校验
   1. 符合业务规则可入库的数据readyList
   2. 不符合业务规则的数据放入failList,失败原因
3. readyList进行入库
   1. 成功
   2. 失败数据放入failList,失败原因
[大数据量同步](大数据量同步.md)
[mybatisPlus批量操作]()

[多线程事务]()

PS：
[数据校验问题](#数据业务逻辑校验)，无业务的必填长短等校验通过Java Bean Validation，从各层中抽取出来在bean上做。
有业务逻辑的校验如数据唯一性，目前看要提取出校验接口然后调用比较合理。像新增学生只涉及一个表操作的，无论数据来源 web 还是 excel 导入还是微信同步，都能在新增学生接口控制字段唯一，没有提前业务验证到三个入口的必要。业务验证前置的是因为涉及多个表多个模块避免做补偿。

[百万数据的导入导出解决方案_liu.kai的博客-CSDN博客_百万数据导出](https://blog.csdn.net/weixin_44848900/article/details/117701981)


![大数据](大数据.md#SPL)