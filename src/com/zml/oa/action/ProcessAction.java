package com.zml.oa.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zml.oa.entity.BaseVO;
import com.zml.oa.entity.User;
import com.zml.oa.pagination.Pagination;
import com.zml.oa.pagination.PaginationThreadUtils;
import com.zml.oa.service.IUserService;
import com.zml.oa.util.UserUtil;

/**
 * 流程控制类
 * @author ZML
 *
 */
@Controller
@RequestMapping("/processAction")
public class ProcessAction {
	private static final Logger logger = Logger.getLogger(VacationAction.class);
	
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
    
    /**
	 * 查询待办任务
	 * @param session
	 * @param redirectAttributes
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/todoTaskList_page", method = {RequestMethod.POST, RequestMethod.GET})
	public String todoTaskList_page(HttpSession session, Model model) throws Exception{
		String userId = UserUtil.getUserFromSession(session).getId().toString();
		User user = this.userService.getUserById(new Integer(userId));
		// 根据当前用户组查询
//      TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(userId);
		TaskQuery taskQuery = this.taskService.createTaskQuery().taskCandidateGroup(user.getGroup().getType());
		Integer totalSum = taskQuery.list().size();
		int[] pageParams = getPagination(totalSum, model);
		List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().listPage(pageParams[0], pageParams[1]);
		List<BaseVO> taskList = getBaseVOList(tasks);
        model.addAttribute("tasklist", taskList);
		model.addAttribute("taskType", BaseVO.CANDIDATE);
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
    @RequestMapping(value="/doTaskList_page", method = {RequestMethod.POST, RequestMethod.GET})
    public String doTaskList_page(HttpSession session, Model model) throws NumberFormatException, Exception{
    	User user = UserUtil.getUserFromSession(session);
    	TaskQuery taskQuery = this.taskService.createTaskQuery().taskAssignee(user.getId().toString());
    	Integer totalSum = taskQuery.list().size();
        //计算分页
		int[] pageParams = getPagination(totalSum, model);
		List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().listPage(pageParams[0], pageParams[1]);
		List<BaseVO> taskList = getBaseVOList(tasks);
        model.addAttribute("tasklist", taskList);
		model.addAttribute("taskType", BaseVO.ASSIGNEE);
		return "task/list_task";
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
        return "redirect:/processAction/todoTaskList_page";
	}
    
    
    /**
     * 将Task集合转为BaseVO集合
     * @param tasks
     * @return
     */
    protected List<BaseVO> getBaseVOList(List<Task> tasks) {
    	List<BaseVO> taskList = new ArrayList<BaseVO>();
        for (Task task : tasks) {
        	String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
          //获取当前流程下的key为entity的variable
            BaseVO base = (BaseVO) this.runtimeService.getVariable(processInstance.getId(), "entity");
            base.setTask(task);
            base.setProcessInstance(processInstance);
            base.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
            taskList.add(base);
        }
    	return taskList;
    }
    
    /**
     * 计算分页
     * @param totalSum
     * @return
     */
    protected int[] getPagination(Integer totalSum, Model model){
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
		model.addAttribute("page", pagination.getPageStr());
		return new int[]{firstResult, maxResult};
    }
}
