/**
 * Project Name:SpringOA
 * File Name:UserAction.java
 * Package Name:com.zml.oa.action
 * Date:2014-11-9上午12:35:50
 *
 */
package com.zml.oa.action;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zml.oa.entity.Group;
import com.zml.oa.entity.User;
import com.zml.oa.pagination.Pagination;
import com.zml.oa.pagination.PaginationThreadUtils;
import com.zml.oa.service.IGroupService;
import com.zml.oa.service.IUserService;
import com.zml.oa.util.BeanUtils;
import com.zml.oa.util.UserUtil;

/**
 * @ClassName: UserAction
 * @Description:用户Controller
 * @author: zml
 * @date: 2014-11-9 上午12:35:50
 *
 */

@Controller
@RequestMapping("/userAction")
public class UserAction {
	private static final Logger logger = Logger.getLogger(UserAction.class);
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IGroupService groupService;
	
	@RequestMapping("/toList_page")
	public String userList_page(HttpServletRequest request, Model model) throws Exception{
		List<User> listUser = this.userService.getUserList_page();
		Pagination pagination = PaginationThreadUtils.get();
//		pagination.processTotalPage();
		model.addAttribute("page", pagination.getPageStr());
		model.addAttribute("listUser", listUser);
		return "user/list_user";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("name")String name, @RequestParam("passwd")String passwd, HttpServletRequest request, Model model) throws Exception{
		logger.info("login - username=" + name + ", password=" + passwd);
		HttpSession session = request.getSession();
		User user = userService.getUserByName(name);
		if(!BeanUtils.isBlank(user)){
			if(passwd.equals(user.getPasswd())){
				UserUtil.saveUserToSession(session, user);
				return "index";
			}else{
				model.addAttribute("msg", "密码不正确");
				logger.info("密码不正确");
				return "login";
			}
		}else{
			model.addAttribute("msg", "用户名不存在");
			logger.info(name+" 用户名不存在");
			return "login";
		}
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		UserUtil.removeUserFromSession(session);
		return "login";
	}
	
	@RequestMapping("/login_view")
	public String login_view(){
		logger.info("跳转登录页面");
		return "login";
	}

	
	@RequestMapping(value = "/toAdd", method = RequestMethod.GET)
	public String toAdd(Model model) throws Exception{
		List<Group> list = this.groupService.getGroupList();
		model.addAttribute("groupList", list);
		return "user/add_user";
	}

	
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	public String doAdd(@ModelAttribute("user") User user) throws Exception{
		user.setRegisterDate(new Date());
		Serializable id = this.userService.add(user);
		logger.info("Serializable id: "+id);
		return "redirect:/userAction/toList_page";
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Integer id) throws Exception{
		User user = new User();
		user.setId(id);
		this.userService.delete(user);
		return "redirect:/userAction/toList_page";
	}
}
