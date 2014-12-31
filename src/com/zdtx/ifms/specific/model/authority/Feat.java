package com.zdtx.ifms.specific.model.authority;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.zdtx.ifms.common.model.AbstractModel;

/**
 * @ClassName: Feat
 * @Description: 权限管理-功能菜单表PO类
 * @author Leon Liu
 * @date 2012-4-13 上午09:23:23
 * @version V1.0
 */
@Entity
@Table(name = "T_CORE_FEAT")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class Feat extends AbstractModel implements Serializable {

	private static final long serialVersionUID = -3304450413495603886L;

	private Long featID; // 功能ID
	private String featName; // 功能名称
	private Long sort; // 功能序号
	private Long fatherID; // 父功能ID
	private Long featLevel;	//0 级为根节点，其他级别自动加1
	private String isDelete;	//操作人
	private String createName;	//操作人
	private String createTime;	//操作时间
	private String url; // 链接地址
	private String feat;	//1  浏览    0  管理

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "FEAT_ID", columnDefinition = "主键", nullable = false)
	public Long getFeatID() {
		return featID;
	}

	public void setFeatID(Long featID) {
		this.featID = featID;
	}

	@Column(name = "FEATNAME", length = 100, columnDefinition = "功能序号", nullable = false)
	public String getFeatName() {
		return featName;
	}

	public void setFeatName(String featName) {
		this.featName = featName;
	}

	@Column(name = "SORT", columnDefinition = "功能序号", nullable = false)
	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	@Column(name = "FATHERID", columnDefinition = "上级ID", nullable = false)
	public Long getFatherID() {
		return fatherID;
	}

	public void setFatherID(Long fatherID) {
		this.fatherID = fatherID;
	}

	@Column(name = "FEATLEVEL", columnDefinition = "模块级别", nullable = false)
	public Long getFeatLevel() {
		return featLevel;
	}

	public void setFeatLevel(Long featLevel) {
		this.featLevel = featLevel;
	}
	
	@Column(name = "ISDELETE", length = 1, columnDefinition = "是否删除")
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	@Column(name = "CREATENAME", length = 100, columnDefinition = "操作人")
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	@Column(name = "CREATETIME", length = 20, columnDefinition = "操作时间")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "URL", length = 100, columnDefinition = "链接地址")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name = "FEAT", length = 200, columnDefinition = "1 浏览；0 管理")
	public String getFeat() {
		return feat;
	}

	public void setFeat(String feat) {
		this.feat = feat;
	}

	@Override
	public String toString() {
		return "Feat [featID=" + featID + ",featName=" + featName
				+ ",fatherID=" + fatherID + ",featLevel=" + featLevel + ",url=" + url + "]";
	}
}