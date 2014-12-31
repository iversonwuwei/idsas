<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<title>组织机构</title>
		<style type="text/css">
			.addjd {
			width:300px; height:200px; margin-top:50px; margin-left:25%;
			}
			.addjd ul{}
			.addjd li{list-style:none; height:35px;}
			.addjd input{width:120px; border:solid 1px #a2b3bd; background:url(<%=basePath%>images/input_bg.jpg) repeat-x; font-size:14px; height:18px;}
			.addjd select{width:120px;border:solid 1px #a2b3bd; background:url(<%=basePath%>images/input_bg.jpg) repeat-x; font-size:12px; height:18px;}
		</style>
	</head>

<body>
<div class="tools">
  <div class="tools">
			<div class="tools_l">
				当前位置&nbsp;：&nbsp;组织结构&nbsp;&gt;&gt;&nbsp;<span style="font-weight:bold">组织机构</span>
			</div>
  <div class="tools_r">
	  <ul>
	  <c:if test="${editble!=0}">
      <li style="width:82px;"><a style=" cursor:pointer; width:80px;" onclick="remove();"><img src="<%=basePath%>images/ico_home.gif" border="0" align="absmiddle" /> 删除节点</a></li>
      <li style="width:82px;"><a style="cursor:pointer; width:80px;" onclick="rename();"><img src="<%=basePath%>images/ico_new.GIF" border="0" align="absmiddle" /> 编辑节点</a></li>
      <li style="width:90px;"><a style="cursor:pointer; width:88px;" onclick="xiangxi();"><img src="<%=basePath%>images/ico_new.GIF" border="0" align="absmiddle" /> 添加子节点</a></li>
	  </c:if>
    </ul>
  </div>
</div>
</div>
<div class="main" style=" margin-left: 0px;padding-left: 0px;">
  <div style="overflow:auto;width:43%;height:360px; border:solid 1px #ccc; float:left; margin-top:15px; margin-left: 0px;padding-left: 0px;background: #eee">
    <div style=" width:100%; background:url(<%=basePath%>images/zzjg_t_bg.jpg) repeat-x; height:29px; font-weight:bolder; line-height:29px;"><span style="margin-left:15px;">组织机构图</span></div>
    <div>
  	  <div style="float: left; margin-left: 0px;padding-left: 0px;">
			<form action="<%=basePath%>authority/org!save" method="POST">
				<script type="text/javascript">
					//这里设置树默认打开节点ID的集合
					var open = new Array();
					// 这里设置树默认选择节点ID的集合
					var select = new Array();
					// 设置默认选中节点
					function createSelectNode() {
						if ("" != "${select}") {
							select.push('${select}');
						}
					}
					// 设置默认打开的节点(这里是在第二种添加方式[xiangxi]中保存后刷新界面后以此展开节点)
					function createOpenTree() {
						<c:forEach items="${open}" var="o">
							open.push("${o}");
						</c:forEach>
					}
					$(function() {
						createOpenTree();
						createSelectNode();
						$.ajaxSetup({cache:false});
						$("#orgtree").jstree({
							"plugins" : [ "themes", "json_data", "ui", "crrm", "dnd", "search", "types"],
							"json_data" : {
								"ajax" : {
									"url" : "<%=basePath%>authority/org!getTree",
									"data" : function(n) {
										return {
											"id" : n.attr ? n.attr("id") : 0,
											"type" : n.attr ? n.attr("rel") : "default"
										};
									}
								}
							},
							"core" : {
								"animation" : 150,
								"strings" : {
									loading : "正在加载，请稍后 ...",
									new_node : "新节点"
								},
								// 默认打开节点的集合(js Array)
								"initially_open" : open
							},
							"ui" : {
								initially_select : select
							},
							"themes" : {
								// 插件提供的样式，共有apple,classic,default,default-rtl
								"theme" : "default",
								// 是否显示该样式的节点名称前的图标
								"icons" : false
							},
							"types" : {
								"valid_children" : [ "root" ],
								"types" : {
									"root" : {
										//"icon" : {
										//	"image" : contextPath + "scripts/jsTree.v.1.0rc2/_demo/root.png"
										//},
										"valid_children" : [ "default" ],
										"hover_node" : false,
										"move_node" : false,
										"delete_node" : false
									},
									"line" : {
										"valid_children" : [ "default" ],
										"create_node" : false ,
										"move_node" : false
									},
									"default" : {
										//"icon" : {
										//	"image" : contextPath + "scripts/jsTree.v.1.0rc2/_demo/folder.png"
										//},
										"valid_children" : [ "default" ],
										"move_node" : false
									}
								}
							}
						}).bind("create.jstree", function(e, data) {
							if(-1 == data.rslt.parent) {
								alert("无法新增根节点！");
								$.jstree.rollback(data.rlbk);
								return;
							}
							if(data.rslt.name.length > 25) {
								alert("名称最大允许输入25个字符！");
								$.jstree.rollback(data.rlbk);
								return;
							}
							alert($.trim($("#myname").val()));
							$.post("<%=basePath%>authority/org!add", {
								"id" : data.rslt.parent.attr("id"),
								"name" : $.trim($("#myname").val())
							},// 回调函数的d是后台传回的参数，这里我用的是json格式的参数，故一下采用'd.xxx'格式
							function(d) {
								// 一下是根据传回参数做出相应的操作
								if (d.status == "cannot") {
									alert("该节点无法继续添加子节点");
									// 该方法用于jsTree自带的回滚功能
									$.jstree.rollback(data.rlbk);
								}
								if (d.status == "repeat") {
									alert("同级不能有相同名称");
									$.jstree.rollback(data.rlbk);
								}
								if (d.status == "error") {
									alert("添加子节点失败");
									$.jstree.rollback(data.rlbk);
								}
								// 添加成功后会将生成的id(d.id)赋给新节点
								if (d.status == "success") {
									$(data.rslt.obj).attr("id", d.id);
									alert("添加节点成功");
								}
							});
						}).bind("remove.jstree", function(e, data) {
							// 同样这里会对树的方法和ajax同上
							if(-1 == data.rslt.parent){
								alert("根节点无法删除");
								return;
							}
							$.post("<%=basePath%>authority/org!remove", {
								"id" : data.rslt.obj.attr("id")
							}, function(r) {
								if (!r.status) {
									alert("删除失败");
									$.jstree.rollback(data.rlbk);
								}else{
								alert("删除成功");
								}
							});
						}).bind("rename.jstree", function(e, data) {
							$.post("<%=basePath%>authority/org!rename", {
								"id" : data.rslt.obj.attr("id"),
								"name" : data.rslt.new_name
							}, function(d) {
								if (d.status == "repeat") {
									alert("同级不能有相同名称");
									$.jstree.rollback(data.rlbk);
								}
								if (d.status == "error") {
									alert("修改失败");
									$.jstree.rollback(data.rlbk);
								}
								alert("保存成功");
							});
						}).bind("select_node.jstree", function(e, data) {
							// 这里我将点击的节点的id放在了一个隐藏域中以扩展其功能
							$("#df").val(data.rslt.obj.attr("id"));
							$("#xxx").hide();
						});
					});
				</script>
				<script type="text/javascript">
					function addOrg() {
						$("#orgtree").jstree("create", null);
					}
		
					function rename() {
						if($("#df").val() == null||$("#df").val() ==""){
							alert("请选择你要编辑的节点");
							return;
						}
						$("#orgtree").jstree("rename");
					}
		
					function remove() {
						if($("#df").val() == null||$("#df").val() ==""){
							alert("请选择你要删除的节点");
							return;
						}
						if (confirm("删除该数据将导致其所属数据也被删除，你确定吗？")) {
							$("#orgtree").jstree("remove");
						}
					}
					// 另外一种添加方式，用与很多属性的写入
					// 当前实体类没有那么多的参数，此处仅仅为了演示
					function xiangxi() {
						if($("#df").val() == null||$("#df").val() ==""){
							alert("请选择你要添加的根节点");
							return;
						}
						if($("#df").val() == 1){
							$("#jigou").show();
							$("#orgdepart").show();
							$("#inType").empty();
							$("#inType").append("<option value='0'>公司机构</option>");
							$("#inType").append("<option value='1'>事业部机构</option>");
							
						}else{
							$("#jigou").hide();
							$("#orgdepart").hide();
							$("#inType").empty();
							$("#inType").append("<option value='2'></option>");
						}
						$("#xxx").show();
					}
					// xiangxi添加方法会将本届面刷新
					function save() {
						if ("" == $.trim($("#orgName").val())) {
							alert("请输入名称");
							return;
						}
						$("form").submit();
					}
				</script>
							<div id="orgtree" style="overflow:auto;"></div>
		</div>
    </div>
  </div>
  <div id="xxx" style="width:43%; display:none; height:360px; border:solid 1px #ccc; float:right; margin-right:5%; margin-top:15px;">
  <div style="width:100%; background:url(<%=basePath%>images/zzjg_t_bg.jpg) repeat-x; height:29px; font-weight:bolder; line-height:29px;"><span style="margin-left:15px;">添加子节点</span></div>
  <div class="addjd">
    <ul>
      <li style="width:90px; float:left; text-align:right;">节点名称：</li>
      <li style="width:200px; float:right;">
        <label>
        <input id="orgName" name="orgName" value=""/>
        </label>
      </li>
      <li id="orgdepart" style="width:90px; float:left; text-align:right;">选择部门：</li>
      <li id="jigou" style="width:200px; float:right;">
      		<select id="inType" name="inType" style="width: 120px">
					<option value="0">公司机构</option>
					<option value="1">事业部机构</option>
			</select>
      </li>
      <li style="width:90px; cursor:pointer; float:left; text-align:right;" onclick="save()"><img src="<%=basePath%>images/save.jpg" /></li>
      <li style="width:90px; float:left;cursor:pointer; text-align:right;"  onclick="$('#xxx').hide()"><img src="<%=basePath%>images/cancle.jpg"/></li>
    </ul>
  </div>
  </div>
  <input type="hidden" id="df" name="fatherOrg" value="" /> 
</form>
</div>
</body>
</html>
