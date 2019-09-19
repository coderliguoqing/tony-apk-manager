package com.tony.admin.web.im.service.impl;

import com.tony.admin.web.im.entity.SysGroupMessage;
import com.tony.admin.web.im.entity.SysUserMessage;
import com.tony.admin.web.im.mapper.SysGroupMessageMapper;
import com.tony.admin.web.im.mapper.SysUserMessageMapper;
import com.tony.admin.web.im.model.Chat;
import com.tony.admin.web.im.model.ChatBody;
import com.tony.admin.web.im.service.ImMessageService;
import com.tony.admin.web.im.service.ImUserService;
import com.tony.admin.web.sys.model.SysUser;
import com.tony.admin.web.sys.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author Guoqing
 * @desc
 * @date 2019/7/31
 */
@Service("imMessageService")
public class ImMessageServiceImpl implements ImMessageService {

    @Autowired
    private ISystemService iSystemService;
    @Autowired
    private ImUserService imUserService;
    @Autowired
    private SysUserMessageMapper userMessageMapper;
    @Autowired
    private SysGroupMessageMapper groupMessageMapper;

    @Override
    public void updateChatCache(ChatBody chatBody) {
        //从发送人的角度
        //获取发送信息用户ID，先实现私聊
        Chat fromChat = imUserService.getChat(chatBody.getFrom(), chatBody.getTo(), chatBody.getChatType());
        if(fromChat == null){
            fromChat = new Chat();
            fromChat.setChatId(chatBody.getTo());
            SysUser toUser = iSystemService.getUserById(chatBody.getTo());
            fromChat.setChatName(toUser.getNick());
            fromChat.setAvatar(toUser.getAvatar());
            fromChat.setChatType(chatBody.getChatType());
            fromChat.setContent(chatBody.getContent());
            fromChat.setContentType(chatBody.getContentType());
            fromChat.setUpdateTime(new Date());
            fromChat.setFromUserId(chatBody.getFrom());
            SysUser fromUser = iSystemService.getUserById(chatBody.getFrom());
            fromChat.setFromNick(fromUser.getNick());
        }else{
            fromChat.setUpdateTime(new Date());
            fromChat.setContent(chatBody.getContent());
            fromChat.setContentType(chatBody.getContentType());
        }
        imUserService.updateChat(chatBody.getFrom(), fromChat);
        //从接收人的角度
        Chat toChat = imUserService.getChat(chatBody.getTo(), chatBody.getFrom(), chatBody.getChatType());
        if(toChat == null){
            toChat = new Chat();
            toChat.setChatId(chatBody.getFrom());
            SysUser toUser = iSystemService.getUserById(chatBody.getFrom());
            toChat.setChatName(toUser.getNick());
            toChat.setAvatar(toUser.getAvatar());
            toChat.setChatType(chatBody.getChatType());
            toChat.setContent(chatBody.getContent());
            toChat.setContentType(chatBody.getContentType());
            toChat.setUpdateTime(new Date());
            toChat.setFromUserId(chatBody.getTo());
            SysUser fromUser = iSystemService.getUserById(chatBody.getTo());
            toChat.setFromNick(fromUser.getNick());
        }else{
            toChat.setUpdateTime(new Date());
            toChat.setContent(chatBody.getContent());
            toChat.setContentType(chatBody.getContentType());
        }
        imUserService.updateChat(chatBody.getTo(), toChat);
    }

    @Override
    public void updateGroupChatCache(ChatBody chatBody, Integer userId) {
        Chat chat = imUserService.getChat(userId, chatBody.getGroupId(), chatBody.getChatType());
        chat.setUpdateTime(new Date());
        chat.setContent(chatBody.getContent());
        chat.setContentType(chatBody.getContentType());
        chat.setFromUserId(chatBody.getFrom());
        chat.setFromNick(chatBody.getFromUserNick());
        imUserService.updateChat(userId, chat);
    }

    @Override
    public void addUserMessage(ChatBody chatBody) {
        SysUserMessage message = new SysUserMessage();
        message.setFromUserId(chatBody.getFrom());
        message.setToUserId(chatBody.getTo());
        message.setContent(chatBody.getContent());
        message.setContentType(chatBody.getContentType());
        message.setReadFlag(0);
        message.setBackFlag(0);
        message.setCreateTime(new Date());
        message.setUpdateTime(new Date());
        message.setDelFlag(0);
        userMessageMapper.insert(message);
    }

    @Override
    public List<SysUserMessage> getMessageList(Integer fromUserId, Integer toUserId) {
        Example example = new Example(SysUserMessage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fromUserId",fromUserId)
                .andEqualTo("toUserId", toUserId);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("fromUserId",toUserId)
                .andEqualTo("toUserId", fromUserId);
        example.or(c);
        example.orderBy("createTime").desc();
        return userMessageMapper.selectByExample(example);
    }

    @Override
    public void addGroupMessage(ChatBody chatBody) {
        SysGroupMessage message = new SysGroupMessage();
        message.setGroupId(chatBody.getGroupId());
        message.setUserId(chatBody.getFrom());
        message.setContent(chatBody.getContent());
        message.setContentType(chatBody.getContentType());
        message.setReadFlag(1);
        message.setBackFlag(0);
        message.setCreateTime(new Date());
        message.setUpdateTime(new Date());
        message.setDelFlag(0);
        groupMessageMapper.insert(message);
    }

    @Override
    public List<SysGroupMessage> getGroupMessageList(Integer groupId) {
        Example example = new Example(SysGroupMessage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupId",groupId);
        example.orderBy("createTime").desc();
        return groupMessageMapper.selectByExample(example);
    }
}


