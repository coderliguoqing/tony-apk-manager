package com.tony.admin.web.im.handler;


import com.alibaba.fastjson.JSONObject;
import com.tony.admin.web.common.response.ResponseBean;
import com.tony.admin.web.common.security.TokenUtil;
import com.tony.admin.web.common.security.model.AuthUser;
import com.tony.admin.web.common.utils.SpringContextUtil;
import com.tony.admin.web.common.utils.StringHelper;
import com.tony.admin.web.im.command.CmdHandler;
import com.tony.admin.web.im.command.CmdManager;
import com.tony.admin.web.im.group.ImChannelGroup;
import com.tony.admin.web.im.listener.ImMessageListener;
import com.tony.admin.web.im.model.Chat;
import com.tony.admin.web.im.model.HeartBeatBody;
import com.tony.admin.web.im.model.Message;
import com.tony.admin.web.im.service.ImUserService;
import com.tony.admin.web.im.session.SessionUtil;
import com.tony.admin.web.sys.model.SysUser;
import com.tony.admin.web.sys.service.ISystemService;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tony.admin.web.im.constant.Constants;

import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.channel.ChannelHandler.Sharable;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * IM websocket 核心业务处理器
 * @author Guoqing.Lee
 * @date 2019年6月20日 下午4:26:52
 *
 */
@Sharable
@Component
public class ImWebSocketHandler extends SimpleChannelInboundHandler<Object> {

	private static Logger logger = LoggerFactory.getLogger(ImWebSocketHandler.class);

	private WebSocketServerHandshaker handshaker;
	private ImUserService imUserService;
	private TokenUtil tokenUtil;
	private ISystemService iSystemService;

	public ImWebSocketHandler(){
		this.imUserService = (ImUserService) SpringContextUtil.getBean("imUserService");
		this.tokenUtil = (TokenUtil) SpringContextUtil.getBean("tokenUtil");
		this.iSystemService = (ISystemService) SpringContextUtil.getBean("systemServiceImpl");
	}


