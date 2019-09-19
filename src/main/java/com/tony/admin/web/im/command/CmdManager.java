package com.tony.admin.web.im.command;

import com.tony.admin.web.common.utils.SpringContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Guoqing
 * @desc cmd命令解析处理类
 * @date 2019/7/25
 */
@Configuration
public class CmdManager {

    private static final Map<Integer, CmdHandler> cmdHandlerMap = new ConcurrentHashMap<>();

    @Bean
    public Map<Integer, CmdHandler> cmdHandlers(){
        Map<String, CmdHandler> beans = SpringContextUtil.getApplicationContext().getBeansOfType(CmdHandler.class);
        for(CmdHandler cmdHandler : beans.values()){
            cmdHandlerMap.put(cmdHandler.cmd(), cmdHandler);
        }
        return cmdHandlerMap;
    }

    public static CmdHandler find(Integer cmd){
        return cmdHandlerMap.get(cmd);
    }

}
