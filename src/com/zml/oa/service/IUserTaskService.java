package com.zml.oa.service;

import java.io.Serializable;
import java.util.List;

import com.zml.oa.entity.UserTask;

public interface IUserTaskService {

	public Serializable doAdd(UserTask userTask) throws Exception;
	
	public void doUpdate(UserTask userTask) throws Exception;
	
	public void doDelete(UserTask userTask) throws Exception;
	
	public List<UserTask> toList(String procDefKey) throws Exception;
	
	public UserTask findById(String procDefId) throws Exception;
}
