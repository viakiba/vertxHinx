package io.github.viakiba.hinx.data;

import io.github.viakiba.hinx.boot.GlobalContext;
import io.github.viakiba.hinx.boot.SystemServiceImpl;
import io.github.viakiba.hinx.config.ServerConfig;
import io.github.viakiba.hinx.data.client.MongoDBService;
import io.github.viakiba.hinx.data.client.RxHibernateService;
import io.github.viakiba.hinx.data.client.RedisService;
import io.github.viakiba.hinx.enums.DataType;

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
