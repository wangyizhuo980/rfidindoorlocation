package com.wangyizhuo.rfidindoorlocation.db;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by wangyizhuo on 2017/8/21.
 */

public class Map {
    private int id;
    private LatLng startLatLng;
    private LatLng endLatLng;
    private int src;

    public Map() {
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getStartLatLng() {
        return startLatLng;
    }

    public void setStartLatLng(double latitude, double longitude) {
        this.startLatLng = new LatLng(latitude, longitude);
    }

    public LatLng getEndLatLng() {
        return endLatLng;
    }

    public void setEndLatLng(double latitude, double longitude) {
        this.endLatLng = new LatLng(latitude, longitude);
    }
}
