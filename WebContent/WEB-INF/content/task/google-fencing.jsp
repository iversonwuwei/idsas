<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<%response.setHeader("Pragma","No-cache"); response.setHeader("Cache-Control","No-cache"); response.setDateHeader("Expires",-10);%> 
<script type="text/javascript" src="<%=basePath%>scripts/common/jsTree.v.1.0rc2/jquery.jstree.js"></script>
<link href="<%=basePath%>scripts/common/jsTree.v.1.0rc2/themes/apple/style.css" rel="stylesheet" type="text/css" />

<script charset="UTF-8" type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=drawing&key=AIzaSyAs2HwT6PEJzn2P8WYiSScJpq0oigo_pGA&sensor=false"></script>
 
<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/specific/task/google-map.js"></script>
<html>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
 
 
 
<script type="text/javascript">
$(function(){
	createTree();
});
</script>
	<body onload="initMap();">
	 
		<custom:navigation father="Task Management" model="Geo Fencing" addPath="task/google-fencing"   addMethod="add" />
		 
		
	<br/>
	<table  ><tr>
	<td>
	 <div id="memo" class="mains" style="margin-left: 50px;overflow: auto;height: 500px">
	 
 
		<div style="float: left;width: 100%;">
		<table style="width: 80%;" border="0">
			<tr>
				<td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input   type="button" value="Edit Caption " onclick="rename1();" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input   type="button" value="Delete" onclick="remove();" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			</tr>
			

			<tr>
			<td colspan="2"><div id="demo" style="width: 400px; height: 375px; overflow: auto; font-size: 12px; float: left;"></div></td>
	 
	      </tr>
</table>
</div>
 
<div>
	</td>
	<td  valign="top"  width="650px" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 	<div id="basicMap" style="width:600px; height:390px;"></div>
	</td>
</tr>
		
 
<br>

</table>

 
<form name="queryResult" id="feacn" action="<%=basePath%>task/google-fencing!edit" method="post">
		  <s:hidden name="editble" value="%{editble}" />
          <input type="hidden" id="df"   value="" /> 
           <input type="hidden" id="fa"   value="" /> 
        <center>
		 
	</center>
