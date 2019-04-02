package com.tony.admin.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务应用启动类
 * @author Guoqing.Lee
 * @date 2019年4月1日 下午2:21:39
 *
 */
@RestController
@SpringBootApplication
public class TonyApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(TonyApplication.class);

    public static void main( String[] args ){
    	SpringApplication.run(TonyApplication.class, args);
    	logger.info("Tony Admin Service started!!!");
    }

}
