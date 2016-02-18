/**
 * Project Name:SpringOA
 * File Name:UserAction.java
 * Package Name:com.zml.oa.action
 * Date:2014-11-9上午12:35:50
 *
 */
package com.zml.oa.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zml.oa.entity.Datagrid;
import com.zml.oa.entity.Group;
import com.zml.oa.entity.Message;
import com.zml.oa.entity.User;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IGroupService;
import com.zml.oa.service.IUserService;
import com.zml.oa.shiro.realm.UserRealm;
import com.zml.oa.util.BeanUtils;
import com.zml.oa.util.Constants;
import com.zml.oa.util.DateUtil;
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
	@Autowired
    private SessionDAO sessionDAO;
	
//	@RequiresPermissions("admin:*")
//	@RequestMapping("/toList_page")
//	public String userList_page(HttpServletRequest request, Model model) throws Exception{
//		List<User> listUser = this.userService.getUserList_page();
//		Pagination pagination = PaginationThreadUtils.get();
////		pagination.processTotalPage();
//		model.addAttribute("page", pagination.getPageStr());
//		model.addAttribute("listUser", listUser);
//		return "user/list_user";
//	}
	
	@RequiresPermissions("admin:*")
	@RequestMapping(value = "/userDatagrid")
	public String toList_page() throws Exception{
		return "user/list_user";
	}
	
	/**
	 * 
	 * @param page 当前第几页
	 * @param rows 每页显示记录数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toList")
	@ResponseBody
	public Datagrid<Object> userList(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) throws Exception{
		Page<User> p = new Page<User>(page, rows);
		List<User> userList = this.userService.getUserList(p);
//		Datagrid<User> dataGrid = new Datagrid<User>(p.getTotal(), p.getResult());
		List<Object> jsonList=new ArrayList<Object>(); 
		for(User user : userList){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", user.getId());
			map.put("name", user.getName());
			map.put("passwd", user.getPasswd());
			map.put("registerDate", user.getRegisterDate());
			map.put("locked", user.getLocked());
			map.put("group_name", user.getGroup().getName());
			jsonList.add(map);
		}
		return new Datagrid<Object>(p.getTotal(), jsonList);
	}
	
	//此方法没用到，用Shiro提供的授权和认证服务
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("name")String name, @RequestParam("passwd")String passwd, HttpServletRequest request, Model model) throws Exception{
		logger.info("login - username=" + name + ", password=" + passwd);
		Session session = SecurityUtils.getSubject().getSession();
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

	@RequiresPermissions("admin:user:toAdd")
	@RequestMapping(value = "/toAdd")
	public String toAdd() throws Exception{
//		List<Group> list = this.groupService.getGroupList();
//		model.addAttribute("groupList", list);
		return "user/add_user";
	}

	@RequiresPermissions("admin:user:doAdd")
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	@ResponseBody
	public Message doAdd(@ModelAttribute("user") User user,
						@Value("#{APP_PROPERTIES['account.user.add.syntoactiviti']}") Boolean synToActiviti) throws Exception{
		user.setRegisterDate(new Date());
		//user.setLocked(0);
		String groupId = user.getGroup().getId().toString();
		this.userService.doAdd(user, groupId, synToActiviti);
		return new Message(Boolean.TRUE, "添加成功！");
		
	}
	
	@RequiresPermissions("admin:user:toUpdate")
	@RequestMapping(value = "/toUpdate/{id}")
	public String toUpdate(@PathVariable("id") Integer id,Model model) throws Exception{
//		List<Group> list = this.groupService.getGroupList();
		User user = this.userService.getUserById(id);
//		model.addAttribute("groupList", list);
		model.addAttribute("user", user);
		return "user/update_user";
	}
	
	@RequiresPermissions("admin:user:doUpdate")
    @RequestMapping(value = "/doUpdate")
    @ResponseBody
	public Message doUpdate(HttpServletRequest request) throws Exception{
		String id = request.getParameter("id");
		String salt = request.getParameter("salt");
		String name = request.getParameter("name");
		String registerDate = request.getParameter("registerDate");
		String passwd = request.getParameter("passwd");
		String groupId = request.getParameter("group.id");
		String locked = request.getParameter("locked");
		Message message = new Message();
		User user = new User();
		if(StringUtils.isNotEmpty(id)){
			user.setId(new Integer(id));
			user.setName(name);
			user.setSalt(salt);
			user.setPasswd(passwd);
			user.setLocked(new Integer(locked));
			if(StringUtils.isNotEmpty(groupId)){
				user.setGroup(new Group(new Integer(groupId)));
			}else{
				message.setStatus(Boolean.FALSE);
				message.setMessage("group.id 为空！");
			}
			Date date = DateUtil.StringToDate(registerDate, "yyyy-MM-dd HH:mm");
			user.setRegisterDate(date);
		}else{
			message.setStatus(Boolean.FALSE);
			message.setMessage("userId 为空！");
		}
		if(message.getStatus()){
			this.userService.doUpdate(user);
			//清空认证缓存
			Subject currentUser = SecurityUtils.getSubject();
			UserRealm ur = new UserRealm();
			ur.clearCachedAuthenticationInfo(currentUser.getPrincipals());
			
			message.setStatus(Boolean.TRUE);
			message.setMessage("修改成功！");
		}
		return message;
	}
	
	
	@RequiresPermissions("admin:user:doDelete")
	@RequestMapping(value = "/delete/{id}")
	@ResponseBody
	public Message delete(@PathVariable("id") Integer id,
						@Value("#{APP_PROPERTIES['account.user.delete.syntoactiviti']}") Boolean synToActiviti) throws Exception{
		if(!BeanUtils.isBlank(id)){
			User user = new User();
			user.setId(id);
			this.userService.doDelete(user, synToActiviti);
			//清空认证和权限缓存
			Subject currentUser = SecurityUtils.getSubject();
			UserRealm ur = new UserRealm();
			ur.clearCache(currentUser.getPrincipals());
			
			return new Message(Boolean.TRUE, "删除成功！");
		}else{
			return new Message(Boolean.FALSE, "删除失败！ID为空！");
		}
	}
	
	@RequiresPermissions("admin:*")
	@RequestMapping(value = "/toListOnlineUser")
	public String toOnlineUser() throws Exception{
		return "user/online_user";
	}
	
	@RequiresPermissions("admin:*")
	@RequestMapping(value = "/listOnlineUser")
	@ResponseBody
    public List<Object> list(Model model) {
        Collection<Session> sessions =  sessionDAO.getActiveSessions();
        List<Object> jsonList=new ArrayList<Object>(); 
        for(Session session : sessions){
        	Map<String, Object> map=new HashMap<String, Object>();
        	PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        	String userName = (String)principalCollection.getPrimaryPrincipal();
        	Boolean forceLogout = session.getAttribute(Constants.SESSION_FORCE_LOGOUT_KEY) != null;
        	map.put("id", session.getId());
        	map.put("userName", userName);
        	map.put("host", session.getHost());
        	map.put("lastAccessTime", session.getLastAccessTime());
        	map.put("forceLogout", forceLogout);
        	jsonList.add(map);
        }
        return jsonList;
    }

	@RequiresPermissions("admin:session:forceLogout")
    @RequestMapping("/forceLogout/{sessionId}")
	@ResponseBody
    public Message forceLogout(@PathVariable("sessionId") String sessionId) {
		Message message = new Message();
        try {
            Session session = sessionDAO.readSession(sessionId);
            if(session != null) {
                session.setAttribute(Constants.SESSION_FORCE_LOGOUT_KEY, Boolean.TRUE);
            }
            message.setStatus(Boolean.TRUE);
            message.setMessage("强制退出成功！");
        } catch (Exception e) {
        	message.setStatus(Boolean.FALSE);
            message.setMessage("强制退出失败！");
        }
        return message;
    }
	
	/**
	 * 同步所有用户到activiti表
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("admin:user:syncUser")
	@RequestMapping("/syncUserToActiviti")
	@ResponseBody
	public Message syncUserToActiviti() throws Exception {
		Message message = new Message();
		try {
			this.userService.synAllUserAndRoleToActiviti();
			message.setStatus(Boolean.TRUE);
            message.setMessage("同步用户信息成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
            message.setMessage("同步用户信息失败！");
		}
		return message;
	}
	
	//如果执行删除，工作流审批中的代办任务和待签收任务将无法使用。（在act_ru_identitylink将查不到act_id_user、act_id_group和act_id_membership的信息）
	@RequiresPermissions("admin:user:delAllIdentifyData")
	@RequestMapping("/delAllIdentifyData")
	public String delAllIdentifyData(RedirectAttributes redirectAttributes) throws Exception {
		this.userService.deleteAllActivitiIdentifyData();
		redirectAttributes.addFlashAttribute("msg", "成功删除工作流引擎Activiti的用户、角色以及关系！");
		return "redirect:/userAction/toList_page";
	}  
	
	
	/**
	 * 跳转选择候选人页面-easyui
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/toChooseUser")
	public ModelAndView toChooseUser(@RequestParam("multiSelect") boolean multiSelect, @RequestParam("taskDefKey") String taskDefKey) throws Exception{
		ModelAndView mv = new ModelAndView("bpmn/choose_user");
		List<Group> groupList = this.groupService.getGroupList();
		mv.addObject("taskDefKey", taskDefKey);
		mv.addObject("multiSelect", multiSelect);
		mv.addObject("groupList", groupList);
		return mv;
	}
	
	/**
	 * 跳转选择任务委派人页面-easyui
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/toChooseDelegateUser")
	public ModelAndView toChooseDelegateUser() throws Exception{
		ModelAndView mv = new ModelAndView("task/delegate_user");
		List<Group> groupList = this.groupService.getGroupList();
		mv.addObject("groupList", groupList);
		return mv;
	}
	
	/**
	 * 在tabs中根据groupId显示用户列表
	 * @return
	 */
	@RequestMapping(value = "/toShowUser")
	public ModelAndView toShowUser(
			@RequestParam("groupId") String groupId,
			@RequestParam("multiSelect") boolean multiSelect, 
			@RequestParam("taskDefKey") String taskDefKey){
		ModelAndView mv = new ModelAndView("bpmn/show_user");
		mv.addObject("groupId", groupId);
		mv.addObject("multiSelect", multiSelect);
		mv.addObject("taskDefKey", taskDefKey);
		return mv;
	}
	
	/**
	 * 在tabs中根据groupId显示用户列表
	 * @return
	 */
	@RequestMapping(value = "/toShowDelegateUser")
	public ModelAndView toShowDelegateUser(
			@RequestParam("groupId") String groupId){
		ModelAndView mv = new ModelAndView("task/show_user");
		mv.addObject("groupId", groupId);
		return mv;
	}
	
	/**
	 * 获取候选人列表 - easyui
	 * @param page
	 * @param rows
	 * @param groupId
	 * @param flag
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/chooseUser")
	@ResponseBody
	public Datagrid<Object> chooseUser(
			@RequestParam(value = "page", required = false) Integer page, 
			@RequestParam(value = "rows", required = false) Integer rows,
			@RequestParam(value = "groupId", required = false) String groupId) throws Exception{
		Page<User> p = new Page<User>(page, rows);
		if(groupId == null){
 			this.userService.getUserList(p);
		}else{
			this.userService.getUserByGroupId(groupId, p);
		}
		List<Object> jsonList=new ArrayList<Object>(); 
		
		for(User user: p.getResult()){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", user.getId());
			map.put("name", user.getName());
			map.put("group", user.getGroup().getName());
			map.put("registerDate", user.getRegisterDate());
			jsonList.add(map);
		}
		Datagrid<Object> dataGrid = new Datagrid<Object>(p.getTotal(), jsonList);
		return dataGrid;
		
		
//		List<User> userList = new ArrayList<>();
//		if(groupId != null){
//			userList = this.userService.getUserList_page();
//		}else{
//			userList = this.userService.getUserByGroupId(groupId);
//		}
//		Pagination pagination = PaginationThreadUtils.get();
//		List<Group> groupList = this.groupService.getGroupList();
//		
//		model.addAttribute("userList", userList);
//		model.addAttribute("groupList", groupList);
//		model.addAttribute("groupId", groupId);
//		model.addAttribute("key", key);
//		model.addAttribute("flag", flag);
//		return "user/choose_user";
	}
	
}
