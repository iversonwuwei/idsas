/**
 * @File: IpCam.java
 * @path: idsas - com.zdtx.ifms.specific.model.monitor
 */
package com.zdtx.ifms.specific.model.monitor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: IpCam
 * @Description: IP Camera's model
 * @author: Leon Liu
 * @date: 2013-7-8 下午2:38:44
 * @version V1.0
 */
@Entity
@Table(name = "T_CORE_CAM_USER")
public class CamUser implements Serializable {

	private static final long serialVersionUID = -1267051207501730810L;

	private Long userID; // ID
	private String userName; // camera authority's username
	private String userPass; // camera authority's password
	private Long authLevel; // camera user's authority level
	private String disable; // is disabled

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USERID", nullable = false)
	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	@Column(name = "USERNAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "USERPASS")
	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	@Column(name = "AUTHLEVEL")
	public Long getAuthLevel() {
		return authLevel;
	}

	public void setAuthLevel(Long authLevel) {
		this.authLevel = authLevel;
	}

	@Column(name = "DISABLE")
	public String getDisable() {
		return disable;
	}

	public void setDisable(String disable) {
		this.disable = disable;
	}

	@Override
	public String toString() {
		return "CamUser [userID=" + userID + ", userName=" + userName
				+ ", userPass=" + userPass + ", authLevel=" + authLevel
				+ ", disable=" + disable + "]";
	}
}