package com.zml.oa.service;

import java.io.Serializable;
import java.util.List;

import com.zml.oa.entity.SalaryAdjust;

public interface ISalaryAdjustService {

	public Serializable doAdd(SalaryAdjust bean) throws Exception;
	
	public void doUpdate(SalaryAdjust bean) throws Exception;
	
	public void doDelete(SalaryAdjust bean) throws Exception;
	
	public List<SalaryAdjust> toList(Integer userId) throws Exception;
	
	public SalaryAdjust findById(Integer id) throws Exception;
	
	public List<SalaryAdjust> findByStatus(Integer userId, String status) throws Exception; 
}
