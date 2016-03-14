package com.zml.oa.ProcessTask.TaskCommand;

import java.util.Date;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
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
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.zml.oa.service.activiti.WorkflowService;
import com.zml.oa.util.ApplicationContextHelper;

/**
 * Activiti 命令拦截器 Command
 * @author ZML
 *
 */

@Component
public class RevokeTaskCmd implements Command<Integer> {

	private static final Logger logger = LoggerFactory.getLogger(RevokeTaskCmd.class);
    private WorkflowService workflowService;
	
    private HistoryService historyService;
	
    private RuntimeService runtimeService;

	private String historyTaskId;
	
	private String processInstanceId;
	
	public RevokeTaskCmd(){
		
	}
	
	public RevokeTaskCmd(String historyTaskId, String processInstanceId, RuntimeService runtimeService, WorkflowService workflowService,
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
		//获得历史任务
    	HistoricTaskInstanceEntity historicTaskInstanceEntity = Context
                .getCommandContext()
                .getHistoricTaskInstanceEntityManager()
                .findHistoricTaskInstanceById(historyTaskId);
    	// 获得历史节点
        HistoricActivityInstanceEntity historicActivityInstanceEntity = getHistoricActivityInstanceEntity(historyTaskId);
    	
        //获取当前节点
        String currentTaskId = null;
        Task currentTask = null;
        ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if(processInstance != null){
        	
        	currentTask = this.workflowService.getCurrentTaskInfo(processInstance);
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
//        this.deleteActiveTasks(processInstance.getProcessInstanceId());
        Command<Void> cmd = new DeleteActiveTaskCmd((TaskEntity)currentTask, "revoke", true);
       //Context.getCommandContext().getCommand().execute((CommandContext) cmd);
        Context.getProcessEngineConfiguration().getManagementService().executeCommand(cmd);
        
        this.deleteHistoryActivities(this.historyTaskId, this.processInstanceId);
        // 恢复期望撤销的任务和历史
        this.processHistoryTask(historicTaskInstanceEntity,
        		historicActivityInstanceEntity);

        logger.info("activiti is revoke {}", historicTaskInstanceEntity.getName());

        return 0;
	}

	
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
        
    }
    
    /**
     * 删除历史节点.
     */
    public void deleteHistoryActivities(String historyTaskId, String processInstanceId) {
    	JdbcTemplate jdbcTemplate = ApplicationContextHelper.getBean(JdbcTemplate.class);
    	//List<HistoricActivityInstance> list = this.historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
    	List<HistoricActivityInstance> list = this.historyService.createHistoricActivityInstanceQuery().unfinished().processInstanceId(processInstanceId).list();
    	
    	//删除历史评论
    	Context.getCommandContext().getCommentEntityManager().deleteCommentsByTaskId(historyTaskId);
    	for(HistoricActivityInstance hai : list){
    		String taskId = hai.getTaskId();
    		if(taskId != null && !taskId.equals(historyTaskId)){
    			//删除历史任务
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
        task.setProcessDefinitionId(historicTaskInstanceEntity.getProcessDefinitionId());
        task.setId(historicTaskInstanceEntity.getId());
        task.setAssigneeWithoutCascade(historicTaskInstanceEntity.getAssignee());
        task.setParentTaskIdWithoutCascade(historicTaskInstanceEntity.getParentTaskId());
        task.setNameWithoutCascade(historicTaskInstanceEntity.getName());
        task.setTaskDefinitionKey(historicTaskInstanceEntity.getTaskDefinitionKey());
        task.setExecutionId(historicTaskInstanceEntity.getExecutionId());
        task.setPriority(historicTaskInstanceEntity.getPriority());
        task.setProcessInstanceId(historicTaskInstanceEntity.getProcessInstanceId());
        task.setDescriptionWithoutCascade(historicTaskInstanceEntity.getDescription());

        Context.getCommandContext().getTaskEntityManager().insert(task);

        // 把流程指向任务对应的节点
        ExecutionEntity executionEntity = Context.getCommandContext()
                .getExecutionEntityManager()
                .findExecutionById(historicTaskInstanceEntity.getExecutionId());
        executionEntity.setActivity(getActivity(historicActivityInstanceEntity));
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
