# Maven

## 多模块打包 
##### 问题：
1. 统一版本号属性传递，层级大于 2 的情况
   1. maven3.5 以后子 pom 的 parent 标签可以使用父 pom 中 properties 属性。但子项目的 install/deploy 不会替换占位符，需引入 flatten-maven-plugin 插件。
      1. [Properties in parent definition are prohibited](https://chenyongjun.vip/articles/98)
      2. [flatten-maven-plugin ： 处理版本占位符](https://blog.csdn.net/sayyy/article/details/103994302)
      3. [maven 版本管理与 flatten-maven-plugin](https://zhuanlan.zhihu.com/p/270574226)
      4. [](https://stackoverflow.com/questions/45598007/properties-in-parent-definition-are-prohibited-in-the-intellij-maven-on-my-mac-o)
      5. [Maven多模块结构下版本号管理的正确姿势](https://juejin.cn/post/6946138904802099231)
   2. 单体仓库模式，所有模块代码都在一起，也不用更改版本号，都写死也可接受
2. 单模块构建
   1. -am 参数
3. reactor 机制与构建参数同时存在时的构建情况
   1. 聚合、继承项目结构分为两种情况
      1. 构建当前模块下所有子模块，即在父 pom 执行 mvn clean package， 那么会构建父 pom 及协调顺序后各个子 pom
      2. 构建当前模块下指定子模块 mvn -pl moduleA -am

#### 前置知识：
1. maven 的聚合与继承
2. maven reactor 机制
3. maven 单模块构建参数、向上构建参数、向下构 cd 建参数

### 源码组织模式

#### 单体仓库还是多仓库？

1. [单体仓库与多仓库——两种源码组织模式介绍](https://jiapan.me/2020/multi-repo-vs-mono-repo/)
2. 业务上有无必要区分 AI 项目？
   1. 区别在细枝末节，费力重构区分还不如直接分开。
   2. 公共模块也较为个性化，如接入原有用户体系，原有体系厂家各异。

#### maven 多模块项目结构

```CSS
│  │
│  ├─project(pom)
│  │  │
│  │  └─moduleA(pom)
│  │  │     │——clentA(jar)
│  │  │     └─ serviceA(war)
│  │  │
│  │  │
│  │  │
│  │  └─moduleB(pom)
│  │  │     │——clentB(jar)
│  │  │     └─ serviceB(war)
│  │  └─clientC(jar)
│  │  └─serviceC(war)

```

1. 整体构建
2. 单模块单 jar

##### 情况
1. 若子模块之间没有依赖关系，则构建顺序为父模块，子模块依次按 mdules 中定义顺序
2. 构建 serviceA
   1. 在 serviceA 只依赖 clientA 情况下
      1. 在顶级目录 project 执行： 
         1. mvn -pl moduleA/serviceA -am package   **可以** 会构建 moduleA、clientA、serviceA
      2. 在 moduleA 执行：
         1. mvn package  **可以**   会构建 moduleA、clientA、serviceA
         2. mvn -pl serviceA -am package   **可以** 会构建 moduleA、clientA、serviceA
   2. 在 serviceA 依赖 clientA、clientB、clientC 情况下
      1. 在顶级目录 project 执行： mvn -pl moduleA/serviceA -am package   **可以** 会构建 project 、clientC、 moduleA、 clientA 、moduleB 、clientB、serviceA
      2. 在 moduleA 执行：
         1. mvn package  **不可以** 只会尝试构建 moduleA、clientA、serviceA，因 serviceA 找不到 pom 中首先引入的 clientB 而失败。
         2. mvn -pl serviceA -am package   不可以 同上
3. 构建 moduleA
   1. mvn package  即情况 2 在 moduleA 执行情况，取决于子模块是否引入 moduleA 外模块
   2. mvn -pl moduleA -am package 只会构建 moduleA，不会构建子模块
4. 构建整体  顶级目录 project mvn package 
5. 总结：
   1. 若构建顶级父项目 project，则 mvn package 就可以
   2. 若构建中间层 moduleA 项目，要么保证 serviceA 等子模块依赖的 moduleA 外部模块都在仓库，然后执行 mvn package；要么执行 moduleA 下每个子项目。
   3. 若构建底层子模块 serviceA, 则在顶级 pom 文件夹下执行 mvn -pl moduleA/serviceA -am package

6. **总总结**：无论是依赖 maven 自身 reactor 还是手动加参，成功的前提都是要能找到所依赖的其他模块，所以最佳实践应该是在顶级目录执行，拼接执行命令中的路径

##### reference
1. [Maven编译指定(跳过)Module - batch-norm - 博客园](https://www.cnblogs.com/yqyang/p/11328139.html)
2. [Maven提高篇系列之（一）——多模块 vs 继承 - 无知者云 - 博客园](https://www.cnblogs.com/davenkin/p/advanced-maven-multi-module-vs-inheritance.html)
3. [Maven--反应堆(Reactor) - MicroCat - 博客园](https://www.cnblogs.com/microcat/p/7243074.html)
4. [Maven 反应堆、按需构建多模块，玩转Maven反应堆（反应堆的构建顺序，裁剪反应堆](https://www.javatt.com/p/89888)
5. [Maven – Guide to Working with Multiple Modules](https://maven.apache.org/guides/mini/guide-multiple-modules.html)

#### 参数

```text
-am --also-make 同时构建所列模块的 依赖的 模块；
-amd -also-make-dependents 同时构建 依赖于 所列模块的模块；
-pl --projects <arg> 构建制定的模块，模块间用逗号分隔；
-rf -resume-from <arg> 从指定的模块恢复反应堆。
mvn clean  -pl edge -am 
```


[Maven常用参数说明 - 简书](https://www.jianshu.com/p/25aff2bf6e56)

| 缩写 | 全名                            | 说明                                                         |
| ---- | ------------------------------- | ------------------------------------------------------------ |
| -h   | --help                          | 显示帮助信息                                                 |
| -am  | --also-make                     | 构建指定模块, 同时构建指定模块依赖的其他模块                  |
| -amd | --also-make-dependents          | 构建指定模块, 同时构建依赖于指定模块的其他模块                |
| -B   | --batch-mode                    | 以批处理 (batch) 模式运行                                      |
| -C   | --strict-checksums              | 检查不通过, 则构建失败 严格检查                             |
| -c   | --lax-checksums                 | 检查不通过, 则警告 宽松检查                                |
| -D   | --define `<arg>`                 | 定义系统变量                                      |
| -e   | --errors                        | 显示详细错误信息                                             |
| -emp | --encrypt-master-password `<arg>` | 加密主安全密码，用于用户访问管理等                           |
| -ep  | --encrypt-password `<arg>`        | 加密服务器密码                                               |
| -f   | --file `<arg>`                   | 使用指定的 POM 文件替换当前 POM 文件                             |
| -fae | --fail-at-end                   | 最后失败模式：Maven 会在构建最后失败（停止）。如果 Maven refactor 中一个失败了，Maven 会继续构建其它项目，并在构建最后报告失败。 |
| -ff  | --fail-fast                     | 最快失败模式： 多模块构建时, 遇到第一个失败的构建时停止。     |
| -fn  | --fail-never                    | 从不失败模式：Maven 从来不会为一个失败停止，也不会报告失败。  |
| -gs  | --global-settings  `<arg>`        | 替换全局级别 settings.xml 文件                                 |
| -l   | --log-file `<arg>`                | 指定输出日志文件                                             |
| -N   | --non-recursive                 | 仅构建当前模块，而不构建子模块 (即关闭 Reactor 功能)            |
| -nsu | --no-snapshot-updates           | 强制不更新 SNAPSHOT                                           |
| -U   | --update-snapshots              | 强制更新 releases、snapshots 类型的插件或依赖库 (否则 maven 一天只会更新一次 snapshot 依赖) |
| -o   | --offline                       | 运行 offline 模式, 不联网进行依赖更新                           |
| -P   | --activate-profiles  `<arg>`       | 激活指定的 profile 文件列表 (用逗号[,]隔开)                     |
| -pl  | --projects  `<arg>`                | 手动选择需要构建的项目, 项目间以逗号分隔                      |
| -q   | --quiet                         | 安静模式, 只输出 ERROR                                         |
| -rf  | --resume-from `<arg>`             | 从指定的项目 (或模块) 开始继续构建                             |
| -s   | --settings `<arg>`                | 替换用户级别 settings.xml 文件                                 |
| -T   | --threads `<arg>`                 | 线程计数，例如 2.0c，其中 c 是核心数，两者相乘即为总线程数      |
| -t   | --toolchains `<arg>`              | 指定用户的 toolchains 文件路径                                 |
| -V   | --show-version                  | 显示版本信息而不停止构建                                     |
| -v   | --version                       | 显示版本信息                                                 |
| -X   | --debug                         | Debug 模式，输出详细日志信息                                  |
| -cpu | --check-plugin-updates          | 【废弃】, 仅为了向后兼容                                      |
| -npr | --no-plugin-registry            | 【废弃】, 仅为了向后兼容                                      |
| -npu | --no-plugin-updates             | 【废弃】, 仅为了向后兼容                                      |
| -up  | --update-plugins                | 【废弃】, 仅为了向后兼容                                      |



## POM 配置
##### dependencies 和 dependencyManagement
1. 所有声明在 dependencies 里的依赖都会自动引入，并默认被所有的子项目继承。
2. dependencyManagement 里只是声明依赖，并不实现引入，因此子项目需要显式的声明需要用的依赖。如果不在子项目中声明依赖，是不会从父项目中继承下来的；只有在子项目中写了该依赖项，并且没有指定具体版本，才会从父项目中继承该项，并且 version 和 scope 都读取自父 pom; 另外如果子项目中指定了版本号，那么会使用子项目中指定的 jar 版本。

##### scope 分类
1. compile
	1. 默认值，适用于所有阶段（开发、测试、部署、运行），本jar会一直存在所有阶段。
	2. 运行期有效，需要打入包中
2. test
	1.  `<scope>test</scope>` 只能在 maven test 文件夹下引到
	2. 用于编译和运行测试代码，不会打入包中
3. provided
	1. 编译有效，运行期不需要提供，不会打入包中
	2. 只在开发、测试阶段使用，目的是不让Servlet容器和你本地仓库的jar包冲突 。如servlet.jar。
4. runtime
	1. 编译不需要， 运行期有效，需要打入包中
	2. 只在运行时使用，如JDBC驱动，适用运行和测试阶段。
5. system
	1. 本地读取
	2. [Maven 实战问题和最佳实践](https://github.com/dunwu/java-tutorial/blob/master/docs/11.%E8%BD%AF%E4%BB%B6/01.%E6%9E%84%E5%BB%BA/01.Maven/04.Maven%E5%AE%9E%E6%88%98%E9%97%AE%E9%A2%98%E5%92%8C%E6%9C%80%E4%BD%B3%E5%AE%9E%E8%B7%B5.md)

## settings 文件配置
##### 仓库与镜像
[Maven 多仓库和镜像配置 - 掘金](https://juejin.cn/post/6993190688846266375)
1. 如果在 `profiles` 中指定 `repository` 存储库，需要激活该特定 `profiles`, 我们通过在 `activeProfiles` 中进行配置
2. 镜像**拦截 maven 对远程存储库的请求**，将请求中的远程存储库地址重定向到镜像中配置的地址
3. mirrors 与 profiles 设置 repository 的区别
	1. 假如配置同一个 repository 多个 mirror 时，相互之间是备份关系，只有当仓库连不上时才会切换到另一个，而如果能连上但是找不到依赖时是不会尝试下一个 mirror 地址的

## 常见问题
1. 本地仓库有 jar 却去远程库拉取
	1. [记 Maven 本地仓库埋坑之依赖包为何不能用 ](https://www.cnblogs.com/dasusu/p/11825786.html)
	2. [Maven 本地仓库明明有对应的jar包但还是报找不到](https://blog.csdn.net/IT_0008/article/details/107761728)
	3. [Maven本地仓库有jar但是却不能找到依赖_](https://blog.csdn.net/weixin_44817761/article/details/118499606)
2. idea 相关命令
	1. 清除idea系统设置:mvn idea:clean  
	2. 生成 idea 项目：mvn idea:idea
	3. 离线打包: mvn package -o
3. idea 编译和 maven 编译区别
	1. [IDEA面板Build Project和maven compile、package、install、deploy的区别_小小野猪的博客-CSDN博客_build project](https://blog.csdn.net/gongjin28_csdn/article/details/121544078)
4. [Maven中optional和scope元素的使用场景，你弄明白了？ - 程序新视界](https://www.choupangxia.com/2020/12/01/maven-optional-scope/)
5. 引入本地jar




[Maven](https://arch-long.cn/articles/maven/Maven.html)
-   **mvn compile:** 会在项目根目录生成 `target` 文件夹，这个命令并不生成 jar 包！`target` 包含 `classes` 和 `maven-status` 两个文件夹，其中 `classes` 存有编译好的 `.class` 文件和 `src/main/resources` 中的资源文件，如: `log4j.properties`、`hdfs-site.xml` … 注意如果资源文件夹配置在 `src/main` 目录之外的话，需要在 `pom.xml` 文件指定 `resource` 目录才可以! `maven-status` 存有 maven plugins 的信息。
-   **mvn clean:** 将根目录下的 `target` 文件夹删除
-   **mvn package:** 在 `target` 文件夹中生成 `jar` 文件和 `maven-archiver` 文件夹 (存放 `pom.properties`)
-   **mvn install:** 会将 `target` 中的 `jar` 包安装到 `local maven repository` 中，就是 `.m2/repository` 中。这样，本地其他项目就可以通过配置 `<dependency>` 使用这个这个项目
-   **mvn deploy:** 将 `target` 中的 `jar` 包上传到 `remote server(nexus)` 的 `maven repository` (私服)，使得其他连接到这个远程库的开发者或者工程可以使用/