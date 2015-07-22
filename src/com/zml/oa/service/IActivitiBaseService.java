package com.zml.oa.service;

import com.zml.oa.entity.User;

public interface IActivitiBaseService {

	/**
     * 删除用户和组的关系
     */
	public void deleteAllUser() throws Exception;
	
	/**
     * 删除用户和组的关系
     */
    public void deleteAllRole() throws Exception;
    
    /**
     * 删除用户和组的关系
     */
    public void deleteAllMemerShip() throws Exception;
    
    /**
     * 更新用户和组的关系
     */
    public void updateMembership(String userId, String groupId) throws Exception;
}
