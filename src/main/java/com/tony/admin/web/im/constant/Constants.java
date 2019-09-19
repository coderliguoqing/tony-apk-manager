package com.tony.admin.web.im.constant;

import com.tony.admin.web.sys.model.SysUser;
import io.netty.util.AttributeKey;
import org.omg.PortableInterceptor.INACTIVE;

/**
 * IM常量类
 * @author Guoqing.Lee
 * @date 2019年6月20日 下午3:37:48
 *
 */
public class Constants {
	
	public static interface ImServerConfig {
		//连接空闲时间
      	public static final int READ_IDLE_TIME = 60;//秒
      	//发送心跳包循环时间
      	public static final int WRITE_IDLE_TIME = 40;//秒
        //心跳响应 超时时间
      	public static final int PING_TIME_OUT = 70; //秒   需大于空闲时间
        // 最大协议包长度
        public static final int MAX_FRAME_LENGTH = 1024 * 10; // 10k
        
        public static final int MAX_AGGREGATED_CONTENT_LENGTH = 65536;
        
        public static final String REBOT_SESSIONID = "0";	//机器人sessionId
        
        public static final int WEBSOCKET = 1;	//websocket标识
        
        public static final int SOCKET = 0;		//socket标识
        
        public static final int DWR = 2;		//dwr标识

		public static final String WS_URL = "/socket";
	}

	public static interface ImUserConfig {
		public static final String CHAT_LIST_KEY = "IM_USER_CHAT_LIST_";	//用户会话列表key
		public static final String USER_FRIEND_LIST_KEY = "IM_USER_FRIEND_LIST_KEY_";	//用户好友列表key
		public static final String CHAT_INIT_MSG = "你们已经是好友啦，快开始聊天吧！";		//im添加好友初始化用语
		public static final String GROUP_CHAT_INIT_MSG = "你已经是群聊成员啦，快开始聊天吧！";		//im群聊初始化用于
	}
	
	public static interface SessionConfig {
		public static final String SESSION_KEY = "session";
		public static final String HEARTBEAT_KEY = "heartbeat";
		public static final AttributeKey<SysUser> SEVER_SESSION_ID = AttributeKey.newInstance(SESSION_KEY);
		public static final AttributeKey<Object> SERVER_SESSION_HEARTBEAT = AttributeKey.valueOf(HEARTBEAT_KEY);
	}

	public static interface ChatType {
		int UNKNOW = 0;		//未知
		int GROUP_CHAT = 1;	//群聊
		int USER_CHAT = 2;	//私聊
	}

	/**
	 * 聊天消息内容类型,0:text、1:image、2:voice、3:vedio、4:music、5:news
	 */
	public static interface ContentType {
		Integer TEXT = 0;
		Integer IMAGE = 1;
		Integer VOICE = 2;
		Integer VIDEO = 3;
		Integer MUSIC = 4;
		Integer NEWS = 5;
	}
	
	public static interface MsgType {
		int SEND = 1;		//请求
		int RECEIVE = 2;	//接收
		int NOTIFY = 3;		//通知
		int REPLY = 4;		//回复
	}
	
	public static interface CmdType {
		int BIND = 1;		//绑定
		int HEARTBEAT = 2;	//心跳
		int ONLINE = 3;		//上线
		int OFFLINE = 4;	//下线
		int MESSAGE = 5;	//私聊消息
		int GROUP_MESSAGE = 6;		//群聊重连
		int INIT_IM = 7;	//初始化IM信息
		int JOIN_FRIEND = 8;	//加好友
		int UPDATE_CHAT_LIST = 9;	//更新会话对象列表
		int HISTORY_MESSAGE = 10;	//获取聊天历史消息
		int HISTORY_MESSAGE_RESP = 11;	//响应获取聊天历史消息
		int CREATE_GROUP = 12;	//创建群聊
	}

	/**
	 * 添加好友请求类型
	 */
	public static interface JoinType {
		Integer ADD_REQUEST = 1;	//添加请求
		Integer ADD_RESPONSE = 2;	//响应请求
	}

	/**
	 * 添加好友响应状态
	 */
	public static interface JoinStatus {
		Integer ADD_REJECT = 0;		//拒绝添加
		Integer ADD_ACCEPT = 1;		//同意添加
	}

}
