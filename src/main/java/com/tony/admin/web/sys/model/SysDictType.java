package com.tony.admin.web.sys.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 业务字典实体类
 *
 * @author Guoqing.Lee
 * @date 2019年1月16日 下午1:26:16
 */
public class SysDictType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer id;
    private String dicttypeId;
    private String dicttypeName;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date opTime;
    /**
     * 删除标记(0:正常;1:删除;)
     */
    private String delFlag;

    public Date getCreateTime() {
        return createTime == null ? null : (Date) createTime.clone();
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime == null ? null : (Date) createTime.clone();
    }

    public Date getOpTime() {
        return opTime == null ? null : (Date) opTime.clone();
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime == null ? null : (Date) opTime.clone();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDicttypeId() {
        return dicttypeId;
    }

    public void setDicttypeId(String dicttypeId) {
        this.dicttypeId = dicttypeId;
    }

    public String getDicttypeName() {
        return dicttypeName;
    }

    public void setDicttypeName(String dicttypeName) {
        this.dicttypeName = dicttypeName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", dicttypeId=").append(dicttypeId);
        sb.append(", dicttypeName=").append(dicttypeName);
        sb.append("]");
        return sb.toString();
    }
}