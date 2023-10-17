![2-1 互联网公司如何进行持续集成【理论支撑】【公众号：某课联盟】-0003.png](https://cdn.nlark.com/yuque/0/2021/png/2411569/1627481467395-0448d82a-9cea-4419-b720-075f3830af0e.png)![2-1 互联网公司如何进行持续集成【理论支撑】【公众号：某课联盟】-0001.png](https://cdn.nlark.com/yuque/0/2021/png/2411569/1627481344588-b8184e42-e179-4ac8-a250-5c95a475085a.png)





Artifactory的仓库主要分三类：local、remote、virtual

本地私有仓库（local）：用于内部使用，上传的组件不会向外部进行同步；

远程仓库（remote）：用于代理及缓存公共仓库，不能向此类型的仓库上传私有组件；

虚拟仓库（virtual）：不是真实在存储上的仓库，用于组织本地仓库和远程仓库



maven会先下载maven-metadata.xml 更新后上传回去  这是maven索引文件 找jar 从这个文件里找



可以给jar加标签 比如是解决哪个问题



根据layout配置识别文件夹



# 6-3

###### artifactory三个版本

oss  支持maven,gradle和通用仓库，适合个人和50人左右小团队

jcr 支持docker、helm和通用仓库

企业版



