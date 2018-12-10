package com.impinj1.microsmarter.bean;

/**
 * Created by Administrator on 2018/1/11 0011.
 */

public class CompanyBean {
    private String id;
    private String start;
    private String ccompanyID;
    private String phoneNo;
    private String companyName;
    private String contacts;
    private String contactsName;
    private String busNumbers;
    private String remark;
    private String delStatus;
    private int companyID;
    private String adminNumbers;
    private String companyAddress;
    private String rows;
    private String failedBusNum7days;


    @Override
    public String toString() {
        return "CompanyBean{" +
                "id='" + id + '\'' +
                ", start='" + start + '\'' +
                ", ccompanyID='" + ccompanyID + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", companyName='" + companyName + '\'' +
                ", contacts='" + contacts + '\'' +
                ", contactsName='" + contactsName + '\'' +
                ", busNumbers='" + busNumbers + '\'' +
                ", remark='" + remark + '\'' +
                ", delStatus='" + delStatus + '\'' +
                ", companyID=" + companyID +
                ", adminNumbers='" + adminNumbers + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                ", rows='" + rows + '\'' +
                ", failedBusNum7days='" + failedBusNum7days + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getCcompanyID() {
        return ccompanyID;
    }

    public void setCcompanyID(String ccompanyID) {
        this.ccompanyID = ccompanyID;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getBusNumbers() {
        return busNumbers;
    }

    public void setBusNumbers(String busNumbers) {
        this.busNumbers = busNumbers;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public String getAdminNumbers() {
        return adminNumbers;
    }

    public void setAdminNumbers(String adminNumbers) {
        this.adminNumbers = adminNumbers;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getFailedBusNum7days() {
        return failedBusNum7days;
    }

    public void setFailedBusNum7days(String failedBusNum7days) {
        this.failedBusNum7days = failedBusNum7days;
    }
}
