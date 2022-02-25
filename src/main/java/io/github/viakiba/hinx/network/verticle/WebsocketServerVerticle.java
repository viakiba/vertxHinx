package io.github.viakiba.hinx.network.verticle;

import io.github.viakiba.hinx.boot.GlobalContext;
import io.github.viakiba.hinx.enums.NetType;
import io.github.viakiba.hinx.player.PlayerSystemService;
import io.github.viakiba.hinx.player.enums.AttributeEnum;
import io.github.viakiba.hinx.player.model.WebsocketPlayer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
@Slf4j
public class WebsocketServerVerticle extends AbstractVerticle implements BaseServerVerticle {

    private HttpServer server;

    @Override
    public void start() throws Exception {
        HttpServerOptions options = new HttpServerOptions(new JsonObject(configJson(NetType.websocket)));
        server = vertx.createHttpServer(options);
        server.webSocketHandler(this::handler);
        server.listen(8080);
        log.info("Websocket Init Success");
    }

    private void handler(ServerWebSocket webSocket) {
        PlayerSystemService playerSystemService = GlobalContext.getSystemService(PlayerSystemService.class);
        WebsocketPlayer websocketPlayer = new WebsocketPlayer();
        websocketPlayer.setAttribute(AttributeEnum.link.name(),webSocket);
        webSocket.handler(buffer -> {
            playerSystemService.websocketHandle(buffer,websocketPlayer);
        });

        webSocket.exceptionHandler(event -> {

        });
        webSocket.closeHandler( e ->{

        });
    }

    @Override
    public void stop() throws Exception {
        server.close();
    }
}
