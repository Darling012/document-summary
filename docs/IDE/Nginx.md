# Nginx
[LVS、Nginx、HAProxy区别](https://www.cnblogs.com/struggle-1216/p/13511703.html)
## DeepBlue
### 实时流量复制
[Fetching Title#ggc0](https://www.cnblogs.com/cjsblog/p/12163207.html)
- nginx mirror: 应用层复制；支持流量放大；不支持录制回放；
- goreplay:应用层复制；支持流量放大；不支持录制回放；
- tcpcopy:网络层复制；支持流量放大；支持录制与回放；
nginx 1.13.4及后续版本内置 ngx_http_mirror_module 模块，提供流量镜像(复制)的功能。通过--without-http_mirror_module 移除模块。处理请求时，生成子请求访问其他服务，对子请求返回值不做处理。
```
worker_processes  1;
events {
    worker_connections  1024;
}
http {
    default_type  application/octet-stream;
    sendfile        on;
    server {
        listen 8181;
        root html/test;
    }
    server {
        listen 8282;
        access_log /usr/local/nginx/logs/mir1.log;
        root html/mir1;
    }
    server {
        listen 8383;
        access_log /usr/local//nginx/logs/mir2.log;
        root html/mir2;
    }
    upstream backend {
        server 127.0.0.1:8181;
    }
    upstream test_backend1 {
        server 127.0.0.1:8282;
    }
    upstream test_backend2 {
        server 127.0.0.1:8383;
    }
    server {
        listen 80;
        index  index.html;
        server_name localhost;
        location / {
            mirror /mirrorone;
            mirror /mirrortwo;
            proxy_pass http://backend;
        }
        location = /mirrorone {
            internal;
            proxy_pass http://test_backend1$request_uri;
        }
        location = /mirrortwo {
            internal;
            proxy_pass http://test_backend2$request_uri;
        }
    }
}
```
### 玻纤正向代理
Nginx本身不支持HTTPS正向代理，需要安装ngx_http_proxy_connect_module模块后才可以支持https正向代理
1. git clone https://github.com/chobits/ngx_http_proxy_connect_module.git
2. 确认Nginx版本及对应的patch  https://github.com/chobits/ngx_http_proxy_connect_module?spm=a2c4e.10696291.0.0.668319a4yTdxw4
3. patch -p1 < ngx_http_proxy_connect_module本地路径/对应版本patch
4. 其他步骤直接搜
##### 背景
玻纤内网分为192和172网段，192不能访问外网，生产环境部署在192网段，所以在172.16.110.225部署Nginx正向代理实现访问外网。
##### Nginx部署
1. Nginx本身不支持HTTPS正向代理，需要安装ngx_http_proxy_connect_module模块后才可以支持https正向代理
2. 参考 https://www.infoq.cn/article/taujwgln6d_6qls6yj6s
##### 后端改动
后端目前需要连接外网的需求为企业微信相关，有两块
1. 使用WxJava工具包的部分在WxCpConfiguration中配置代理
2. 使用RestTemplate部分通过RestTemplate配置代理
###### nginx错误
invalid PID number "" in "/usr/local/nginx/logs/nginx. pid"
解决： /usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx. conf
## 安装
##### 1.Ubuntu下安装Nginx比较简单
敲入下列命令即可：
```
    sudo apt-get update
    sudo apt-get install nginx1212
```
##### 2.Ubuntu下卸载，稍不注意就会入坑
```
    sudo apt-get remove nginx nginx-common # 卸载删除除了配置文件以外的所有文件。
    sudo apt-get purge nginx nginx-common # 卸载所有东东，包括删除配置文件。
    sudo apt-get autoremove # 在上面命令结束后执行，主要是卸载删除Nginx的不再被使用的依赖包。
    sudo apt-get remove nginx-full nginx-common #卸载删除两个主要的包。
```
#### 编译安装
1.  wget http://nginx.org/download/nginx-1.20.2.tar.gz
2. wget https://github.com/winshining/nginx-http-flv-module/archive/refs/tags/v1.2.10.tar.gz
3. 解压
3. 缺少依赖的话 sudo apt-get install build-essential
4. ```
   ./configure --prefix=/data/nginx --add-dynamic-module=/data/nginx/nginx-http-flv-module-1.2.10
   make
   make install
   ```
设为全局
ln -s /usr/local/nginx/sbin/nginx  /usr/local/bin/
## nginx模块
##### nginx 查看安装哪些模块
1. [[nginx]查看安装了哪些模块_orangleliu的博客-CSDN博客]( https://blog.csdn.net/orangleliu/article/details/44219387 )
2. [nginx查看已安装模块_sayyy的博客-CSDN博客_nginx 查看模块](https://blog.csdn.net/sayyy/article/details/121178421)
[Nginx中常用的模块整理 | 程序员poetry's Blog](https://blog.poetries.top/2018/11/27/nginx-module-summary/)
## 命令
Linux 下查看 Nginx 安装目录、版本号信息及当前运行的配置文件
Linux环境下，怎么确定Nginx是以那个config文件启动的？
输入命令行： ps  -ef | grep nginx
master process 后面的就是 nginx的目录。
怎么查看服务器上安装的nginx版本号，主要是通过ngix的-v或-V选项
-v 显示 nginx 的版本。
-V 显示 nginx 的版本，编译器版本和配置参数。
Linux下如何查看定位当前正在运行的Nginx的配置文件
因为备份、不同版本等问题，导致ECS Linux上存放有多个Nginx目录，可以通过如下方法定位当前正在运行的Nginx的配置文件：
1.  查看nginx的PID，以常用的80端口为例：  
    netstat -anop | grep 0.0.0.0:80
2.  通过相应的进程ID(比如：4562）查询当前运行的nginx路径：  
    ll  /proc/4562/exe
3.  获取到nginx的执行路径后，使用-t参数即可获取该进程对应的配置文件路径，如：  
    /usr/local/nginx/sbin/nginx -t  
    nginx: the configuration file /usr/local/nginx/conf/nginx.conf syntax is ok  
    nginx: configuration file /usr/local/nginx/conf/nginx.conf test is successful
nginx  ##打开 nginx
nginx -t     ##测试配置文件是否有语法错误
nginx -s reopen  ##重启Nginx
nginx -s reload    ##重新加载Nginx配置文件，然后以优雅的方式重启Nginx
nginx -s stop   ##强制停止Nginx服务
nginx -s quit   ##优雅地停止Nginx服务（即处理完所有请求后再停止服务）
nginx [-?hvVtq] [-s signal] [-c filename] [-p prefix] [-g directives]
-?,-h           : 打开帮助信息
-v              : 显示版本信息并退出
-V              : 显示版本和配置选项信息，然后退出
-t              : 检测配置文件是否有语法错误，然后退出
-q              : 在检测配置文件期间屏蔽非错误信息
-s signal       : 给一个 nginx 主进程发送信号：stop（强制停止）, quit（优雅退出）, reopen（重启）, reload（重新加载配置文件）
-p prefix       : 设置前缀路径（默认是：/usr/share/nginx/）
-c filename     : 设置配置文件（默认是：/etc/nginx/nginx.conf）
-g directives   : 设置配置文件外的全局指令