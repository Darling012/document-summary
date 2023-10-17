---
title: "一文读懂什么是进程、线程、协程"
alias: 
  - "一文读懂什么是进程、线程、协程"
created-date: 2023-06-15T09:55:23+0800
type: Simpread
banner: "https://raw.githubusercontent.com/SurvivalBoy/imageGallery/master/JVM/process.png "
banner_icon: 🔖
tag: 
idx: 14
---

# 一文读懂什么是进程、线程、协程

> [!example]- [🧷内部链接](<http://localhost:7026/reading/14>) [🌐外部链接](<https://www.cnblogs.com/Survivalist/p/11527949.html>)    
> URI:: [🧷](<http://localhost:7026/reading/14>) [🌐](<https://www.cnblogs.com/Survivalist/p/11527949.html>) 
> intURI:: [🧷内部链接](<http://localhost:7026/reading/14>)

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
>  **Description**:: [TOC] 进程 &emsp;&emsp;我们都知道计算机的核心是CPU，它承担了所有的计算任务；而操作系统是计算机的管理者，它负责任务的调度、资源的分配和管理，统领整个计算机硬件；应用程序则是具有某种功能的程序，程序是运行于操作系统之上的。 &emsp;&emsp;进程是一个具有一定独立功能的程序
%%

> [!md] Metadata  
> **标题**:: [一文读懂什么是进程、线程、协程](https://www.cnblogs.com/Survivalist/p/11527949.html)  
> **日期**:: [[2023-06-15]]  

## Annotations


> [!srhl2] [[SR14@一文读懂什么是进程、线程、协程|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/14#id=1686794123894>)   
> 每一个处理核心对应一个内核线程。
> ^sran-1686794123894

> [!srhl2] [[SR14@一文读懂什么是进程、线程、协程|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/14#id=1686794268165>)   
> 内核线程（Kernel Thread，KLT）就是直接由操作系统内核支持的线程，这种线程由内核来完成线程切换，内核通过操作调度器对线程进行调度，并负责将线程的任务映射到各个处理器上。
> ^sran-1686794268165

> [!srhl2] [[SR14@一文读懂什么是进程、线程、协程|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/14#id=1686794258941>)   
> 一般一个处理核心对应一个内核线程，
> ^sran-1686794258941

> [!srhl2] [[SR14@一文读懂什么是进程、线程、协程|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/14#id=1686794276074>)   
> 比如单核处理器对应一个内核线程，双核处理器对应两个内核线程，四核处理器对应四个内核线程。
> ^sran-1686794276074

> [!srhl2] [[SR14@一文读懂什么是进程、线程、协程|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/14#id=1686794817995>)   
> 现在的电脑一般是双核四线程、四核八线程，是采用超线程技术将一个物理处理核心模拟成两个逻辑处理核心，对应两个内核线程
> ^sran-1686794817995

> [!srhl2] [[SR14@一文读懂什么是进程、线程、协程|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/14#id=1686794686334>)   
> 程序一般不会直接去使用内核线程，而是去使用内核线程的一种高级接口——轻量级进程（Lightweight Process，LWP），轻量级进程就是我们通常意义上所讲的线程，也被叫做用户线程。由于每个轻量级进程都由一个内核线程支持，因此只有先支持内核线程，才能有轻量级进程。
> ^sran-1686794686334

> [!srhl2] [[SR14@一文读懂什么是进程、线程、协程|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/14#id=1686794893731>)   
> 线程之间的切换由用户态的代码来进行
> ^sran-1686794893731

> [!srhl2] [[SR14@一文读懂什么是进程、线程、协程|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/14#id=1686794878655>)   
> 由线程库负责在可用的可调度实体上调度用户线程
> ^sran-1686794878655

> [!srhl2] [[SR14@一文读懂什么是进程、线程、协程|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/14#id=1686796256115>)   
> 这种由程序员自己写程序来管理的轻量级线程叫做『用户空间线程』，具有对内核来说不可见的特性。
> ^sran-1686796256115

> [!srhl2] [[SR14@一文读懂什么是进程、线程、协程|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/14#id=1686796273563>)   
> 因为是自主开辟的异步任务，所以很多人也更喜欢叫它们纤程（Fiber），或者绿色线程（GreenThread）。正如一个进程可以拥有多个线程一样，一个线程也可以拥有多个协程。
> ^sran-1686796273563

