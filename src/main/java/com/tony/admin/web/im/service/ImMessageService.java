package com.tony.admin.web.im.service;

import com.tony.admin.web.im.entity.SysGroupMessage;
import com.tony.admin.web.im.entity.SysUserMessage;
import com.tony.admin.web.im.model.ChatBody;

import java.util.List;

/**
 * IM 消息相关处理接口
 * @author Guoqing
 * @date 2019年7月31日 15:59
 */
public interface ImMessageService {

    /**
     * 更新用户的缓存对话列表，需要更新发送双方的
     * @param chatBody
     */
    void updateChatCache(ChatBody chatBody);

    /**
     * 更新群聊用户的缓存对话列表
     * @param chatBody
     * @param userId    用户ID
     */
    void updateGroupChatCache(ChatBody chatBody, Integer userId);

    /**
     * 持久化用户聊天消息
     * @param chatBody
     */
    void addUserMessage(ChatBody chatBody);

    /**
     * 获取历史对话消息
     * @param fromUserId
     * @param toUserId
     * @return
     */
    List<SysUserMessage> getMessageList(Integer fromUserId, Integer toUserId);

    /**
     * 持久化群聊消息
     * @param chatBody
     */
    void addGroupMessage(ChatBody chatBody);

    /**
     * 获取群聊历史对话消息
     * @param groupId   群聊ID
     * @return
     */
    List<SysGroupMessage> getGroupMessageList(Integer groupId);

}
