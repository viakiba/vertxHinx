package com.ohayoo.whitebird.config;

import com.ohayoo.whitebird.enums.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@Data
@NoArgsConstructor
public class ServerConfig {
//  Vertx core 配置
    public int vertxBlockedThreadCheckInterval ;
    public int vertxEventLoopPoolSize ;
    public long vertxEventLoopExecuteTime ;
    public int vertxInternalBlockingPoolSize ;
    public int vertxWorkerPoolSize ;
    public long vertxMaxWorkerExecuteTime ;
//  集群
    public VertxClusterType vertxClusterType;
//  指标服务
    public boolean metricClock ;
    public String[] metricMeasured ;
//  excel config
    public String excelConfigPath;
    public String excelResourcePath;
//  http  tcp  websocket 启用的网络类型及其端口
    public NetType[] netType;
    public String[] netConfig;
//  service 处理类所在包路径
    public String[] bizServicePkgPath;
    public List<Integer> noDataExecuteMsgId;
//  数据库配置
    public DataType[] dataType ;
    //https://vertx.io/docs/vertx-mongo-client/java/#_configuring_the_client   https://vertx.io/docs/vertx-pg-client/java/#_configuration   https://vertx.io/docs/vertx-mongo-client/java/#_configuring_the_client   https://vertx.io/docs/vertx-redis-client/java/#_connecting_to_redis
    public String[] dataOptionPath ;
//  TCP读写模式 true 小端 false 大端
    public boolean byteOrder;
    public MessageType msgType;
//  GRPC监听端口
    public int grpcPort;
    public String[] grpcServicePkgPath;
}
