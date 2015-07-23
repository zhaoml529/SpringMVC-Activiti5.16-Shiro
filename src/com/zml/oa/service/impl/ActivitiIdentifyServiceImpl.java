package com.zml.oa.service.impl;

import java.util.HashMap;
import java.util.Map;

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

	@Override
	public void updateMembership(String userId, String groupId) throws Exception {
		String sql = "update ACT_ID_MEMBERSHIP set GROUP_ID_=:groupId where USER_ID_=:userId ";
		Map<String, Object> paramMap = new HashMap<String, Object>();  
	    paramMap.put("groupId", groupId);  
	    paramMap.put("userId", userId);  
		this.jdbcDao.saveOrUpdate(sql, paramMap);
		logger.debug("update ACT_ID_MEMBERSHIP.");
		
	}

}
