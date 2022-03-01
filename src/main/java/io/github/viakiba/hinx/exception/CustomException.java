package io.github.viakiba.hinx.exception;

import io.github.viakiba.hinx.generate.message.CommonMessage;

/**
 * @description CustomException
 * @author viakiba@gmail.com
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