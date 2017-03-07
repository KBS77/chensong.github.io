package com.kbs.sohu.hushuov1.model.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by cs on 2017/02/10.
 */

public class UserInfo extends BmobUser{

    private String name;// 用户名称
    private String hxid;// 环信id
    private String nick;// 用户的昵称
    private String photo;// 头像

    private String password2;

    public UserInfo() {
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

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getPassword2() {
        return password2;
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
