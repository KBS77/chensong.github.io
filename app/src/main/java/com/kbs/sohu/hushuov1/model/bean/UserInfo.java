package com.kbs.sohu.hushuov1.model.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by cs on 2017/02/10.
 */

public class UserInfo extends BmobUser{

    private String name;// 用户名称
    private String hxid;// 环信id
    private String nick;// 用户的昵称
    private BmobFile photo;// 头像
    public UserInfo() {
    }

    public UserInfo(String name) {
        this.name = name;
        this.hxid = name;
    }

    public UserInfo(String name, String nick , BmobFile photo) {
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

    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
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
