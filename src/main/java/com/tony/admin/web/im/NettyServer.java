package com.tony.admin.web.im;

/**
 * 服务接口
 * @author Guoqing.Lee
 * @date 2019年6月19日 上午11:51:11
 *
 */
public interface NettyServer {
	
	void start() throws Exception;
	
	void restart() throws Exception;
	
	void shutdown();

}
