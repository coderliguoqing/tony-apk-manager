package com.tony.admin.web.common.exception;

/**
 * 抽象接口
 * @author Guoqing.Lee
 * @date 2019年4月1日 下午2:31:26
 *
 */
public interface ServiceExceptionEnum {
    
    /**
     * 请求是否成功
     */
    Boolean getIsSuccess();
    
    /**
     * 获取返回的code
     */
    Integer getResponseCode();
    
    /**
     * 获取返回的message
     */
    String getResponseMsg();
}
