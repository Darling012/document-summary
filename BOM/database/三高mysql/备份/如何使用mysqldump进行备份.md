Outfile 如何进行改进？[02:17](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-3%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump%E8%BF%9B%E8%A1%8C%E5%A4%87%E4%BB%BD.mp4#t=137.074213)
1. 自动执行每个表的命令
2. 自动开启事务
3. 输出 insert 语句，可以用于还原

mysqldump[04:12](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-3%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump%E8%BF%9B%E8%A1%8C%E5%A4%87%E4%BB%BD.mp4#t=252.216304)
1. mysql server 自带
2. 输出内容为 sql 语句，平衡了阅读与还原
3. sql 语句占用空间小

原理[06:51](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-3%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump%E8%BF%9B%E8%A1%8C%E5%A4%87%E4%BB%BD.mp4#t=411.383481)
1. 执行时 sql 加 sql_no_cache，不进行缓存
2. --single-transation


[使用](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-3%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump%E8%BF%9B%E8%A1%8C%E5%A4%87%E4%BB%BD.mp4#t=434.290344)

缺点：
1. 导出逻辑数据，备份较慢
2. 还原需要执行 sql，速度也比较慢