package com.zml.oa.ProcessTask.RevokeTask;

import java.util.Date;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.HistoricActivityInstanceQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zml.oa.service.activiti.WorkflowService;

public class RevokeTask {

	private static final Logger logger = LoggerFactory.getLogger(RevokeTask.class);
	
    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    protected HistoryService historyService;
    
    @Autowired
    protected TaskService taskService;
    
    @Autowired
    private RuntimeService runtimeService;
    
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
    
	/**
	 * 0-撤销成功 1-流程结束 2-下一结点已经通过,不能撤销
	 * @param historyTaskId
	 * @param processInstanceId
	 * @return
	 */
    public Integer revoke(String historyTaskId, String processInstanceId) throws Exception{
    	// 获得历史任务
    	HistoricTaskInstanceEntity historicTaskInstanceEntity = Context
                .getCommandContext().getHistoricTaskInstanceEntityManager()
                .findHistoricTaskInstanceById(historyTaskId);
    	// 获得历史节点
        HistoricActivityInstanceEntity historicActivityInstanceEntity = getHistoricActivityInstanceEntity(historyTaskId);
    	
        //获取当前节点
        ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if(processInstance != null){
        	Task currentTask = this.workflowService.getCurrentTaskInfo(processInstance);
        	String taskId = currentTask.getId();
        	HistoricTaskInstance hti = this.historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        	//下一结点已经通过,不能撤销。
        	if("completed".equals(hti.getDeleteReason())){
        		logger.info("cannot revoke {}", historyTaskId);
        		return 2;
        	}
        }else{
        	return 1;
        }
        // 删除所有活动中的task
        this.deleteActiveTasks(processInstanceId);
        // 恢复期望撤销的任务和历史
        this.processHistoryTask(historicTaskInstanceEntity,
                historicActivityInstanceEntity);

        logger.info("activiti is revoke {}", historicTaskInstanceEntity.getName());

        return 0;
    	
    }
    
    public HistoricActivityInstanceEntity getHistoricActivityInstanceEntity(
            String historyTaskId) {

        String historicActivityInstanceId = this.jdbcTemplate.queryForObject(
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
    }
    
    /**
     * 删除历史节点.
     */
    public void deleteHistoryActivities(String currentTaskId) {
    	
    	List<HistoricActivityInstance> list = this.historyService.createHistoricActivityInstanceQuery().processInstanceId(currentTaskId).list();
    	
    	for(HistoricActivityInstance hai : list){
    		String taskId = hai.getTaskId();
    		if(taskId != null){
    			//删除历史任务
    			Context.getCommandContext()
                .getHistoricTaskInstanceEntityManager()
                .deleteHistoricTaskInstanceById(taskId);
    			//删除历史行为
    			this.jdbcTemplate.update("delete from ACT_HI_ACTINST where task_id_=?", taskId);
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
            HistoricActivityInstanceEntity historicActivityInstanceEntity) {
        ProcessDefinitionEntity processDefinitionEntity = new GetDeploymentProcessDefinitionCmd(
                historicActivityInstanceEntity.getProcessDefinitionId())
                .execute(Context.getCommandContext());

        return processDefinitionEntity
                .findActivity(historicActivityInstanceEntity.getActivityId());
    }
    
}
