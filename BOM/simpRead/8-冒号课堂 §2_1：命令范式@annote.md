---
title: "冒号课堂 §2_1：命令范式"
alias: 
  - "冒号课堂 §2_1：命令范式"
created-date: 2023-04-26T17:06:17+0800
type: Simpread
banner: "https://images.cnblogs.com/cnblogs_com/xyz98/img/figure2-1.jpg "
banner_icon: 🔖
tag: 
idx: 8
---

# 冒号课堂 §2_1：命令范式

> [!example]- [🧷内部链接](<http://localhost:7026/reading/8>) [🌐外部链接](<https://www.cnblogs.com/xyz98/archive/2009/03/22/1419103.html>)    
> URI:: [🧷](<http://localhost:7026/reading/8>) [🌐](<https://www.cnblogs.com/xyz98/archive/2009/03/22/1419103.html>) 
> intURI:: [🧷内部链接](<http://localhost:7026/reading/8>)

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
>  **Description**:: 命令范式——一切行动听指挥（命令式编程简谈）
•	（命令式编程）其世界观是：程序是由若干行动指令组成的有序列表；其方法论是：用变量来储存数据，用语句来执行指令
•	（结构化编程）在微观上，主张循规
%%

> [!md] Metadata  
> **标题**:: [冒号课堂 §2_1：命令范式](https://www.cnblogs.com/xyz98/archive/2009/03/22/1419103.html)  
> **日期**:: [[2023-04-26]]  

## Annotations


> [!srhl5] [[SR8@冒号课堂 §2_1：命令范式|📄]] <mark style="background-color: #a8ea68">Highlights</mark> [🧷](<http://localhost:7026/reading/8#id=1682499977502>)   
> 命令式编程（imperative programming）。用命令式编写的程序由命令序列组成，即一系列祈使句：‘先做这，再做那’，强调‘怎么做’。更学术点说，命令式编程是电脑——准确地讲，是冯 · 诺伊曼机（von Neumann machine）——运行机制的抽象，即_依序_从内存中获取指令和数据然后去执行。从范式的角度看，**其世界观是：程序是由若干行动指令组成的有序列表；其方法论是：用变量来储存数据，用语句来执行指令**
> ^sran-1682499977502

> [!srhl2] [[SR8@冒号课堂 §2_1：命令范式|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/8#id=1682500138457>)   
> 过程式编程（procedural programming）是指引入了过程（procedure）、函数（function）或子程序（subroutine/subprogram）的命令式编程。但由于现代的命令式语言均具备此特征，故二者往往不加区分
>  
> - 📝 脱离机器第一步：面向过程 不再关注机器本身的操作指令，存储等方面，而是关注如何一步步解决具体的问题，即解决问题的过程。
> ^sran-1682500138457

> [!srhl2] [[SR8@冒号课堂 §2_1：命令范式|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/8#id=1682500201503>)   
> 结构化编程（structured programming 或简称 SP），它是在过程式编程的基础上发展起来的。其本质是一种编程原则，提倡代码应具有清晰的逻辑结构，以保证程序易于读写、测试、维护和优化。可别小瞧它，在上世纪六十年代首次爆发的软件危机中，它曾起着中流砥柱的作用
>  
> - 📝第一次软件危机：结构化程序设计
		1. 软件规模越来越大，复杂度越来越高
		2. 采用自顶向下、逐步细化、模块化。将软件复杂度控制在一定范围内，从而在从整体上上降低了软件开发复杂度
> ^sran-1682500201503

> [!srhl2] [[SR8@冒号课堂 §2_1：命令范式|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/8#id=1682500223362>)   
> OOP
>  
> - 📝第二次软件危机：面向对象程序设计
		1. 传统面向过程方法已经不能适应快速多变业务需求，需要解决可扩展性和可维护性问题。
> ^sran-1682500223362

