<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.opensymphony.xwork2.ognl.OgnlValueStack"%>
<%
	((OgnlValueStack) request.getAttribute("struts.valueStack")).set(
	"treeType", request.getParameter("treeType"));
	((OgnlValueStack) request.getAttribute("struts.valueStack")).set(
	"dataList", request.getParameter("dataList"));
	((OgnlValueStack) request.getAttribute("struts.valueStack")).set(
	"dataStr", request.getParameter("dataStr"));
	String path = request.getContextPath() + "/";
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%--
	title：自定义树形菜单
	Useage (JSP):
	<s:include value="/common/myTree.jsp">
		<s:param name="treeName" value="'***'" /> ：树名(js name)
		<s:param name="treeType" value="'****'" />	：树类型：	base：(jsTree)基本带增删改3按钮；
																show：(jsTree)只能查看带搜索框；
																dtree：(dTree)只能查看；
																checkbox：(dTree)带多选框；
																uniqueRadio：(dTree)带单选框
		<s:param name="propertys" value="'id,name,fatherID,inlevel,sort,url'" /> ：主要属性名url只用于dTree
		[jsTree]
		<s:param name="className" value="'***'" /> ：数据对应实体名(全名)
		<s:param name="level" value="'******'" /> ：最大限制层数，0为无限制
		[dTree]
		<s:param name="dataList" value="'listName'" /> : dTree的数据来源,使用dTree必填
		<s:param name="dataStr" value="'dataStr'" /> : 已被选择的树节点id串，用于初始化checkbox、uniqueRadio数据
	</s:include>
	注：checkbox、uniqueRadio树对象为tree['treeName']返回的节点id串数据，使用getDate('treeName')方法获得，返回结构："id1,id2,id3,..."
--%>
<head>
	<title>自定义树形菜单</title>
	<meta name="author" content="Leon Liu" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/JavaScript" src="<%=basePath%>scripts/common/dtree/input-dtree.js"></script>
	<style>
		.dTreeNode p{
	text-align: center
	}
	
	</style>
</head>
<!-- base or show use jsTree -->
<s:if test="treeType == 'base' || treeType == 'show'">
	<table>
		<tr>
			<td>
				<div class="search_g">
					<s:if test="treeType == 'base'">
					<input class="btnn" type="button" value="添加子节点 " onclick="create('${param.treeName}');"/>&nbsp;
					<input class="btnn" type="button" value="编辑选中节点 " onclick="rename('${param.treeName}');"/>&nbsp;
					<input class="btnn" type="button" value="删除选中节点 " onclick="remove('${param.treeName}');"/>
					&nbsp;
					</s:if>
					<input id="search_text" />
					<input class="btnn" type="button"  value="搜索 " onclick="search('${param.treeName}');"/>
				</div>
			</td>
		</tr>
		<tr height="200px">
			<td>
		  		<div id="${param.treeName}" style="width:400px; height:100%; overflow:auto;">
		  		</div>
			</td>
		</tr>
	</table>
