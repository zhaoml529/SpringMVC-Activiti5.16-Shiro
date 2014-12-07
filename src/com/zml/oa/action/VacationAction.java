package com.zml.oa.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
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
    protected HistoryService historyService;
    
    @Autowired
    protected TaskService taskService;
    
    @Autowired
    protected RepositoryService repositoryService;
    
	@Autowired
	private IUserService userService;
	
	/**
	 * 查询某人的所有请假申请
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toList_page")
	public String toList(HttpSession session, Model model) throws Exception{
		User user = UserUtil.getUserFromSession(session);
		List<Vacation> list = this.vacationService.toList(user.getId());
//		for(Vacation v : list){
//			if(BaseVO.APPROVAL_SUCCESS.equals(v.getStatus())){
//				Vacation vacation = (Vacation)this.historyService.createHistoricVariableInstanceQuery()
//					.processInstanceId(v.getProcessInstanceId()).variableName("entity");
//				
//			}
//		}
		Pagination pagination = PaginationThreadUtils.get();
		model.addAttribute("page", pagination.getPageStr());
		model.addAttribute("vacationList", list);
		return "vacation/list_vacation";
	}
	
	/**
	 * 跳转添加页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toAdd", method = RequestMethod.GET)
	public ModelAndView toAdd(Model model){
		if(!model.containsAttribute("vacation")) {
            model.addAttribute("vacation", new Vacation());
        }
		return new ModelAndView("vacation/add_vacation").addObject(model);
	}
	
	/**
	 * 详细信息
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/details/{id}", method = RequestMethod.GET)
	public String details(@PathVariable("id") Integer id, Model model) throws Exception{
		Vacation vacation = this.vacationService.findById(id);
		model.addAttribute("vacation", vacation);
		return "/vacation/details_vacation";
	}
	
    /**
     * 添加并启动请假流程
     *
     * @param leave
     */
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	public String doAdd(
			@ModelAttribute("vacation") @Valid Vacation vacation,BindingResult results, 
			RedirectAttributes redirectAttributes, 
			HttpSession session, 
			Model model) throws Exception{
        User user = UserUtil.getUserFromSession(session);
        
        if(results.hasErrors()){
        	model.addAttribute("vacation", vacation);
        	return "vacation/add_vacation";
        }
        
        
        // 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
        if (user == null || user.getId() == null) {
        	model.addAttribute("msg", "登录超时，请重新登录!");
            return "login";
        }
        vacation.setUserId(user.getId());
        vacation.setUser_name(user.getName());
        vacation.setTitle(user.getName()+" 的请假申请");
        vacation.setBusinessType(BaseVO.VACATION); 			//业务类型：请假申请
        vacation.setStatus(BaseVO.PENDING);					//审批中
        vacation.setApplyDate(new Date());
        this.vacationService.add(vacation);
        String businessKey = vacation.getId().toString();
        vacation.setBusinessKey(businessKey);
        ProcessInstance processInstance = null;
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(user.getId().toString());
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("entity", vacation);
            if(vacation.getDays() <= 3){
            	variables.put("auditGroup", "manager");
            }else{
            	variables.put("auditGroup", "director");
            }
            processInstance = runtimeService.startProcessInstanceByKey("com.zml.oa.vacation", businessKey, variables);
            String processInstanceId = processInstance.getId();
            vacation.setProcessInstanceId(processInstanceId);
            this.vacationService.update(vacation);
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
		logger.info("processInstanceId: "+processInstanceId+"--- pi.getId(): "+pi.getId());
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
		String taskDefinitionKey = task.getTaskDefinitionKey();
		logger.info("taskDefinitionKey: "+taskDefinitionKey);
		String result = null;
		if("modifyApply".equals(taskDefinitionKey)){
			result = "vacation/modify_vacation";
		}else{
			result = "vacation/audit_vacation";
		}
		model.addAttribute("vacation", vacation);
		model.addAttribute("commentList", commnetList);
    	return result;
    }
    
    /**
     * 完成任务
     * @param content
     * @param completeFlag
     * @param taskId
     * @param redirectAttributes
     * @param session
     * @return
     * @throws Exception
     */
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
		//评论人的id  一定要写，不然查看的时候会报错，没有用户
    	this.identityService.setAuthenticatedUserId(user.getId().toString());
		// 添加评论
		this.taskService.addComment(taskId, pi.getId(), content);
		//获取当前流程下的variable
        Vacation vacation = (Vacation) this.runtimeService.getVariable(pi.getId(), "entity");
//		String procId = this.vacationService.findById(vacation.getId()).getProcessInstanceId();
		vacation.setProcessInstanceId(processInstanceId);
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("isPass", completeFlag);
		if(completeFlag){
			variables.put("auditGroup", "hr");
			vacation.setStatus(BaseVO.APPROVAL_SUCCESS);
		}else{
            vacation.setTitle(vacation.getUser_name()+" 的请假申请失败,需修改后重新提交！");
            vacation.setStatus(BaseVO.APPROVAL_FAILED);
			variables.put("entity", vacation);
		}
		this.vacationService.update(vacation);
		// 完成任务
		this.taskService.complete(taskId, variables);
		redirectAttributes.addFlashAttribute("message", "任务办理完成！");
    	return "redirect:/processAction/doTaskList_page";
    }
    
    /**
     * 调整请假申请
     * @param vacation
     * @param results
     * @param taskId
     * @param session
     * @param reApply
     * @param model
     * @return
     * @throws Exception 
     */
	@RequestMapping(value = "/modifyVacation/{taskId}", method = RequestMethod.POST)
	public String modifyVacation(
			@ModelAttribute("vacation") @Valid Vacation vacation,
			BindingResult results,
			@PathVariable("taskId") String taskId,
			@RequestParam("reApply") Boolean reApply,
			RedirectAttributes redirectAttributes,
			HttpSession session,
			Model model
			) throws Exception {
		
		if(results.hasErrors()){
        	model.addAttribute("vacation", vacation);
        	return "vacation/modify_vacation";
        }
		
		User user = UserUtil.getUserFromSession(session);
        
        // 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
        if (user == null || user.getId() == null) {
        	model.addAttribute("msg", "登录超时，请重新登录!");
            return "login";
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        if(reApply){
        	//修改请假申请
	        vacation.setUserId(user.getId());
	        vacation.setUser_name(user.getName());
	        vacation.setTitle(user.getName()+" 的请假申请！");
	        vacation.setBusinessType(BaseVO.VACATION);
	        vacation.setStatus(BaseVO.PENDING);
	        vacation.setApplyDate(new Date());
	        vacation.setBusinessKey(vacation.getId().toString());
	        this.vacationService.update(vacation);
	        variables.put("entity", vacation);
	        if(vacation.getDays() <= 3){
            	variables.put("auditGroup", "manager");
            }else{
            	variables.put("auditGroup", "director");
            }
	        redirectAttributes.addFlashAttribute("message", "任务办理完成，请假申请已重新提交！");
        }else{
        	redirectAttributes.addFlashAttribute("message", "任务办理完成，已经取消您的请假申请！");
        }
		variables.put("reApply", reApply);
    	this.taskService.complete(taskId, variables);
		
    	return "redirect:/processAction/doTaskList_page";
    }
}
