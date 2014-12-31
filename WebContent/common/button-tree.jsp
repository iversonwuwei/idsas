<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.opensymphony.xwork2.ognl.OgnlValueStack"%>
<%response.setHeader("Pragma","No-cache"); response.setHeader("Cache-Control","No-cache"); response.setDateHeader("Expires",-10);%> 
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
		<s:param name="buttonValue" value="'***'" /> ：按钮value
		<s:param name="treeName" value="'***'" /> ：树名(js name)
		<s:param name="treeType" value="'****'" />	：树类型：	dtree：(dTree)只能查看；
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
	<title>按钮弹出tree</title>
	<meta name="author" content="" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="<%=basePath%>css/fileuploader.css" rel="stylesheet" type="text/css">
</head>
<style>
	*{margin:0;padding:0;}
	.divBackGround {
		display: none;
		filter: alpha(opacity =   70);	/* 修改IE浏览器的透明度 */
		-moz-opacity: 0.7;	/* 修改MOZ浏览器的透明度 */
		opacity: 0.7;	/* 修改MOZ浏览器的透明度 */
		background: #ccc;
		position: absolute; /* 绝对路径 */
		z-index: 99;
		text-align: center;
	}
	.dTreeNode p{
	text-align: center
	}
	.mainInputTreeDiv {
		display: none;	
		width: 447px;
		height: 400px;
		z-index: 100;
		position: absolute;
	}
	
	.innerInputTreeDiv {
		filter: alpha(opacity = 100);
		-moz-opacity: 1.0; /* Moz + FF */
		opacity: 1.0;
		float:left;
		width: 400px;
		height: 365px;
		overflow: hidden;
		border-color:#2383C0; 
		border-style:solid; 
		border-width:1px 1px 1px 1px;
		background-color: #FFFFFF;
	}
</style>
<script type="text/javascript">
	var TreeDiv =function(){};
	if (typeof(buttonTree) == "undefined") {
		var buttonTree = new Object();
	}
	buttonTree['${param.treeName}'] = new TreeDiv();

	TreeDiv.prototype.openDivTree=function(){
		$("#backGroundDiv"+'${param.treeName}')
		.addClass("divBackGround")
		.css("left",0)
		.css("top",0)
		.width($(window).width())
		.height($(window).height())
		.show();

		
		$("#mainInputTreeDiv"+'${param.treeName}')
		.css("left",(($(window).width()-400)/2))
		.css("top",getTopSize())
		.addClass("mainInputTreeDiv").show("quick");
	
		
		$("#inputTreeDiv"+'${param.treeName}')
			.addClass("innerInputTreeDiv")
			.css("left",($(window).width()-400)/2)
			.css("top",getTopSize())
			.show("quick");
	
		$("#inputTreeDiv"+'${param.treeName}').focus();
	};

	TreeDiv.prototype.divTreeBlur=function(){
		$("#inputTreeDiv"+'${param.treeName}').hide("quick",function (){
			$("#backGroundDiv"+'${param.treeName}').hide();
		});
		$("#mainInputTreeDiv"+'${param.treeName}').hide("quick");
		buttonTree['${param.treeName}'].setInputTreeSelectedText();
	};
	TreeDiv.prototype.divTreeBlurClose=function(){
		$("#inputTreeDiv"+'${param.treeName}').hide("quick",function (){
			$("#backGroundDiv"+'${param.treeName}').hide();
		});
		$("#mainInputTreeDiv"+'${param.treeName}').hide("quick");
		if ('${param.isAdd}' != null && "" != '${param.isAdd}') {
			zz();
		} 
		if ("" == '${param.isAdd}' || null == '${param.isAdd}') {
			if('${treeType}' == 'checkbox') {
				$(":checkbox").attr("checked", false);
			}
			if('${treeType}' == 'uniqueRadio') {
				$(":radio").attr("checked", false);
			}
			buttonTree['${param.treeName}'].setInputTreeSelectedText();
		}
		
	};

	TreeDiv.prototype.setInputTreeSelectedText=function(){
		var selectInputValueArray = tree['${param.treeName}'].getCheckedValue(true);
		var selectInputValueStr = "";
		if(null != selectInputValueArray && "" != selectInputValueArray){
			for ( var num = 0; num < selectInputValueArray.length; num++) {
				selectInputValueStr += selectInputValueArray[num] + ";";
				if(num > 2)break;
			}
			$("#inputTreeSelectedText"+'${param.treeName}').val(selectInputValueStr);
		}else{
			$("#inputTreeSelectedText"+'${param.treeName}').val("Empty");
		}

		if ('${treeType}' == 'dtree' || '${treeType}' == 'checkbox' || '${treeType}' == 'uniqueRadio'){
			var selectInputNode = tree['${param.treeName}'].getCheckedNodes(true);
		}
	};
	// 如果它跑上面去了，就点不到确定了，所以判断一下
	function getTopSize(){
		var topSize = ($(window).height()-400)/2
		return topSize < 0 ? 0 : topSize;
	}
	$(function(){
		buttonTree['${param.treeName}'].setInputTreeSelectedText();
	});
