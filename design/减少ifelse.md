# 减少ifelse
## 问题：
1. 根据传入的起止时间和类型，生成 中间年份、月份、天
2. 减少按类型判断的 `if else if`


经过编码发现，代码量减少不了，只是换了种形式。比如策略，或者下边
``` java
test.generateByType(time,i -> i == 1, a -> a.plusMonths(1), result);
test.generateByType(time,i -> i == 2, a -> a.plusDays(1), result);

void generateByType(TimeReq timeReq, Predicate<Integer> predicate,Function<LocalDateTime, LocalDateTime> function, List<LocalDateTime> result ) {  
    if (predicate.test(timeReq.getType())) {  
        while (timeReq.getEnd().isAfter(timeReq.getStart())) {  
            result.add(timeReq.getStart());  
            LocalDateTime temp = function.apply(timeReq.getStart());  
            timeReq.setStart(temp);  
        }  
    }  
}

```


##### reference
1. [9条消除if...else的锦囊妙计](https://mp.weixin.qq.com/s/FOeCm-JVNtoUy3mkkYpfXw)
2. [聊聊java中一些减少if else 的编码习惯 - 掘金](https://juejin.cn/post/6844903925506654221)
3. [如何减少代码中的if-else语句,写出优雅的代码, - 掘金](https://juejin.cn/post/6844903950215462919)
4. [Map+函数式接口代替策略模式\_函数式接口+map\_zhongh Jim的博客-CSDN博客](https://blog.csdn.net/qq_44384533/article/details/109197926)