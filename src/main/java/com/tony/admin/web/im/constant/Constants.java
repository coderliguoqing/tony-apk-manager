package com.tony.admin.web.im.constant;

import io.netty.util.AttributeKey;

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
	}
	
	public static interface SessionConfig {
		public static final String SESSION_KEY = "session";
		public static final String HEARTBEAT_KEY = "heartbeat";
		public static final AttributeKey<String> SEVER_SESSION_ID = AttributeKey.newInstance(SESSION_KEY);
		public static final AttributeKey<Object> SERVER_SESSION_HEARTBEAT = AttributeKey.valueOf(HEARTBEAT_KEY);
	}
	
	public static interface ProtobufType {
		byte SEND = 1;		//请求
		byte RECEIVE = 2;	//接收
		byte NOTIFY = 3;	//通知
		byte REPLY = 4;		//回复
	}
	
	public static interface CmdType {
		byte BIND = 1;		//绑定
		byte HEARTBEAT = 2;	//心跳
		byte ONLINE = 3;	//上线	
		byte OFFLINE = 4;	//下线
		byte MESSAGE = 5;	//消息
		byte RECON = 6;		//重连
	}

}
