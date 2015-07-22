package com.zml.oa.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zml.oa.entity.User;
import com.zml.oa.util.BeanUtils;
import com.zml.oa.util.UserUtil;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Session session = SecurityUtils.getSubject().getSession();
		User user = UserUtil.getUserFromSession(session);
		if(!BeanUtils.isBlank(user)){
			return true;
		}
		response.sendRedirect(request.getContextPath() + "/userAction/login_view");
		return false;
	}

}
