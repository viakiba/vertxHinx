package com.ohayoo.whitebird.gmadmin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-04-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerData {
    String ip;
    int port;
    String workId;
    String centerId;
}
