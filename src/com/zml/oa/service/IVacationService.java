package com.zml.oa.service;

import java.io.Serializable;
import java.util.List;

import com.zml.oa.entity.Vacation;
import com.zml.oa.pagination.Page;

public interface IVacationService {

	public Serializable doAdd(Vacation vacation) throws Exception;
	
	public void doUpdate(Vacation vacation) throws Exception;
	
	public void doDelete(Vacation vacation) throws Exception;
	
	public List<Vacation> toList(Integer userId) throws Exception;
	
	public Vacation findById(Integer id) throws Exception;
	
	public List<Vacation> findByStatus(Integer userId, String status, Page<Vacation> page) throws Exception; 
}
