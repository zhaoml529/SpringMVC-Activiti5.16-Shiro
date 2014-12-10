package com.zml.oa.service.impl;

import java.io.Serializable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zml.oa.entity.Salary;
import com.zml.oa.service.ISalaryService;

@Service
public class SalaryServiceImpl extends BaseServiceImpl<Salary> implements ISalaryService {

	
	@Override
	public Serializable doAdd(Salary salary) throws Exception {
		return add(salary);
	}

	

}
