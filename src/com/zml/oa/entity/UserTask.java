package com.zml.oa.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 用户任务表-待完善
 * @author ZML
 *
 */
public class UserTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8889804050417208965L;
	private Integer id;
	private String procDefKey;		//com.zml.oa.vacation
	private String procDefName;		//请假申请
	private String taskDefKey;		//hrAudti
	private Integer taskType;		//0.受理人(唯一) 1.候选人(多个) 2.候选组（多个）
	private String assignee;
	private List<String> candidateUser;
	private List<String> candidateGroup;
	
}
