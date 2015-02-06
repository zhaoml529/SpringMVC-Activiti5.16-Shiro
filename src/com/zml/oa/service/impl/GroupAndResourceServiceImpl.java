package com.zml.oa.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zml.oa.entity.GroupAndResource;
import com.zml.oa.service.IGroupAndResourceService;

@Service
public class GroupAndResourceServiceImpl extends
		BaseServiceImpl<GroupAndResource> implements IGroupAndResourceService {

	@Override
	public List<GroupAndResource> getResource(Integer groupId) throws Exception {
		List<GroupAndResource> list = findByWhere("GroupAndResource", new String[]{"groupId"}, new String[]{groupId.toString()});
		if (list == null) {
			return Collections.emptyList();
		}else{
			return list;
		}
	}

}