	/**
	 * 心跳检测机制
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		//发送心跳包
		if(evt instanceof IdleStateEvent && ((IdleStateEvent)evt).state().equals(IdleState.WRITER_IDLE)) {
			HeartBeatBody heartBeat = new HeartBeatBody();
			heartBeat.setCmd(Constants.CmdType.HEARTBEAT);
			heartBeat.setMsgType(Constants.MsgType.SEND);
			heartBeat.setMessage(Constants.SessionConfig.HEARTBEAT_KEY + "-" + System.currentTimeMillis());
			String heartBeatMsg = JSONObject.toJSONString(heartBeat);
			ctx.channel().writeAndFlush(new TextWebSocketFrame(heartBeatMsg));
			logger.info(IdleState.WRITER_IDLE + "...from " + "-->" + ctx.channel().remoteAddress()
					+ " nid:" + ctx.channel().id().asShortText());
		}
		//如果心跳请求发出70秒内没有收到响应，则关闭连接
		if(evt instanceof IdleStateEvent && ((IdleStateEvent)evt).state().equals(IdleState.READER_IDLE)) {
			logger.info(IdleState.READER_IDLE + "...from " + "nid:" + ctx.channel().id().asShortText());
			Long lastTime = (Long) ctx.channel().attr(Constants.SessionConfig.SERVER_SESSION_HEARTBEAT).get();
			if( lastTime == null || ((System.currentTimeMillis() - lastTime)/1000 >= Constants.ImServerConfig.PING_TIME_OUT)) {
				ImChannelGroup.discard(ctx.channel());
			}
		}
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof FullHttpRequest){
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		}else if(msg instanceof WebSocketFrame){
			handWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	/**
	 * websocket 消息处理
	 * @param ctx
	 * @param frame
	 * @throws Exception
	 */
	public void handWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception{
		//关闭请求
		if(frame instanceof CloseWebSocketFrame){
			handshaker.close(ctx.channel(), ((CloseWebSocketFrame) frame).retain());
			return;
		}
		//ping 请求
		if(frame instanceof PingWebSocketFrame){
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		//只支持文本格式，不支持二进制消息
		if(!(frame instanceof TextWebSocketFrame)){
			logger.error("目前只支持文本格式的消息");
			String errorMsg = ResponseBean.error(1101, "目前只支持文本格式的消息").toString();
			ctx.channel().writeAndFlush(new TextWebSocketFrame(errorMsg));
		}
		//消息处理
		String message = ((TextWebSocketFrame)frame).text();
		//数据解析与处理
        logger.info(message);
		Message requestPacket = JSONObject.parseObject(message, Message.class);
		int cmd = requestPacket.getCmd();
		CmdHandler cmdHandler = CmdManager.find(cmd);
		cmdHandler.handler(ctx, message);
	}

	public void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception{
		//如果http解码失败，返回http异常
		if(request instanceof HttpRequest){
			//如果是websocket请求就握手升级
			if ("Upgrade".equals(request.headers().get("Connection"))) {
				WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(null, null, false);
				handshaker = wsFactory.newHandshaker(request);
				if(handshaker == null){
					WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
				}else{
					//获取token
					String token = getRequestParameter(request, "token");
					if(StringUtils.isEmpty(token)){
						FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
								Unpooled.copiedBuffer("Token not found in request url", CharsetUtil.UTF_8));
						ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
						logger.error("Token not found in request url");
						return;
					}
					//根据token获取用户信息
					final String authToken = StringHelper.substring(token, 7);
					AuthUser user = (AuthUser) tokenUtil.getUserDetails(authToken);
					SysUser sysUser = iSystemService.getUserByLoginName(user.getLoginName());
					handshaker.handshake(ctx.channel(), request);
					//用户数据初始化
					initUser(ctx, sysUser);
				}
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	/**
	 * 用户数据初始化
	 */
	public void initUser(ChannelHandlerContext ctx,  SysUser sysUser){
		SessionUtil.bindSession(sysUser, ctx.channel());
		SessionUtil.bindChannelIdAndUserId(ctx.channel().id().toString(), sysUser.getId());
		//获取缓存的会话对象列表
		List<Chat> chatList = imUserService.getChatList(sysUser.getId());
		if(chatList == null || chatList.size() == 0){
			Chat chat = new Chat();
			chat.setChatId(sysUser.getId());
			chat.setChatName(sysUser.getNick());
			chat.setAvatar(sysUser.getAvatar());
			chat.setChatType(Constants.ChatType.USER_CHAT);
			chat.setUpdateTime(new Date());
			chat.setFromUserId(sysUser.getId());
			chat.setFromNick(sysUser.getNick());
			chatList.add(chat);
		}else{
			//初始化群聊通道
			for(Chat chat : chatList){
				if(chat.getChatType().equals(Constants.ChatType.GROUP_CHAT)){
					ChannelGroup channelGroup = SessionUtil.getChannelGroup(chat.getChatId());
					if(channelGroup == null){
						channelGroup = new DefaultChannelGroup(ctx.executor());
					}
					channelGroup.add(ctx.channel());
					SessionUtil.bindChannelGroup(chat.getChatId(), channelGroup);
				}
			}
		}
		Message response = new Message();
		response.setId(UUID.randomUUID().toString());
		response.setCmd(7);
		response.setCreateTime(System.currentTimeMillis());
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("chatList", chatList);
		jsonObject.put("user", sysUser);
		response.setExtras(jsonObject);
		ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(response)));
	}

	/**
	 * 得到http请求的query String
	 * @param req
	 * @param name
	 * @return
	 */
	private static String getRequestParameter(FullHttpRequest req, String name) {
		QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
		Map<String, List<String>> parameters = decoder.parameters();
		Set<Map.Entry<String, List<String>>> entrySet = parameters.entrySet();
		for (Map.Entry<String, List<String>> entry : entrySet) {
			if (entry.getKey().equalsIgnoreCase(name)) {
				return entry.getValue().get(0);
			}
		}
		return null;
	}

}
