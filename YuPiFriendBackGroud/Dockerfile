# 依赖的基础镜像，也就是我们的打包工具
FROM maven:3.5-jdk-8-alpine as builder

# 我们的项目都有哪些组件，并且将项目放到哪里运行
WORKDIR /app
COPY pom.xml .
COPY src ./src

# 对项目进行打包
RUN mvn package -DskipTests

# 运行项目
CMD["java","-jar","/app/target/YuPiFriendBackGroud-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]