package com.ohayoo.whitebird;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.ohayoo.whitebird.config.ExcelCommonConfig;
import com.ohayoo.whitebird.config.ServerSystemConfig;
import com.ohayoo.whitebird.message.MsgSystemService;
import com.ohayoo.whitebird.network.NetSystemService;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.data.DataSystemService;
import com.ohayoo.whitebird.excel.ExcelSystemService;
import com.ohayoo.whitebird.grpc.GrpcSystemService;
import com.ohayoo.whitebird.player.PlayerSystemService;
import com.ohayoo.whitebird.schedule.ScheduleSystemService;
import com.ohayoo.util.HttpUtil;
import io.vertx.core.json.jackson.DatabindCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class Start {
    private static Logger log = LoggerFactory.getLogger(Start.class);
    public static void main(String[] args) {
        //初始化 服务基础配置
        ServerSystemConfig serverSystemConfig = new ServerSystemConfig();
        GlobalContext.addSystemService(serverSystemConfig);
        serverSystemConfig.startService();
        //初始化 vertx
        GlobalContext.initVertx(h -> init());
    }

    public static void init()  {
        try {
            //初始化 配置表
            ExcelSystemService excelSystemService = new ExcelSystemService();
            GlobalContext.addSystemService(excelSystemService);
            excelSystemService.startService();
            //初始化 消息服务
            MsgSystemService msgSystemService = new MsgSystemService();
            GlobalContext.addSystemService(msgSystemService);
            msgSystemService.startService();
            //初始化 定时调度
            ScheduleSystemService scheduleSystemService = new ScheduleSystemService();
            GlobalContext.addSystemService(scheduleSystemService);
            scheduleSystemService.startService();
            //初始化 第三方组件
            initThirdComponent();
            //初始化 数据服务
            DataSystemService dataSystemService = new DataSystemService();
            GlobalContext.addSystemService(dataSystemService);
            dataSystemService.startService();
            //初始化 玩家服务
            PlayerSystemService playerSystemService = new PlayerSystemService();
            GlobalContext.addSystemService(playerSystemService);
            playerSystemService.startService();
            // GRPC
            GrpcSystemService grpcSystemService = new GrpcSystemService();
            GlobalContext.addSystemService(grpcSystemService);
            grpcSystemService.startService();
            //初始化 网络服务
            NetSystemService netSystemService = new NetSystemService();
            GlobalContext.addSystemService(netSystemService);
            netSystemService.startService();
            // shutdownHook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> GlobalContext.getSystemServiceAll().stream().forEach(x -> {
                x.stopService();
            })));
        }catch (Exception e){
            log.error("启动异常 ",e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void initThirdComponent() {
        HttpUtil.initAsyncClient(2,2000,200);
        ExcelCommonConfig.INSTANCE.initConstant();
        //vertx JsonObject 忽略json中的未知字段
        DatabindCodec.mapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        DatabindCodec.prettyMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //敏感词参数
//        SensitiveUtil.init(renameAk,renamesk,"218475","218536");
        //游戏云登录参数
//        InitService.init(gameCloudAppID+"",gameCloudSecret,5000);
        //以下是两种埋点配置
        //参数看文档 https://datarangers.com.cn/help/doc?lid=2230&did=90158  埋点平台信息设定埋点参数登录参数
//        DotLogUtil.init(
//                218475,"a621650b92c66f0c3d959c0168f40fa8",
//                "nostopgame_ios","com.whitebird.bxcs.ios.ohayoo",
//                218536, "e18c1c24e674b975ad1b13dd9a4ccac0",
//                "nostopgame_android","com.whitebird.bxcs.android.ohayoo",
//                10,10,2000);
        //kafka中继模式 需要初始化参数
//        DoKafkaLogProducerAgent.initKafkaProducerClient(
//                kafkaIpHost, kafkaAck, kafkaBufferMemory, kafkaRetries,
//                kafkaBatchSize, kafkaLingerMs, kafkaRequestTimeoutMs);
    }

}