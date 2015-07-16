package com.zml.oa.ProcessTask.TaskListener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zml.oa.entity.UserTask;
import com.zml.oa.service.IUserTaskService;

/**
 * 动态用户任务分配
 * @author ZML
 *
 */
@Component("userTaskListener")
public class UserTaskListener implements TaskListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2190559253653576032L;
	
	private static final Logger logger = Logger.getLogger(UserTaskListener.class);
    @Autowired
    protected RepositoryService repositoryService;

	@Autowired
	private IUserTaskService userTaskService;
    
	@Override
	public void notify(DelegateTask delegateTask) {
		String processDefinitionId = delegateTask.getProcessDefinitionId();	//com.zml.oa.vacation:8:30012
		ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
//		String processDefinitionName = processDefinition.getName();			//请假流程
		String processDefinitionKey = processDefinition.getKey();			//com.zml.oa.vacation
		String taskDefinitionKey = delegateTask.getTaskDefinitionKey();		//directorAudit
		try {
			List<UserTask> taskList = this.userTaskService.findByWhere(processDefinitionKey);
			for(UserTask userTask : taskList){
				String taskKey = userTask.getTaskDefKey();
				String taskType = userTask.getTaskType();
				String ids = userTask.getCandidate_ids();
				if(taskDefinitionKey.equals(taskKey)){
					switch (taskType){
						case "assignee" : {
							delegateTask.setAssignee(ids);
							logger.info("assignee id: "+ids);
							break;
						}
						case "candidateUser" : {
							String[] userIds = ids.split(",");
							List<String> users = new ArrayList<String>();
							for(int i=0; i<userIds.length;i++){
								users.add(userIds[i]);
							}
							delegateTask.addCandidateUsers(users);
							logger.info("候选人审批 ids: "+ids);
							break;
						}
						case "candidateGroup" : {
							String[] groupIds = ids.split(",");
							List<String> groups = new ArrayList<String>();
							for(int i=0; i<groupIds.length;i++){
								groups.add(groupIds[i]);
							}
							delegateTask.addCandidateGroups(groups);
							logger.info("候选组审批 ids: "+ids);
							break;
						}
					}
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
