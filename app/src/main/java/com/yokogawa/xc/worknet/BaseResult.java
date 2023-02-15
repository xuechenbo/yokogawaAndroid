package com.yokogawa.xc.worknet;



/**
 * Description: 网络返回消息，最外层解析实体
 */
public class BaseResult{
    private int code;
    private String message;

    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
