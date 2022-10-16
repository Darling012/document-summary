# redis 持久化
### RDB 
1. redis database backup file, Redis 数据备份文件, 也叫作 redis 数据快照
2. redis 周期性将内存中数据创建快照，生成 RDB 文件保存磁盘
3. fork 一个子进程，采用二进制压缩存储。对 redis 性能影响小，数据恢复比 AOF 快。
4. 生成多个数据文件，每个文件代表某一时刻 redis 数据
5. rdb 文件占用空间小，适合备份或迁移指定时间点的 redis 数据
6. 突然宕机会丢失当前周期内数据

### AOF
1. append only file，追加文件
2. redis 以日志形式记录所有写入类操作。重启时，重新执行 aof 文件中的操作来恢复数据
3. 采用 appendonly 追加方式写入，没有磁盘寻址开销，性能高，文件不易损坏
4. 当 aof 文件过大时，redis 会自动执行 aof rewrite，重组 aof 文件，降低其占用空间。
5. AOF 通常设置 1s 让后台线程追加一次数据，所以最多丢失 1s 数据
6. 同样的数据 AOF 要比 RDB 大的多
7. 类似 mysql 的 binlog

通常采用 RDB 做冷备，AOF 做热备。第一时间用 RDB 恢复，然后 AOF 补全。

#### 混合持久化
1. 开启混合持久化后，AOF 文件重写时，将之前内存数据做 RDB 快照处理，重写期间是转换为 resp 命令，都写入新的 AOF 文件。

##### RDB 持久化中 save 和 bgsave 区别
1. 都是调用 rdbSave 函数，save 阻塞，bgsave 非阻塞
2. save 阻塞 redis 主进程，直到保存完。
3. bgsave 则 fork 一个子进程，子进程负责调用 rdbSave, 保存完后通知主进程
	1. 本质是 fork 和 cow，fork 子进程创建后，父子进程共享数据段，copy on write。

##### reference
1. [Redis持久化方式RDB和AOF以及Redis集群介绍_Penda xx的博客-CSDN博客_redis集群aof](https://blog.csdn.net/Doreamonx/article/details/125642133)
2. [Fetching Title#bcrq](https://www.cnblogs.com/yaopengfei/p/13879077.html)
3. 