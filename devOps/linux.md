## 查看各种东西



## linux下的文件结构

/bin 二进制可执行命令
/dev 设备特殊文件
/etc 系统管理和配置文件
/etc/rc.d 启动的配置文件和脚本
/home 用户主目录的基点，比如用户user的主目录就是/home/user，可以用~user表示
/lib 标准程序设计库，又叫动态链接共享库，作用类似windows里的.dll文件
/sbin 系统管理命令，这里存放的是系统管理员使用的管理程序
/tmp 公用的临时文件存储点
/root 系统管理员的主目录（呵呵，特权阶级）
/mnt 系统提供这个目录是让用户临时挂载其他的文件系统。
/lost+found 这个目录平时是空的，系统非正常关机而留下“无家可归”的文件（windows下叫什么.chk）就在这里
/proc 虚拟的目录，是系统内存的映射。可直接访问这个目录来获取系统信息。
/var 某些大文件的溢出区，比方说各种服务的日志文件
/usr 最庞大的目录，要用到的应用程序和文件几乎都在这个目录。其中包含：
/usr/x11r6 存放x window的目录
/usr/bin 众多的应用程序
/usr/sbin 超级用户的一些管理程序
/usr/doc linux文档
/usr/include linux下开发和编译应用程序所需要的头文件
/usr/lib 常用的动态链接库和软件包的配置文件
/usr/man 帮助文档
/usr/src 源代码，linux内核的源代码就放在/usr/src/linux里
/usr/local/bin 本地增加的命令
/usr/local/lib 本地增加的库根文件系统

通常情况下，根文件系统所占空间一般应该比较小，因为其中的绝大部分文件都不需要经常改动，而且包括严格的文件和一个小的不经常改变的文件系统不容易损坏。除了可能的一个叫/ v m l i n u z标准的系统引导映像之外，根目录一般不含任何文件。所有其他文件在根文件系统的子目录中。

### 详解

1. /bin目录
   / b i n目录包含了引导启动所需的命令或普通用户可能用的命令(可能在引导启动后)。这些命令都是二进制文件的可执行程序( b i n是b i n a r y - -二进制的简称)，多是系统中重要的系统文件。
1. /sbin目录
   / s b i n目录类似/bin ，也用于存储二进制文件。因为其中的大部分文件多是系统管理员使用的基本的系统程序，所以虽然普通用户必要且允许时可以使用，但一般不给普通用户使用。
1. /etc目录
   / e t c目录存放着各种系统配置文件，其中包括了用户信息文件/ e t c / p a s s w d，系统初始化文件/ e t c / r c等。l i n u x正是*这些文件才得以正常地运行。
1. /root目录
   /root 目录是超级用户的目录。
1. /lib目录
   / l i b目录是根文件系统上的程序所需的共享库，存放了根文件系统程序运行所需的共享文
   件。这些文件包含了可被许多程序共享的代码，以避免每个程序都包含有相同的子程序的副
   本，故可以使得可执行文件变得更小，节省空间。
1. /lib/modules 目录
   /lib/modules 目录包含系统核心可加载各种模块，尤其是那些在恢复损坏的系统时重新引
   导系统所需的模块(例如网络和文件系统驱动)。
1. /dev目录
   / d e v目录存放了设备文件，即设备驱动程序，用户通过这些文件访问外部设备。比如，用户可以通过访问/ d e v / m o u s e来访问鼠标的输入，就像访问其他文件一样。
1. /tmp目录
   /tmp 目录存放程序在运行时产生的信息和数据。但在引导启动后，运行的程序最好使用 / v a r / t m p来代替/tmp ，因为前者可能拥有一个更大的磁盘空间。
1. /boot目录
   / b o o t目录存放引导加载器(bootstrap loader)使用的文件，如l i l o，核心映像也经常放在这里，而不是放在根目录中。但是如果有许多核心映像，这个目录就可能变得很大，这时使用单独的
   文件系统会更好一些。还有一点要注意的是，要确保核心映像必须在i d e硬盘的前1 0 2 4柱面内。
1. /mnt目录
   / m n t目录是系统管理员临时安装( m o u n t )文件系统的安装点。程序并不自动支持安装到
   /mnt 。/mnt 下面可以分为许多子目录，例如/mnt/dosa 可能是使用m s d o s文件系统的软驱，
   而/mnt/exta 可能是使用e x t 2文件系统的软驱，/mnt/cdrom 光驱等等。
