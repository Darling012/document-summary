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