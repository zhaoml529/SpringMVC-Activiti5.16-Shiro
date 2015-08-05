package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.zml.oa.dao.IJdbcDao;
import com.zml.oa.pagination.Pagination;
import com.zml.oa.pagination.PaginationThreadUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml", "classpath*:/springMVC.xml" })  
public class jdbcTest {
	
	@Autowired
	private IJdbcDao jdbcDao;
	
	@Test
	public void testJdbc(){
		Pagination p = new Pagination();
		p.setCurrentPage(1);
		p.setPageNum(3);
		PaginationThreadUtils.set(p);
//		String sql = "select * from t_resource where type = :type";
		String sql = "select g.name as g_name, r.name as r_name, r.type "
					+ "from t_group_resource gr "
					+ "INNER JOIN t_group g on gr.group_id = g.GROUP_ID "
					+ "INNER JOIN t_resource r on gr.resource_id = r.id "
					+ "where g.GROUP_ID = :group_id ";
		Map<String, Object> paramMap = new HashMap<String, Object>();  
	    paramMap.put("group_id", 1);  
//	    List<Map<String, Object>> list = this.jdbcDao.findAll(sql, paramMap);
	    List<Map<String, Object>> list = this.jdbcDao.find(sql, paramMap);
		for(Map<String, Object> map : list){
//			System.out.println(map);
			System.out.println(map.get("G_NAME"));
		}
		
		String sql2 = "update T_SALARY set BASE_MONEY=:money where USER_ID=:userId ";
		Map<String, Object> paramMap2 = new HashMap<String, Object>();  
	    paramMap.put("money", 6666);  
	    paramMap.put("userId", 12);  
		this.jdbcDao.saveOrUpdate(sql2, paramMap2);
	    
		String sql3 = "insert into T_SALARY (id, base_money, user_id) values (?, ?, ?)";
		final int count = 2000;    
	    final List<String> money = new ArrayList<String>(count);    
	    final List<String> userIds = new ArrayList<String>(count);    
	    for (int i = 0; i < count; i++) {    
	    	money.add("First Name " + i);    
	    	userIds.add("Last Name " + i);    
	    }  
		
	}
}
