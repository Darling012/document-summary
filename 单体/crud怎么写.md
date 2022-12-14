# crud怎么写
##### 1. 查询
敏感信息处理，字典转换
###### 1. 分页查询
1. pojo中属性，哪个有值查哪个。注意时间段查询边界问题。
###### 2. 单个查询
##### 2.删除
级联删除业务问题
###### 1. 批量删除
###### 2. 单个删除
##### 3.新增
新增只需要保证不可重复的属性数据库中未存在即可。
1. 业务判断 [数据业务逻辑校验](数据业务逻辑校验.md)
   1. 不能跟表中数据重复
```Bash
carService.lambdaQuery()
        .eq(Car::getCarNum, carVO.getCarNum())
        .select(Car::getId)
        .oneOpt()
        .ifPresent(cars->{throw new BizException("车牌号已存在,请重新绑定");});
1. param中属性需要关联已有数据
BizUserBase bizUserBase = bizUserBaseService.lambdaQuery()
        .eq(BizUserBase::getId, bizUser.getBaseUserId())
        .select(BizUserBase::getPhone, BizUserBase::getSex,BizUserBase::getIdCard).oneOpt()
        .orElseThrow(()->  new BizException("不存在相关用户基础信息"));
```
1. copyProperties
2. save
##### 4. 修改
修改需要保证业务上具有唯一性的字段数据库中唯一，这就把前台修改分为两种：
1. 一种是非唯一字段修改
2. 一种唯一字段修改
我们不知道前台是哪种修改，但我们要保证数据库中唯一性字段的不重复。具体讨论为：
1. 用唯一字段去数据库查，没有查到数据。说明当前pojo修改了唯一字段的数据，且没有重复，允许修改。（唯一性字段相等没有数据）
2. 用唯一字段去数据库查，查到数据了，此时就要判断ID是否为它本身：
   1. 第一种是，ID相等，查出的来是它本身，情况为当前pojo没有修改唯一字段，修改了其他字段，允许修改；（唯一性字段相等，且ID相等）
   2. 第二种是，ID不相等，查出来的不是它本身，则当前pojo的唯一性字段为修改后的值且与数据库重复。不允许修改。（唯一性字段相等，且ID不相等）
**总结为**：若存在唯一性字段相等且ID不相等的数据，则重复。
1. 业务判断，某业务上具有唯一性属性修改后的数据不能跟已有数据重复
```Bash
carService.lambdaQuery().eq(Car::getCarNum, carVO.getCarNum()).select(Car::getId).oneOpt().ifPresent(cars->{
    if (!Objects.equals(cars.getId(), carVO.getId())){
       throw new BizException("车牌号已存在,本次绑定无效");
    }
});
```
1. copyProperties
2. save
##### reference
1. [SQL语句关于添加编辑等检验重名的业务实现（Java后台）](https://blessing.blog.csdn.net/article/details/109631779)
2. [新增、修改判断数据重复问题](https://blog.csdn.net/qq_45893748/article/details/118668620)
3. [新增/修改数据时校验字段唯一性](https://blog.csdn.net/Samurai_L/article/details/102859598)
4. [【测试用例】之增删改查](https://www.jianshu.com/p/a187bffcfe1c)
mybatisplus 的 ne  简化




