package com.tony.admin.web.im.proxy.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tony.admin.web.im.constant.Constants;
import com.tony.admin.web.im.entity.UserMessageEntity;
import com.tony.admin.web.im.model.MessageWrapper;
import com.tony.admin.web.im.model.MessageWrapper.MessageProtocol;
import com.tony.admin.web.im.model.proto.MessageBodyModel;
import com.tony.admin.web.im.model.proto.MessageModel;
import com.tony.admin.web.im.model.proto.MessageModel.Message;
import com.tony.admin.web.im.proxy.MessageProxy;
import com.tony.admin.web.im.service.UserMessageService;

public class MessageProxyImpl implements MessageProxy {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageProxyImpl.class);
	
	@Autowired
	private UserMessageService userMessageService;

	@Override
	public MessageWrapper convertToMessageWrapper(String sessionId, Message message) {
		switch (message.getCmd()) {
		case Constants.CmdType.BIND:
			try {
				return new MessageWrapper(MessageProtocol.CONNECT, message.getSender(), null, message);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case Constants.CmdType.HEARTBEAT:
			try {
				return new MessageWrapper(MessageProtocol.HEART_BEAT, sessionId, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case Constants.CmdType.ONLINE:
			break;
		case Constants.CmdType.OFFLINE:
			break;
		case Constants.CmdType.MESSAGE:
			try {
				MessageModel.Message.Builder result = MessageModel.Message.newBuilder(message);
				result.setTimeStamp(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				result.setSender(sessionId);
				message = MessageModel.Message.parseFrom(result.build().toByteArray());
				//判断是否有接收人
				if(StringUtils.isNotEmpty(message.getReceiver())) {
					//判断是否发消息给机器人
					if(message.getReceiver().equals(Constants.ImServerConfig.REBOT_SESSIONID)) {
						
					}else {
						return new MessageWrapper(MessageProtocol.REPLY, sessionId, message.getReceiver(), message);
					}
				}else if(StringUtils.isNotEmpty(message.getGroupId())) {
					return new MessageWrapper(MessageProtocol.GROUP, sessionId, null, message);
				}else {
					return new MessageWrapper(MessageProtocol.SEND, sessionId, null, message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		return null;
	}

	@Override
	public void saveOnlineMessageToDB(MessageWrapper wrapper) {
		try {
			UserMessageEntity userMessage = convertMessageWrapperToBean(wrapper);
			if(userMessage != null) {
				userMessage.setIsread(1);
				userMessageService.save(userMessage);
			}
		} catch (Exception e) {
			logger.error("MessageProxyImpl user {} send msg to {} error", wrapper.getSessionId(), wrapper.getReSessionId());
			throw new RuntimeException(e.getCause());
		}
	}

	@Override
	public void saveOfflineMessageToDB(MessageWrapper wrapper) {
		try {
			UserMessageEntity userMessage = convertMessageWrapperToBean(wrapper);
			if(userMessage != null) {
				userMessage.setIsread(0);
				userMessageService.save(userMessage);
			}
		} catch (Exception e) {
			logger.error("MessageProxyImpl user {} send msg to {} error", wrapper.getSessionId(), wrapper.getReSessionId());
			throw new RuntimeException(e.getCause());
		}
	}

	@Override
	public Message getOnLineStateMsg(String sessionId) {
		MessageModel.Message.Builder result = MessageModel.Message.newBuilder();
		result.setTimeStamp(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		result.setSender(sessionId);
		result.setCmd(Constants.CmdType.ONLINE);
		return result.build();
	}

	@Override
	public Message getReConnectionStateMsg(String sessionId) {
		MessageModel.Message.Builder result = MessageModel.Message.newBuilder();
		result.setTimeStamp(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		result.setSender(sessionId);
		result.setCmd(Constants.CmdType.RECON);
		return result.build();
	}

	@Override
	public Message getOffLineStateMsg(String sessionId) {
		MessageModel.Message.Builder result = MessageModel.Message.newBuilder();
		result.setTimeStamp(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		result.setSender(sessionId);
		result.setCmd(Constants.CmdType.OFFLINE);
		return result.build();
	}
	
	private UserMessageEntity convertMessageWrapperToBean(MessageWrapper wrapper) {
		try {
			if(!wrapper.getSessionId().equals(Constants.ImServerConfig.REBOT_SESSIONID)) {
				MessageModel.Message msg = (MessageModel.Message)wrapper.getBody();
				MessageBodyModel.MessageBody msgContent = MessageBodyModel.MessageBody.parseFrom(msg.getContent());
				UserMessageEntity userMessage = new UserMessageEntity();
				userMessage.setSenduser(wrapper.getSessionId());
				userMessage.setReceiveuser(wrapper.getReSessionId());
				userMessage.setContent(msgContent.getContent());
				userMessage.setGroupid(msg.getGroupId());
				userMessage.setCreatedate(msg.getTimeStamp());
				userMessage.setIsread(1);
				return userMessage;
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getCause());
		}
		return null;
	}

}
