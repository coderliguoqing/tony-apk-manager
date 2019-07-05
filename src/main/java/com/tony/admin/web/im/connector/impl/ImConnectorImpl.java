package com.tony.admin.web.im.connector.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tony.admin.web.im.connector.ImConnector;
import com.tony.admin.web.im.model.MessageWrapper;

import io.netty.channel.ChannelHandlerContext;

public class ImConnectorImpl implements ImConnector {
	
	private static Logger logger = LoggerFactory.getLogger(ImConnectorImpl.class);

	@Override
	public void heartbeatToClient(ChannelHandlerContext ctx, MessageWrapper wrapper) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushMessage(MessageWrapper wrapper) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushGroupMessage(MessageWrapper wrapper) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validateSession(MessageWrapper wrapper) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close(ChannelHandlerContext ctx, MessageWrapper wrapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close(String sessionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close(ChannelHandlerContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connect(ChannelHandlerContext ctx, MessageWrapper wrapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean exist(String sessionId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pushMessage(String sessionId, MessageWrapper wrapper) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getChannelSessionId(ChannelHandlerContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

}
