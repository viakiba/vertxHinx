package io.github.viakiba.hinx.data;

import io.github.viakiba.hinx.base.TestBase;
import io.github.viakiba.hinx.data.client.RedisService;
import io.github.viakiba.hinx.data.model.GameData;
import io.vertx.redis.client.RedisAPI;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * @author viakiba@gmail.com
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
