package com.zml.oa.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zml.oa.entity.Group;
import com.zml.oa.pagination.Pagination;
import com.zml.oa.pagination.PaginationThreadUtils;
import com.zml.oa.service.IGroupService;

@Controller
@RequestMapping("/groupAction")
public class GroupAction {

	@Autowired
	private IGroupService groupService;
	
	@RequestMapping("/getAll")
	public String toList(Model model) throws Exception{
		List<Group> list = this.groupService.getGroupList();
		model.addAttribute("groupList", list);
		return "group/list_group";
	}
	
	@RequestMapping("/toList_page")
	public String toListPage(Model model) throws Exception{
		List<Group> list = this.groupService.getGroupListPage();
		Pagination pagination = PaginationThreadUtils.get();
		model.addAttribute("page", pagination.getPageStr());
		model.addAttribute("groupList", list);
		return "group/list_group";
	}
	
	@RequestMapping("/chooseGroup_page")
	public String chooseGroup(@RequestParam("key") String key, Model model) throws Exception{
		List<Group> list = this.groupService.getGroupListPage();
		Pagination pagination = PaginationThreadUtils.get();
		model.addAttribute("page", pagination.getPageStr());
		model.addAttribute("groupList", list);
		model.addAttribute("key", key);
		return "group/choose_group";
	}
	
}
