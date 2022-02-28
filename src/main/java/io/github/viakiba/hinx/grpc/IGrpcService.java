package io.github.viakiba.hinx.grpc;

import io.vertx.grpc.VertxServerBuilder;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-08-06
 */
public interface IGrpcService {

    void init(VertxServerBuilder vertxServer);

}
