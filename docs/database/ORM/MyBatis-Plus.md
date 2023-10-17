# MyBatis-Plus

## 问题

1. 
2. `stationService.lambdaUpdate().update(station);`生成的sql where只有个delflag  不是根据id删  
3. [mybatis-plus-boot-starter 3.5.1 版本，removeById方法，类型强制和主键同一类型了 · Issue #4302 · baomidou/mybatis-plus · GitHub](https://github.com/baomidou/mybatis-plus/issues/4302)
4. [MyBatis-Plus批量操作](MyBatis-Plus批量操作.md)
5. 
6. 更新不自动填充问题
	1. `LambdaUpdateWrapper` 更新部分字段时，不会触发配置的自动填充。
		1. [关于Mybatis-plus Lambda自动填充失效和条件为空报错的问题](https://blog.csdn.net/assember/article/details/108617148)
	2. 先 select 出 entity 再 updateById (entity), 更新字段不自动填充问题。在 updateById 方法中所传的实体参数，针对自动填充的字段
		1. 如果字段值非空，则按照所传的值更新；
		2. 如果字段值为空，则按照自动填充的规则更新。
	3. 统一解决
		1. [mybatisplus自动填充更新时间失效 - 知乎](https://zhuanlan.zhihu.com/p/475613715)

## 自动填充
```java
// 起始版本 3.3.0(推荐)  
// 严格填充,只针对非主键的字段,只有该表注解了fill 并且 字段名和字段属性 能匹配到才会进行填充(严格模式填充策略,默认有值不覆盖,如果提供的值为null也不填充)  
// this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());  
setFieldValByName("updateTime",LocalDateTime.now(),metaObject);  
// String userName = SysUserUtil.getUserName();  
String userName = null;  
this.strictUpdateFill(metaObject, "updateName", String.class, userName);
```

`strictUpdateFill()` 先判断原数据这个属性值是否为 null(默认有值不覆盖)，再判断提供的值是否为 null(提供的值为 null 也不填充)
```java 


if (metaObject.getValue(fieldName) == null) {  
    Object obj = fieldVal.get();  
    if (Objects.nonNull(obj)) {  
        metaObject.setValue(fieldName, obj);  
    }  
}
```

`setFieldValByName` 只判断 entity 有没有这个属性 set 方法，及提供的值是否为null
```Java
if (Objects.nonNull(fieldVal) && metaObject.hasSetter(fieldName)) {  
    metaObject.setValue(fieldName, fieldVal);  
}
```

更新的两种写法 
1. 先 select 出 entity，然后 set 更新属性，最后 updateById (entity)，这时整个 entity 的属性都有值（关联[乐观锁](MyBatis-Plus.md#乐观锁)）
	1. `strictUpdateFill()` 严格模式填充策略,默认有值不覆盖，所以 update 字段不会更新
	2. `setFieldValByName` 可以正常更新
2. new 一个新的 entity，然后赋值，此时这个 enity 只有更新字段和 id 有值
	1. `strictUpdateFill()`，update 字段为 null，所以可以自动填充
	2. `setFieldValByName` 可以正常更新

但是  `strictUpdateFill()` 的 `如果提供的值为null也不填充` 即第二个判断，并没有测试出什么情况下会不更新。
经过测试 `updateName` 字段，无论哪种写法，第二个判断都没有进去的情况下，最终生成的 sql 都有 set `update_name`。

使用 `strictUpdateFill()` 存在一种情况，就是数据库 `updateName` 字段有值，但我们提供的值为 null。
第一种写法，第一个判断过不了，数据库不会更新。也就是不存在自动填充。
第二种写法，第一个判断能过，第二个过不了，但是最终 sql 存在 set `update_name` 为 null。

尝试查看是否有别的配置起作用，无果。
```yaml
mybatis-plus:  
  global-config:  
    db-config:  
      update-strategy: NOT_NULL
```
这个配置当前台没有传入某个属性时，也就是实体属性值为 null 时忽略更新即生成的 sql 中不去 set 这个属性。
但是，按上面的写法， `updateName` 仍会存在与生成的 sql 中，set 的值为 null。


不用过多纠结，因为自动填充就是在有操作时必须填充有意义数据，所以应该给数据默认值，而不是 null。

## 乐观锁
[乐观锁插件 | MyBatis-Plus](https://baomidou.com/pages/0d93c0/)

## 备忘

1. @Mapper 与@MapperScan 不可同时使用
2. @Mapper 用于注解单个 mapper 接口
3. @Mapper Scan 用于批量注解 Mapper 接口
4. @Mapper 不起作用时，有可能是缺少 `mybatis-plus-boot-statter` 包


## reference
1. [mybatis plus使用](https://mp.weixin.qq.com/s?__biz=MzU2MTI4MjI0MQ==&mid=2247492292&idx=3&sn=4c7703a896eba59992bc85aea642adb7)
2. [mybatis plus 看这篇就够了，一发入魂 - 掘金](https://juejin.cn/post/6961721367846715428)
3. [基于Mybatis-plus实现多租户架构｜Java 刷题打卡 - 掘金](https://juejin.cn/post/6966770686652219406)
5. [mybatis-mate 轻松搞定数据权限](https://mp.weixin.qq.com/s/Wkd02LXL0_1ENbZCBz9_gg)
6. [MybatisPlus 使用 saveOrUpdate 详解(慎用)，及问题解决方法！](https://blog.csdn.net/weixin_45369440/article/details/116044771)