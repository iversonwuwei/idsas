package com.zdtx.ifms.specific.model.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: CamModel
 * @Description: system-IP Camera Model-POJO
 * @author: Leon Liu
 * @date: 2013-7-24 下午2:43:46
 * @version V1.0
 */
@Entity
@Table(name = "T_CORE_CAMERA_MODEL")
public class CamModel implements Serializable {

	private static final long serialVersionUID = -3496716888001292013L;

	private Long modelID; // 主键，模块ID
	private String modelName; // 模块名
	private String creater; // 修改人
	private String createTime; // 修改时间
	private String memo; // 备注
	private String isDelete; // 是否删除
	private String type; // 是否删除

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MODELID", nullable = false, unique = true)
	public Long getModelID() {
		return modelID;
	}

	public void setModelID(Long modelID) {
		this.modelID = modelID;
	}

	@Column(name = "MODEL")
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	@Column(name = "CREATER")
	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	@Column(name = "CREATIME")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MEMO")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ISDELETE")
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CamModel [modelID=" + modelID + ", modelName=" + modelName
				+ ", creater=" + creater + ", createTime=" + createTime
				+ ", memo=" + memo + ", isDelete=" + isDelete + "]";
	}
}