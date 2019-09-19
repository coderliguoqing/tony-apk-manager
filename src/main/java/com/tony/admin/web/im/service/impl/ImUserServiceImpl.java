package com.tony.admin.web.im.service.impl;

import com.tony.admin.web.common.redis.RedisRepository;
import com.tony.admin.web.im.constant.Constants;
import com.tony.admin.web.im.entity.SysGroup;
import com.tony.admin.web.im.entity.SysGroupUser;
import com.tony.admin.web.im.entity.SysUserFriend;
import com.tony.admin.web.im.mapper.SysGroupMapper;
import com.tony.admin.web.im.mapper.SysGroupUserMapper;
import com.tony.admin.web.im.mapper.SysUserFriendMapper;
import com.tony.admin.web.im.model.Chat;
import com.tony.admin.web.im.service.ImUserService;
import com.tony.admin.web.sys.model.SysUser;
import com.tony.admin.web.sys.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @author Guoqing
 * @desc
 * @date 2019/7/30
 */
@Service("imUserService")
public class ImUserServiceImpl implements ImUserService {

    @Autowired
    private SysGroupMapper groupMapper;
    @Autowired
    private SysUserFriendMapper userFriendMapper;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private SysGroupUserMapper groupUserMapper;
    @Autowired
    private ISystemService iSystemService;

    @Override
    public List<SysGroup> getGroupList(Integer userId) {
        Example example = new Example(SysGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return groupMapper.selectByExample(example);
    }

    @Override
    public List<SysUserFriend> getFriendList(Integer userId) {
        Example example = new Example(SysUserFriend.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return userFriendMapper.selectByExample(example);
    }

    @Override
    public List<SysUser> getFriendUserList(Integer userId) {
        List<SysUser> userlist = new ArrayList<>();
        String key = Constants.ImUserConfig.USER_FRIEND_LIST_KEY + userId;
        if(redisRepository.exists(key)){
            Map<String, Object> userMaps = redisRepository.getHashValue(key);
            if(!userMaps.isEmpty()){
                Iterator<Map.Entry<String, Object>> iterators = userMaps.entrySet().iterator();
                while (iterators.hasNext()){
                    userlist.add((SysUser) iterators.next().getValue());
                }
            }
        }else{
            List<SysUserFriend> userFriendList = getFriendList(userId);
            for(SysUserFriend friend : userFriendList){
                SysUser user = iSystemService.getUserById(friend.getFriendId());
                userlist.add(user);
                redisRepository.putHashValue(key, user.getId().toString(), user);
            }
        }
        return userlist;
    }

    @Override
    public List<Chat> getChatList(Integer userId) {
        List<Chat> chatList = new ArrayList<>();
        //获取缓存的对话列表
        String key = Constants.ImUserConfig.CHAT_LIST_KEY + userId;
        Map<String, Object> chatMap = redisRepository.getHashValue(key);
        if(!chatMap.isEmpty()){
            Iterator<Map.Entry<String, Object>> iterators = chatMap.entrySet().iterator();
            while(iterators.hasNext()){
                Chat chat = (Chat) iterators.next().getValue();
                chatList.add(chat);
            }
            Collections.sort(chatList);
        }
        return chatList;
    }

    @Override
    public void addChatList(Chat chat, Integer userId) {
        String key = Constants.ImUserConfig.CHAT_LIST_KEY + userId;
        redisRepository.putHashValue(key, chat.getChatType() + "-" + chat.getChatId(), chat);
    }

    @Override
    public Chat getChat(Integer userId, Integer chatId, Integer chatType) {
        String key = Constants.ImUserConfig.CHAT_LIST_KEY + userId;
        return (Chat) redisRepository.getHashValues(key, chatType + "-" + chatId);
    }

    @Override
    public void updateChat(Integer userId, Chat chat) {
        addChatList(chat, userId);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId, Integer typeId) {
        SysUserFriend userFriend = new SysUserFriend();
        userFriend.setUserId(userId);
        userFriend.setFriendId(friendId);
        userFriend.setTypeId(typeId);
        userFriend.setCreateTime(new Date());
        userFriend.setUpdateTime(new Date());
        userFriend.setDelFlag(0);
        userFriendMapper.insert(userFriend);
        SysUserFriend friend = new SysUserFriend();
        friend.setUserId(friendId);
        friend.setFriendId(userId);
        friend.setTypeId(typeId);
        friend.setCreateTime(new Date());
        friend.setUpdateTime(new Date());
        friend.setDelFlag(0);
        userFriendMapper.insert(friend);
    }

    @Override
    public void createGroup(SysGroup group) {
        groupMapper.insert(group);
    }

    @Override
    public void addGroupUser(Integer groupId, List<Integer> userIds) {
        for(Integer userId : userIds){
            SysGroupUser groupUser = new SysGroupUser();
            groupUser.setGroupId(groupId);
            groupUser.setUserId(userId);
            groupUser.setCreateTime(new Date());
            groupUser.setUpdateTime(new Date());
            groupUser.setDelFlag(0);
            groupUserMapper.insert(groupUser);
        }
    }

    @Override
    public List<SysGroupUser> getGroupUserList(Integer groupId) {
        Example example = new Example(SysGroupUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupId", groupId);
        criteria.andEqualTo("delFlag", 0);
        return groupUserMapper.selectByExample(example);
    }

    @Override
    public SysGroup getGroupInfo(Integer groupId) {
        return groupMapper.selectByPrimaryKey(groupId);
    }
}
