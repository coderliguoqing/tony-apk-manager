## 项目框架介绍
**1.概述 **

项目是基于SpringBoot搭建的单体应用服务框架架构

### 核心支撑组件
- 认证授权中心 Spring Security OAuth2、JWTToken
- 服务框架 Spring MVC/Boot
- 分布式锁 Redis
- Mybatis 插件 tk-mybatis

### google protocol buffer 代码生成 
 - java:protoc --java_out=./ ./Message.proto  
 - js:protoc --js_out=import_style=commonjs,binary:. ./Message.proto
