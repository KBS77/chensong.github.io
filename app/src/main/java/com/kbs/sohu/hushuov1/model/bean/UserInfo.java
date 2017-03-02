package com.kbs.sohu.hushuov1.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by tarena on 2017/02/10.
 */

public class UserInfo extends BmobObject{

   private String name;// 用户名称
    private String hxid;// 环信id
    private String nick;// 用户的昵称
    private String photo;// 头像
    private String mobliePhoneNumber;
    private String password;
    public UserInfo() {
        this.setTableName("User_infomation");
    }

    public UserInfo(String name) {
        this.name = name;
        this.hxid = name;
    }

    public UserInfo(String name, String nick , String photo) {
        this.name = name;
        this.nick = nick;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHxid() {
        return hxid;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMobliePhoneNumber() {
        return mobliePhoneNumber;
    }

    public void setMobliePhoneNumber(String mobliePhoneNumber) {
        this.mobliePhoneNumber = mobliePhoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", hxid='" + hxid + '\'' +
                ", nick='" + nick + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

}
