package com.ohayoo.whitebird.grpc;

import com.ohayoo.whitebird.annotate.GrpcServiceAnnotate;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.boot.SystemServiceImpl;
import com.ohayoo.whitebird.compoent.ClassScanUtil;
import com.ohayoo.whitebird.compoent.LocalIpUtil;
import io.vertx.core.Vertx;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

import static com.ohayoo.whitebird.compoent.ClassScanUtil.getClzFromPkg;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-02
 */
public class GrpcSystemService implements SystemServiceImpl {
    private static Logger log = LoggerFactory.getLogger(GrpcSystemService.class);
    private VertxServer rpcServer;

    @Override
    public void startService() throws IOException {
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
                log.error(e.getMessage());
                throw new RuntimeException("biz service init fail");
            }
        }
    }

    @Override
    public void stopService() {
        rpcServer.shutdown();
    }
}
