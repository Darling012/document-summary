# WebRTC
[零基础快速入门WebRTC：基本概念、关键技术、与WebSocket的区别等-IM开发/专项技术区 - 即时通讯开发者社区!](http://www.52im.net/thread-4184-1-1.html)


### kms

```
docker run -d \
--restart always \
--name kms \
--network host \
kurento/kurento-media-server:6.15
```

测试启动成功

```
curl -i -N \
    -H "Connection: Upgrade" \
    -H "Upgrade: websocket" \
    -H "Host: 127.0.0.1:8888" \
    -H "Origin: 127.0.0.1" \
    http://127.0.0.1:8888/kurento
```

Chrome，再打开一个标签，输入地址：chrome://webrtc-internals/ ，可以查看 WebRTC 的详情

##### 连接播放流程

```java
//1. 新建MediaPipeline对象
        KurentoClient kurentoClient = KurentoClient.create("ws://192.168.216.125:8888/kurento");
        MediaPipeline pipeline = kurentoClient.createMediaPipeline();
        // 2. 新建负责播放的PlayerEndpoint对象
        final PlayerEndpoint playerEndpoint = new PlayerEndpoint.Builder(pipeline,
                                                                         "rtsp://jijian:JJ*db2022@192.168.215.23:554/Streaming/Channels/701?transportmode=multicast").build();
        // 3. 通过KMS开始连接远程媒体
        playerEndpoint.play();
        // 以当前的年月日时分秒作为文件名
        String path = "file:///tmp/test-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".mp4";
        // 实例化录制组件
        RecorderEndpoint recorderEndpoint = new RecorderEndpoint
                .Builder(pipeline, path)
                .withMediaProfile(MediaProfileSpecType.MP4_VIDEO_ONLY)
                .build();
        // 连接到播放组件
        playerEndpoint.connect(recorderEndpoint);
        // 开始录制
        recorderEndpoint.record();
```

##### WebRTC 和 WebSocket 区别和关系
WebRTC 和 WebSocket 是两种不同的通信技术，它们的主要区别在于它们的目的和用途。
WebRTC是一种实时通信技术，主要用于在浏览器之间进行视频和音频通信。它提供了一种方法，可以在不使用中间服务器的情况下在浏览器之间进行P2P通信。
而WebSocket则是一种全双工通信技术，它允许浏览器和服务器在单个TCP连接上进行双向数据通信。它通常用于在浏览器和服务器之间构建实时应用程序，例如聊天应用程序、在线游戏等。
总的来说，WebRTC 可以为浏览器之间的实时通信提供支持，而 WebSocket 则可以在浏览器和服务器之间提供实时通信。

[WebRTC和WebSocket有什么关系和区别？ - 知乎](https://www.zhihu.com/question/424264607)
[[译] WebRTC 与 WebSockets 教程 — Web 端的实时通信 - 掘金](https://juejin.cn/post/7138015673850003493)