package com.ohayoo.whitebird.data;

import com.ohayoo.whitebird.base.TestBase;
import com.ohayoo.whitebird.data.client.RedisService;
import com.ohayoo.whitebird.data.model.GameData;
import io.vertx.redis.client.RedisAPI;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-28
 */
@Slf4j
public class TestRedisClient extends TestBase {

    @Test
    private void test0() throws Exception {
        RedisService redisService = new RedisService() ;
        redisService.init();
        RedisAPI client = redisService.getRedisAPI() ;
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
