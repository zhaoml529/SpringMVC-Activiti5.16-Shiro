/**
 * Project Name:SpringOA
 * File Name:UserServiceImpl.java
 * Package Name:com.zml.oa.service.impl
 * Date:2014-11-9上午12:53:20
 *
 */
package com.zml.oa.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private PasswordHelper passwordHelper;

	@Override
	public User getUserByName(String user_name) throws Exception{
		User user = getUnique("User", new String[]{"name"}, new String[] {user_name});
		if(BeanUtils.isBlank(user)){
			return null;
		}else{
			return user;
		}
	}

	@Override
	public List<User> getUserList_page() throws Exception{
		List<User> list = findByPage("User", new String[]{}, new String[]{});
		return list;
	}

	@Override
	public User getUserById(Integer id) throws Exception {
		return getUnique("User", new String[]{"id"}, new String[]{id.toString()});
	}

	@Override
	public Serializable doAdd(User user) throws Exception {
        //加密密码
        passwordHelper.encryptPassword(user);
		return add(user);
	}

	@Override
	public void doUpdate(User user) throws Exception {
		//pwd 为修改后的
		passwordHelper.encryptPassword(user);
		update(user);
	}

	@Override
	public void doDelete(User user) throws Exception {
		delete(user);
	}
	

}
