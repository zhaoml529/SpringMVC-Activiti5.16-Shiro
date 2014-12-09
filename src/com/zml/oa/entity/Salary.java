package com.zml.oa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 薪资对象
 * @author zml
 *
 */
@Entity
@Table(name = "T_SALARY")
public class Salary extends BaseVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 179260132812559678L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Integer id;

	// 每月基本工资
	@Column(name = "BASE_MONEY", scale= 2)
	private BigDecimal baseMoney;
	
	// 用户ID，保存在流程引擎中
	@Column(name = "USER_ID")
	private String userId;

	public BigDecimal getBaseMoney() {
		return baseMoney;
	}

	public void setBaseMoney(BigDecimal baseMoney) {
		this.baseMoney = baseMoney;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
