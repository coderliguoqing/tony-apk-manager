package com.tony.admin.web.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tony.admin.web.common.dao.CrudDao;
import com.tony.admin.web.model.DictInfo;
import com.tony.admin.web.model.SysDictEntry;
import com.tony.admin.web.model.SysDictType;



/**
 * 业务字典mapper
 * @author Guoqing.Lee
 * @date 2019年1月16日 下午1:26:42
 *
 */
@Mapper
public interface SysDictTypeMapper extends CrudDao<SysDictType> {
	
	/**
	 * 根据字典id查询业务字典信息
	 * @param dicttypeId, del_flag
	 * @return
	 */
	List<DictInfo> getDictInfoByTypeId(HashMap<String, Object> params);
	
	/**
	 * 根据字典信息获取entry列表
	 * @param dicttypeId
	 * @return
	 */
	List<SysDictEntry> getDictEntryByTypeId( Integer id );
	
}