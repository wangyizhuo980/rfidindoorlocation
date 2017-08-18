package com.wangyizhuo.rfidindoorlocation.db;

import java.net.URL;

/**
 * Created by wangyizhuo on 2017/8/14.
 */

public class User {
    //id of SQLite
    private int id;
    //账号
    private int userCode;
    //昵称
    private String userName;
    //用户名
    private String loginName;
    //密码
    private String password;
    //图像
    private Integer imageSrc;
    private URL imageURL;

    public User(int id, int userCode, String userName, String loginName, String password,
                URL imageURL) {
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
        this.loginName = loginName;
        this.password = password;
        this.imageURL = imageURL;
    }
    public User(int id, int userCode, String userName, String loginName, String password,
                int imageSrc) {
        this.id = id;
        this.userCode = userCode;
        this.userName = userName;
        this.loginName = loginName;
        this.password = password;
        this.imageSrc = imageSrc;
    }

    public Integer getImageSrc() {
        return imageSrc;
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

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
