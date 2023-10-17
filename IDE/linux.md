# linux 

## 磁盘相关的几个常用命令

[https://daemon369.github.io/linux/2018/01/06/01-get-disks-info-in-linux](https://daemon369.github.io/linux/2018/01/06/01-get-disks-info-in-linux)
df -h：查看磁盘占用情况
df -T：查看所有磁盘的文件系统类型(type)
fdisk -l：查看所有被系统识别的磁盘
mount -t type device dir：挂载device到dir

## scp传输文件

scp /home/work/source. txt work@192.168.0.10:/home/work/   # 把本地的 source. txt 文件拷贝到 192.168.0.10 机器上的/home/work 目录下

scp work@192.168.0.10:/home/work/source. txt /home/work/   # 把 192.168.0.10 机器上的 source. txt 文件拷贝到本地的/home/work 目录下

scp work@192.168.0.10:/home/work/source. txt work@192.168.0.11:/home/work/   # 把 192.168.0.10 机器上的 source. txt 文件拷贝到 192.168.0.11 机器的/home/work 目录下

scp -r /home/work/sourcedir work@192.168.0.10:/home/work/   # 拷贝文件夹，加-r 参数

## 查看文件权限

查看linux文件的权限：ls -l 文件名称

查看linux文件夹的权限：ls -ld 文件夹名称（所在目录）

在root用户登录的情况，赋予opt目录给liuhai这个用户权限 示例代码：

```sh
# 将目录/opt 及其下面的所有文件、子目录的文件主改成 liuhai
chown -R liuhai:liuhai /opt
#给目录opt设置权限
chmod 760 /opt
```

## firewall

1. 安装sudo yum install firewalld
2. firewalld的基本使用
   1. 启动： service firewalld start
   2. 查看状态： service firewalld  status
   3. 停止： service firewalld disable
   4. 禁用： service firewalld stop
3. 配置firewalld-cmd
   查看版本： firewall-cmd --version
   查看帮助： firewall-cmd --help
   显示状态： firewall-cmd –state
   列出所有的区域：firewall-cmd --get-zones
   列出默认区域：firewall-cmd --get-default-zone
   列出所有区域配置： firewall-cmd --list-all-zone
   查看所有打开的端口： firewall-cmd --zone=public --list-ports
   查看规则：iptables -L -n
   更新防火墙规则： firewall-cmd --reload
   查看区域信息:  firewall-cmd --get-active-zones
   查看指定接口所属区域： firewall-cmd --get-zone-of-interface=eth0
4. 添加服务
   firewall-cmd –add-service=ssh
   firewall-cmd –query-service=ssh
   firewall-cmd –remove-service=ssh
5. 那怎么开启一个端口呢
   添加
   firewall-cmd --zone=public --add-port=80/tcp
   重新载入
   firewall-cmd --reload
   查看
   firewall-cmd --zone=public --query-port=80/tcp
   删除
   firewall-cmd --zone=public --remove-port=80/tcp



添加firewall-cmd --zone=public --add-port=8091/tcp --permanent （--permanent永久生效，没有此参数重启后失效）重新载入firewall-cmd --reload查看firewall-cmd --zone=public --query-port=8091/tcp删除firewall-cmd --zone=public --remove-port=80/tcp --permanent端口列表firewall-cmd --list-ports

47.95.202.187 -----金融 ----root----l@ngy@KEJI!*&

## 内核版本与系统版本

1. 内核版本与系统版本的区别？

    Linux其实就是一个操作系统最底层的核心及其提供的核心工具。Linux也用了很多的GNU相关软件，所以Stallman认为Linux的全名应该称之为GNU/Linux。

很多的商业公司或非营利团体，就将Linux Kernel(含tools)与可运行的软件整合起来，加上自己具有创意的工具程序,这个工具程序可以让用户以光盘/DVD或者透过网络直接安装/管理Linux系统。这个Kernel+Softwares+Tools的可安装程序我们称之为Linux distribution（Linux发行版本）

2. 查看Linux内核版本命令（2种方法）：
   1. cat /proc/version
   2. uname -a

如果有x86_64就是64位的，没有就是32位的

后面是X686或X86_64则内核是64位的，i686或i386则内核是32位的

3. 查看Linux系统版本的命令（3种方法）：
   1. lsb_release -a，即可列出所有版本信息
   2. cat /etc/redhat-release，这种方法只适合Redhat系的Linux
   3. cat /etc/issue，此命令也适用于所有的Linux发行版

## Vim

### Vim 强制保存只读文件的方法

:w !sudo tee %

### 设置vim 永久显示行号

#####  临时显示行号 set number

##### 永久显示行号

需要修改vim配置文件vimrc。如果没有此文件可以创建一个。在启动vim时，当前用户根目录下的vimrc文件会被自动读取，因此一般在当前用户的根目录下创建vimrc文件，即使用下面的命令：  

vim ~./ vimrc

在打开的vimrc文件中最后一行输入：set number ，然后保存退出

#### 解决ubuntu下vi上下左右方向键出现字母，backspace键不能删除字符

方法1：将vimrc档案拷贝到home目录

$cp /etc/vim/vimrc ~/.vimrc

方法2：卸载vim-tiny，安装vim-full

 ubuntu默认安装装的是vim tiny版本，而需要的是vim full版本。执行下面的语句安装vim full版本：

$sudo apt-get remove vim-common

$sudo apt-get install vim

方法3：修改vimrc不适用兼容模式

打开/etc/vim/vimrc，并加入以下内容

$gedit /etc/vim/vimrc

 set nocompatible

 set backspace=2

## 端口号 进程号

##### linux下查看某一端口被哪个进程占用

netstat -tunpl | grep 端口号

##### 根据进程的PID查询对应端口号

netstat -anop|grep pid

## 包管理

##### apt-get

apt-get是一条linux命令，适用于deb包管理式的操作系统，主要用于自动从互联网的软件仓库中搜索、安装、升级、卸载软件或操作系统。

##### Yum

Yum（全称为 Yellow dog Updater, Modified）是一个在Fedora和RedHat以及CentOS中的Shell前端软件包管理器。基于RPM包管理，能够从指定的服务器自动下载RPM包并且安装，可以自动处理依赖性关系，并且一次安装所有依赖的软件包，无须繁琐地一次次下载、安装。

##### Yum和apt-get的区别

[Linux] yum和apt-get用法及区别

一般来说著名的linux系统基本上分两大类：

1.RedHat系列：Redhat、Centos、Fedora等

2.Debian系列：Debian、Ubuntu等

##### RedHat 系列 

1 常见的安装包格式 rpm包,安装rpm包的命令是“rpm -参数” 

2 包管理工具 yum 

3 支持tar包

##### Debian系列 

1 常见的安装包格式 deb包,安装deb包的命令是“dpkg -参数” 

2 包管理工具 apt-get 

3 支持tar包

tar 只是一种压缩文件格式，所以，它只是把文件压缩打包而已。 

rpm 相当于windows中的安装文件，它会自动处理软件包之间的依赖关系。 

优缺点来说，rpm一般都是预先编译好的文件，它可能已经绑定到某种CPU或者发行版上面了。 

tar一般包括编译脚本，你可以在你的环境下编译，所以具有通用性。 

如果你的包不想开放源代码，你可以制作成rpm，如果开源，用tar更方便了。 

tar一般都是源码打包的软件，需要自己解包，然后进行安装三部曲，./configure, make, make install. 来安装软件。

##### Homebrew

linux系统有个让人蛋疼的通病，软件包依赖，好在当前主流的两大发行版本都自带了解决方案，Red hat有yum，Ubuntu有apt-get

神马，你用mac os，不好意Mac os木有类似的东东，泪奔中几经折腾总算找到了第三方支持：Homebrew，Homebrew简称brew，是Mac OSX上的软件包管理工具，能在Mac中方便的安装软件或者卸载软件，可以说Homebrew就是mac下的apt-get、yum神器apt-get是一条linux命令，适用于deb包管理式的操作系统，主要用于自动从互联网的软件仓库中搜索、安装、升级、卸载软件或操作系统。

## 查看网关

netstat -rn

##  [ln 命令](https://www.cnblogs.com/peida/archive/2012/12/11/2812294.html)