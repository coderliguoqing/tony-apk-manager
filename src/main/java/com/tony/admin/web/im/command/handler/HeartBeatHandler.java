package com.tony.admin.web.im.command.handler;

import com.alibaba.fastjson.JSONObject;
import com.tony.admin.web.im.command.CmdHandler;
import com.tony.admin.web.im.constant.Constants;
import com.tony.admin.web.im.model.HeartBeatBody;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 心跳处理
 * @author Guoqing 创建时间：2019年7月23日 16:41
 */
@Component
public class HeartBeatHandler implements CmdHandler {

    private static Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);

    @Override
    public Integer cmd() {
        return Constants.CmdType.HEARTBEAT;
    }

    @Override
    public void handler(ChannelHandlerContext ctx, String object) throws Exception {
        logger.info("heart beat msg: {}", object);
        HeartBeatBody heartBeat = new HeartBeatBody();
        heartBeat.setCmd(Constants.CmdType.HEARTBEAT);
        heartBeat.setMsgType(Constants.MsgType.SEND);
        heartBeat.setMessage(Constants.SessionConfig.HEARTBEAT_KEY + "-" + System.currentTimeMillis());
        String heartBeatMsg = JSONObject.toJSONString(heartBeat);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(heartBeatMsg));
    }
}
