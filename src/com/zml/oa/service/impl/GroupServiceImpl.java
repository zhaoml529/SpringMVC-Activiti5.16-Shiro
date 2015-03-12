package com.zml.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.entity.Group;
import com.zml.oa.service.IBaseService;
import com.zml.oa.service.IGroupService;


@Service
public class GroupServiceImpl implements IGroupService {

	@Autowired 
	private IBaseService<Group> baseService;
	
	@Override
	public List<Group> getGroupList() throws Exception{
		List<Group> list = this.baseService.getAllList("Group");
		return list;
	}

	@Override
	public List<Group> getGroupListPage() throws Exception {
		List<Group> list = this.baseService.findByPage("Group", new String[]{}, new String[]{});
		return list;
	}

}
