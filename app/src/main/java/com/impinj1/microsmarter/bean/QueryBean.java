package com.impinj1.microsmarter.bean;

/**
 * Created by Administrator on 2017/8/26.
 */

public class QueryBean {

    private String brand_id;
    private String fst_place_stamp;
    private String last_stamp;
    private String mile_count;
    private String place;
    private String plate_no;
    private String pressure;
    private String temp;


    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public void setFst_place_stamp(String fst_place_stamp) {
        this.fst_place_stamp = fst_place_stamp;
    }

    public void setLast_stamp(String last_stamp) {
        this.last_stamp = last_stamp;
    }

    public void setMile_count(String mile_count) {
        this.mile_count = mile_count;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public String getFst_place_stamp() {
        return fst_place_stamp;
    }

    public String getLast_stamp() {
        return last_stamp;
    }

    public String getMile_count() {
        return mile_count;
    }

    public String getPlace() {
        return place;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public String getPressure() {
        return pressure;
    }

    public String getTemp() {
        return temp;
    }

    @Override
    public String toString() {
        return "QueryBean{" +
                "brand_id='" + brand_id + '\'' +
                ", fst_place_stamp='" + fst_place_stamp + '\'' +
                ", last_stamp='" + last_stamp + '\'' +
                ", mile_count='" + mile_count + '\'' +
                ", place='" + place + '\'' +
                ", plate_no='" + plate_no + '\'' +
                ", pressure='" + pressure + '\'' +
                ", temp='" + temp + '\'' +
                '}';
    }
}
