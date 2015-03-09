package com.zml.oa.service;

import java.io.Serializable;
import java.util.List;

import com.zml.oa.entity.UserTask;

public interface IUserTaskService {

	public Serializable doAdd(UserTask userTask) throws Exception;
	
	public void doUpdate(UserTask userTask) throws Exception;
	
	public void doDelete(UserTask userTask) throws Exception;
	
	public List<UserTask> toList(String procDefKey) throws Exception;
	
	public Integer deleteAll() throws Exception; 
	
	public UserTask findById(Integer id) throws Exception;
	
	public List<UserTask> findByWhere(String procDefKey) throws Exception;
	
	public List<UserTask> getAll() throws Exception;
	
}
