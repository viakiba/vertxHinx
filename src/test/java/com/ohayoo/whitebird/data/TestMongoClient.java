//package com.ohayoo.whitebird.data;
//
//import com.ohayoo.whitebird.base.TestBase;
//import com.ohayoo.whitebird.boot.GlobalContext;
//import com.ohayoo.whitebird.data.client.MongoDBService;
//import com.ohayoo.whitebird.data.model.GameData;
//import groovyjarjarasm.asm.Handle;
//import io.reactivex.rxjava3.core.Flowable;
//import io.vertx.core.AsyncResult;
//import io.vertx.core.Future;
//import io.vertx.core.Handler;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.mongo.MongoClient;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.List;
//
///**
// * @author huangpeng.12@bytedance.com
// * @createTime 2021-07-28
// */
//@Slf4j
//public class TestMongoClient extends TestBase {
//    static MongoClient client ;
//    static MongoDBService mongoDBService ;
//    static {
//        init();
//        DataSystemService dataSystemService = new DataSystemService();
//        GlobalContext.addSystemService(dataSystemService);
//        dataSystemService.start();
//        DataSystemService systemService = GlobalContext.getSystemService(DataSystemService.class);
//        mongoDBService = systemService.getIdbService();
//        client = mongoDBService.getClient();
//    }
//
//    public static void main(String[] args) throws Exception {
////        test0();
//        test1();
//    }
//
//    private static void test1() throws Exception {
//        GameData gameData = new GameData(1,"sss333");
//        mongoDBService.saveObj(gameData,"test",
//                success ->{
//                    System.out.println(success.succeeded());
//                },fail->{
//                    System.out.println(fail.succeeded());
//                });
//        mongoDBService.queryById(2,"test",success->{
//            List<JsonObject> result = success.result();
//            System.out.println(result);
//        },fail->{
//            System.out.println(fail.cause().getMessage());
//        });
//        Thread.sleep(100000);
//    }
//
//    private static void test0() throws Exception {
//        TestMongoClient testMongoClient = new TestMongoClient();
//        testMongoClient.test(success->{
//            System.out.println("xxxxx"+success.result());
//        },fail ->{
//            System.out.println("fail");
//        });
//    }
//
//    public void test(Handler<AsyncResult<List<String>>> success,Handler<AsyncResult<Void>> fail) throws InterruptedException {
//        Future<List<String>> collections = client.getCollections();
//        collections
//                .onSuccess(x->{
//                    success.handle(collections);
//                })
//                .onFailure(x->{
//                    log.error(x.getCause().getMessage());
//                    fail.handle(null);
//                })
//        ;
//        Thread.sleep(10000);
//        System.exit(0);
//    }
//
//}
