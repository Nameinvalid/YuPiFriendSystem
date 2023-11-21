# 指定基础镜像
FROM nginx

# 设置工作目录
WORKDIR /usr/share/nginx/html/

# 切换用户，也就是后续指令的执行身份。这里将用户切换为 root，也就是超级用户，拥有最高的权限。
USER root

# 将本地的 ./docker/nginx.conf 文件复制到镜像中的/etc/nginx/conf.d/default.conf
COPY ./docker/nginx.conf /etc/nginx/conf.d/default.conf

# 将本地的 ./dist 目录复制到镜像中的 /usr/share/nginx/html/
COPY ./dist  /usr/share/nginx/html/

# 暴露一个端口，也就是容器对外提供服务的端口
EXPOSE 80

# 指定了容器启动时的默认命令
CMD ["nginx", "-g", "daemon off;"]
