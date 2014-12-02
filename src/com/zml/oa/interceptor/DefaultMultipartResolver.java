package com.zml.oa.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 解决springMVC与kindEditor文件上传时的问题
 * org.springframework.web.multipart.commons.CommonsMultipartResolver是spring默认提供的,
 * 它具有一个public boolean isMultipart(HttpServletRequest request) 接口,
 * 这个接口用于判断哪些request用spring的CommonsMultipartResolver处理.
 * 于是,自己写一个CommonsMultipartResolver的子类,覆盖isMultipart方法,在spring中重新配置.
 * 这样的话就特定的请求就可以绕过spring框架中的解决方案,
 * @author Administrator
 *
 */
public class DefaultMultipartResolver extends CommonsMultipartResolver {
	//com.rjxy.action.KindEditorController方法中的保存路径
	private static final String ATTACHED = "file_upload";
    @Override
    public boolean isMultipart(HttpServletRequest request) {
        if (request.getRequestURI().contains(ATTACHED)) {
            return false;
        }
        return super.isMultipart(request);
    }
}
