package com.zml.oa.service;

import java.io.Serializable;

import com.zml.oa.entity.Salary;

public interface ISalaryService extends IBaseService<Salary> {

	public Serializable doAdd(Salary salary) throws Exception;
}
