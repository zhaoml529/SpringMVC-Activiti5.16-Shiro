package com.zml.oa.entity;

import java.io.Serializable;
import java.util.Date;

public class CommentVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3549695946267239515L;

	// 评论人
	private String userName;
	
	// 评论内容
	private String content;
	
	// 评论时间
	private Date time;

	public CommentVO(){
		
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
