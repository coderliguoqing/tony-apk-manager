package com.tony.admin.web.im.entity;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Guoqing
 * @desc  用户好友关系
 * @date 2019/7/30
 */
public class SysUserFriend implements Serializable {

    private static final long serialVersionUID = -2344600249143380116L;
    /**
     * 主键ID
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 好友ID
     */
    private Integer friendId;
    /**
     * 好友分组ID
     */
    private Integer typeId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除标记\\n1：删除\\n0：未删除
     */
    private Integer delFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
