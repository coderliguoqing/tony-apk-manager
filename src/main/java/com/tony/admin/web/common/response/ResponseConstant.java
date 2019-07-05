package com.tony.admin.web.common.response;

/**
 * 系统接口结果常量枚举类
 * @author Guoqing.Lee
 * @date 2019年6月6日 下午2:50:01
 *
 */
public enum ResponseConstant {
	
	SUCCESS(true, 0, "请求成功"),
	SERVER_ERROR(false, 1000, "服务器繁忙，请稍后再试"),
	IVALID_ERROR(false, 1102, "请求参数格式错误"),
	IVALID_VERIFY_CODE(false, 1001, "验证码已失效，请重新登录"),
	VERIFY_CODE_ERROR(false, 1002, "验证码错误，请重新登录"),
	USERNAME_ERROR(false, 1004, "用户名或密码错误"),
	ACCOUNT_LOCKED(false, 1005, "账号已被锁定，不能登录"),
	USER_INFO_ERROR(false, 1006, "用户信息错误，请重试"),
	IVALID_QRCODE(false, 1101, "二维码已失效，请重新获取"),
	CANCEL_SUCCESS(true, 1103, "取消成功"),
	GET_MESSAGE_FAILED(false, 1202, "获取信息失败，请重新获取"),
	SCAN_SUCCESS(true, 1102, "扫描成功");
	
	private boolean isSuccess;
	
	private int responseCode;
	
	private String responseMsg;
	
	ResponseConstant(boolean isSuccess, int responseCode, String responseMsg){
		this.isSuccess = isSuccess;
		this.responseCode = responseCode;
		this.responseMsg = responseMsg;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	
}
