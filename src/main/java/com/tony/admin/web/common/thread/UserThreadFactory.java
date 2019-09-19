package com.tony.admin.web.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 线程工厂
 * 自定义实现线程工厂的好处是，对创建的线程有明确的标识，就像生产的产品批号一样，为线程本身指定有意义的名称和响应的序列号
 * @author Guoqing.Lee
 * @date 2019年5月24日 上午11:20:52
 *
 */
public class UserThreadFactory implements ThreadFactory{
	
	private static final Logger logger = LoggerFactory.getLogger(UserThreadFactory.class);

	private final String namePrefix;
	private final AtomicInteger nextId = new AtomicInteger(1);
	
	public UserThreadFactory(String whatFeatureOfGroup) {
		this.namePrefix = "UserThreadFactory's " + whatFeatureOfGroup + "-Worker-";
	}

	@Override
	public Thread newThread(Runnable r) {
		String name = namePrefix + nextId.getAndIncrement();
		Thread thread = new Thread(null, r, name, 0);
		logger.info(thread.getName());
		return thread;
	}
	
}
