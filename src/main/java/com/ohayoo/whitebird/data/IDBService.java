package com.ohayoo.whitebird.data;

import com.alibaba.fastjson.JSONObject;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.enums.DataType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-26
 */
public interface IDBService {

    void init();

    void stop();

    default JSONObject config(DataType dataType){
        DataType[] dataType1 = GlobalContext.serverConfig().getDataType();
        int index = Integer.MAX_VALUE;
        for (int i = 0; i < dataType1.length; i++) {
            if(dataType1[i] == dataType){
                index = i;
            }
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(GlobalContext.serverConfig().getDataOptionPath()[index]);
            byte[] bytes = fileInputStream.readAllBytes();
            String configStr = new String(bytes, StandardCharsets.UTF_8);
            return JSONObject.parseObject(configStr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("database config init fail! ");
        }
    }
}
