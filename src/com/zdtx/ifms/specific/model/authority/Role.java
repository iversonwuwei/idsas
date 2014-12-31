package com.zdtx.ifms.specific.model.authority;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * @ClassName: Role
 * @Description: 权限管理-角色表PO类
 * @author Leon Liu
 * @date 2012-4-13 上午09:39:23
 * @version V1.0
 */
@Entity
@Table(name = "T_CORE_ROLE")
public class Role implements Serializable {

	private static final long serialVersionUID = 592390581010048450L;

	private Long roleID; // 角色编号 1 超级管理员
	private Long[] feats; // 角色对应权限
	private String roleName; // 角色名称
	private Long inLevel; // 所属级别 0 为最高级
	private String inSystem;	//摄像头权限级别0：超管；1：控制者；2：浏览者；
	private String creater;	//修改人
	private String createTime;	//修改时间
	private String isDelete;	//是否删除
	private Set<User> users; // 角色下人员
	private String description;//备注
	private Long comid;//公司id
	private String comname;//公司名称
	private String aflag;//0 super admin 1admin 2other

	@Id
	@SequenceGenerator(name = "S_CORE_ROLE", sequenceName = "S_CORE_ROLE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CORE_ROLE")
	@Column(name = "ROLE_ID", nullable = false, columnDefinition = "主键")
	public Long getRoleID() {
		return roleID;
	}

	public void setRoleID(Long roleID) {
		this.roleID = roleID;
	}

	@ElementCollection
	@CollectionTable(name = "T_CORE_ROLE_FEAT", joinColumns = @JoinColumn(name = "ROLE_ID"))
	@IndexColumn(name = "LONG_INDEX", base = 1)
	@Column(name = "FEAT_ID", columnDefinition = "角色对应权限")
	@NotFound(action = NotFoundAction.IGNORE)
	public Long[] getFeats() {
		return feats;
	}

	public void setFeats(Long[] feats) {
		this.feats = feats;
	}

	@Column(name = "ROLENAME", length = 100, columnDefinition = "角色名称", nullable = false)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "INLEVEL", columnDefinition = "所属级别", nullable = false)
	public Long getInLevel() {
		return inLevel;
	}

	public void setInLevel(Long inLevel) {
		this.inLevel = inLevel;
	}

	@OneToMany(targetEntity = User.class, mappedBy = "userRole", cascade = CascadeType.ALL)
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Column(name = "INSYSTEM", nullable = false, columnDefinition = "所属系统")
	public String getInSystem() {
		return inSystem;
	}

	public void setInSystem(String inSystem) {
		this.inSystem = inSystem;
	}

	@Column(name = "CREATER", length = 100, columnDefinition = "修改人")
	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	@Column(name = "CREATETIME", length = 20, columnDefinition = "修改时间")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "ISDELETE", length = 1, columnDefinition = "是否删除")
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Role [roleID=" + roleID + ", feats=" + Arrays.toString(feats)
				+ ", roleName=" + roleName + ", inLevel=" + inLevel
				+ ", inSystem=" + inSystem + ", creater=" + creater
				+ ", createTime=" + createTime + ", isDelete=" + isDelete
				+ ", users=" + users + ", description=" + description + "]";
	}

	public Long getComid() {
		return comid;
	}

	public void setComid(Long comid) {
		this.comid = comid;
	}

	public String getComname() {
		return comname;
	}

	public void setComname(String comname) {
		this.comname = comname;
	}

	public String getAflag() {
		return aflag;
	}

	public void setAflag(String aflag) {
		this.aflag = aflag;
	}
	
}