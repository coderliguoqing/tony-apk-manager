package com.tony.admin.web.im.entity;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Guoqing
 * @desc 用户群组基本信息
 * @date 2019/7/30
 */
public class SysGroup implements Serializable {

    private static final long serialVersionUID = -1462780105947528965L;
    /**
     * 主键ID
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    /**
     * 群组名称
     */
    private String groupName;
    /**
     * 群组头像
     */
    private String avatar;
    /**
     * 创建人ID
     */
    private Integer userId;
    /**
     * 群描述
     */
    private String description;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除标记 1：删除 0：未删除
     */
    private Integer delFalg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getDelFalg() {
        return delFalg;
    }

    public void setDelFalg(Integer delFalg) {
        this.delFalg = delFalg;
    }
}
