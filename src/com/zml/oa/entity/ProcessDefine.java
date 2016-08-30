package com.zml.oa.entity;

/**
 * 流程节点定义 - 测试
 * @author zhao
 *
 */
public class ProcessDefine {
	
	private Long id;
	
	private Long modelId;	// 外键
	
	private String taskId;	// 节点id，页面不用填写
	
	private String taskName;
	
	private Integer candidateType;
	
	private String candidateIds;
	
	private Integer isStartEvent;
	
	private Integer isInitiator;
	
	private String targetGateway;	// 指向的关口，页面不用选
	
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getCandidateType() {
		return candidateType;
	}

	public void setCandidateType(Integer candidateType) {
		this.candidateType = candidateType;
	}

	public String getCandidateIds() {
		return candidateIds;
	}

	public void setCandidateIds(String candidateIds) {
		this.candidateIds = candidateIds;
	}

	public Integer getIsStartEvent() {
		return isStartEvent;
	}

	public void setIsStartEvent(Integer isStartEvent) {
		this.isStartEvent = isStartEvent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTargetGateway() {
		return targetGateway;
	}

	public void setTargetGateway(String targetGateway) {
		this.targetGateway = targetGateway;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getIsInitiator() {
		return isInitiator;
	}

	public void setIsInitiator(Integer isInitiator) {
		this.isInitiator = isInitiator;
	}
	
}
