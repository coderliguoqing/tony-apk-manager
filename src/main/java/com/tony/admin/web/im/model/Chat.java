package com.tony.admin.web.im.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Guoqing
 * @desc 当前聊天窗口对象，实现排序，按照更新时间降序排列
 * @date 2019/7/31
 */
public class Chat implements Comparable<Chat>, Serializable {

    private static final long serialVersionUID = 6945751779688847705L;
    /**
     * 对话对象ID，userId或者groupId
     */
    private Integer chatId;
    /**
     * 对话对象名称，用户的nick或者groupName
     */
    private String chatName;
    /**
     * 聊天类型(0:未知,1: 群聊,2:私聊)
     */
    private Integer chatType;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 最新消息内容
     */
    private String content;
    /**
     * 最新消息的类型
     */
    private Integer contentType;
    /**
     * 消息更新时间
     */
    private Date updateTime;
    /**
     * 最新消息发送用户ID
     */
    private Integer fromUserId;
    /**
     * 最新消息发送昵称
     */
    private String fromNick;

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public Integer getChatType() {
        return chatType;
    }

    public void setChatType(Integer chatType) {
        this.chatType = chatType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromNick() {
        return fromNick;
    }

    public void setFromNick(String fromNick) {
        this.fromNick = fromNick;
    }

    /**
     * 按照更新时间降序排列
     * @param chat
     * @return
     */
    @Override
    public int compareTo(Chat chat) {
        return (int) (chat.updateTime.getTime() - this.updateTime.getTime());
    }
}
