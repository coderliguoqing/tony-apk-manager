package com.tony.admin.web.im.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;


public class UserMessageEntity {
	private static final long serialVersionUID = 1L;
	
 
	//发送人
	private String senduser;
	//发送人昵称或姓名
	private String sendusername;
	//发送人头像
	private String avatar;
	//接收人
	private String receiveuser;
	//群ID
	private String groupid;
	//是否已读 0 未读  1 已读
	private Integer isread;
	//类型 0 单聊消息  1 群消息
	private Integer type=0;
	//消息内容
	private String content;
	//创建人
	private Integer createuser;
	//创建s时间
	private String createdate;
	//更新时间
	private String updatedate;
 
	public String getSendusername() {
		return sendusername;
	}
	public void setSendusername(String sendusername) {
		this.sendusername = sendusername;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	/**
	 * 设置：发送人
	 */
	public void setSenduser(String senduser) {
		this.senduser = senduser;
	}
	/**
	 * 获取：发送人
	 */
	public String getSenduser() {
		return senduser;
	}
	/**
	 * 设置：接收人
	 */
	public void setReceiveuser(String receiveuser) {
		this.receiveuser = receiveuser;
	}
	/**
	 * 获取：接收人
	 */
	public String getReceiveuser() {
		return receiveuser;
	}
	/**
	 * 设置：群ID
	 */
	public void setGroupid(String groupid) {
		if(StringUtils.isNotEmpty(groupid)){
			setType(1);
		}
		this.groupid = groupid;
	}
	/**
	 * 获取：群ID
	 */
	public String getGroupid() {
		return groupid;
	}
	/**
	 * 设置：是否已读 0 未读  1 已读
	 */
	public void setIsread(Integer isread) {
		this.isread = isread;
	}
	/**
	 * 获取：是否已读 0 未读  1 已读
	 */
	public Integer getIsread() {
		return isread;
	}
	/**
	 * 设置：类型 0 单聊消息  1 群消息
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型 0 单聊消息  1 群消息
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：消息内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：消息内容
	 */
	public String getContent() {
		return content;
	}
	 

	public Integer getCreateuser() {
		return createuser;
	}
	public void setCreateuser(Integer createuser) {
		this.createuser = createuser;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}
	@Override
	public String toString() {
		return  ReflectionToStringBuilder.toString(this);
	}
}
