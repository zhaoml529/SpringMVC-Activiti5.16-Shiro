package com.zml.oa.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zml.oa.entity.Vacation;
import com.zml.oa.service.IVacationService;

@Service
public class VacationServiceImpl extends BaseServiceImpl<Vacation> implements IVacationService {

	@Override
	public Serializable doAdd() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable doUpdate() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable doDelete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vacation> toList(Integer userId) throws Exception {
		List<Vacation> list = findByPage("Vacation", new String[]{"userId"}, new String[]{userId.toString()});
		return list;
	}

	@Override
	public Vacation findById(Integer id) throws Exception {
		return getUnique("Vacation", new String[]{"id"}, new String[]{id.toString()});
	}

}
