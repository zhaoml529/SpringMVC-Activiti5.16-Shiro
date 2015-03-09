package com.zml.oa.dao;

import java.util.List;
import java.util.Map;

public interface IJdbcDao {

	/**
	 * 添加或修改
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public int saveOrUpdate(String sql, Map<String, Object> paramMap);
	
	/**
	 * 删除
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public int delete(String sql, Map<String, Object> paramMap);
	
	/**
	 * 批量增删改
	 * @param sql
	 * @param paramList
	 * @return
	 */
	public int[] batchExecute(String sql, List<Object[]> paramList);
	
	/**
	 * 分页查询
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> find(String sql, Map<String, Object> paramMap);
	
	/**
	 * 查询全部
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> findAll(String sql, Map<String, Object> paramMap);
	
	/**
	 * 查询记录数
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public int getCount(String sql, Map<String, Object> paramMap);
	
	
}
