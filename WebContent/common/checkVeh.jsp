<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.opensymphony.xwork2.ognl.OgnlValueStack"%>
<%response.setHeader("Pragma","No-cache"); response.setHeader("Cache-Control","No-cache"); response.setDateHeader("Expires",-10);%> 
<%
	((OgnlValueStack) request.getAttribute("struts.valueStack")).set(
	"checkname", request.getParameter("checkname"));
	((OgnlValueStack) request.getAttribute("struts.valueStack")).set(
	"orgid", request.getParameter("orgid"));
	((OgnlValueStack) request.getAttribute("struts.valueStack")).set(
	"checklist", request.getParameter("checklist"));
	String path = request.getContextPath() + "/";
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
	String CDN = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			 + "/erp/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%--
	title：选择车队后  弹出div 选择该车队下车辆 复选
	Useage (JSP):
	<s:include value="/common/checkVeh.jsp">  车队下拉框的name是orgid
	  <s:param name="checkname" value="'vehs'"/>    复选框的name的值
	  <s:param name="checklist" value="%{vehstr}"/> 被选中的车的id组成的字符串   用,分开   811,812,813
	  <s:param name="orgid" value="%{orgid}"/>  被选中的车队的id
	</s:include>
--%>
<head>
	<title>按钮弹出车辆</title>
	<link type="text/css" rel="stylesheet" href="<%=CDN%>css/fileuploader.css" />
	<link type="text/css" rel="stylesheet" href="<%=CDN%>css/check-veh.css" />
</head>
<script type="text/javascript">
	/* 初始加载，获得车队 */
	$(function(){
		$.ajax({
			url : basePath + "veh-select!getOrgs",
			type : "POST",
			async : false,
			data : {},
			dataType : "json",
			success : function(data) {
				for(var i = 0; i < data.length; i++) {
					if('${param.orgid}'!=null&&'${param.orgid}'!=''&&'${param.orgid}'==data[i].key){
						$("#orgid").append("<option value='"+data[i].key+"' selected='selected'>"+data[i].value+"</option>");
					}else{
						$("#orgid").append("<option value='"+data[i].key+"'>"+data[i].value+"</option>");
					}
				}
			},
			error : function(json) {
				alert("查询数据异常");
				window.history.back();
			}
		});
		change();	//生成车辆
		init();	//生成上次保存结果
	});
	
	/* 修改车队触发, 生成车辆 */
	function change(){
		getVehs();
	}
	
	/* 初始化上次选择结果 */
	function init(){
		var init;
		var arr = new Array();
		if('${param.checklist}'!=null&&'${param.checklist}'!=""){
			init='${param.checklist}';
			arr=init.split(",");
			for(var i=0;i<arr.length;i++){
				$("#ck"+arr[i]).attr("checked",true);
			}
		}
	}
	
	/* 选择数量限制 */
	function checkednum(id){
		var n = $("input[type=checkbox]:checked").length; 
		if(n>15)
		{
		   alert("最多只能选择15辆车！");
		   id.checked=false;
		}
	}
	
	/* 根据车队查询车辆 */
	function getVehs() {
		var orgid=$("#orgid").val();
		$.ajax({
			url : basePath + "veh-select!getVehicles",
			type : "POST",
			async : false,
			data : {
				"orgid":orgid
			},
			dataType : "json",
			success : function(data) {
				var tab=document.getElementById("showVeh");
				$("#showVeh").html("");
		
				for(var i = 0; i < data.length; i++) {
					if(i%3==0){
						var newTr = tab.insertRow();
						if(i<data.length){
							var newTd0 = newTr.insertCell();
							newTd0.innerHTML = "<input type='checkbox' id='ck"+data[i].key+"' value='"+data[i].key+"' name='"+'${param.checkname}'+"' style='width:14px;border:none' onclick='checkednum(this)'/>&nbsp\;&nbsp\;"+data[i].value;
						}
						if(i+1<data.length){
							var newTd1 = newTr.insertCell();
							newTd1.innerHTML = "<input type='checkbox' id='ck"+data[i+1].key+"' value='"+data[i+1].key+"' name='"+'${param.checkname}'+"' style='width:14px;border:none' onclick='checkednum(this)'/>&nbsp\;&nbsp\;"+data[i+1].value;
						}
						if(i+2<data.length){
							var newTd2 = newTr.insertCell();
							newTd2.innerHTML = "<input type='checkbox' id='ck"+data[i+2].key+"' value='"+data[i+2].key+"' name='"+'${param.checkname}'+"' style='width:14px;border:none' onclick='checkednum(this)'/>&nbsp\;&nbsp\;"+data[i+2].value;
						}
					}
				}
			},
			error : function(json) {
				alert("查询数据异常");
				window.history.back();
			}
		});
	}
	
	var TreeDiv =function(){};
	
	if (typeof(buttonTree) == "undefined") {
		var buttonTree = new Object();
	}
	
	buttonTree= new TreeDiv();

	/* 弹窗动作 */
	TreeDiv.prototype.openDivTree = function() {
		$("#backGroundDiv")
			.addClass("divBackGround")
			.css("left",0)
			.css("top",0)
			.width($(window).width())
			.height($(window).height())
			.show();
	
		$("#mainInputTreeDiv")
			.css("left",($(window).width()-400)/2)
			.css("top",($(window).height()-400)/2)
			.addClass("mainInputTreeDiv").show("quick");

	
		$("#inputTreeDiv")
			.addClass("innerInputTreeDiv")
			.css("left",($(window).width()-400)/2)
			.css("top",($(window).height()-400)/2)
			.show("quick");

		$("#inputTreeDiv").focus();
	};

	TreeDiv.prototype.divTreeBlur=function(){
		$("#inputTreeDiv").hide("quick",function (){
			$("#backGroundDiv").hide();
		});
		$("#mainInputTreeDiv").hide("quick");
	};
	
	TreeDiv.prototype.divTreeBlurClose=function(){
		$("#inputTreeDiv").hide("quick",function (){
			$("#backGroundDiv").hide();
		});
		$("#mainInputTreeDiv").hide("quick");
	};
