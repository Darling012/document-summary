# ORM类型映射

##  JavaType 与 JdbcType

[JDBCType (Java SE 20 & JDK 20)](https://docs.oracle.com/en/java/javase/20/docs/api/java.sql/java/sql/JDBCType.html)
[MyBatis的jdbcType和javaType详解](https://cloud.tencent.com/developer/article/1797599)

1. 在 mybatis 的 mapper 文件中 jdbcType 属性底层对应的是一个 JdbcType 枚举类，所以 jdbcType 属性的值对应的都是大写
2. 在 mybatis 源码中 JdbcType 枚举类的每一个值都有对应的处理类，如 ARRAY 对应的处理类为 ARRAYTypeHandler。所有的处理类都在 org.apache.ibatis.type 包下。[MyBatis--系统定义的TypeHandler、BaseTypeHandler和StringTypeHandler源码分析](https://blog.csdn.net/cold___play/article/details/102792448)
3. [字段类型处理器 | MyBatis-Plus](https://baomidou.com/pages/fd41d8/)

[MySQL数据类型与Java数据类型 - 知乎](https://zhuanlan.zhihu.com/p/68670328)
![500|500](Pasted%20image%2020230322141435.png)



[MySQL数据类型](MySQL数据类型.md)

mysql 5.7.8 后新增 `json` 格式，javaType 的 `String` 、`List` 都可以映射，但需要自定义 `covert`
[关于json类型在java-mysql之间的存取\_mysql json实体类对象声明\_钦之璇的博客-CSDN博客](https://blog.csdn.net/qq_42305089/article/details/120194101)


##### javaType 的 List 转 mysql 字符串类型
利用 mybaitsplus 在 `entity` 中 ` list 类型 ` 字段加 ` @TableField(typeHandler = ListTypeHandler.class) ` 实现转为 mysql 的字符串类型