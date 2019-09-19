package com.tony.admin.web.im.entity;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Guoqing
 * @desc 用户好友分组类型
 * @date 2019/7/30
 */
public class SysFriendType implements Serializable {


    private static final long serialVersionUID = 3874686594470675545L;
    /**
     * 主键ID
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    /**
     * 好友分组名称
     */
    private String typeName;
    /**
     * 所属用户ID
     */
    private Integer userId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     *删除标记 1：删除 0：未删除
     */
    private Integer delFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
