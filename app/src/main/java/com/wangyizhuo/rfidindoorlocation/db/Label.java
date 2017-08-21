package com.wangyizhuo.rfidindoorlocation.db;

import com.baidu.mapapi.model.LatLng;

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
    //经纬度
    private LatLng latLng = null;
    //位置信息
    private String location = "无";
    //信息更新时间
    private Date date;

    private int imageSrc;
    private URL imageURL;

    public Label() {
        date = new Date();
    }


    public int getImageSrc() {
        return imageSrc;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(double latitude, double longitude) {
        this.latLng = new LatLng(latitude, longitude);
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
