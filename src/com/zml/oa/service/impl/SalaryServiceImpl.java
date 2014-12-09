package com.zml.oa.service.impl;

import java.io.Serializable;

import com.zml.oa.entity.Salary;
import com.zml.oa.service.ISalaryService;

public class SalaryServiceImpl extends BaseServiceImpl<Salary> implements ISalaryService {

	
	@Override
	public Serializable doAdd(Salary salary) throws Exception {
		return add(salary);
	}

	

}