1. /proc, /usr,/var,/home目录
   其他文件系统的安装点。

下面详细介绍：

#### /etc文件系统

/etc 目录包含各种系统配置文件，下面说明其中的一些。其他的你应该知道它们属于哪个程序，并阅读该程序的m a n页。许多网络配置文件也在/etc 中。

1. /etc/rc或/etc/rc.d或/etc/rc?.d
   启动、或改变运行级时运行的脚本或脚本的目录。
1. /etc/passwd
   用户数据库，其中的域给出了用户名、真实姓名、用户起始目录、加密口令和用户的其他信息。
1. /etc/fdprm
   软盘参数表，用以说明不同的软盘格式。可用setfdprm 进行设置。更多的信息见s e t f d p r m的帮助页。
1. /etc/fstab
   指定启动时需要自动安装的文件系统列表。也包括用swapon -a启用的s w a p区的信息。
1. /etc/group
   类似/etc/passwd ，但说明的不是用户信息而是组的信息。包括组的各种数据。
1. /etc/inittab
   init 的配置文件。
1. /etc/issue
   包括用户在登录提示符前的输出信息。通常包括系统的一段短说明或欢迎信息。具体内容由系统管理员确定。
1. /etc/magic
   “f i l e”的配置文件。包含不同文件格式的说明，“f i l e”基于它猜测文件类型。
1. /etc/motd
   m o t d是message of the day的缩写，用户成功登录后自动输出。内容由系统管理员确定。常用于通告信息，如计划关机时间的警告等。
1. /etc/mtab
   当前安装的文件系统列表。由脚本( s c r i t p )初始化，并由mount 命令自动更新。当需要一个当前安装的文件系统的列表时使用(例如df 命令)。
1. /etc/shadow
   在安装了影子( s h a d o w )口令软件的系统上的影子口令文件。影子口令文件将/ e t c / p a s s w d文件中的加密口令移动到/ e t c / s h a d o w中，而后者只对超级用户( r o o t )可读。这使破译口令更困难，以此增加系统的安全性。
1. /etc/login.defs
   l o g i n命令的配置文件。
1. /etc/printcap
   类似/etc/termcap ，但针对打印机。语法不同。
1. /etc/profile 、/ e t c / c s h . l o g i n、/etc/csh.cshrc
   登录或启动时b o u r n e或c shells执行的文件。这允许系统管理员为所有用户建立全局缺省环境。
1. /etc/securetty
   确认安全终端，即哪个终端允许超级用户( r o o t )登录。一般只列出虚拟控制台，这样就不可能(至少很困难)通过调制解调器( m o d e m )或网络闯入系统并得到超级用户特权。
1. /etc/shells
   列出可以使用的s h e l l。chsh 命令允许用户在本文件指定范围内改变登录的s h e l l。提供一台机器f t p服务的服务进程ftpd 检查用户s h e l l是否列在/etc/shells 文件中，如果不是，将不允许该用户登录。
1. /etc/termcap
   终端性能数据库。说明不同的终端用什么“转义序列”控制。写程序时不直接输出转义序列(这样只能工作于特定品牌的终端)，而是从/etc/termcap 中查找要做的工作的正确序列。这样，多数的程序可以在多数终端上运行。

#### /dev文件系统

/dev 目录包括所有设备的设备文件。设备文件用特定的约定命名，这在设备列表中说明。
设备文件在安装时由系统产生，以后可以用/dev/makedev 描述。/ d e v / m a k e d e v.local 是系统管理员为本地设备文件(或连接)写的描述文稿(即如一些非标准设备驱动不是标准makedev 的一部分)。下面简要介绍/ d e v下一些常用文件。

1. /dev/console
   系统控制台，也就是直接和系统连接的监视器。
1. /dev/hd
   i d e硬盘驱动程序接口。如： / d e v / h d a指的是第一个硬盘， h a d 1则是指/ d e v / h d a的第一个
   分区。如系统中有其他的硬盘，则依次为/ d e v / h d b、/ d e v / h d c、. . . . . .；如有多个分区则依次为
   h d a 1、h d a 2 . . . . . .
