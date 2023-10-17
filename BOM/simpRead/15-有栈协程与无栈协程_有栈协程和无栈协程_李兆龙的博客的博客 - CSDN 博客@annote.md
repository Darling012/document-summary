---
title: "有栈协程与无栈协程_有栈协程和无栈协程_李兆龙的博客的博客 - CSDN 博客"
alias: 
  - "有栈协程与无栈协程_有栈协程和无栈协程_李兆龙的博客的博客 - CSDN 博客"
created-date: 2023-06-30T16:02:04+0800
type: Simpread
banner: "https://img-blog.csdnimg.cn/2020062316092970.png "
banner_icon: 🔖
tag: 
idx: 15
---

# 有栈协程与无栈协程_有栈协程和无栈协程_李兆龙的博客的博客 - CSDN 博客

> [!example]- [🧷内部链接](<http://localhost:7026/reading/15>) [🌐外部链接](<https://blog.csdn.net/weixin_43705457/article/details/106924435>)    
> URI:: [🧷](<http://localhost:7026/reading/15>) [🌐](<https://blog.csdn.net/weixin_43705457/article/details/106924435>) 
> intURI:: [🧷内部链接](<http://localhost:7026/reading/15>)

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
>  **Description**:: 引言关于协程是什么这类基本概念我们不再多提，有兴趣的朋友可以看看我写的这篇文章《聊聊协程》。写这篇文章的原因是当我对这个问题感到疑惑的时候发现CSDN上并没有相关的文章，遂在有了一点理解以后想写下一点对这个问题的看法，以帮助后来学习的朋友。正文如今虽不敢说协程已经是红的发紫，但确实是越来越受到了大家的重视。Golang中的已经是只有goroutine，以至于很多go程序员是只知有协程，不知有线程了。就连C++这样的“老顽固”也在最新的C++20中原生支持协程。更不用说很多活跃的语言如python，ja_有栈协程和无栈协程
%%

> [!md] Metadata  
> **标题**:: [有栈协程与无栈协程_有栈协程和无栈协程_李兆龙的博客的博客 - CSDN 博客](https://blog.csdn.net/weixin_43705457/article/details/106924435)  
> **日期**:: [[2023-06-30]]  

## Annotations


> [!srhl2] [[SR15@有栈协程与无栈协程_有栈协程和无栈协程_李兆龙的博客的博客 - CSDN 博客|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/15#id=1688112124891>)   
> 这里我们想说的一点是**所谓的有栈，无栈并不是说这个协程运行的时候有没有栈，而是说协程之间是否存在调用栈（callbackStack）**
> ^sran-1688112124891

