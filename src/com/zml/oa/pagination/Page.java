package com.zml.oa.pagination;

import java.util.Collections;
import java.util.List;
/**
 * easyui 分页
 * page   当前第几页
 * rows   每页显示数据数
 * total  一共有多少数据
 * result 页面显示的数据
 * @author ZML
 *
 * @param <T>
 */
public class Page<T> {
	private Integer page;
	private Integer rows;
	
	private Integer total;
	private List<T> result = Collections.emptyList();
	
	public Page(){
		
	}
	
	public Page(Integer page, Integer rows){
		this.page = page;
		this.rows = rows;
	}
	
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	/**
	 * 计算分页
	 * @param total
	 * @return
	 */
	public int[] getPageParams(Integer total) {
		if(this.page == null || this.page < 1){
			this.page = 1;
		}
		if(this.rows == null || this.rows < 1){
			this.rows = 10;
		}
		int firstResult = (this.page - 1) * this.rows;
		int maxResult = this.rows;
		this.total = total;
		
		//校验分页情况
		if (firstResult >= total || firstResult < 0) {
			firstResult = 0;
			this.page = 1;
		}
		return new int[]{firstResult, maxResult};
	}
}
