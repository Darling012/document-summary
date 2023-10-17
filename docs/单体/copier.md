##### 问题
1. [`bean` 映射有几种方式](copier.md#Bean%20映射的几种方式)
2. 与 [Java clone](值传递.md#Java%20clone) 关系
3. 创建对象有哪几种方式
	1. [Java clone](值传递.md#Java%20clone)和 `copier` 与此有何关系
1. [主流对象复制框架使用与比较](https://mp.weixin.qq.com/s/k_Ogz0qyHZ45nPbGgV2APw)
4. [对象拷贝 - 优雅的解决方案 Mapstruct - 掘金](https://juejin.cn/post/6943036004571807775)
5. [Java对象深拷贝浅拷贝总结 - 远方789 - 博客园](https://www.cnblogs.com/chenfangzhi/p/11910064.html)

##### 需要解决
1. 同名同类型属性自动赋值
2. 同名不同类型赋值
	1. 基本类型与包装类型
	2. `boolean <-> integer`，`0,1->false,true`
	3. `integer <-> String`，字典项
	4. `date <-> String`，日期
3. 不同名同类型赋值
	1. `userEntity.name->xxxDTO.userName`
4. 嵌套对象
5. 集合

## copier

##### copy 工具应该具备的特性
1. 深 clone 浅 clone
2. 同名不同类型
3. 字段名不同支持自定义映射
4. 嵌套对象
5. 集合

##### 创建对象的几种形式
[Java中创建对象的5种方式，你都知道几种？【享学Java】 - 腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1497720)
1.  new关键字
2.  Class.newInstance
3.  Constructor.newInstance
4.   [Java clone](值传递.md#Java%20clone)方法
5.  反序列化

##### Bean 映射的几种方式
1. 开发过程中硬编码
	1. idea 插件生成代码 getset
	2. [Simple Object Copy，一款idea插件帮你优雅转化DTO、VO、BO、PO、DO - 掘金](https://juejin.cn/post/7053264631262871583)
2. 编译期生成
	1. [mapStruct](copier.md#mapStruct)
3. 运行期
	1. 反射
	2. 字节码

##### Bean 映射的几种工具
[七种对象复制工具类](https://mp.weixin.qq.com/s/rA0o7vYbVoKXc4ySbBTh2Q)
1. Apache BeanUtils
	1. 不支持 `String` 转为 `Date` 类型，可通过注入 `Converter` 转换类解决
	2. 字段名不同不能赋值
	3. 类型不一致将会进行默认转化，如 `Integer ->String`, 18->"18"
	4. 嵌套对象为浅拷贝
	5. 源码使用反射做了过多校验，效率低
	6. Apache
2. Spring BeanUtils
	1. 字段名不一致，无法赋值
	2. 类型不一致，无法赋值（基本类型与包装类型可自动转化，`Integer ->String` 不行）
	3. 嵌套对象为
	4. 重载方法 `copyProperties(Object source, Object target, String... ignoreProperties)`
	5. 基于反射实现，使用了缓存，没有过多校验，效率比 Apache 高
3. Cglib BeanCopier
	1. 字段名不一致，无法赋值
	2. 类型不一致，无法赋值（基本类型与包装类型 不能  自动转化）
	3. 嵌套对象为浅拷贝
	4. 字节码技术动态生成一个代理类，代理类实现get 和 set方法
4. Dozer
	1. 支持以上场景，除了不支持 `String` 转为 `Date` 类型
	2. 支持枚举，map 集合属性复制
	3. 深拷贝
	4. 反射
5. orika
	1. 支持字段名不同映射
	2. 支持集合映射
	3. 深拷贝
	4. 底层其使用了 javassist 生成字段属性的映射的字节码，然后直接动态加载执行字节码文件
	5. [Orika对象复制教程 - 简书](https://www.jianshu.com/p/e65aad9010e4)
6. MapStruct
	1. 深拷贝
	2. [GitHub - mapstruct/mapstruct-examples: Examples for using MapStruct](https://github.com/mapstruct/mapstruct-examples)

 [5种常见Bean映射工具的性能比对 - 掘金](https://juejin.cn/post/6844903997283762183#heading-9)
[这四种对象属性拷贝方式，你都知道吗？ - 风尘博客 - 博客园](https://www.cnblogs.com/VanFan/p/12757992.html)

## mapStruct
利用 Java 编译期的  annotation processor 机制 生成代码，最终调用的是 `setter` 和 `getter` 方法。生成代码的 `mapstruct-processor` 可以通过 maven 依赖或者插件。

##### 例子
1. [官方示例](https://github.com/mapstruct/mapstruct-examples)
2. [几个简单]( http://www.52xingchen.cn/detail/86 )
3. [较全](https://www.cnblogs.com/DDgougou/p/13362788.html)

##### 可以自动映射的类型
1. 基本类型及其他们对应的包装类型。
	1. 对于包装类是自动拆箱封箱操作的，并且是线程安全的。
	2. 怎么保证的？注入spring后是单例，怎么保证线程安全？
		1. 生成的对象是局部变量，各个线程都自有。
2. 基本类型的包装类型和 String 类型之间
3. String 类型和枚举类型之间
	1. 注意下面代码枚举取得的是枚举值 `MEN`，而不是后边的 key value；
生成的代码
``` java 
public UserRoleDto toUserRoleDto(User user) {  
    if ( user == null ) {  
        return null;  
    }  
  
    UserRoleDto userRoleDto = new UserRoleDto();  
  
    if ( user.getId() != null ) {  
        userRoleDto.setUserId( String.valueOf( user.getId() ) );  
    }  
    userRoleDto.setUserName( user.getName() );  
    userRoleDto.setRoleName( userRoleRoleName( user ) );  
    if ( user.getAge() != null ) {  
        userRoleDto.setAge( user.getAge() );  
    }  
    if ( user.getGender() != null ) {  
        userRoleDto.setGender( user.getGender().name() );  
    }  
  
    return userRoleDto;  
}
```

##### 类型不一致映射
使用 Java8 后接口提供的默认方法，抽象类使用非抽象方法
[数值格式化，集合格式化，导入外部类格式化](https://segmentfault.com/a/1190000020663215)

[@MappingTarget、@InheritInverseConfiguration、@Mapper( uses = { BooleanStrFormat.class})](https://blog.csdn.net/qq122516902/article/details/87259752)




#### 集成到 spring convert
[MapStruct Spring Extensions 0.1.2 Reference Guide](https://mapstruct.org/documentation/spring-extensions/reference/html/)

经测试与文档 生成的 ConversionServiceAdapter 不同，本地未有 转换代码。
以及自定义 conversionService 名称没起作用

需要配置 `maven-compiler-plugin`，加了此插件后，lombok 注解编译也需要配置，猜测是不再使用 springboot2.0默认提供

感觉并没有简化什么


####  原理
9. [Mapstruct源码解析- 框架实现原理 - 掘金](https://juejin.cn/post/6844904199755415559)
10. [mapstruct原理 - 余生请多指教ANT - 博客园](https://www.cnblogs.com/wangbiaohistory/p/15848203.html)
11. [MapStruct学习_古今三月晨的博客-CSDN博客](https://blog.csdn.net/xiaodao0706/article/details/109032867)


[IDEA MapStruct Support 插件]( https://mp.weixin.qq.com/s?__biz=MzUzMTA2NTU2Ng==&mid=2247489616&idx=2&sn=66a96d19fd07895f5e850cbfc1c2bedc ) 




