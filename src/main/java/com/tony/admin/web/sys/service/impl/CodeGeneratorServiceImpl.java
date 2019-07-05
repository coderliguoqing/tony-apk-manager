package com.tony.admin.web.sys.service.impl;

import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tony.admin.web.common.redis.RedisRepository;
import com.tony.admin.web.sys.service.ICodeGeneratorService;


/**
 * 
 * @author Guoqing.Lee
 * @date 2019年1月16日 下午1:28:55
 *
 */
@Service
public class CodeGeneratorServiceImpl implements ICodeGeneratorService{
	
	@Autowired
	private RedisRepository redisRepository;

	@Override
	public String getUniqueCode(String bpName, String bpType, int codeLength) {
		String redisKey = "OM_CODE_" + bpName + "_" + bpType;
		
		// 规定编号的格式
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < codeLength; i++) {
			sb.append("0");
		}
		DecimalFormat df = new DecimalFormat(sb.toString());
		long code = redisRepository.incr(redisKey);
		String resCode = df.format( code );

		return bpType + resCode;
	}

}