</s:if>
<!-- checkbox or radio use dTree -->
<s:elseif test="treeType == 'dtree' || treeType == 'checkbox' || treeType == 'uniqueRadio'">
	<script type="text/javascript">
		if (typeof(tree) == "undefined") {
			var tree = new Object();
		}
		tree['${param.treeName}'] = new dTree("tree['${param.treeName}']", basePath + 'images/dtree/');
		tree['${param.treeName}'].config.useCookies = false;
		if('${param.treeType}' != 'dtree') {
			tree['${param.treeName}'].config.inputType = '${param.treeType}';
		}
		tree['${param.treeName}'].config.closeOthers = true;
		var pattern = "(.*)\\=(.*)";
		var str = '${param.dataList}';
		var pid = '${param.propertys}'.split(',')[0].replace(' ','');
		var pname = '${param.propertys}'.split(',')[1].replace(' ','');
		var pf = '${param.propertys}'.split(',')[2].replace(' ','');
		var purl = '${param.propertys}'.split(',')[5].replace(' ','');
		var id = "";
		var name = "";
		var fatherID = "";
		var url = "";
		str = str.substring(1, str.length - 1);	//去最外层大括号
		for(var i = 0, j = str.split('],').length; i < j; i ++) {
			var kv = str.split('],')[i].split('[')[1];
			for(var k = 0, l = kv.split(',').length; k < l; k ++) {
				var reg = kv.split(',')[k].match(pattern);
				if(reg[1].replace(' ','') == pid) {
					id = reg[2];
				} else if (reg[1].replace(' ','') == pname) {
					name = reg[2];
				} else if (reg[1].replace(' ','') == pf) {
					fatherID = reg[2];
				} else if (reg[1].replace(' ','') == purl) {
					url = reg[2];
				}
			}
			tree['${param.treeName}'].add(id, fatherID, name, "");
		}
		document.write(tree['${param.treeName}']);
		if('${param.treeType}' != 'dtree') {
			//set value
			if("" != "${dataStr}"){
				var fun = eval("(${dataStr})");
				if('${param.treeType}' == 'uniqueRadio') {
					if(typeof tree['${param.treeName}'].co(fun) != 'undefined'){
						if(typeof tree['${param.treeName}'].co(fun).checked != 'undefined'){
							tree['${param.treeName}'].co(fun).checked = true;
							tree['${param.treeName}'].openTo(fun);
						}
					}
				} else if('${param.treeType}' == 'checkbox'){
					for (var n = 0; n < fun.funcs.length; n++) {
						if(typeof tree['${param.treeName}'].co(fun.funcs[n].menu) != 'undefined'){
							tree['${param.treeName}'].co(fun.funcs[n].menu).checked = true;
						}
					}
				}
			}
		}
		//get selected value (value1,value2,...)
		function getDate(treeName) {
			var selids = tree[treeName].getCheckedNodes(true);
			var str = '';
			for (var n = 0; n < selids.length; n++) {
		    	str += selids[n];
		    	if (n != selids.length - 1) {
		        	str += ',';
		    	}
			}
		    return str;
		}
		function getDateValue(treeName) {
			var selids = tree[treeName].getCheckedValue(true);
			var str = '';
			for (var n = 0; n < selids.length; n++) {
		    	str += selids[n];
		    	if (n != selids.length - 1) {
		        	str += ',';
		    	}
			}
		    return str;
		}
	</script>
</s:elseif>

