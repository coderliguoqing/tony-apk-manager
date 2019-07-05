package com.tony.admin.web.im.codec;

import java.util.List;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * 协议包编码
 * @author Guoqing.Lee
 * @date 2019年6月20日 下午3:50:56
 *
 */
public class MessageEncoder extends MessageToMessageEncoder<MessageLiteOrBuilder> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) throws Exception {
		ByteBuf buffer = null;
		if(msg instanceof MessageLite) {
			buffer = Unpooled.wrappedBuffer(((MessageLite)msg).toByteArray());
		}
		if(msg instanceof MessageLite.Builder) {
			buffer = Unpooled.wrappedBuffer(((MessageLite.Builder)msg).build().toByteArray());
		}
		//因为客户端不能直接解析protobuf编码生成的，所以在转成websocket二进制流
		WebSocketFrame frame = new BinaryWebSocketFrame(buffer);
		out.add(frame);
	}

}
