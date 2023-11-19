## YuPiFriendSystem

复习用项目

## 后台服务器

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

## 前端部署

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

## 后端部署

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

