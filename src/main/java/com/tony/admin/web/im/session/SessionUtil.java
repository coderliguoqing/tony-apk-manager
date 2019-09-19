package com.tony.admin.web.im.session;

import com.tony.admin.web.im.constant.Constants;
import com.tony.admin.web.sys.model.SysUser;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * session处理工具类
 * @author Guoqing 创建时间：2019年7月24日 11:45
 */
public class SessionUtil {

    private static final Map<Integer, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    private static final Map<Integer, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();

    private static final Map<String, Integer> channelIdUserIdMap = new ConcurrentHashMap<>();

    public static void bindSession(SysUser user, Channel channel){
        userIdChannelMap.put(user.getId(), channel);
        channel.attr(Constants.SessionConfig.SEVER_SESSION_ID).set(user);
    }

    public static Channel getSession(Integer id){
        return userIdChannelMap.get(id);
    }

    public static Integer getUserIdByChannelId(String channelId){
        return channelIdUserIdMap.get(channelId);
    }

    public static void bindChannelIdAndUserId(String channelId, Integer userId){
        channelIdUserIdMap.put(channelId, userId);
    }

    public static void bindChannelGroup(Integer groupId, ChannelGroup channelGroup) {
        groupIdChannelGroupMap.put(groupId, channelGroup);
    }

    public static ChannelGroup getChannelGroup(Integer groupId) {
        return groupIdChannelGroupMap.get(groupId);
    }

}
