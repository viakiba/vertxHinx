## hinx

    基于Vertx的异步非阻塞服务端脚手架
    example/kotlin 分支是基于kotlin实现的协程分支

```text
  关于 kotlin及其协程以及vertx与kotlin的深度结合：
    https://www.kotlincn.net/docs/tutorials/getting-started.html
    https://www.kotlincn.net/docs/tutorials/coroutines/async-programming.html
    https://vertx.io/docs/vertx-lang-kotlin-coroutines/kotlin/
    https://vertx.io/docs/vertx-core/kotlin/
  在 example/kotlin 分支的 test 下 ，有关于 kotlin的代码示例。
  玩家消息的处理基于 channel 保证串行 线程安全
```

## 简介

### 基础框架

    使用Java语言，JDK 11版本。基于 vert.X 构建出异步非阻塞高性能服务器脚手架。
### 构建目标

    模块化, 可配置, 异步IO 并且 拥有简单高效的业务实现体验。同时引入类似 集群，RPC等常用的组件。
    此脚手架不对游戏进行架构约束，架构设计应具体场景具体分析，但是此脚手架的目标是满足不同的架构所需要的代码脚手架支撑。

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

## Start 类

```text
此类展示了如何启动并管理当前程序中存在的所有服务，同时对外进行业务处理。
start 的 main 启动 vm 参数 需要添加：
    -Dconfig=config/server.properties -Dvertx.zookeeper.config=./config/cluster/zookeeper.json
```

## 使用

### 服务抽象

```text
SystemServiceImpl 此类对于程序中所有的单例服务 规范了 启动 与 停止方法。
所有在程序中存在的单例对象都要实现此接口，并被 GlobalContext 进行管理，方便业务进行使用。
```

### 配置文件

#### 主配置文件

server.properties 此配置文件的 key 与 类 ServerConfig 的属性名称 一一对应，如类定义的属性，支持各种结构。此配置文件路径 由 vm 启动参数 指定并获取（ -Dconfig=config/server.properties ）。初始化代码：

```java
ServerSystemConfig serverSystemConfig = new ServerSystemConfig();
GlobalContext.addSystemService(serverSystemConfig);
serverSystemConfig.start();
```

#### Excel 配置

excel.xml 配置文件会由主配置文件指定位置，此配置文件的配置解释见引入的项目的 [readme](https://code.byted.org/whitebird/common_excel_tool "readme")。

#### JSON 配置

如代码所示，config 下 cluster / data / net 文件夹中，每个配置文件都是以json格式的形式进行配置，并被主配置文件指定到。以 data 下的 mongoDB.json 为例子，这个json文件会被 MongoDBService 的 init 方法读取并直接使用。


#### VertX 

在初始化 主配置文件之后，需要进行 vertx 的初始化，vertx是核心对象。

    根据配置初始化我们可以得到 vertx 对象。
    如果启用了集群配置我们还会得到 clusterManager。
    启用指标 会得到 metricsService。

##### Core

初始化 vertx ，其参数 会由 主配置文件获得 详情见 GlobalContext 的 initVertx 方法。

```java
VertxOptions vertxOptions = new VertxOptions();
vertxOptions.setBlockedThreadCheckInterval(serverConfig.getVertxBlockedThreadCheckInterval());
vertxOptions.setEventLoopPoolSize(serverConfig.getVertxEventLoopPoolSize());
vertxOptions.setMaxEventLoopExecuteTime(serverConfig.getVertxEventLoopExecuteTime());
vertxOptions.setInternalBlockingPoolSize(serverConfig.getVertxInternalBlockingPoolSize());
vertxOptions.setWorkerPoolSize(serverConfig.getVertxWorkerPoolSize());
vertxOptions.setMaxWorkerExecuteTime(serverConfig.getVertxMaxWorkerExecuteTime());
```

##### Metrics 指标

```java
//如果打开指标监控 则初始化指标参数
DropwizardMetricsOptions dropwizardMetricsOptions = new DropwizardMetricsOptions();
dropwizardMetricsOptions.setEnabled(true);
vertxOptions.setMetricsOptions(dropwizardMetricsOptions);
```

##### Cluster 集群

```java
//启用 ZK 集群
clusterManager = new ZookeeperClusterManager();
vertxOptions.setClusterManager(clusterManager);
```
注意 集群 clusterManager 可以通过 setNodeInfo 向 zk 写入 本身的节点信息。此信息可以放入自定义的内容。
```java
JsonObject nodeInfoAtt = new JsonObject();
NodeInfo nodeInfo = new NodeInfo(LocalIpUtil.get10BeginIp(),
                        serverConfig.getGrpcPort(),nodeInfoAtt);
clusterManager.setNodeInfo(nodeInfo, Promise.promise());
```

#### 可插拔的服务

以下是可以按需取用的 系统服务，功能如名称。
每一个服务都有对应的配置，具体配置见主配置文件以及json文件。

##### 配置表

```java
ExcelSystemService excelSystemService = new ExcelSystemService();
GlobalContext.addSystemService(excelSystemService);
excelSystemService.start();
```

##### 定时调度

```java
ScheduleSystemService scheduleSystemService = new ScheduleSystemService();
GlobalContext.addSystemService(scheduleSystemService);
scheduleSystemService.start();
```

##### 第三方组件

```java
initThirdComponent();
```

##### 数据库

```java
DataSystemService dataSystemService = new DataSystemService();
GlobalContext.addSystemService(dataSystemService);
dataSystemService.start();
```

##### 玩家服务

```java
PlayerSystemService playerSystemService = new PlayerSystemService();
GlobalContext.addSystemService(playerSystemService);
playerSystemService.start();
```

##### GRPC

```java
GrpcSystemService grpcSystemService = new GrpcSystemService();
GlobalContext.addSystemService(grpcSystemService);
grpcSystemService.start();
```

##### 网络与消息

消息处理见 CommonService 样例。

```java
NetSystemService netSystemService = new NetSystemService();
GlobalContext.addSystemService(netSystemService);
netSystemService.start();
MsgSystemService msgSystemService = new MsgSystemService();
GlobalContext.addSystemService(msgSystemService);
msgSystemService.start();
```
