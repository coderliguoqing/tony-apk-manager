package com.tony.admin.web.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.tony.admin.web.common.redis.RedisRepository;
import com.tony.admin.web.common.redis.RedissonMessage;
import com.tony.admin.web.common.response.ResponseBean;
import com.tony.admin.web.im.constant.Constants;
import com.tony.admin.web.im.entity.SysGroup;
import com.tony.admin.web.im.model.Chat;
import com.tony.admin.web.im.service.ImUserService;
import com.tony.admin.web.sys.model.SysUser;
import com.tony.admin.web.sys.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author Guoqing
 * @desc IM相关http接口
 * @date 2019/8/5
 */
@RestController
@RequestMapping("/admin/admin/im")
public class ImController {

    @Autowired
    private ImUserService imUserService;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private ISystemService iSystemService;
    @Autowired
    private RedissonMessage redissonMessage;

    /**
     * 获取好友列表
     * @param jsonObject
     * @return
     */
    @RequestMapping("/getFriendList")
    public ResponseBean getFriendList(@RequestBody JSONObject jsonObject){
        Integer userId = jsonObject.getInteger("userId");
        List<SysUser> userList = imUserService.getFriendUserList(userId);
        return new ResponseBean(true, 0, "请求成功", userList);
    }

    /**
     * 获取群聊列表
     * @param jsonObject
     * @return
     */
    @RequestMapping("/getGroupList")
    public ResponseBean getGroupList(@RequestBody JSONObject jsonObject){
        Integer userId = jsonObject.getInteger("userId");
        List<SysGroup> groupList = imUserService.getGroupList(userId);
        return new ResponseBean(true, 0, "请求成功", groupList);
    }

    /**
     * redis消息订阅发布测试
     * @param jsonObject
     * @return
     */
    @RequestMapping("/sendMessage")
    public ResponseBean sendMessage(@RequestBody JSONObject jsonObject){
        redissonMessage.sendMessage("rTopic", jsonObject.toJSONString());
        return ResponseBean.success();
    }

    /**
     * 新旧数据迁移改造接口
     * @param jsonObject
     */
    @RequestMapping("/update")
    public void update(@RequestBody JSONObject jsonObject){
        String userId = jsonObject.getString("userId");
        String key = Constants.ImUserConfig.CHAT_LIST_KEY + userId;
        Map<String, Object> chatMap = redisRepository.getHashValue(key);
        if(!chatMap.isEmpty()){
            Iterator<Map.Entry<String, Object>> iterators = chatMap.entrySet().iterator();
            while(iterators.hasNext()){
                Chat chat = (Chat) iterators.next().getValue();
                if(chat.getChatType().equals(Constants.ChatType.USER_CHAT)){
                    chat.setAvatar(iSystemService.getUserById(chat.getChatId()).getAvatar());
                }else{
                    chat.setAvatar(imUserService.getGroupInfo(chat.getChatId()).getAvatar());
                }
                redisRepository.putHashValue(key, chat.getChatType() + "-" + chat.getChatId(), chat);
                redisRepository.delHashValues(key, chat.getChatId().toString());
            }
        }
    }

}
