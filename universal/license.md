# license 软件许可

## 背景  
  
当前系统需要部署在客户服务器，存在我们对系统失去控制风险，如连接不上客户内网环境，不利于商务谈判。在系统加入版权许可证书，实现在离线环境对系统使用的有效控制。  
  
## 需求  
  
需要满足我们在连接不上系统时，在证书的有效时间内，客户可以正常使用系统。并且防止客户迁移部署制品、破解等手段突破限制。  
  
1. 获取不同系统机器信息，包括容器化部署时宿主机信息。  
2. 根据部署机器信息颁发证书，使得只有特定机器才可以部署系统。  
3. 在部署认证服务时安装证书，验证当前机器是否允许，防止迁移。  
4. 在用户获取token使用系统前，验证证书是否在有效期。  
  
## 风险  
  
1. 证书有效时间是根据当前系统时间判定，若机器时间并不是客观时间，则存在证书有效时间检验失效情况。  
2. 针对1，我们系统有很多业务需要正确的系统时间，则先不考虑此种情况  
3. 当前为容器化部署，当前机器信息只获取宿主机 CPU 序列号，主板序列号，是否可伪造? PS: windows、Linux 可增加 IP、Mac 等信息校验。  #容器化导致与主机信息不一致 
4. 容器内获取宿主机信息采用 ssh 登录到宿主机后执行命令，这需要宿主机账号密码且此账号有执行命令权限。  
	1. [linux - Can't run dmidecode on docker container - Stack Overflow](https://stackoverflow.com/questions/54068234/cant-run-dmidecode-on-docker-container)
5. 针对 3,4 实操比较麻烦，目前去除验证主机信息，只通过验证证书有效期实现。  
  
  
###  生成公私钥  
  
依赖jdk的keytool命令  
  
1. 生成私钥库  
  
   ```sh  
   keytool -genkeypair -keysize 1024 -validity 3650 -alias "privateKey" -keystore "privateKeys.keystore" -storepass "deepbluejn1234" -keypass "jnaivideo1234" -dname "CN=aivideo, OU=deepblue, O=deepblue, L=JN, ST=SD, C=CN"  
   ```  
   **命令参数说明：**  
  
| **参数**  | **说明**               | **备注**                                                     |  
| --------- | ---------------------- | ------------------------------------------------------------ |  
| keysize   | 生成密钥长度           | 请务必设为1024                                               |  
| validity  | 密钥有效期（单位：天） | 生成证书时证书有效期不应大于密钥有效期                       |  
| alias     | 私钥别名               |                                                              |  
| keystore  | 生成私钥库的文件名     |                                                              |  
| storepass | 私钥库口令             | 必须为小写字母和数字组合，否则程序校验不通过                 |  
| keypass   | 密钥口令               | 保持与私钥库口令一致，否则解密证书会出现问题                 |  
| dname     | 指定证书拥有者信息     | CN=名字与姓氏,OU=组织单位名称,O=组织名称,L=城市或区域名称,ST=州或省份名称,C=单位的两字母国家代码 |  
  
2. 导出公钥证书文件  
  
```shell  
keytool -exportcert -alias "privateKey" -keystore "privateKeys.keystore" -storepass "deepbluejn1234" -file "certfile.cer"  
```  
**命令参数说明：**  
  
|**参数**|**说明**|**备注**|  
|-|-|-|  
|alias|私钥别名||  
|keystore|上一步生成的私钥库的文件名||  
|storepass|私钥库口令|上一步设置的私钥库口令|  
|file|导出后的的公钥证书文件名||  
  
3. 将公钥证书导入公钥库  
 ```sh  
keytool -import -alias "publicCert" -file "certfile.cer" -keystore "publicCerts.keystore" -storepass "deepbluejn1234"  
 ```  
**命令参数说明：**  
  
|**参数**|**说明**|**备注**|  
|-|-|-|  
|alias|公钥别名||  
|file|要导入的公钥文件||  
|keystore|要导入的公钥库的文件名||  
|storepass|公钥库口令，保持与第一步设置的私钥库口令一致，否则证书安装将不通过||

##### reference
1. [SpringBoot -- 软件许可（License）证书生成+验证+应用完整流程](https://blog.csdn.net/Appleyk/article/details/101530203)
2. [Spring Boot项目中使用 TrueLicense 生成和验 | zifangsky的个人博客](https://www.zifangsky.cn/1277.html)
3. [Java truelicense 实现License授权许可和验证](https://blog.csdn.net/Genmer/article/details/118574390)
4. [LicenseDemo: 在基于Spring的项目中使用 TrueLicense 生成和验证License（服务器许可）的示例代码](https://gitee.com/zifangsky/LicenseDemo?_from=gitee_search)