</script>
 
<table cellpadding="0" cellspacing="0" style="width: 315px; margin-top: 0;">
	<tr>
		<td style="width: 110px;">
			<select id="orgid" name="orgid" onchange="change();"></select>
		</td>
		<td style="width: 110px; text-align: right;">车牌号：</td>
		<td style="width: 80px;">
			<img style="cursor: pointer;" src="<%=CDN%>images/button_select.gif" alt="选择" onclick="buttonTree.openDivTree();"/>
		</td>
	</tr>
</table>

<!-- 透明层 -->
<div id="backGroundDiv" class="divBackGround"></div>
<!-- inputTree层 -->
<div id="mainInputTreeDiv" class="mainInputTreeDiv">
	<div id="inputTreeDiv"  class="innerInputTreeDiv" >
		<%-- checkbox or radio use dTree 
		<img src="<%=CDN%>images/main_14.gif"/>  请选择：
		<br/><br/> --%>
		<div class="poptitle">
	    	<ul>
	  			<li>&nbsp;&nbsp;&nbsp;请选择</li>
	  			<li style="float:right; margin-right:5px; margin-top:3px;">
	  				<img src="<%=CDN%>images/button_sure.jpg" style="cursor: pointer;" alt="确定" onclick="buttonTree.divTreeBlur();"/>
	  			</li>
			</ul>
		</div>
		<div style="border-top:1px solid #C1CDDB; width: 400px;"></div>
		<div class="aaa" style="margin-top:10px;margin-left: 10px;height:300px;overflow: auto;">
			<table id="showVeh" style="width: 370px;border: 1px"></table>
		</div>
	</div>
	<div id="innerOkDiv" class="innerOkDiv">
		<%-- <img src="<%=CDN%>images/button_sure.jpg"  alt="确定" onclick="buttonTree['${param.treeName}'].divTreeBlur();"/> --%>
	</div>
</div>	