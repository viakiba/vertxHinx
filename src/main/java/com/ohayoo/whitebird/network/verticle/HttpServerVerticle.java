package com.ohayoo.whitebird.network.verticle;

import com.ohayoo.whitebird.enums.NetType;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.generate.message.CommonMessage;
import com.ohayoo.whitebird.gmadmin.GmRouteService;
import com.ohayoo.whitebird.player.PlayerSystemService;
import com.ohayoo.whitebird.player.enums.AttributeEnum;
import com.ohayoo.whitebird.player.model.HttpPlayer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class HttpServerVerticle extends AbstractVerticle implements BaseServerVerticle {
    private static Logger log = LoggerFactory.getLogger(HttpServerVerticle.class);

    @Override
    public void start() throws Exception {
        JsonObject entries = new JsonObject(configJson(NetType.http));
        HttpServerOptions options = new HttpServerOptions(entries);
        HttpServer server = vertx.createHttpServer(options);
        Router message = Router.router(vertx);
        message.route("/handler").handler(ctx -> handler(ctx) );
        server.requestHandler(message);
        initGmHandler(message);
        server.requestHandler(message);

        Future<HttpServer> listen = server.listen();
        listen.onSuccess(x ->{
            log.info("Http Init Success");
        }).onFailure(x->{
            log.info("Http Init Fail");
        });

    }
    /**
     * @description  参考
     *   // https://vertx.io/docs/vertx-web/java/#_routing_by_exact_path
     *   // https://vertx.io/docs/vertx-web/java/#_authentication_authorization
     * @author huangpeng.12@bytedance.com
     * @param router:  Router
     * @return:
     */
    private void initGmHandler(Router router) {
        new GmRouteService().initRoute(router,vertx);
    }

    private void handler(RoutingContext context) {
        HttpServerRequest req = context.request();
        req.bodyHandler(buffer -> {
                PlayerSystemService systemService = GlobalContext.getSystemService(PlayerSystemService.class);
                HttpPlayer httpPlayer = new HttpPlayer();
                httpPlayer.setAttribute(AttributeEnum.link,req);
                try {
                    systemService.httpHandle(req, buffer, httpPlayer);
                }catch (Exception e){
                    log.error("未知异常",e);
                    httpPlayer.sendError(CommonMessage.ErrorCode.ServerInnerException_VALUE,0);
                }
            });
    }

}
