package com.zml.oa.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.entity.Vacation;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IBaseService;
import com.zml.oa.service.IVacationService;

@Service
public class VacationServiceImpl implements IVacationService {
	
	@Autowired 
	private IBaseService<Vacation> baseService;
	
	@Override
	public Serializable doAdd(Vacation vacation) throws Exception {
		return this.baseService.add(vacation);
	}

	@Override
	public void doUpdate(Vacation vacation) throws Exception {
		this.baseService.update(vacation);
	}

	@Override
	public void doDelete(Vacation vacation) throws Exception {
		this.baseService.delete(vacation);
	}

	@Override
	public List<Vacation> toList(Integer userId) throws Exception {
		List<Vacation> list = this.baseService.findByPage("Vacation", new String[]{"userId"}, new String[]{userId.toString()});
		return list;
	}

	@Override
	public Vacation findById(Integer id) throws Exception {
		return this.baseService.getUnique("Vacation", new String[]{"id"}, new String[]{id.toString()});
	}

	@Override
	public List<Vacation> findByStatus(Integer userId, String status, Page<Vacation> page) throws Exception {
		List<Vacation> list = this.baseService.getListPage("Vacation", new String[]{"userId","status"}, new String[]{userId.toString(), status}, page);
		return list;
	}
}
