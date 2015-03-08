package com.zml.oa.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ActivitiIdentifyCommonDao {

	private static final Logger logger = Logger.getLogger(ActivitiIdentifyCommonDao.class);
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	/**
     * 删除用户和组的关系
     */
    public void deleteAllUser() {
        String sql = "delete from ACT_ID_USER";
        jdbcTemplate.execute(sql);
        logger.debug("deleted from activiti user.");
    }
 
    /**
     * 删除用户和组的关系
     */
    public void deleteAllRole() {
        String sql = "delete from ACT_ID_GROUP";
        jdbcTemplate.execute(sql);
        logger.debug("deleted from activiti group.");
    }
 
    /**
     * 删除用户和组的关系
     */
    public void deleteAllMemerShip() {
        String sql = "delete from ACT_ID_MEMBERSHIP";
        jdbcTemplate.execute(sql);
        logger.debug("deleted from activiti membership.");
    }
}
