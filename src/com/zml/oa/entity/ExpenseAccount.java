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

/**
 * 报销记录
 * @author zml
 *
 */
@Entity
@Table(name = "T_EXPENSE_ACCOUNT")
public class ExpenseAccount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3755379573379214873L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Integer id;
	
	// 申请人
	@Column(name = "USER_ID")
	private String userId;
	
	// 报销金额
	@Column(name = "MONEY")
	private BigDecimal money;
	
	// 日期
	@Column(name = "DATE")
	private Date date;
	
	@Column(name = "PROC_INST_ID")
	private String processInstanceId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	
}
