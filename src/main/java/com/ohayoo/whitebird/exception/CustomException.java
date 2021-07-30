package com.ohayoo.whitebird.exception;

import com.ohayoo.whitebird.generate.message.CommonMessage;

/**
 * @description CustomException
 * @author huangpeng.12@bytedance.com
 */
public class CustomException extends Exception {

    private int statusCode ;

    public CustomException(CommonMessage.ErrorCode messageCode){
        statusCode = messageCode.getNumber();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
