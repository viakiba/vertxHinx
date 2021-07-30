package com.ohayoo.whitebird.network.verticle;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.enums.NetType;
import com.ohayoo.whitebird.player.PlayerSystemService;
import com.ohayoo.whitebird.player.enums.AttributeEnum;
import com.ohayoo.whitebird.player.model.HttpPlayer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@Slf4j
public class HttpServerVerticle extends AbstractVerticle implements BaseServerVerticle {

    @Override
    public void start() throws Exception {
        HttpServerOptions options = new HttpServerOptions(new JsonObject(configJson(NetType.http)));
        options.setLogActivity(true);
        HttpServer server = vertx.createHttpServer(options);
        Router router = Router.router(vertx);
        router.route("/handler").handler(ctx -> {
            HttpServerRequest request = ctx.request();
            handler(request);
        });
        server.requestHandler(router);
        server.listen();
        log.info("Http Init Success");
    }

    private void handler(HttpServerRequest req) {
            req.bodyHandler(buffer -> {
                PlayerSystemService systemService = GlobalContext.getSystemService(PlayerSystemService.class);
                HttpPlayer httpPlayer = new HttpPlayer();
                httpPlayer.setAttribute(AttributeEnum.link.name(),req);
                systemService.httpHandle(req,buffer,httpPlayer);
            });
    }

}
