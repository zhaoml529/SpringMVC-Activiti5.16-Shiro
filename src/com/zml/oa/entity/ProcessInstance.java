package com.zml.oa.entity;

/**
 * 流程实例 - 测试
 * @author zhao
 *
 */
public class ProcessInstance {

	private Long id;
	
	private Long modelId;
	
	private Long procDefId;
	
	private String operationType;
	
	private Long targetRef;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(Long procDefId) {
		this.procDefId = procDefId;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public Long getTargetRef() {
		return targetRef;
	}

	public void setTargetRef(Long targetRef) {
		this.targetRef = targetRef;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	
}
