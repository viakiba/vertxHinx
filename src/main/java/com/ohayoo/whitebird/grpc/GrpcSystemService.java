package com.ohayoo.whitebird.grpc;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.boot.SystemServiceImpl;
import com.ohayoo.whitebird.generate.grpc.HelloReply;
import com.ohayoo.whitebird.generate.grpc.HelloRequest;
import com.ohayoo.whitebird.generate.grpc.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import io.vertx.core.Vertx;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

import java.io.IOException;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-02
 */
public class GrpcSystemService implements SystemServiceImpl {

    @Override
    public void start() throws IOException {
        Vertx vertx = GlobalContext.getVertx();

        HelloServiceGrpc.HelloServiceImplBase helloServiceImplBase = new HelloServiceGrpc.HelloServiceImplBase() {
            @Override
            public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
                responseObserver.onNext(
                        HelloReply.newBuilder()
                                .setMessage(request.getName()+"xxxxxxxxxx")
                                .build());
                responseObserver.onCompleted();
            }
        };

        VertxServerBuilder vertxServer = VertxServerBuilder.forAddress(vertx, "10.79.19.67", 8080);
        vertxServer.addService(helloServiceImplBase);

        VertxServer rpcServer = vertxServer.build();
        rpcServer.start();
    }
}
