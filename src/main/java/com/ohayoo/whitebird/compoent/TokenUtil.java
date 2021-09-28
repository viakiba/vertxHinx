package com.ohayoo.whitebird.compoent;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.ohayoo.whitebird.exception.CustomException;
import com.ohayoo.whitebird.generate.message.CommonMessage;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TokenUtil {

    public static final String secret = "ssssssssssssssssssssssssssssssss";

    public static String buildToken(String openId, long id, String gameCloudId) throws JoseException {
        JsonWebSignature jws = new JsonWebSignature();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id",id);
        jsonObject.addProperty("openId",openId);
        jsonObject.addProperty("createTime",System.currentTimeMillis());
        jsonObject.addProperty("gameCloudId",gameCloudId);
        jws.setPayload(jsonObject.toString());
        jws.setKey(new HmacKey(secret.getBytes(StandardCharsets.UTF_8)));
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        String jwt = jws.getCompactSerialization();
        return jwt;
    }

    public static String checkToken(String token) throws Exception {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setKey(new HmacKey(secret.getBytes(StandardCharsets.UTF_8)));
        jws.setCompactSerialization(token);
        String payload = jws.getPayload();
        Map jsonObject = JSON.parseObject(payload, Map.class);
        if(jws.verifySignature()){
            String openId = (String) jsonObject.get("openId");
            return openId;
        }else{
           throw new CustomException(CommonMessage.ErrorCode.SUCCESS);
        }
    }

    public static String buildGmToken() throws JoseException {
        JsonWebSignature jws = new JsonWebSignature();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("createTime",String.valueOf(System.currentTimeMillis()));
        jws.setPayload(jsonObject.toString());
        jws.setKey(new HmacKey(secret.getBytes(StandardCharsets.UTF_8)));
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        String jwt = jws.getCompactSerialization();
        return jwt;
    }

    public static long checkGmToken(String token) throws Exception {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setKey(new HmacKey(secret.getBytes(StandardCharsets.UTF_8)));
        jws.setCompactSerialization(token);
        String payload = jws.getPayload();
        Map jsonObject = JSON.parseObject(payload, Map.class);
        if(jws.verifySignature()){
            String createTime = (String) jsonObject.get("createTime");
            return Long.parseLong(createTime);
        }else{
            return 0L;
        }
    }
}
