### push的本质
1.  `push` 是把当前的分支上传到远程仓库，并把这个 `branch` 的路径上的所有 `commit`s 也一并上传。
2.  `push` 的时候，如果当前分支是一个本地创建的分支，需要指定远程仓库名和分支名，用 `git push origin branch_name` 的格式，而不能只用 `git push`；或者可以通过 `git config` 修改 `push.default` 来改变 `push` 时的行为逻辑。
3.  `push` 的时候之后上传当前分支，并不会上传 `HEAD`；远程仓库的 `HEAD` 是永远指向默认分支（即 `master`）的。
### merge 的本质
1.  `merge` 的含义：从两个 `commit`「分叉」的位置起，把目标 `commit` 的内容应用到当前 `commit`（`HEAD` 所指向的 `commit`），并生成一个新的 `commit`；
2.  `merge` 的适用场景：
1.  单独开发的 `branch` 用完了以后，合并回原先的 `branch`；
2.  `git pull`  的内部自动操作。
3.  `merge` 的三种特殊情况：
1.  冲突
1.  原因：当前分支和目标分支修改了同一部分内容，Git 无法确定应该怎样合并；
2.  应对方法：解决冲突后手动 `commit`。
2.  `HEAD` 领先于目标 `commit`：Git 什么也不做，空操作；
3.  `HEAD` 落后于目标 `commit`：fast-forward。
### `reset` 指令的本质
移动 `HEAD` 以及它所指向的 `branch` 的位置。同时，介绍了 `reset` 的三种参数：
1.  `--hard`：重置位置的同时，清空工作目录的所有改动；
2.  `--soft`：重置位置的同时，保留工作目录和暂存区的内容，并把重置 `HEAD` 的位置所导致的新的文件差异放进暂存区。
3.  `--mixed`（默认）：重置位置的同时，保留工作目录的内容，并清空暂存区。
除了上面这三种参数，还有一些没有列出的较为不常用的参数；另外除了我讲的功能外，`reset` 其实也还有一些别的功能和用法。不过 `reset` 最关键的功能、用法和本质原理就是上面这些了，想了解更多的话，可以去官网了解一下。
### checkout的本质
`checkout` 本质上的功能其实是：**签出（ checkout ）指定的** `**commit**`**。**所以你不止可以切换 `branch`，也可以直接指定 `commit` 作为参数，来把 `HEAD` 移动到指定的 `commit`。
`**git checkout branch 名**` **的本质，其实是把** `**HEAD**` **指向指定的** `**branch**`**，然后签出这个** `**branch**` **所对应的** `**commit**` **的工作目录**。
#### checkout 和 reset 的不同
`checkout` 和 `reset` 都可以切换 `HEAD` 的位置，它们除了有许多细节的差异外，最大的区别在于：`**reset**` **在移动** `**HEAD**` **时会带着它所指向的** `**branch**` **一起移动，而** `**checkout**` **不会。**当你用 `checkout` 指向其他地方的时候，`HEAD` 和 它所指向的 `branch` 就自动脱离了。
事实上，`checkout` 有一个专门用来只让 `HEAD` 和 `branch` 脱离而不移动 `HEAD` 的用法：
git checkout --detach
### rebase 的本质
给你的 `commit` 序列重新设置基础点（也就是父 `commit`）。展开来说就是，**把你指定的** `**commit**` **以及它所在的** `**commit**` **串，以指定的目标** `**commit**` **为基础，依次重新提交一次**。原来的还存在。不要在master rebase






## gerrit
ssh clone 不成功
1. [windows 10 - Get-Service, Get-Module, Get-ChildItem are not being recognized in PowerShell - Super User](https://superuser.com/questions/1453691/get-service-get-module-get-childitem-are-not-being-recognized-in-powershell)
2. [Windows下使用ssh-add报错 Error connecting to agent: No such file or directory - 进击的Milo - 博客园](https://www.cnblogs.com/attackingmilo/p/Windows-ssh-add-error.html)
3. [Permission denied (publickey)解决办法-阿里云开发者社区](https://developer.aliyun.com/article/1007151)
4. [解决github Permission denied (publickey)问题 - 简书](https://www.jianshu.com/p/f22d02c7d943)


## 其他版本控制
1. Java [一台机器下，多个Java版本的粗放与精细管理 - 腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/2162731)
2. nodejs nvm