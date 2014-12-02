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
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zml.oa.dao.IBaseDao;
import com.zml.oa.pagination.Pagination;
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
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public List<T> getAllList(String tableSimpleName) throws Exception{
		StringBuffer sff = new StringBuffer();  
        sff.append("select a from ").append(tableSimpleName).append(" a ");  
        List<T> list = this.baseDao.createQuery(sff.toString());  
        return list; 
	}

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
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
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
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
            return list.size()>0?list:null;  
        }else{  
            return null;  
        } 
	}

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public List<T> getCount(String tableSimpleName) throws Exception{
    	String hql = "select count(*) from " + tableSimpleName;
    	List<T> list = this.baseDao.createQuery(hql);
    	return list;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Serializable add(T bean) throws Exception{
		return this.baseDao.add(bean);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void saveOrUpdate(T bean) throws Exception{
		this.baseDao.saveOrUpdate(bean);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void delete(T bean) throws Exception{
		this.baseDao.delete(bean);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void update(T bean) throws Exception{
		this.baseDao.update(bean);
	}

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public T getBean(Class<T> obj, Serializable id) throws Exception{
		return this.baseDao.getBean(obj, id);
	}

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public List<T> findByPage(String tableSimpleName, String[] columns, String[] values) throws Exception{
		Pagination pagination = PaginationThreadUtils.get();
		if (pagination == null) {
			pagination = new Pagination();
			PaginationThreadUtils.set(pagination);
			pagination.setCurrentPage(1);
		}
		if (pagination.getTotalSum() == 0) {
			List<T> list = new ArrayList<T>();
			if(columns.length <= 0 && values.length <= 0){
				list = getAllList(tableSimpleName);
			}else{
				list = findByWhere(tableSimpleName, columns, values);
			}
			if(BeanUtils.isBlank(list)){
				pagination.setTotalSum(0);
			}else{
				pagination.setTotalSum(list.size());
			}
		}
		
		int firstResult = (pagination.getCurrentPage() - 1) * pagination.getPageNum();
		int maxResult = pagination.getPageNum();
		/* 校验分页情况 */
		if (firstResult >= pagination.getTotalSum() || firstResult < 0) {
			firstResult = 0;
			pagination.setCurrentPage(1);
		}

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
	       List<T> list = this.baseDao.findByPage(hql, firstResult, maxResult); 
	       return list.size()>0?list:null;
        }else{
        	logger.info("findByPage: columns.length != values.length");
        	return null;
        }
	}

}
