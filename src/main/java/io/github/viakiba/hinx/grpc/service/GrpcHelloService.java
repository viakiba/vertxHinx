package io.github.viakiba.hinx.grpc.service;

import io.github.viakiba.hinx.annotate.GrpcServiceAnnotate;
import io.github.viakiba.hinx.generate.grpc.HelloReply;
import io.github.viakiba.hinx.generate.grpc.HelloRequest;
import io.github.viakiba.hinx.generate.grpc.HelloServiceGrpc;
import io.github.viakiba.hinx.grpc.IGrpcService;
import io.grpc.stub.StreamObserver;
import io.vertx.grpc.VertxServerBuilder;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-08-06
 */
@GrpcServiceAnnotate
public class GrpcHelloService implements IGrpcService {
    @Override
    public void init(VertxServerBuilder vertxServer) {
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
        vertxServer.addService(helloServiceImplBase);
    }

}
