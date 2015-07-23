package com.zml.oa.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zml.oa.entity.Datagrid;
import com.zml.oa.entity.GroupAndResource;
import com.zml.oa.entity.Message;
import com.zml.oa.entity.Resource;
import com.zml.oa.entity.UserTask;
import com.zml.oa.pagination.Page;
import com.zml.oa.pagination.Pagination;
import com.zml.oa.pagination.PaginationThreadUtils;
import com.zml.oa.service.IGroupAndResourceService;
import com.zml.oa.service.IResourceService;
import com.zml.oa.service.IUserTaskService;
import com.zml.oa.util.BeanUtils;

/**
 * @ClassName: PermissionAction
 * @Description:权限管理
 * @author: zml
 * @date: 2015-03-09 上午12:19:10
 *
 */

@Controller
@RequiresPermissions("admin:*")
@RequestMapping("/permissionAction")
public class PermissionAction {

    @Autowired
    protected RepositoryService repositoryService;
    
    @Autowired
    protected IUserTaskService userTaskService;
    
    @Autowired
    protected IGroupAndResourceService garService;
    
    @Autowired
    protected IResourceService resourceService;
    
	@RequestMapping(value = "/setAuthor")
	public String setAuthor(@RequestParam("id") String processDefinitionId) {
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点
		for (ActivityImpl activity : activitiList) {
			
		}
		
		return null;
	}
	
	@RequestMapping(value = "/listUserTask")
	@ResponseBody
	public List<UserTask> listUserTask(@RequestParam("procDefKey") String procDefKey) throws Exception{
		List<UserTask> list = this.userTaskService.findByWhere(procDefKey);
		return list;
	}
	/**
	 * easyui-删除 usertask表中数据，初始化节点信息。
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/initialization")
	@ResponseBody
	public Message initialization(HttpServletResponse response) throws Exception {
		Message message = new Message();
		try {
			this.userTaskService.deleteAll();
			ProcessDefinitionQuery proDefQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
			List<ProcessDefinition> processDefinitionList = proDefQuery.list();
			for(ProcessDefinition processDefinition : processDefinitionList){
				//读取节点信息保存到usertask表
				setSingleActivitiInfo(processDefinition);
			}
			message.setMessage("初始化成功！");
			message.setStatus(Boolean.TRUE);
		} catch (Exception e) {
			message.setMessage("初始化失败！");
			message.setStatus(Boolean.FALSE);
			throw e;
		}
		return message;
		
	}
	
	/**
	 * 初始化单个bpmn文件到usertask表
	 * @param processDefinitionId
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loadSingleBpmn")
	public String loadSingleBpmn(@RequestParam("processDefinitionId") String processDefinitionId,
								RedirectAttributes redirectAttributes) throws Exception{
		ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
		//读取节点信息保存到usertask表
		setSingleActivitiInfo(processDefinition);
		redirectAttributes.addFlashAttribute("message", "加载成功！");
		return "redirect:/permissionAction/loadBpmn_page";
	}

	
	private void setSingleActivitiInfo(ProcessDefinition processDefinition) throws Exception{
		String proDefKey = processDefinition.getKey();
		List<UserTask> list = this.userTaskService.findByWhere(proDefKey);
		ProcessDefinitionEntity processDef = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinition.getId());
		List<ActivityImpl> activitiList = processDef.getActivities();//获得当前任务的所有节点
		for (ActivityImpl activity : activitiList) {
			ActivityBehavior activityBehavior = activity.getActivityBehavior();
			boolean isFound = false;
			//是否为用户任务
			if (activityBehavior instanceof UserTaskActivityBehavior) {
				UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
	            TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
	            //任务所属角色
	            String taskDefKey = taskDefinition.getKey();
	            Expression taskName = taskDefinition.getNameExpression();
	            
	            //判断表中是否存在此节点
	            if(list.size() != 0){
					for(UserTask userTask : list){
						if(taskDefKey.equals(userTask.getTaskDefKey())){
							userTask.setProcDefKey(processDefinition.getKey());
				            userTask.setProcDefName(processDefinition.getName());
				            userTask.setTaskDefKey(taskDefKey);
				            userTask.setTaskName(taskName.toString());
				            this.userTaskService.doUpdate(userTask);
				            isFound = true;
				            break;
						}
					}
					
				}
	            if(!isFound){
	            	UserTask userTask = new UserTask();
		            userTask.setProcDefKey(processDefinition.getKey());
		            userTask.setProcDefName(processDefinition.getName());
		            userTask.setTaskDefKey(taskDefKey);
		            userTask.setTaskName(taskName.toString());
		            this.userTaskService.doAdd(userTask);
	            }
			}
		}
	}
	
	/**
	 * 保存设定的审批人员-easyui
	 * @param procDefKey
	 * @param request
	 * @param redirectAttribute
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/setPermission")
	@ResponseBody
	public Message setPermission(@RequestParam("procDefKey") String procDefKey, HttpServletRequest request) throws Exception{
		List<UserTask> list = this.userTaskService.findByWhere(procDefKey);
		for(UserTask userTask : list){
			String taskDefKey = userTask.getTaskDefKey();
			String ids = request.getParameter(taskDefKey+"_id");
			String names = request.getParameter(taskDefKey+"_name");
			String taskType = request.getParameter(taskDefKey+"_taskType");
			userTask.setTaskType(taskType);
			userTask.setCandidate_name(names);
			userTask.setCandidate_ids(ids);
			this.userTaskService.doUpdate(userTask);
		}
		return new Message(Boolean.TRUE, "设置审批人员成功！");
	}
	
	@RequestMapping(value = "/listPermission_page")
	public String listPermission(@RequestParam("groupId") Integer groupId, Model model) throws Exception{
		List<Resource> resList = this.resourceService.getResourceListPage();
		Pagination pagination = PaginationThreadUtils.get();
		
		List<GroupAndResource> garList = this.garService.getResource(groupId);
		Map<Integer, Integer> garMap = new HashMap<Integer, Integer>();
		for(GroupAndResource gar : garList){
			garMap.put(gar.getResourceId(), gar.getId());
		}
		model.addAttribute("resList", resList);
		model.addAttribute("garMap", garMap);
		model.addAttribute("groupId", groupId);
		model.addAttribute("page", pagination.getPageStr());
		return "permission/list_permission";
	}
	
	@RequestMapping(value = "/addPermission")
	public void addPermission(@RequestParam("resourceId") Integer resourceId,
								@RequestParam("groupId") Integer groupId,
								HttpServletResponse response) throws Exception {
		PrintWriter print = response.getWriter();
		try {
			GroupAndResource gar = new GroupAndResource();
			gar.setGroupId(groupId);
			gar.setResourceId(resourceId);
			this.garService.doAdd(gar);
			print.print("success");
		} catch (Exception e) {
			print.print("fail");
			throw e;
		}
	}
	
	@RequestMapping(value = "/delPermission")
	public void delPermission(@RequestParam("id") Integer id, HttpServletResponse response) throws Exception {
		PrintWriter print = response.getWriter();
		try {
			GroupAndResource gar = new GroupAndResource();
			gar.setId(id);
			this.garService.doDelete(gar);
			print.print("success");
		} catch (Exception e) {
			print.print("fail");
			throw e;
		}
	}
	
	/**
	 * easyui
	 * @return
	 */
	@RequestMapping(value = "/toListBpmn")
	public String toListBpmn(){
		return "bpmn/list_bpmn";
	}
	
