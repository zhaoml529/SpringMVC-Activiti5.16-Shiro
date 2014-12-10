package com.zml.oa.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zml.oa.entity.Vacation;
import com.zml.oa.service.IVacationService;

@Service
public class VacationServiceImpl extends BaseServiceImpl<Vacation> implements IVacationService {

	@Override
	public Serializable doAdd(Vacation vacation) throws Exception {
		return add(vacation);
	}

	@Override
	public void doUpdate(Vacation vacation) throws Exception {
		update(vacation);
	}

	@Override
	public void doDelete(Vacation vacation) throws Exception {
		delete(vacation);
	}

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public List<Vacation> toList(Integer userId) throws Exception {
		List<Vacation> list = findByPage("Vacation", new String[]{"userId"}, new String[]{userId.toString()});
		return list;
	}

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public Vacation findById(Integer id) throws Exception {
		return getUnique("Vacation", new String[]{"id"}, new String[]{id.toString()});
	}

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public List<Vacation> findByStatus(Integer userId, String status) throws Exception {
		List<Vacation> list = findByPage("Vacation", new String[]{"userId","status"}, new String[]{userId.toString(), status});
		return list;
	}
}
