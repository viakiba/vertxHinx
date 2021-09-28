package com.ohayoo.whitebird.data.client;

import com.alibaba.fastjson.JSONObject;
import com.ohayoo.whitebird.data.IDBService;
import com.ohayoo.whitebird.enums.DataType;
import com.ohayoo.whitebird.boot.GlobalContext;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.impl.future.FailedFuture;
import io.vertx.core.impl.future.SucceededFuture;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.WriteOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://vertx.io/docs/vertx-mongo-client/java/#_using_the_api
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-26
 */
public class MongoDBService implements IDBService {
    private static Logger log = LoggerFactory.getLogger(MongoDBService.class);

    private MongoClient client ;
    @Override
    public void init() {
        JSONObject jsonObject = config(DataType.mongoDB);
        JsonObject config = new JsonObject(jsonObject.toJSONString());
        client = MongoClient.createShared(GlobalContext.getVertx(), config);
    }

    @Override
    public void stop() {
        Future<Void> close = client.close();
        close.onSuccess(handler -> {
            log.info("stop mongoDB ok!");
        });
    }

    public MongoClient getClient() {
        return client;
    }

    @Override
    public void selectByOpenId(String id,String collectionName
            ,Handler<AsyncResult<JsonObject>> handler){
        JsonObject query = new JsonObject();
        query.put("openId",id);
        client.findOne(collectionName, query,null, h ->{
            if(h.succeeded()){
                handler.handle(new SucceededFuture(h.result()));
            }else{
                handler.handle(new FailedFuture(h.cause()));
            }
        });
    }

    @Override
    public void saveObj(Object obj, String collectionName, Handler<AsyncResult<String>> handler){
        JsonObject data = JsonObject.mapFrom(obj);
        client.saveWithOptions(collectionName, data,  WriteOption.UNACKNOWLEDGED, res -> {
            if(res.succeeded()){
                handler.handle(new SucceededFuture(res.result()));
            }else{
                handler.handle(new FailedFuture(res.cause()));
            }
        });
    }

    @Override
    public void deleteObj(String keyName, Object keyValue, String collectionName, Handler<AsyncResult<String>> handler){
        JsonObject query = new JsonObject();
        query.put(keyName,keyValue);
        client.removeDocumentsWithOptions(collectionName, query,  WriteOption.UNACKNOWLEDGED, res -> {
            if(res.succeeded()){
                handler.handle(new SucceededFuture(res.result()));
            }else{
                handler.handle(new FailedFuture(res.cause()));
            }
        });
    }
}
