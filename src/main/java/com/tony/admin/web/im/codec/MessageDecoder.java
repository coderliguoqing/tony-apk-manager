package com.tony.admin.web.im.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * 协议包解码
 * @author Guoqing.Lee
 * @date 2019年6月20日 下午3:48:56
 *
 */
public class MessageDecoder extends MessageToMessageDecoder<WebSocketFrame>{

	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
		ByteBuf buffer = ((WebSocketFrame)frame).content();
		out.add(buffer);
		buffer.retain();
	}

}
