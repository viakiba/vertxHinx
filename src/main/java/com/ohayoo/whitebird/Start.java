package com.ohayoo.whitebird;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.config.ServerSystemConfig;
import com.ohayoo.whitebird.data.DataSystemService;
import com.ohayoo.whitebird.excel.ExcelSystemService;
import com.ohayoo.whitebird.grpc.GrpcSystemService;
import com.ohayoo.whitebird.message.MsgSystemService;
import com.ohayoo.whitebird.network.NetSystemService;
import com.ohayoo.whitebird.player.PlayerSystemService;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.core.spi.cluster.NodeInfo;
import io.vertx.ext.dropwizard.MetricsService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class Start {

    public static void main(String[] args) throws IOException {
        //初始化 服务基础配置
        ServerSystemConfig serverSystemConfig = new ServerSystemConfig();
        GlobalContext.addSystemService(serverSystemConfig);
        serverSystemConfig.start();
        //初始化 vertx
        GlobalContext.initVertx();
        //初始化 配置表
        ExcelSystemService excelSystemService = new ExcelSystemService();
        GlobalContext.addSystemService(excelSystemService);
        excelSystemService.start();
        //初始化 消息服务
        MsgSystemService msgSystemService = new MsgSystemService();
        GlobalContext.addSystemService(msgSystemService);
        msgSystemService.start();
        //初始化 第三方组件
        initThirdComponent();
        //初始化 数据服务
        DataSystemService dataSystemService = new DataSystemService();
        GlobalContext.addSystemService(dataSystemService);
        dataSystemService.start();
        //初始化 玩家服务
        PlayerSystemService playerSystemService = new PlayerSystemService();
        GlobalContext.addSystemService(playerSystemService);
        playerSystemService.start();
        // GRPC
        GrpcSystemService grpcSystemService = new GrpcSystemService();
        GlobalContext.addSystemService(grpcSystemService);
        grpcSystemService.start();
        //初始化 网络服务
        NetSystemService netSystemService = new NetSystemService();
        GlobalContext.addSystemService(netSystemService);
        netSystemService.start();
        // shutdownHook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> GlobalContext.getSystemServiceAll().stream().forEach(x ->{
            x.stop();
        })));
    }

    private static void initThirdComponent() {

    }

}