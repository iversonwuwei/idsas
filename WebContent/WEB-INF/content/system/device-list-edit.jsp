<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/system/devicelist.js"></script>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/common/input-box.js"></script>
		<script src="<%=basePath%>scripts/common/dhtmlxCombo/dhtmlxcombo_5.js" type="text/javascript"></script>
	</head>
	<body>
		<custom:navigation father="Organization Management" model="Device" operate="Edit" saveMethod="doSubmit();" homePath="system/device-list" />
		<form action="<%=basePath%>system/device-list!create" name="queryResult" method="post">
			<input type="hidden" name="o_deviceno" id="o_deviceno" value="${o_deviceno}" /> 
			<input type="hidden" name="o_busid" id="o_busid" value="${o_busid}" /> 
			<input type="hidden" name="o_busname" id="o_busname" value="${o_busname}" /> 
			<input type="hidden" name="o_loginhost" id="o_loginhost" value="${o_loginhost}" />
			<input type="hidden" name="serverip" id="serverip" value="${serverip}" />
			<input type="hidden" name="serverport" id="serverport" value="${serverport}" />
			<input type="hidden" name="ipstatus" id="ipstatus" value="${ipstatus}" />
			<div class="pop_main">
				<table id="deviceTable" cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="30%"><font color="red">*&nbsp;</font>Device Name:</td>
						<td width="70%" class="valid">
							<%-- 如果设备已被绑定到车辆或单兵则不可修改设备名称 --%>
							<s:if test="%{o_busname == null || o_busname == ''}">
								<input type="text" id="vehDeviceName" name="o_devicename" maxlength="12" value="${o_devicename}" class="1,12,12">
								<input type="text" id="policeDeviceName" name="o_devicename" maxlength="12" value="${o_devicename}" class="1,5,5" disabled="disabled" style="display: none;">
							</s:if>
							<s:else>
								${o_devicename}
								<s:hidden name="o_devicename" value="%{o_devicename}"/>
							</s:else>
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Department:</td>
						<td>
							<%-- 如果设备已被绑定到车辆或单兵则不可修改设备所在部门 --%>
							<s:if test="%{o_busname == null || o_busname == ''}">
								<s:select id="deptid" name="deptid" list="depts" listKey="key" listValue="value" theme="simple" onchange="selectDepartment(this.value)" ></s:select>
							</s:if>
							<s:else>
								${deptname}
								<s:hidden name="deptid" value="%{deptid}"/>
							</s:else>
						</td>
					</tr>	
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Device Type:</td>
						<td>
							<%-- 如果设备已被绑定到车辆或单兵则不可修改设备类型 --%>
							<s:if test="%{o_busname == null || o_busname == ''}">
								<s:radio list="#{'1':'BNT5000', '2':'A5', '3':'Police'}" name="o_unittype" value="%{o_unittype}" theme="simple" onchange="showChannels();" />
							</s:if>
							<s:else>
								<c:if test="${o_unittype == '1'}">&nbsp;BNT5000</c:if>
								<c:if test="${o_unittype == '2'}">&nbsp;A5</c:if>
								<c:if test="${o_unittype == '3'}">&nbsp;Police</c:if>
								<s:hidden name="o_unittype" value="%{o_unittype}"/>
							</s:else>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Channel 1:</td>
						<td>
							<s:select id="videoid1" name="videoid1" list="channelList" listKey="key" listValue="value" headerKey="-1" headerValue="" theme="simple" cssStyle="width:150px;"/> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Channel 2:</td>
						<td>
							<s:select id="videoid2" name="videoid2" list="channelList" listKey="key" listValue="value" headerKey="-1" headerValue="" theme="simple" cssStyle="width:150px;"/> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Channel 3:</td>
						<td>
							<s:select id="videoid3" name="videoid3" list="channelList" listKey="key" listValue="value" headerKey="-1" headerValue="" theme="simple" cssStyle="width:150px;"/> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Channel 4:</td>
						<td>
							<s:select id="videoid4" name="videoid4" list="channelList" listKey="key" listValue="value" headerKey="-1" headerValue="" theme="simple" cssStyle="width:150px;"/> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Channel 5:</td>
						<td>
							<s:select id="videoid5" name="videoid5" list="channelList" listKey="key" listValue="value" headerKey="-1" headerValue="" theme="simple" cssStyle="width:150px;"/> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Channel 6:</td>
						<td>
							<s:select id="videoid6" name="videoid6" list="channelList" listKey="key" listValue="value" headerKey="-1" headerValue="" theme="simple" cssStyle="width:150px;"/> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Channel 7:</td>
						<td>
							<s:select id="videoid7" name="videoid7" list="channelList" listKey="key" listValue="value" headerKey="-1" headerValue="" theme="simple" cssStyle="width:150px;"/> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Channel 8:</td>
						<td>
							<s:select id="videoid8" name="videoid8" list="channelList" listKey="key" listValue="value" headerKey="-1" headerValue="" theme="simple" cssStyle="width:150px;"/> 
						</td>
					</tr>
				</table>
			</div>
			<script type="text/JavaScript">
				var video1 = dhtmlXComboFromSelect("videoid1");//姓名
				video1.enableFilteringMode(true);
				var video2 = dhtmlXComboFromSelect("videoid2");//姓名
				video2.enableFilteringMode(true);
				var video3 = dhtmlXComboFromSelect("videoid3");//姓名
				video3.enableFilteringMode(true);
				var video4 = dhtmlXComboFromSelect("videoid4");//姓名
				video4.enableFilteringMode(true);
				var video5 = dhtmlXComboFromSelect("videoid5");//姓名
				video5.enableFilteringMode(true);
				var video6 = dhtmlXComboFromSelect("videoid6");//姓名
				video6.enableFilteringMode(true);
				var video7 = dhtmlXComboFromSelect("videoid7");//姓名
				video7.enableFilteringMode(true);
				var video8 = dhtmlXComboFromSelect("videoid8");//姓名
				video8.enableFilteringMode(true);
				
				function selectDepartment(id) {
					video1.setComboValue("");
					video1.setComboText("");
					
					video2.setComboValue("");
					video2.setComboText("");
					
					video3.setComboValue("");
					video3.setComboText("");
					
					video4.setComboValue("");
					video4.setComboText("");
					
					video5.setComboValue("");
					video5.setComboText("");
					
					video6.setComboValue("");
					video6.setComboText("");
					
					video7.setComboValue("");
					video7.setComboText("");
					
					video8.setComboValue("");
					video8.setComboText("");
					
					video1.clearAll();
					video2.clearAll();
					video3.clearAll();
					video4.clearAll();
					video5.clearAll();
					video6.clearAll();
					video7.clearAll();
					video8.clearAll();
					
					$.ajax({
						type: "POST",
					    async: false,
					    dataType:'json',
					    data :{
					    	'id' : id,
					    	'o_deviceno':"${o_deviceno}"
					    },
					    url : basePath + "system/device-list!getcambydept",
					    success : function(data) {
					    	var s=new Array();
					    	for(var i = 0; i < data.length; i++) {
								s[i]={value:data[i].key,text:data[i].value};
			 				}
					    	video1.addOption(s);
					    	video2.addOption(s);
					    	video3.addOption(s);
					    	video4.addOption(s);
					    	video5.addOption(s);
					    	video6.addOption(s);
					    	video7.addOption(s);
					    	video8.addOption(s);
					    },
					    error : function(dd) {
							alert("Exception occurs!");
						}
					});
				}
				
				function doSubmit() {
					var v_deviceName;
					//根据不同设备类型选择设备名称输入框（验证条件不同）
					if('${o_unittype}' == '3') {
						v_deviceName = $.trim($("#policeDeviceName").val());
					} else {
						v_deviceName = $.trim($("#vehDeviceName").val());
					}
					if($.trim($("#o_busname").val()) == '' && v_deviceName == '') {
						alert("please entering the device name!");
						$("#o_devicename").focus();
						return;
					}
					if($.trim($("#o_busname").val()) == '' && $.trim($("#deptid").val()) == '') {
						alert("Please select Department");
						return;
					}
					if($("[name='o_unittype']:checked").val() == '1' || "${o_unittype == '1'}") {	//type vehicle
						var aaa=[];
						if(video1.getSelectedText()==""){
							video1.setComboValue(null);
						}else{
							aaa[video1.getSelectedValue()]=video1.getSelectedValue();
						}
						if(video2.getSelectedText()==""){
							video2.setComboValue(null);
						}else{
							if(aaa[video2.getSelectedValue()]!=null){
								alert("Channel 2 cameras binding already exists");
								return;
							}else{
								aaa[video2.getSelectedValue()]=video2.getSelectedValue();
							}
						}
						if(video3.getSelectedText()==""){
							video3.setComboValue(null);
						}else{
							if(aaa[video3.getSelectedValue()]!=null){
								alert("Channel 3 cameras binding already exists");
								return;
							}else{
								aaa[video3.getSelectedValue()]=video3.getSelectedValue();
							}
						}
						if(video4.getSelectedText()==""){
							video4.setComboValue(null);
						}else{
							if(aaa[video4.getSelectedValue()]!=null){
								alert("Channel 4 cameras binding already exists");
								return;
							}else{
								aaa[video4.getSelectedValue()]=video4.getSelectedValue();
							}
						}
						if(video5.getSelectedText()==""){
							video5.setComboValue(null);
						}else{
							if(aaa[video5.getSelectedValue()]!=null){
								alert("Channel 5 cameras binding already exists");
								return;
							}else{
								aaa[video5.getSelectedValue()]=video5.getSelectedValue();
							}
						}
						if(video6.getSelectedText()==""){
							video6.setComboValue(null);
						}else{
							if(aaa[video6.getSelectedValue()]!=null){
								alert("Channel 6 cameras binding already exists");
								return;
							}else{
								aaa[video6.getSelectedValue()]=video6.getSelectedValue();
							}
						}
						if(video7.getSelectedText()==""){
							video7.setComboValue(null);
						}else{
							if(aaa[video7.getSelectedValue()]!=null){
								alert("Channel 7 cameras binding already exists");
								return;
							}else{
								aaa[video7.getSelectedValue()]=video7.getSelectedValue();
							}
						}
						if(video8.getSelectedText()==""){
							video8.setComboValue(null);
						}else{
							if(aaa[video8.getSelectedValue()]!=null){
								alert("Channel 8 cameras binding already exists");
								return;
							}else{
								aaa[video8.getSelectedValue()]=video8.getSelectedValue();
							}
						}
					}
					var isPass = true;
					$(".novalid").each(function() {
						if($(this).length > 0) {
							alert("Illegal content entered, please verify!");
							isPass = false;
							return false;
						}
					});
					if(!isPass) {
						return;
					}
					$.ajax({
						type: "POST",
					    async: false,
					    dataType:'text',
					    data :{
					    	'ids' : $.trim($("#o_deviceno").val()),
					    	'names' : v_deviceName,
					    },
					    url : basePath + "system/device-list!checkEdit",
					    success : function(dd) {
							if("true" == dd) {
								alert("The Name has already exists!");
								$("#o_devicedescript").select();
								return;
							} 
							var str= $("#o_devicedetype").find("option:selected").text();
							$("#o_devicedescript").val(str);
							document.queryResult.submit();
					    },
					    error : function(dd) {
							alert("Exception occurs!");
							window.history.back();
						}
					});
				}
				
				$(function(){
					$("#deptid").val("${deptid}");
				});
				
				/**
				 * 编辑页切换设备类型
				 */
				function showChannels() {
					if($('#o_busname').val() == null || $('#o_busname').val() == '') {	//has not been bounded
						if($("[name='o_unittype']:checked").val() == '1' || $("[name='o_unittype']:checked").val() == '2') {								//type vehicle
							if($("[name='o_unittype']:checked").val() == '1') {	//BNT5000
								$("#deviceTable tr:gt(2)").show();
							} else {											//A5
								$("#deviceTable tr:gt(2)").hide();
							}
							$("#vehDeviceName").show();
							$("#vehDeviceName").attr('disabled', false);
							$("#vehDeviceName").attr('value', '${o_devicename}');
							$("#policeDeviceName").hide();
							$("#policeDeviceName").attr('disabled', true);
							$("#policeDeviceName").attr('value', '${o_devicename}');
							if($("#policeDeviceNameTip").length > 0) {										//判断提示信息是否存在
								$("#policeDeviceNameTip").remove();												//存在则删除
							}
							$("#policeDeviceName").css("border", "1px solid #A2B3BD");	//输入内容合法，恢复原样式
							$("#policeDeviceName").css("background", "#FFFFFF");
							$("#vehDeviceName").focus();
						} else {												//Police																			//type police
							$("#deviceTable tr:gt(2)").hide();
							$("#policeDeviceName").show();
							$("#policeDeviceName").attr('disabled', false);
							$("#policeDeviceName").attr('value', '${o_devicename}');
							$("#vehDeviceName").hide();
							$("#vehDeviceName").attr('disabled', true);
							$("#vehDeviceName").attr('value', '${o_devicename}');
							if($("#vehDeviceNameTip").length > 0) {											//判断提示信息是否存在
								$("#vehDeviceNameTip").remove();													//存在则删除
							}
							$("#vehDeviceName").css("border", "1px solid #A2B3BD");		//输入内容合法，恢复原样式
							$("#vehDeviceName").css("background", "#FFFFFF");
							$("#policeDeviceName").focus();
						}
					} else {	//has been bounded
						if($("[name='o_unittype']").val() == '1') {	//type BNT5000
							$("#deviceTable tr:gt(3)").show();
						} else {	//type A5/Police
							$("#deviceTable tr:gt(3)").hide();
						}
					}
				}
			</script>
		</form>
	</body>
</html>