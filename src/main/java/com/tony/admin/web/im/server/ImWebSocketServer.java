package com.tony.admin.web.im.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tony.admin.web.im.NettyServer;
import com.tony.admin.web.im.codec.MessageDecoder;
import com.tony.admin.web.im.codec.MessageEncoder;
import com.tony.admin.web.im.constant.Constants;
import com.tony.admin.web.im.model.proto.MessageModel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * websocket server
 * @author Guoqing.Lee
 * @date 2019年6月19日 上午11:52:10
 *
 */
@Component("iMwebSocketServer")
public class ImWebSocketServer implements NettyServer {
	
	private Logger logger = LoggerFactory.getLogger(ImWebSocketServer.class);
	
	private ProtobufDecoder decoder = new ProtobufDecoder(MessageModel.Message.getDefaultInstance());
	
	@Value("${server.websocket.port}")
	private int port;
	
	private final EventLoopGroup boosGroup = new NioEventLoopGroup();
	private final EventLoopGroup workGroup = new NioEventLoopGroup();
	
	private ChannelFuture channelFuture;

	@Override
	public void start() throws InterruptedException {
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boosGroup, workGroup)
				//指定IO模型为NIO，netty的优势就在于NIO的处理
				.channel(NioServerSocketChannel.class)
				//指定客户端连接请求队列大小		
				.option(ChannelOption.SO_BACKLOG, 1024)
				//开启心跳监测
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				//关闭nagel算法，true表示关闭，如果要求高实时性，就设置为true关闭；如果需要减少发送次数，减少网络交互，就设置为false开启；
				.childOption(ChannelOption.TCP_NODELAY, true);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					//编解码HTTP请求
					ch.pipeline().addLast(new HttpServerCodec());
					// 主要用于处理大数据流，比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的; 增加之后就不用考虑这个问题了
					ch.pipeline().addLast(new ChunkedWriteHandler());
					// 把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse，
		            // 原因是HTTP解码器会在每个HTTP消息中生成多个消息对象HttpRequest/HttpResponse,HttpContent,LastHttpContent
					ch.pipeline().addLast(new HttpObjectAggregator(Constants.ImServerConfig.MAX_AGGREGATED_CONTENT_LENGTH));
					 // WebSocket数据压缩
		            ch.pipeline().addLast(new WebSocketServerCompressionHandler());
		            //协议包长度限制
					ch.pipeline().addLast(new WebSocketServerProtocolHandler("/socket", null, true, Constants.ImServerConfig.MAX_FRAME_LENGTH));
					//协议包解码
					ch.pipeline().addLast(new MessageDecoder());
					//协议包编码
					ch.pipeline().addLast(new MessageEncoder());
					// 协议包解码时指定Protobuf字节数实例化为CommonProtocol类型
		            ch.pipeline().addLast(decoder);
		            //心跳检测
		            ch.pipeline().addLast(new IdleStateHandler(Constants.ImServerConfig.READ_IDLE_TIME,Constants.ImServerConfig.WRITE_IDLE_TIME,0));
				}
			});
			logger.info("Starting ImWebSocketServer... Port:" + port);
			channelFuture = bootstrap.bind(port).sync();
		} finally {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					shutdown();
				}
			});
		}
	}

	@Override
	public void restart() throws InterruptedException {
		shutdown();
		start();
	}

	@Override
	public void shutdown() {
		logger.info("shutdown im websocketserver...");
		if(channelFuture != null) {
			channelFuture.channel().close().syncUninterruptibly();
		}
		if(boosGroup != null) {
			boosGroup.shutdownGracefully();
		}
		if(workGroup != null) {
			workGroup.shutdownGracefully();
		}
		logger.info("shutdown im websocketserver complete.");
	}

}
