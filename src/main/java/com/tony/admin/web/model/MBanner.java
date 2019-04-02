package com.tony.admin.web.model;

public class MBanner {
	private Integer id;
	private String code;
	private String type;
	private String title;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "MBanner [id=" + id + ", code=" + code + ", type=" + type + ", title=" + title + "]";
	}
	
}
