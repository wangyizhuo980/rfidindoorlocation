package com.wangyizhuo.rfidindoorlocation.db;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

/**
 * Created by wangyizhuo on 2017/8/14.
 */

public class Label implements Serializable {
    //id of SQLite
    private int id = 0;
    //所属账号id
    private int userId = 0;
    //标签名称
    private String labelName = "标签";
    //标签RFID码
    private String rfidCode = "0";
    //纬度
    private double latitude = 0;
    //经度
    private double longitude = 0;
    //位置信息
    private String location = "无";
    //信息更新时间
    private Date date;

    private int imageSrc;
    private URL imageURL;

    public Label() {
        date = new Date();
    }

    public Label(int id, int userId, String labelName, String rfidCode, double latitude,
                 double longitude, String location, Date date, URL imageURL) {
        this.id = id;
        this.userId = userId;
        this.labelName = labelName;
        this.rfidCode = rfidCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.date = date;
        this.imageURL = imageURL;
    }
    public Label(int id, int userId, String labelName, String rfidCode, double latitude,
                 double longitude, String location, Date date, int imageSrc) {
        this.id = id;
        this.userId = userId;
        this.labelName = labelName;
        this.rfidCode = rfidCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.date = date;
        this.imageSrc = imageSrc;
    }

    public int getImageSrc() {
        return imageSrc;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setImageSrc(int imageSrc) {
        this.imageSrc = imageSrc;
    }

    public URL getImageURL() {
        return imageURL;
    }

    public void setImageURL(URL imageURL) {
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getRfidCode() {
        return rfidCode;
    }

    public void setRfidCode(String rfidCode) {
        this.rfidCode = rfidCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
