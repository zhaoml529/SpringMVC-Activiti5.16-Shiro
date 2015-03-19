package com.zml.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.entity.GroupAndResource;
import com.zml.oa.service.IBaseService;
import com.zml.oa.service.IGroupAndResourceService;

@Service
public class GroupAndResourceServiceImpl extends
		BaseServiceImpl<GroupAndResource> implements IGroupAndResourceService {
	@Autowired 
	private IBaseService<GroupAndResource> baseService;
	
	@Override
	public List<GroupAndResource> getResource(Integer groupId) throws Exception {
		List<GroupAndResource> list = this.baseService.findByWhere("GroupAndResource", new String[]{"groupId"}, new String[]{groupId.toString()});
		return list;
	}

	@Override
	public void doAdd(GroupAndResource gar) throws Exception {
		this.baseService.add(gar);
	}

	@Override
	public void doDelete(GroupAndResource gar) throws Exception {
		this.baseService.delete(gar);
	}

}
