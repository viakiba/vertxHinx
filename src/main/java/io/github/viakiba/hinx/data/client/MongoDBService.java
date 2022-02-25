package io.github.viakiba.hinx.data.client;

import com.alibaba.fastjson.JSONObject;
import io.github.viakiba.hinx.boot.GlobalContext;
import io.github.viakiba.hinx.data.IDBService;
import io.github.viakiba.hinx.enums.DataType;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import io.vertx.ext.mongo.UpdateOptions;
import io.vertx.ext.mongo.WriteOption;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * https://vertx.io/docs/vertx-mongo-client/java/#_using_the_api
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-26
 */
@Slf4j
public class MongoDBService implements IDBService {
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
            log.info("stop mysql ok!");
        });
    }

    public MongoClient getClient() {
        return client;
    }

    public void queryById(long id,String collectionName
            ,Handler<AsyncResult<List<JsonObject>>> success, Handler<AsyncResult<List<JsonObject>>> fail){
        JsonObject query = new JsonObject();
        query.put("_id",id);
        client.find(collectionName, query, handler ->{
            if(handler.succeeded()){
                success.handle(handler);
            }else{
                fail.handle(handler);
            }
        });
    }

    public  void saveObj(Object obj, String collectionName, Handler<AsyncResult<MongoClientUpdateResult>> success, Handler<AsyncResult<MongoClientUpdateResult>> fail){
        JsonObject data = JsonObject.mapFrom(obj);
        JsonObject query = new JsonObject()
                .put("_id", data.getLong("_id"))
                ;
        JsonObject update = new JsonObject()
                .put("$set", data)
                ;
        UpdateOptions options = new UpdateOptions()
                .setUpsert(true)
                .setWriteOption(WriteOption.ACKNOWLEDGED)
                ;
        client.updateCollectionWithOptions(collectionName, query, update, options, res -> {
            if (res.succeeded()) {
                success.handle(res);
            } else {
                System.out.println(res.cause().getMessage());
                fail.handle(res);
            }
        });
    }
}
