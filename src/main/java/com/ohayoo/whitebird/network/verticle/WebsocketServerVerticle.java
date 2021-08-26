package com.ohayoo.whitebird.network.verticle;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.enums.NetType;
import com.ohayoo.whitebird.player.PlayerSystemService;
import com.ohayoo.whitebird.player.enums.AttributeEnum;
import com.ohayoo.whitebird.player.model.WebsocketPlayer;
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
        websocketPlayer.setAttribute(AttributeEnum.link,webSocket);
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