1. /dev/sd
   s c s i磁盘驱动程序接口。如有系统有s c s i硬盘，就不会访问/ d e v / h a d，而会访问/ d e v / s d a。
1. /dev/fd
   软驱设备驱动程序。如： / d e v / f d 0指系统的第一个软盘，也就是通常所说的a：盘，
   / d e v / f d 1指第二个软盘，. . . . . .而/ d e v / f d 1 h 1 4 4 0则表示访问驱动器1中的4 . 5高密盘。
1. /dev/st
   s c s i磁带驱动器驱动程序。
1. /dev/tty
   提供虚拟控制台支持。如： / d e v / t t y 1指的是系统的第一个虚拟控制台， / d e v / t t y 2则是系统
   的第二个虚拟控制台。
1. /dev/pty
   提供远程登陆伪终端支持。在进行te l n e t登录时就要用到/ d e v / p t y设备。
1. /dev/ttys
   计算机串行接口，对于d o s来说就是“ c o m 1”口。
1. /dev/cua
   计算机串行接口，与调制解调器一起使用的设备。
1. /dev/null
   “黑洞”，所有写入该设备的信息都将消失。例如：当想要将屏幕上的输出信息隐藏起来
   时，只要将输出信息输入到/ d e v / n u l l中即可。

#### /usr文件系统

/usr 是个很重要的目录，通常这一文件系统很大，因为所有程序安装在这里。/usr 里的所有文件一般来自linux发行版(distribution)；本地安装的程序和其他东西在/usr/local 下，因为这样可以在升级新版系统或新发行版时无须重新安装全部程序。/usr 目录下的许多内容是
可选的，但这些功能会使用户使用系统更加有效。/usr可容纳许多大型的软件包和它们的配置
文件。下面列出一些重要的目录(一些不太重要的目录被省略了)。

1. /usr/x11r6
   包含x wi n d o w系统的所有可执行程序、配置文件和支持文件。为简化x的开发和安装，
   x的文件没有集成到系统中。x wi n d o w系统是一个功能强大的图形环境，提供了大量的图形
   工具程序。用户如果对microsoft wi n d o w s或m a c h i n t o s h比较熟悉的话，就不会对x wi n d o w系统感到束手无策了。
1. /usr/x386
   类似/ u s r / x 11r6 ，但是是专门给x 11 release 5的。
1. /usr/bin
   集中了几乎所有用户命令，是系统的软件库。另有些命令在/bin 或/usr/local/bin 中。
1. /usr/sbin
   包括了根文件系统不必要的系统管理命令，例如多数服务程序。
1. /usr/man、/ u s r / i n f o、/ u s r / d o c
   这些目录包含所有手册页、g n u信息文档和各种其他文档文件。每个联机手册的“节”
   都有两个子目录。例如： / u s r / m a n / m a n 1中包含联机手册第一节的源码(没有格式化的原始文
   件)，/ u s r / m a n / c a t 1包含第一节已格式化的内容。l联机手册分为以下九节：内部命令、系统调
   用、库函数、设备、文件格式、游戏、宏软件包、系统管理和核心程序。
1. /usr/include
   包含了c语言的头文件，这些文件多以. h结尾，用来描述c语言程序中用到的数据结构、
   子过程和常量。为了保持一致性，这实际上应该放在/usr/lib 下，但习惯上一直沿用了这个名
   字。
1. /usr/lib
   包含了程序或子系统的不变的数据文件，包括一些s i t e - w i d e配置文件。名字l i b来源于库
   (library); 编程的原始库也存在/usr/lib 里。当编译程序时，程序便会和其中的库进行连接。也
   有许多程序把配置文件存入其中。
1. /usr/local
   本地安装的软件和其他文件放在这里。这与/ u s r很相似。用户可能会在这发现一些比较大
   的软件包，如t e x、e m a c s等。

#### var文件系统

/var 包含系统一般运行时要改变的数据。通常这些数据所在的目录的大小是要经常变化或扩充的。原来/var目录中有些内容是在/usr中的，但为了保持/usr目录的相对稳定，就把那些需要经常改变的目录放到/var中了。每个系统是特定的，即不通过网络与其他计算机共享。
下面列出一些重要的目录(一些不太重要的目录省略了)。

