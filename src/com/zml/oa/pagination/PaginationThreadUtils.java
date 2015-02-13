package com.zml.oa.pagination;

import org.apache.log4j.Logger;

public class PaginationThreadUtils {
	private static final Logger logger = Logger.getLogger(PaginationThreadUtils.class);
	private static final ThreadLocal<Pagination> pagination = new ThreadLocal<Pagination>();

	public static Pagination get() {
		return pagination.get();
	}

	public static void set(Pagination value) {
		pagination.set(value);
	}

	/**
	 * @功能: 清空线程对象
	 * @作者: zml
	 * @创建日期: 2014-11-18 14:19:43
	 */
	public static void clear() {
		pagination.remove();
	}

	/**
	 * 计算分页
	 * @param totalSum
	 * @return
	 */
	public static int[] setPage(Integer totalSum) {
		Pagination pagination = PaginationThreadUtils.get();
		if (pagination == null) {
			pagination = new Pagination();
			PaginationThreadUtils.set(pagination);
			pagination.setCurrentPage(1);
		}
		
		if (pagination.getTotalSum() == 0) {
			pagination.setTotalSum(totalSum);
		}
		int firstResult = (pagination.getCurrentPage() - 1) * pagination.getPageNum();
		int maxResult = pagination.getPageNum();
		//校验分页情况
		if (firstResult >= pagination.getTotalSum() || firstResult < 0) {
			firstResult = 0;
			pagination.setCurrentPage(1);
		}
		pagination.setFirstResult(firstResult);
		pagination.setMaxResult(maxResult);
		//分页处理
		pagination.processTotalPage();
		return new int[]{firstResult, maxResult};
	}
}
