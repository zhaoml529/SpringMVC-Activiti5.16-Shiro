package com.zml.oa.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zml.oa.entity.BaseVO;
import com.zml.oa.entity.CommentVO;
import com.zml.oa.entity.User;
import com.zml.oa.entity.Vacation;
import com.zml.oa.pagination.Pagination;
import com.zml.oa.pagination.PaginationThreadUtils;
import com.zml.oa.service.IUserService;
import com.zml.oa.service.IVacationService;
import com.zml.oa.util.UserUtil;

/**
 * @ClassName: VacationAction
 * @Description:请假控制类
 * @author: zml
 * @date: 2014-12-1 上午12:35:50
 *
 */

@Controller
@RequestMapping("/vacationAction")
public class VacationAction {
	private static final Logger logger = Logger.getLogger(VacationAction.class);
	
	@Autowired
	private IVacationService vacationService;
	
	@Autowired
	protected RuntimeService runtimeService;
	
    @Autowired
    protected IdentityService identityService;
    
    @Autowired
    protected TaskService taskService;
    
    @Autowired
    protected RepositoryService repositoryService;
    
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/toList_page")
	public String toList(Model model) throws Exception{
		List<Vacation> list = this.vacationService.toList();
		Pagination pagination = PaginationThreadUtils.get();
		pagination.processTotalPage();
		model.addAttribute("page", pagination.getPageStr());
		model.addAttribute("vacationList", list);
		return "vacation/list_vacation";
	}
	
	@RequestMapping(value = "/toAdd", method = RequestMethod.GET)
	public ModelAndView toAdd(Model model){
		if(!model.containsAttribute("vacation")) {
            model.addAttribute("vacation", new Vacation());
        }
		
		return new ModelAndView("vacation/add_vacation").addObject(model);
	}
	
    /**
     * 添加并启动请假流程
     *
     * @param leave
     */
//	@RequestMapping("/doAdd")
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	public String doAdd(
			@ModelAttribute("vacation") @Valid Vacation vacation,BindingResult results, 
			RedirectAttributes redirectAttributes, 
			HttpSession session, 
			Model model) throws Exception{
        User user = UserUtil.getUserFromSession(session);
        
        if(results.hasErrors()){
        	model.addAttribute("vacation", vacation);
        	return "vacation/add";
        }
        
        
        // 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
        if (user == null || user.getId() == null) {
        	model.addAttribute("msg", "登录超时，请重新登录!");
            return "login";
        }
        vacation.setUserId(user.getId());
        vacation.setTitle(user.getName()+" 的请假申请");
        vacation.setBusinessType("请假申请");
        vacation.setApplyDate(new Date());
        this.vacationService.add(vacation);
      //待测试--添加完成后vacation.getId() 是否有值？
        String businessKey = vacation.getId().toString();
        ProcessInstance processInstance = null;
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(vacation.getUserId().toString());
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("entity", vacation);
            if(vacation.getDays() <= 3){
            	variables.put("auditUser", "manager");
            }else{
            	variables.put("auditUser", "director");
            }
            processInstance = runtimeService.startProcessInstanceByKey("com.zml.oa.vacation", businessKey, variables);
            String processInstanceId = processInstance.getId();
            vacation.setProcessInstanceId(processInstanceId);
            redirectAttributes.addFlashAttribute("message", "流程已启动，流程ID：" + processInstanceId);
            logger.info("processInstanceId: "+processInstanceId);
        } catch (ActivitiException e) {
            if (e.getMessage().indexOf("no processes deployed with key") != -1) {
                logger.warn("没有部署流程!", e);
                redirectAttributes.addFlashAttribute("error", "没有部署流程，请在[工作流]->[流程管理]页面点击<重新部署流程>-待完成");
            } else {
                logger.error("启动请假流程失败：", e);
                redirectAttributes.addFlashAttribute("error", "系统内部错误！");
            }
        } catch (Exception e) {
            logger.error("启动请假流程失败：", e);
            redirectAttributes.addFlashAttribute("error", "系统内部错误！");
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
		return "redirect:/vacationAction/toAdd";
	}
	
	/**
	 * 签收任务
	 * @return
	 */
	@RequestMapping("/claim/{taskId}")
	public String claim(@PathVariable("taskId") String taskId, HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
		String userId = UserUtil.getUserFromSession(session).getId().toString();
        taskService.claim(taskId, userId);
        redirectAttributes.addFlashAttribute("message", "任务已签收");
        return "redirect:/vacationAction/todoTaskList_page";
	}
	
