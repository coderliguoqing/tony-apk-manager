package com.tony.admin.web.im.model;

import com.tony.admin.web.sys.model.SysUser;

import java.io.Serializable;
import java.util.List;

/**
 * 群组
 * @author Guoqing 创建时间：2019年7月24日 14:04
 */
public class Group implements Serializable {

    private static final long serialVersionUID = -2293900751713398639L;
    /**
     * 群组ID
     */
    private String groupId;
    /**
     * 群组名称
     */
    private String groupName;
    /**
     * 群组头像
     */
    private String avatar;
    /**
     * 在线人数
     */
    private Integer online;
    /**
     * 群组用户
     */
    private List<SysUser> users;

    public Group(){}

    public Group(String groupId, String name){
        this.groupId = groupId;
        this.groupName = groupName;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public List<SysUser> getUsers() {
        return users;
    }

    public void setUsers(List<SysUser> users) {
        this.users = users;
    }
}
