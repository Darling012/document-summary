# 
##### 问题
1. 定长字段与变长字段
	1. [分析MySQL数据类型的长度_12163069的技术博客_51CTO博客](https://blog.51cto.com/u_12173069/1956305)
2. 单精度浮点与双精度浮点
	1. [单精度与双精度是什么意思，有什么区别？ - 知乎](https://www.zhihu.com/question/26022206)
3. 手机号怎么存？用 varchar
	1. [数据库手机号存储格式的思考](https://www.jianshu.com/p/60bb53ea1dfe)
	2. 支持 like 操作
		1. [mysql存储手机号为什么不用bigint? - 腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1888392)
4. dateTime 默认值
	1. 无论采用哪种类型，在开发当中都是采用 orm 框架给日期赋值的方式，不需要数据库自身的默认值。所以 `timestamp` 会[自动更新](MySQL数据类型.md#关于%20timestamp%20的自动更新)的特性就用不到。
5. 如何存数组

## 阿里规范
1. ![|300](Pasted%20image%2020221102215427.png)
2. ![|200*300](Pasted%20image%2020221102215525.png)

## mysql 各个版本特性及新增数据类型

[Mysql 各版本特性 - 掘金](https://juejin.cn/post/6844904110563524615)
MySQL 数据库从5.7.8版本开始，也提供了对 JSON 的支持。 · 可以混合存储结构化数据和非结构化数据，同时拥有关系型数据库和非关系型数据库的优点。

## mysql 数据类型

[MySQL 数据类型 | 菜鸟教程](https://www.runoob.com/mysql/mysql-data-types.html)
int 4 个字节

### 数字类型
[MySQL数字类型如何选择？](https://mp.weixin.qq.com/s?__biz=MzAwMTk4NjM1MA==&mid=2247490306&idx=1&sn=ed9c1d76c276156fb53d6cf5c0a9da67)
1. 销售数目字段为设计无符号类型
	1. 在遇到统计两个月数量差时就会报错，需要改为有符号类型
2. float 和 double 只是保存的近似值，且在未来版本废弃
	1. 采用 decimal（m, n）
	2. 金钱一般采用 INT 整型
3. 主键做自增
	1. 必须采用 bigint
		1. 若采用 int，达到上限后，再次插入会报重复错误
		2. mysql8.0 之前，比如 ID 当前为 3, 删除这条数据，重启数据库再插入还会是 3。
4. 资金类型采用分为单位用整型存贮
	1. decimal 是变长字段
	2. 定长存贮性能更好

##### 整型（`tinyint(m)`、`smallint(m)`、`mediumint(m)`、`int(m)`、`bigint(m)`）
1. unsigned 和 signed
2. `int(m)` m 在 zerofill 的时候才有意义，指定数的宽度，比如 int (5),insert 的时候写的是 9，那么结果是 00009，而且还会自动变成无符号类型。
	1. [Int (4) 和 Int (11) 你选的是哪个？](https://mp.weixin.qq.com/s/0fSekP3DkwG-Gs20vrLpCg )
			1. 在存储空间上没区别，只是让开发人员看见这个字段的设计意图
			2. int 占用 4 个字节

##### 浮点型 (`float(m,d)、double(m,d)`)
1. 存储的是近似值, m 是精度，即保存值得主要位数，d 是标度，即小数点后的位数。位数说的是十进制位数。
2. ~~一个字段定义为 float (6, 3)，如果插入一个数 123.45678, 实际数据库里存的是 123.456，但总个数还以实际为准，即 6 位~~ <sub>不知道从哪看的，感觉是错误举例，或者抄错了</sub>。

##### 定点数（`decimal(m,d)`）
1. 存放的是精确值
2. `decimal(m,d)` 参数 m<65 是总个数，d<30 且 d<m 是小数位

###### 对比
在 MySQL 中，整数和浮点数的定义都是有多种类型，整数根据实际范围定义，浮点数语言指定整体长度和小数长度。浮点数类型包括单精度浮点数（float 型）和双精度浮点数（double 型）。定点数类型就是 decimal 型。定点数以字符串形式存储，因此，其精度比浮点数要高，而且浮点数会出现误差，这是浮点数一直存在的缺陷。如果要对数据的精度要求比较高，还是选择定点数 decimal 比较安全。

[数值计算](数值计算.md)

### 字符串（`char` 、`varchar` 、`text`）

##### char (n）和 varchar（n）
1. （n）是字符数
2. [关于char/varchar(n)中n的探究：字符数or字节数](https://cloud.tencent.com/developer/article/1159062)
3.  [MySQL的几个character_set变量的说明](https://cloud.tencent.com/developer/article/1508503)
	1. `character_set_client、character_set_connection、character_set_results` 与客户端一致，客户端就不会乱码
	2. `character_set_database` 数据库字符集; 
4. ==实际存储大小应该与字符集有关，n 最大值受限于字符集和 [此数据类型存贮限制]== ( https://blog.csdn.net/flyfreelyit/article/details/79700306 )
##### 最大长度
1. 对 `char` 来说，最多存放 255 个字符，和编码无关
2. varchar 可以表示 65535 个字节，需要 2 个字节存放字符长度，所以有效长度是 65532。字符数目跟字符集有关
3. text  字符存储上限为 65535 字节，会用 2 字节记录存储数据大小，但是不同的是 2 字节不占用 text 数据的空间
4. `text` 的类型还有 `TINYTEXT` 256 字节、`MEDIUMTEXT` 16 MB、`LONGTEXT` 4 GB

##### 定长和变长
1. char 的长度是不可变的，不足 n 会补空格，大于会截断（严格模式会拒绝插入）
2. varchar 是变长，大于 n 截断或拒绝，则只会占用字符加上 1 到 2 字节的空间，另外加上一个代表结束的字节，其他的就是所需表示字符
	1. 当N<=255是需要1个字节来表示，当N>255就需要2个字节来表示
3. text 变长

###### 末尾空格处理
1. char(10)，保存字符串”hello “（末尾有一个空格），存到数据库就是’hello ’（有5个空格），然后查询出来只是”hello”（没有空格），占10个字节。
2. varchar (10)，保存字符串”hello “（有一个空格），存到数据库就是”hello “（有 1 个空格），查询出来也是”hello “（有 1 个空格），占 6 个字节。

##### 检索效率
char > varchar > text
1. 因为 char 是固定长度方便程序的存储与查找
1. varchar 和 text
		2. text 类型不能有默认值。
		3. varchar 可直接创建索引，text 创建索引要指定前多少个字符。varchar 查询速度快于 text, 在都创建索引的情况下，text 的索引似乎不起作用。
		4. text 字段删除干净字符后，读取到 Java 中是 `""` 空字符，不是 null, 很奇怪。估计得 `set null` 吧。
2. 
	


[【Mysql】：搞清楚字符串类型char、varchar、text - JoyoHub](https://joyohub.com/mysql/mysql-string/)
[MySQL的各种数据类型 - wenxuehai - 博客园](https://www.cnblogs.com/wenxuehai/p/15600562.html)
### 日期时间（`date`、`time`、`year`、`datetime`、`timestamp`）

##### 日期选择
1. mysql 8  [查询效率](https://juejin.cn/post/6844903701094596615)：bigint > timestamp > datetime
2. 日期类型选择 ~~timestamp（4 个字节）~~<sub>增加负担</sub>, datetime (8 字节)
3. 注意存储范围
4. int, 占 4 个字节，需要函数 FROM_UNIXTIME () 和 UNIX_TIMESTAMP ()在 int 和 datetime 间转换
5. 阿里开发手册采用 `dateTime` 类型


[MySQL中timestamp数据类型的特点](https://www.imooc.com/article/16158)

[数据库的几种日期时间类型](https://mp.weixin.qq.com/s?__biz=MzAxODcyNjEzNQ==&mid=2247492479&idx=2&sn=ed2f3312d37d02d3e3e14f50993a9fa8)
1. 字符串
	1. 不建议采用，比较、处理、范围都是麻烦
2. dateTime
	1. yyyy-MM-dd HH:mm:ss
	2. 时间范围是“1000-00-00 00:00:00”到“9999-12-31 23:59:59”
	3. 只存储了本地时间，不包含时区
3. timestamp
	1. yyyy-MM-dd HH:mm:ss
	2. 存储范围 `1970-01-01 00:00:01` to `2038-01-19 03:14:07`
	3. 存储了时区，但要看数据库具体实现，是否能够自动转换时间，mysql 支持自动转。
4. unix timestamp
	1. “绝对时间”：从一个基准时间（1970-1-1 00:00:00 +0:00）到现在的秒数，用一个整数表示
	2. `System.currentTimeMillis();` Java 8 `Instant.now().toEpochMilli()`
	3. 将表示绝对时间的时间戳通过 Long 类型或 float 类型保存到数据库中，对应数据库类型为 Bigint 或 float
	4. 当不同时区使用时直接格式化成对应的字符串，mysql 提供了丰富的函数，可以进行转换
5. 阿里开发手册采用 `dateTime` 类型

##### 关于默认值
1. 代码或者 orm 框架赋值
2. 当前时间
3. null
4. `0000-00-00 00:00:00` 等一眼假的默认值

 [MySQL 日期类型及默认设置_gxy_2016的博客-CSDN博客_mysql datetime 默认值](https://blog.csdn.net/gxy_2016/article/details/53436865)
 [MySQL 8 创建表时 datetime 和 timestamp 的默认值 - 简单教程，简单编程](https://www.twle.cn/t/19251)
1. `datetime`
	1. mysql 8 之前， 默认值可以设置为 `0000-00-00 00:00:00`
	2. mysql 8 中，范围是 `1000-01-01 00:00:00` 到 `9999-12-31 23:59:59` 之间
		1. 经测试 `0000-01-01 00:00:00` 也可以
		2. [MySQL关于日期为零值的处理-腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1643993)
2. `timestamp`
	1. mysql 8 中范围是 `1970-01-01 00:00:01` UTC 到 `2038-01-19 03:14:07` UTC
	2. 在中国，我们一般会设置为东 8 区，那么，默认的时间范围就是 `1970-01-01 08:00:01` 到 `2038-01-19 03:14:07`。
3. `timestamp` 本质是int，`datetime` 是字符串，所以 `timestamp` 从空间和效率上来说要比 `datetime` 强

###### 没有值怎么表示
1. null
2. `0000-00-00 00:00:00` 等一眼假的默认值
	1. 但是有可能做不到统一

##### 关于自动更新
在 navicate 中可以为 `datetime`、`timestamp` 勾选根据 `当前时间更新`，当某字段更新，数据的所有这俩类型时间字段都会更新。不勾选则不会自动更新。
1. 从 MySQL 5.6.5开始，TIMESTAMP 和 DATETIME 列都支持自动更新，且一个表可设置多个自动更新列。
2. 在 MySQL 5.6.5 之前:
	1. 只有 TIMESTAMP 支持自动更新
	2. [第一个timestamp 字段](https://blog.51cto.com/u_14349334/3479965) ，<font color= #C32E94 >若其他字段改变则此字段值更新</font>，感觉增加了负担，不采用。
		1. 还被限定住了第一个 `timestamp` 字段，也就是一般案例是其他时间字段是 `datetime`，`updateTime` 字段采用，还要配置 `DEFAULT CURRENT_TIMESTAMP`，不合适。

[MySQL自动更新列时间戳ON UPDATE CURRENT\_TIMESTAMP\_default current\_timestamp(6)\_王大雄\_的博客-CSDN博客](https://blog.csdn.net/zgdwxp/article/details/103168552)
[Mysql设置时间字段格式是用TIMESTAMP，DATETIME还是INT？ - 问答 - 腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/ask/24753)

[Mysql修改时间的年月日，时分秒不变的做法](https://blog.csdn.net/qq_26599807/article/details/51151262)

### 数组
#### json
[How to store array data type in MySQL](https://sebhastian.com/mysql-array/)


## 数据类型的属性
![](Pasted%20image%2020221214102220.png)

##### mysql 关于 null

mysql 中的 `Null` 和 `""`，在读取到 Java 中后，假如有后续操作， `Null` 就会触发空指针。
mysql 5.7 默认启用了严格模式，BLOB、TEXT、GEOMETRY、JSON 不能有默认值，也就是默认为 `Null`。可以通过关闭严格模式设置默认值。

###### 注意点
1. sum 没统计到任何记录时，返回的是 null 而不是 0，可通过 ifnull 函数把 null 转为 0
2. count 函数不统计 null 值， `count (*)` 会
3. sql 中 =null  不是判断而是赋值，只能使用 is null， is not null

推荐把所有列设为 not null。但数据类型呢？假如不为 null 则会出现默认 0，就会产生歧义。

1. [为什么建议MySQL列属性尽量用 NOT NULL ？](https://mp.weixin.qq.com/s?__biz=MzU0MzQ5MDA0Mw==&mid=2247486545&idx=1&sn=5a554d422c693956777552e551c51a5f)
2. [mysql中字段究竟该不该为null](https://leokongwq.github.io/2019/01/05/mysql-null-column.html)
5. [MySQL 不推荐默认值为 null 的 N 个坑！](https://mp.weixin.qq.com/s/2EBNw6T2UcAb6kbdt9kR4w)
6. [MySQL为Null会导致5个问题](https://mp.weixin.qq.com/s/pPc0Mlg7wApxgBZ9J7zfAA)

## reference

[一个 MySQL 隐式转换的坑](https://mp.weixin.qq.com/s/dqp0eEoAd1L8pGPxPp3-1g)


