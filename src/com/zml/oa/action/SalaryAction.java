package com.zml.oa.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.zml.oa.entity.SalaryAdjust;
import com.zml.oa.entity.User;
import com.zml.oa.service.IProcessService;
import com.zml.oa.service.ISalaryAdjustService;
import com.zml.oa.service.IUserService;
import com.zml.oa.util.BeanUtils;
import com.zml.oa.util.UserUtil;

/**
 * 薪资调整控制类
 * @author ZML
 *
 */

@Controller
@RequestMapping("/salaryAction")
public class SalaryAction {
	private static final Logger logger = Logger.getLogger(ExpenseAction.class);
	
	@Autowired
	private ISalaryAdjustService saService;
	
	@Autowired
	protected RuntimeService runtimeService;
	
    @Autowired
    protected IdentityService identityService;
    
    @Autowired
    protected HistoryService historyService;
    
    @Autowired
    protected TaskService taskService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IProcessService processService;
	
	
	/**
	 * 跳转添加页面
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user:salary:toAdd")
	@RequestMapping(value = "/toAdd", method = RequestMethod.GET)
	public ModelAndView toAdd(Model model){
		if(!model.containsAttribute("salary")) {
            model.addAttribute("salary", new SalaryAdjust());
        }
		return new ModelAndView("salary/add_salaryAdjust").addObject(model);
	}

	/**
	 * 详细信息
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:salary:details")
	@RequestMapping(value="/details/{id}", method = RequestMethod.GET)
	public String details(@PathVariable("id") Integer id, Model model) throws Exception{
		SalaryAdjust salaryAd = this.saService.findById(id);
		model.addAttribute("salary", salaryAd);
		return "/salary/details_salary";
	}
	
	/**
	 * 添加并启动薪资调整流程
	 * @param salary
	 * @param results
	 * @param redirectAttributes
	 * @param session
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:salary:doAdd")
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	public String doAdd(
			@ModelAttribute("salary") @Valid SalaryAdjust salary,BindingResult results, 
			RedirectAttributes redirectAttributes, 
			HttpSession session, 
			Model model) throws Exception{
        User currentUser = UserUtil.getUserFromSession(session);
        
        if(results.hasErrors()){
        	model.addAttribute("salary", salary);
        	return "salary/add_salary";
        }
        
        // 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
        if (currentUser == null || currentUser.getId() == null) {
        	model.addAttribute("msg", "登录超时，请重新登录!");
            return "login";
        }
        
        String userName = salary.getUser_name();
        User user = this.userService.getUserByName(userName);
        if(!BeanUtils.isBlank(user)){
        	if(user.getName().equals(currentUser.getName())){
		        salary.setApplyDate( new Date() );
		        salary.setUserId(user.getId());
		        salary.setUser_id(currentUser.getId());
		        salary.setUser_name(currentUser.getName());
		        salary.setTitle(user.getName()+" 的薪资调整申请");
		        salary.setBusinessType(SalaryAdjust.SALARY);
		        salary.setStatus(SalaryAdjust.PENDING);
		        this.saService.doAdd(salary);
		        String businessKey = salary.getId().toString();
		        salary.setBusinessKey(businessKey);
		        try{
		        	String processInstanceId = this.processService.startSalaryAdjust(salary);
		            redirectAttributes.addFlashAttribute("message", "流程已启动，流程ID：" + processInstanceId);
		            logger.info("processInstanceId: "+processInstanceId);
		        }catch (ActivitiException e) {
		            if (e.getMessage().indexOf("no processes deployed with key") != -1) {
		                logger.warn("没有部署流程!", e);
		                redirectAttributes.addFlashAttribute("error", "没有部署流程，请在[工作流]->[流程管理]页面点击<重新部署流程>");
		            } else {
		                logger.error("启动薪资调整流程失败：", e);
		                redirectAttributes.addFlashAttribute("error", "系统内部错误！");
		            }
		        } catch (Exception e) {
		            logger.error("启动薪资调整流程失败：", e);
		            redirectAttributes.addFlashAttribute("error", "系统内部错误！");
		        } finally {
		            identityService.setAuthenticatedUserId(null);
		        }
        	}else{
        		redirectAttributes.addFlashAttribute("error", "申请错误，只能申请调整自己的薪资！");
        	}
        }else{
        	redirectAttributes.addFlashAttribute("error", "此用户不存在，不能调整薪资！");
        }
        return "redirect:/salaryAction/toAdd";
	}
	
	/**
     * 审批报销流程
     * @param taskId
     * @param model
     * @return
     * @throws NumberFormatException
     * @throws Exception
     */
	@RequiresPermissions("user:salary:toApproval")
    @RequestMapping("/toApproval/{taskId}")
    public String toApproval(@PathVariable("taskId") String taskId, Model model) throws NumberFormatException, Exception{
    	Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		// 根据任务查询流程实例
    	String processInstanceId = task.getProcessInstanceId();
		ProcessInstance pi = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		SalaryAdjust salary = (SalaryAdjust) this.runtimeService.getVariable(pi.getId(), "entity");
		salary.setTask(task);
		List<CommentVO> commentList = this.processService.getComments(processInstanceId);
		String taskDefinitionKey = task.getTaskDefinitionKey();
		logger.info("taskDefinitionKey: "+taskDefinitionKey);
		String result = null;
		if("modifyApply".equals(taskDefinitionKey)){
			result = "salary/modify_salaryAdjust";
		}else{
			result = "salary/audit_salaryAdjust";
		}
		
		model.addAttribute("commentList", commentList);
		model.addAttribute("salary", salary);
    	return result;
    }
    
