package com.tony.admin.web.im.connector;

import com.tony.admin.web.im.model.MessageWrapper;

import io.netty.channel.ChannelHandlerContext;

public interface ImConnector {
	
	/**
	 * 发送心跳监测到客户端
	 * @param ctx
	 * @param wrapper
	 */
	void heartbeatToClient(ChannelHandlerContext ctx, MessageWrapper wrapper) throws Exception;
	
	/**
	 * 发送消息
	 * @param wrapper
	 */
	void pushMessage(MessageWrapper wrapper) throws Exception;
	
	/**
	 * 发送组消息
	 * @param wrapper
	 */
	void pushGroupMessage(MessageWrapper wrapper) throws Exception;
	
	/**
	 * 验证session
	 * @param wrapper
	 * @return
	 */
	boolean validateSession(MessageWrapper wrapper) throws Exception;
	
	void close(ChannelHandlerContext ctx, MessageWrapper wrapper);
	
	void close(String sessionId);
	
	void close(ChannelHandlerContext ctx);
	
	void connect(ChannelHandlerContext ctx, MessageWrapper wrapper);

	boolean exist(String sessionId) throws Exception;
	
	/**
	 * 发送消息
	 * @param sessionId 发送人
	 * @param wrapper	发送内容
	 * @throws Exception
	 */
	void pushMessage(String sessionId, MessageWrapper wrapper) throws Exception;
	
	/**
	 * 获取用户唯一标识
	 * @param ctx
	 * @return
	 */
	String getChannelSessionId(ChannelHandlerContext ctx);

}
