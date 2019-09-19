package com.tony.admin.web.im.model.request;

import com.tony.admin.web.im.model.Message;

/**
 * 加好友申请
 * @author Guoqing 创建时间：2019年7月24日 15:42
 */
public class JoinFriendRequest extends Message {

    private static final long serialVersionUID = -3833641139109798570L;

    /**
     * 当前用户userId
     */
    private Integer userId;
    /**
     * 添加人的userId
     */
    private Integer joinUserId;
    /**
     * 请求类型  1：请求添加好友，2：响应添加请求
     */
    private Integer joinType;
    /**
     * 响应状态 0：拒绝添加，1：同意添加
     */
    private Integer status;
    /**
     * 描述
     */
    private String desc;

    public JoinFriendRequest() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getJoinUserId() {
        return joinUserId;
    }

    public void setJoinUserId(Integer joinUserId) {
        this.joinUserId = joinUserId;
    }

    public Integer getJoinType() {
        return joinType;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
