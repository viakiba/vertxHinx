package com.ohayoo.whitebird.boot;

import com.ohayoo.whitebird.compoent.LocalIpUtil;
import com.ohayoo.whitebird.config.ServerConfig;
import com.ohayoo.whitebird.config.ServerSystemConfig;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.core.spi.cluster.NodeInfo;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@Slf4j
public class GlobalContext {
    private static Vertx vertx;
    private static ClusterManager clusterManager ;
    private static MetricsService metricsService ;
    public static Map<Class,SystemServiceImpl> systemServiceMap = new ConcurrentHashMap<>();
    public static List<SystemServiceImpl> systemServices = new ArrayList<>();

    public static void initVertx(Handler<Void> handler){
        ServerConfig serverConfig = GlobalContext.serverConfig();
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setBlockedThreadCheckInterval(serverConfig.getVertxBlockedThreadCheckInterval());
        vertxOptions.setEventLoopPoolSize(serverConfig.getVertxEventLoopPoolSize());
        vertxOptions.setMaxEventLoopExecuteTime(serverConfig.getVertxEventLoopExecuteTime());
        vertxOptions.setInternalBlockingPoolSize(serverConfig.getVertxInternalBlockingPoolSize());
        vertxOptions.setWorkerPoolSize(serverConfig.getVertxWorkerPoolSize());
        vertxOptions.setMaxWorkerExecuteTime(serverConfig.getVertxMaxWorkerExecuteTime());
        if(serverConfig.isMetricClock()){
            DropwizardMetricsOptions dropwizardMetricsOptions = new DropwizardMetricsOptions();
            dropwizardMetricsOptions.setEnabled(true);
            vertxOptions.setMetricsOptions(dropwizardMetricsOptions);
        }
        if(serverConfig.getVertxClusterType()!=null){
            clusterManager = new ZookeeperClusterManager();
            vertxOptions.setClusterManager(clusterManager);
            Future<Vertx> vertxFuture = Vertx.clusteredVertx(vertxOptions);
            vertxFuture.onComplete(h ->{
                NodeInfo nodeInfo = new NodeInfo(
                        LocalIpUtil.get10BeginIp(),
                        serverConfig.getGrpcPort(),
                        new JsonObject());
                //可以以一个频率设置 NodeInfo 并执行下面的  setNodeInfo 方法，可以向zk同步数据
                //NodeInfo nodeInfo = clusterManager.getNodeInfo();
                clusterManager.setNodeInfo(nodeInfo, Promise.promise());
                vertx = vertxFuture.result();
                if(serverConfig.isMetricClock()){
                    metricsService = MetricsService.create(vertx);
                }
                handler.handle(null);
            });
        }else{
            vertx = Vertx.vertx(vertxOptions);
            if(serverConfig.isMetricClock()){
                metricsService = MetricsService.create(vertx);
            }
            handler.handle(null);
        }
    }

    public static void addSystemService(SystemServiceImpl systemService){
        systemServiceMap.put(systemService.getClass(),systemService);
    }

    public static <T> T getSystemService(Class clazz){
        return (T) systemServiceMap.get(clazz);
    }

    public static ServerSystemConfig serverSystemConfig(){
        return (ServerSystemConfig) systemServiceMap.get(ServerSystemConfig.class);
    }

    public static ServerConfig serverConfig(){
        return ((ServerSystemConfig) systemServiceMap.get(ServerSystemConfig.class)).getServerConfig();
    }

    public static List<SystemServiceImpl> getSystemServiceAll(){
        return systemServices;
    }

    public static Vertx getVertx() {
        return vertx;
    }

    public static ClusterManager getClusterManager() {
        return clusterManager;
    }

    public static MetricsService getMetricsService() {
        return metricsService;
    }
}
