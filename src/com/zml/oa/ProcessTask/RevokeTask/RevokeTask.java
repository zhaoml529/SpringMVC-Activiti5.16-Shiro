package com.zml.oa.ProcessTask.RevokeTask;

import java.util.Date;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.HistoricActivityInstanceQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.zml.oa.service.activiti.WorkflowService;
import com.zml.oa.util.ApplicationContextHelper;

@Component
public class RevokeTask implements Command<Integer> {

	private static final Logger logger = LoggerFactory.getLogger(RevokeTask.class);
    private WorkflowService workflowService;
	
    private HistoryService historyService;
//	
//	@Autowired
//    private TaskService taskService;
	
    private RuntimeService runtimeService;

    
//	@Autowired
//    private JdbcTemplate jdbcTemplate;
	
	private String historyTaskId;
	
	private String processInstanceId;
	
	public RevokeTask(){
		
	}
	
	public RevokeTask(String historyTaskId, String processInstanceId, RuntimeService runtimeService, WorkflowService workflowService,
			HistoryService historyService){
		this.historyTaskId = historyTaskId;
		this.processInstanceId = processInstanceId;
		this.runtimeService = runtimeService;
		this.workflowService = workflowService;
		this.historyService = historyService;
	}
	
	/**
	 * 0-撤销成功 1-流程结束 2-下一结点已经通过,不能撤销
	 * @param historyTaskId
	 * @param processInstanceId
	 * @return
	 */
	@Override
	public Integer execute(CommandContext commandContext) {
//		HistoricTaskInstance historicTaskInstance = this.historyService.createHistoricTaskInstanceQuery().taskId(historyTaskId).singleResult();
    	HistoricTaskInstanceEntity historicTaskInstanceEntity = Context
                .getCommandContext()
                .getHistoricTaskInstanceEntityManager()
                .findHistoricTaskInstanceById(historyTaskId);
    	// 获得历史节点
//    	HistoricActivityInstance historicActivityInstance = getHistoricActivityInstance(historyTaskId);
        HistoricActivityInstanceEntity historicActivityInstanceEntity = getHistoricActivityInstanceEntity(historyTaskId);
    	
        //获取当前节点
        String currentTaskId = null;
        ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if(processInstance != null){
        	
//        	List<TaskEntity> list = Context.getCommandContext().getTaskEntityManager().findTasksByProcessInstanceId(processInstanceId);
        	
        	Task currentTask = this.workflowService.getCurrentTaskInfo(processInstance);
        	currentTaskId = currentTask.getId();
        	HistoricTaskInstance hti = this.historyService.createHistoricTaskInstanceQuery().taskId(currentTaskId).singleResult();
        	//下一结点已经通过,不能撤销。
        	if("completed".equals(hti.getDeleteReason())){
        		logger.info("cannot revoke {}", historyTaskId);
        		return 2;
        	}
        }else{
        	return 1;
        }
        // 删除所有活动中的task
        this.deleteActiveTasks(processInstance.getProcessInstanceId());
        this.deleteHistoryActivities(this.historyTaskId, this.processInstanceId);
        // 恢复期望撤销的任务和历史
        this.processHistoryTask(historicTaskInstanceEntity,
        		historicActivityInstanceEntity);

        logger.info("activiti is revoke {}", historicTaskInstanceEntity.getName());

        return 0;
	}

	
//    public Integer revoke(String historyTaskId, String processInstanceId) throws Exception{
//    	// 获得历史任务
//    	
//    	HistoricTaskInstance historicTaskInstance = this.historyService.createHistoricTaskInstanceQuery().taskId(historyTaskId).singleResult();
////    	HistoricTaskInstanceEntity historicTaskInstanceEntity = Context
////                .getCommandContext()
////                .getHistoricTaskInstanceEntityManager()
////                .findHistoricTaskInstanceById(historyTaskId);
//    	// 获得历史节点
//    	HistoricActivityInstance historicActivityInstance = getHistoricActivityInstance(historyTaskId);
////        HistoricActivityInstanceEntity historicActivityInstanceEntity = getHistoricActivityInstanceEntity(historyTaskId);
//    	
//        //获取当前节点
//        ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
//        if(processInstance != null){
//        	Task currentTask = this.workflowService.getCurrentTaskInfo(processInstance);
//        	String taskId = currentTask.getId();
//        	HistoricTaskInstance hti = this.historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
//        	//下一结点已经通过,不能撤销。
//        	if("completed".equals(hti.getDeleteReason())){
//        		logger.info("cannot revoke {}", historyTaskId);
//        		return 2;
//        	}
//        }else{
//        	return 1;
//        }
//        // 删除所有活动中的task
//        this.deleteActiveTasks(processInstanceId);
//        // 恢复期望撤销的任务和历史
//        this.processHistoryTask(historicTaskInstance,
//        		historicActivityInstance);
//
//        logger.info("activiti is revoke {}", historicTaskInstance.getName());
//
//        return 0;
//    	
//    }
    
