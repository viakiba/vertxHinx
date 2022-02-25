package io.github.viakiba.hinx.data.client;

import com.alibaba.fastjson.JSONObject;
import io.github.viakiba.hinx.boot.GlobalContext;
import io.github.viakiba.hinx.data.IDBService;
import io.github.viakiba.hinx.enums.DataType;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-26
 */
@Slf4j
public class RedisService implements IDBService {

    private Redis redis ;

    private RedisAPI redisAPI;

    @Override
    public void init() {
        RedisOptions redisOptions = getRedisOptions();
        redis = Redis.createClient(GlobalContext.getVertx(), redisOptions);
        redisAPI = RedisAPI.api(redis);
    }

    private RedisOptions getRedisOptions() {
        JSONObject config = config(DataType.redis);
        JsonObject jsonObject = new JsonObject(config.toJSONString());
        RedisOptions redisOptions = new RedisOptions(jsonObject);
        return redisOptions;
    }

    public RedisAPI getRedisAPI() {
        return redisAPI;
    }

    @Override
    public void stop() {
        redisAPI.close();
    }

    public void queryById( long key,String hashName
            , Handler<Response> success, Handler<Throwable> fail){
        redisAPI.hget(hashName,String.valueOf(key))
                .onSuccess(handler->{
                    success.handle(handler);
                })
                .onFailure(handler->{
                    fail.handle(handler);
                });
    }

    public void saveObj( String hashName,Object obj, Handler<AsyncResult<io.vertx.redis.client.Response>> complete){
        JsonObject jsonObject = JsonObject.mapFrom(obj);
        redisAPI.hset(Arrays.asList(hashName,String.valueOf(jsonObject.getString("_id")),jsonObject.encodePrettily()),handler ->{
            complete.handle(handler);
        });
    }


}
