
[思路](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-4%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump+binlog%E5%A2%9E%E9%87%8F%E5%A4%87%E4%BB%BD.mp4#t=436.852572)
1. Mysqldump 就是把当前数据库数据 select 出来后导出
2. binlog 记录了 MySQL 数据的变化
3. Binlog 与存储引擎无关
4. Mysqldump 全量备份后，用 Binlog 做增量备份
5. Mysqldump 全量备份时，切换新的 binlog 文件
6. 从零还原时，采用全量还原+binlog 还原


##### 操作
[全量](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-4%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump+binlog%E5%A2%9E%E9%87%8F%E5%A4%87%E4%BB%BD.mp4#t=776.771854)
1. --flush-logs:
2. --master-data=2:
[增量](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-4%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump+binlog%E5%A2%9E%E9%87%8F%E5%A4%87%E4%BB%BD.mp4#t=921.793833)
1. 切换 binlog 文件

[还原](file:///Volumes/resources/database/mysql/%E9%AB%98%E5%B9%B6%E5%8F%91%20%E9%AB%98%E6%80%A7%E8%83%BD%20%E9%AB%98%E5%8F%AF%E7%94%A8%20MySQL%20%E5%AE%9E%E6%88%98/%E7%AC%AC8%E7%AB%A0%20%E6%80%8E%E4%B9%88%E7%BB%99%E6%95%B0%E6%8D%AE%E4%B8%8A%E4%BF%9D%E9%99%A9/8-4%20%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8mysqldump+binlog%E5%A2%9E%E9%87%8F%E5%A4%87%E4%BB%BD.mp4#t=997.979894)



