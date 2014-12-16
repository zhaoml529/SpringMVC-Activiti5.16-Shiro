package com.zml.oa.service.activiti;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.zml.oa.entity.BaseVO;
import com.zml.oa.entity.CommentVO;
import com.zml.oa.entity.ExpenseAccount;
import com.zml.oa.entity.Salary;
import com.zml.oa.entity.SalaryAdjust;
import com.zml.oa.entity.User;
import com.zml.oa.entity.Vacation;
import com.zml.oa.pagination.Pagination;
import com.zml.oa.pagination.PaginationThreadUtils;
import com.zml.oa.service.IExpenseService;
import com.zml.oa.service.IProcessService;
import com.zml.oa.service.ISalaryAdjustService;
import com.zml.oa.service.ISalaryService;
import com.zml.oa.service.IUserService;
import com.zml.oa.service.IVacationService;
import com.zml.oa.util.BeanUtils;

/**
 * 流程相关Service
 * @author zml
 *
 */
@Service
@Transactional
public class ProcessServiceImp implements IProcessService{

	private static final Logger logger = Logger.getLogger(ProcessServiceImp.class);
	
	@Autowired
	protected RuntimeService runtimeService;
	
    @Autowired
    protected IdentityService identityService;
    
    @Autowired
    protected TaskService taskService;
    
    @Autowired
    protected RepositoryService repositoryService;
    
    @Autowired
    protected HistoryService historyService;
    
	@Autowired
	protected IUserService userService;
	
    @Autowired
    ProcessEngineFactoryBean processEngine;

    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;
    
    @Autowired
    protected WorkflowService traceService;
    
	@Autowired
	private IVacationService vacationService;
	
	@Autowired
	private IExpenseService expenseService;
	
	@Autowired
	private ISalaryAdjustService saService;
	
	@Autowired
	private ISalaryService salaryService;
	
    /**
     * 查询代办任务
     * @param user
     * @param model
     * @return
     */
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public List<BaseVO> findTodoTask(User user, Model model){
    	// 根据当前用户组查询
//      TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(userId);
		TaskQuery taskQuery = this.taskService.createTaskQuery().taskCandidateGroup(user.getGroup().getType());
		Integer totalSum = taskQuery.list().size();
		int[] pageParams = getPagination(totalSum, model);
		List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().listPage(pageParams[0], pageParams[1]);
		List<BaseVO> taskList = getBaseVOList(tasks);
		return taskList;
    }
    
