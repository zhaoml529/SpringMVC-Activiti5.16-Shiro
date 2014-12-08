package com.zml.oa.service.activiti;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zml.oa.entity.BaseVO;
import com.zml.oa.entity.ExpenseAccount;
import com.zml.oa.entity.User;
import com.zml.oa.entity.Vacation;
import com.zml.oa.pagination.Pagination;
import com.zml.oa.pagination.PaginationThreadUtils;
import com.zml.oa.service.IExpenseService;
import com.zml.oa.service.IUserService;
import com.zml.oa.service.IVacationService;

/**
 * 流程相关Service
 * @author zml
 *
 */
@Component
@Transactional
public class ProcessService {

	private static final Logger logger = Logger.getLogger(ProcessService.class);
	
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
    protected WorkflowTraceService traceService;
    
	@Autowired
	private IVacationService vacationService;
	
	@Autowired
	private IExpenseService expenseService;
    
    
    /**
     * 查询代办任务
     * @param user
     * @param model
     * @return
     */
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
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public void doClaim(User user, String taskId){
    	this.identityService.setAuthenticatedUserId(user.getId().toString());
        this.taskService.claim(taskId, user.getId().toString());
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
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public String findFinishedProcessInstaces(Model model) {
        List<BaseVO> results = new ArrayList<BaseVO>();
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
     * 检查付款金额
     * @param exe
     */
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public void bankTransfer(Execution exe) {
		ExpenseAccount expense = (ExpenseAccount)this.runtimeService.getVariable(exe.getProcessInstanceId(), "entity");
		// 具体业务会与第三方支付系统产生交互，这样就有可能产生请求发送失败，
		// 用边界错误事件来处理，如果银行转账失败，流程则会到达现金支付的用户任务
		
		if (expense.getMoney().compareTo(new BigDecimal(1000)) == 1 ) {
			System.out.println("银行转账失败");
			throw new BpmnError("to much");
		} else {
			//具体业务
			System.out.println("银行转帐成功");
		}
	}
}