<script type="text/javascript">
  
	function rename1(){
		if(-1 != $("#fa").val()){
			alert("Please select Caption!");
			return false;
		}
		var df=$("#df").val();
		document.forms['feacn'].action=basePath+'task/google-fencing!edit?id='+df+"&date="+new Date();
        document.forms['feacn'].submit();
		 
	}
	function createTree() {
		$.ajaxSetup({cache:false});
		/*创建树的大方法*/
		$("#demo").jstree({
			/* 插件*/
			// 插件数组参数里是添加的是该绑定方法的jstree应用到什么功能
			// 例如如果我会用到"themes"功能，却没在这里添加，那么执行时会报错
			"plugins" : [ "themes", "json_data", "ui", "crrm", "dnd", "search", "types" ],
			/*数据类型*/
			"json_data" : {
				"ajax" : {
					"url" : basePath + "task/google-fencing!getTree",
					// 此处的data是向后台传值的数据，并非回调函数的data
					// 此处没有回调函数，或者说我没找到如何对这里的ajax添加回调
					// 那么也就意味着该控件会根据ajax返回值自行绘制下一级的节点，我们在这期间不必对其做任何操作
					"data" : function(n) {
						return {
							// 初始传回去的id为-1，之后再点击直接传回节点id
							"id" : n.attr ? n.attr("id").replace("node_", "") : "0",
							"type" : n.attr ? n.attr("rel") : "default"
						};
					}
				}
			},
			/*核心配置*/
			"core" : {
				// 打开子节点所用时间的毫秒数
				"animation" : 150,
				// 正在打开节点
				"strings" : {
					// 正在打开节点所显示的字符串
					loading : "Pro, are you trying to load, please wait ...",
					// 添加新节点的默认名称
					new_node : "The new node"
				}
			//,
				// 默认打开节点的集合(js Array)
				//"initially_open" : open
			},
			/*UI设置*/
			//"ui" : {
				// 默认选择节点的id
			//	"initially_select" : select
			//},
			/*类型设置*/
			"types" : {
				// valid_children我实在不知道什么意思
				"valid_children" : [ "root" ],
				"types" : {
					"root" : {
						"valid_children" : [ "default" ],
						// 是否可以删除等配置参数
						"delete_node" : false,
						"hover_node" : false,
						"move_node" : false
					},
					// 后台自定义的末节点rel名称
					"last" : {
						// 为其设置图标
						"icon" : {
							"image" : basePath + "images/ico_tree02.gif"
						}
					},
					"default" : {
						"valid_children" : [ "default" ],
						"move_node" : false
					},
					"valid_children" : [ "default" ],
					// 禁止移动子节点
					"move_node" : false
				}
			},
			//"sort" : function (a, b) {
		    //	return  $(a).attr("sort") > $(b).attr("sort") ? 1 : -1;
		    	//(jQuery(a).attr ? parseFloat(jQuery(a).attr("sort")) : 1) > (jQuery(b).attr ? parseFloat(jQuery(b).attr("sort")) : 0) ? 1 : -1;
			//},
			/*样式设置*/
			"themes" : {
				// 插件提供的样式，共有apple,classic,default,default-rtl
				"theme" : "default",
				// 是否显示该样式的节点名称前的图标
				"icons" : false
			},
			/*搜索方法*/
			"search" : {
				// 屏蔽大小写
				"case_insensitive" : true,
				// 从后台模糊查询的请求
				"ajax" : {
					"type": "POST",
					"url" : basePath+"task/google-fencing!search"
          		}
			}
		})
		/*绑定添加方法*/
		.bind("create.jstree", function(e, data) {
			// 这里根节点的父节点为-1，用此判断其是否为根节点，是则禁止添加根节点，也就是和根节点同级的节点
//			if (-1 == data.rslt.parent) {
//				alert("无法新增根节点");
//				$.jstree.rollback(data.rlbk);
//				return;
//			}
			// 限定新添加节点的名称
			if (data.rslt.name.length > 25) {
				alert("Name Enter the maximum allowable 25 characters");
				$.jstree.rollback(data.rlbk);
				return;
			}
			// 将父节点ID和名称传到后台
			$.post(basePath + "task/google-fencing!add", {
				"id" : data.rslt.parent.attr("id"),
				"name" : $.trim(data.rslt.name)
			},
			// 回调函数的d是后台传回的参数，这里我用的是json格式的参数，故一下采用'd.xxx'格式
			function(d) {
				// 一下是根据传回参数做出相应的操作
				if (d.status == "cannot") {
					alert("The node can not continue to add child nodes");
					// 该方法用于jsTree自带的回滚功能
					$.jstree.rollback(data.rlbk);
				}
				if (d.status == "repeat") {
					alert("At the same level can not have the same name");
					$.jstree.rollback(data.rlbk);
				}
				if (d.status == "error") {
					alert("Failed to add a child node");
					$.jstree.rollback(data.rlbk);
				}
				// 添加成功后会将生成的id(d.id)赋给新节点
				if (d.status == "success") {
					$(data.rslt.obj).attr("id", d.id);
				}
			});
		})
		/*绑定重命名方法*/
		.bind("rename.jstree", function(e, data) {
			// 将节点id和新输入的名称传到后台
			$.post(basePath + "task/google-fencing!rename", {
				"id" : data.rslt.obj.attr("id"),
				"name" : data.rslt.new_name
			}, function(d) {
				if (d.status == "repeat") {
					alert("At the same level can not have the same name");
					$.jstree.rollback(data.rlbk);
				}
				if (d.status == "error") {
					alert("Modification fails");
					$.jstree.rollback(data.rlbk);
				}
			});
		})
		/*绑定删除方法*/
		.bind("remove.jstree", function(e, data) {
			// 同样这里会对树的方法和ajax同上
			
			$.post(basePath + "task/google-fencing!remove", {
				"id" : data.rslt.obj.attr("id")
			}, function(r) {
				if ("error" == r.status) {
					alert("Delete failed");
					$.jstree.rollback(data.rlbk);
				}
			});
		})
		/*绑定点击节点事件*/
		.bind("select_node.jstree", function(event, data) {
			
			// 这里我将点击的节点的id放在了一个隐藏域中以扩展其功能
			$("#df").val(data.rslt.obj.attr("id"));
			$("#fa").val(data.rslt.obj.attr("rel"));
			if(-1 == data.rslt.obj.attr("rel")){
				increate1($("#df").val());
			}
			
		});
	}

	
</script>

	<script type="text/javascript">
	// 添加新节点
	function add() {
		$("#demo").jstree("create", null, "last");
	}
	// 节点删除
	function remove() {
		// 上面点击节点方法将节点id放入了隐藏于
		// 那么在这里，我查看隐藏域中的value是否为空来判断其是否选中了一个节点
		if ("" == $("#df").val()) {
			alert("Please select a node");
			return;
		}
		if (confirm("Sure you talk about the current node and its child nodes be deleted?")) {
			// 调用jstree中绑定了的删除方法
			$("#demo").jstree("remove");
		}
	}
	// 调用jstree中绑定的重命名方法
	function rename() {
		$("#demo").jstree("rename");
	}
	// 另外一种添加方式，用与很多属性的写入
	// 当前实体类没有那么多的参数，此处仅仅为了演示
	function xiangxi() {
		if (null == $("#df").val() || "" == $("#df").val()) {
			alert("Please select a node");
			return;
		}
		$("#xxx").show();
	}
	// xiangxi添加方法会将本届面刷新
	function save() {
		if ("" == $.trim($("#name").val())) {
			alert("Please enter a name");
			return;
		}
		$("form").submit();
	}
	// 将搜索输入框里的值作为参数传到后台进行搜索
	function search(){
		if ("" == $.trim($("#search_text").val())) {
			return;
		}
		$("#demo").jstree("close_all");// 关闭全部
		$("#demo").jstree("search", $("#search_text").val());
	}
	
	</script>
			 
		</form>
	</body>
</html>