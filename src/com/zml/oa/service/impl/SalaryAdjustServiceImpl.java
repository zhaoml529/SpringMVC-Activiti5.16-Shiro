package com.zml.oa.service.impl;

import java.io.Serializable;
import java.util.List;

import com.zml.oa.entity.SalaryAdjust;
import com.zml.oa.service.ISalaryAdjustService;

public class SalaryAdjustServiceImpl extends BaseServiceImpl<SalaryAdjust> implements ISalaryAdjustService {

	@Override
	public Serializable doAdd(SalaryAdjust bean) throws Exception {
		return add(bean);
	}

	@Override
	public void doUpdate(SalaryAdjust bean) throws Exception {
		update(bean);
	}

	@Override
	public void doDelete(SalaryAdjust bean) throws Exception {
		delete(bean);
	}

	@Override
	public List<SalaryAdjust> toList(Integer userId) throws Exception {
		List<SalaryAdjust> list = findByPage("SalaryAdjust", new String[]{"userId"}, new String[]{userId.toString()});
		return list;
	}

	@Override
	public SalaryAdjust findById(Integer id) throws Exception {
		return getUnique("SalaryAdjust", new String[]{"userId"}, new String[]{id.toString()});
	}

	@Override
	public List<SalaryAdjust> findByStatus(Integer userId, String status)
			throws Exception {
		List<SalaryAdjust> list = findByWhere("SalaryAdjust", new String[]{"userId", "status"}, new String[]{userId.toString(), status});
		return list;
	}

}
