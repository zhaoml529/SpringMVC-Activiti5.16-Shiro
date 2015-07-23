package com.zml.oa.action;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zml.oa.entity.Datagrid;
import com.zml.oa.entity.Group;
import com.zml.oa.entity.Message;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IGroupAndResourceService;
import com.zml.oa.service.IGroupService;
import com.zml.oa.util.BeanUtils;

@Controller
@RequiresPermissions("admin:*")
@RequestMapping("/groupAction")
public class GroupAction {

	@Autowired
	private IGroupService groupService;
	
	@Autowired
	private IGroupAndResourceService grService;
	
	@RequestMapping("/getAll")
	public String toList(Model model) throws Exception{
		List<Group> list = this.groupService.getGroupList();
		model.addAttribute("groupList", list);
		return "group/list_group";
	}
	
//	@RequestMapping("/toList_page")
//	public String toListPage(Model model) throws Exception{
//		List<Group> list = this.groupService.getGroupListPage();
//		Pagination pagination = PaginationThreadUtils.get();
//		model.addAttribute("page", pagination.getPageStr());
//		model.addAttribute("groupList", list);
//		return "group/list_group";
//	}
	
	/**
	 * 跳转候选组也没-easyui
	 * @param taskDefKey
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toChooseGroup")
	public ModelAndView toChooseGroup(@RequestParam("taskDefKey") String taskDefKey, Model model) throws Exception{
		ModelAndView mv = new ModelAndView("bpmn/choose_group");
		mv.addObject("taskDefKey", taskDefKey);
		return mv;
	}
	
	/**
	 * 候选组-easyui
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/chooseGroup")
	@ResponseBody
	public Datagrid<Group> chooseGroup(
			@RequestParam(value = "page", required = false) Integer page, 
			@RequestParam(value = "rows", required = false) Integer rows) throws Exception{
		Page<Group> p = new Page<Group>(page, rows);
		this.groupService.getGroupListPage(p);
		return new Datagrid<Group>(p.getTotal(), p.getResult());
		
	}
	
	/**
	 * 加载组信息-easyui
	 * @return
	 */
	@RequestMapping("/getGroupList")
	@ResponseBody
	public Datagrid<Group> getList(@RequestParam(value = "page", required = false) Integer page, 
								@RequestParam(value = "rows", required = false) Integer rows) throws Exception{
		Page<Group> p = new Page<Group>(page, rows);
		this.groupService.getGroupListPage(p);
		Datagrid<Group> dataGrid = new Datagrid<Group>(p.getTotal(), p.getResult());
		return dataGrid;
	}
	
	/**
	 * 跳转组权限管理页面-easyui
	 * @return
	 */
	@RequestMapping(value ="/permissionAssignment")
	public String permissionAssignment() throws Exception{
		return "permission/permissionAssignment";
	}
	
	/**
	 * 跳转到添加组页面-easyui
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd() throws Exception{
		return "permission/add_group";
	}
	
	/**
	 * 跳转到添加组页面-easyui
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toUpdate")
	public String toUpdate() throws Exception{
//		if(StringUtils.isNoneBlank(id)){
//			Group group = this.groupService.getGroupById(id);
//			model.addAttribute("group", group);
//		}
		return "permission/update_group";
	}
	
	/**
	 * 添加Group-easyui
	 * @param group
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	@ResponseBody
	public Message doAdd(@ModelAttribute Group group) throws Exception{
		this.groupService.doAdd(group);
		return new Message(Boolean.TRUE, "添加成功！");
	}
	
	/**
	 * 更新Group-easyui
	 * @param group
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Message doUpdate(@ModelAttribute Group group) throws Exception{
		this.groupService.doUpdate(group);
		return new Message(Boolean.TRUE, "更新成功！");
	}
	
	/**
	 * 删除组和组权限-easyui
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete/{id}")
	@ResponseBody
	public Message delete(@PathVariable("id") Integer id) throws Exception{
		if(!BeanUtils.isBlank(id)){
			this.grService.doDelByGroup(id);
			this.groupService.doDelete(new Group(id));
			return new Message(Boolean.TRUE, "删除成功！");
		}else{
			return new Message(Boolean.FALSE, "删除失败！ID为空！");
		}
	}
	
	/**
	 * 候选人页面、添加用户页面使用-easyui
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAllGroup")
	@ResponseBody
	public List<Group> getGroupList() throws Exception{
		List<Group> list = this.groupService.getGroupList();
		return list;
	}
}
