/**
 * Project Name:SpringOA
 * File Name:IUserService.java
 * Package Name:com.zml.oa.service
 * Date:2014-11-9上午12:39:07
 *
 */
package com.zml.oa.service;

import java.util.List;

import com.zml.oa.entity.User;

/**
 * @ClassName: IUserService
 * @Description:User接口
 * @author: zml
 * @date: 2014-11-9 上午12:39:07
 *
 */
public interface IUserService extends IBaseService<User>{

	public List<User> getUserList_page() throws Exception;
	
	public User getUserByName(String user_name) throws Exception;

	public User getUserById(Integer id) throws Exception;
}
