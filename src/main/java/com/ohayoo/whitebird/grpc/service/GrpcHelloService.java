package com.ohayoo.whitebird.grpc.service;

import com.ohayoo.whitebird.grpc.IGrpcService;
import com.ohayoo.whitebird.annotate.GrpcServiceAnnotate;
import io.vertx.grpc.VertxServerBuilder;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-06
 */
@GrpcServiceAnnotate
public class GrpcHelloService implements IGrpcService {
    @Override
    public void init(VertxServerBuilder vertxServer) {
//        HelloServiceGrpc.HelloServiceImplBase helloServiceImplBase = new HelloServiceGrpc.HelloServiceImplBase() {
//            @Override
//            public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
//                responseObserver.onNext(
//                        HelloReply.newBuilder()
//                                .setMessage(request.getName()+"xxxxxxxxxx")
//                                .build());
//                responseObserver.onCompleted();
//            }
//        };
//        vertxServer.addService(helloServiceImplBase);
    }

}
