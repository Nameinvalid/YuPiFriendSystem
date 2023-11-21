## YuPiFriendSystem

复习用项目

## 服务器部署

### 配置nginx

- 参考文章（https://zhuanlan.zhihu.com/p/425790769）

```bash
# mkdir ngnix
//创建ngnix单独的文件夹
# cd ngnix
# curl -o nginx-1.24.0.tar.gz http://nginx.org/download/nginx-1.24.0.tar.gz
//linux,下载nginx命令
# tar -zxvf nginx-1.24.0.tar.gz 
//解压nginx压缩包，其中-zxvf是可视化，能够显示下载进度，如果不想显示就把V去掉
# ll -ah 
//查看当前目录下所有文件的详细信息
# cd nginx-1.24.0
# ./configure //查看nginx的配置
------如果没有prce-------
# yum install pcre pcre-devel -y 
//安装prce
------如果有prce，--------
结果如下：
Configuration summary
  + using system PCRE2 library
  + OpenSSL library is not used
  + using system zlib library
# yum install openssl openssl-devel -y
//安装 OpenSSL
# ./configure --with-http_ssl_module --with-http_v2_module --with-stream
//配置OpenSSL，是的Https生效
结果如下：
Configuration summary
  + using system PCRE2 library
  + using system OpenSSL library
  + using system zlib library
# make
//编译
# make install
//安装
# ls /usr/local/nginx/sbin/nginx
//跳转到nginx可执行文件
# vim /etc/profile
//在最后一行添加 export PATH=$PATH:/usr/local/nginx/sbin
# source /etc/profile
//刷新配置文件，重新激活配置文件，nginx才可以用
# netstat -ntlp
//查看端口占用
```

### 前端部署

``` bash
//首先使用build进行打包，把dist.zip上传到相应的目录
# unzip dist.zip -d user-center-front
//将dist.zip解压到user-center-front中
# cd nginx-1.24.0
//进入到nginx-1.24.0中
# cd conf
# vim nginx.conf
//对nginx的配置文件进行修改
//在root中添加指定目录/root/services/user-center-front;
//在最上面添加权限user root;
# nginx -s reload
//对ngnix的配置进行保存并进行重启
# cp nginx.conf /usr/local/nginx/conf
//由于nginx配置入口在/usr/local/nginx/conf中，所以进行替换
# nginx -s reload
//替换完成，保存并重启
```

### 后端部署

```bash
# yum install -y java-1.8.0-openjdk*
//安装java 1.8 jdk
//将后端jar包导入
# java -jar ./包名 --spring.profiles.active=prod
//按照生产环境运行
# nohup java -jar ./包名 --spring.profiles.active=prod &
//在后台运行
# cd /usr/local/nginx/conf
# vim nginx.conf
//修改nginx.conf文件
添加反向代理服务器代码
location ^~ /api/ {
    proxy_pass http://127.0.0.1:8080/api/;

    add_header 'Access-Control-Allow-Origin' $http_origin;
    add_header 'Access-Control-Allow-Credentials' 'true';
    add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
    add_header Access-Control-Allow-Headers '*';
    if ($request_method = 'OPTIONS') {
      add_header 'Access-Control-Allow-Credentials' 'true';
      add_header 'Access-Control-Allow-Origin' $http_origin;
      add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS';
      add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range';
      add_header 'Access-Control-Max-Age' 1728000;
      add_header 'Content-Type' 'text/plain; charset=utf-8';

      add_header 'Content-Length' 0;
      return 204;
    }
  }
//使得nignx能够代发到相应的java后端服务
```

### MySQL数据库部署

- 安装mysql教程:https://blog.csdn.net/qq_39150374/article/details/112471108
- mysql远程访问，设置密码，绑定ipv4:https://blog.csdn.net/qq_45950024/article/details/122487644

```bash
# yum install mysql80-community-release-el8-1.noarch.rpm
//下载安装mysql数据库
# yum repolist enabled | grep "mysql.*-community.*"
//查看mysql是否安装成功
# yum install mysql-community-server 
//这一步的时候可能很多人安装不上，因为是yum安装库的问题，错误（Error: GPG check FAILED），可以将--nogpgcheck添加到后面：
# yum install mysql-community-server --nogpgcheck
# service mysqld status
//启动mysql
# grep 'temporary password' /var/log/mysqld.log
//显示mysql随机生成的密码
# mysql -u root -p
//输入生成的密码
# use mysql;
# ALTER USER 'root'@'localhost' IDENTIFIED BY '新密码';
//为了安全不建议修改密码策略
# flush privileges;
//刷新权限
# update user set host='%' where user = 'root';
//由于不允许远程登录，所以更改表user将host参数localhost改成%，所有人都能登录
# select host,user from user;
//查看是否更改完成
# quit
//退出mysql
# vim /etc/my.cnf
//设置配置文件，添加 bind-address=0.0.0.0，设置成ipv4监听
# service mysql restart
//重启mysql
# firewall-cmd --query-port=3306/tcp
//查看防火墙状态：mysql 3306端口能否被访问
# firewall-cmd --zone=public --add-port=3306/tcp --permanent
//永久开启3306端口能被访问
# firewall-cmd --reload
//重新加载防火墙
# firewall-cmd --query-port=3306/tcp
//再次查看mysql能否被远程访问
```

## Linux宝塔面板

重置服务器之后，自动安装完成宝塔面板

- 在防火墙中打开8888端口，能够登录宝塔面板

- 在应用管理中登录，使用 sudo /etc/init.d/bt default 命令获取用户名和密码
- 然后登录宝塔
- 设置完自己的密码记得保存
- 什么软件先都不要装，按照自己的需求来安装

### 数据库安装

- 在软件商店安装mysql数据库

- 点击添加数据库
- 用户名自己起
- 密码大小写+数字
- 访问权限可以本地服务器/所有人（这个可以让你的idea也进行连接）
- 在备份一栏点击导入，将自己的sql文件进行上传，然后导入到数据库
- 这样就可以看到自己的表名等信息

### 前端部署

- 在根目录下建立一个前端文件夹，将前端编译好的代码上传

- 在网站点击添加站点
- 写下自己备案的域名/ip地址
- 提交即可
- 在设置中添加反向代理的配置，确保能访问到后端

### 后端部署

- 在软件商店安装tomcat服务器
- 在根目录下建立一个后端文件夹，将打好的jar包上传
- 在软件商店停止tomcat服务，因为我们只是需要tomcat中的JDK，tomcat会占用你的8080端口
- 在网站点击java项目，添加java项目
- 在项目jar路径中选择自己的jar包
- 将项目名称改成自己想要的，项目端口也要改成你自己项目的端口
- 在执行命令末尾加--spring.profiles.active=prod，使项目运行在生产环境下
- 提交项目，项目就会自动运行
- 在设置中能够看到个人的项目能够运行成功。

## Docker部署

docker相当于软件安装包

Dockerfile用来指定构建相应的docker镜像的方法

Dockerfile一帮情况下不需要自己写

~~~java
dockerfile
FROM 基础镜像
WORKDIR 镜像工作目录（项目代码所在地）
COPY pom.xml
COPY src ./src
//镜像构建完成
RUN mvn package -DskipTests
//运行打包工具
CMD["java","-jar","jar包所在地","--spring.profiles.active=prod"]
//运行项目
~~~

