package com.tony.admin.web;

import com.tony.admin.web.im.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 服务应用启动类
 * @author Guoqing.Lee
 * @date 2019年4月1日 下午2:21:39
 *
 */
@RestController
@SpringBootApplication
public class TonyApplication implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(TonyApplication.class);

	@Resource(name = "imWebSocketServer")
	private NettyServer imWebSocketServer;

    public static void main( String[] args ){
    	SpringApplication.run(TonyApplication.class, args);
    	logger.info("Tony Admin Service started!!!");
    }

    @Override
    public void run(String...strings){
		try{
			imWebSocketServer.start();
		}catch (Exception e){
			logger.error("startup error!", e);
		}
	}

}
