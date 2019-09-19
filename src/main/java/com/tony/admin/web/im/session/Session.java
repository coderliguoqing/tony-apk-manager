package com.tony.admin.web.im.session;

import java.io.Serializable;

import io.netty.channel.Channel;

public class Session implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private transient Channel session;
	
	private String nid;			//session在本台服务器上的id
	private int source;			//来源 用于区分是websocket还是socket
	private String deviceId;	//客户端ID	（设备号+应用包名）ios为devicetoken
	private String hoString;	//session绑定的ip
	private String account;		//session绑定的账号
	private String platform;	//终端类型
	private String platformVersion;		//终端版本号
	private String appKey;		//客户端key
	private Long bindTime;		//登录时间
	private Long updateTime;	//更新时间
	private String sign;		//签名
	private String location;	//位置信息
	private int status;			//状态
	
	public Channel getSession() {
		return session;
	}
	public void setSession(Channel session) {
		this.session = session;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getHoString() {
		return hoString;
	}
	public void setHoString(String hoString) {
		this.hoString = hoString;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getPlatformVersion() {
		return platformVersion;
	}
	public void setPlatformVersion(String platformVersion) {
		this.platformVersion = platformVersion;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public Long getBindTime() {
		return bindTime;
	}
	public void setBindTime(Long bindTime) {
		this.bindTime = bindTime;
	}
	public Long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
