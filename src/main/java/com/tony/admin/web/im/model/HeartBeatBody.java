package com.tony.admin.web.im.model;

/**
 * 心跳请求
 * @author Guoqing
 */
public class HeartBeatBody extends Message {


    /**
     * 心跳字节
     */
    private String message;

    /**
     * 消息类型
     */
    private Integer msgType;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }
}
