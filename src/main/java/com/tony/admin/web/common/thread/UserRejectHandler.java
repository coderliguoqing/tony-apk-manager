package com.tony.admin.web.common.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 拒绝策略应该考虑到业务场景，返回响应的提示或者友好的跳转
 * 如下示例为简单实例，在实际应用中应该根据业务场景进行调整
 * @author Guoqing.Lee
 * @date 2019年5月24日 上午11:54:09
 *
 */
public class UserRejectHandler implements RejectedExecutionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRejectHandler.class);

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		 logger.info("task rejected. " + executor.toString());
	}

}
