package com.zml.oa.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zml.oa.entity.ExpenseAccount;
import com.zml.oa.service.IExpenseService;

@Service
public class ExpenseServiceImpl extends BaseServiceImpl<ExpenseAccount> implements IExpenseService {

	@Override
	public Serializable doAdd(ExpenseAccount bean) throws Exception {
		return add(bean);
	}

	@Override
	public void doUpdate(ExpenseAccount bean) throws Exception {
		update(bean);
	}

	@Override
	public void doDelete(ExpenseAccount bean) throws Exception {
		delete(bean);
	}

	@Override
	public List<ExpenseAccount> toList(Integer userId) throws Exception {
		List<ExpenseAccount> list = findByPage("ExpenseAccount", new String[]{"userId"}, new String[]{userId.toString()});
		return list;
	}

	@Override
	public ExpenseAccount findById(Integer id) throws Exception {
		return getUnique("ExpenseAccount", new String[]{"id"}, new String[]{id.toString()});
	}

	@Override
	public List<ExpenseAccount> findByStatus(Integer userId, String status)
			throws Exception {
		List<ExpenseAccount> list = findByPage("ExpenseAccount", new String[]{"userId","status"}, new String[]{userId.toString(), status});
		return list;
	}

}
