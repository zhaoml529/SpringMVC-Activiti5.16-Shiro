package com.zml.oa.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.entity.Group;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IBaseService;
import com.zml.oa.service.IGroupService;


@Service
public class GroupServiceImpl implements IGroupService {

	@Autowired 
	private IBaseService<Group> baseService;
	
	@Override
	public List<Group> getGroupListPage(Page<Group> page) throws Exception{
		List<Group> list = this.baseService.getListPage("Group", new String[]{}, new String[]{}, page);
		return list;
	}

	@Override
	public List<Group> getGroupListPage() throws Exception {
		List<Group> list = this.baseService.findByPage("Group", new String[]{}, new String[]{});
		return list;
	}

	@Override
	public Serializable doAdd(Group group) throws Exception {
		return this.baseService.add(group);
	}

	@Override
	public void doUpdate(Group group) throws Exception {
		this.baseService.update(group);
	}

	@Override
	public void doDelete(Group group) throws Exception {
		this.baseService.delete(group);
	}

	@Override
	public List<Group> getGroupList() throws Exception {
		List<Group> list = this.baseService.getAllList("Group");
		return list;
	}

	@Override
	public Group getGroupById(String id) throws Exception {
		return this.baseService.getUnique("Group", new String[]{"id"}, new String[]{id});
	}

}
