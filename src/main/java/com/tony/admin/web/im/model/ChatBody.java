package com.tony.admin.web.im.model;

/**
 * 消息类
 * @author Guoqing
 */
public class ChatBody extends Message {

    private static final long serialVersionUID = 5540009110379711207L;
    /**
     * 来源ID
     */
    private Integer from;
    /**
     * 目标ID
     */
    private Integer to;
    /**
     * 聊天类型(0:未知,1: 群聊,2:私聊)
     */
    private Integer chatType;
    /**
     * 群组ID，进在chatType为（1）时需要
     */
    private Integer groupId;
    /**
     * 内容
     */
    private String content;
    /**
     * 消息类型 0:text、1:image、2:voice、3:vedio、4:music、5:news
     */
    private Integer contentType;
    /**
     * 群聊发送消息用户昵称
     */
    private String fromUserNick;
    /**
     * 群聊发送用户头像
     */
    private String fromUserAvatar;

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getChatType() {
        return chatType;
    }

    public void setChatType(Integer chatType) {
        this.chatType = chatType;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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

    public String getFromUserNick() {
        return fromUserNick;
    }

    public void setFromUserNick(String fromUserNick) {
        this.fromUserNick = fromUserNick;
    }

    public String getFromUserAvatar() {
        return fromUserAvatar;
    }

    public void setFromUserAvatar(String fromUserAvatar) {
        this.fromUserAvatar = fromUserAvatar;
    }
}
