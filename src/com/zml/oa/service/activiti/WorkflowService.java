package com.zml.oa.service.activiti;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zml.oa.entity.ExpenseAccount;
import com.zml.oa.entity.Salary;
import com.zml.oa.entity.SalaryAdjust;
import com.zml.oa.entity.User;
import com.zml.oa.service.IGroupService;
import com.zml.oa.service.ISalaryService;
import com.zml.oa.service.IUserService;
import com.zml.oa.util.BeanUtils;
import com.zml.oa.util.WorkflowUtils;

/**
 * 工作流跟踪相关Service
 * @Component注解(把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>)
 * 目的是让Spring容器管理workflowService，这样@Autowired 才能自动注入
 * @author zml
 */
@Component
public class WorkflowService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected IdentityService identityService;
    
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ISalaryService salaryService;
	
	@Autowired
	private IGroupService groupService;

	
	
    /**
     * 检查付款金额
     * 此方法演示使用ServiceTask中的activiti:expression属性配合EL表达式执行java方法
     * @param exe
     */
    public void bankTransfer(Execution exe) {
		// 具体业务会与第三方支付系统产生交互，这样就有可能产生请求发送失败，
		// 用边界错误事件来处理，如果银行转账失败，流程则会到达现金支付的用户任务
		// ExpenseAccount.bpmn流程文件中，错误边界事件可以通过workflowService调用此方法，也是由于@Component的作用.
		// 如果此方法是放在@Service中，则需要在流程参数中加入variables.put("workflowService", new WorkflowService())，这样才能通过el获取到bankTransfer();
    	ExpenseAccount expense = (ExpenseAccount)this.runtimeService.getVariable(exe.getProcessInstanceId(), "entity");

		if (expense.getMoney().compareTo(new BigDecimal(1000)) == 1 ) {
			System.out.println("银行转账失败,大于1000块，转为现金支付！");
			throw new BpmnError("to much");
		} else {
			//具体银行转账业务，调用第三方服务
			System.out.println("银行转帐成功");
		}
	}
    
    /**
     * 回滚薪资调整
     * @param exe
     * @throws Exception 
     */
    public void rollbackApply(Execution exe) throws Exception{
    	SalaryAdjust salaryAdjust = (SalaryAdjust)this.runtimeService.getVariable(exe.getProcessInstanceId(), "entity");
    	BigDecimal baseMoney = (BigDecimal) this.runtimeService.getVariable(exe.getProcessInstanceId(), "baseMoney");
		Salary salary = this.salaryService.findByUserId(salaryAdjust.getUserId().toString());
		salary.setBaseMoney(baseMoney);
		this.salaryService.doUpdate(salary);
    }
    
    /**
     * 记录薪资调整
     * @param exe
     * @throws Exception
     */
	public void contentSalary(Execution exe) throws Exception {
		SalaryAdjust salaryAdjust = (SalaryAdjust) this.runtimeService.getVariable(exe.getProcessInstanceId(), "entity");
		Salary salary = this.salaryService.findByUserId(salaryAdjust.getUserId().toString());
		BigDecimal newMoney = salaryAdjust.getAdjustMoney();
		salary.setBaseMoney(newMoney);
		this.salaryService.doUpdate(salary);
	}
	
	/**
     * 流程跟踪图
     *
     * @param processInstanceId 流程实例ID
     * @return 封装了各种节点信息
     */
    public List<Map<String, Object>> traceProcess(String processInstanceId) throws Exception {
        Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();//执行实例
        Object property = PropertyUtils.getProperty(execution, "activityId");
        String activityId = "";
        if (property != null) {
            activityId = property.toString();
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
        List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点

        List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
        for (ActivityImpl activity : activitiList) {

            boolean currentActiviti = false;
            String id = activity.getId();
            
            // 当前节点
            if (id.equals(activityId)) {
                currentActiviti = true;
            }

            Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, processInstance, currentActiviti);

            activityInfos.add(activityImageInfo);
        }

        return activityInfos;
    }

    /**
     * 封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述
     *
     * @param activity
     * @param processInstance
     * @param currentActiviti
     * @return
     */
    private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, ProcessInstance processInstance,
                                                          boolean currentActiviti) throws Exception {
        Map<String, Object> vars = new HashMap<String, Object>();
        Map<String, Object> activityInfo = new HashMap<String, Object>();
        activityInfo.put("currentActiviti", currentActiviti);
        setPosition(activity, activityInfo);
        setWidthAndHeight(activity, activityInfo);

        Map<String, Object> properties = activity.getProperties();
        vars.put("任务类型", WorkflowUtils.parseToZhType(properties.get("type").toString()));

        ActivityBehavior activityBehavior = activity.getActivityBehavior();
        logger.info("activityBehavior={}", activityBehavior);
        if (activityBehavior instanceof UserTaskActivityBehavior) {

            Task currentTask = null;

			/*
             * 当前节点的task
			 */
            if (currentActiviti) {
                currentTask = getCurrentTaskInfo(processInstance);
            }

			/*
			 * 当前任务的分配角色
			 */
            UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
            TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
            
            //任务所属角色
            String taskDefinKey = taskDefinition.getKey();
            logger.info("taskDefinKey: "+taskDefinKey);
            if(taskDefinKey.startsWith("director")){
            	vars.put("任务所属角色", "总监组");
            }else if(taskDefinKey.startsWith("finance")){
            	vars.put("任务所属角色", "财务组");
            }else if(taskDefinKey.startsWith("hr")){
            	vars.put("任务所属角色", "人事组");
            }else if(taskDefinKey.startsWith("manager")){
            	vars.put("任务所属角色", "经理组");
            }
            
            //当前处理人
	         if (currentTask != null) {
	             setCurrentTaskAssignee(vars, currentTask);
	         }
	         
//            Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
//            if (!candidateGroupIdExpressions.isEmpty()) {
//
//                // 任务的处理角色
//            	setTaskGroup(vars, candidateGroupIdExpressions);
//            	vars.put("任务所属角色", "-待完成-");
//            	
//            	
//                // 当前处理人
//                if (currentTask != null) {
//                    setCurrentTaskAssignee(vars, currentTask);
//                }
//            }
        }

        vars.put("节点说明", properties.get("documentation"));
        logger.info("properties: "+properties);
        String description = activity.getProcessDefinition().getDescription();
        vars.put("描述", description);
        logger.info("trace variables: {}", vars);
        activityInfo.put("vars", vars);
        return activityInfo;
    }

