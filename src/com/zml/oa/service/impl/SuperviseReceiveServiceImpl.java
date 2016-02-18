package com.zml.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.entity.SuperviseReceive;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IBaseService;
import com.zml.oa.service.ISuperviseReceiveService;

@Service
public class SuperviseReceiveServiceImpl implements ISuperviseReceiveService {
	
	@Autowired 
	private IBaseService<SuperviseReceive> baseService;

	@Override
	public List<SuperviseReceive> getListPage(Page<SuperviseReceive> page) throws Exception {
		return this.baseService.getListPage("SuperviseReceive", new String[]{}, new String[]{}, page);
	}

}
