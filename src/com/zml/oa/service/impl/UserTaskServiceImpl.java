package com.zml.oa.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.entity.UserTask;
import com.zml.oa.service.IBaseService;
import com.zml.oa.service.IUserTaskService;

@Service
public class UserTaskServiceImpl implements IUserTaskService {

	@Autowired 
	private IBaseService<UserTask> baseService;
	
	@Override
	public Serializable doAdd(UserTask userTask) throws Exception {
		return baseService.add(userTask);
	}

	@Override
	public void doUpdate(UserTask userTask) throws Exception {
		this.baseService.update(userTask);
	}

	@Override
	public void doDelete(UserTask userTask) throws Exception {
		this.baseService.delete(userTask);
	}

	@Override
	public List<UserTask> toList(String procDefKey) throws Exception {
		List<UserTask> list = this.baseService.findByPage("UserTask", new String[]{"procDefKey"}, new String[]{procDefKey});
		return list;
	}

	@Override
	public UserTask findById(String procDefId) throws Exception {
		return this.baseService.getBean(UserTask.class, procDefId);
	}

}
