package com.tony.admin.web.sys.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.qiniu.storage.model.DefaultPutRet;
import com.tony.admin.web.common.utils.BMCloudStoreUtil;
import com.tony.admin.web.common.utils.QiNiuOssUtil;
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
import com.tony.admin.web.sys.model.DictInfo;
import com.tony.admin.web.sys.service.IDictService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;


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
		//是否强制刷新验证码
		String refresh = jsonObject.getString("refresh");
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

	/**
	 * <p>Title: createCloudUploadToken</p>
	 * <p>Description: 获取云盘上传的token</p>
	 * @param jsonObject
	 * @return
	 */
	@PostMapping(value="/createCloudUploadToken")
	public ResponseBean createCloudUploadToken(@RequestBody JSONObject jsonObject) {
		String token = null;
		Map<String, Object> paramsInToken = null;
		JSONObject params = jsonObject.getJSONObject("params");
		paramsInToken = params;
		try {
			token = BMCloudStoreUtil.createNSecondsUploadToken(jsonObject.getString("securityKey"), 1000, paramsInToken);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseBean(false, 1101, "获取凭证错误", null);
		}
		return new ResponseBean(true, 0, "请求成功", token);
	}

	/**
	 * 七牛OSS上传
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value="/qiNiuUpload")
	public ResponseBean qiNiuUpload(HttpServletRequest request){
		try{
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");
			InputStream is = file.getInputStream();
			String picName = file.getOriginalFilename();
			byte[] dataBuf = new byte[ 2048 ];
			//转成字节流
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			try {
				int rc = 0;
				while ((rc = is.read(dataBuf, 0, 100)) > 0) {
					swapStream.write(dataBuf, 0, rc);
				}
				dataBuf = swapStream.toByteArray();
			}finally {
				swapStream.flush();
				swapStream.close();
			}
			DefaultPutRet ret = QiNiuOssUtil.upload(null, picName, dataBuf);
			return new ResponseBean(true, 0, "上传成功", QiNiuOssUtil.getPrefixUrl() + ret.key);
		}catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
