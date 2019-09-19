/**  
* <p>Title: CloudUploadConfig.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2018</p>  
* <p>Company: www.bluemoon.com</p>  
* @author Guoqing  
* @date 2018年7月4日  
*/  
package com.tony.admin.web.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**  
* <p>Title: CloudUploadConfig</p>  
* <p>Description: 云盘简单上传参数读取类</p>  
* @author Guoqing  
* @date 2018年7月4日  
*/
@Component
@ConfigurationProperties(prefix="cloud")
public class CloudUploadConfig {
	
	private String appID;
	private String secretKey;
	private String simpleUploadEntry;
	private String bmCloud;
	
	/**
	 * @return the appID
	 */
	public String getAppID() {
		return appID;
	}
	/**
	 * @param appID the appID to set
	 */
	public void setAppID(String appID) {
		this.appID = appID;
	}
	/**
	 * @return the secretKey
	 */
	public String getSecretKey() {
		return secretKey;
	}
	/**
	 * @param secretKey the secretKey to set
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	/**
	 * @return the simpleUploadEntry
	 */
	public String getSimpleUploadEntry() {
		return simpleUploadEntry;
	}
	/**
	 * @param simpleUploadEntry the simpleUploadEntry to set
	 */
	public void setSimpleUploadEntry(String simpleUploadEntry) {
		this.simpleUploadEntry = simpleUploadEntry;
	}
	/**
	 * @return the bmCloud
	 */
	public String getBmCloud() {
		return bmCloud;
	}
	/**
	 * @param bmCloud the bmCloud to set
	 */
	public void setBmCloud(String bmCloud) {
		this.bmCloud = bmCloud;
	}
	
	

}
