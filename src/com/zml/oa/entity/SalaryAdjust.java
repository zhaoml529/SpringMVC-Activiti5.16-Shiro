package com.zml.oa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 薪资调整记录
 * @author zml
 *
 */
@Entity
@Table(name = "T_SALARY_ADJUST")
public class SalaryAdjust extends BaseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 785682620591617355L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Integer id;
	
	// 用户id
	@Column(name = "USER_ID")
	private Integer userId;
	
	// 调整金额
	@Column(name = "ADJUST_MONEY", scale= 2)
	private BigDecimal adjustMoney;
	
	// 申请日期
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "APPLY_DATE")
	private Date applyDate;
	
	// 描述
	@Column(name = "DSCP")
	private String dscp;
	
	// 流程实例id
	@Column(name = "PROC_INST_ID")
	private String processInstanceId;
	
	//审批状态
	@Column(name = "STATUS")
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getAdjustMoney() {
		return adjustMoney;
	}

	public void setAdjustMoney(BigDecimal adjustMoney) {
		this.adjustMoney = adjustMoney;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getDscp() {
		return dscp;
	}

	public void setDscp(String dscp) {
		this.dscp = dscp;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
