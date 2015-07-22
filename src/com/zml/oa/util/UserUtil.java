package com.zml.oa.util;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.zml.oa.entity.User;

/**
 * 用户工具类
 *
 * @author ZML
 */
public class UserUtil {

    /**
     * 设置用户到session
     *
     * @param session
     * @param user
     */
    public static void saveUserToSession(Session session, User user) {
        session.setAttribute(Constants.CURRENT_USER, user);
    }

    /**
     * 从Shiro 的Session获取当前用户信息
     *
     * @param session
     * @return
     */
    public static User getUserFromSession(Session session) {
//        Object attribute = session.getAttribute(Constants.CURRENT_USER);
    	Subject currentUser = SecurityUtils.getSubject();
		User use = (User) currentUser.getSession().getAttribute(Constants.CURRENT_USER);
        return use == null ? null : use;
    }

    /**
     * 从Session移除当前用户信息
     * @param session
     */
    public static void removeUserFromSession(Session session) {
    	session.removeAttribute(Constants.CURRENT_USER);
    }
    
}
