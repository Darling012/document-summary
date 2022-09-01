##### 背景

玻纤内网分为192和172网段，192不能访问外网，生产环境部署在192网段，所以在172.16.110.225部署Nginx正向代理实现访问外网。

##### Nginx部署

1. Nginx本身不支持HTTPS正向代理，需要安装ngx_http_proxy_connect_module模块后才可以支持https正向代理
2. 参考https://www.infoq.cn/article/taujwgln6d_6qls6yj6s

##### 后端改动

后端目前需要连接外网的需求为企业微信相关，有两块

1. 使用WxJava工具包的部分在WxCpConfiguration中配置代理
2. 使用RestTemplate部分通过RestTemplate配置代理

