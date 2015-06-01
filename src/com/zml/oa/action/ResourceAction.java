package com.zml.oa.action;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zml.oa.entity.Message;
import com.zml.oa.entity.Resource;
import com.zml.oa.service.IGroupAndResourceService;
import com.zml.oa.service.IResourceService;

@Controller
@RequiresPermissions("admin:*")
@RequestMapping(value = "/resourceAction")
public class ResourceAction {

	@Autowired
    protected IResourceService resourceService;
	
	@Autowired
	protected IGroupAndResourceService irService;
	
//	@RequestMapping(value = "/listResource_page")
//	public String listResource_page(Model model) throws Exception{
//		List<Resource> list = this.resourceService.getResourceListPage();
//		Pagination pagination = PaginationThreadUtils.get();
//		model.addAttribute("list", list);
//		model.addAttribute("page", pagination.getPageStr());
//		return "resource/list_resource";
//	}
	

	/**
	 * 此方法会在其他方法之前执行，并将其自动添加到模型对象中，
	 * 在功能处理方法中调用Model 入参的containsAttribute("list")将会返回true。
	 * @return
	 * @throws Exception
	 */
//	@ModelAttribute("list")
//	public List<Resource> getResource() throws Exception{
//		return this.resourceService.getResourceByType();
//	}
	
//	@RequestMapping(value = "/doAdd")
//	public String doAdd(@Valid @ModelAttribute("res") Resource res, 
//						 BindingResult results,
//						 Model model) throws Exception {
//		if(results.hasErrors()){
//			return toAdd(model);
//		}
//		
//		res.setAvailable(1);
//		this.resourceService.doAdd(res);
//		return "redirect:/resourceAction/listResource_page";
//	}
	
//	@RequestMapping(value = "/toUpdate/{id}")
//	public String toUpdate(@PathVariable("id") Integer id, Model model) throws Exception{
//		if(!model.containsAttribute("resource")){
//			if(id != null){
//				Resource resource = this.resourceService.getPermissions(id);
//				model.addAttribute("resource", resource);
//			}else{
//				model.addAttribute(Constants.MESSAGE, "出错，id为空！");
//				return toAdd(model);
//			}
//		}
//		List<Resource> menuList = this.resourceService.getResourceByType();
//		model.addAttribute("list", menuList);
//		return "resource/update_resource";
//	}
	
//	@RequestMapping(value = "/doUpdate")
//	public String doUpdate(@Valid Resource resource, BindingResult results,
//							Model model,
//							RedirectAttributes redirectAttribute) throws Exception{
//		if(results.hasErrors()){
//			return toUpdate(resource.getId(), model);
//		}
//		try {
//			this.resourceService.doUpdate(resource);
//			redirectAttribute.addFlashAttribute(Constants.MESSAGE, "修改成功！");
//		} catch (Exception e) {
//			redirectAttribute.addFlashAttribute(Constants.MESSAGE, "修改失败！");
//			throw e;
//		}
//		return "redirect:/resourceAction/toUpdate/"+resource.getId();
//	}
	
//	@RequestMapping(value = "/doDelete/{id}")
//	public String doDelete(@PathVariable("id") Integer id, RedirectAttributes redirectAttribute) throws Exception {
//		try {
//			Resource resource = this.resourceService.getPermissions(id);
//			resource.setAvailable(0);
//			this.resourceService.doUpdate(resource);
//			redirectAttribute.addFlashAttribute(Constants.MESSAGE, "删除成功！");
//		} catch (Exception e) {
//			redirectAttribute.addFlashAttribute(Constants.MESSAGE, "删除失败！");
//			throw e;
//		}
//		return "redirect:/resourceAction/listResource_page";
//	}
	
	/**
	 * 跳转列表页面-easyui
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public String toListResource(){
		return "resource/list_resource";
	}
	
	/**
	 * 获取所有resource数据
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listResource")
	@ResponseBody
	public List<Resource> listResource() throws Exception{
		List<Resource> list = this.resourceService.getAllResource();
		return list;
	}
	
	/**
	 * 获取菜单树-easyui
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/getMenuList")
	@ResponseBody
	public List<Resource> getMenuList() throws Exception{
		List<Resource> menuList = this.resourceService.getResourceByType();
		return menuList;
	} 
	
	/**
	 * 跳转添加页面-easyui
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(Model model) throws Exception {
//		if(!model.containsAttribute("res")){
//			model.addAttribute("res", new Resource());
//		}
//		List<Resource> menuList = this.resourceService.getResourceByType();
//		model.addAttribute("list", menuList);
		return "resource/add_resource";
	}
	
	/**
	 * 添加资源-easyui
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doAdd")
	@ResponseBody
	public Message doAdd(@ModelAttribute Resource resource) throws Exception{
		resource.setAvailable(1);
		this.resourceService.doAdd(resource);
		return new Message(Boolean.TRUE, "添加成功！");
	}
	
	/**
	 * 跳转修改页面-easyui
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toUpdate/{id}")
	public String toUpdate(@PathVariable("id") String id) throws Exception{
		return "resource/update_resource";
	}
	
	/**
	 * 修改Resource-easyui
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Message doUpdate(@ModelAttribute Resource resource) throws Exception{
		Message message = new Message();
		try {
			this.resourceService.doUpdate(resource);
			message.setStatus(Boolean.TRUE);
			message.setMessage("保存成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("保存失败！");
			throw e;
		}
		return message;
	}
	
	/**
	 * 根据id删除t_resource 和 t_group_resource 的相应数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doDelete/{id}")
	@ResponseBody
	public Message doDelete(@PathVariable("id") String id) throws Exception{
		Message message = new Message();
		try {
			if(StringUtils.isNotBlank(id)){
				this.irService.doDelByResource(id);
				this.resourceService.doDelete(id);
				message.setStatus(Boolean.TRUE);
				message.setMessage("删除成功！");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("删除失败！");
			throw e;
		}
		return message;
	}
}
