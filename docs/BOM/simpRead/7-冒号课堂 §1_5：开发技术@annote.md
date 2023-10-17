---
title: "冒号课堂 §1_5：开发技术"
alias: 
  - "冒号课堂 §1_5：开发技术"
created-date: 2023-04-26T17:01:17+0800
type: Simpread
tag: 
idx: 7
---

# 冒号课堂 §1_5：开发技术

> [!example]- [🧷内部链接](<http://localhost:7026/reading/7>) [🌐外部链接](<https://www.cnblogs.com/xyz98/archive/2009/03/19/1416215.html>)    
> URI:: [🧷](<http://localhost:7026/reading/7>) [🌐](<https://www.cnblogs.com/xyz98/archive/2009/03/19/1416215.html>) 
> intURI:: [🧷内部链接](<http://localhost:7026/reading/7>)

%%
> [!example]+ **Comments**  
> ```dataview
> TABLE 
>     WITHOUT ID
>     link(Source, dateformat(date(Source), "yyyy-MM-dd")) as Date___, 
>     regexreplace(rows.Comments,"^@@\[\[.+?\]\]\s","") as "Comments"
> FROM "journals"
> WHERE  contains(cmnt, this.file.name)
> FLATTEN cmnt as Comments
> WHERE contains(Comments, this.file.name)
> GROUP BY file.link as Source
> SORT rows.file.day desc
> ```
>  **Description**:: 开发技术——实用还是时髦？（关于框架、设计模式、架构和编程范式等开发技术的讨论）
•	任何概念和技术都不是孤立的，如果不能在纵向的时间和横向的联系中找准坐标，便似那群摸象的盲人，各执一端却又自以为是
%%

> [!md] Metadata  
> **标题**:: [冒号课堂 §1_5：开发技术](https://www.cnblogs.com/xyz98/archive/2009/03/19/1416215.html)  
> **日期**:: [[2023-04-26]]  

## Annotations


> [!srhl2] [[SR7@冒号课堂 §1_5：开发技术|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/7#id=1682590255672>)   
> 任何概念和技术都不是孤立的，如果不能在纵向的时间和横向的联系中找准坐标，便似那群摸象的盲人，各执一端却又自以为是。 ”
> ^sran-1682590255672

> [!srhl2] [[SR7@冒号课堂 §1_5：开发技术|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/7#id=1682590291030>)   
> 在_宏观管理_上选取一些框架以控制整体的结构和流程；在_微观实现_上利用库和工具包来解决具体的细节问题。框架的意义在于使_设计者_在特定领域的整体设计上不必重新发明轮子；库和工具包的意义在于使_开发者_摆脱底层编码，专注特定问题和业务逻辑。”
> ^sran-1682590291030

> [!srhl2] [[SR7@冒号课堂 §1_5：开发技术|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/7#id=1682590300300>)   
> 框架与工具包最大的差别在截然相反的_设计理念_ 上：**库和工具包是为程序员带来自由的，框架是为程序员带来约束的。**具体地说，库和工具包是为程序员提供武器装备的，框架则利用_控制反转_（IoC）[1] 机制实现对各模块的统一调度从而剥夺了程序员对全局的掌控权，使他们成为手执编程武器、随时听候调遣的士兵。 ”
> ^sran-1682590300300

> [!srhl2] [[SR7@冒号课堂 §1_5：开发技术|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/7#id=1682499677865>)   
> 设计模式（design pattern）和架构（architecture）不是_软件产品_，而是_软件思想_。**设计模式是软件的战术思想，架构是软件的战略决策。**设计模式是针对某些经常出现的问题而提出的行之有效的设计解决方案，它侧重_思想重用_，因此比框架更抽象、更普适，但多限于局部解决方案，没有框架的整体性。与之相似的还有惯用法（idiom），也是针对常发问题的解决方案，但偏重实现而非设计，与实现语言密切相关，是一种更底层更具体的编程技巧。至于架构，一般指一个软件系统的最高层次的整体结构和规划，一个架构可能包含多个框架，而一个框架可能包含多个设计模式。
> ^sran-1682499677865

> [!srhl2] [[SR7@冒号课堂 §1_5：开发技术|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/7#id=1682499711085>)   
> 一个惯用 C 语言编程的人也许很快就能写一些 C++ 程序，但如果他只注重 C++ 的_语法_而不注重培养 OOP 的_语感_，那么写出的程序一定是‘C 式 C++’。与其如此，倒不如直接用 C 呢。
>  
> - 📝像一直用Java这个面向对象语言写事务脚本代码一样

> ^sran-1682499711085

> [!srhl2] [[SR7@冒号课堂 §1_5：开发技术|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/7#id=1682590336807>)   
> 编程范式正体现了编程的思维方式，因而是培养编程语言的语感的关键。
> ^sran-1682590336807

