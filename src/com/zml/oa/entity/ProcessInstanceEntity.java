package com.zml.oa.entity;

import java.io.Serializable;

/**
 * 流程实例试题 act_ru_execution
 * @author ZML
 *
 */
public class ProcessInstanceEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String processInstanceId; 		//流程实例id
	private String processDefinitionId; 	//流程定义id
	private String activityId; 				//流程节点id
	private Boolean suspended;				//挂起/激活
	private String taskName;				//当前节点名称
	
	  /**
	   * The name of the process definition of the process instance.
	   */
	private String processDefinitionName;
	  
	  /**
	   * The key of the process definition of the process instance.
	   */
	private String processDefinitionKey;
	  
	  /**
	   * The version of the process definition of the process instance.
	   */
	private Integer processDefinitionVersion;
	  
	  /**
	   * The deployment id of the process definition of the process instance.
	   */
	private String deploymentId;
	  
	  /**
	   * The business key of this process instance.
	   */
	private String businessKey;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public Integer getProcessDefinitionVersion() {
		return processDefinitionVersion;
	}

	public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
		this.processDefinitionVersion = processDefinitionVersion;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public Boolean getSuspended() {
		return suspended;
	}

	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	
}