    //新增
//    public HistoricActivityInstance getHistoricActivityInstance(String historyTaskId){
//    	JdbcTemplate jdbcTemplate = ApplicationContextHelper.getBean(JdbcTemplate.class);
//    	String historicActivityInstanceId = jdbcTemplate.queryForObject(
//                "select id_ from ACT_HI_ACTINST where task_id_=?",
//                String.class, historyTaskId);
//        logger.info("historicActivityInstanceId : {}",historicActivityInstanceId);
//
//    	HistoricActivityInstance hai = this.historyService.createHistoricActivityInstanceQuery().activityInstanceId(historicActivityInstanceId).singleResult();
//    	return hai;
//    }
    
    //报错
    public HistoricActivityInstanceEntity getHistoricActivityInstanceEntity(
            String historyTaskId) {
    	JdbcTemplate jdbcTemplate = ApplicationContextHelper.getBean(JdbcTemplate.class);
        String historicActivityInstanceId = jdbcTemplate.queryForObject(
                "select id_ from ACT_HI_ACTINST where task_id_=?",
                String.class, historyTaskId);
        logger.info("historicActivityInstanceId : {}",historicActivityInstanceId);

        HistoricActivityInstanceQueryImpl historicActivityInstanceQueryImpl = new HistoricActivityInstanceQueryImpl();
        historicActivityInstanceQueryImpl.activityInstanceId(historicActivityInstanceId);

        HistoricActivityInstanceEntity historicActivityInstanceEntity = (HistoricActivityInstanceEntity) Context
                .getCommandContext()
                .getHistoricActivityInstanceEntityManager()
                .findHistoricActivityInstancesByQueryCriteria(
                        historicActivityInstanceQueryImpl, new Page(0, 1))
                .get(0);

        return historicActivityInstanceEntity;
    }
    
    /**
     * 删除未完成任务.
     */
    public void deleteActiveTasks(String processInstanceId) {
        Context.getCommandContext().getTaskEntityManager()
                .deleteTasksByProcessInstanceId(processInstanceId, "revoke", true);
        
        
//       List<Task> taskList = this.taskService.createTaskQuery().processInstanceId(processInstanceId).list();
//       for(Task task : taskList){
//    	   this.taskService.deleteTask(task.getId(), true);
//    	   //this.jdbcTemplate.update("delete from ACT_RU_IDENTITYLINK where task_id_=?", task.getId());
//       }
    }
    
    /**
     * 删除历史节点.
     */
    public void deleteHistoryActivities(String historyTaskId, String processInstanceId) {
    	JdbcTemplate jdbcTemplate = ApplicationContextHelper.getBean(JdbcTemplate.class);
    	List<HistoricActivityInstance> list = this.historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
    	
    	for(HistoricActivityInstance hai : list){
    		String taskId = hai.getTaskId();
    		if(taskId != null && !taskId.equals(historyTaskId)){
    			//删除历史任务
//    			this.historyService.deleteHistoricTaskInstance(taskId);
    			
    			Context.getCommandContext()
                .getHistoricTaskInstanceEntityManager()
                .deleteHistoricTaskInstanceById(taskId);
    			//删除历史行为
    			jdbcTemplate.update("delete from ACT_HI_ACTINST where task_id_=?", taskId);
    		}
    		
    	}
    }
    
    /**
     * 恢复任务
     * @param historicTaskInstanceEntity
     * @param historicActivityInstanceEntity
     */
    public void processHistoryTask(
            HistoricTaskInstanceEntity historicTaskInstanceEntity,
            HistoricActivityInstanceEntity historicActivityInstanceEntity) {
        historicTaskInstanceEntity.setEndTime(null);
        historicTaskInstanceEntity.setDurationInMillis(null);
        historicActivityInstanceEntity.setEndTime(null);
        historicActivityInstanceEntity.setDurationInMillis(null);
        
        TaskEntity task = TaskEntity.create(new Date());
        task.setProcessDefinitionId(historicTaskInstanceEntity
                .getProcessDefinitionId());
        task.setId(historicTaskInstanceEntity.getId());
        task.setAssigneeWithoutCascade(historicTaskInstanceEntity.getAssignee());
        task.setParentTaskIdWithoutCascade(historicTaskInstanceEntity
                .getParentTaskId());
        task.setNameWithoutCascade(historicTaskInstanceEntity.getName());
        task.setTaskDefinitionKey(historicTaskInstanceEntity
                .getTaskDefinitionKey());
        task.setExecutionId(historicTaskInstanceEntity.getExecutionId());
        task.setPriority(historicTaskInstanceEntity.getPriority());
        task.setProcessInstanceId(historicTaskInstanceEntity
                .getProcessInstanceId());
        task.setDescriptionWithoutCascade(historicTaskInstanceEntity
                .getDescription());

        Context.getCommandContext().getTaskEntityManager().insert(task);


        ExecutionEntity executionEntity = Context.getCommandContext()
                .getExecutionEntityManager()
                .findExecutionById(historicTaskInstanceEntity.getExecutionId());
        executionEntity
                .setActivity(getActivity(historicActivityInstanceEntity));
    }
    
    public ActivityImpl getActivity(
            HistoricActivityInstance historicActivityInstanceEntity) {
        ProcessDefinitionEntity processDefinitionEntity = new GetDeploymentProcessDefinitionCmd(
                historicActivityInstanceEntity.getProcessDefinitionId())
                .execute(Context.getCommandContext());

        return processDefinitionEntity
                .findActivity(historicActivityInstanceEntity.getActivityId());
    }

}
