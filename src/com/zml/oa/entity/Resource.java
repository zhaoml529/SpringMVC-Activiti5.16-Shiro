package com.zml.oa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @ClassName: Resource
 * @Description:资源表
 * @author: zml
 * @date: 2015-01-9 下午19:59:48
 *
 */

@Entity
@Table(name = "T_RESOURCE")
public class Resource implements Serializable {

	private static final long serialVersionUID = 53889172259830160L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id; 								//编号
	
	@Column(name = "name")
	@NotEmpty(message = "{resource.name.not.empty}")
    private String name; 							//资源名称
	
	@Column(name = "type")
    private String type;							//资源类型
	
	@Column(name = "url")
    private String url; 							//资源路径
	
	@Column(name = "permission")
	@NotEmpty(message = "{resource.permission.not.empty}")
    private String permission; 						//权限字符串
	
	@Column(name = "parent_id")
	@NotNull(message = "{resource.parentId.not.empty}")
    private Integer parentId; 							//父编号
	
	@Column(name = "parent_ids")
	/*@NotEmpty(message="{resource.parentIds.not.empty}")
	@Length(min = 2, max = 20, message = "{resource.parentIds.length.illegal}")*/
    private String parentIds; 						//父编号列表
	
	@Column(name = "available")
    private Integer available;
    
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	public boolean isRootNode() {
        return parentId == 0;
    }

    public String makeSelfAsParentIds() {
        return getParentIds() + getId() + "/";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (id != null ? !id.equals(resource.id) : resource.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", permission='" + permission + '\'' +
                ", parentId=" + parentId +
                ", parentIds='" + parentIds + '\'' +
                ", available=" + available +
                '}';
    }
}