//    private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
//        String roles = "";
//        for (Expression expression : candidateGroupIdExpressions) {
//            String expressionText = expression.getExpressionText();
//            logger.info("expressionText: "+expressionText);
//            isCondition("auditGroup", expressionText, "director");
//            //自带group用这个
//            String roleName = identityService.createGroupQuery().groupId(expressionText).singleResult().getName();
//            roles += roleName;
//        }
//        vars.put("任务所属角色", roles);
//    }

    /**
     * 设置当前处理人信息
     *
     * @param vars
     * @param currentTask
     * @throws Exception 
     * @throws NumberFormatException 
     */
    private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) throws NumberFormatException, Exception {
        String assignee = currentTask.getAssignee();
        logger.info("assignee: "+assignee);
        if (assignee != null) {
        	User assigneeUser = this.userService.getUserById(new Integer(assignee));
        	if(!BeanUtils.isBlank(assigneeUser)){
        		vars.put("当前处理人", assigneeUser.getName());
        	}else{
        		vars.put("当前处理人", "不存在！");
        	}
            
            logger.info("当前处理人: "+assigneeUser);
        }
    }

    /**
     * 获取当前节点信息
     *
     * @param processInstance
     * @return
     */
    public Task getCurrentTaskInfo(ProcessInstance processInstance) {
        Task currentTask = null;
        try {
            String activitiId = (String) PropertyUtils.getProperty(processInstance, "activityId");
            logger.info("current activity id: {}", activitiId);

            currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(activitiId)
                    .singleResult();
            logger.info("current task for processInstance: {}", ToStringBuilder.reflectionToString(currentTask));

        } catch (Exception e) {
            logger.error("can not get property activityId from processInstance: {}", processInstance);
        }
        return currentTask;
    }

    /**
     * 设置宽度、高度属性
     *
     * @param activity
     * @param activityInfo
     */
    private void setWidthAndHeight(ActivityImpl activity, Map<String, Object> activityInfo) {
        activityInfo.put("width", activity.getWidth());
        activityInfo.put("height", activity.getHeight());
    }

    /**
     * 设置坐标位置
     *
     * @param activity
     * @param activityInfo
     */
    private void setPosition(ActivityImpl activity, Map<String, Object> activityInfo) {
        activityInfo.put("x", activity.getX());
        activityInfo.put("y", activity.getY());
    }
    
    
    public String isCondition(String key, String el, String value) {  
        ExpressionFactory factory = new ExpressionFactoryImpl();    
        SimpleContext context = new SimpleContext();    
        context.setVariable(key, factory.createValueExpression(value, String.class));    
        ValueExpression e = factory.createValueExpression(context, el, String.class);  
        logger.info("el info key: "+key+" el: "+el+" value: "+value+" e: "+e+" e.value: "+e.getValue(context));
        return (String) e.getValue(context);  
    }  
    
    
}
