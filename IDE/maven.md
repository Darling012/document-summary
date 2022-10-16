#### 问题：

1. 统一版本号属性传递，层级大于2的情况
   1. maven3.5以后子pom的parent标签可以使用父pom中properties属性。但子项目的install/deploy不会替换占位符，需引入 flatten-maven-plugin插件。
      1. [Properties in parent definition are prohibited](https://chenyongjun.vip/articles/98)
      2. [flatten-maven-plugin ： 处理版本占位符](https://blog.csdn.net/sayyy/article/details/103994302)
      3. [maven 版本管理与 flatten-maven-plugin](https://zhuanlan.zhihu.com/p/270574226)
      4. [](https://stackoverflow.com/questions/45598007/properties-in-parent-definition-are-prohibited-in-the-intellij-maven-on-my-mac-o)
      5. [Maven多模块结构下版本号管理的正确姿势](https://juejin.cn/post/6946138904802099231)
   2. 单体仓库模式，所有模块代码都在一起，也不用更改版本号，都写死也可接受
2. 单模块构建
   1. -am 参数
3. reactor机制与构建参数同时存在时的构建情况
   1. 聚合、继承项目结构分为两种情况
      1. 构建当前模块下所有子模块，即在父pom执行mvn clean package， 那么会构建父pom及协调顺序后各个子pom
      2. 构建当前模块下指定子模块mvn -pl moduleA -am

#### 前置知识：

1. maven的聚合与继承
2. maven reactor机制
3. maven单模块构建参数、向上构建参数、向下构cd 建参数

### 源码组织模式

#### 单体仓库还是多仓库？

1. [单体仓库与多仓库——两种源码组织模式介绍](https://jiapan.me/2020/multi-repo-vs-mono-repo/)
2. 业务上有无必要区分AI项目？
   1. 区别在细枝末节，费力重构区分还不如直接分开。
   2. 公共模块也较为个性化，如接入原有用户体系，原有体系厂家各异。

#### maven多模块项目结构

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
2. 单模块单jar

### 情况：

1. 若子模块之间没有依赖关系，则构建顺序为父模块，子模块依次按mdules中定义顺序

2. 构建serviceA

   1. 在serviceA只依赖clientA情况下
      1. 在顶级目录project执行： 
         1. mvn -pl moduleA/serviceA -am package   **可以** 会构建 moduleA、clientA、serviceA
      2. 在moduleA执行：
         1. mvn package  **可以**   会构建 moduleA、clientA、serviceA
         2. mvn -pl serviceA -am package   **可以** 会构建 moduleA、clientA、serviceA
   2. 在serviceA依赖clientA、clientB、clientC情况下
      1. 在顶级目录project执行： mvn -pl moduleA/serviceA -am package   **可以** 会构建project 、clientC、 moduleA、 clientA 、moduleB 、clientB、serviceA
      2. 在moduleA执行：
         1. mvn package  **不可以** 只会尝试构建moduleA、clientA、serviceA，因serviceA找不到pom中首先引入的clientB而失败。
         2. mvn -pl serviceA -am package   不可以 同上

3. 构建moduleA

   1. mvn package  即情况2在moduleA执行情况，取决于子模块是否引入moduleA外模块
   2. mvn -pl moduleA -am package 只会构建moduleA，不会构建子模块

4. 构建整体  顶级目录project mvn package 

5. 总结：

   1. 若构建顶级父项目project，则 mvn package 就可以
   2. 若构建中间层moduleA项目，要么保证serviceA等子模块依赖的moduleA外部模块都在仓库，然后执行mvn package；要么执行moduleA下每个子项目。
   3. 若构建底层子模块serviceA,则在顶级pom文件夹下执行mvn -pl moduleA/serviceA -am package

6. **总总结**：无论是依赖maven自身reactor还是手动加参，成功的前提都是要能找到所依赖的其他模块，所以最佳实践应该是在顶级目录执行，拼接执行命令中的路径



#### 参数

```text
-am --also-make 同时构建所列模块的 依赖的 模块；
-amd -also-make-dependents 同时构建 依赖于 所列模块的模块；
-pl --projects <arg> 构建制定的模块，模块间用逗号分隔；
-rf -resume-from <arg> 从指定的模块恢复反应堆。
mvn clean  -pl edge -am 
```

| 缩写 | 全名                            | 说明                                                         |
| ---- | ------------------------------- | ------------------------------------------------------------ |
| -h   | --help                          | 显示帮助信息                                                 |
| -am  | --also-make                     | 构建指定模块,同时构建指定模块依赖的其他模块                  |
| -amd | --also-make-dependents          | 构建指定模块,同时构建依赖于指定模块的其他模块                |
| -B   | --batch-mode                    | 以批处理(batch)模式运行                                      |
| -C   | --strict-checksums              | 检查不通过,则构建失败;(严格检查)                             |
| -c   | --lax-checksums                 | 检查不通过,则警告;(宽松检查)                                 |
| -D   | --define <arg>                  | 定义系统变量                                                 |
| -e   | --errors                        | 显示详细错误信息                                             |
| -emp | --encrypt-master-password <arg> | 加密主安全密码，用于用户访问管理等                           |
| -ep  | --encrypt-password <arg>        | 加密服务器密码                                               |
| -f   | --file <arg>                    | 使用指定的POM文件替换当前POM文件                             |
| -fae | --fail-at-end                   | 最后失败模式：Maven会在构建最后失败（停止）。如果Maven refactor中一个失败了，Maven会继续构建其它项目，并在构建最后报告失败。 |
| -ff  | --fail-fast                     | 最快失败模式： 多模块构建时,遇到第一个失败的构建时停止。     |
| -fn  | --fail-never                    | 从不失败模式：Maven从来不会为一个失败停止，也不会报告失败。  |
| -gs  | --global-settings <arg>         | 替换全局级别settings.xml文件                                 |
| -l   | --log-file <arg>                | 指定输出日志文件                                             |
| -N   | --non-recursive                 | 仅构建当前模块，而不构建子模块(即关闭Reactor功能)            |
| -nsu | --no-snapshot-updates           | 强制不更新SNAPSHOT                                           |
| -U   | --update-snapshots              | 强制更新releases、snapshots类型的插件或依赖库(否则maven一天只会更新一次snapshot依赖) |
| -o   | --offline                       | 运行offline模式,不联网进行依赖更新                           |
| -P   | --activate-profiles <arg>       | 激活指定的profile文件列表(用逗号[,]隔开)                     |
| -pl  | --projects <arg>                | 手动选择需要构建的项目,项目间以逗号分隔                      |
| -q   | --quiet                         | 安静模式,只输出ERROR                                         |
| -rf  | --resume-from <arg>             | 从指定的项目(或模块)开始继续构建                             |
| -s   | --settings <arg>                | 替换用户级别settings.xml文件                                 |
| -T   | --threads <arg>                 | 线程计数，例如2.0c，其中c是核心数，两者相乘即为总线程数      |
| -t   | --toolchains <arg>              | 指定用户的toolchains文件路径                                 |
| -V   | --show-version                  | 显示版本信息而不停止构建                                     |
| -v   | --version                       | 显示版本信息                                                 |
| -X   | --debug                         | Debug模式，输出详细日志信息                                  |
| -cpu | --check-plugin-updates          | 【废弃】,仅为了向后兼容                                      |
| -npr | --no-plugin-registry            | 【废弃】,仅为了向后兼容                                      |
| -npu | --no-plugin-updates             | 【废弃】,仅为了向后兼容                                      |
| -up  | --update-plugins                | 【废弃】,仅为了向后兼容                                      |


##### dependencies 和 dependencyManagement

1. 所有声明在dependencies里的依赖都会自动引入，并默认被所有的子项目继承。
2. dependencyManagement里只是声明依赖，并不实现引入，因此子项目需要显式的声明需要用的依赖。如果不在子项目中声明依赖，是不会从父项目中继承下来的；只有在子项目中写了该依赖项，并且没有指定具体版本，才会从父项目中继承该项，并且version和scope都读取自父pom;另外如果子项目中指定了版本号，那么会使用子项目中指定的jar版本。
3. `<scope>test</scope>` 只能在maven test 文件夹下引到