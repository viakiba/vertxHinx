package com.ohayoo.whitebird.compoent;

import cn.hutool.core.lang.Filter;
import cn.hutool.core.net.NetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.LinkedHashSet;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-06
 */
public class LocalIpUtil {
    private static Logger log = LoggerFactory.getLogger(LocalIpUtil.class);

    public static String get10BeginIp(){
        LinkedHashSet<String> hostAddress = NetUtil.toIpList(NetUtil.localAddressList(new Filter<InetAddress>() {
            @Override
            public boolean accept(InetAddress inetAddress) {
                String hostAddress1 = inetAddress.getHostAddress();
                if(hostAddress1.startsWith("10.")){
                    return true;
                }
                return false;
            }
        }));
        if(hostAddress.size()!=1){
            log.info("获取本地IP " + hostAddress);
            throw new RuntimeException("IP 获取异常！");
        }
        String[] strings = hostAddress.toArray(new String[1]);
        log.info("获取本地IP " + strings);
        return strings[0];
    }
}
