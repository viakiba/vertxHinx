package com.ohayoo.whitebird.network.verticle;

import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.enums.NetType;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-26
 */
public interface BaseServerVerticle {
    default String configJson(NetType netType){
        NetType[] netTypes = GlobalContext.serverConfig().getNetType();
        int index = Integer.MAX_VALUE;
        for (int i = 0; i < netTypes.length; i++) {
            if(netTypes[i] == netType){
                index = i;
            }
        }
        if(index> netTypes.length){
           throw new RuntimeException("网络配置计算异常！");
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(GlobalContext.serverConfig().getNetConfig()[index]);
            byte[] bytes = fileInputStream.readAllBytes();
            String configStr = new String(bytes, StandardCharsets.UTF_8);
            return configStr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("database config init fail! ");
        }
    }
}
