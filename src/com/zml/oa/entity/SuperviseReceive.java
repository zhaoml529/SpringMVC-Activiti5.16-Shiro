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

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 督察接收
 * @author zhao
 *
 */
@Entity
@Table(name = "SUPERVISE_RECEIVE")
public class SuperviseReceive implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1092445107465575680L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, length = 10, nullable = false)
	private Integer id;
	
	@Column(name = "TITLE", length = 100)
	private String title;
	
	@Column(name = "STATUS", length = 5)
	private String status;
	
	@Column(name = "NUMBER", length = 50)
	private String number; 
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "CREATE_DATE")
	private Date createDate;		//立项日期
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "HANDLE_DATE")
    private Date handleDate;		//交办日期
	
	@Column(name = "FEEDBACK_CYCLE", length = 10)
	private String feedback_cycle;		//反馈周期 
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FEEDBACK_LIMIT")
	private Date feedback_limit;		//反馈时限
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(Date handleDate) {
		this.handleDate = handleDate;
	}

	public String getFeedback_cycle() {
		return feedback_cycle;
	}

	public void setFeedback_cycle(String feedback_cycle) {
		this.feedback_cycle = feedback_cycle;
	}

	public Date getFeedback_limit() {
		return feedback_limit;
	}

	public void setFeedback_limit(Date feedback_limit) {
		this.feedback_limit = feedback_limit;
	}

	
}
