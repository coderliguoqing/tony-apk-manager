package com.tony.admin.web.mapper;

import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

import com.tony.admin.web.common.dao.CrudDao;
import com.tony.admin.web.model.SysDictEntry;


/**
 * 业务字典mapper
 * @author Guoqing.Lee
 * @date 2019年1月16日 下午1:25:58
 *
 */
@Mapper
public interface SysDictEntryMapper extends CrudDao<SysDictEntry> {
	
	/**
	 * 根据类型和Id查询
	 * @param params
	 * @return
	 */
	SysDictEntry getDictEntryByTypeIdAndId(HashMap<String, Object> params);
}