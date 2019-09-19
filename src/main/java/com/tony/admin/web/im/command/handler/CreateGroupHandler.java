package com.tony.admin.web.im.command.handler;

import com.alibaba.fastjson.JSONObject;
import com.tony.admin.web.common.utils.SpringContextUtil;
import com.tony.admin.web.im.command.CmdHandler;
import com.tony.admin.web.im.constant.Constants;
import com.tony.admin.web.im.entity.SysGroup;
import com.tony.admin.web.im.model.Chat;
import com.tony.admin.web.im.model.ChatBody;
import com.tony.admin.web.im.model.Message;
import com.tony.admin.web.im.model.request.CreateGroupRequest;
import com.tony.admin.web.im.service.ImUserService;
import com.tony.admin.web.im.session.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Guoqing
 * @desc 创建群聊处理器
 * @date 2019/8/5
 */
@Component
public class CreateGroupHandler implements CmdHandler {

    private ImUserService imUserService;

    public CreateGroupHandler(){
        this.imUserService = (ImUserService) SpringContextUtil.getBean("imUserService");
    }

    @Override
    public Integer cmd() {
        return Constants.CmdType.CREATE_GROUP;
    }

    @Override
    public void handler(ChannelHandlerContext ctx, String object) throws Exception {
        CreateGroupRequest request = JSONObject.parseObject(object, CreateGroupRequest.class);
        SysGroup group = new SysGroup();
        group.setGroupName(request.getGroupName());
        group.setCreateTime(new Date());
        group.setAvatar(request.getAvatar());
        group.setUserId(request.getUserId());
        group.setUpdateTime(new Date());
        group.setDescription(request.getDesc());
        group.setDelFalg(0);
        imUserService.createGroup(group);
        //添加群主本身
        List<Integer> userlist = request.getJoinUserId();
        userlist.add(request.getUserId());
        imUserService.addGroupUser(group.getId(), userlist);
        //初始化群主消息和消息列表
        initOwnerUserChat(ctx, request, group);
        //初始化群聊用户消息和消息列表
        initUserChat(ctx, request, group);
    }

    public void initUserChat(ChannelHandlerContext ctx, CreateGroupRequest request, SysGroup group){
        //创建一个channlGroup
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        channelGroup.add(ctx.channel());
        for(Integer userId : request.getJoinUserId()){
            Chat chat = new Chat();
            chat.setChatId(group.getId());
            chat.setChatName(request.getGroupName());
            chat.setFromUserId(group.getId());
            chat.setFromNick(group.getGroupName());
            chat.setAvatar(group.getAvatar());
            chat.setContent(Constants.ImUserConfig.GROUP_CHAT_INIT_MSG);
            chat.setContentType(Constants.ContentType.TEXT);
            chat.setChatType(Constants.ChatType.GROUP_CHAT);
            chat.setUpdateTime(new Date());
            imUserService.addChatList(chat, userId);
            Channel channel = SessionUtil.getSession(userId);
            if(channel != null){
                channelGroup.add(channel);
                //更新用户的消息聊天列表
                Message response = new Message();
                response.setId(UUID.randomUUID().toString());
                response.setCmd(Constants.CmdType.UPDATE_CHAT_LIST);
                response.setCreateTime(System.currentTimeMillis());
                JSONObject jsonObject = new JSONObject();
                List<Chat> chatList = imUserService.getChatList(userId);
                jsonObject.put("chatList", chatList);
                response.setExtras(jsonObject);
                channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(response)));
                //推送初始化消息给用户
                ChatBody chatBody = new ChatBody();
                chatBody.setId(UUID.randomUUID().toString());
                chatBody.setCmd(Constants.CmdType.MESSAGE);
                chatBody.setCreateTime(System.currentTimeMillis());
                chatBody.setFrom(group.getId());
                chatBody.setTo(userId);
                chatBody.setChatType(Constants.ChatType.GROUP_CHAT);
                chatBody.setContent(Constants.ImUserConfig.GROUP_CHAT_INIT_MSG);
                chatBody.setContentType(Constants.ContentType.TEXT);
                channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(chatBody)));
            }
        }
        SessionUtil.bindChannelGroup(group.getId(), channelGroup);
    }

    public void initOwnerUserChat(ChannelHandlerContext ctx, CreateGroupRequest request, SysGroup group){
        Chat chat = new Chat();
        chat.setChatId(group.getId());
        chat.setChatName(request.getGroupName());
        chat.setFromUserId(group.getId());
        chat.setFromNick(group.getGroupName());
        chat.setAvatar(group.getAvatar());
        chat.setContent(Constants.ImUserConfig.GROUP_CHAT_INIT_MSG);
        chat.setContentType(Constants.ContentType.TEXT);
        chat.setChatType(Constants.ChatType.GROUP_CHAT);
        chat.setUpdateTime(new Date());
        imUserService.addChatList(chat, request.getUserId());
        //更新用户的消息聊天列表
        Message response = new Message();
        response.setId(UUID.randomUUID().toString());
        response.setCmd(Constants.CmdType.UPDATE_CHAT_LIST);
        response.setCreateTime(System.currentTimeMillis());
        JSONObject jsonObject = new JSONObject();
        List<Chat> chatList = imUserService.getChatList(request.getUserId());
        jsonObject.put("chatList", chatList);
        response.setExtras(jsonObject);
        ctx.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(response)));
        //推送初始化消息给用户
        ChatBody chatBody = new ChatBody();
        chatBody.setId(UUID.randomUUID().toString());
        chatBody.setCmd(Constants.CmdType.MESSAGE);
        chatBody.setCreateTime(System.currentTimeMillis());
        chatBody.setFrom(group.getId());
        chatBody.setTo(request.getUserId());
        chatBody.setChatType(Constants.ChatType.GROUP_CHAT);
        chatBody.setContent(Constants.ImUserConfig.GROUP_CHAT_INIT_MSG);
        chatBody.setContentType(Constants.ContentType.TEXT);
        ctx.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(chatBody)));
    }
}
