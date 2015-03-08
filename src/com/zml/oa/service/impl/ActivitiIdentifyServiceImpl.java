package com.zml.oa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.dao.ActivitiIdentifyCommonDao;
import com.zml.oa.service.IActivitiBaseService;

@Service
public class ActivitiIdentifyServiceImpl implements IActivitiBaseService {

	@Autowired
    protected ActivitiIdentifyCommonDao activitiIdentifyCommonDao;
	
	@Override
	public void deleteAllUser() {
		this.activitiIdentifyCommonDao.deleteAllUser();

	}

	@Override
	public void deleteAllRole() {
		this.activitiIdentifyCommonDao.deleteAllRole();
	}

	@Override
	public void deleteAllMemerShip() {
		this.activitiIdentifyCommonDao.deleteAllMemerShip();
	}

}
