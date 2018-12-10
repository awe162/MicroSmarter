package com.impinj1.microsmarter.bean;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class LoginBean {
    private String realName;   //胎管员姓名
    private int companyID;  //登陆的公司ID
    private String companyName; //登陆的公司名称
    private String phoneNo;    //胎管员手机号

    @Override
    public String toString() {
        return "LoginBean{" +
                "realName='" + realName + '\'' +
                ", companyID='" + companyID + '\'' +
                ", companyName='" + companyName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
