/**
 * Project Name:SpringOA
 * File Name:User.java
 * Package Name:com.zml.oa.entity
 * Date:2014-11-8下午11:12:48
 *
 */
package com.zml.oa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @ClassName: User
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: zml
 * @date: 2014-11-8 下午11:12:48
 *
 */

@Entity
@Table(name = "T_USER")
public class User implements Serializable{

	private static final long serialVersionUID = -6662232329895785824L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", unique = true)
	private Integer id;
	
	@Column(name = "USER_NAME")
	private String name;
	
	@Column(name = "USER_PWD")
	private String passwd;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "REG_DATE")
	private Date registerDate;
	
	
	//bi-directional many-to-many association to Group
//    @ManyToMany
//    @JoinTable(name = "T_MEMBERSHIP", joinColumns = {@JoinColumn(name = "USER_ID")}, inverseJoinColumns = {@JoinColumn(name = "GROUP_ID")})
//    private List<Group> group;

	//多对一，@JoinColumn与@column类似，指定映射的数据库字段
	@ManyToOne(targetEntity = Group.class)
	@JoinColumn(name="GROUP_ID",updatable=false)
    private Group group;
    
	public User(){
		
	}
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPasswd() {
		return passwd;
	}


	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}


	public Date getRegisterDate() {
		return registerDate;
	}


	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Group getGroup() {
		return group;
	}


	public void setGroup(Group group) {
		this.group = group;
	}
    
	
}
