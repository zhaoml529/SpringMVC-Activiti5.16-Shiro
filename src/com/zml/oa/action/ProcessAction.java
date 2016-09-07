package com.zml.oa.action;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zml.oa.entity.BaseVO;
import com.zml.oa.entity.Datagrid;
import com.zml.oa.entity.ExpenseAccount;
import com.zml.oa.entity.Message;
import com.zml.oa.entity.ProcessInstanceEntity;
import com.zml.oa.entity.SalaryAdjust;
import com.zml.oa.entity.User;
import com.zml.oa.entity.Vacation;
import com.zml.oa.pagination.Page;
import com.zml.oa.service.IProcessService;
import com.zml.oa.service.IUserService;
import com.zml.oa.service.activiti.WorkflowDeployService;
import com.zml.oa.service.activiti.WorkflowService;
import com.zml.oa.util.ProcessDefinitionCache;
import com.zml.oa.util.UserUtil;
import com.zml.oa.util.WorkflowUtils;

/**
 * 流程控制类
 * @author ZML
 *
 */
@Controller
@RequestMapping("/processAction")
public class ProcessAction {
	private static final Logger logger = Logger.getLogger(ProcessAction.class);
    
	@Autowired
	protected IUserService userService;
    
    @Autowired
    protected WorkflowService traceService;

	@Autowired
	private IProcessService processService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private WorkflowDeployService workflowProcessDefinitionService;
    
//	@Autowired
//	private RevokeTask revokeTaskService;
	
//	@Autowired
//	private ProcessEngine processEngine;
	
    /**
     * 显示流程图,带流程跟踪
     * @param processInstanceId
     * @param response
     * @throws Exception 
     */
    @RequestMapping(value = "/process/showDiagram/{processInstanceId}", method = RequestMethod.GET)
	public void showDiagram(@PathVariable("processInstanceId") String processInstanceId, HttpServletResponse response) throws Exception {
	        InputStream imageStream = this.processService.getDiagram(processInstanceId);
	        // 输出资源内容到相应对象
	        byte[] b = new byte[1024];
	        int len;
	        while ((len = imageStream.read(b, 0, 1024)) != -1) {
	            response.getOutputStream().write(b, 0, len);
	        }
	}
    
    /**
     * 显示图片通过部署id，不带流程跟踪(没有乱码问题)
     * @param processDefinitionId
     * @param resourceType	资源类型(xml|image)
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/process/process-definition")
    public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resourceType") String resourceType,
                                 HttpServletResponse response) throws Exception {
    	InputStream resourceAsStream = this.processService.getDiagramByProDefinitionId_noTrace(resourceType, processDefinitionId);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
    
    
    /**
     * 显示图片通过流程id，不带流程跟踪(没有乱码问题)
     *
     * @param resourceType      资源类型(xml|image)
     * @param processInstanceId 流程实例ID
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/process/process-instance")
    public void loadByProcessInstance(@RequestParam("type") String resourceType, @RequestParam("pid") String processInstanceId, HttpServletResponse response)
            throws Exception {
        InputStream resourceAsStream = this.processService.getDiagramByProInstanceId_noTrace(resourceType, processInstanceId);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
    
    
    /**
     * 自定义流程跟踪信息-比较灵活(现在用的这个)
     *
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    @RequiresPermissions("user:process:trace")
    @RequestMapping(value = "/process/trace/{pid}")
    @ResponseBody
    public List<Map<String, Object>> traceProcess(@PathVariable("pid") String processInstanceId) throws Exception {
        List<Map<String, Object>> activityInfos = this.traceService.traceProcess(processInstanceId);
        return activityInfos;
    }
    
    /**
     * 以下方法是对应easyui的写法
     * 
     * @author ZML
     */
    
    /**
     * 跳转待办任务、已完成任务页面
     * @return
     */
    @RequestMapping(value = "/userTaskList")
    public String userTaskList(){
    	return "task/list_task";
    }
    

