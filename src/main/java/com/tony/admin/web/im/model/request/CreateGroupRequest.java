package com.tony.admin.web.im.model.request;

import com.tony.admin.web.im.model.Message;

import java.util.List;

/**
 * @author Guoqing
 * @desc
 * @date 2019/8/5
 */
public class CreateGroupRequest extends Message {

    private static final long serialVersionUID = -6650837718160258995L;
    /**
     * 群主ID
     */
    private Integer userId;
    /**
     * 加入用户ID
     */
    private List<Integer> joinUserId;
    /**
     * 群聊名称
     */
    private String groupName;
    /**
     * 群聊头像
     */
    private String avatar;
    /**
     * 描述
     */
    private String desc;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Integer> getJoinUserId() {
        return joinUserId;
    }

    public void setJoinUserId(List<Integer> joinUserId) {
        this.joinUserId = joinUserId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "CreateGroupRequest{" +
                "userId=" + userId +
                ", joinUserId=" + joinUserId +
                ", groupName='" + groupName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
