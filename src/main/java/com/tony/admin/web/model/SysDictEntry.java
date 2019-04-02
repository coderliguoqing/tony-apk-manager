package com.tony.admin.web.model;

import com.tony.admin.web.common.security.entity.DataEntity;

/**
 * 业务字典项实体类
 * @author Guoqing.Lee
 * @date 2019年1月16日 下午1:25:46
 *
 */
public class SysDictEntry extends DataEntity {
	private Integer id;
	
    private String dicttypeId;

    private String dictId;

    private String dictName;

    private Integer status;

    private Integer sort;

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

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", dicttypeId=").append(dicttypeId);
        sb.append(", dictId=").append(dictId);
        sb.append(", dictName=").append(dictName);
        sb.append(", status=").append(status);
        sb.append(", sort=").append(sort);
        sb.append("]");
        return sb.toString();
    }
}