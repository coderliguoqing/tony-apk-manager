package com.tony.admin.web.im.model;

import java.io.Serializable;

public class MessageWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private MessageProtocol protocol;
	private String sessionId;	//请求人
	private String reSessionId;	//接收人
	private int source;			//来源 用于区分是websocket还是socket
	private Object body;
	
	public MessageWrapper() {}
	
	public MessageWrapper(MessageProtocol protocol, String sessionId, String reSessionId, Object object) {
		this.protocol = protocol;
		this.sessionId = sessionId;
		this.reSessionId = reSessionId;
		this.body = object;
	}
	
	public enum MessageProtocol {
		CONNECT, CLOSE, HEART_BEAT, SEND, GROUP, NOTIFY, REPLY, ON_LINE, OFF_LINE
	}
	
	public boolean isConnect() {
		return MessageProtocol.CONNECT.equals(this.protocol);
	}
	
	public boolean isClose() {
		return MessageProtocol.CLOSE.equals(this.protocol);
	}
	
	public boolean isHeartbeat() {
		return MessageProtocol.HEART_BEAT.equals(this.protocol);
	}
	
	public boolean isSend() {
		return MessageProtocol.SEND.equals(this.protocol);
	}
	
	public boolean isGroup() {
		return MessageProtocol.GROUP.equals(this.protocol);
	}
	
	public boolean isNotify() {
		return MessageProtocol.NOTIFY.equals(this.protocol);
	}
	
	public boolean isReply() {
		return MessageProtocol.REPLY.equals(this.protocol);
	}
	
	public boolean isOnline() {
		return MessageProtocol.ON_LINE.equals(this.protocol);
	}
	
	public boolean isOffline() {
		return MessageProtocol.OFF_LINE.equals(this.protocol);
	}

	public MessageProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(MessageProtocol protocol) {
		this.protocol = protocol;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getReSessionId() {
		return reSessionId;
	}

	public void setReSessionId(String reSessionId) {
		this.reSessionId = reSessionId;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
	
}
