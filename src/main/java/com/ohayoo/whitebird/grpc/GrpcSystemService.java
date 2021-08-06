package com.ohayoo.whitebird.grpc;

import com.ohayoo.whitebird.annotate.BizServiceAnnotate;
import com.ohayoo.whitebird.annotate.GrpcServiceAnnotate;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.boot.SystemServiceImpl;
import com.ohayoo.whitebird.compoent.LocalIpUtil;
import com.ohayoo.whitebird.generate.grpc.HelloReply;
import com.ohayoo.whitebird.generate.grpc.HelloRequest;
import com.ohayoo.whitebird.generate.grpc.HelloServiceGrpc;
import com.ohayoo.whitebird.service.abs.BaseService;
import io.grpc.stub.StreamObserver;
import io.vertx.core.Vertx;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

import java.io.IOException;
import java.util.Set;

import static com.ohayoo.whitebird.compoent.ClassScanUtil.getClzFromPkg;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-02
 */
public class GrpcSystemService implements SystemServiceImpl {

    private VertxServer rpcServer;

    @Override
    public void start() throws IOException {
        Vertx vertx = GlobalContext.getVertx();
        VertxServerBuilder vertxServer = VertxServerBuilder.forAddress(vertx, LocalIpUtil.get10BeginIp(), GlobalContext.serverConfig().getGrpcPort());
        addGrpcService(vertxServer);
        rpcServer = vertxServer.build();
        rpcServer.start();
    }

    private void addGrpcService(VertxServerBuilder vertxServer) {
        String[] bizServicePkgPath = GlobalContext.serverConfig().getGrpcServicePkgPath();
        for(String path : bizServicePkgPath) {
            Set<Class<?>> glazes = getClzFromPkg(path, GrpcServiceAnnotate.class);
            try {
                for (Class<?> c : glazes) {
                    Object bizService = c.getDeclaredConstructor().newInstance();
                    IGrpcService iGrpcService = (IGrpcService) bizService;
                    iGrpcService.init(vertxServer);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("biz service init fail");
            }
        }
    }

    @Override
    public void stop() {
        rpcServer.shutdown();
    }
}
