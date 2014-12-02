package com.zml.oa.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zml.oa.entity.Group;
import com.zml.oa.service.IGroupService;


@Service
public class GroupServiceImpl extends BaseServiceImpl<Group> implements IGroupService {

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public List<Group> getGroupList() throws Exception{
		List<Group> list = getAllList("Group");
		return list;
	}

}
