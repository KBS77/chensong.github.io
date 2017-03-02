package com.kbs.sohu.hushuov1.model.bean;

/**
 * Created by tarena on 2017/02/14.
 */

public class GroupInfo {

    private String groupName;
    private String groupId;
    private String invatePerson;

    public GroupInfo(String groupName, String groupId, String invitePerson) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.invatePerson = invitePerson;
    }

    public GroupInfo(){

    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getInvatePerson() {
        return invatePerson;
    }

    public void setInvatePerson(String invitePerson) {
        this.invatePerson = invitePerson;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", invitePerson='" + invatePerson + '\'' +
                '}';
    }
}
