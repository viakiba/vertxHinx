package com.ohayoo.whitebird.data;

import com.ohayoo.whitebird.boot.SystemServiceImpl;
import com.ohayoo.whitebird.data.client.MongoDBService;
import com.ohayoo.whitebird.data.client.RedisService;
import com.ohayoo.whitebird.data.model.GameData;
import com.ohayoo.whitebird.enums.DataType;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.config.ServerConfig;
import com.ohayoo.whitebird.data.client.RxHibernateService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.impl.future.SucceededFuture;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class DataSystemService implements SystemServiceImpl {
    private static Logger log = LoggerFactory.getLogger(DataSystemService.class);
    private IDBService idbService;
    private Handler<AsyncResult<Void>> fa;

    @Override
    public void startService() {
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
    public void stopService() {
        idbService.stop();
    }

    public <T> T getIdbService() {
        return (T) idbService;
    }


    public void selectByOpenId(String openId, Handler<AsyncResult<GameData>> h) {
        idbService.selectByOpenId(openId,"gameData",handler ->{
            JsonObject result = handler.result();
            if(result == null){
                h.handle(new SucceededFuture<>(null));
            }else{
                GameData gameData = result.mapTo(GameData.class);
                h.handle(new SucceededFuture<>(gameData));
            }

        });

    }

    public void saveGameData(GameData gameData, Handler<AsyncResult<String>> h) {
        idbService.saveObj(gameData,"gameData",success ->{
            h.handle(success);
        });
    }
}
