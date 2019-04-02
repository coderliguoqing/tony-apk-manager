package com.tony.admin.web.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限树展示类
 * @author Guoqing.Lee
 * @date 2019年3月21日 上午9:08:11
 *
 */
public class RightTreeVo {
	
	private Integer id;
	
	private Boolean isShow;
	
	private String name;
	
	private List<RightTreeVo> children = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RightTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<RightTreeVo> children) {
		this.children = children;
	}

	

}
