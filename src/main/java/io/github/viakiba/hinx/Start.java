package io.github.viakiba.hinx;

import io.github.viakiba.hinx.boot.GlobalContext;
import io.github.viakiba.hinx.config.ServerSystemConfig;
import io.github.viakiba.hinx.data.DataSystemService;
import io.github.viakiba.hinx.excel.ExcelSystemService;
import io.github.viakiba.hinx.grpc.GrpcSystemService;
import io.github.viakiba.hinx.message.MsgSystemService;
import io.github.viakiba.hinx.network.NetSystemService;
import io.github.viakiba.hinx.player.PlayerSystemService;
import io.github.viakiba.hinx.schedule.ScheduleSystemService;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-07-23
 */
public class Start {

    public static void main(String[] args) {
        //初始化 服务基础配置
        ServerSystemConfig serverSystemConfig = new ServerSystemConfig();
        GlobalContext.addSystemService(serverSystemConfig);
        serverSystemConfig.start();
        //初始化 vertx
        GlobalContext.initVertx(h -> init());
    }

    public static void init()  {
        try {
            //初始化 配置表
            ExcelSystemService excelSystemService = new ExcelSystemService();
            GlobalContext.addSystemService(excelSystemService);
            excelSystemService.start();
            //初始化 消息服务
            MsgSystemService msgSystemService = new MsgSystemService();
            GlobalContext.addSystemService(msgSystemService);
            msgSystemService.start();
            //初始化 定时调度
            ScheduleSystemService scheduleSystemService = new ScheduleSystemService();
            GlobalContext.addSystemService(scheduleSystemService);
            scheduleSystemService.start();
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
            Runtime.getRuntime().addShutdownHook(new Thread(() -> GlobalContext.getSystemServiceAll().stream().forEach(x -> {
                x.stop();
            })));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void initThirdComponent() {

    }

}