1. /var/catman
   包括了格式化过的帮助( m a n )页。帮助页的源文件一般存在/ u s r / m a n / m a n中；有些m a n页
   可能有预格式化的版本，存在/ u s r / m a n / c a t中。而其他的m a n页在第一次看时都需要格式化，
   格式化完的版本存在/var/man 中，这样其他人再看相同的页时就无须等待格式化了。
   (/var/catman 经常被清除，就像清除临时目录一样。)
1. /var/lib
   存放系统正常运行时要改变的文件。
1. /var/local
   存放/usr/local 中安装的程序的可变数据(即系统管理员安装的程序)。注意，如果必要，
   即使本地安装的程序也会使用其他/var 目录，例如/var/lock 。
1. /var/lock
   锁定文件。许多程序遵循在/var/lock 中产生一个锁定文件的约定，以用来支持他们正在
   使用某个特定的设备或文件。其他程序注意到这个锁定文件时，就不会再使用这个设备或文
   件。
1. /var/log
   各种程序的日志( l o g )文件，尤其是login (/var/log/wtmp log纪录所有到系统的登录和注
   销) 和syslog (/var/log/messages 纪录存储所有核心和系统程序信息)。/var/log 里的文件经常不
   确定地增长，应该定期清除。
1. /var/run
   保存在下一次系统引导前有效的关于系统的信息文件。例如， /var/run/utmp 包含当前登
   录的用户的信息。
1. /var/spool
   放置“假脱机( s p o o l )”程序的目录，如m a i l、n e w s、打印队列和其他队列工作的目录。每
   个不同的s p o o l在/var/spool 下有自己的子目录，例如，用户的邮箱就存放在/var/spool/mail 中。
1. /var/tmp
   比/tmp 允许更大的或需要存在较长时间的临时文件。
   注意系统管理员可能不允许/var/tmp 有很旧的文件。

#### /proc文件系统

/proc 文件系统是一个伪的文件系统，就是说它是一个实际上不存在的目录，因而这是一个非常特殊的目录。它并不存在于某个磁盘上，而是由核心在内存中产生。这个目录用于提供关于系统的信息。下面说明一些最重要的文件和目录(/proc 文件系统在proc man页中有更详细的说明)。

1. /proc/x
   关于进程x的信息目录，这一x是这一进程的标识号。每个进程在/proc 下有一个名为自
   己进程号的目录。
1. /proc/cpuinfo
   存放处理器( c p u )的信息，如c p u的类型、制造商、型号和性能等。
1. /proc/devices
   当前运行的核心配置的设备驱动的列表。
1. /proc/dma
   显示当前使用的d m a通道。
1. /proc/filesystems
   核心配置的文件系统信息。
1. /proc/interrupts
   显示被占用的中断信息和占用者的信息，以及被占用的数量。
1. /proc/ioports
   当前使用的i / o端口。
1. /proc/kcore
   系统物理内存映像。与物理内存大小完全一样，然而实际上没有占用这么多内存；它仅
   仅是在程序访问它时才被创建。(注意：除非你把它拷贝到什么地方，否则/proc 下没有任何
   东西占用任何磁盘空间。)
1. /proc/kmsg
   核心输出的消息。也会被送到s y s l o g。
1. /proc/ksyms
   核心符号表。
1. /proc/loadavg
   系统“平均负载”； 3个没有意义的指示器指出系统当前的工作量。
1. /proc/meminfo
   各种存储器使用信息，包括物理内存和交换分区( s w a p )。
1. /proc/modules
   存放当前加载了哪些核心模块信息。
1. /proc/net
   网络协议状态信息。
1. /proc/self
   存放到查看/proc 的程序的进程目录的符号连接。当2个进程查看/proc 时，这将会是不同
   的连接。这主要便于程序得到它自己的进程目录。
1. /proc/stat
   系统的不同状态，例如，系统启动后页面发生错误的次数。
1. /proc/uptime
   系统启动的时间长度。
1. /proc/version
   核心版本

## 磁盘相关的几个常用命令

[https://daemon369.github.io/linux/2018/01/06/01-get-disks-info-in-linux](https://daemon369.github.io/linux/2018/01/06/01-get-disks-info-in-linux)
df -h：查看磁盘占用情况
df -T：查看所有磁盘的文件系统类型(type)
fdisk -l：查看所有被系统识别的磁盘
mount -t type device dir：挂载device到dir

#### scp传输文件

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