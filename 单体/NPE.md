# NPE
发生NPE的情况：
1. **在空对象上调用实例方法**。对空对象调用静态方法或类方法时，不会报 NPE，因为静态方法不需要实例来调用任何方法；
2. **访问或更改空对象上的任何变量或字段时**；
3. **抛出异常时抛出 null**；
4. **数组为 null 时，访问数组长度**；
5. **数组为 null 时，访问或更改数组的插槽**；
6. 对空对象进行同步或在同步块内使用 null。
7. Integer 等包装类，自动拆箱时出现
8. 字符串比较
9. 如 ConcurrentHashMap 这种不支持 key、value 为 null 的容器强行 put null。
10. A对象包含B对象，通过A对象的字段获得B对象后，没有判空B就调用B的方法
11. 方法或其它服务返回的List不是空而是null，没有进行判空就直接调用List的方法
处理：
1. 两个可能为 null 的字符串用 `Objects.equals()` `
2. 判空、optinal 处理掉空指针可能会隐藏更深的 bug，要多想为什么会可能为 null。


1. [java匠人手法-优雅的处理空值 | 西格玛的博客](https://lrwinx.github.io/2018/08/30/java%E5%8C%A0%E4%BA%BA%E6%89%8B%E6%B3%95-%E4%BC%98%E9%9B%85%E7%9A%84%E5%A4%84%E7%90%86%E7%A9%BA%E5%80%BC/)
2. [关于Java空指针异常的几种情况的总结（java.lang.NullPointerException）_Seachal的博客-CSDN博客_java空指针异常](https://blog.csdn.net/Zhangxichao100/article/details/73124228)
3. [避开NullPointerException的10条建议](https://mp.weixin.qq.com/s?__biz=MzAwNjkxNzgxNg==&mid=2247486010&idx=1&sn=6a45b35f536a1d42453988c103dd112d)
4. [一千个不用 Null 的理由 - leejun2005的个人页面 - OSCHINA - 中文开源技术交流社区](https://my.oschina.net/leejun2005/blog/1342985)
5. [阿里华为等大厂架构师如何解决空指针问题_JavaEdge.的博客-CSDN博客](https://blog.csdn.net/qq_33589510/article/details/110874849)
6. 