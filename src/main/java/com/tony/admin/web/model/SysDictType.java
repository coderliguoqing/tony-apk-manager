package com.tony.admin.web.model;

import com.tony.admin.web.common.security.entity.DataEntity;

/**
 * 业务字典实体类
 * @author Guoqing.Lee
 * @date 2019年1月16日 下午1:26:16
 *
 */
public class SysDictType extends DataEntity {
	private Integer id;
	
    private String dicttypeId;

    private String dicttypeName;

    private static final long serialVersionUID = 1L;
    
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