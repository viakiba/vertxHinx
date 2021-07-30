package com.ohayoo.whitebird.network;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.boot.SystemServiceImpl;
import com.ohayoo.whitebird.enums.NetType;
import com.ohayoo.whitebird.network.verticle.HttpServerVerticle;
import com.ohayoo.whitebird.network.verticle.TcpServerVerticle;
import com.ohayoo.whitebird.network.verticle.WebsocketServerVerticle;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class NetSystemService implements SystemServiceImpl {

    /**
     * https://vertx.io/docs/vertx-core/java/#_in_the_beginning_there_was_vert_x
     * https://vertx.io/docs/apidocs/io/vertx/core/VertxOptions.html
     */
    @Override
    public void start() {
        NetType[] netTypes = GlobalContext.serverConfig().getNetType();
        for (NetType netType: netTypes) {
            if(netType == NetType.http){
                GlobalContext.getVertx().deployVerticle(new HttpServerVerticle());
            }
            if(netType == NetType.tcp){
                GlobalContext.getVertx().deployVerticle(new TcpServerVerticle());
            }
            if(netType == NetType.websocket){
                GlobalContext.getVertx().deployVerticle(new WebsocketServerVerticle());
            }
        }
    }

    @Override
    public void stop() {

    }

}
