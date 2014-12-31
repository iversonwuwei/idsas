package com.zdtx.ifms.specific.model.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @ClassName: Dict
 * @Description: 基础信息-字典数据-PO类
 * @author Leon Liu
 * @date 2012-4-16 下午03:49:37
 * @version V1.0
 */
@Entity
@Table(name = "T_CORE_DICT")
public class Dict implements Serializable {

	private static final long serialVersionUID = -2404455818102028315L;

	private Long dictID;	//主键
	private String dictName; //名称
	private Long categoryID;	//字典分类编号:101000开始,步长1000
	private String categoryName;	//类别名称
	private String assistName;	//辅助名称
	private Long createrID;
	private String creater;
	private String createTime;
	private String isDelete;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DICT_ID", columnDefinition = "字典主键", nullable = false)
	public Long getDictID() {
		return dictID;
	}

	public void setDictID(Long dictID) {
		this.dictID = dictID;
	}

	@Column(name = "DICTNAME", length = 100, columnDefinition = "字典名称")
	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	@Column(name = "SORT_ID", columnDefinition = "字典分类编号")
	public Long getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(Long categoryID) {
		this.categoryID = categoryID;
	}

	@Column(name = "SORTNAME", length = 100, columnDefinition = "字典分类名称")
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Column(name = "ASSISTNAME", length = 100, columnDefinition = "辅助名称")
	@NotBlank(message = "事件参数不可为空！")
	@Length(max = 15,message = "事件参数字符个数不可超过15！")
	public String getAssistName() {
		return assistName;
	}

	public void setAssistName(String assistName) {
		this.assistName = assistName;
	}

	@Column(name = "CREATERID", nullable = false, columnDefinition = "修改人ID")
	public Long getCreaterID() {
		return createrID;
	}

	public void setCreaterID(Long createrID) {
		this.createrID = createrID;
	}

	@Column(name = "CREATER", nullable = false, length = 100, columnDefinition = "修改人")
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


	@Column(name = "ISDELETE", nullable = false, length = 1, columnDefinition = "已删除")
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "Dict [dictID=" + dictID + ", dictName=" + dictName
				+ ", categoryID=" + categoryID + ", categoryName="
				+ categoryName + ", assistName=" + assistName + ", createrID="
				+ createrID + ", creater=" + creater + ", createTime="
				+ createTime + ", isDelete=" + isDelete + "]";
	}	 
}