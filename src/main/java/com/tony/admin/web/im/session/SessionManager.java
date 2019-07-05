package com.tony.admin.web.im.session;

import java.util.Set;

import com.tony.admin.web.im.model.MessageWrapper;
import com.tony.admin.web.im.model.Session;

import io.netty.channel.ChannelHandlerContext;

public interface SessionManager {
	
	/**
	 * 添加指定session
	 * @param session
	 */
	void addSession(Session session);
	
	/**
	 * 更新session
	 * @param session
	 */
	void updateSession(Session session);
	
	/**
	 * 删除指定session
	 * @param sessionId
	 */
	void removeSession(String sessionId);
	
	/**
	 * 删除指定session
	 * @param sessionId
	 * @param nid
	 */
	void removeSession(String sessionId, String nid);
	
	/**
	 * 根据指定sessionId获取session
	 * @param sessionId
	 * @return
	 */
	Session getSession(String sessionId);
	
	/**
	 * 获取所有的session
	 * @return
	 */
	Session[] getSessions();
	
	/**
	 * 获取所有的session的id集合
	 * @return
	 */
	Set<String> getSessionKeys();
	
	/**
	 * 获取所有session的数目
	 * @return
	 */
	int getSessionCount();

	Session createSession(MessageWrapper wrapper, ChannelHandlerContext ctx);
	
}
