package com.zml.oa.util;

/**
 * 
 * @ClassName: StringUtils
 * @Description:TODO(String工具类)
 * @author: zml
 * @date: 2014-4-18 上午10:19:58
 *
 */
public class StringUtils {
	public static boolean isBlank(String...judgeString){
		
		for(String str:judgeString){
			if(str == null || "".equals(str)){
				return true;
			}
		}
		return false;
	}
}
