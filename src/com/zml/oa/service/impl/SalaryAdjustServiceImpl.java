package com.zml.oa.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.entity.SalaryAdjust;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IBaseService;
import com.zml.oa.service.ISalaryAdjustService;

@Service
public class SalaryAdjustServiceImpl implements ISalaryAdjustService {

	@Autowired
	private IBaseService<SalaryAdjust> baseService;
	
	@Override
	public Serializable doAdd(SalaryAdjust bean) throws Exception {
		return this.baseService.add(bean);
	}

	@Override
	public void doUpdate(SalaryAdjust bean) throws Exception {
		this.baseService.update(bean);
	}

	@Override
	public void doDelete(SalaryAdjust bean) throws Exception {
		this.baseService.delete(bean);
	}

	@Override
	public List<SalaryAdjust> toList(Integer userId) throws Exception {
		List<SalaryAdjust> list = this.baseService.findByPage("SalaryAdjust", new String[]{"userId"}, new String[]{userId.toString()});
		return list;
	}

	@Override
	public SalaryAdjust findByUserId(Integer userId) throws Exception {
		return this.baseService.getUnique("SalaryAdjust", new String[]{"userId"}, new String[]{userId.toString()});
	}

	@Override
	public List<SalaryAdjust> findByStatus(Integer userId, String status, Page<SalaryAdjust> page)
			throws Exception {
		List<SalaryAdjust> list = this.baseService.getListPage("SalaryAdjust", new String[]{"userId", "status"}, new String[]{userId.toString(), status}, page);
		return list;
	}

	@Override
	public SalaryAdjust findById(Integer id) throws Exception {
		return this.baseService.getUnique("SalaryAdjust", new String[]{"id"}, new String[]{id.toString()});
	}

}
