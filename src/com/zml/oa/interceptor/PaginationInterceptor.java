package com.zml.oa.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zml.oa.pagination.Pagination;
import com.zml.oa.pagination.PaginationThreadUtils;
import com.zml.oa.util.StringUtils;

public class PaginationInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger(PaginationInterceptor.class);

	// 前端js对分页请求的名字
	private static final String PAGE_NUM = "pageNum";
	private static final String CURRENT_PAGE = "currentPage";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		if (uri.endsWith("_page")) {
			Pagination pagination = PaginationThreadUtils.get();
			if (pagination == null) {
				pagination = new Pagination();
				PaginationThreadUtils.set(pagination);
			}
			Map<String, String[]> params = request.getParameterMap();
			// 设置要跳转到的页数
			if (params.get(CURRENT_PAGE) == null) {
				pagination.setCurrentPage(1);
			} else {
				String pageNow = params.get(CURRENT_PAGE)[0];
				if (StringUtils.isBlank(pageNow)) {
					pagination.setCurrentPage(1);
				} else {
					pagination.setCurrentPage(Integer.parseInt(pageNow));
				}
			}
			// 设置每页的行数
			if (params.get(PAGE_NUM) != null) {
				String pageSize = params.get(PAGE_NUM)[0];
				if (!StringUtils.isBlank(pageSize)) {
					pagination.setPageNum(Integer.parseInt(pageSize));
				}
			}
			logger.info("PaginationInterceptor - CURRENT_PAGE=" + pagination.getCurrentPage() + ", PAGE_NUM=" + pagination.getPageNum()+", uri="+uri+", params="+params);
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		logger.info("清除PaginationThreadUtils--");
		PaginationThreadUtils.clear();
	}

}
