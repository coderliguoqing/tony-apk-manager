package com.tony.admin.web.im.command.handler;

import com.alibaba.fastjson.JSONObject;
import com.tony.admin.web.common.thread.UserRejectHandler;
import com.tony.admin.web.common.thread.UserThreadFactory;
import com.tony.admin.web.common.utils.SpringContextUtil;
import com.tony.admin.web.im.command.CmdHandler;
import com.tony.admin.web.im.constant.Constants;
import com.tony.admin.web.im.entity.SysGroupUser;
import com.tony.admin.web.im.model.ChatBody;
import com.tony.admin.web.im.service.ImMessageService;
import com.tony.admin.web.im.service.ImUserService;
import com.tony.admin.web.im.session.SessionUtil;
import com.tony.admin.web.sys.model.SysUser;
import com.tony.admin.web.sys.service.ISystemService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 新消息处理器
 * @author Guoqing 创建时间：2019年7月23日 16:49
 */
@Component
public class NewMessageHandler implements CmdHandler {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    private static UserThreadFactory userThreadFactory = new UserThreadFactory("netty-group-chat-cache-update");
    private static UserRejectHandler userRejectHandler = new UserRejectHandler();

    /**
     * 线程池的大小取决于任务的类型，避免“过大”或者“过小”两种极端。
     * 过大，大量的线程将在更少的CPU和有限的内存上面竞争，不仅影响性能，还可能过高的消耗内存导致OOM；
     * 过小，处理器得不到充分的利用，降低了效率；
     * 可以根据任务的类型，比如计算密集型或者IO密集型还是两者都有来设置线程池大小；
     * 说明：通常计算型任务可以设置corePoolSize = Runtime.getRuntime().availableProcessors(); 及CPU个数
     * 	   而IO密集型任务可以稍微调大参数，如下列，则是corePoolSize*2  因为是较多的IO处理，通过多次测试，合理调大线程池大小之后效率提升明显
     *   自定义线程工厂有助于快速定位问题，打印出更多有用的线程信息
     */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            corePoolSize*2,
            corePoolSize*2,
            10L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            userThreadFactory,
            userRejectHandler);

    private ImMessageService imMessageService;
    private ImUserService imUserService;
    private ISystemService iSystemService;

    public NewMessageHandler(){
        this.imMessageService = (ImMessageService) SpringContextUtil.getBean("imMessageService");
        this.imUserService = (ImUserService) SpringContextUtil.getBean("imUserService");
        this.iSystemService = (ISystemService) SpringContextUtil.getBean("systemServiceImpl");
    }

    @Override
    public Integer cmd() {
        return Constants.CmdType.MESSAGE;
    }

    @Override
    public void handler(ChannelHandlerContext ctx, String object) throws Exception {
        ChatBody chatBody = JSONObject.parseObject(object, ChatBody.class);
        //私聊信息处理
        if(chatBody.getChatType().equals(Constants.ChatType.USER_CHAT)){
            Integer toId = chatBody.getTo();
            if(!chatBody.getTo().equals(chatBody.getFrom())){
                Channel channel = SessionUtil.getSession(toId);
                chatBody.setCreateTime(System.currentTimeMillis());
                if(channel != null){
                    String bodyMsg = JSONObject.toJSONString(chatBody);
                    channel.writeAndFlush(new TextWebSocketFrame(bodyMsg));
                }
            }
            //更新缓存会话列表
            imMessageService.updateChatCache(chatBody);
            //消息持久化
            imMessageService.addUserMessage(chatBody);
        }else{
            SysUser fromUser = iSystemService.getUserById(chatBody.getFrom());
            chatBody.setFromUserNick(fromUser.getNick());
            chatBody.setFromUserAvatar(fromUser.getAvatar());
            //群聊新消息处理
            ChannelGroup channelGroup = SessionUtil.getChannelGroup(chatBody.getGroupId());
            chatBody.setCreateTime(System.currentTimeMillis());
            String bodyMsg = JSONObject.toJSONString(chatBody);
            channelGroup.writeAndFlush(new TextWebSocketFrame(bodyMsg));
            //更新群聊用户会话列表
            updateGroupChatCache(chatBody);
            //消息持久化
            imMessageService.addGroupMessage(chatBody);
        }
    }

    public void updateGroupChatCache(ChatBody chatBody){
        List<SysGroupUser> userList = imUserService.getGroupUserList(chatBody.getGroupId());
        for(SysGroupUser user : userList){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    imMessageService.updateGroupChatCache(chatBody, user.getUserId());
                }
            });
        }
    }

}
