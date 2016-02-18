package com.zml.oa.service;

import java.util.List;

import com.zml.oa.entity.SuperviseReceive;
import com.zml.oa.pagination.Page;

public interface ISuperviseReceiveService {

	public List<SuperviseReceive> getListPage(Page<SuperviseReceive> page) throws Exception;
}