	/**
	 * BPMN列表 easyui
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/listBpmn")
	@ResponseBody
	public Datagrid<Object> listBpmn(
			@RequestParam(value = "page", required = false) Integer page,
    		@RequestParam(value = "rows", required = false) Integer rows){
		Page<Object> p = new Page<Object>(page, rows);
		List<Object> jsonList=new ArrayList<Object>(); 
		
		ProcessDefinitionQuery proDefQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
		Integer totalSum = proDefQuery.list().size();
		int[] pageParams = p.getPageParams(totalSum);
		List<ProcessDefinition> processDefinitionList = proDefQuery.listPage(pageParams[0], pageParams[1]);
		for(ProcessDefinition pd : processDefinitionList){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", pd.getId());
			map.put("name", pd.getName());
			map.put("key", pd.getKey());
			map.put("resourceName", pd.getResourceName());
			map.put("diagramResourceName", pd.getDiagramResourceName());
			jsonList.add(map);
		}
		return new Datagrid<Object>(p.getTotal(), jsonList);
		
	}
	
	
	/**
	 * 获取组权限-easyui
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/getGroupPermission")
	@ResponseBody
	public List<GroupAndResource> getGroupPermission(@RequestParam("groupId") Integer groupId) throws Exception{
		List<GroupAndResource> garList = this.garService.getResource(groupId);
		return garList;
	}
	
	/**
	 * 获取所有资源列表-easyui
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getResourceList")
	@ResponseBody
	public List<Resource> getResource() throws Exception{
		List<Resource> resList = this.resourceService.getAllResource();
		return resList;
	}
	
	/**
	 * 保存权限信息-easyui
	 * @param groupId
	 * @param resourceIds
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/savePermission")
	@ResponseBody
	public Message savePermission(@RequestParam("groupId") Integer groupId,
			  @RequestParam("resourceIds[]") String[] resourceIds) throws Exception{
		System.out.println(groupId+"-----"+resourceIds);
		if(!BeanUtils.isBlank(groupId)){
			this.garService.doDelByGroup(groupId);
			for(String resourceId: resourceIds){
				GroupAndResource gr = new GroupAndResource();
				gr.setGroupId(groupId);
				gr.setResourceId(new Integer(resourceId));
				this.garService.doAdd(gr);
			}
		}else{
			return new Message(Boolean.FALSE, "保存失败，请选择用户组！");
		}
		return new Message(Boolean.TRUE, "保存成功！");
	}
	
}
