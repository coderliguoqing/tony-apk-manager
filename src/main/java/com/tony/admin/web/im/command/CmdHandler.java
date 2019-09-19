package com.tony.admin.web.im.command;

import io.netty.channel.ChannelHandlerContext;

/**
 * 接入信息操作处理类
 * @author Guoqing
 * @date 2019年7月25日 13:34
 */
public interface CmdHandler {

    /**
     * 命令主键
     * @return
     */
    public Integer cmd();

    /**
     * 交互信息处理
     * @param ctx   通道
     * @param object    消息对象
     */
    public void handler(ChannelHandlerContext ctx, String object) throws Exception;
}
