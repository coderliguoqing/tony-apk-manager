package com.tony.admin.web.im.service;

import com.tony.admin.web.im.entity.SysGroup;
import com.tony.admin.web.im.entity.SysGroupUser;
import com.tony.admin.web.im.entity.SysUserFriend;
import com.tony.admin.web.im.model.Chat;
import com.tony.admin.web.sys.model.SysUser;

import java.util.List;

/**
 * @author Guoqing
 * @desc  用户IM相关接口
 * @date 2019/7/30
 */
public interface ImUserService {

    /**
     * 获取用户的群组列表
     * @param userId
     * @return
     */
    List<SysGroup> getGroupList(Integer userId);

    /**
     * 获取用户好友列表
     * @param userId
     * @return
     */
    List<SysUserFriend> getFriendList(Integer userId);

    /**
     * 获取用户好友列表，包含具体信息
     * @param userId
     * @return
     */
    List<SysUser> getFriendUserList(Integer userId);

    /**
     * 获取用户的会话列表
     * @param userId 用户ID
     * @return
     */
    List<Chat> getChatList(Integer userId);

    /**
     * 添加会话到缓存列表
     * @param chat  会话对象
     * @param userId    用户ID
     */
    void addChatList(Chat chat, Integer userId);

    /**
     * 获取用户的某一个chat
     * @param userId    用户ID
     * @param chatId    会话对象的ID
     * @param chatType  聊天类型
     * @return
     */
    Chat getChat(Integer userId, Integer chatId, Integer chatType);

    /**
     * 更新缓存中的chat信息
     * @param userId    用户ID
     * @param chat      会话对象
     */
    void updateChat(Integer userId, Chat chat);

    /**
     * 添加好友，双方添加好友只需添加一条记录
     * @param userId    用户ID
     * @param friendId  好友ID
     * @param typeId    好友分组ID，默认0，默认分组
     */
    void addFriend(Integer userId, Integer friendId, Integer typeId);

    /**
     * 创建群组
     * @param group 群组对象
     */
    void createGroup(SysGroup group);

    /**
     * 添加群聊用户
     * @param groupId
     * @param userIds
     */
    void addGroupUser(Integer groupId, List<Integer> userIds);

    /**
     * 获取群聊用户列表
     * @param groupId  群聊ID
     * @return
     */
    List<SysGroupUser> getGroupUserList(Integer groupId);

    /**
     * 获取群聊信息
     * @param groupId
     * @return
     */
    SysGroup getGroupInfo(Integer groupId);

}
