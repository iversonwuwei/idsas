<!-- 这个jsp是为了解决大数据浏览器卡死或直接不显示而写的，用法如下 -->
<%--
<s:include value="/common/z-tree.jsp">
	<!-- 给树起名 -->
	<s:param name="treeName" value="'fff'" />
	<!-- 类型，先写了一个 checkbox  如果是空的话，或者不写这个参数 树就展示为没有复选框 -->
	<s:param name="treeType" value="'checkbox'"/>
	<!-- 值栈里的那个json字符串 -->
	<s:param name="treeList" value="%{treeList}"/>
	<!-- 用于保存选中数据id的隐性input标签    其中，数据格式为1,2,3,4,54,... -->
	<s:param name="hiddenId" value="'fff'"/>
</s:include>
 --%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "No-cache");
	response.setDateHeader("Expires", -10);
	String path = request.getContextPath() + "/";
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<head>
<title>Ztree</title>
<meta name="author" content="LiuGuilong" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="<%=basePath%>scripts/common/JQuery zTree v3.5.12/css/zTreeStyle/zTreeStyle.css" type="text/css" />
<script type="text/javascript" src="<%=basePath%>scripts/common/JQuery zTree v3.5.12/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/common/JQuery zTree v3.5.12/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/common/JQuery zTree v3.5.12/js/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/common/JQuery zTree v3.5.12/js/jquery.ztree.all-3.5.js"></script>
</head>
<script type="text/javascript">
$(function() {
	var nodes = new Array();
	// 将字符串用json变成对象然后遍历添加到nodes数组中
	$.each($.parseJSON('${param.treeList}'), function(i, data) {
		nodes.push({
			id : data.id,
			pId : data.pid,
			name : data.name,
			open: data.open,
			icon: "true" == data.ischildren ? false : "<%=basePath%>images/dtree/ico_tree01.gif",
			chkDisabled: data.chkdisabled,// 是不是不让复选
			checked : data.checked
		});
	});
	// 创建树
	$.fn.zTree.init($("#ztree${param.treeName}"), setting, nodes);
	// 获取选中的值附于该页面提供的input和父页面的隐性input中
	if('' != '${param.treeType}'){
		getZteeSelected();
	}
});
</script>
<c:if test="${param.treeType != null && param.treeType != ''}">
<table border="0" cellpadding="0" cellspacing="0" style="width: 280px; border: 0px;">
	<tr>
		<td style="border: 0px; width: 125px;"><input id="selectinput${param.treeName}" type="text" value="Empty" disabled="disabled" /></td>
		<td style="border: 0px; padding-top: 3px; width: 80px;"><input id="check${param.treeName}" type="button" class="select_button" onclick="showZtree()" value="  select" title="select" style="width:80px;background: url('<%=basePath%>images/select_back_ico.gif') no-repeat #e9e9e6;cursor: pointer;height:20px;margin-left: 10px;"/>
		</td>
		<td style="border: 0px; width: 45px;"><font id="isSelect${param.treeName}" color="red"></font></td>
	</tr>
</table>
<!-- 透明层 -->
<div id="b_div${param.treeName}" class="b_div"></div>
<!--tree层 -->
<div id="m_div${param.treeName}" class="m_div">
	<div id="i_div${param.treeName}" class="i_div">
		<div class="poptitle" style="cursor: move;">
			<ul>
				<li>&nbsp;&nbsp;&nbsp;Select Data</li>
				<li style="float: right; margin-right: 5px; margin-top: 3px;">
				<img src="<%=basePath%>images/button_sure.jpg" style="cursor: pointer;" alt="ok" onclick="$('.b_div').hide();$('.m_div').hide();getZteeSelected();" /></li>
				<li style="float: right; margin-right: 5px; margin-top: 3px;">
				<img src="<%=basePath%>images/button_cancel.jpg" style="cursor: pointer;" alt="ok" onclick="$('.b_div').hide();$('.m_div').hide();cancelZteeSelected();" /></li>
			</ul>
		</div>
		<div style="border-top: 1px solid #C1CDDB; width: 400px;"></div>
		<div id="ztree${param.treeName}"  class="ztree"style="margin-top: 10px; margin-left: 10px; height: 300px; overflow: auto;">
		</div>
	</div>
</div>
</c:if>	
	<script type="text/javascript">
	// 显示树
	function showZtree() {
		$(".b_div").css("height", $(window).height()).css("width", $(window).width()).css("top", 0).css("left", 0).show();// 显示遮罩层
		$(".m_div").css("top", getTop()).css("left", ($(window).width() - $(".m_div").width()) / 2).show();// 显示主数据层
	}
	// 给class=m_div的div一个高度(不能小于0)
	function getTop() {
		var top = ($(window).height() - $(".m_div").height()) / 2;
		return top < 0 ? 0 : top;
	}
	// ztree的设置
	var setting = {
		check : {
			enable : '${param.treeType}' == 'checkbox'
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		view : {
			showIcon : true
		}
	};
	
	// 获取选中的节点的id和名称
	function getZteeSelected() {
		var ztree = $.fn.zTree.getZTreeObj("ztree${param.treeName}");
		var nodes = ztree.getCheckedNodes(true);
		var str = "", id = "";
		$.each(nodes, function(i, n) {
			str += n.name + ";";
			id += n.id + ";";
		});
		$("#selectinput${param.treeName}").val("" == str ? "Empty" : str).attr( "title", str);
		$("#${param.hiddenId}").val(id.substring(0, id.length - 1));
	}
	// 取消按钮
	function cancelZteeSelected() {
		var ztree = $.fn.zTree.getZTreeObj("ztree${param.treeName}");
		ztree.destroy();
		var nodes = new Array();
		// 将字符串用json变成对象然后遍历添加到nodes数组中
		$.each($.parseJSON('${param.treeList}'), function(i, data) {
			nodes.push({
				id : data.id,
				pId : data.pid,
				name : data.name,
				open: data.open,
				icon: "true" == data.ischildren ? false : "<%=basePath%>images/dtree/ico_tree01.gif",
				chkDisabled: data.chkdisabled,// 是不是不让复选
				checked : data.checked
			});
		});
		// 创建树
		$.fn.zTree.init($("#ztree${param.treeName}"), setting, nodes);
		
		if('' != '${param.treeType}'){
			getZteeSelected();
		}
	}
	function filter(treeId, parentNode, childNodes) {
		if (!childNodes)
			return null;
		if (null != childNodes[i] && typeof childNodes[i] != 'undefined') {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
		}
		return childNodes;
	}
</script>
<c:if test="${param.treeType == null || param.treeType == '' }">
	<div id="ztree${param.treeName}"  class="ztree"style="margin-top: 10px; margin-left: 10px; overflow: auto;"></div>
</c:if>	