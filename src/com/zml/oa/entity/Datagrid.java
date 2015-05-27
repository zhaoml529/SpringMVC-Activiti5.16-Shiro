package com.zml.oa.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
/**
 * easyui分页组件datagrid
 * @author ZML
 *
 */
public class Datagrid<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<T>  rows= Collections.emptyList();
	private Integer total=0;
	
	public Datagrid(){
		
	}
	
	public Datagrid(Integer total, List<T> rows){
		this.rows = rows;
		this.total = total;
	}
	
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}

	
	
}
