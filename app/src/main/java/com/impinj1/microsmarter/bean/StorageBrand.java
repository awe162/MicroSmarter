package com.impinj1.microsmarter.bean;

/**
 * Created by Administrator on 2017/12/11 0011.
 */

public class StorageBrand  {

    private String brandId;

    private String brandName;

    private int counrDuration;

    private int countMileage;

    private int tireCount;

    private int tirePosition;

    private int totalDistance;

    private int totalTime;

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setCounrDuration(int counrDuration) {
        this.counrDuration = counrDuration;
    }

    public void setCountMileage(int countMileage) {
        this.countMileage = countMileage;
    }

    public void setTireCount(int tireCount) {
        this.tireCount = tireCount;
    }

    public void setTirePosition(int tirePosition) {
        this.tirePosition = tirePosition;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public int getCounrDuration() {
        return counrDuration;
    }

    public int getCountMileage() {
        return countMileage;
    }

    public int getTireCount() {
        return tireCount;
    }

    public int getTirePosition() {
        return tirePosition;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalTime() {
        return totalTime;
    }

    @Override
    public String toString() {
        return "StorageBrand{" +
                "brandId='" + brandId + '\'' +
                ", brandName='" + brandName + '\'' +
                ", counrDuration=" + counrDuration +
                ", countMileage=" + countMileage +
                ", tireCount=" + tireCount +
                ", tirePosition=" + tirePosition +
                ", totalDistance=" + totalDistance +
                ", totalTime=" + totalTime +
                '}';
    }
}
