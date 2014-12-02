package com.zml.oa.service;

import java.util.List;

import com.zml.oa.entity.Group;

/**
 * @ClassName: IGroupService
 * @Description:Group接口
 * @author: zml
 * @date: 2014-11-27 下午15:05:07
 *
 */
public interface IGroupService extends IBaseService<Group> {

	public List<Group> getGroupList() throws Exception;
	
}
