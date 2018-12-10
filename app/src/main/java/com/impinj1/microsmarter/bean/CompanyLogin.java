package com.impinj1.microsmarter.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/11 0011.
 */

public class CompanyLogin  {

    private List<CompanyBean> data;
    private String msg;
    private int code;

    @Override
    public String toString() {
        return "CompanyLogin{" +
                "data=" + data +
                ", msg='" + msg + '\'' +
                ", code=" + code +
                '}';
    }

    public List<CompanyBean> getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public void setData(List<CompanyBean> data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
