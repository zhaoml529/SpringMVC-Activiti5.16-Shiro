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
	private Integer taskType;		//0.受理人(唯一) 1.候选人(多个) 2.候选组（多个）
	
	@Column(name = "ASSIGNEE")
	private String assignee;
	
	@Column(name = "CANDIDATE_USERS")
	private String candidateUsers;
	
	@Column(name = "CANDIDATE_GROUPS")
	private String candidateGroups;
	
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
	public Integer getTaskType() {
		return taskType;
	}
	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getCandidateUsers() {
		return candidateUsers;
	}
	public void setCandidateUsers(String candidateUsers) {
		this.candidateUsers = candidateUsers;
	}
	public String getCandidateGroups() {
		return candidateGroups;
	}
	public void setCandidateGroups(String candidateGroups) {
		this.candidateGroups = candidateGroups;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	
}
