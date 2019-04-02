package com.tony.admin.web.common.security.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 数据Entity类
 *
 * @author GuoqingLee
 */
public abstract class DataEntity extends BaseEntity {

    /**
     * 删除标记0：正常
     */
    public static final String DEL_FLAG_NORMAL = "0";
    /**
     * 删除标记1：删除
     */
    public static final String DEL_FLAG_DELETE = "1";

    private static final long serialVersionUID = -4839183827907638164L;

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

    public DataEntity() {
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    @Override
    public void preInsert() {
        this.opTime = new Date();
        this.createTime = this.opTime;
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    @Override
    public void preUpdate() {
        this.opTime = new Date();
    }

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

    @JsonIgnore
    @Length(min = 1, max = 1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

}
