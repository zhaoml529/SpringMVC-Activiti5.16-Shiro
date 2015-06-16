package com.zml.oa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 休假对象
 * @author ZML
 *
 */
@Entity
@Table(name = "T_VACATION")
// 在Hibernate中可以利用@DynamicInsert和@DynamicUpdate生成动态SQL语句，
//即在插入和修改数据的时候,语句中只包括要插入或者修改的字段。
@DynamicUpdate(true)
@DynamicInsert(true)
public class Vacation extends BaseVO implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1495795296316800235L;
	// 带薪假
	public final static int TYPE_PAID = 0;	
	// 病假
	public final static int TYPE_SICK = 1;	
	// 事假
	public final static int TYPE_MATTER = 2;
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Integer id;

	// 休假的工作日
//	@NotNull  
//    @NotEmpty(message = "This cannot be null") 
	@Column(name = "WORK_DAYS")
	private Integer days;
	
	// 请假开始日期
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "BEGIN_DATE")
	private Date beginDate;
	
	// 请假结束日期
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "END_DATE")
	private Date endDate;
	
	// 申请日期
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "APPLY_DATE")
	private Date applyDate;
	
	// 休假类型
	@Column(name = "VAC_TYPE")
	private Integer vacationType;
	
	//原因
	@Column(name = "REASON")
	private String reason;
	
	// 对应的流程实例id
	@Column(name = "PROC_INST_ID")
	private String processInstanceId;
	
	// 用户id
	@Column(name = "USER_ID")
	private Integer userId;
	
	//审批状态
	@Column(name = "STATUS")
	private String status;
	
	public Vacation(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public Integer getVacationType() {
		return vacationType;
	}

	public void setVacationType(Integer vacationType) {
		this.vacationType = vacationType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
