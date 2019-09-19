package com.tony.admin.web.im.entity;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Guoqing
 * @desc 群组消息记录
 * @date 2019/7/30
 */
public class SysGroupMessage implements Serializable,Comparable<SysGroupMessage> {

    private static final long serialVersionUID = 783210926189626384L;
    /**
     * 主键ID
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    /**
     * 群组ID
     */
    private Integer groupId;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息类型 0:text、1:image、2:voice、3:vedio、4:music、5:news
     */
    private Integer contentType;
    /**
     * 是否已读，1是，0否
     */
    private Integer readFlag;
    /**
     * 是否撤回，1是 0否
     */
    private Integer backFlag;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否删除，1是 0否'
     */
    private Integer delFlag;
    /**
     * 用户昵称
     */
    @Transient
    private String userNick;
    /**
     * 用户头像
     */
    @Transient
    private String userAvatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public Integer getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(Integer readFlag) {
        this.readFlag = readFlag;
    }

    public Integer getBackFlag() {
        return backFlag;
    }

    public void setBackFlag(Integer backFlag) {
        this.backFlag = backFlag;
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

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    @Override
    public int compareTo(SysGroupMessage message) {
        return (int)(this.createTime.getTime() - message.createTime.getTime());
    }
}
