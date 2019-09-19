package com.tony.admin.web.im.command.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tony.admin.web.common.utils.SpringContextUtil;
import com.tony.admin.web.im.command.CmdHandler;
import com.tony.admin.web.im.constant.Constants;
import com.tony.admin.web.im.entity.SysGroupMessage;
import com.tony.admin.web.im.entity.SysUserMessage;
import com.tony.admin.web.im.model.Message;
import com.tony.admin.web.im.model.request.HistoryMessageRequest;
import com.tony.admin.web.im.service.ImMessageService;
import com.tony.admin.web.sys.model.SysUser;
import com.tony.admin.web.sys.service.ISystemService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Guoqing
 * @desc 获取聊天历史消息
 * @date 2019/8/2
 */
@Component
public class HistoryMessageHandler implements CmdHandler {

    private ImMessageService imMessageService;
    private ISystemService iSystemService;

    public HistoryMessageHandler(){
        this.imMessageService = (ImMessageService) SpringContextUtil.getBean("imMessageService");
        this.iSystemService = (ISystemService) SpringContextUtil.getBean("systemServiceImpl");
    }

    @Override
    public Integer cmd() {
        return Constants.CmdType.HISTORY_MESSAGE;
    }

    @Override
    public void handler(ChannelHandlerContext ctx, String object) throws Exception {
        HistoryMessageRequest request = JSONObject.parseObject(object, HistoryMessageRequest.class);
        PageHelper.startPage(request.getPageNum(), request.getPageSize(), false);
        Message response = new Message();
        response.setId(UUID.randomUUID().toString());
        response.setCmd(Constants.CmdType.HISTORY_MESSAGE_RESP);
        response.setCreateTime(System.currentTimeMillis());
        JSONObject jsonObject = new JSONObject();
        //私聊消息
        if(request.getChatType().equals(Constants.ChatType.USER_CHAT)){
            List<SysUserMessage> messageList = imMessageService.getMessageList(request.getFromUserId(), request.getToUserId());
            Collections.sort(messageList);
            PageInfo<SysUserMessage> pageList = new PageInfo<>(messageList);
            jsonObject.put("messageList", pageList);
            jsonObject.put("chatType", Constants.ChatType.USER_CHAT);
        }else{
            //群聊消息
            List<SysGroupMessage> messageList = imMessageService.getGroupMessageList(request.getToUserId());
            Collections.sort(messageList);
            for(SysGroupMessage message : messageList){
                SysUser user = iSystemService.getUserById(message.getUserId());
                message.setUserNick(user.getNick());
                message.setUserAvatar(user.getAvatar());
            }
            PageInfo<SysGroupMessage> pageList = new PageInfo<>(messageList);
            jsonObject.put("messageList", pageList);
            jsonObject.put("chatType", Constants.ChatType.GROUP_CHAT);
        }
        response.setExtras(jsonObject);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(response)));
    }
}
