package com.tony.admin.web.im.handler;


import com.tony.admin.web.im.group.ImChannelGroup;
import com.tony.admin.web.im.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.StringUtil;
import com.tony.admin.web.im.constant.Constants;
import com.tony.admin.web.im.model.MessageWrapper;
import com.tony.admin.web.im.model.proto.MessageModel;
import com.tony.admin.web.im.model.proto.MessageModel.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * IM websocket 核心业务处理器
 * @author Guoqing.Lee
 * @date 2019年6月20日 下午4:26:52
 *
 */
@Sharable
public class ImWebSocketHandler extends SimpleChannelInboundHandler<MessageModel.Message> {

	private static Logger logger = LoggerFactory.getLogger(ImWebSocketHandler.class);
	
	/**
	 * 心跳检测机制
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		String sessionId = ctx.channel().attr(Constants.SessionConfig.SEVER_SESSION_ID).get();
		//发送心跳包
		if(evt instanceof IdleStateEvent && ((IdleStateEvent)evt).state().equals(IdleState.WRITER_IDLE)) {
			if(StringUtil.isNotEmpty(sessionId)) {
				MessageModel.Message.Builder builder = MessageModel.Message.newBuilder();
				builder.setCmd(Constants.CmdType.HEARTBEAT);
				builder.setMsgtype(Constants.ProtobufType.SEND);
				ctx.channel().writeAndFlush(builder);
			}
			logger.debug(IdleState.WRITER_IDLE + "...from " + sessionId + "-->" + ctx.channel().remoteAddress() 
					+ " nid:" + ctx.channel().id().asShortText());
		}
		//如果心跳请求发出70秒内没有收到响应，则关闭连接
		if(evt instanceof IdleStateEvent && ((IdleStateEvent)evt).state().equals(IdleState.READER_IDLE)) {
			logger.debug(IdleState.READER_IDLE + "...from " + sessionId + "nid:" + ctx.channel().id().asShortText());
			Long lastTime = (Long) ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_HEARTBEAT).get();
			if( lastTime == null || ((System.currentTimeMillis() - lastTime)/1000 >= Constants.ImServerConfig.PING_TIME_OUT)) {
				ImChannelGroup.discard(ctx.channel());
				ctx.channel().close();
			}
		}
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		
		
	}
	
	public void receiveMessage(ChannelHandlerContext ctx, MessageWrapper wrapper) {
		
	}

}
