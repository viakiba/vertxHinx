package com.ohayoo.whitebird.vertx;

import com.alibaba.fastjson.JSONObject;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.ParsedHeaderValues;
import io.vertx.ext.web.Router;

import java.util.List;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-26
 */
public class HttpVertxServer {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServerOptions options = new HttpServerOptions();
        HttpServer httpServer = vertx.createHttpServer(options);
        Router router = Router.router(vertx);
        router.route("/1")
            .method(HttpMethod.GET)
            .handler(
                x->{
                    MultiMap headers = x.request().headers();
                    List<String> p1 = x.queryParam("p1");
//                    System.out.println(JSONObject.toJSONString(headers.getAll("h1")) + "   " + JSONObject.toJSONString(p1));
                    System.out.println( System.currentTimeMillis());
                    x.request().response().end("test1");
                }
        );
        router.route("/2")
            .method(HttpMethod.POST)
            .handler(
                x->{
                    List<String> p1 = x.queryParam("p1");
                    MultiMap headers = x.request().headers();
//                    System.out.println(JSONObject.toJSONString(headers.getAll("h1")) + "   " + JSONObject.toJSONString(p1));
                    System.out.println( System.currentTimeMillis());
                    x.request().response().end("test2");
                }
            );

        httpServer.requestHandler(router);
        httpServer.listen(8080);
    }
}