    /**
	 * 查询待办任务
	 * @param session
	 * @param redirectAttributes
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:task:todoTask")
	@RequestMapping(value = "/todoTask", method = {RequestMethod.POST})
	@ResponseBody
	public Datagrid<Object> todoTask(
			@RequestParam(value = "page", required = false) Integer page,
		  	@RequestParam(value = "rows", required = false) Integer rows) throws Exception{
		String userId = UserUtil.getUserFromSession().getId().toString();
		User user = this.userService.getUserById(new Integer(userId));
		Page<BaseVO> p = new Page<BaseVO>(page, rows);
		List<BaseVO> taskList = this.processService.findTodoTask(user, p);
		List<Object> jsonList=new ArrayList<Object>(); 
		for(BaseVO base : taskList){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("businessType", base.getBusinessType());
			map.put("user_name", base.getUser_name());
			map.put("title", base.getTitle());
			map.put("taskId", base.getTask().getId());
			map.put("taskName", base.getTask().getName());
			map.put("createTime", base.getTask().getCreateTime());
			String assign = base.getTask().getAssignee();
			if(assign != null){
				User u = this.userService.getUserById(new Integer(assign));
				assign = u.getName();
			}
			String owner = base.getTask().getOwner();
			if(owner != null){
				User u = this.userService.getUserById(new Integer(owner));
				owner = u.getName();
			}
			
			map.put("assign", assign);
			map.put("owner", owner);
			map.put("taskDefinitionKey", base.getTask().getTaskDefinitionKey());
			map.put("processInstanceId", base.getProcessInstance().getId());
			map.put("processDefinitionId", base.getProcessInstance().getProcessDefinitionId());
			map.put("processDefinitionKey", base.getProcessDefinition().getKey());	//任务跳转用
			map.put("supended", base.getProcessInstance().isSuspended());
			map.put("version", base.getProcessDefinition().getVersion());
			
			jsonList.add(map);
		}
		return new Datagrid<Object>(p.getTotal(), jsonList);
	}
    
	
    /**
     * 查看已完成任务列表
     *
     * @return
     * @throws Exception 
     */
    @RequiresPermissions("user:process:finished")
    @RequestMapping(value = "/endTask")
    @ResponseBody
    public Datagrid<Object> findFinishedTaskInstances(
			@RequestParam(value = "page", required = false) Integer page,
		  	@RequestParam(value = "rows", required = false) Integer rows) throws Exception {
    	User user = UserUtil.getUserFromSession();
    	Page<BaseVO> p = new Page<BaseVO>(page, rows);
    	List<BaseVO> taskList = this.processService.findFinishedTaskInstances(user, p);
    	List<Object> jsonList=new ArrayList<Object>(); 
    	for(BaseVO base : taskList){
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("businessType", base.getBusinessType());
    		map.put("user_name", base.getUser_name());
    		map.put("title", base.getTitle());
    		map.put("taskId", base.getHistoricTaskInstance().getId());
    		map.put("processInstanceId", base.getHistoricTaskInstance().getProcessInstanceId());
    		map.put("startTime", base.getHistoricTaskInstance().getStartTime());
    		map.put("claimTime", base.getHistoricTaskInstance().getClaimTime());
    		map.put("endTime", base.getHistoricTaskInstance().getEndTime());
    		map.put("deleteReason", base.getHistoricTaskInstance().getDeleteReason());
    		map.put("version", base.getProcessDefinition().getVersion());
    		jsonList.add(map);
    	}
    	return new Datagrid<Object>(p.getTotal(), jsonList);
    }
    
