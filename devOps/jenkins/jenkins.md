

CI：1. 利用 k12 代码中的 jenkins 脚本 (只有 CI，需改造) 2. maven 插件，自动化程度低，需打开本地 docker，再执行任务
CD: 自写 jenkins 脚本

```text
1. docker-compose是每次都上传？若不是，怎么判断更改过了
1. docker-compose外界传入参数，如镜像库地址等，动态生成改变
```


## docker部署

```yaml
version: '3'
services:
  jenkins:
    image: jenkinsci/blueocean:latest
    container_name: jenkins
    group_add:
      - 127
    ports:
      - '9000:8080'
      - '50000:50000'
    volumes:
      - '/usr/local/devops/jenkins/jenkins_home:/var/jenkins_home'
      - '/usr/local/devops/jenkins/settings.xml:/opt/settings.xml'
      - '/usr/local/devops/jenkins/repository:/opt/maven/repository'
      - '/var/run/docker.sock:/var/run/docker.sock'
      - '/etc/localtime:/etc/localtime:ro'
      
    restart: always
```

admin 654321

注意目录授权

更新容器配置：docker container update  --restart=alwaysjenkins 需看此命令支持项

docker-compose stop jenkins && docker-compose rm -f jenkins && docker-compose up -d jenkins

##### maven

> 配置→全局属性→环境变量 jenkins 内安装 maven、docker 在初次使用时才会安装, 触发 maven 安装要建个 maven 任务。 配置 jdk、git、maven 别名和路径（git 和 jdk 本身就存在，maven 选择自动安装就行） maven 全局参数注意配置 path+extra 先进入 jenkins 容器中：docker exec -it jenkins bash 查看容器中的 jdk 路径：echo $JAVA_HOME 查看容器中的 git 路径：which git 改时间 系统管理-> 脚本命令行   System.setProperty ('org.apache.commons.jelly.tags.fmt.timeZone', 'Asia/Shanghai') /var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/maven/conf、


settings.xml

```XML
<?xml version="1.0" encoding="UTF-8"?>

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <localRepository>/opt/maven/repository</localRepository>
  <proxies></proxies> 
  <servers>
    <server>
      <id>deepblue-company</id>
      <username>deepblue</username>
      <password>wvDZghFN6ALGQFUY</password>
    </server>
    <server>
      <id>deepblue-snapshots</id>
      <username>deepblue</username>
      <password>wvDZghFN6ALGQFUY</password>
    </server>
    <server>
      <id>deepblue-releases</id>
      <username>deepblue</username>
      <password>wvDZghFN6ALGQFUY</password>
    </server>
    <server>
      <id>ali-jcr</id>
      <username>admin</username>
      <password>Shenlan2018</password>
    </server>
  </servers>

  <mirrors>
    <mirror>
      <id>aliyun</id>
      <name>aliyun</name>
      <mirrorOf>aliyun</mirrorOf>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    </mirror>

    <mirror>
      <id>deepblue-company</id>
      <name>deepblue-company</name>
      <mirrorOf>deepblue-company</mirrorOf>
      <url>https://nexus.deepblueai.com/repository/maven-public/</url>
    </mirror>
  </mirrors>

  <profiles>
    <profile>
      <id>jdk1.8</id>
      <activation>
        <activeByDefault>true</activeByDefault>
        <jdk>1.8</jdk>
      </activation>
      <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
        <encoding>UTF-8</encoding>
      </properties>
    </profile>
    <profile>
      <id>deepblue-company</id>
      <repositories>
        <repository>
          <id>deepblue-company</id>
          <url>https://nexus.deepblueai.com/repository/maven-public/</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
          </snapshots>
        </repository>
      </repositories>
    </profile>
    <profile>
      <id>aliyun</id>
      <repositories>
        <repository>
          <id>aliyun</id>
          <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
          </snapshots>
        </repository>
      </repositories>
    </profile>

  </profiles>
  <activeProfiles>
     <activeProfile>deepblue-company</activeProfile>
     <activeProfile>aliyun</activeProfile>
  </activeProfiles>

</settings>
```

CI/CD 所需 git jdk 已自带，  maven 容器内部安装，docker 使用宿主机

改为流水线脚本部署，与公司统一。之前脚本式部署方式每个子服务都会将整个项目代码 clone 下来，再进行打包。尤其是 100 上的由分支区分的老项目，造成空间占用太大，100 报空间不足。66 服务器端口占用 8083、50000



#### 插件:

1. Publish Over  SSH 目前下架。 https://plugins.jenkins.io/publish-over-ssh下载到本地离线安装 , 在 name 下有个 Verbose output in console 可打印输出
1. Extended Choice Parameter plugin


### ssh 登录远程节点更新镜像
##### aivideo
``` shell
#!/bin/bash
#rm -rf /Users/a/.m2/repository/com/deepblue/deep-blue-display
#rm -rf /Users/a/.m2/repository/com/deepblue/deep-blue-display-api
cd source/deep-blue-heat-map/deep-blue-heat-map/
DOCKER_HOSTS=tcp://192.168.215.100:2375 mvn clean package -P ${PROFILE} -Ddockerfile.tag=${VERSION} -Ddocker.image.prefix=${imagePrefix} dockerfile:build dockerfile:push

#echo 推送 ${imagePrefix}/deep-blue-gateway-${PROFILE}:${VERSION}
#docker -H tcp://192.168.215.100:2375 push  ${imagePrefix}/deep-blue-gateway-${PROFILE}:${VERSION}
docker rmi   ${imagePrefix}/dbhealth-heat-map-${PROFILE}:${VERSION}
```

##### ailife
```shell
SERVER_NAME_DEMO=${JOB_NAME}
echo "SERVER_NAME_DEMO: $SERVER_NAME_DEMO"
CID=$(docker ps -a | grep -w "$SERVER_NAME_DEMO" | awk '{print $1}')
IID=$(docker images | grep -w "$SERVER_NAME_DEMO" | awk '{print $3}')    
    if [ -n "$CID" ]; then
        echo "存在$SERVER_NAME容器，CID=$CID,删除容器 ..."
        docker rm -f $CID
        echo "$SERVER_NAME容器删除完成..."
    else
        echo "不存在$SERVER_NAME容器..."
    fi
    if [ -n "$IID" ]; then
        echo "存在$SERVER_NAME镜像，IID=$IID"
        docker rmi -f $IID
        echo "镜像删除完成"
    else
        echo "不存在$SERVER_NAME镜像，开始构建镜像"
    fi
# 加载docker镜像
docker load -i /home/docker_images/$SERVER_NAME_DEMO.tar
# 启动镜像
docker run  -m 1024m --name $SERVER_NAME_DEMO -itd --restart=always --net=host $SERVER_NAME_DEMO:$tag
# 删除镜像文件
rm -rf /home/docker_images/$SERVER_NAME_DEMO.tar

```


##### 66 服务器部署

CI/CD 所需 git jdk 已自带，  maven 容器内部安装，docker 使用宿主机

改为流水线脚本部署，与公司统一。之前脚本式部署方式每个子服务都会将整个项目代码 clone 下来，再进行打包。尤其是 100 上的由分支区分的老项目，造成空间占用太大，100 报空间不足。

脚本路径：指定到源码中 grovvy 脚本

全局属性
![](Pasted%20image%2020230214113211.png)


## reference
1. [通过Jenkins构建CI/CD实现全链路灰度](https://mp.weixin.qq.com/s/RATpDB4Gq1IGFncyj7uaGg)