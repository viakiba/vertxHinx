package com.ohayoo.whitebird.data;

import com.alibaba.fastjson.JSONObject;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.compoent.LoggerUtil;
import com.ohayoo.whitebird.enums.DataType;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-26
 */
public interface IDBService {
    Logger logBase = LoggerFactory.getLogger(IDBService.class);

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
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(GlobalContext.serverConfig().getDataOptionPath()[index]);
            byte[] bytes = fileInputStream.readAllBytes();
            String configStr = new String(bytes, StandardCharsets.UTF_8);
            return JSONObject.parseObject(configStr);
        } catch (Exception e) {
            LoggerUtil.error(e.getMessage());
            throw new RuntimeException("database config init fail! ");
        }finally {
            if(fileInputStream !=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logBase.error("读取异常 ",e);
                }
            }
        }
    }

    default void saveObj(Object obj, String collectionName, Handler<AsyncResult<String>> handler){
        throw new RuntimeException("database client 未实现! ");
    }

    default void selectByOpenId(String id, String collectionName, Handler<AsyncResult<JsonObject>> handler){
        throw new RuntimeException("database client 未实现! ");
    }

    default void deleteObj(String keyName, Object keyValue, String collectionName, Handler<AsyncResult<String>> handler){
        throw new RuntimeException("database client 未实现! ");
    }
}
