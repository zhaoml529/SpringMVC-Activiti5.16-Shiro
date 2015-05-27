/**
 * Project Name:SpringOA
 * File Name:BaseServiceImpl.java
 * Package Name:com.zml.oa.service.impl
 * Date:2014-11-9下午5:42:11
 *
 */
package com.zml.oa.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zml.oa.dao.IBaseDao;
import com.zml.oa.pagination.Page;
import com.zml.oa.pagination.PaginationThreadUtils;
import com.zml.oa.service.IBaseService;
import com.zml.oa.util.BeanUtils;

/**
 * @ClassName: BaseServiceImpl
 * @Description: BaseServiceImpl
 * @author: zml
 * @date: 2014-11-9 下午5:42:11
 *
 */
@Service
public class BaseServiceImpl<T> implements IBaseService<T> {
	private static final Logger logger = Logger.getLogger(BaseServiceImpl.class);
	
    @Autowired  
    private IBaseDao<T> baseDao;  
    
    
	public IBaseDao<T> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public List<T> getAllList(String tableSimpleName) throws Exception{
		StringBuffer sff = new StringBuffer();  
        sff.append("select a from ").append(tableSimpleName).append(" a ");  
        List<T> list = this.baseDao.createQuery(sff.toString());  
        if( list.size()>0 ){
      	   return list;
         }else{
      	   return Collections.emptyList();
         }
	}

	@Override
	public T getUnique(String tableSimpleName, String[] columns, String[] values) throws Exception{
		StringBuffer sb = new StringBuffer();  
        sb.append("select a from ").append(tableSimpleName).append( " a where ");  
        if(columns.length==values.length){  
            for(int i = 0; i < columns.length; i++){  
                sb.append("a.").append(columns[i]).append("='").append(values[i]).append("'");  
                if(i < columns.length-1){  
                    sb.append(" and ");  
                }  
           }  
           T entity = this.baseDao.unique(sb.toString());  
           return entity; 
        }else{  
           logger.error("columns.length != values.length");
           return null;  
        } 
	}
	
	@Override
	public List<T> findByWhere(String tableSimpleName, String[] columns,
			String[] values) throws Exception{
		StringBuffer sb = new StringBuffer();  
        sb.append("select a from ").append(tableSimpleName).append( " a where ");  
        if(columns.length==values.length){  
            for(int i = 0; i < columns.length; i++){  
                sb.append("a.").append(columns[i]).append("='").append(values[i]).append("'");  
                if(i < columns.length-1){  
                    sb.append(" and ");  
                }  
           }  
           List<T> list = this.baseDao.createQuery(sb.toString());  
//         最好用JDK提供的Collections.emptyList()来返回一个空的集合对象 而不是 null
//         Collections.EMPTY_LIST 是返回一个空集合对象，emptyList()是对EMPTY_LIST做了一个泛型支持，具体看源码
//         Collections.EMPTY_LIST的返回值是一个不可变的空List，不能进行例如add的各种操作！
           if( list.size()>0 ){
        	   return list;
           }else{
        	   return Collections.emptyList();
           }
        }else{  
        	return Collections.emptyList();  
        } 
	}

	@Override
	public List<T> getCount(String tableSimpleName) throws Exception{
    	String hql = "select count(*) from " + tableSimpleName;
    	List<T> list = this.baseDao.createQuery(hql);
    	return list;
	}

	@Override
	public Serializable add(T bean) throws Exception{
		return this.baseDao.add(bean);
	}

	@Override
	public void saveOrUpdate(T bean) throws Exception{
		this.baseDao.saveOrUpdate(bean);
	}

	@Override
	public void delete(T bean) throws Exception{
		this.baseDao.delete(bean);
	}

	@Override
	public void update(T bean) throws Exception{
		this.baseDao.update(bean);
	}

	@Override
	public T getBean(Class<T> obj, Serializable id) throws Exception{
		return this.baseDao.getBean(obj, id);
	}

	@Override
	public List<T> findByPage(String tableSimpleName, String[] columns, String[] values) throws Exception{
		Integer totalSum = 0;
		List<T> list = new ArrayList<T>();
		if(columns.length <= 0 && values.length <= 0){
			list = getAllList(tableSimpleName);
		}else{
			list = findByWhere(tableSimpleName, columns, values);
		}
		if(!BeanUtils.isBlank(list)){
			totalSum = list.size();
		}
		int[] pageParams = PaginationThreadUtils.setPage(totalSum);
		
		StringBuffer sb = new StringBuffer();  
        sb.append("select a from ").append(tableSimpleName).append( " a where ");  
        if(columns.length==values.length){  
            for(int i = 0; i < columns.length; i++){  
                sb.append("a.").append(columns[i]).append("='").append(values[i]).append("'");  
                if(i < columns.length-1){  
                    sb.append(" and ");  
                }  
           } 
	       String hql = sb.toString();
	       if(hql.endsWith("where ")){
	    	   hql = hql.substring(0, hql.length()-6);
	       }
	       logger.info("findByPage: HQL: "+hql);
	       list = this.baseDao.findByPage(hql, pageParams[0], pageParams[1]); 
	       if( list.size()>0 ){
        	   return list;
           }else{
        	   return Collections.emptyList();
           }
        }else{
        	logger.info("findByPage: columns.length != values.length");
        	return Collections.emptyList();
        }
	}

	@Override
	public List<T> getListPage(String tableSimpleName, String[] columns,
			String[] values, Page<T> page) throws Exception {
		Integer totalSum = 0;
		List<T> list = new ArrayList<T>();
		if(columns.length <= 0 && values.length <= 0){
			list = getAllList(tableSimpleName);
		}else{
			list = findByWhere(tableSimpleName, columns, values);
		}
		if(!BeanUtils.isBlank(list)){
			totalSum = list.size();
		}
		int[] pageParams = page.getPageParams(totalSum);
		
		StringBuffer sb = new StringBuffer();  
        sb.append("select a from ").append(tableSimpleName).append( " a where ");  
        if(columns.length==values.length){  
            for(int i = 0; i < columns.length; i++){  
                sb.append("a.").append(columns[i]).append("='").append(values[i]).append("'");  
                if(i < columns.length-1){  
                    sb.append(" and ");  
                }  
            } 
	        String hql = sb.toString();
	        if(hql.endsWith("where ")){
	    	    hql = hql.substring(0, hql.length()-6);
	        }
	        logger.info("findByPage: HQL: "+hql);
	        list = this.baseDao.findByPage(hql, pageParams[0], pageParams[1]); 
	        if( list.size()>0 ){
	        	page.setResult(list);
        	    return list;
            }else{
        	    return Collections.emptyList();
            }
        }else{
        	logger.info("findByPage: columns.length != values.length");
        	return Collections.emptyList();
        }
	}
	
}
