package io.github.viakiba.hinx.network;

import io.github.viakiba.hinx.boot.GlobalContext;
import io.github.viakiba.hinx.boot.SystemServiceImpl;
import io.github.viakiba.hinx.enums.NetType;
import io.github.viakiba.hinx.network.verticle.*;

/**
 * @author viakiba@gmail.com
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
            if(netType == NetType.udp){
                GlobalContext.getVertx().deployVerticle(new UdpServerVerticle());
            }
            if(netType == NetType.kcp){
                GlobalContext.getVertx().deployVerticle(new KcpServerVerticle());
            }
            if(netType == NetType.quic){
                GlobalContext.getVertx().deployVerticle(new QuicServerVerticle());
            }
        }
    }
}
