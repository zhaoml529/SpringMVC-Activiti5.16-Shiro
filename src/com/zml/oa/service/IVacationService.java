package com.zml.oa.service;

import java.io.Serializable;
import java.util.List;

import com.zml.oa.entity.Vacation;

public interface IVacationService extends IBaseService<Vacation> {

	public Serializable doAdd() throws Exception;
	
	public Serializable doUpdate() throws Exception;
	
	public Serializable doDelete() throws Exception;
	
	public List<Vacation> toList(Integer userId) throws Exception;
	
	public Vacation findById(Integer id) throws Exception;
	
	public List<Vacation> findByStatus(Integer userId, String status) throws Exception; 
}
