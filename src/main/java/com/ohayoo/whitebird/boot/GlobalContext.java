package com.ohayoo.whitebird.boot;

import com.ohayoo.whitebird.config.ServerConfig;
import com.ohayoo.whitebird.config.ServerSystemConfig;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class GlobalContext {
    private static Vertx vertx;

    public static Map<Class,SystemServiceImpl> systemServiceMap = new ConcurrentHashMap<>();
    public static List<SystemServiceImpl> systemServices = new ArrayList<>();

    public static void initVertx(){
        ServerConfig serverConfig = GlobalContext.serverConfig();
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setBlockedThreadCheckInterval(serverConfig.getVertxBlockedThreadCheckInterval());
        vertxOptions.setEventLoopPoolSize(serverConfig.getVertxEventLoopPoolSize());
        vertxOptions.setMaxEventLoopExecuteTime(serverConfig.getVertxEventLoopExecuteTime());
        vertxOptions.setInternalBlockingPoolSize(serverConfig.getVertxInternalBlockingPoolSize());
        vertxOptions.setWorkerPoolSize(serverConfig.getVertxWorkerPoolSize());
        vertxOptions.setMaxWorkerExecuteTime(serverConfig.getVertxMaxWorkerExecuteTime());
        vertx = Vertx.vertx(vertxOptions);
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
}
