package com.zml.oa.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.entity.ExpenseAccount;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IBaseService;
import com.zml.oa.service.IExpenseService;

@Service
public class ExpenseServiceImpl implements IExpenseService {

	@Autowired 
	private IBaseService<ExpenseAccount> baseService;
	
	@Override
	public Serializable doAdd(ExpenseAccount bean) throws Exception {
		return this.baseService.add(bean);
	}

	@Override
	public void doUpdate(ExpenseAccount bean) throws Exception {
		this.baseService.update(bean);
	}

	@Override
	public void doDelete(ExpenseAccount bean) throws Exception {
		this.baseService.delete(bean);
	}

	@Override
	public List<ExpenseAccount> toList(Integer userId) throws Exception {
		List<ExpenseAccount> list = this.baseService.findByPage("ExpenseAccount", new String[]{"userId"}, new String[]{userId.toString()});
		return list;
	}

	@Override
	public ExpenseAccount findById(Integer id) throws Exception {
		return this.baseService.getUnique("ExpenseAccount", new String[]{"id"}, new String[]{id.toString()});
	}

	@Override
	public List<ExpenseAccount> findByStatus(Integer userId, String status, Page<ExpenseAccount> page)
			throws Exception {
		List<ExpenseAccount> list = this.baseService.getListPage("ExpenseAccount", new String[]{"userId","status"}, new String[]{userId.toString(), status}, page);
		return list;
	}

}
