package com.zml.oa.pagination;

public class PaginationThreadUtils {

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

}
