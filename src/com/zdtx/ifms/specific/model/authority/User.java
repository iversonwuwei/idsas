package com.zdtx.ifms.specific.model.authority;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;



/**
 * @ClassName: User
 * @Description: 权限管理-人员信息表PO类
 * @author Leon Liu
 * @date 2012-9-5 下午05:17:43
 * @version V1.0
 */

@Entity
@Table(name = "T_CORE_USERINFO")
public class User implements Serializable {

	private static final long serialVersionUID = 151404710405666661L;
	
	private Long userID;	//用户表ID
	private String loginName;	//登录用户名
	private String userName;	//真实姓名
	private String userCode;	//用户卡号
	private String password;	//密码
	private Role userRole;	//角色
	private Org userOrg;	//组织结构编号
	private Long[] orgs;	//组织权限
	private String creater;	//修改人
	private String createTime;	//修改时间
	private String isDelete;	//是否删除
	private String mobilephone; //电话
	private String e_mail;		//邮箱
	private String mailpassword; //邮箱密码
	private String education; //学历
	private String school; //学校
	private String gender; //性别
	private String description; //描述

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CORE_USERINFO")
	@SequenceGenerator(name = "S_CORE_USERINFO", sequenceName = "S_CORE_USERINFO", allocationSize = 1)
	@Column(name = "USERID", columnDefinition = "用户表ID", nullable = false)
	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	@Column(name = "LOGINNAME", length = 100, columnDefinition = "登录用户名", nullable = false)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name = "USERNAME", length = 100, columnDefinition = "真实姓名", nullable = false)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "USERCODE", length = 20, columnDefinition = "用户卡号", nullable = false)
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Column(name = "PASSWORD", length = 100, columnDefinition = "用户密码", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ElementCollection
	@CollectionTable(name = "T_CORE_USER_DATA", joinColumns = @JoinColumn(name = "USERID"))
	@IndexColumn(name = "LONG_INDEX", base = 1)
	@Column(name = "ORG_ID", columnDefinition = "组织机构主键")
	@NotFound(action = NotFoundAction.IGNORE)
	public Long[] getOrgs() {
		return orgs;
	}

	public void setOrgs(Long[] orgs) {
		this.orgs = orgs;
	}

	@ManyToOne(targetEntity = Role.class, fetch=FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "ROLE_ID", columnDefinition = "所属角色")
	public Role getUserRole() {
		return userRole;
	}

	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}

	@ManyToOne(targetEntity = Org.class, fetch=FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "ORG_ID", columnDefinition = "所属机构")
	public Org getUserOrg() {
		return userOrg;
	}

	public void setUserOrg(Org userOrg) {
		this.userOrg = userOrg;
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

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getE_mail() {
		return e_mail;
	}

	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}

	public String getMailpassword() {
		return mailpassword;
	}

	public void setMailpassword(String mailpassword) {
		this.mailpassword = mailpassword;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "User [userID=" + userID + ", loginName=" + loginName
				+ ", userName=" + userName + ", userCode=" + userCode
				+ ", password=" + password + ", userRole=" + userRole
				+ ", userOrg=" + userOrg + ", orgs=" + Arrays.toString(orgs)
				+ ", creater=" + creater + ", createTime=" + createTime
				+ ", isDelete=" + isDelete + ", mobilephone=" + mobilephone
				+ ", e_mail=" + e_mail + ", mailpassword=" + mailpassword
				+ ", education=" + education + ", school=" + school
				+ ", gender=" + gender + ", description=" + description + "]";
	}
	
}