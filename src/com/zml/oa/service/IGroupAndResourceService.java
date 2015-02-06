package com.zml.oa.service;

import java.util.List;

import com.zml.oa.entity.GroupAndResource;

public interface IGroupAndResourceService {

	public List<GroupAndResource> getResource(Integer groupId) throws Exception;
}

