/*
 * Copyright (c) 2012-2032 Accounting Center of China Aviation(ACCA).
 * All Rights Reserved.
 */
package com.zml.oa.filter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;

import com.zml.oa.service.IUserService;


/**
 * 
 * 
 * @version EBAS v1.0
 * @author zhouhua, 2014-9-3
 */
public class SysUserFilter extends PathMatchingFilter {
    
    @Resource(name="userService")
    private IUserService userService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response,
                                  Object mappedValue) throws Exception {

        String username = (String) SecurityUtils.getSubject().getPrincipal();
//        request.setAttribute("user", userService.findByUsername(username));
        return true;
    }
}
