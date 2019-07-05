package com.tony.admin.web.sys.service;

/**
 * 主数据编码自动生成工具
 * @author Guoqing
 *
 */
public interface ICodeGeneratorService {
	
	/**
	 * 该方法主要为主数据生成有顺序唯一性编码编写
	 * 该方法主要利用redis的单线程和原子自增来保证数据的唯一性
	 * 如生成classification主数据编码L001  参数传入：classification,L,3
	 *   生成classification主数据编码L001001  参数传入：classification,L001,3
	 *   
	 * @param bpName	编码名称 如  classification
	 * @param bpType	编码开始类型   如  L、L001等
	 * @param codeLength	类型后面生成的编码的长度
	 * @return
	 */
	public String getUniqueCode( String bpName, String bpType, int codeLength );

}