	/**
	 * 查询待办任务
	 * @param session
	 * @param redirectAttributes
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/todoTaskList_page", method = RequestMethod.GET)
	public String todoTaskList_page(HttpSession session, Model model) throws Exception{
		String userId = UserUtil.getUserFromSession(session).getId().toString();
		
		User user = this.userService.getUserById(new Integer(userId));
		 // 根据当前人的ID查询
//        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(userId);
		TaskQuery taskQuery = this.taskService.createTaskQuery().taskCandidateGroup(user.getGroup().getType());
        Integer totalSum = taskQuery.list().size();
        logger.info("UserID: "+userId +" totalSum: "+ totalSum);
        //计算分页
        Pagination pagination = PaginationThreadUtils.get();
        if (pagination == null) {
			pagination = new Pagination();
			PaginationThreadUtils.set(pagination);
			pagination.setCurrentPage(1);
		}
		if (pagination.getTotalSum() == 0) {
			pagination.setTotalSum(totalSum);
		}
		pagination.processTotalPage();
		int firstResult = (pagination.getCurrentPage() - 1) * pagination.getPageNum();
		int maxResult = pagination.getPageNum();
		
		List<Task> tasks = taskQuery.listPage(firstResult, maxResult);
        List<Vacation> vacationList = new ArrayList<Vacation>();
     // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
        	String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            Vacation vacation = this.vacationService.findById(new Integer(businessKey));
            String user_name = this.userService.getUserById(vacation.getUserId()).getName();
            vacation.setUser_name(user_name);
            vacation.setBusinessType(BaseVO.CANDIDATE);
            vacation.setTask(task);
            vacation.setProcessInstance(processInstance);
            vacation.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
            vacationList.add(vacation);
        }
		model.addAttribute("vacationList", vacationList);
		model.addAttribute("businessType", BaseVO.CANDIDATE);
		model.addAttribute("page", pagination.getPageStr());
		return "task/list_task";
	}
	
    /**
     * 查询流程定义对象
     *
     * @param processDefinitionId 流程定义ID
     * @return
     */
    protected ProcessDefinition getProcessDefinition(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        logger.info(processDefinition.getVersion());
        return processDefinition;
    }
    
    /**
     * 查询受理任务列表
     * @param session
     * @param model
     * @return
     * @throws NumberFormatException
     * @throws Exception
     */
    @RequestMapping(value="/doTaskList_page", method = RequestMethod.GET)
    public String doTaskList_page(HttpSession session, Model model) throws NumberFormatException, Exception{
    	User user = UserUtil.getUserFromSession(session);
    	TaskQuery taskQuery = this.taskService.createTaskQuery().taskAssignee(user.getId().toString());
    	
    	Integer totalSum = taskQuery.list().size();
        //计算分页
        Pagination pagination = PaginationThreadUtils.get();
        if (pagination == null) {
			pagination = new Pagination();
			PaginationThreadUtils.set(pagination);
			pagination.setCurrentPage(1);
		}
		if (pagination.getTotalSum() == 0) {
			pagination.setTotalSum(totalSum);
		}
		pagination.processTotalPage();
		int firstResult = (pagination.getCurrentPage() - 1) * pagination.getPageNum();
		int maxResult = pagination.getPageNum();
		
		List<Task> tasks = taskQuery.listPage(firstResult, maxResult);
		List<Vacation> vacationList = new ArrayList<Vacation>();
	     // 根据流程的业务ID查询实体并关联
	        for (Task task : tasks) {
	        	String processInstanceId = task.getProcessInstanceId();
	            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
	            String businessKey = processInstance.getBusinessKey();
	            if (businessKey == null) {
	                continue;
	            }
	            Vacation vacation = this.vacationService.findById(new Integer(businessKey));
	            vacation.setUser_name(user.getName());
	            vacation.setBusinessType(BaseVO.CANDIDATE);
	            vacation.setTask(task);
	            vacation.setProcessInstance(processInstance);
	            vacation.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
	            vacationList.add(vacation);
	        }
			model.addAttribute("vacationList", vacationList);
			model.addAttribute("businessType", BaseVO.ASSIGNEE);
			model.addAttribute("page", pagination.getPageStr());
    	return "task/list_task"; 
    }
    
    /**
     * 审批请假流程
     * @param taskId
     * @param model
     * @return
     * @throws NumberFormatException
     * @throws Exception
     */
    @RequestMapping("/toApproval/{taskId}")
    public String toApproval(@PathVariable("taskId") String taskId, Model model) throws NumberFormatException, Exception{
    	Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		// 根据任务查询流程实例
    	String processInstanceId = task.getProcessInstanceId();
		ProcessInstance pi = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		// 查询一个任务所在流程的全部评论
		List<Comment> comments = this.taskService.getProcessInstanceComments(pi.getId());
		List<CommentVO> commnetList = new ArrayList<CommentVO>();
		for(Comment comment : comments){
			User user = this.userService.getUserById(new Integer(comment.getUserId()));
			CommentVO vo = new CommentVO();
			vo.setContent(comment.getFullMessage());
			vo.setTime(comment.getTime());
			vo.setUserName(user.getName());
			commnetList.add(vo);
		}
		Vacation vacation = (Vacation) this.runtimeService.getVariable(pi.getId(), "entity");
		vacation.setTask(task);
		model.addAttribute("vacation", vacation);
		model.addAttribute("commentList", commnetList);
    	return "vacation/audit_vacation";
    }
    
    @RequestMapping("/complate/{taskId}")
    public String complate(
    		@RequestParam("content") String content,
    		@RequestParam("completeFlag") Boolean completeFlag,
    		@PathVariable("taskId") String taskId, 
    		RedirectAttributes redirectAttributes,
    		HttpSession session) throws Exception{
    	User user = UserUtil.getUserFromSession(session);
    	Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		// 根据任务查询流程实例
    	String processInstanceId = task.getProcessInstanceId();
    	ProcessInstance pi = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		this.identityService.setAuthenticatedUserId(user.getId().toString());
		// 添加评论
		this.taskService.addComment(taskId, pi.getId(), content);
		Map<String, Object> variables = new HashMap<String, Object>();
//		Boolean flag = "true".equals(completeFlag);
		variables.put("isPass", completeFlag);
		variables.put("auditUser", "hr");
		logger.info("variables key:isPass, value:"+completeFlag+"---flag:"+completeFlag);
		// 完成任务
		this.taskService.complete(taskId, variables);
		redirectAttributes.addFlashAttribute("message", "任务办理完成！");
    	return "redirect:/vacationAction/doTaskList_page";
    }
}
