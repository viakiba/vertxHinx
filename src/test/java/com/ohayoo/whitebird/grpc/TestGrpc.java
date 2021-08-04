package com.ohayoo.whitebird.grpc;

import com.ohayoo.whitebird.base.TestBase;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.config.ServerSystemConfig;
import com.ohayoo.whitebird.generate.grpc.HelloReply;
import com.ohayoo.whitebird.generate.grpc.HelloRequest;
import com.ohayoo.whitebird.generate.grpc.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import io.vertx.grpc.VertxChannelBuilder;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-02
 */
public class TestGrpc extends TestBase {

    static {
        ServerSystemConfig serverSystemConfig = new ServerSystemConfig();
        GlobalContext.addSystemService(serverSystemConfig);
        serverSystemConfig.start();
        GlobalContext.initVertx();
    }

    public static void main(String[] args) {
        ManagedChannel channel;
        channel = VertxChannelBuilder
                .forAddress(GlobalContext.getVertx(), "127.0.0.1", GlobalContext.serverConfig().getGrpcPort())
                .usePlaintext()
                .build();
        // Get a stub to use for interacting with the remote service
        HelloServiceGrpc.HelloServiceStub stub = HelloServiceGrpc.newStub(channel);
        HelloRequest request = HelloRequest.newBuilder().setName("Julien").build();
        GlobalContext.getVertx().setPeriodic(10000,h ->{
            stub.sayHello(request, new StreamObserver<HelloReply>() {
                private HelloReply helloReply;

                @Override
                public void onNext(HelloReply helloReply) {
                    this.helloReply = helloReply;
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("Coult not reach server " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("Got the server response: " + helloReply.getMessage());
                }
            });
        });
    }
}
