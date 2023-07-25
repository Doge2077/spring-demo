# [前后端分离项目模板](https://lys2021.com/?p=1498)

****

## 功能简介-[视频演示](https://www.bilibili.com/video/BV1qP411k7Fr/)

****

### 功能概要

****

* 支持登录和注册功能。
* 支持密码找回和邮箱验证。
* 支持“记住我”功能。
* 包含完整的上述功能的基础页面。

****

### 登录功能

****

* 支持“用户名/密码”登录。
* 登录可选“记住我”，勾选后在用户不主动登出时保留登录信息。
* 只有用户登录后，才能访问站内界面，非法访问都会返回主界面。

![](https://image.itbaima.net/images/40/image-20230717221694970.png)

****

### 注册功能

****

* 需要提供：
  * 用户名（唯一）
  * 密码（两次确认）
  * 邮箱（唯一且必填，用于找回密码）
  * 验证码（发送验证码到邮箱）
* 用户名和邮箱一一对应。
* 验证码发送有效期3分钟，发送一次后需要等待60秒。

![](https://image.itbaima.net/images/40/image-20230717228024584.png)

****

### 密码找回

****

* 重置密码需要验证邮箱，发送验证码进行验证。
* 验证通过后才能进行重置。

![](https://image.itbaima.net/images/40/image-20230717222022386.png)

****

## 快速开始

****

### 环境搭建

****

* 首先 `Star` 并且 `Fork` 本项目到你的 `repo` （bushi）
* 环境依赖：`jdk17`、`node.js16+`、`SpringBoot3`、`Vue3`、`redis5.0.14.1`
* 推荐使用 `idea` 进行构建：
  * 将 `demo-beckend` 模块导入，并执行 `Maven` 构建。
  * 将 `demo-frontend` 模块导入，并执行 `npm` 构建。
* 安装 `redis`，`windows` [下载地址](https://github.com/tporadowski/redis/releases)；
* 安装 `mysql`，运行 `demo.sql` 文件，注意字符集为 `utf8mb4`，排序规则为 `utf8mb4_unicode_ci`。

****

### 数据源配置

****

进入 `demo-backend/src/main/resources` 打开 `application.yaml` 文件修改数据源配置：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&charterEncoding=utf-8  # 数据库连接地址，数据库名称默认 demo
    username: xxx             # 数据库连接用户名
    password: xxxxxx          # 密码
  mail:                       # 配置 smtp 邮件代发
    host: smtp.163.com        # 邮件代理主机地址
    username: xxx@xxx.com     # 代理用户名
    password: xxxxxx          # 密码
    protocol: smtps
    port: 465
    properties:
      from: xxx@xxx.com       # 代理用户名
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
          socketFactory:
            class: javax.net.ssl.SSLSocketFactory
  data:
    redis:                    # redis 配置
      database: 0
      host: 127.0.0.1
      port: 6379
```

****

### 启动项目

****

* 确认 `mysql` 数据库成功连接；
* 启动 `redis` 服务端；
* 最后启动前后端即可。

****

## 常见问题

****

### 启动连接数据库报错

****

* 检查数据源配置，确认 `mysql` 数据库端口正确并放行。
* 检查数据库名和用户权限。

****

### 验证码发送失败

****

#### 连接不到主机

****

* 检查邮件代理用户是否已经开启 `smtp` 服务。
* 检查代理邮箱和代理服务密码是否正确。

****

#### 报错：springboot Got bad greeting from SMTP host: smtp.xxx.com, port: 465, response: [EOF]

****

* 关闭 `vpn` 等网络代理服务。
* 检查数据源配置 `mail` 的 `protocal`类型，`port: 465` 对应 `protocal:smtps`。
