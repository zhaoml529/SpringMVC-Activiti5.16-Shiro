/**
 * Project Name:SpringOA
 * File Name:IUserService.java
 * Package Name:com.zml.oa.service
 * Date:2014-11-9上午12:39:07
 *
 */
package com.zml.oa.service;

import java.io.Serializable;
import java.util.List;

import com.zml.oa.entity.User;
import com.zml.oa.pagination.Page;

/**
 * @ClassName: IUserService
 * @Description:User接口
 * @author: zml
 * @date: 2014-11-9 上午12:39:07
 *
 */
public interface IUserService {

	public List<User> getUserList_page() throws Exception;

	public List<User> getUserList(Page<User> page) throws Exception;
	
	public User getUserByName(String user_name) throws Exception;

	public User getUserById(Integer id) throws Exception;
	
	public List<User> getUserByGroupId(String groupId, Page<User> page) throws Exception;
	
	public void doUpdate(User user) throws Exception;
	
	/**
     * 添加用户并[同步其他数据库]
     * <ul>
     * <li>step 1: 保存系统用户，同时设置和部门的关系</li>
     * <li>step 2: 同步用户信息到activiti的identity.User，同时设置角色</li>
     * </ul>
     * 
     * @param user              用户对象
     * @param groupId           部门ID
     * @param synToActiviti     是否同步到Activiti数据库，通过配置文件方式设置，使用属性：account.user.add.syntoactiviti
     * @throws  Exception                       其他未知异常
     */
    public Serializable doAdd(User user, String groupId, boolean synToActiviti) throws Exception;
        
    /**
     * 删除用户
     * @param userId        用户ID
     * @param synToActiviti     是否同步到Activiti数据库，通过配置文件方式设置，使用属性：account.user.add.syntoactiviti
     * @throws Exception
     */
    public void doDelete(User user, boolean synToActiviti) throws Exception;
    
    /**
     * 同步用户、角色数据到工作流
     * @throws Exception
     */
    public void synAllUserAndRoleToActiviti() throws Exception;
 
    /**
     * 删除工作流引擎Activiti的用户、角色以及关系
     * @throws Exception
     */
    public void deleteAllActivitiIdentifyData() throws Exception;
}


