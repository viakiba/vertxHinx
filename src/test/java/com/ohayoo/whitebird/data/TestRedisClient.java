package com.ohayoo.whitebird.data;

import com.ohayoo.whitebird.base.TestBase;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.data.client.MongoDBService;
import com.ohayoo.whitebird.data.client.RedisService;
import com.ohayoo.whitebird.data.model.GameData;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.redis.client.RedisAPI;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-28
 */
@Slf4j
public class TestRedisClient extends TestBase {
    static RedisService redisService ;
    static RedisAPI client ;
    static {
        init();
        DataSystemService dataSystemService = new DataSystemService();
        GlobalContext.addSystemService(dataSystemService);
        dataSystemService.start();
        DataSystemService systemService = GlobalContext.getSystemService(DataSystemService.class);
        redisService = systemService.getIdbService();
        client = redisService.getRedisAPI();
    }

    public static void main(String[] args) throws Exception {
        test1();
    }

    private static void test1() throws Exception {
        GameData gameData = new GameData(1,"22222222222222");
        redisService.saveObj("test",gameData,
                success ->{
                    System.out.println("2222    "+success.result().toString());
                });
        redisService.queryById(1,"test",success->{
            System.out.println( "1111      "+success.toString());
        },fa ->{
            System.out.println( fa.toString());
        });
        Thread.sleep(100000);
    }


}
