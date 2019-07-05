package com.tony.admin.web.im.proxy;

import com.tony.admin.web.im.model.MessageWrapper;
import com.tony.admin.web.im.model.proto.MessageModel;

public interface MessageProxy {

	MessageWrapper convertToMessageWrapper(String sessionId, MessageModel.Message message);
	
	/**
	 * 保存在线消息
	 * @param wrapper
	 */
	void saveOnlineMessageToDB(MessageWrapper wrapper);
	
	/**
	 * 保存离线消息
	 * @param wrapper
	 */
	void saveOfflineMessageToDB(MessageWrapper wrapper);
	
	/**
	 * 获取上线状态消息
	 * @param sessionId
	 * @return
	 */
	MessageModel.Message getOnLineStateMsg(String sessionId);
	
	/**
	 * 重新状态消息
	 * @param sessionId
	 * @return
	 */
	MessageModel.Message getReConnectionStateMsg(String sessionId);
	
	/**
	 * 获取下线状态消息
	 * @param sessionId
	 * @return
	 */
	MessageModel.Message getOffLineStateMsg(String sessionId);
	
}
