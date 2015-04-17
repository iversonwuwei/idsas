package com.zdtx.ifms.common.utils;
/**
 * 在现有项目中没有引用-基本废弃。
 */
/**
 * Ztree用Node节点类
 * 
 * @author LiuGuilong
 * @since 2012-05-02
 */
public class Node {
	private Long id;// 节点ID
	private Long pid;// 父节点ID
	private String name;// 节点名称
	private String checked;// 是否选中(是:"true",否:"false")
	private String open;// 是否打开(是:"true",否:"false")
	private String ischildren;// 是否为末节点(是:"true",否:"false")
	private String chkdisabled;// 是不是不能选，是不是不让选(是:"true",否:"false")

	public Node() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getIschildren() {
		return ischildren;
	}

	public void setIschildren(String ischildren) {
		this.ischildren = ischildren;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getChkdisabled() {
		return chkdisabled;
	}

	public void setChkdisabled(String chkdisabled) {
		this.chkdisabled = chkdisabled;
	}

	@Override
	public String toString() {
		return "Node [id=" + id + ", pid=" + pid + ", name=" + name + ", checked=" + checked + ", open=" + open + ", ischildren=" + ischildren + ", chkdisabled=" + chkdisabled + "]";
	}

}
