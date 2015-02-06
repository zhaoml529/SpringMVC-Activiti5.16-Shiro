package com.zml.oa.service;

import java.util.List;

import com.zml.oa.entity.GroupAndResource;
import com.zml.oa.entity.Resource;

public interface IResourceService {

	public Resource getPermissions(Integer id) throws Exception;
	
	public List<Resource> getMenus(List<GroupAndResource> gr) throws Exception;
}
