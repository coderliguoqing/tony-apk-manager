package com.tony.admin.web.im.command.handler;

import com.alibaba.fastjson.JSONObject;
import com.tony.admin.web.common.utils.SpringContextUtil;
import com.tony.admin.web.im.command.CmdHandler;
import com.tony.admin.web.im.constant.Constants;
import com.tony.admin.web.im.model.Chat;
import com.tony.admin.web.im.model.ChatBody;
import com.tony.admin.web.im.model.Message;
import com.tony.admin.web.im.model.request.JoinFriendRequest;
import com.tony.admin.web.im.service.ImUserService;
import com.tony.admin.web.im.session.SessionUtil;
import com.tony.admin.web.sys.model.SysUser;
import com.tony.admin.web.sys.service.ISystemService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 加好友请求
 * 发送添加好友请求，响应自己的请求消息，发送消息给对方添加好友的消息
 * @author Guoqing 创建时间：2019年7月24日 13:55
 */
@Component
public class JoinFriendHandler implements CmdHandler {

    private ISystemService iSystemService;
    private ImUserService imUserService;

    public JoinFriendHandler(){
        this.iSystemService = (ISystemService) SpringContextUtil.getBean("systemServiceImpl");
        this.imUserService = (ImUserService) SpringContextUtil.getBean("imUserService");
    }

    @Override
    public Integer cmd() {
        return Constants.CmdType.JOIN_FRIEND;
    }

    @Override
    public void handler(ChannelHandlerContext ctx, String object) throws Exception {
        JoinFriendRequest request = JSONObject.parseObject(object, JoinFriendRequest.class);
        //添加好友请求
        if(Constants.JoinType.ADD_REQUEST.equals(request.getJoinType())){
            //回复消息
            Integer joinUserId = request.getJoinUserId();
            Channel joinChannel = SessionUtil.getSession(joinUserId);
            if(joinChannel == null){
                //添加该用户的离线消息
            }else{
                //发送消息给被添加的用户
                SysUser clientUser = ctx.channel().attr(Constants.SessionConfig.SEVER_SESSION_ID).get();
                JSONObject userJson = new JSONObject();
                userJson.put("user", clientUser);
                request.setExtras(userJson);
                joinChannel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(request)));
            }
        }else{
            /**
             * 响应添加好友请求，同意或者拒绝；当前的消息来源应该是被添加人
             * 同意添加，则将双方都添加到对方的好友列表中
             */
            if(Constants.JoinStatus.ADD_ACCEPT.equals(request.getStatus())){
                imUserService.addFriend(request.getUserId(), request.getJoinUserId(), 0);
                //推送消息给最开始的请求添加方
                Integer joinUserId = request.getJoinUserId();
                Chat chat = new Chat();
                chat.setChatId(request.getUserId());
                SysUser chatUser = iSystemService.getUserById(request.getUserId());
                chat.setChatName(chatUser.getNick());
                chat.setFromUserId(request.getUserId());
                chat.setFromNick(chatUser.getNick());
                chat.setAvatar(chatUser.getAvatar());
                chat.setContent(Constants.ImUserConfig.CHAT_INIT_MSG);
                chat.setContentType(Constants.ContentType.TEXT);
                chat.setChatType(Constants.ChatType.USER_CHAT);
                chat.setUpdateTime(new Date());
                imUserService.addChatList(chat, joinUserId);
                ChatBody chatBody = new ChatBody();
                chatBody.setId(UUID.randomUUID().toString());
                chatBody.setCmd(Constants.CmdType.MESSAGE);
                chatBody.setCreateTime(System.currentTimeMillis());
                chatBody.setFrom(request.getUserId());
                chatBody.setTo(request.getJoinUserId());
                chatBody.setChatType(Constants.ChatType.USER_CHAT);
                chatBody.setContent(Constants.ImUserConfig.CHAT_INIT_MSG);
                chatBody.setContentType(Constants.ContentType.TEXT);
                Channel requestChannel = SessionUtil.getSession(joinUserId);
                //先更新原始请求用户的聊天列表
                Message response = new Message();
                response.setId(UUID.randomUUID().toString());
                response.setCmd(Constants.CmdType.UPDATE_CHAT_LIST);
                response.setCreateTime(System.currentTimeMillis());
                JSONObject jsonObject = new JSONObject();
                List<Chat> chatList = imUserService.getChatList(joinUserId);
                jsonObject.put("chatList", chatList);
                response.setExtras(jsonObject);
                if(requestChannel != null){
                    requestChannel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(response)));
                    requestChannel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(chatBody)));
                }
                //推送消息给被请求添加方，也就是当前通道中的用户
                Chat toJoinChat = new Chat();
                toJoinChat.setChatId(joinUserId);
                SysUser joinUser = iSystemService.getUserById(request.getJoinUserId());
                toJoinChat.setChatName(joinUser.getNick());
                toJoinChat.setFromUserId(joinUserId);
                toJoinChat.setFromNick(joinUser.getNick());
                toJoinChat.setAvatar(joinUser.getAvatar());
                toJoinChat.setContent(Constants.ImUserConfig.CHAT_INIT_MSG);
                toJoinChat.setContentType(Constants.ContentType.TEXT);
                toJoinChat.setChatType(Constants.ChatType.USER_CHAT);
                toJoinChat.setUpdateTime(new Date());
                imUserService.addChatList(toJoinChat, request.getUserId());
                ChatBody fromBody = new ChatBody();
                fromBody.setId(UUID.randomUUID().toString());
                fromBody.setCmd(Constants.CmdType.MESSAGE);
                fromBody.setCreateTime(System.currentTimeMillis());
                fromBody.setFrom(request.getJoinUserId());
                fromBody.setTo(request.getUserId());
                fromBody.setChatType(Constants.ChatType.USER_CHAT);
                fromBody.setContent(Constants.ImUserConfig.CHAT_INIT_MSG);
                fromBody.setContentType(Constants.ContentType.TEXT);
                ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(fromBody)));
                //更新被请求用户的聊天列表
                Message joinResponse = new Message();
                joinResponse.setId(UUID.randomUUID().toString());
                joinResponse.setCmd(Constants.CmdType.UPDATE_CHAT_LIST);
                joinResponse.setCreateTime(System.currentTimeMillis());
                JSONObject joinJson = new JSONObject();
                List<Chat> joinChatList = imUserService.getChatList(request.getUserId());
                joinJson.put("chatList", joinChatList);
                joinResponse.setExtras(joinJson);
                ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(joinResponse)));
            }
        }
    }
}
