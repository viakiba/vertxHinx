package io.github.viakiba.hinx.network.verticle;

import io.github.viakiba.hinx.boot.GlobalContext;
import io.github.viakiba.hinx.enums.NetType;
import io.github.viakiba.hinx.player.PlayerSystemService;
import io.github.viakiba.hinx.player.enums.AttributeEnum;
import io.github.viakiba.hinx.player.model.HttpPlayer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

/**
 * @author viakiba@gmail.com
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
        initGmHandler(router);
        server.requestHandler(router);
        server.listen();
        log.info("Http Init Success");
    }

    private void initGmHandler(Router router) {
        // https://vertx.io/docs/vertx-web/java/#_routing_by_exact_path
        // https://vertx.io/docs/vertx-web/java/#_authentication_authorization
        // 参考
        router.route("/").handler(x->{x.request().response().end("test");});
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
