package com.zml.oa.service.impl;

import java.beans.Beans;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zml.oa.entity.GroupAndResource;
import com.zml.oa.entity.Resource;
import com.zml.oa.service.IResourceService;
import com.zml.oa.util.BeanUtils;

@Service
public class ResourceServiceImpl extends BaseServiceImpl<Resource> implements
		IResourceService {

	@Override
	public Resource getPermissions(Integer id) throws Exception {
		Resource res = getUnique("Resource", new String[]{"id", "available"}, new String[]{id.toString(), "1"});
		return res;
	}

	@Override
	public List<Resource> getMenus(List<GroupAndResource> gr) throws Exception {
		List<Resource> menus = new ArrayList<Resource>();
		for(GroupAndResource gar : gr){
			Resource resource= getPermissions(gar.getResourceId());
			if(!BeanUtils.isBlank(resource)){
				if(resource.isRootNode()) {
	                continue;
	            }
	            if(!"menu".equals(resource.getType())) {
	                continue;
	            }
	            menus.add(resource);
			}
		}
		return menus;
	}

}