<script type="text/javascript">
	if('${param.treeType}' == 'base' || '${param.treeType}' == 'show') {
		$(function() {
			$.ajaxSetup({cache:false});
			$('#' + '${param.treeName}').jstree({
				"plugins" : [ 'themes', 'json_data', 'ui', 'crrm', 'dnd', 'search', 'types', 'sort' ],
				'json_data' : {
					'ajax' : {
						'url' : basePath + 'common/my-tree!getTree',
						'data' : function(n) {
							return {
								'id' : n.attr ? n.attr('id') : -1,
								'type' : n.attr ? n.attr('rel') : 'default',
								'className' : '${param.className}',
								'level' : '${param.level}',
								'idProperty' : '${param.propertys}'.split(',')[0].replace(' ',''),
								'nameProperty' : '${param.propertys}'.split(',')[1].replace(' ',''),
								'fatherProperty' : '${param.propertys}'.split(',')[2].replace(' ',''),
								'levelProperty' : '${param.propertys}'.split(',')[3].replace(' ',''),
								'sortProperty' : '${param.propertys}'.split(',')[4].replace(' ','')
							};
						}
					}
				},
		 		'search' : {
					'case_insensitive' : true,
					'ajax' : {
						'type': 'POST',
						'url' : basePath + 'common/my-tree!search',
						'data' : {
							'className' : '${param.className}',
							'level' : '${param.level}',
							'idProperty' : '${param.propertys}'.split(',')[0].replace(' ',''),
							'nameProperty' : '${param.propertys}'.split(',')[1].replace(' ',''),
							'fatherProperty' : '${param.propertys}'.split(',')[2].replace(' ',''),
							'levelProperty' : '${param.propertys}'.split(',')[3].replace(' ',''),
							'sortProperty' : '${param.propertys}'.split(',')[4].replace(' ','')
						}
		      		}
				},
				'core' : {
					'animation' : 150,
					'strings' : {
						loading : '正在加载，请稍后 ...',
						new_node : '新机构'
					}
				},
				'sort' : function (a, b) {
				    return (jQuery(a).attr ? parseFloat(jQuery(a).attr('sort')) : 0) > (jQuery(b).attr ? parseFloat(jQuery(b).attr('sort')) : 0) ? 1 : -1;
				},
				'types' : {
					'valid_children' : [ 'root' ],
					'types' : {
						'root' : {
							'valid_children' : [ 'default' ],
							'hover_node' : false,
							'move_node' : false,
							'delete_node' : false
						},
						'last' : {
							'valid_children' : [ 'none' ],
							'create_node' : false,
							'move_node' : false
						},
						'default' : {
							'valid_children' : [ 'default' ],
							'move_node' : false
						},
						'valid_children' : [ 'default' ],
						'move_node' : false
					}
				}
			}).bind('create.jstree', function(e, data) {
				if(-1 == data.rslt.parent) {
					alert('无法新增根节点！');
					$.jstree.rollback(data.rlbk);
					return;
				}
				if(data.rslt.name.length > 25) {
					alert('名称最大允许输入25个字符！');
					$.jstree.rollback(data.rlbk);
					return;
				}
				$.post(basePath + 'common/my-tree!addNode', {
					'className' : '${param.className}',
					'id' : (-1 != data.rslt.parent) ? data.rslt.parent.attr('id') : '',
					'name' : data.rslt.name,
					'className' : '${param.className}',
					'level' : '${param.level}',
					'idProperty' : '${param.propertys}'.split(',')[0].replace(' ',''),
					'nameProperty' : '${param.propertys}'.split(',')[1].replace(' ',''),
					'fatherProperty' : '${param.propertys}'.split(',')[2].replace(' ',''),
					'levelProperty' : '${param.propertys}'.split(',')[3].replace(' ',''),
					'sortProperty' : '${param.propertys}'.split(',')[4].replace(' ','')
				}, function(r) {
					if(r == 'duplicate'){
						alert('同级名称重复，请重新添加！');
						$.jstree.rollback(data.rlbk);
					}
					if(r == 'levelout'){
						alert('该机构不可增加子机构');
						$.jstree.rollback(data.rlbk);
					}
					if(r == 'error'){
						alert('数据库查询错误！');
						$.jstree.rollback(data.rlbk);
					}
					else{
						$(data.rslt.obj).attr('id', r.id);
					}
				});
			}).bind('rename.jstree', function(e, data) {
				if('root' == data.rslt.obj.attr('rel')) {
					alert('无法编辑根节点！');
					$.jstree.rollback(data.rlbk);
					return;
				}
				if(data.rslt.new_name.length > 25) {
					alert('名称最大允许输入25个字符！');
					$.jstree.rollback(data.rlbk);
					return;
				}
				$.post(basePath + 'common/my-tree!rename', {
					'id' : data.rslt.obj.attr('id'),
					'name' : data.rslt.new_name,
					'className' : '${param.className}',
					'level' : '${param.level}',
					'idProperty' : '${param.propertys}'.split(',')[0].replace(' ',''),
					'nameProperty' : '${param.propertys}'.split(',')[1].replace(' ',''),
					'fatherProperty' : '${param.propertys}'.split(',')[2].replace(' ',''),
					'levelProperty' : '${param.propertys}'.split(',')[3].replace(' ',''),
					'sortProperty' : '${param.propertys}'.split(',')[4].replace(' ','')
				}, function(r) {
					if (r == 'duplicate') {
						alert('同级下不能有重复的名称，请重新填写！');
						$.jstree.rollback(data.rlbk);
					} else if (!r.status) {
						$.jstree.rollback(data.rlbk);
					}
				});
			}).bind('click.jstree', function(event) {
				var eventNodeName = event.target.nodeName;
				if (eventNodeName == 'INS') {
				    return;
				} else{
				    return;
				}
			}).bind('remove.jstree', function(e, data) {
				if(!data.rslt.obj) {
					return;
				}
				$.post(basePath + 'common/my-tree!remove', {
					'id' : data.rslt.obj.attr('id'),
					'className' : '${param.className}',
					'level' : '${param.level}',
					'idProperty' : '${param.propertys}'.split(',')[0].replace(' ',''),
					'nameProperty' : '${param.propertys}'.split(',')[1].replace(' ',''),
					'fatherProperty' : '${param.propertys}'.split(',')[2].replace(' ',''),
					'levelProperty' : '${param.propertys}'.split(',')[3].replace(' ',''),
					'sortProperty' : '${param.propertys}'.split(',')[4].replace(' ','')
				}, function(r) {
					if (!r.status) {
						$.jstree.rollback(data.rlbk);
					}
				});
			});
		});
		if('${param.treeType}' == 'base') {
			// 创建节点
			function create(treeName) {
				$('#' + treeName).jstree('create', null, 'last');
			}

			// 重命名节点
			function rename(treeName) {
				$('#' + treeName).jstree('rename', null, 'last');
			}

			// 删除节点
			function remove(treeName) {
				if (confirm('确定要删除当前节点吗？ \n删除后，其子节点将一起被删除！')) {
					$('#' + treeName).jstree('remove');
				}
			}
		}

		//搜索功能
		function search(treeName) {
			$('#' + treeName).jstree('close_all');// 关闭全部
			if ('' == $.trim($('#search_text').val())) {
				return;
			}
			$('#' + treeName).jstree('search', $('#search_text').val());
		}
	}
</script>