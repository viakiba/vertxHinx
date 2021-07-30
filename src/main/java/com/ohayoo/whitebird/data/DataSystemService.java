package com.ohayoo.whitebird.data;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.boot.SystemServiceImpl;
import com.ohayoo.whitebird.config.ServerConfig;
import com.ohayoo.whitebird.data.client.MongoDBService;
import com.ohayoo.whitebird.data.client.RxHibernateService;
import com.ohayoo.whitebird.data.client.RedisService;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class DataSystemService implements SystemServiceImpl {
    private IDBService idbService;

    @Override
    public void start() {
        ServerConfig serverConfig = GlobalContext.serverConfig();
        for (int i = 0; i < serverConfig.getDataType().length; i++) {
            switch (serverConfig.getDataType()[i]){
                case rxHibernate:
                    idbService = new RxHibernateService();
                    idbService.init();
                    break;
                case mongoDB:
                    idbService = new MongoDBService();
                    idbService.init();
                    break;
                case redis:
                    idbService = new RedisService();
                    idbService.init();
                    break;
                default:
                    throw new RuntimeException(" 数据持久服务 初始化失败！");
            }
        }
    }

    public <T> T getIdbService() {
        return (T) idbService;
    }


}