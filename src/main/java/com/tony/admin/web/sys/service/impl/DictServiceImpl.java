package com.tony.admin.web.sys.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tony.admin.web.common.dao.Paging;
import com.tony.admin.web.sys.mapper.SysDictEntryMapper;
import com.tony.admin.web.sys.mapper.SysDictTypeMapper;
import com.tony.admin.web.sys.model.DictInfo;
import com.tony.admin.web.sys.model.SysDictEntry;
import com.tony.admin.web.sys.model.SysDictType;
import com.tony.admin.web.sys.service.IDictService;
import tk.mybatis.mapper.entity.Example;


/**
 * 业务字典相关的实现类
 * 
 * @author Guoqing
 *
 */
@Service
public class DictServiceImpl implements IDictService {
	
	@Autowired
	private SysDictEntryMapper dictEntryMapper;
	@Autowired
	private SysDictTypeMapper dictTypeMapper;

	@Override
	public PageInfo<SysDictType> findDictTypePage(Paging page, SysDictType dictType) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize(), true, true);
		Example example = new Example(SysDictType.class);
		Example.Criteria criteria = example.createCriteria();
		if(StringUtils.isNotEmpty(dictType.getDicttypeId())){
			criteria.andLike("dicttypeId", dictType.getDicttypeId());
		}
		if(StringUtils.isNotEmpty(dictType.getDicttypeName())){
			criteria.andLike("dicttypeName", dictType.getDicttypeName());
		}
		criteria.andEqualTo("delFlag", "0");
		example.orderBy("dicttypeId").asc();
		List<SysDictType> list = dictTypeMapper.selectByExample(example);
		return new PageInfo<>(list);
	}

	@Override
	public SysDictType saveDictType(SysDictType dictType) {
		if( dictType.getId() == null ){
			dictType.setCreateTime(new Date());
			dictType.setOpTime(new Date());
			dictType.setDelFlag("0");
			dictTypeMapper.insert(dictType);
		}else{
			dictType.setOpTime(new Date());
			dictTypeMapper.updateByPrimaryKeySelective(dictType);
		}
		return dictType;
	}

	@Override
	public void updateDictType(SysDictType dictType) {
		dictType.setOpTime(new Date());
		dictTypeMapper.updateByPrimaryKeySelective(dictType);
	}

	@Override
	public void deleteDictTypeById(Integer id) {
		SysDictType dictType = new SysDictType();
		dictType.setId(id);
		dictType.setDelFlag("1");
		dictType.setOpTime(new Date());
		dictTypeMapper.updateByPrimaryKeySelective(dictType);
	}

	@Override
	public PageInfo<SysDictEntry> findDictEntryPage(Paging page, SysDictEntry dictEntry) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize(), true, true);
		Example example = new Example(SysDictEntry.class);
		Example.Criteria criteria = example.createCriteria();
		if(StringUtils.isNotEmpty(dictEntry.getDicttypeId())){
			criteria.andEqualTo("dicttypeId", dictEntry.getDicttypeId());
		}
		criteria.andEqualTo("delFlag", "0");
		example.orderBy("sort").asc();
		List<SysDictEntry> list = dictEntryMapper.selectByExample(example);
		return new PageInfo<> (list);
	}

	@Override
	public SysDictEntry saveDictEntry(SysDictEntry dictEntry) {
		if( dictEntry.getId() == null ){
			dictEntry.setCreateTime(new Date());
			dictEntry.setOpTime(new Date());
			dictEntry.setDelFlag("0");
			dictEntryMapper.insert(dictEntry);
		}else{
			dictEntry.setOpTime(new Date());
			dictEntryMapper.updateByPrimaryKeySelective(dictEntry);
		}
		return dictEntry;
	}

	@Override
	public void updateDictEntry(SysDictEntry dictEntry) {
		dictEntry.setOpTime(new Date());
		dictEntryMapper.updateByPrimaryKeySelective(dictEntry);
	}

	@Override
	public void deleteDictEntryById(Integer id) {
		SysDictEntry dictEntry = new SysDictEntry();
		dictEntry.setId(id);
		dictEntry.setDelFlag("1");
		dictEntry.setOpTime(new Date());
		dictEntryMapper.updateByPrimaryKeySelective(dictEntry);
	}

	@Override
	public boolean checkDictTypeId(String dicttypeId) {
		SysDictType dictType = new SysDictType();
		dictType.setDicttypeId(dicttypeId);
		dictType.setDelFlag("0");
		List<SysDictType> list = dictTypeMapper.select(dictType);
		if( list.size() > 0 ){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean checkDictId(String dicttypeId, String dictId) {
		SysDictEntry dictEntry = new SysDictEntry();
		dictEntry.setDicttypeId(dicttypeId);
		dictEntry.setDictId(dictId);
		dictEntry.setDelFlag("0");
		List<SysDictEntry> list = dictEntryMapper.select(dictEntry);
		if( list.size() > 0 ){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<DictInfo> getDictInfoByTypeId(String dicttypeId, String delFlag) {
		HashMap<String, Object> params = new HashMap<String, Object>(2);
		params.put("type", dicttypeId);
		params.put("delFlag", delFlag);
		return dictTypeMapper.getDictInfoByTypeId(params);
	}

	@Override
	public List<SysDictEntry> getDictEntryByTypeId(Integer id) {
		return dictTypeMapper.getDictEntryByTypeId(id);
	}
	
	@Override
	public SysDictEntry getDictEntryByTypeIdAndId(String dicttypeId, String dictId ) {
		HashMap<String, Object> params = new HashMap<String, Object>(2);
		params.put("dicttypeId", dicttypeId);
		params.put("dictId", dictId);
		return dictEntryMapper.getDictEntryByTypeIdAndId(params);
	}

}
