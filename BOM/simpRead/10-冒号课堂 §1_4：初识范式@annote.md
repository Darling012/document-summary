---
title: "冒号课堂 §1_4：初识范式"
alias: 
  - "冒号课堂 §1_4：初识范式"
created-date: 2023-04-27T18:08:38+0800
type: Simpread
tag: 
idx: 10
---

# 冒号课堂 §1_4：初识范式

> [!example]- [🧷内部链接](<http://localhost:7026/reading/10>) [🌐外部链接](<https://www.cnblogs.com/xyz98/archive/2009/03/17/1414324.html>)    
> URI:: [🧷](<http://localhost:7026/reading/10>) [🌐](<https://www.cnblogs.com/xyz98/archive/2009/03/17/1414324.html>) 
> intURI:: [🧷内部链接](<http://localhost:7026/reading/10>)

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
>  **Description**:: 初识范式——程序王国中的世界观与方法论（初步引入编程范式）
•	得形而忘意，无异舍本逐末；得意而忘形，方能游刃有余
•	当你编程之时，便进入到自己创造的世界之中。这是你的世界，只有注入你的想象力、
%%

> [!md] Metadata  
> **标题**:: [冒号课堂 §1_4：初识范式](https://www.cnblogs.com/xyz98/archive/2009/03/17/1414324.html)  
> **日期**:: [[2023-04-27]]  

## Annotations


> [!srhl2] [[SR10@冒号课堂 §1_4：初识范式|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/10#id=1682590118644>)   
> 编程范式是抽象的，必须通过具体的编程语言来体现。它代表的世界观往往体现在语言的**核心概念**中，代表的方法论往往体现在语言的**表达机制**中。一种范式可以在不同的语言中实现，一种语言也可以同时支持多种范式。任何语言在设计时都会倾向某些范式、同时回避某些范式，由此形成了不同的语法特征和语言风格。
> ^sran-1682590118644

