package com.ohayoo.whitebird.config;

import cn.hutool.core.bean.BeanUtil;
import com.ohayoo.whitebird.boot.SystemServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class ServerSystemConfig implements SystemServiceImpl {
    private static Logger log = LoggerFactory.getLogger(ServerSystemConfig.class);
    private ServerConfig serverConfig = new ServerConfig();

    @Override
    public void startService() {
        try {
            String serverConfigPath = System.getProperty("config");
            Properties properties = new Properties();
            BufferedInputStream inputStreamBase = new BufferedInputStream(new FileInputStream(serverConfigPath));
            properties.load(inputStreamBase);
            properties.keySet().parallelStream().forEach(x ->{
                Object o = properties.get(x);
                BeanUtil.setProperty(serverConfig,(String) x,o);
            });
            log.info("server config init success!");
        }catch (Exception e){
            log.error("server config init fail!",e);
        }
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

}
