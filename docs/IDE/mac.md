# mac
## redis
redis-server
//Redis 默认端口是6379，你也可以换个端口号启动，
redis-server --port 6380
//使用了肯定需要停止，停止怎么弄呢？
//停止
//执行命令
redis-cli shutdown
## mysql
//启动MySQL服务
sudo /usr/local/MySQL/support-files/mysql.server start
//停止MySQL服务
sudo /usr/local/mysql/support-files/mysql.server stop
//重启MySQL服务
sudo /usr/local/mysql/support-files/mysql.server restart
##### 启动 mysql, 并设置为开机启动
brew services start mysql
##### 关闭 mysql
brew services stop mysql
##### 重启 mysql
brew services restart mysql