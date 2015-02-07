package com.zml.oa.action;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.runtime.ProcessInstance;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zml.oa.entity.BaseVO;
import com.zml.oa.entity.User;
import com.zml.oa.pagination.Pagination;
import com.zml.oa.pagination.PaginationThreadUtils;
import com.zml.oa.service.IProcessService;
import com.zml.oa.service.IUserService;
import com.zml.oa.service.activiti.WorkflowService;
import com.zml.oa.util.UserUtil;

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
	
    
    /**
	 * 查询待办任务
	 * @param session
	 * @param redirectAttributes
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:task:todoTask")
	@RequestMapping(value = "/todoTaskList_page", method = {RequestMethod.POST, RequestMethod.GET})
	public String todoTaskList_page(HttpSession session, Model model) throws Exception{
		String userId = UserUtil.getUserFromSession(session).getId().toString();
		User user = this.userService.getUserById(new Integer(userId));
		List<BaseVO> taskList = this.processService.findTodoTask(user, model);
        model.addAttribute("tasklist", taskList);
		model.addAttribute("taskType", BaseVO.CANDIDATE);
		return "task/list_task";
	}
	
    
    /**
     * 查询受理任务列表
     * @param session
     * @param model
     * @return
     * @throws NumberFormatException
     * @throws Exception
     */
	@RequiresPermissions("user:task:doTask")
    @RequestMapping(value="/doTaskList_page", method = {RequestMethod.POST, RequestMethod.GET})
    public String doTaskList_page(HttpSession session, Model model) throws NumberFormatException, Exception{
    	User user = UserUtil.getUserFromSession(session);
    	List<BaseVO> taskList = this.processService.findDoTask(user, model);
        model.addAttribute("tasklist", taskList);
		model.addAttribute("taskType", BaseVO.ASSIGNEE);
		return "task/list_task";
    }
    
	/**
	 * 签收任务
	 * @return
	 */
	@RequiresPermissions("user:task:claim")
	@RequestMapping("/claim/{taskId}")
	public String claim(@PathVariable("taskId") String taskId, HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
		User user = UserUtil.getUserFromSession(session);
		this.processService.doClaim(user, taskId);
        redirectAttributes.addFlashAttribute("message", "任务已签收");
        return "redirect:/processAction/todoTaskList_page";
	}
    
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
     * 显示图片，不带流程跟踪(没有乱码问题)
     *
     * @param resourceType      资源类型(xml|image)
     * @param processInstanceId 流程实例ID
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/process/process-instance")
    public void loadByProcessInstance(@RequestParam("type") String resourceType, @RequestParam("pid") String processInstanceId, HttpServletResponse response)
            throws Exception {
        InputStream resourceAsStream = this.processService.getDiagram_noTrace(resourceType, processInstanceId);
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
        List<Map<String, Object>> activityInfos = traceService.traceProcess(processInstanceId);
        return activityInfos;
    }
    
    
    /**
     * 读取已结束的流程
     *
     * @return
     * @throws Exception 
     */
    @RequiresPermissions("user:process:finished")
    @RequestMapping(value = "/process/finished")
    public String findFinishedProcessInstaces(Model model) throws Exception {
        //待完成，见ProcessService
    	this.processService.findFinishedProcessInstaces(model);
        return null;
    }
    
    /**
     * 读取运行中的流程
     * @param businessType
     * @param session
     * @param model
     * @return
     * @throws Exception
     */
    @RequiresPermissions("user:process:running*") //process:vacation,salary,expense:running
    @RequestMapping(value="/process/runingProcessInstance/{businessType}/list_page")
    public String getRuningProcessInstance(@PathVariable("businessType") String businessType,HttpSession session , Model model) throws Exception{
    	User user = UserUtil.getUserFromSession(session);
    	List<BaseVO> baseVO = null;
    	if(BaseVO.VACATION.equals(businessType)){
    		//请假
    		baseVO = this.processService.listRuningVacation(user);
    		model.addAttribute("businessType", BaseVO.VACATION);
    	}else if(BaseVO.SALARY.equals(businessType)){
    		//调薪
    		baseVO = this.processService.listRuningSalaryAdjust(user);
    		model.addAttribute("businessType", BaseVO.SALARY);
    	}else if(BaseVO.EXPENSE.equals(businessType)){
    		//报销
    		baseVO = this.processService.listRuningExpense(user);
    		model.addAttribute("businessType", BaseVO.EXPENSE);
    	}
    	Pagination pagination = PaginationThreadUtils.get();
		model.addAttribute("page", pagination.getPageStr());
    	model.addAttribute("baseList", baseVO);
    	return "apply/list_running";
    }
    /**
     * 管理运行中的流程
     * @param model
     * @return
     * @throws Exception
     */
    @RequiresPermissions("admin:process:*")
    @RequestMapping(value="/process/runningProcess_page")
    public String listRuningProcess(Model model) throws Exception{
    	List<ProcessInstance> list = this.processService.listRuningProcess(model);
    	model.addAttribute("list", list);
		return "workflow/running_manage";
    }
    
    /**
     * 激活、挂起流程实例
     * @param status
     * @param processInstanceId
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequiresPermissions("admin:process:suspend,active")
    @RequestMapping(value = "process/updateProcessStatus/{status}/{processInstanceId}")
    public String updateProcessStatus(
    		@PathVariable("status") String status, 
    		@PathVariable("processInstanceId") String processInstanceId,
            RedirectAttributes redirectAttributes) throws Exception{
    	
    	if (status.equals("active")) {
    		this.processService.activateProcessInstance(processInstanceId);
            redirectAttributes.addFlashAttribute("message", "已激活ID为[ " + processInstanceId + " ]的流程实例。");
        } else if (status.equals("suspend")) {
        	this.processService.suspendProcessInstance(processInstanceId);
            redirectAttributes.addFlashAttribute("message", "已挂起ID为[ " + processInstanceId + " ]的流程实例。");
        }
    	return "redirect:/processAction/process/runningProcess_page";
    }
}
