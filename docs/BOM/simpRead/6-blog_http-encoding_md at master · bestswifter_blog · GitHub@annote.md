
# blog_http-encoding_md at master · bestswifter_blog · GitHub

> [!example]- [🧷内部链接](<http://localhost:7026/reading/6>) [🌐外部链接](<https://github.com/bestswifter/blog/blob/master/articles/http-encoding.md>)    
> URI:: [🧷](<http://localhost:7026/reading/6>) [🌐](<https://github.com/bestswifter/blog/blob/master/articles/http-encoding.md>) 
> intURI:: [🧷内部链接](<http://localhost:7026/reading/6>)

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
>  **Description**:: 我的博客. Contribute to bestswifter/blog development by creating an account on GitHub.
%%

> [!md] Metadata  
> **标题**:: [blog_http-encoding_md at master · bestswifter_blog · GitHub](https://github.com/bestswifter/blog/blob/master/articles/http-encoding.md)  
> **日期**:: [[2023-04-17]]  

## Annotations


> [!srhl2] [[SR6@blog_http-encoding_md at master · bestswifter_blog · GitHub|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/6#id=1681714234912>)   
> JSON 的全文是 JavaScript Object Notation，也就是 JavaScript 中的对象表示方法
> ^sran-1681714234912

> [!srhl2] [[SR6@blog_http-encoding_md at master · bestswifter_blog · GitHub|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/6#id=1681714242945>)   
> 可见 JSON 其实是字符串的一种描述方式，它的本质还是字符串
> ^sran-1681714242945

> [!srhl2] [[SR6@blog_http-encoding_md at master · bestswifter_blog · GitHub|📄]] <mark style="background-color: #ffeb3b">Highlights</mark> [🧷](<http://localhost:7026/reading/6#id=1681714267414>)   
> JSON 只是一种编码规范，它仅仅规定了 JSON 格式的字符串应该具备什么特征，至于原来的数据如何转化为 JSON 串，则是由各个编程语言自己实现
> ^sran-1681714267414

