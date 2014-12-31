package com.zdtx.ifms.specific.vo.authority;

public class UserVo {
	private String passWord;
	private String passWordNew;
	private String loginName;// 登录�?
	private String userName;// 用户真是�?
	private String userCode;// 用户编号
	private String roleID;// 角色id
	private String roleName;// 角色id
	private String orgID;// 机构id
	private String orgName;// 机构名称
	private String sysName;
	private String logBTime;
	private String logETime;
	private String cardBTime;
	private String cardETime;
	private String opertype;
	private String engno; //车载机编�?
	private String engname;//车载机名
	private String vehcode;//车牌
	private String status;
	private String phone;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getPassWordNew() {
		return passWordNew;
	}
	public void setPassWordNew(String passWordNew) {
		this.passWordNew = passWordNew;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getRoleID() {
		return roleID;
	}
	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}
	public String getOrgID() {
		return orgID;
	}
	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	public String getEngno() {
		return engno;
	}
	public void setEngno(String engno) {
		this.engno = engno;
	}
	public String getEngname() {
		return engname;
	}
	public void setEngname(String engname) {
		this.engname = engname;
	}
	
	public String getVehcode() {
		return vehcode;
	}
	public void setVehcode(String vehcode) {
		this.vehcode = vehcode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLogBTime() {
		return logBTime;
	}
	public void setLogBTime(String logBTime) {
		this.logBTime = logBTime;
	}
	public String getLogETime() {
		return logETime;
	}
	public void setLogETime(String logETime) {
		this.logETime = logETime;
	}
	public String getOpertype() {
		return opertype;
	}
	public void setOpertype(String opertype) {
		this.opertype = opertype;
	}
	
	public String getCardBTime() {
		return cardBTime;
	}
	public void setCardBTime(String cardBTime) {
		this.cardBTime = cardBTime;
	}
	public String getCardETime() {
		return cardETime;
	}
	public void setCardETime(String cardETime) {
		this.cardETime = cardETime;
	}
	@Override
	public String toString() {
		return "UserVo [passWord=" + passWord + ", passWordNew=" + passWordNew
				+ ", loginName=" + loginName + ", userName=" + userName
				+ ", userCode=" + userCode + ", roleID=" + roleID
				+ ", roleName=" + roleName + ", orgID=" + orgID + ", orgName="
				+ orgName + ", sysName=" + sysName + ", logBTime=" + logBTime
				+ ", logETime=" + logETime + ", cardBTime=" + cardBTime
				+ ", cardETime=" + cardETime + ", opertype=" + opertype
				+ ", engno=" + engno + ", engname=" + engname + ", vehcode="
				+ vehcode + ", status=" + status + ", phone=" + phone + "]";
	}

	
}