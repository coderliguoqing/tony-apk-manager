package com.tony.admin.web.im.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 消息类
 * @author Guoqing 创建时间：2019年7月23日 13:25
 */
public class Message implements Serializable {

    private static final long serialVersionUID = -778286473562350488L;
    /**
     * 全局唯一ID
     */
    private String id;

    /**
     * 消息cmd命令码
     */
    private Integer cmd;

    /**
     * 消息创建时间
     */
    private Long createTime;

    /**
     * 扩展字段消息
     */
    private JSONObject extras;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public JSONObject getExtras() {
        return extras;
    }

    public void setExtras(JSONObject extras) {
        this.extras = extras;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", cmd=" + cmd +
                ", createTime=" + createTime +
                ", extras=" + extras +
                '}';
    }
}
