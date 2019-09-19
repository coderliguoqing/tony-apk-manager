package com.tony.admin.web.im.model.request;

import com.tony.admin.web.im.model.Message;

/**
 * @author Guoqing
 * @desc
 * @date 2019/8/2
 */
public class HistoryMessageRequest extends Message {


    private static final long serialVersionUID = -739428924270825570L;
    /**
     * 消息来源ID
     */
    private Integer fromUserId;
    /**
     * 接收方ID
     */
    private Integer toUserId;
    /**
     * 聊天类型
     */
    private Integer chatType;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 每页条数
     */
    private Integer pageSize;

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getChatType() {
        return chatType;
    }

    public void setChatType(Integer chatType) {
        this.chatType = chatType;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "HistoryMessageRequest{" +
                "fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", chatType=" + chatType +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
