# ftp
[FTP的端口号是多少？ftp服务器端口21和22有什么区别？](https://www.huaweicloud.com/zhishi/edits-15756268.html)
FTP (File Transfer Protocol，文件传输协议) 是 TCP/IP 协议组中的协议之一。

默认情况下FTP协议使用TCP端口中的 20和21这两个端口，其中20用于传输数据，21用于传输控制信息。
但是，是否使用 20 作为传输数据的端口与 FTP 使用的传输模式有关，如果采用主动模式，那么数据传输端口就是 20；如果采用被动模式，则具体最终使用哪个端口要服务器端和客户端协商决定。

在 FileZilla 中：
1. 用 sftp 协议连接 Server ，需要用 port 22 
2. 用 ftp 协议连接 Server, 用 port21
3. 一个控制端口一个数据传输端口。
4. 端口20才是真正传输所用到的端口，端口21只用于FTP的登陆认证

```yaml
version: '3.3'
services:
    ftp-edu:
        container_name: ftp-edu
        image: fauria/vsftpd
        ports:
            - '20:20'
            - '21:21'
            - '21100-21110:21100-21110'
        volumes:
            - '/data/ftp/files:/home/vsftpd'
        environment:
            - FTP_USER=deepblueftp
            - FTP_PASS=deepblue
            - PASV_ADDRESS=192.168.215.66
            - PASV_MIN_PORT=21100
            - PASV_MAX_PORT=21110
        restart: always
        networks:
            - edu-network

networks:
  edu-network:
    external:
      name: edu-network
```

1. [Docker | 搭建一个ftp服务器 - 简书](https://www.jianshu.com/p/c84f6e6de002)
2. [Docker安装FTP服务器\_docker ftp\_歪桃的博客-CSDN博客](https://blog.csdn.net/m0_37892044/article/details/110912700)
3. [Ftp、Ftps与Sftp的差异](https://mp.weixin.qq.com/s?__biz=MzIzMzgxOTQ5NA==&mid=2247489241&idx=2&sn=791f3cd59f8497ec1d266f3054b46658)
4. [再见 FTP/SFTP，是时候拥抱下一代文件传输利器 Croc 了！ - 腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/1846978)
5. 