<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.opensymphony.xwork2.ognl.OgnlValueStack"%>
<%
	((OgnlValueStack) request.getAttribute("struts.valueStack")).set(
	"vehID", request.getParameter("vehID"));
	((OgnlValueStack) request.getAttribute("struts.valueStack")).set(
		"vehicleNum", request.getParameter("vehicleNum"));
	String path = request.getContextPath() + "/";
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
	String CDN = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			 + "/erp/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%--
	title：动态获取车辆牌照号
	Useage (JSP):
	 <s:include value="/common/catchVehicle.jsp">
		 <s:param name="vehID" value="'***'" /> 获取VO中所对应的车辆ID的KEY，
		 <s:param name="vehicleNum" value="'***'" />获取VO中所对应的车辆ID的VAULE，车辆牌照号
	 </s:include>
	 注明：引用的页面需引入dhtmlxcombo_3.js.
--%>
<head>
	<title>动态获取车辆牌照号</title>
	<link href="<%=CDN%>css/fileuploader.css" rel="stylesheet" type="text/css">
</head>
<s:select style="width:120px;" list="{}" id='select_%{vehID}' theme="simple" onchange="transferHideData();" />
<s:hidden id='hid_vehicleNum' name='%{vehicleNum}' theme="simple" />
<s:hidden id='hid_vehicleID' name='%{vehID}' theme="simple" />
<script type="text/javascript">
	$(function() {
		//实时抓取维修材料和部位信息，初始化
		catchVehicles("");
		if("" != $("#hid_vehicleNum").val()) {
			catchVehicles($("#hid_vehicleNum").val());
			sel.setComboValue($("#hid_vehicleID").val());
		} else {
			$("#hid_vehicleID").val('');
			$("#hid_vehicleNum").val('');
		}
	
	});
	
	if (typeof(sel) == "undefined") {
		var sel = new Object();
	}
	sel = dhtmlXComboFromSelect_3("select_${param.vehID}");
	sel.enableFilteringMode(true);

	/**
	 * 实时抓取车辆牌照号
	 * @return
	 */
	function catchVehicles(text) {
		var v = new Array();
		$.ajax({
			url : basePath + "veh-select!catchVehicles",
			type : "POST",
			async : false,
			dataType : "json",
			data : {
				"text_veh" : text
			},
			success : function(data) {
				//线路框赋值
				for(var i = 0; i < data.length; i++) {
					v[i] = {value : data[i].key, text : data[i].value};
				}
				sel.optionsArr = new Array();
				sel.addOption(v);
			},
			error : function(json) {
				alert("查询数据异常");
				window.history.back();
			}
		});
		v = null;
	}
	
	//传值器
	function transferHideData(){
		$("#hid_vehicleID").val(sel.getSelectedValue());
		$("#hid_vehicleNum").val(sel.getComboText());
		if("" == sel.getComboText()) {
			$("#hid_vehicleID").val("");
			$("#hid_vehicleNum").val("");
			catchVehicles("");
		}
	}
</script>