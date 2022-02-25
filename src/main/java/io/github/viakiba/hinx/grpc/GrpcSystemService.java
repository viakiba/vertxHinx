package io.github.viakiba.hinx.grpc;

import io.github.viakiba.hinx.annotate.GrpcServiceAnnotate;
import io.github.viakiba.hinx.boot.GlobalContext;
import io.github.viakiba.hinx.boot.SystemServiceImpl;
import io.github.viakiba.hinx.compoent.LocalIpUtil;
import io.github.viakiba.hinx.compoent.ClassScanUtil;
import io.vertx.core.Vertx;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

import java.io.IOException;
import java.util.Set;

import static io.github.viakiba.hinx.compoent.ClassScanUtil.getClzFromPkg;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-02
 */
public class GrpcSystemService implements SystemServiceImpl {

    private VertxServer rpcServer;

    @Override
    public void start() throws IOException {
        int grpcPort = GlobalContext.serverConfig().getGrpcPort();
        if(grpcPort == 0){
            return;
        }
        Vertx vertx = GlobalContext.getVertx();
        VertxServerBuilder vertxServer = VertxServerBuilder.forAddress(vertx, LocalIpUtil.get10BeginIp(), GlobalContext.serverConfig().getGrpcPort());
        addGrpcService(vertxServer);
        rpcServer = vertxServer.build();
        rpcServer.start();
    }

    private void addGrpcService(VertxServerBuilder vertxServer) {
        String[] bizServicePkgPath = GlobalContext.serverConfig().getGrpcServicePkgPath();
        if(bizServicePkgPath == null){
            return;
        }
        for(String path : bizServicePkgPath) {
            Set<Class<?>> glazes = ClassScanUtil.getClzFromPkg(path, GrpcServiceAnnotate.class);
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
