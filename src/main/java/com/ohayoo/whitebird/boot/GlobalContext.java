package com.ohayoo.whitebird.boot;

import cn.hutool.core.lang.Snowflake;
import com.ohayoo.whitebird.compoent.LocalIpUtil;
import com.ohayoo.whitebird.compoent.LoggerUtil;
import com.ohayoo.whitebird.config.ServerSystemConfig;
import com.ohayoo.whitebird.config.ServerConfig;
import com.ohayoo.whitebird.data.DataSystemService;
import com.ohayoo.whitebird.hotfix.groovy.GroovyScriptEngine;
import groovy.lang.Tuple4;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.core.spi.cluster.NodeInfo;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class GlobalContext {
    private static Logger log = LoggerFactory.getLogger(GlobalContext.class);

    private static Vertx vertx;
    private static ClusterManager clusterManager ;
    private static MetricsService metricsService ;
    private static final Map<Class,SystemServiceImpl> systemServiceMap = new ConcurrentHashMap<>();
    private static final List<SystemServiceImpl> systemServices = new ArrayList<>();
    private static Snowflake idUtil ;
    private static GroovyScriptEngine groovyScriptEngine =  new GroovyScriptEngine() ;

    public static void initVertx(Handler<Void> handler){
        ServerConfig serverConfig = GlobalContext.serverConfig();
        try {
            initIdUtil(serverConfig);
        } catch (IOException e) {
            LoggerUtil.error("初始化ID失败 "+e.getMessage());
            return;
        }
        //vertx 基础参数
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setBlockedThreadCheckInterval(serverConfig.getVertxBlockedThreadCheckInterval());
        vertxOptions.setEventLoopPoolSize(serverConfig.getVertxEventLoopPoolSize());
        vertxOptions.setMaxEventLoopExecuteTime(serverConfig.getVertxEventLoopExecuteTime());
        vertxOptions.setInternalBlockingPoolSize(serverConfig.getVertxInternalBlockingPoolSize());
        vertxOptions.setWorkerPoolSize(serverConfig.getVertxWorkerPoolSize());
        vertxOptions.setMaxWorkerExecuteTime(serverConfig.getVertxMaxWorkerExecuteTime());

        if(serverConfig.isMetricClock()){
            //如果打开指标监控 则初始化指标参数
            DropwizardMetricsOptions dropwizardMetricsOptions = new DropwizardMetricsOptions();
            dropwizardMetricsOptions.setEnabled(true);
            vertxOptions.setMetricsOptions(dropwizardMetricsOptions);
        }
        if(serverConfig.getVertxClusterType()!=null){
            //启用 ZK 集群
            clusterManager = new ZookeeperClusterManager();
            vertxOptions.setClusterManager(clusterManager);
            Future<Vertx> vertxFuture = Vertx.clusteredVertx(vertxOptions);
            vertxFuture.onComplete(h ->{
                JsonObject nodeInfoAtt = new JsonObject();
                NodeInfo nodeInfo = new NodeInfo(
                        LocalIpUtil.get10BeginIp(),
                        serverConfig.getGrpcPort(),
                        nodeInfoAtt);
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

    private static void initIdUtil(ServerConfig serverConfig) throws IOException {
        //        2021-09-01 12:00:00  -> 1630468800000L
        if(serverConfig.isDebug()){
            idUtil = new Snowflake(new Date(1630468800000L),1, 1,false);
            return;
        }
        Map<String, Tuple4<String, Integer, Integer, Integer>> allServerMap = getAllServerMap();
        if(allServerMap == null){
            throw new RuntimeException("获取服务器列表失败");
        }
        String beginIp = LocalIpUtil.get10BeginIp();
        Tuple4<String, Integer, Integer, Integer> tuple4 = allServerMap.get(beginIp);
        if(tuple4 == null){
            throw new RuntimeException("id生成配置不存在");
        }
        idUtil = new Snowflake(new Date(1630468800000L),tuple4.getV3(), tuple4.getV4(),false);
    }
    public static Map<String,Tuple4<String,Integer,Integer,Integer>> getAllServerMap() throws IOException {
        BufferedInputStream inputStreamBase = null;
        try {
            inputStreamBase = new BufferedInputStream(new FileInputStream("config/server.json"));
            byte[] bytes = inputStreamBase.readAllBytes();
            String s1 = new String(bytes, StandardCharsets.UTF_8);
            JsonArray json = new JsonArray(s1);
            Map<String,Tuple4<String,Integer,Integer,Integer>> serverDataList = new HashMap<>();
            Iterator<Object> iterator = json.stream().iterator();
            while (iterator.hasNext()){
                Object next = iterator.next();
                String s = next.toString();
                log.debug(s);
                String[] split = s.split(",");
                Tuple4<String, Integer, Integer,Integer> tuple = Tuple4.tuple(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
                serverDataList.put(split[0],tuple);
            }
            return serverDataList;
        }catch (Exception e){
            log.error(" 服务器列表 "+e.getMessage());
        }finally {
            if(inputStreamBase!=null) {
                inputStreamBase.close();
            }
        }
        return null;
    }

    public static GroovyScriptEngine getGroovyScriptEngine() {
        return groovyScriptEngine;
    }

    public static Snowflake getSnowflake() {
        return idUtil;
    }

    public static void addSystemService(SystemServiceImpl systemService){
        systemServiceMap.put(systemService.getClass(),systemService);
    }

    public static <T> T getSystemService(Class clazz){
        return (T) systemServiceMap.get(clazz);
    }

    public static ServerConfig serverConfig(){
        return ((ServerSystemConfig) systemServiceMap.get(ServerSystemConfig.class)).getServerConfig();
    }

    public static DataSystemService getDataSystemService(){
        return ((DataSystemService) systemServiceMap.get(DataSystemService.class));
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

    public static void setVertx(Vertx vertx) {
        GlobalContext.vertx = vertx;
    }

}
