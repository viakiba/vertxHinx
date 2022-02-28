package io.github.viakiba.hinx.compoent;

import cn.hutool.core.lang.Filter;
import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.util.LinkedHashSet;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-08-06
 */
@Slf4j
public class LocalIpUtil {
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