</script>
<table border="0" cellpadding="0" cellspacing="0" style="width: 280px;border: 0px;">
	<tr>
	 	<td style="border: 0px;width: 125px;">
	 		<input id="inputTreeSelectedText${param.treeName}" type="text" value="Empty" disabled="disabled"/> 
	 	</td>
	 	<td style="border: 0px; padding-top: 3px; width: 80px;">
	 		<input id="check${param.treeName}" type="button" class="select_button" onclick="buttonTree['${param.treeName}'].openDivTree();" value="  select" title="select" style="width:80px;background: url('<%=basePath%>images/select_back_ico.gif') no-repeat #e9e9e6;cursor: pointer;height:20px;margin-left: 10px;"/>
	 		<%-- <img style="cursor: pointer;" src="<%=basePath%>images/button_select.gif" alt="选择" onclick="buttonTree['${param.treeName}'].openDivTree();"/> --%>
	 	</td>
	 	<td style="border: 0px; width: 45px;">
	 		<font id="isSelect${param.treeName}" color="red"></font>
	 	</td>
	</tr>
</table>
<!-- 透明层 -->
<div id="backGroundDiv${param.treeName}" class="divBackGround"></div>
<!-- inputTree层 -->
<div id="mainInputTreeDiv${param.treeName}" class="mainInputTreeDiv">
	<div id="inputTreeDiv${param.treeName}"  class="innerInputTreeDiv" >
		<div class="poptitle" style="cursor: move;">
    		<ul>
		  		<li>&nbsp;&nbsp;&nbsp;Select Data</li>
		  		<li style="float:right; margin-right:5px; margin-top:3px;">
  					<img src="<%=basePath%>images/button_sure.jpg" style="cursor: pointer;" alt="Confirm" onclick="buttonTree['${param.treeName}'].divTreeBlur();"/>
  				</li>
  				<li style="float:right; margin-right:5px; margin-top:3px;">
  					<img src="<%=basePath%>images/button_cancel.jpg" style="cursor: pointer;" alt="Cancel" onclick="buttonTree['${param.treeName}'].divTreeBlurClose();"/>
  				</li>
    		</ul>
		</div>
		<div style="border-top:1px solid #C1CDDB; width: 400px;"></div>
		<div style="margin-top:10px;margin-left: 10px;height:300px;overflow: auto;">
			<s:if test="treeType == 'dtree' || treeType == 'checkbox' || treeType == 'uniqueRadio'">
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
					if("" != "${dataStr}") {
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
			</script>
			</s:if>
		</div>
	</div>
	<div id="innerOkDiv${param.treeName}" class="innerOkDiv"></div>
</div>
<script>
function zz(){
	$(":checkbox").attr("checked", false);
	buttonTree['${param.treeName}'].setInputTreeSelectedText();
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
			} 
			if('${param.treeType}' == 'checkbox'){
				for (var n = 0; n < fun.funcs.length; n++) {
					if(typeof tree['${param.treeName}'].co(fun.funcs[n].menu) != 'undefined'){
						tree['${param.treeName}'].co(fun.funcs[n].menu).checked = true;
					}
				}
			}
		}
	}
}
</script>