package com.zml.oa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户任务表-待完善
 * @author ZML
 *
 */
@Entity
@Table(name = "T_USER_TASK")
public class UserTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8889804050417208965L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Integer id;
	
//	private String procDefId;		//com.zml.oa.salary:1:127525-- 去掉，procDefId重新部署会改变的，保存这个id是无意义的
	
	@Column(name = "PROC_DEF_KEY")
	private String procDefKey;		//com.zml.oa.vacation
	
	@Column(name = "PROC_DEF_NAME")
	private String procDefName;		//请假申请
	
	@Column(name = "TASK_DEF_KEY")
	private String taskDefKey;		//hrAudti
	
	@Column(name = "TASK_NAME")
	private String taskName;		//人事审批
	
	@Column(name = "TASK_TYPE")
	private String taskType;		//1.assignee.受理人(唯一) 1.candidateUser候选人(多个) 2.candidateGroup候选组（多个）
	
	@Column(name = "CANDIDATE_NAME")
	private String candidate_name; 	//人或候选人或组的名称
	
	@Column(name = "CANDIDATE_IDS")
	private String candidate_ids;
	
	public String getProcDefKey() {
		return procDefKey;
	}
	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}
	public String getProcDefName() {
		return procDefName;
	}
	public void setProcDefName(String procDefName) {
		this.procDefName = procDefName;
	}
	public String getTaskDefKey() {
		return taskDefKey;
	}
	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getCandidate_name() {
		return candidate_name;
	}
	public void setCandidate_name(String candidate_name) {
		this.candidate_name = candidate_name;
	}
	public String getCandidate_ids() {
		return candidate_ids;
	}
	public void setCandidate_ids(String candidate_ids) {
		this.candidate_ids = candidate_ids;
	}
}
