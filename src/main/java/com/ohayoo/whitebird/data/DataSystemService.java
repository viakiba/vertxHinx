package com.ohayoo.whitebird.data;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.boot.SystemServiceImpl;
import com.ohayoo.whitebird.config.ServerConfig;
import com.ohayoo.whitebird.data.client.MongoDBService;
import com.ohayoo.whitebird.data.client.RxHibernateService;
import com.ohayoo.whitebird.data.client.RedisService;
import com.ohayoo.whitebird.enums.DataType;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class DataSystemService implements SystemServiceImpl {
    private IDBService idbService;

    @Override
    public void start() {
        ServerConfig serverConfig = GlobalContext.serverConfig();
        DataType[] dataType = serverConfig.getDataType();
        if(dataType == null){
            return;
        }
        for (int i = 0; i < dataType.length; i++) {
            switch (dataType[i]){
                case rxHibernate:
                    idbService = new RxHibernateService();
                    break;
                case mongoDB:
                    idbService = new MongoDBService();
                    break;
                case redis:
                    idbService = new RedisService();
                    break;
                default:
                    throw new RuntimeException(" 数据持久服务 初始化失败！");
            }
        }
        if(idbService != null) {
            idbService.init();
        }
    }

    @Override
    public void stop() {
        idbService.stop();
    }

    public <T> T getIdbService() {
        return (T) idbService;
    }


}
