[LVS、Nginx、HAProxy区别](https://www.cnblogs.com/struggle-1216/p/13511703.html)

##### 实时流量复制

- nginx mirror: 应用层复制；支持流量放大；不支持录制回放；
- goreplay:应用层复制；支持流量放大；不支持录制回放；
- tcpcopy:网络层复制；支持流量放大；支持录制与回放；

nginx 1.13.4及后续版本内置ngx_http_mirror_module模块，提供流量镜像(复制)的功能。通过--without-http_mirror_module移除模块。处理请求时，生成子请求访问其他服务，对子请求返回值不做处理。



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

### 安装

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

#### rtmp

```shell
#worker_processes  1; #运行在 Windows 上时，设置为 1，因为 Windows 不支持 Unix domain socket
worker_processes  auto; #1.3.8 和 1.2.5 以及之后的版本

#worker_cpu_affinity  0001 0010 0100 1000; #只能用于 FreeBSD 和 Linux
#worker_cpu_affinity  auto; #1.9.10 以及之后的版本

error_log /usr/local/nginx/logs/error.log error;

#如果此模块被编译为动态模块并且要使用与 RTMP 相关的功
#能时，必须指定下面的配置项并且它必须位于 events 配置
#项之前，否则 NGINX 启动时不会加载此模块或者加载失败

load_module modules/ngx_http_flv_live_module.so;

events {
    worker_connections  4096;
}

http {
    include       mime.types;
    default_type  application/octet-stream;

    keepalive_timeout  65;

    server {
        listen 8181;
        root html/;
    }
    upstream backend {
        server 127.0.0.1:8181;
    }
    upstream test_backend {
        server 192.168.215.100;
    }
    
    server {
        listen       80; #拉流端口

        location / {
            mirror /mirrorone;
            proxy_pass http://backend;
        }
        location = /mirrorone {
            internal;
            proxy_pass http://test_backend$request_uri;
        }
        
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        location /live {
            flv_live on; #打开 HTTP 播放 FLV 直播流功能
            chunked_transfer_encoding on; #支持 'Transfer-Encoding: chunked' 方式回复

            add_header 'Access-Control-Allow-Origin' '*'; #添加额外的 HTTP 头
            add_header 'Access-Control-Allow-Credentials' 'true'; #添加额外的 HTTP 头
        }

        location /hls {#未使用，HLS是苹果公司提出的基于HTTP的流媒体网络传输协议
            types {
                application/vnd.apple.mpegurl m3u8;
                video/mp2t ts;
            }

            root /tmp;
            add_header 'Cache-Control' 'no-cache';
        }

        location /dash {
            root /tmp;
            add_header 'Cache-Control' 'no-cache';
        }

        location /stat {
            #推流播放和录制统计数据的配置

            rtmp_stat all;
            rtmp_stat_stylesheet stat.xsl;
        }

        location /stat.xsl {
            root /var/www/rtmp; #指定 stat.xsl 的位置，需要注意该文件夹下是否存在stat.xsl页面
        }

        #如果需要 JSON 风格的 stat, 不用指定 stat.xsl
        #但是需要指定一个新的配置项 rtmp_stat_format

        #location /stat {
        #    rtmp_stat all;
        #    rtmp_stat_format json;
        #}

        location /control {
            rtmp_control all; #rtmp 控制模块的配置
        }
    }
}

rtmp_auto_push on;  #因为Nginx可能开启多个子进程，这个选项表示推流时，媒体流会发布到多个子进程
rtmp_auto_push_reconnect 1s;
rtmp_socket_dir /tmp;  #多个子进程情况下，推流时，最开始只有一个子进程在竞争中接收到数据，然后它再relay给其他子进程，他们之间通过 socket传输数据，这个选项表示unix domain socket的路径

rtmp {
    out_queue           4096;
    out_cork            8;
    max_streams         128;
    timeout             15s;
    drop_idle_publisher 15s;

    log_interval 5s; #log 模块在 access.log 中记录日志的间隔时间，对调试非常有用
    log_size     1m; #log 模块用来记录日志的缓冲区大小

    server {
        listen 1935;
        server_name www.test.*; #用于虚拟主机名后缀通配

        application camera {
            live on; #开启直播直播模式，一对多广播
            gop_cache on; #打开 GOP 缓存，减少首屏等待时间
            
             #录播配置
             record all;         #启用录制所有信息，包括视频和声音（有其他可选项，参考文档）
             record_path /data/video; #flv录制文件存放地址。注意该目录的所有者要是nginx，或者777权限
             record_max_size 300M;   #控制文件的大小，每个文件到了300m后，结合下面的参数新创建一个文件继续保存
             record_unique on;       #在文件名后加上unlix时间戳，防止重复写同一文件造成视频丢失（默认是关闭的）
        }
    }
}
```

#### ffmpeg

##### 推流

```
./ffmpeg -rtsp_transport tcp -i "rtsp://jijian:JJ*db2022@192.168.215.23:554/Streaming/Channels/701?transportmode=multicast"  -s 320x240   -vcodec copy -f flv  "rtmp://192.168.215.53:1935/camera/701" 
```

### 正向代理

Nginx本身不支持HTTPS正向代理，需要安装ngx_http_proxy_connect_module模块后才可以支持https正向代理

1. git clone https://github.com/chobits/ngx_http_proxy_connect_module.git
2. 确认Nginx版本及对应的patch  https://github.com/chobits/ngx_http_proxy_connect_module?spm=a2c4e.10696291.0.0.668319a4yTdxw4
3. patch -p1 < ngx_http_proxy_connect_module本地路径/对应版本patch
4. 其他步骤直接搜

### 问题

###### nginx错误

invalid PID number "" in "/usr/local/nginx/logs/nginx.pid"

解决： /usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf