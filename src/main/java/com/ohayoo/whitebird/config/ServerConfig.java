package com.ohayoo.whitebird.config;

import com.ohayoo.whitebird.enums.DataType;
import com.ohayoo.whitebird.enums.MessageType;
import com.ohayoo.whitebird.enums.NetType;
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
//  Vertx core option
    private int vertxBlockedThreadCheckInterval ;
    private int vertxEventLoopPoolSize ;
    private long vertxEventLoopExecuteTime ;
    private int vertxInternalBlockingPoolSize ;
    private int vertxWorkerPoolSize ;
    private long vertxMaxWorkerExecuteTime ;
//  excel config
    private String excelConfigPath;
    private String excelResourcePath;
//  http  tcp  websocket 启用的网络类型及其端口
    private NetType[] netType;
    private String[] netConfig;
//  service 处理类所在包路径
    private String bizServicePkgPath;
    private List<Integer> noDataExecuteMsgId;
//  数据库配置
    private DataType[] dataType ;
    /**
     https://vertx.io/docs/vertx-mongo-client/java/#_configuring_the_client
     https://vertx.io/docs/vertx-pg-client/java/#_configuration
     https://vertx.io/docs/vertx-mongo-client/java/#_configuring_the_client
     https://vertx.io/docs/vertx-redis-client/java/#_connecting_to_redis
     */
    private String[] dataOptionPath ;

//    TCP读写模式 true 小端 false 大端
    private boolean byteOrder;

    private MessageType msgType;
}