	@RequiresPermissions("user:salary:complate")
    @RequestMapping("/complate/{taskId}")
    public String complate(
    		@RequestParam("salaryAdjustId") Integer salaryAdjustId,
    		@RequestParam("content") String content,
    		@RequestParam("completeFlag") Boolean completeFlag,
    		@PathVariable("taskId") String taskId, 
    		RedirectAttributes redirectAttributes,
    		HttpSession session) throws Exception{
    	User user = UserUtil.getUserFromSession(session);
    	String groupType = user.getGroup().getType();
    	SalaryAdjust salaryAdjust = this.saService.findById(salaryAdjustId);
    	SalaryAdjust baseSalary = (SalaryAdjust) this.runtimeService.getVariable(salaryAdjust.getProcessInstanceId(), "entity");
    	Map<String, Object> variables = new HashMap<String, Object>();
    	if("boss".equals(groupType) && completeFlag){
    		salaryAdjust.setStatus(BaseVO.APPROVAL_SUCCESS);
    	}else if(!completeFlag){
    		baseSalary.setTitle(baseSalary.getUser_name()+" 的申请失败，需修改后重新提交！");
    		salaryAdjust.setStatus(BaseVO.APPROVAL_FAILED);
    		variables.put("entity", baseSalary);
    	}
    	//如果用同一个salary，Hibernage报错a different object with the same identifier value was already associated with the session:
    	this.saService.doUpdate(salaryAdjust);
		variables.put("isPass", completeFlag);
		this.processService.complete(taskId, content, user.getId().toString(), variables);
		redirectAttributes.addFlashAttribute("message", "任务办理完成！");
		return "redirect:/processAction/todoTaskList_page";

    }
    
    /**
     * 修改薪资调整
     * @param salary
     * @param results
     * @param taskId
     * @param salaryId
     * @param reApply
     * @param processInstanceId
     * @param redirectAttributes
     * @param session
     * @param model
     * @return
     * @throws Exception 
     */
	@RequiresPermissions("user:salary:modify")
    @RequestMapping("/modifySalary/{taskId}")
    public String modifySalary(
    		@ModelAttribute("salary") @Valid SalaryAdjust salary,
			BindingResult results,
			@PathVariable("taskId") String taskId,
			@RequestParam("reApply") Boolean reApply,
			RedirectAttributes redirectAttributes,
			HttpSession session,
			Model model) throws Exception{
    	
    	
    	if(results.hasErrors()){
        	model.addAttribute("salary", salary);
        	return "salary/modify_salaryjust";
        }
		
		User currentUser = UserUtil.getUserFromSession(session);
        String userName = salary.getUser_name();
        User user = this.userService.getUserByName(userName);
        
        Map<String, Object> variables = new HashMap<String, Object>();
        
        
        
        
        
        
        
        
        
        
        
        
        if(reApply){
        	if(!BeanUtils.isBlank(user)){
        		if(user.getName().equals(currentUser.getName())){
		        	//修改薪资调整
		        	salary.setUserId(user.getId());
		        	salary.setUser_id(currentUser.getId());
		        	salary.setUser_name(currentUser.getName());
		        	salary.setTitle(user.getName()+" 的薪资调整申请！");
		        	salary.setBusinessType(BaseVO.SALARY);
		        	salary.setStatus(BaseVO.PENDING);
		        	salary.setApplyDate(new Date());
		        	salary.setBusinessKey(salary.getId().toString());
	//		        variables.put("auditGroup", "director");//返回总监重新审批
			        redirectAttributes.addFlashAttribute("message", "任务办理完成，薪资调整申请已重新提交！");
        		}else{
        			redirectAttributes.addFlashAttribute("error", "申请错误，只能申请调整自己的薪资！");
                	return "redirect:/salaryAction/toApproval/"+taskId;
        		}
        	}else{
            	redirectAttributes.addFlashAttribute("error", "此用户不存在，不能调整薪资！");
            	return "redirect:/salaryAction/toApproval/"+taskId;
            }
        }else{
        	salary.setUserId(user.getId());
        	salary.setUser_id(currentUser.getId());
        	salary.setUser_name(currentUser.getName());
        	salary.setTitle(user.getName()+" 的薪资调整申请已取消！");
        	salary.setBusinessType(BaseVO.SALARY);
        	salary.setStatus(BaseVO.APPROVAL_FAILED);
        	salary.setApplyDate(new Date());
        	salary.setBusinessKey(salary.getId().toString());
        	redirectAttributes.addFlashAttribute("message", "任务办理完成，已经取消您的薪资调整申请！");
        }
        this.saService.doUpdate(salary);
        variables.put("entity", salary);
        variables.put("reApply", reApply);
        //完成任务
        this.processService.complete(taskId, null, user.getId().toString(), variables);
        
        return "redirect:/processAction/todoTaskList_page";
    	
    }
    
}
