package com.zml.oa.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程定义实体 act_re_procdef，此实体类是配合easyui使用
 * @author ZML
 *
 */
public class ProcessDefinitionEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String key;
	private int revision = 1;
    private int version;
    private String category;
    private String deploymentId;
    private String resourceName;
    private Date deploymentTime;
    private String diagramResourceName;
    private Boolean suspended;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getRevision() {
		return revision;
	}
	public void setRevision(int revision) {
		this.revision = revision;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDeploymentId() {
		return deploymentId;
	}
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public Date getDeploymentTime() {
		return deploymentTime;
	}
	public void setDeploymentTime(Date deploymentTime) {
		this.deploymentTime = deploymentTime;
	}
	public String getDiagramResourceName() {
		return diagramResourceName;
	}
	public void setDiagramResourceName(String diagramResourceName) {
		this.diagramResourceName = diagramResourceName;
	}
	public Boolean getSuspended() {
		return suspended;
	}
	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}

    
}
