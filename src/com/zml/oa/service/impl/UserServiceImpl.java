/**
 * Project Name:SpringOA
 * File Name:UserServiceImpl.java
 * Package Name:com.zml.oa.service.impl
 * Date:2014-11-9上午12:53:20
 *
 */
package com.zml.oa.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zml.oa.entity.User;
import com.zml.oa.service.IUserService;
import com.zml.oa.util.BeanUtils;

/**
 * @ClassName: UserServiceImpl
 * @Description:user实现类，开启事务
 * @author: zml
 * @date: 2014-11-9 上午12:53:20
 *
 */

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {


	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public User getUserByName(String user_name) throws Exception{
		User user = getUnique("User", new String[]{"name"}, new String[] {user_name});
		if(BeanUtils.isBlank(user)){
			return null;
		}else{
			return user;
		}
	}

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public List<User> getUserList_page() throws Exception{
		List<User> list = findByPage("User", new String[]{}, new String[]{});
		return list;
	}

	@Override
	public User getUserById(Integer id) throws Exception {
		return getUnique("User", new String[]{"id"}, new String[]{id.toString()});
	}
	

}
