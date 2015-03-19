package com.zml.oa.action;

import java.util.List;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zml.oa.entity.Resource;
import com.zml.oa.pagination.Pagination;
import com.zml.oa.pagination.PaginationThreadUtils;
import com.zml.oa.service.IResourceService;

@Controller
@RequiresPermissions("admin:*")
@RequestMapping(value = "/resourceAction")
public class ResourceAction {

	@Autowired
    protected IResourceService resourceService;
	
	@RequestMapping(value = "/listResource_page")
	public String listResource_page(Model model) throws Exception{
		List<Resource> list = this.resourceService.getResourceListPage();
		Pagination pagination = PaginationThreadUtils.get();
		model.addAttribute("list", list);
		model.addAttribute("page", pagination.getPageStr());
		return "resource/list_resource";
	}
	
	@RequestMapping(value = "/toAdd")
	public String toAdd(Model model) throws Exception {
		if(!model.containsAttribute("resource")){
			model.addAttribute("resource", new Resource());
		}
		return "resource/add_reoustce";
	}
	
	@RequestMapping(value = "/doAdd")
	public String doAdd(@ModelAttribute("resource") @Valid Resource resource, 
						 BindingResult results,
						 Model model) throws Exception {
		if(results.hasErrors()){
			model.addAttribute("resource", resource);
			return toAdd(model);
		}
		
		resource.setAvailable(1);
		return "redirect:/resourceAction/listResource_page";
	}
	
	@RequestMapping(value = "/toUpdate/{id}")
	public String toUpdate(@PathVariable("id") Integer id, Model model) throws Exception{
		if(!model.containsAttribute("resource")){
			if(id != null){
				Resource resource = this.resourceService.getPermissions(id);
				List<Resource> menuList = this.resourceService.getResourceByType();
				model.addAttribute("list", menuList);
				model.addAttribute("resource", resource);
			}else{
				
			}
		}
		return "resource/update_resource";
	}
	
	@RequestMapping(value = "/doUpdate")
	public String doUpdate(@ModelAttribute("resource") @Valid Resource resource, 
							BindingResult results,
							RedirectAttributes model) throws Exception{
		if(results.hasErrors()){
			model.addAttribute("resource", resource);
			return toUpdate(resource.getId(), model);
		}
		try {
			this.resourceService.doUpdate(resource);
			model.addFlashAttribute("message", "修改成功！");
		} catch (Exception e) {
			model.addFlashAttribute("message", "修改失败！");
			throw e;
		}
		return "redirect:/resourceAction/toUpdate/"+resource.getId();
	}
	
	@RequestMapping(value = "/doDelete/{id}")
	public String doDelete(@PathVariable("id") Integer id, RedirectAttributes redirectAttribute) throws Exception {
		try {
			Resource resource = this.resourceService.getPermissions(id);
			resource.setAvailable(0);
			this.resourceService.doUpdate(resource);
			redirectAttribute.addFlashAttribute("message", "删除成功！");
		} catch (Exception e) {
			redirectAttribute.addFlashAttribute("message", "删除失败！");
			throw e;
		}
		return "redirect:/resourceAction/listResource_page";
	}
}
