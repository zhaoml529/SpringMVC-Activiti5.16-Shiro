package com.zml.oa.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.dao.IJdbcDao;
import com.zml.oa.service.IActivitiBaseService;

@Service
public class ActivitiIdentifyServiceImpl implements IActivitiBaseService {

	private static final Logger logger = Logger.getLogger(ActivitiIdentifyServiceImpl.class);

	@Autowired
	protected IJdbcDao jdbcDao;
	
	@Override
	public void deleteAllUser() {
		String sql = "delete from ACT_ID_USER";
		this.jdbcDao.delete(sql, null);
		logger.debug("deleted from activiti user.");
	}

	@Override
	public void deleteAllRole() {
		String sql = "delete from ACT_ID_GROUP";
		this.jdbcDao.delete(sql, null);
		logger.debug("deleted from activiti group.");
	}

	@Override
	public void deleteAllMemerShip() {
		String sql = "delete from ACT_ID_MEMBERSHIP";
		this.jdbcDao.delete(sql, null);
		logger.debug("deleted from activiti membership.");
	}

}
