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

