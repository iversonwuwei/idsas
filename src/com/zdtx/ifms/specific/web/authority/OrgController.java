package com.zdtx.ifms.specific.web.authority;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.authority.Org;

public class OrgController extends URLSupport<Org>{

	private static final long serialVersionUID = 9173702565438285389L;
	private Org org = new Org();
//	private Long newIntype;
//	// 默认打开集合
//		private List<String> open = new ArrayList<String>();
//	// 默认选中节点ID
//	private String select = "";
//	
//	@Autowired
//	private OrgManager orgMgr;
//
//	public String getTree(){
//		if (null == id || 0 == id) {
//			this.getTreeJson(orgMgr.getTop());
//		} else {
//			this.getTreeJson(orgMgr.getChild(id));
//		}
//		return null;
//	}
//
//	private void getTreeJson(List<Org> orgs) {
//
//		if (null != orgs && 0 != orgs.size()) {
//			List<JSTree> jsonList = new ArrayList<JSTree>();
//			for (Org o : orgs) {
//				JSTree node = new JSTree();
//				Map<String, String> attr = new HashMap<String, String>();
//				attr.put("id", o.getOrgID() + "");
//				attr.put("sort", o.getOrgName() + "");
//				if(null!=o.getInLevel()){
//					if(-1L == o.getFatherOrg())	{
//						attr.put("rel", "root");
//					} else if(3==o.getInLevel()){//默认数展到3级(线路)
//						attr.put("rel", "line");
//						node.setState("");
//					} else {
//						attr.put("rel", "default");
//					}
//				}
//				node.setAttr(attr);
//				node.setData(o.getOrgName());
//				jsonList.add(node);
//			}
//			Struts2Util.renderJson(jsonList);
//		}
//	}
//	public String add() {
//		// 获取后台传过来的父节点ID
//		Long id = Long.parseLong(Struts2Util.getParameter("id"));
//		// 获取新添加节点的名称
//		String name = Struts2Util.getParameter("name");
//		try {
//			// 获取父节点对象，下面会用到它的属性
//			Org father = baseMgr.get(Org.class, id);
//			// 验证是否重复
//			if(!orgMgr.checkName2(id,name)){
//				Struts2Util.renderJson("{\"status\" : \"repeat\"}");
//				return null;
//			}
//			// 创建新节点对象设置各种值，保存到数据库
//			Org tree = new Org();
//			tree.setFatherOrg(id);
//			tree.setIsDelete("F");
//			tree.setOrgName(name);
////			if(father.getInType().equals(0L)){
////				tree.setInType(0L);
////			}else{
////				tree.setInType(1L);
////			}
//			tree.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
//			tree.setCreater("超级管理员");
//			// 父节点的LEVEL+1
//			tree.setInLevel(father.getInLevel() + 1L);
//			baseMgr.save(tree);
//			Struts2Util.renderJson("{\"status\" : \"success\", \"id\" : " + tree.getOrgID() + "}");
//		} catch (Exception e) {
//			Struts2Util.renderJson("{\"status\" : \"error\"}");
//			e.printStackTrace();
//		}
//		return null;
//	}
//	public String re() {
//		if (null != id) {
//			Org o=baseMgr.get(Org.class, id);
//			if(o.getInLevel()>1){
//				if(!orgMgr.checkName3(id,org.getOrgName())){
//					Struts2Util.renderHtml("duplicate1");
//					return null;
//				}
//				
//			}else{
//				if(!orgMgr.checkName(org.getOrgName(),o.getFatherOrg(),id)){
//					Struts2Util.renderHtml("duplicate");
//					return null;
//				}
//			}
//			
//			
//			o.setOrgName(org.getOrgName());
//			o.setCreateTime(new Date().toString());
//			o.setCreater(getCurrentUser().getUserName());
//			baseMgr.save(o);
//			Struts2Util.renderJson("{\"status\" : 1}");
//		}
//		return null;
//	}
//	/**
//	 * @title 重命名
//	 * @return
//	 */
//	public String rename() {
//		// 获取后台传过来的父节点ID
//		Long id = Long.parseLong(Struts2Util.getParameter("id"));
//		// 获取新名称
//		String newName = Struts2Util.getParameter("name");
//		// 如果该节点没有ID，意为脏数据，则不对其进行任何操作
//		if (null == id) {
//			return null;
//		}
//		try {
//			Org t = baseMgr.get(Org.class, id);
//			// 判断其新名称是否和就名称相同，相同则不做任何操作
//			if (t.getOrgName().equals(newName)) {
//				return null;
//			}
//			// 重复验证
//			if (!orgMgr.checkName(newName,t.getFatherOrg(),id)) {
//				Struts2Util.renderJson("{\"status\" : \"repeat\"}");
//				return null;
//			}
//			// 保存更改
//			t.setOrgName(newName);
//			baseMgr.save(t);
//		} catch (Exception e) {
//			Struts2Util.renderJson("{\"status\" : \"error\"}");
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	/**
//	 * 删除部位方法
//	 * @param id 要删除的部位ID
//	 * @param userID 操作人ID
//	 * @param userName 操作人名
//	 */
//	public String remove() {
//		try{
//			orgMgr.destory(id, "超级");
//		}catch(Exception e){
//			e.printStackTrace();
//			Struts2Util.renderJson("{\"status\" : \"false\"}");
//			
//		}
//		Struts2Util.renderJson("{\"status\" : \"true\"}");
//		return null;
//	}
//	
//	/**
//	 * @title 另外一种添加方法
//	 * @return
//	 * @throws IOException
//	 */
//	public String save() throws IOException {
//		User u = getCurrentUser();
//		try {
//			// 获取父节点对象
//			Org father = baseMgr.get(Org.class, org.getFatherOrg());
//			org.setIsDelete("F");
////			if(org.getInType().equals(2L)){
////				org.setInType(father.getInType());
////			}
//			org.setFatherOrg(father.getOrgID());
//			org.setInLevel(father.getInLevel() + 1L);
//			org.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
//			org.setCreater(u.getUserName());
//			// 保存添加的对象
//			baseMgr.save(org);
//			// 用于刷新界面后让其默认选中
//			select = org.getOrgID().toString();
//			// 设置默认打开的节点序列
//			open = orgMgr.getFathers(org.getOrgID());
//		} catch (Exception e) {
//				e.printStackTrace();
//		}
//		return index();
//	}
//
//	
	@Override
	public Org getModel() {
		return org;
	}
//
//	public Long getNewIntype() {
//		return newIntype;
//	}
//
//	public void setNewIntype(Long newIntype) {
//		this.newIntype = newIntype;
//	}
//
//	public String getSelect() {
//		return select;
//	}
//
//	public void setSelect(String select) {
//		this.select = select;
//	}
//
//	public List<String> getOpen() {
//		return open;
//	}
//
//	public void setOpen(List<String> open) {
//		this.open = open;
//	}
//

}
