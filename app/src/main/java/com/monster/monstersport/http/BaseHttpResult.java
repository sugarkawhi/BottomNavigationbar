package com.monster.monstersport.http;

import java.io.Serializable;

/**
 * 网络请求返回数据类型模板
 *
 * @author zhzy
 * @date 2017/11/2
 */

public class BaseHttpResult<T> implements Serializable {
    //状态码
    private int code;
    //数据结构
    private T data;
    //返回状态信息
    private String message;
    //是否成功
    private boolean success;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
