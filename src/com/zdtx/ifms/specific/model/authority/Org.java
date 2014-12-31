package com.zdtx.ifms.specific.model.authority;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @ClassName: Org
 * @Description: 权限管理-组织机构-PO类
 * @author Leon Liu
 * @date 2012-9-5 下午05:19:40
 * @version V1.0
 */
@Entity
@Table(name = "T_CORE_ORG")
public class Org implements Serializable {

	private static final long serialVersionUID = 1651352824837872320L;

	private Long orgID;// 组织结构编号
	private String orgName;// 组织结构名称
	private Long inLevel; // 所在级别0 为最高级，默认写入。其他级别在新增时写入。1公司 2 部门 3车队
	private Long parentID; // 上级ID
	private String parentName; // 上级名称
	private String creater;	//修改人
	private String createTime;	//修改时间
	private String isDelete;	//是否删除
	private Long placeNumber; // 排序
	private String description;
	private Integer flag;
	private Set<User> users; // 组织下人员
	private Long lineno;//公司级别用来保存Schedule Option：'1':RFID,'0':'Non-RFID'

	@Id
	@SequenceGenerator(name = "org_sequence", sequenceName = "s_core_org", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_sequence")
	@Column(name = "ORG_ID", columnDefinition = "Org's ID", nullable = false)
	public Long getOrgID() {
		return orgID;
	}

	public void setOrgID(Long orgID) {
		this.orgID = orgID;
	}

	@Column(name = "ORGNAME", length = 100, columnDefinition = "Org's name", nullable = false)
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Column(name = "INLEVEL", columnDefinition = "Level in tree", nullable = false)
	public Long getInLevel() {
		return inLevel;
	}

	public void setInLevel(Long inLevel) {
		this.inLevel = inLevel;
	}

	@Column(name = "PARENTID", columnDefinition = "Parent's id", nullable = false)
	public Long getParentID() {
		return parentID;
	}

	public void setParentID(Long parentID) {
		this.parentID = parentID;
	}

	@Column(name = "PARENTNAME", length = 100, columnDefinition = "Parent's name")
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	@Column(name = "PLACENUMBER", nullable = false)
	public Long getPlaceNumber() {
		return placeNumber;
	}

	public void setPlaceNumber(Long placeNumber) {
		this.placeNumber = placeNumber;
	}

	@OneToMany(targetEntity = User.class, mappedBy = "userOrg", cascade = CascadeType.ALL)
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	@Column(name = "CREATER", length = 100)
	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	@Column(name = "CREATETIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "ISDELETE", length = 1)
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	@Column(name = "DESCRIPTION", length = 100)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "FLAG")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	@Override
	public String toString() {
		return "Org [orgID=" + orgID + ", orgName=" + orgName + ", inLevel="
				+ inLevel + ", parentID=" + parentID + ", parentName="
				+ parentName + ", creater=" + creater + ", createTime="
				+ createTime + ", isDelete=" + isDelete + ", placeNumber="
				+ placeNumber + ", description=" + description + ", flag=" + flag + "]";
	}

	public Long getLineno() {
		return lineno;
	}

	public void setLineno(Long lineno) {
		this.lineno = lineno;
	}


}