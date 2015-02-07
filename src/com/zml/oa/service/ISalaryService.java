package com.zml.oa.service;

import java.io.Serializable;

import com.zml.oa.entity.Salary;

public interface ISalaryService {

	public Serializable doAdd(Salary salary) throws Exception;
	
	public void doUpdate(Salary salary) throws Exception;
	
	public Salary findByUserId(String userId) throws Exception;
	
	public Salary findById(Integer id) throws Exception;
}
