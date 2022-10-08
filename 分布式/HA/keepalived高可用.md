### keepalived

###### 安装：

1. 直接安装 

   1.  sudo apt-get update ;  sudo apt-get install keepalived
   2. sudo apt-get install keepalived

2. 编译

   https://keepalived-doc.readthedocs.io/zh_CN/latest/%E5%AE%89%E8%A3%85Keepalived.html



###### apt安装

启动： sudo service keepalived start 

查看状态: sudo systemctl status keepalived

停止：sudo service keepalived stop

 在/etc/keepalived/下的keepalived.conf配置  主

```conf
#添加检测脚本
vrrp_script chk_http_port {
        script "/usr/local/nginx/nginx_check.sh"
        interval 2
        weight 2
}
vrrp_instance VI_1 {
    state MASTER   #主机这里是MASTER 从机是BACKUP
    interface eno1  #网卡
    virtual_router_id 51  # 主、从机的virtual_router_id必须相同
    priority 100   # 主备机取不同的优先级，主机优先级大
    advert_int 1  #心跳检测间隔时间
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
        192.168.99.225   # VRRP 虚拟IP ；可换行输入多个进行绑定
    }
}
```

/usr/local/nginx/nginx_check.sh 脚本内容如下：

```bash
#!/bin/bash
counter=$(ps -C nginx --no-heading|wc -l)
if [ "${counter}" = "0" ]; then
    /usr/local/nginx/sbin/nginx
    sleep 2
    counter=$(ps -C nginx --no-heading|wc -l)
    if [ "${counter}" = "0" ]; then
        systemctl stop keepalived
    fi
fi
```

从

```
! Configuration File for keepalived
vrrp_script chk_http_port {
        script "/usr/local/nginx/nginx_check.sh"
        interval 2   #检测脚本执行间隔时间
        weight 2    #设置当前服务器权重增量
}
vrrp_instance VI_1 {
    state BACKUP
    interface eno1
    virtual_router_id 51
    priority 90
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
        192.168.215.53
    }
}
```

#### 问题
##### 虚拟ip 
尽量跟实际ip按顺序来，否则存在访问不通情况