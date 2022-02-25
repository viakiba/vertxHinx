package io.github.viakiba.hinx.grpc;

import io.vertx.grpc.VertxServerBuilder;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-06
 */
public interface IGrpcService {

    void init(VertxServerBuilder vertxServer);

}
