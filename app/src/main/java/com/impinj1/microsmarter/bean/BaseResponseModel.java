package com.impinj1.microsmarter.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Auther:WangHao
 * E-Mail:wanghaotopsun@163.com
 * Time:2018/1/4  14:46
 * <p>
 * Description:网络请求响应实体模板
 */

public class BaseResponseModel<T> {
    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private T data;

    public BaseResponseModel() {
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponseModel{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