	/**
	 * 签收任务
	 * @return
	 */
	@RequiresPermissions("user:task:claim")
	@RequestMapping("/claim/{taskId}")
	@ResponseBody
	public Message claim(@PathVariable("taskId") String taskId) {
		Message message = new Message();
		try {
			User user = UserUtil.getUserFromSession();
			this.processService.doClaim(user, taskId);
			message.setStatus(Boolean.TRUE);
			message.setMessage("任务签收成功！");
		}catch (ActivitiObjectNotFoundException e){
			message.setStatus(Boolean.FALSE);
			message.setMessage("此任务不存在！任务签收失败！");
		}catch (ActivitiTaskAlreadyClaimedException e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("此任务已被其他组成员签收！请刷新页面重新查看！");
		}catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("任务签收失败！请联系管理员！");
		} 
        return message;
	}
    
	/**
	 * 委派任务
	 * 委派也是代办、协办，你领导接到一个任务，让你代办，你办理完成后任务还是回归到你的领导，事情是你做的，功劳是你领导的，这就是代办。
	 * 所以代办人完成任务后，任务还会回到原执行人，流程不会发生变化。
	 * @param taskId	代办人
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/process/delegateTask/{taskId}")
	@ResponseBody
	public Message delegateTask(@PathVariable("taskId") String taskId, @RequestParam("userId") String userId){
		Message message = new Message();
		try {
			this.processService.doDelegateTask(userId, taskId);
			message.setStatus(Boolean.TRUE);
			message.setMessage("委派任务成功！");
		} catch (ActivitiObjectNotFoundException e){
			message.setStatus(Boolean.FALSE);
			message.setMessage("此任务不存在！委派任务失败！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("委派任务失败，系统错误！");
		}
		return message;
	}
	
	/**
	 * 转办任务，办理完成后，流程会继续向下走。
	 * @param taskId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/process/transferTask/{taskId}")
	@ResponseBody
	public Message transferTask(@PathVariable("taskId") String taskId, @RequestParam("userId") String userId){
		Message message = new Message();
		try {
			this.processService.doTransferTask(userId, taskId);
			message.setStatus(Boolean.TRUE);
			message.setMessage("转办任务成功！");
		} catch (ActivitiObjectNotFoundException e){
			message.setStatus(Boolean.FALSE);
			message.setMessage("此任务不存在！委派任务失败！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("转办任务失败，系统错误！");
		}
		return message;
	}
	
	/**
	 * 撤销任务
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/process/revoke/{taskId}/{processInstanceId}")
	@ResponseBody
	public Message revoke(@PathVariable("taskId") String taskId, @PathVariable("processInstanceId") String processInstanceId) throws Exception{
		Message message = new Message();
		
		try {
			Integer revokeFlag = this.processService.revoke(taskId, processInstanceId);
//			Integer revokeFlag = this.revokeTaskService.revoke(taskId, processInstanceId);
//			Command<Integer> cmd = new RevokeTask(taskId, processInstanceId);
//			Integer revokeFlag = this.processEngine.getManagementService().executeCommand(cmd);
			
			
			if(revokeFlag == 0){
				message.setStatus(Boolean.TRUE);
				message.setMessage("撤销任务成功！");
			}else if(revokeFlag == 1){
				message.setStatus(Boolean.FALSE);
				message.setMessage("撤销任务失败 - [ 此审批流程已结束! ]");
			}else if(revokeFlag == 2){
				message.setStatus(Boolean.FALSE);
				message.setMessage("撤销任务失败 - [ 下一结点已经通过,不能撤销! ]");
			}
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
			message.setMessage("撤销任务失败 - [ 内部错误！]");
			throw e;
		}
		return message;
	}
	
    /**
     * 跳转流程管理页面
     * @return
     * @throws Exception
     */
    @RequestMapping("/process/toListProcessManager")
    public String toListProcessRunning() throws Exception{
    	return "workflow/list_process_manager";
    }
    
    /**
     * 管理运行中的流程
     * @param model
     * @return
     * @throws Exception
     */
    @RequiresPermissions("admin:process:*")
    @RequestMapping(value="/process/runningProcess")
    @ResponseBody
    public Datagrid<ProcessInstanceEntity> listRuningProcess(
    			@RequestParam(value = "page", required = false) Integer page,
			  	@RequestParam(value = "rows", required = false) Integer rows) throws Exception{
    	Page<ProcessInstance> p = new Page<ProcessInstance>(page, rows);
    	List<ProcessInstance> list = this.processService.listRuningProcess(p);
    	List<ProcessInstanceEntity> pieList = new ArrayList<ProcessInstanceEntity>();
    	for(ProcessInstance processInstance : list){
    		ProcessInstanceEntity pie = new ProcessInstanceEntity();
    		pie.setId(processInstance.getId());
    		pie.setProcessInstanceId(processInstance.getProcessInstanceId());
    		pie.setProcessDefinitionId(processInstance.getProcessDefinitionId());
    		pie.setActivityId(processInstance.getActivityId());
    		pie.setSuspended(processInstance.isSuspended());
    		
    		ProcessDefinitionCache.setRepositoryService(this.repositoryService);
    		String taskName = ProcessDefinitionCache.getActivityName(processInstance.getProcessDefinitionId(), processInstance.getActivityId());
    		pie.setTaskName(taskName);
    		pieList.add(pie);
    		
    	}
    	return new Datagrid<ProcessInstanceEntity>(p.getTotal(), pieList);
    }
    
    /**
     * 管理已结束的流程
     *
     * @return
     * @throws Exception 
     */
    @RequiresPermissions("admin:process:*")
    @RequestMapping(value = "/process/finishedProcess")
    @ResponseBody
    public Datagrid<Object> findFinishedProcessInstances(
    		@RequestParam(value = "page", required = false) Integer page,
    		@RequestParam(value = "rows", required = false) Integer rows) throws Exception {
    	Page<BaseVO> p = new Page<BaseVO>(page, rows);
    	List<Object> jsonList=new ArrayList<Object>(); 
    	List<BaseVO> processList = this.processService.findFinishedProcessInstances(p);
    	for(BaseVO base : processList){
    		Map<String, Object> map=new HashMap<String, Object>();
    		map.put("businessType", base.getBusinessType());
    		map.put("user_name", base.getUser_name());
    		map.put("title", base.getTitle());
    		map.put("startTime", base.getHistoricProcessInstance().getStartTime());
    		map.put("endTime", base.getHistoricProcessInstance().getEndTime());
    		map.put("deleteReason", base.getHistoricProcessInstance().getDeleteReason());
    		map.put("version", base.getProcessDefinition().getVersion());
    		jsonList.add(map);
    	}
    	return new Datagrid<Object>(p.getTotal(), jsonList);
    }
    
    /**
     * 跳转流程定义页面
     * @return
     * @throws Exception
     */
    @RequestMapping("/process/toListProcessInstance")
    public String toListProcess() throws Exception{
    	return "workflow/list_process_instance";
    }
    
    /**
     * 流程定义的加载
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
	@RequestMapping("/process/listProcess")
	@ResponseBody
    public Datagrid<com.zml.oa.entity.ProcessDefinitionEntity> listProcess(
    		@RequestParam(value = "page", required = false) Integer page,
    		@RequestParam(value = "rows", required = false) Integer rows) throws Exception{
    	ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
    	Page<Object[]> p = new Page<Object[]>(page, rows);
    	int[] pageParams = p.getPageParams(processDefinitionQuery.list().size());
    	List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageParams[0], pageParams[1]);
    	
    	List<com.zml.oa.entity.ProcessDefinitionEntity> pdList = new  ArrayList<com.zml.oa.entity.ProcessDefinitionEntity>();
    	for (ProcessDefinition processDefinition : processDefinitionList) {
    		com.zml.oa.entity.ProcessDefinitionEntity pd = new com.zml.oa.entity.ProcessDefinitionEntity();
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            //封装到ProcessDefinitionEntity中
            pd.setId(processDefinition.getId());
            pd.setName(processDefinition.getName());
            pd.setKey(processDefinition.getKey());
            pd.setDeploymentId(processDefinition.getDeploymentId());
            pd.setVersion(processDefinition.getVersion());
            pd.setResourceName(processDefinition.getResourceName());
            pd.setDiagramResourceName(processDefinition.getDiagramResourceName());
            pd.setDeploymentTime(deployment.getDeploymentTime());
            pd.setSuspended(processDefinition.isSuspended());
            pdList.add(pd);
        }
    	Datagrid<com.zml.oa.entity.ProcessDefinitionEntity> dataGrid = new Datagrid<com.zml.oa.entity.ProcessDefinitionEntity>(p.getTotal(), pdList);
    	return dataGrid;
    }
	
	
    /**
     * 激活、挂起流程定义-根据processDefinitionId
     * @param status
     * @param processInstanceId
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequiresPermissions("admin:process:suspend,active")
    @RequestMapping(value = "/process/updateProcessStatusByProDefinitionId")
    @ResponseBody
    public Message updateProcessStatusByProDefinitionId(
    		@RequestParam("status") String status,
    		@RequestParam("processDefinitionId") String processDefinitionId) throws Exception{
    	//如果用/{status}/{processDefinitionId} rest风格，@PathVariable获取的processDefinitionId 为com.zml.oa,实际是com.zml.oa.vacation:1:32529.难道是BUG?
    	Message message = new Message();
    	if (status.equals("active")) {
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            message.setStatus(Boolean.TRUE);
            message.setMessage("已激活ID为[" + processDefinitionId + "]的流程定义。");
        } else if (status.equals("suspend")) {
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            message.setStatus(Boolean.TRUE);
            message.setMessage("已挂起ID为[" + processDefinitionId + "]的流程定义。");
        }
    	return message;
    }
    
    /**
     * 激活、挂起流程实例-根据processInstanceId
     * @param status
     * @param processInstanceId
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequiresPermissions("admin:process:suspend,active")
    @RequestMapping(value = "/process/updateProcessStatusByProInstanceId/{status}/{processInstanceId}")
    @ResponseBody
    public Message updateProcessStatusByProInstanceId(
    		@PathVariable("status") String status, 
    		@PathVariable("processInstanceId") String processInstanceId,
            RedirectAttributes redirectAttributes) throws Exception{
    	Message message = new Message();
    	if (status.equals("active")) {
    		this.processService.activateProcessInstance(processInstanceId);
//          redirectAttributes.addFlashAttribute("message", "已激活ID为[ " + processInstanceId + " ]的流程实例。");
    		message.setStatus(Boolean.TRUE);
            message.setMessage("已激活ID为[" + processInstanceId + "]的流程实例。");
    	} else if (status.equals("suspend")) {
        	this.processService.suspendProcessInstance(processInstanceId);
//          redirectAttributes.addFlashAttribute("message", "已挂起ID为[ " + processInstanceId + " ]的流程实例。");
        	message.setStatus(Boolean.TRUE);
            message.setMessage("已挂起ID为[" + processInstanceId + "]的流程实例。");
    	}
    	return message;
    }
    
    /**
     * 部署全部流程
     *
     * @return
     * @throws Exception
     */
    @RequiresPermissions("admin:process:*")
    @RequestMapping(value = "/process/redeploy/all")
    @ResponseBody
    public Message redeployAll(@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir, 
				    		HttpServletResponse response) throws Exception {
    	try {
    		List<Deployment> deploymentList = this.repositoryService.createDeploymentQuery().list();
    		//删除现有所有流程实例
    		for(Deployment deployment : deploymentList){
    			String deploymentId = deployment.getId();
    			this.repositoryService.deleteDeployment(deploymentId, true);
    		}
    		//重新部署全部流程实例
    		//方法一：通过classpath/deploy目录下的.zip或.bar文件部署
    		workflowProcessDefinitionService.deployAllFromClasspath(exportDir);
    		
    		//方法二：通过classpath/bpmn下的流程描述文件部署-流程图错乱，一直提倡用打包部署没有任何问题。
//        	workflowProcessDefinitionService.redeployBpmn(exportDir);

    		return new Message(Boolean.TRUE, "已重新部署全部流程！");
		} catch (Exception e) {
			return new Message(Boolean.FALSE, "重新部署流程失败！");
		}
    }
    
    /**
     * 部署单个流程
     *
     * @return
     * @throws Exception
     */
    @RequiresPermissions("admin:process:*")
    @RequestMapping(value = "/process/redeploy/single")
    @ResponseBody
    public Message redeploySingle(@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir,
    							@RequestParam("resourceName") String resourceName,
    							@RequestParam(value = "diagramResourceName", required = false) String diagramResourceName,
    							@RequestParam("deploymentId") String deploymentId) throws Exception {
    	Message message = new Message();
        try {
        	this.repositoryService.deleteDeployment(deploymentId, true);
        	//方法一：通过classpath/deploy目录下的.zip或.bar文件部署
        	String processKey = resourceName.substring(0, resourceName.indexOf('.'))+".zip";
        	this.workflowProcessDefinitionService.redeploySingleFrom(exportDir, processKey);
        	//方法二：通过classpath/bpmn下的流程描述文件部署--流程图错乱，一直提倡用打包部署没有任何问题。
//        	workflowProcessDefinitionService.redeployBpmn(exportDir, resourceName,diagramResourceName);
        	message.setStatus(Boolean.TRUE);
        	message.setMessage("已重新部署选定流程！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
        	message.setMessage("部署选定流程失败！");
			throw e;
		}
        return message;
    }
    
    /**
     * 导入部署
     * --@Value用于将一个SpEL表达式结果映射到到功能处理方法的参数上。
     * @RequestParam(value = "file", required = false) required = false时可以不用传递这个参数，默认为true
     * @param exportDir
     * @param file
     * @return
     */
    @RequiresPermissions("admin:process:*")
    @RequestMapping(value = "/process/deploy")
    @ResponseBody
    public Message deploy(@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir, 
    					  @RequestParam(value = "file", required = false) MultipartFile file) {
    	//@Value("${export.diagram.path}")
    	//@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir,
    	Message message = new Message();
        String fileName = file.getOriginalFilename();
        try {
            InputStream fileInputStream = file.getInputStream();
            Deployment deployment = null;

            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = this.repositoryService.createDeployment().addZipInputStream(zip).deploy();
            } else {
                deployment = this.repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            }

            List<ProcessDefinition> list = this.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

            for (ProcessDefinition processDefinition : list) {
                WorkflowUtils.exportDiagramToFile(this.repositoryService, processDefinition, exportDir);
            }
            message.setStatus(Boolean.TRUE);
        	message.setMessage("流程部署成功！");
        } catch (Exception e) {
        	message.setStatus(Boolean.FALSE);
        	message.setMessage("流程部署失败！");
            logger.error("error on deploy process, because of file input stream", e);
        }

        return message;
    }
    
    /**
     * 删除部署的流程，级联删除流程实例 true。
     * 不管是否指定级联删除，部署的相关数据均会被删除，这些数据包括流程定义的身份数据（IdentityLink）、流程定义数据（ProcessDefinition）、流程资源（Resource）
     * 部署数据（Deployment）。
     * 如果设置级联(true)，则会删除流程实例数据（ProcessInstance）,其中流程实例也包括流程任务（Task）与流程实例的历史数据；如果设置flase 将不会级联删除。
     * 如果数据库中已经存在流程实例数据，那么将会删除失败，因为在删除流程定义时，流程定义数据的ID已经被流程实例的相关数据所引用。
     *
     * @param deploymentId 流程部署ID
     */
    @RequestMapping(value = "/process/delete")
    @ResponseBody
    public Message delete(@RequestParam("deploymentId") String deploymentId) {
    	this.repositoryService.deleteDeployment(deploymentId, true);
        return new Message(Boolean.TRUE, "删除成功！");
    }
    
    
    /**
     * 转换为model
     * @param processDefinitionId
     * @return
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @RequiresPermissions("admin:process:*")
    @RequestMapping(value = "/process/convert_to_model")
    @ResponseBody
    public Message convertToModel(@RequestParam("processDefinitionId") String processDefinitionId)
            throws UnsupportedEncodingException, XMLStreamException {
    	ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        com.fasterxml.jackson.databind.node.ObjectNode modelNode = converter.convertToJson(bpmnModel);
        org.activiti.engine.repository.Model modelData = repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getResourceName());
        modelData.setCategory(processDefinition.getDeploymentId());

        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
        modelData.setMetaInfo(modelObjectNode.toString());

        repositoryService.saveModel(modelData);

        repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));

        return new Message(Boolean.TRUE, "转换成功！请到[ 流程设计模型 ]菜单中查看！");
    }
    @RequiresPermissions("user:listApply")
    @RequestMapping(value = "/process/toListApply")
    public String toListApply(){
    	return "apply/list_apply";
    }
    
    /**
     * 用户查看请假申请、薪资调整申请、报销申请 - easyui
     * @param page
     * @param rows
     * @param businessType
     * @param session
     * @return
     * @throws Exception
     */
   // @RequiresPermissions("user:*:list") //process:vacation,salary,expense:running
    @RequestMapping(value="/process/runingProcessInstance/{businessType}/list")
    @ResponseBody
    public Datagrid<Object> getRuningProcessInstance(
    		@RequestParam(value = "page", required = false) Integer page,
    		@RequestParam(value = "rows", required = false) Integer rows,
    		@PathVariable("businessType") String businessType) throws Exception{
    	User user = UserUtil.getUserFromSession();
    	List<BaseVO> baseVO = new ArrayList<BaseVO>();
    	Integer total = 0;
    	if(BaseVO.VACATION.equals(businessType)){
    		//请假
    		Page<Vacation> p = new Page<Vacation>(page, rows);
    		baseVO = this.processService.listRuningVacation(user, p);
    		total = p.getTotal();
    	}else if(BaseVO.SALARY.equals(businessType)){
    		//调薪
    		Page<SalaryAdjust> p = new Page<SalaryAdjust>(page, rows);
    		baseVO = this.processService.listRuningSalaryAdjust(user, p);
    		total = p.getTotal();
    	}else if(BaseVO.EXPENSE.equals(businessType)){
    		//报销
    		Page<ExpenseAccount> p = new Page<ExpenseAccount>(page, rows);
    		baseVO = this.processService.listRuningExpense(user, p);
    		total = p.getTotal();
    	}
    	List<Object> jsonList=new ArrayList<Object>(); 
    	for(BaseVO base : baseVO){
    		Map<String, Object> map=new HashMap<String, Object>();
    		map.put("taskName", base.getTask().getName());
    		map.put("taskCreateTime", base.getTask().getCreateTime());
    		map.put("userName", base.getUser_name());
    		map.put("title", base.getTitle());
    		map.put("pd_version", base.getProcessDefinition().getVersion());
    		map.put("pi_id", base.getProcessInstance().getId());
    		map.put("pi_processDefinitionId", base.getProcessInstance().getProcessDefinitionId());
    		map.put("pi_suspended", base.getProcessInstance().isSuspended());
    		map.put("businessType", base.getBusinessType());
    		map.put("businessKey", base.getBusinessKey());
    		jsonList.add(map);
    	}
    	return new Datagrid<Object>(total, jsonList);
    }
    
    /**
     * 任务跳转（包括回退和向前）至指定活动节点
     * @param currentTaskId
     * @param targetTaskDefinitionKey
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/process/jumpTask")
    @ResponseBody
    public Message jumpTargetTask(@RequestParam("taskId") String currentTaskId, @RequestParam("taskDefinitionKey") String targetTaskDefinitionKey) throws Exception{
    	Message message = new Message();
    	try {
			this.processService.moveTo(currentTaskId, targetTaskDefinitionKey);
			message.setStatus(Boolean.TRUE);
        	message.setMessage("任务跳转成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
        	message.setMessage("任务跳转失败！");
		}
    	return message;
    }
    
    /**
     * 测试 动态添加流程信息
     * @throws Exception
     */
    @RequestMapping(value = "/addProcessByDynamic")
    public void addProcessByDynamic() throws Exception {
    	this.processService.addProcessByDynamic();
    }
}
