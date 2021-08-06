## hinx

    基于Vertx的异步非阻塞服务端脚手架

## 简介

### 基础框架

    使用Java语言，JDK 11版本。基于 vert.X 构建出异步非阻塞高性能服务器脚手架。
### 构建目标

    模块化, 可配置, 异步IO 并且 拥有简单高效的业务实现体验。并且引入类似 集群，RPC等常用的组件。

### 主要功能

* 配置: 
  * 简单且统一的配置类。
  * 提供支持数组，枚举等类型的配置文件到类的的映射。
  * 风格统一的 系统服务类 SystemServiceImpl 。
* 网络层：
  * 基于配置可以选择已经内置的 TCP / UDP / WebSocket / Http 服务器。
  * 拥有统一的消息处理与回复方式。
  * 可配置的 支持 JSON 与 Proto 的编解码消息。
* 持久层：
  * 异步非阻塞的 Mongo Client 实现
  * 异步非阻塞的 Redis Client 实现
  * 异步非阻塞的 Hibenate Client 实现,支持 mysql, db2, Postgre 等数据库。
* 定时调度：内置 Java 原生的定时调度实现。
* 玩家管理：针对 TCP / WebSocket ,内置了简单的玩家管理。
* GRPC: 
  * 引入了GRPC实现，并对RPC处理进行规范。
  * 新增新的GRPC只需实现对应方法即可，会根据配置包名扫描其下面的所有被指定注解修饰的类。
  * 无需配置，并且已经提供原生的了消息与rpc代码生成工具。
* 策划表：
  * 使用 [common_excel_tool](https://code.byted.org/whitebird/common_excel_tool "common_excel_tool") 进行一键接入，无需导表直接读 excel。
  * 一键生成 excel 对应的 model 映射。具体可看 此项目 readme 。
  * 此项目也支持 lua, json 以及 go结构体导出。
* 异常处理：轻量级的异常处理。
* 热修复组件：接入  [common_hotfix](https://code.byted.org/whitebird/common_hotfix "common_hotfix") 热修复组件，请请查看项目 readme 。
  * groovy脚本执行能力
  * class 修改替换能力
* 常用组件：
  * 随机组件
  * 时间计算组件
  * IP获取组件
  * 等待。。。

## 使用

### 配置文件

