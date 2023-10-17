![结构型-代理模式](结构型-代理模式.md)

[Java 动态代理的两种方式及其优缺点](https://mp.weixin.qq.com/s/DFCVxpsUVd0iI4Md6Oxplw)


## java 静态代理
[Java静态代理实现原理](https://mp.weixin.qq.com/s?__biz=MzUzMTA2NTU2Ng==&mid=2247487368&idx=1&sn=408c385d26083803e1a2a742bd301531)
类加载期通过字节码转换，即 LTW (load time waving), 编译器增强，静态代理。
java. lang. instrument 通过添加 jvm 启动参数 -javaagent，对类字节码进行转换。
## Java 动态代理
动态代理的实现主要由一个类和一个接口组成，即 java.lang.reflect.Proxy 类和 java.lang.reflect.InvocationHandler 接口。
###### jdk 动态代理原理
2. [JDK动态代理实现原理 - Rejoy - ITeye博客](https://www.iteye.com/blog/rejoy-1627405)
3. [【Spring基础】JDK动态代理实现原理(jdk8)_街灯下的小草的博客-CSDN博客](https://blog.csdn.net/yhl_jxy/article/details/80586785)
4. [有点深度的聊聊JDK动态代理 - CodeBear - 博客园](https://www.cnblogs.com/CodeBear/p/10245442.html)

# reference
[静态代理、动态代理、Hook](https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650246424&idx=1&sn=a1710ce31a9f134ba00f9acaa195c794)
提供了一种对目标对象的访问方式。
##### 为什么使用
1. 中间隔离
	1. 某些情况下，客户端不想直接引用某个对象，代理类可以在客户端呢目标对象之间起到中介作用
2. 开闭原则，扩展功能
	1. 可以对目标类进行功能增强，只需要修改代理类。
##### 代理实现方式
1. 静态代理
	1. 编译器实现，在程序运行之前，代理类. class 文件就已经被创建。
2. 动态代理
	1. 在程序运行时通过反射机制动态创建代理对象。
3. ![](Pasted%20image%2020221027103259.png)
	1. 这个图也说明，Java 实现静态代理和动态代理都是基于接口
##### 代理实现原理
[java - Java三种代理模式：静态代理、动态代理和cglib代理_个人文章 - SegmentFault 思否](https://segmentfault.com/a/1190000011291179)
##### 代理模式
设置一个中间代理来提供目标对象的额外访问方式，以达到增强原对象的功能。
##### 静态代理
1. 代理类和目标类实现同一个接口
2. 优点：可以在不修改目标对象的前提下扩展目标对象的功能。
3. 缺点：
	1. 冗余，代理类要和目标类实现同一个接口，会产生过多的代理类
	2. 不易维护，接口改，代理类和，目标类都要改
4. 
##### 动态代理
1. 运行时动态生成字节码文件加载到 jvm 中。?？ 反射算动态生成吗？
2. 目标类必须实现接口
3. 通过反射实现



#####  cglib
1. 可以在运行期扩展 Java 类与实现 Java 接口。
2. `CGLIB（Code Generation Library）`是一个基于ASM的字节码生成库
3. 在运行时对字节码进行修改和动态生成
4. 相比于 jvm 动态代理，目标类无需实现接口
5. 需要继承对象，重写方法，所以目标对象不能是 final 类。


