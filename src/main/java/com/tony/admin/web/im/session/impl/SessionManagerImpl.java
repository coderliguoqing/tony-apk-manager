package com.tony.admin.web.im.session.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tony.admin.web.im.constant.Constants;
import com.tony.admin.web.im.group.ImChannelGroup;
import com.tony.admin.web.im.model.MessageWrapper;
import com.tony.admin.web.im.model.Session;
import com.tony.admin.web.im.model.proto.MessageModel;
import com.tony.admin.web.im.proxy.MessageProxy;
import com.tony.admin.web.im.session.SessionManager;

import io.netty.channel.ChannelHandlerContext;

public class SessionManagerImpl implements SessionManager {
	
	private static final Logger logger = LoggerFactory.getLogger(SessionManagerImpl.class);

	@Autowired
	private MessageProxy proxy;
	
	protected Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	
	@Override
	public synchronized void addSession(Session session) {
		if(session == null) {
			return;
		}
		sessions.put(session.getAccount(), session);
		if(session.getSource() != Constants.ImServerConfig.DWR) {
			
		}
		//全员发送上线消息
		MessageModel.Message message = proxy.getOnLineStateMsg(session.getAccount());
		ImChannelGroup.broadcast(message);
		logger.debug("put a session {} to sessions!", session.getAccount());
		logger.debug("session size {}", sessions.size());
	}

	@Override
	public synchronized void updateSession(Session session) {
		session.setUpdateTime(System.currentTimeMillis());
		sessions.put(session.getAccount(), session);
	}

	@Override
	public synchronized void removeSession(String sessionId) {
		try {
			Session session = getSession(sessionId);
			if(session != null) {
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	@Override
	public void removeSession(String sessionId, String nid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Session getSession(String sessionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session[] getSessions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getSessionKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSessionCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Session createSession(MessageWrapper wrapper, ChannelHandlerContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

}
