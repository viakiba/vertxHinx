package io.github.viakiba.hinx.data;

import io.github.viakiba.hinx.base.TestBase;
import io.github.viakiba.hinx.data.client.MongoDBService;
import io.github.viakiba.hinx.data.model.GameData;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-07-28
 */
@Slf4j
public class TestMongoClient extends TestBase {

    @Test
    private void test0() throws Exception {
        MongoDBService mongoDBService = new MongoDBService();
        mongoDBService.init();
        MongoClient client = mongoDBService.getClient();
        GameData gameData = new GameData(1,"sss333");
        mongoDBService.saveObj(gameData,"test",
                success ->{
                    System.out.println(success.succeeded());
                },fail->{
                    System.out.println(fail.succeeded());
                });
        mongoDBService.queryById(2,"test",success->{
            List<JsonObject> result = success.result();
            System.out.println(result);
        },fail->{
            System.out.println(fail.cause().getMessage());
        });
        Thread.sleep(100000);
    }

    @Test
    public void test1() throws InterruptedException {
        MongoDBService mongoDBService = new MongoDBService();
        mongoDBService.init();
        MongoClient client = mongoDBService.getClient();
        Future<List<String>> collections = client.getCollections();
        collections
                .onSuccess(x->{
                    System.out.println("success");
                })
                .onFailure(x->{
                    System.out.println("fail"+x.getMessage());
                })
        ;
        Thread.sleep(10000);
    }

}
