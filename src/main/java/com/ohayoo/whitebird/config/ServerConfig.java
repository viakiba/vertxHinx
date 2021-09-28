package com.ohayoo.whitebird.config;

import com.ohayoo.whitebird.enums.DataType;
import com.ohayoo.whitebird.enums.MessageType;
import com.ohayoo.whitebird.enums.NetType;
import com.ohayoo.whitebird.enums.VertxClusterType;

import java.util.List;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class ServerConfig {
//  Vertx core 配置
    private int vertxBlockedThreadCheckInterval ;
    private int vertxEventLoopPoolSize ;
    private long vertxEventLoopExecuteTime ;
    private int vertxInternalBlockingPoolSize ;
    private int vertxWorkerPoolSize ;
    private long vertxMaxWorkerExecuteTime ;
//  集群
    private VertxClusterType vertxClusterType;
//  指标服务
    private boolean metricClock ;
    private String[] metricMeasured ;
//  excel config
    private String excelConfigPath;
    private String excelResourcePath;
//  http  tcp  websocket 启用的网络类型及其端口
    private NetType[] netType;
    private String[] netConfig;
//  service 处理类所在包路径
    private String[] bizServicePkgPath;
    private List<String> noDataExecuteMsgId;
//  数据库配置
    private DataType[] dataType ;
    //https://vertx.io/docs/vertx-mongo-client/java/#_configuring_the_client   https://vertx.io/docs/vertx-pg-client/java/#_configuration   https://vertx.io/docs/vertx-mongo-client/java/#_configuring_the_client   https://vertx.io/docs/vertx-redis-client/java/#_connecting_to_redis
    private String[] dataOptionPath ;
//  TCP读写模式 true 小端 false 大端
    private boolean byteOrder;
    private MessageType msgType;
//  GRPC监听端口
    private int grpcPort;
    private String[] grpcServicePkgPath;

    private boolean debug = false;

    public static final long gameCloudAppID = 100000000;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getVertxBlockedThreadCheckInterval() {
        return vertxBlockedThreadCheckInterval;
    }

    public void setVertxBlockedThreadCheckInterval(int vertxBlockedThreadCheckInterval) {
        this.vertxBlockedThreadCheckInterval = vertxBlockedThreadCheckInterval;
    }

    public int getVertxEventLoopPoolSize() {
        return vertxEventLoopPoolSize;
    }

    public void setVertxEventLoopPoolSize(int vertxEventLoopPoolSize) {
        this.vertxEventLoopPoolSize = vertxEventLoopPoolSize;
    }

    public long getVertxEventLoopExecuteTime() {
        return vertxEventLoopExecuteTime;
    }

    public void setVertxEventLoopExecuteTime(long vertxEventLoopExecuteTime) {
        this.vertxEventLoopExecuteTime = vertxEventLoopExecuteTime;
    }

    public int getVertxInternalBlockingPoolSize() {
        return vertxInternalBlockingPoolSize;
    }

    public void setVertxInternalBlockingPoolSize(int vertxInternalBlockingPoolSize) {
        this.vertxInternalBlockingPoolSize = vertxInternalBlockingPoolSize;
    }

    public int getVertxWorkerPoolSize() {
        return vertxWorkerPoolSize;
    }

    public void setVertxWorkerPoolSize(int vertxWorkerPoolSize) {
        this.vertxWorkerPoolSize = vertxWorkerPoolSize;
    }

    public long getVertxMaxWorkerExecuteTime() {
        return vertxMaxWorkerExecuteTime;
    }

    public void setVertxMaxWorkerExecuteTime(long vertxMaxWorkerExecuteTime) {
        this.vertxMaxWorkerExecuteTime = vertxMaxWorkerExecuteTime;
    }

    public VertxClusterType getVertxClusterType() {
        return vertxClusterType;
    }

    public void setVertxClusterType(VertxClusterType vertxClusterType) {
        this.vertxClusterType = vertxClusterType;
    }

    public boolean isMetricClock() {
        return metricClock;
    }

    public void setMetricClock(boolean metricClock) {
        this.metricClock = metricClock;
    }

    public String[] getMetricMeasured() {
        return metricMeasured;
    }

    public void setMetricMeasured(String[] metricMeasured) {
        this.metricMeasured = metricMeasured;
    }

    public String getExcelConfigPath() {
        return excelConfigPath;
    }

    public void setExcelConfigPath(String excelConfigPath) {
        this.excelConfigPath = excelConfigPath;
    }

    public String getExcelResourcePath() {
        return excelResourcePath;
    }

    public void setExcelResourcePath(String excelResourcePath) {
        this.excelResourcePath = excelResourcePath;
    }

    public NetType[] getNetType() {
        return netType;
    }

    public void setNetType(NetType[] netType) {
        this.netType = netType;
    }

    public String[] getNetConfig() {
        return netConfig;
    }

    public void setNetConfig(String[] netConfig) {
        this.netConfig = netConfig;
    }

    public String[] getBizServicePkgPath() {
        return bizServicePkgPath;
    }

    public void setBizServicePkgPath(String[] bizServicePkgPath) {
        this.bizServicePkgPath = bizServicePkgPath;
    }

    public List<String> getNoDataExecuteMsgId() {
        return noDataExecuteMsgId;
    }

    public void setNoDataExecuteMsgId(List<String> noDataExecuteMsgId) {
        this.noDataExecuteMsgId = noDataExecuteMsgId;
    }

    public DataType[] getDataType() {
        return dataType;
    }

    public void setDataType(DataType[] dataType) {
        this.dataType = dataType;
    }

    public String[] getDataOptionPath() {
        return dataOptionPath;
    }

    public void setDataOptionPath(String[] dataOptionPath) {
        this.dataOptionPath = dataOptionPath;
    }

    public boolean isByteOrder() {
        return byteOrder;
    }

    public void setByteOrder(boolean byteOrder) {
        this.byteOrder = byteOrder;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }

    public int getGrpcPort() {
        return grpcPort;
    }

    public void setGrpcPort(int grpcPort) {
        this.grpcPort = grpcPort;
    }

    public String[] getGrpcServicePkgPath() {
        return grpcServicePkgPath;
    }

    public void setGrpcServicePkgPath(String[] grpcServicePkgPath) {
        this.grpcServicePkgPath = grpcServicePkgPath;
    }
}
