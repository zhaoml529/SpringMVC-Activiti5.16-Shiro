package com.zml.oa.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zml.oa.entity.Group;
import com.zml.oa.service.IGroupService;

@Controller
@RequestMapping("/groupAction")
public class GroupAction {

	@Autowired
	private IGroupService groupService;
	
	@RequestMapping("/toList")
	public String toList(Model model) throws Exception{
		List<Group> list = this.groupService.getGroupList();
		model.addAttribute("groupList", list);
		return "group/list_group";
	}
	
}
