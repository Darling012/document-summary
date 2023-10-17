# typora图床

工具
--

**`Typora`**: Markdown 工具。写 Markdown 文件的神器，简洁、方便、免费。

**`PicGo`** 开源的图片管理工具，可以自己上传图片到各种图床。

**`SM.MS`**: 免费、够用。可以自己用，咱们项目分享时用的http://10.16.32.202上的minio。

### 搭建步骤

Typora 支持图片上传功能是最近更新的，所以需要你的版本在**最新版本** (0.9.86 版本)。

文件--》偏好设置--》图像

![image-20210401113030334](http://10.16.32.202:9000/projectimg/image-20210401113030334.png)

PicGo直接github下载最新版本就行，我也会传上来。

PicGo需要安装minio插件才能支持上传到minio。插件安装完配置如下。当前图片都在projectimg桶中，是否需要换桶或者建文件夹，大哥们再商量吧。

![image-20210330144546980](http://10.16.32.202:9000/projectimg/image-20210330144546980.png)

问题出在了PicGo安装picgo-plugin-minio插件上，假若你在上图插件设置中能够成功安装minio插件，那就不用往下看了。我的是不行，

直接解决方案是将我提供的picgo-plugin-minio源码文件夹放在C:\Users\XXX\AppData\Roaming\picgo\node_modules下。然后在C:\Users\XXX\AppData\Roaming\picgo\执行npm i picgo-plugin-minio，

这句主要是让项目packge.json中依赖这个插件模块。这也需要你的机器有Node环境。



##### **解决过程：**

直接安装的话可以安装成功，但是加载模块报错，查看日志文件后是找不到插件模块的index.js入口文件，然后查看C:\Users\XXX\AppData\Roaming\picgo下的node 模块依赖包发现代码不全，然后就直接去github下载源码直接install  save本地再依赖进来。其实直接复制到node_modules下就行。

