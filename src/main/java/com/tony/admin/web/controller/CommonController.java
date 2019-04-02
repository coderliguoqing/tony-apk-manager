package com.tony.admin.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.tony.admin.web.common.redis.RedisRepository;
import com.tony.admin.web.common.response.ResponseBean;
import com.tony.admin.web.common.utils.VerifyCodeUtils;
import com.tony.admin.web.model.DictInfo;
import com.tony.admin.web.service.IDictService;


/**
 * 
 * @Description  公共处理的请求方法
 *
 * @author Guoqing
 * @Date 2018年1月22日
 */
@Validated
@CrossOrigin
@RestController
@RequestMapping("/admin/admin/common")
public class CommonController{
	
	@Autowired
	private IDictService dictService;
	@Autowired
	private RedisRepository redisRepository;
	
	/**
	 * post方法获取业务字典信息
	 * @param jsonObject
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value="/getDict")
	public List<DictInfo> getDictInfo( @RequestBody JSONObject jsonObject){
		String type = jsonObject.getString("dictTypeId");
		if( "".equals(type) || type == null ){
			return new ArrayList<DictInfo>();
		}
		List<DictInfo> dictList = new ArrayList<DictInfo>();
		
		//判断缓存中是否存在业务字典信息
		if( redisRepository.exists( "SYS_DICT_" + type )){
			String json = redisRepository.get("SYS_DICT_" + type);
			dictList = JSONObject.parseArray(JSONObject.parseObject(json).getString("dictList"), DictInfo.class);
		}else{
			dictList = dictService.getDictInfoByTypeId(type, "0");
			if( !dictList.isEmpty() ){
				JSONObject dictJson = new JSONObject();
				dictJson.put("dictList", dictList);
				redisRepository.set("SYS_DICT_" + type, dictJson.toJSONString());
			}
		}
		return dictList;
	}
	
	/**
	 * 生成验证码图片
	 * @param jsonObject
	 * @return
	 */
	@PostMapping(value="/getVerifyCode")
	public ResponseBean getVerifyCode(@RequestBody JSONObject jsonObject) {
		//前端生成的唯一的key，用户登录时验证验证码的正确性
		String uniqueKey = jsonObject.getString("uniqueKey");
		String refresh = jsonObject.getString("refresh");		//是否强制刷新验证码
		String img = null;
		try {
			String code = null;
			String key = VerifyCodeUtils.VERIFY_CODE + uniqueKey;
			if(redisRepository.exists(key)) {
				if( "true".equals(refresh) ) {
					code = VerifyCodeUtils.getRandKey(4);
				}else {
					code = redisRepository.get(key);					
				}
			}else {
				code = VerifyCodeUtils.getRandKey(4);
			}
			img = VerifyCodeUtils.verifyCode(90, 25, code);
			//将验证码存入redis，5分钟有效
			redisRepository.setExpire(key, code, 5*60);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseBean(true, 0, "请求成功", img);
	}
}