    /**
     * 查询待受理任务
     * @param user
     * @param model
     * @return
     */
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public List<BaseVO> findDoTask(User user, Model model){
    	TaskQuery taskQuery = this.taskService.createTaskQuery().taskAssignee(user.getId().toString());
    	Integer totalSum = taskQuery.list().size();
		int[] pageParams = getPagination(totalSum, model);
		List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().listPage(pageParams[0], pageParams[1]);
		List<BaseVO> taskList = getBaseVOList(tasks);
		return taskList;
    }
    
    
    /**
     * 将Task集合转为BaseVO集合
     * @param tasks
     * @return
     */
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    protected List<BaseVO> getBaseVOList(List<Task> tasks) {
    	List<BaseVO> taskList = new ArrayList<BaseVO>();
        for (Task task : tasks) {
        	String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
            if(BeanUtils.isBlank(processInstance)){
            	//如果有挂起的流程则continue
            	continue;
            }
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
     * 查询流程定义对象
     *
     * @param processDefinitionId 流程定义ID
     * @return
     */
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    protected ProcessDefinition getProcessDefinition(String processDefinitionId) {
        ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        logger.info(processDefinition.getVersion());
        return processDefinition;
    }
    
    /**
     * 签收任务
     * @param user
     * @param taskId
     */
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public void doClaim(User user, String taskId){
    	this.identityService.setAuthenticatedUserId(user.getId().toString());
        this.taskService.claim(taskId, user.getId().toString());
    }
    
	
	/**
	 * 获取评论
	 * @param processInstanceId
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public List<CommentVO> getComments(String processInstanceId) throws Exception{
		// 查询一个任务所在流程的全部评论
		List<Comment> comments = this.taskService.getProcessInstanceComments(processInstanceId);
		List<CommentVO> commnetList = new ArrayList<CommentVO>();
		for(Comment comment : comments){
			User user = this.userService.getUserById(new Integer(comment.getUserId()));
			CommentVO vo = new CommentVO();
			vo.setContent(comment.getFullMessage());
			vo.setTime(comment.getTime());
			vo.setUserName(user.getName());
			commnetList.add(vo);
		}
    	return commnetList;
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
    
    
    /**
     * 显示流程图,带流程跟踪
     * @param processInstanceId
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public InputStream getDiagram(String processInstanceId){
    	ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        // 不使用spring请使用下面的两行代码
//    	ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
//    	Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

        // 使用spring注入引擎请使用下面的这行代码
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        //通过引擎生成png图片，并标记当前节点,并把当前节点用红色边框标记出来，弊端和直接部署流程文件生成的图片问题一样-乱码！。
        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
    	return imageStream;
    }
    
    /**
     * 显示图片，不带流程跟踪(没有乱码问题)
     * @param resourceType
     * @param processInstanceId
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public InputStream getDiagram_noTrace(String resourceType, String processInstanceId){
    	
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId())
                .singleResult();

        String resourceName = "";
        if (resourceType.equals("png") || resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        return resourceAsStream;
    }
    
    /**
     * 读取已结束中的流程(待完善)
     *
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public String findFinishedProcessInstaces(Model model) {
        HistoricProcessInstanceQuery historQuery = historyService.createHistoricProcessInstanceQuery().finished();
        Integer totalSum = historQuery.list().size();
		int[] pageParams = getPagination(totalSum, model);
		List<HistoricProcessInstance> list = historQuery.orderByProcessInstanceEndTime().desc().listPage(pageParams[0], pageParams[1]);
		System.out.println(list.size()+" $$$$$$$$$$$");
		
		// 关联业务实体
        for (HistoricProcessInstance historicProcessInstance : list) {
        	
        	String processInstanceId = historicProcessInstance.getId();
        	Map<String, Object> entity = historicProcessInstance.getProcessVariables();
        	System.out.println(entity.size()+" ***********");
        	//获取当前流程下的key为entity的variable
//            BaseVO base = (BaseVO) this.historyService.createHistoricVariableInstanceQuery().variableName(arg0).getVariable(processInstanceId, "entity");
        	System.out.println(processInstanceId+" $$$$$$$$$$$");
            List<HistoricVariableInstance> listHistory = this.historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();

           for(HistoricVariableInstance variableInstance : listHistory) {
             System.out.println("variable: " + variableInstance.getVariableName() + " = " + variableInstance.getValue());
           }

           List<HistoricDetail> formProperties = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId).formProperties().list();
          System.out.println("formProListSize: "+formProperties.size());
           for (HistoricDetail historicDetail : formProperties) {
             HistoricFormProperty field = (HistoricFormProperty) historicDetail;
             System.out.println("field id: " + field.getPropertyId() + ", value: " + field.getPropertyValue());
           }
            
//            String businessKey = historicProcessInstance.getBusinessKey();
//            Leave leave = leaveManager.getLeave(new Long(businessKey));
//            leave.setProcessDefinition(getProcessDefinition(historicProcessInstance.getProcessDefinitionId()));
//            leave.setHistoricProcessInstance(historicProcessInstance);
//            results.add(leave);
        }
//        model.addAttribute("tasklist", taskList);
//		model.addAttribute("taskType", BaseVO.CANDIDATE);
        return null;
    }
    
    /**
     * 查看正在运行的请假流程
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public List<BaseVO> listRuningVacation(User user) throws Exception{
    	List<Vacation> listVacation = this.vacationService.findByStatus(user.getId(), BaseVO.PENDING);
		List<BaseVO> result = new ArrayList<BaseVO>();
		if(listVacation != null ){
			for (Vacation vac : listVacation) {
				// 查询流程实例
				ProcessInstance pi = this.runtimeService
						.createProcessInstanceQuery()
						.processInstanceId(vac.getProcessInstanceId())
						.singleResult();
				Task task = this.taskService.createTaskQuery().processInstanceId(vac.getProcessInstanceId()).singleResult();
				if (pi != null) {
					// 查询流程参数
					BaseVO base = (BaseVO) this.runtimeService.getVariable(pi.getId(), "entity");
					base.setTask(task);
		            base.setProcessInstance(pi);
		            base.setProcessDefinition(getProcessDefinition(pi.getProcessDefinitionId()));
					
					result.add(base);
				}
			}
		}
		return result;
    }
    
    /**
     * 查看正在运行的报销流程
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public List<BaseVO> listRuningExpense(User user) throws Exception{
    	List<ExpenseAccount> listVacation = this.expenseService.findByStatus(user.getId(), BaseVO.PENDING);
		List<BaseVO> result = new ArrayList<BaseVO>();
		if(listVacation != null ){
			for (ExpenseAccount expense : listVacation) {
				// 查询流程实例
				ProcessInstance pi = this.runtimeService
						.createProcessInstanceQuery()
						.processInstanceId(expense.getProcessInstanceId())
						.singleResult();
				Task task = this.taskService.createTaskQuery().processInstanceId(expense.getProcessInstanceId()).singleResult();
				if (pi != null) {
					// 查询流程参数
					BaseVO base = (BaseVO) this.runtimeService.getVariable(pi.getId(), "entity");
					base.setTask(task);
		            base.setProcessInstance(pi);
		            base.setProcessDefinition(getProcessDefinition(pi.getProcessDefinitionId()));
					
					result.add(base);
				}
			}
		}
		return result;
    }
    
    /**
     * 查看正在运行的薪资跳转流程
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public List<BaseVO> listRuningSalaryAdjust(User user) throws Exception {
    	List<SalaryAdjust> listSalary = this.saService.findByStatus(user.getId(), BaseVO.PENDING);
    	List<BaseVO> result = new ArrayList<BaseVO>();
    	if(listSalary != null ){
    		for(SalaryAdjust sa : listSalary){
    			// 查询流程实例
				ProcessInstance pi = this.runtimeService
						.createProcessInstanceQuery()
						.processInstanceId(sa.getProcessInstanceId())
						.singleResult();
				Task task = this.taskService.createTaskQuery().processInstanceId(sa.getProcessInstanceId()).singleResult();
				if (pi != null) {
					// 查询流程参数
					BaseVO base = (BaseVO) this.runtimeService.getVariable(pi.getId(), "entity");
					base.setTask(task);
		            base.setProcessInstance(pi);
		            base.setProcessDefinition(getProcessDefinition(pi.getProcessDefinitionId()));
					result.add(base);
				}
    		}
    	}
    	return result;
	}
    

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public String startSalaryAdjust(SalaryAdjust salary) throws Exception {
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        this.identityService.setAuthenticatedUserId(salary.getUserId().toString());
        Salary sa = this.salaryService.findByUserId(salary.getUserId().toString());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("entity", salary);
        variables.put("auditGroup", "director");		//总监组审批
        variables.put("businessKey", salary.getId());
        variables.put("baseMoney", sa.getBaseMoney());  //原有薪金(回滚用)
        String businessKey = salary.getBusinessKey();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("com.zml.oa.salary", businessKey, variables);
        String processInstanceId = processInstance.getId();
        salary.setProcessInstanceId(processInstanceId);
        this.saService.doUpdate(salary);
        logger.info("processInstanceId: "+processInstanceId);
        //最后要设置null，就是这么做，还没研究为什么
        this.identityService.setAuthenticatedUserId(null);
        return processInstanceId;
	}

	@Override
	public String startVacation(Vacation vacation) throws Exception {
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        identityService.setAuthenticatedUserId(vacation.getUserId().toString());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("entity", vacation);
        if(vacation.getDays() <= 3){
        	variables.put("auditGroup", "manager");
        }else{
        	variables.put("auditGroup", "director");
        }
        String businessKey = vacation.getBusinessKey();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("com.zml.oa.vacation", businessKey, variables);
        String processInstanceId = processInstance.getId();
        vacation.setProcessInstanceId(processInstanceId);
        this.vacationService.update(vacation);

        logger.info("processInstanceId: "+processInstanceId);
        //最后要设置null，就是这么做，还没研究为什么
        this.identityService.setAuthenticatedUserId(null);
        return processInstanceId;
	}

	@Override
	public String startExpense(ExpenseAccount expense) throws Exception {
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        this.identityService.setAuthenticatedUserId(expense.getUserId().toString());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("entity", expense);
        variables.put("auditGroup", "finance");	//财务组审批
        String businessKey = expense.getBusinessKey();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("com.zml.oa.expense", businessKey, variables);
        String processInstanceId = processInstance.getId();
        expense.setProcessInstanceId(processInstanceId);
        this.expenseService.update(expense);

        logger.info("processInstanceId: "+processInstanceId);
        //最后要设置null，就是这么做，还没研究为什么
        this.identityService.setAuthenticatedUserId(null);
        return processInstanceId;
	}

	@Override
	public void complete(String taskId, String content, String userid, Map<String, Object> variables) {
		Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		// 根据任务查询流程实例
    	String processInstanceId = task.getProcessInstanceId();
    	ProcessInstance pi = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		//评论人的id  一定要写，不然查看的时候会报错，没有用户
    	this.identityService.setAuthenticatedUserId(userid);
		// 添加评论
    	if(content != null){
    		this.taskService.addComment(taskId, pi.getId(), content);
    	}
		// 完成任务
		this.taskService.complete(taskId, variables);
	}

	@Override
	public List<ProcessInstance> listRuningProcess(Model model) throws Exception {
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		Integer totalSum = processInstanceQuery.list().size();
		int[] pageParams = getPagination(totalSum, model);
		List<ProcessInstance> list = processInstanceQuery.orderByProcessInstanceId().desc().listPage(pageParams[0], pageParams[1]);
		return list;
	}

	@Override
	public void activateProcessInstance(String processInstanceId)
			throws Exception {
		runtimeService.activateProcessInstanceById(processInstanceId);
	}

	@Override
	public void suspendProcessInstance(String processInstanceId)
			throws Exception {
		runtimeService.suspendProcessInstanceById(processInstanceId);
	}

}
