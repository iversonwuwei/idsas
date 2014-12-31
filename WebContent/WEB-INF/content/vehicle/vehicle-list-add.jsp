<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/JavaScript">
			var xxxaaa=true;
			
			function carder(id){
				$("#keycode").val($.trim(id).split("x")[0]);
			}
		
			function save() {
				if ('' == $("#vehiclename").val()) {
					alert("Please enter the Plate Number!");
					return;
				}
				if ($("#vehiclename").val() != '${veh.vehiclename}') {
					if (check()) {
						alert("Plate Number already exists!");
						return;
					}
				}
				if("" == $("#deptid").val()){
					alert("Please select a department!");
					return;
				}
				if("" == $("#fleetid").val()){
					alert("Please select a fleet!");
					return;
				}
				if("" == $("#typeid").val()){
					alert("Please select a vehicle type!");
					return;
				}
				if("" == $("#brandid").val()){
					alert("Please select a vehicle brand!");
					return;
				}
				if("" != $.trim($("#keycode").val())){
				if(checkkey()){
					alert("Key Code already exists!");
					return;
				}
				}
				beforeSubmit();
				document.queryResult.submit();
			}
			
			function check() {
				var repeat = false;
				$.ajax({
					type : "POST",
					async : false,
					dataType : 'text',
					data : {
						'vehiclename' : $("#vehiclename").val()
					},
					url : basePath + "vehicle/vehicle-list!check",
					success : function(d) {
						if (d != 0) {
							repeat = true;//repeat
						} 
					}
				});
				return repeat;
			}
			
			function checkkey() {
				var repeat = false;
				$.ajax({
					type : "POST",
					async : false,
					dataType : 'text',
					data : {
						'keycode' : $("#keycode").val(),
						'id':'${id}'
					},
					url : basePath + "vehicle/vehicle-list!checkkey",
					success : function(d) {
						if (d != 0) {
							repeat = true;//repeat
						} 
					}
				});
				return repeat;
			}
			
			function checkLicenseplate() {
				var repeat = false;
				$.ajax({
					type : "POST",
					async : false,
					dataType : 'text',
					data : {
						'licenseplate' : $("#veh\\.licenseplate").val()
					},
					url : basePath + "vehicle/vehicle-list!checkLicenseplate",
					success : function(d) {
						if (d != 0) {
							repeat = true;//repeat
						} 
					}
				});
				return repeat;
			}
			
			function sdept(value){
				//alert(value);
				if(value==null || value==""){
					cleanoption("fleetid");
					cleanoption("typeid");
					cleanoption("brandid");
					cleanoption("deviceid");
					return;
				}
				$.ajax({
					type : "POST",
					async : false,
					dataType : 'json',
					data : {
						'id' : value,
						'veh.vehicleid':'${veh.vehicleid}'
					},
					url : basePath + "vehicle/vehicle-list!getftbd",
					success : function(d) {
						setoption("fleetid",d[0]);
						setoption("typeid",d[1]);
						setoption("brandid",d[2]);
						setoption("deviceid",d[3]);
					}
				});
			}
			
			function cleanoption(id){
				$("#"+id).html("");
				$("#"+id).append("<option value='-1'>Please choose</option>");
			}
			
			function setoption(id,ary){
				$("#"+id).html("");
				$("#"+id).append("<option value=''>Please choose</option>");
				for(var i=0;i<ary.length;i++){
					$("#"+id).append("<option value='"+ary[i].key+"'>"+ary[i].value+"</option>");
				}
			}
			
			$(function(){
				$("#deptid").val("${veh.deptid}");
				sdept("${veh.deptid}");
				$("#fleetid").val("${veh.fleetid}");
				$("#deviceid").val("${veh.deviceid}");
				$("#typeid").val("${veh.typeid}");
				$("#brandid").val("${veh.brandid}");
				document.getElementById('description').onkeydown = function() {    
					if(this.value.length >= 300 && event.keyCode != 8) {
						event.returnValue = false; 
					}
				}
			})
		</script>
	</head>
	<body>
		<c:if test="${veh.vehicleid == null }">
			<custom:navigation father="Organization Management" model="Vehicle" operate="Add" saveMethod="save();"  homePath="vehicle/vehicle-list" />
		</c:if>
		<c:if test="${veh.vehicleid != null }">
			<custom:navigation father="Organization Management" model="Vehicle" operate="Edit" saveMethod="save();"  homePath="vehicle/vehicle-list" />
		</c:if>
		<form name="queryResult" action="<%=basePath%>vehicle/vehicle-list!save" method="post">
			<input type="hidden" name="veh.isdelete" value="F" /> 
			<input type="hidden" name="veh.vehicleid" value="${veh.vehicleid}" /></td>
			<div class="pop_main">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="30%" class="text_bg"><font color="red">*&nbsp;</font>Plate Number:</td>
						<td width="70%" class="valid">
							<s:textfield name="veh.vehiclename" id="vehiclename" maxlength="20" cssClass="3,1,20" theme="simple" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Department:</td>
						<td>
							<s:select id="deptid" name="veh.deptid" list="depts" listKey="key" listValue="value" theme="simple" headerKey="" headerValue="Please choose" onchange="sdept(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Fleet:</td>
						<td>
							<s:select id="fleetid" name="veh.fleetid" list="fleets" listKey="key" listValue="value" theme="simple" headerKey="" headerValue="Please choose" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Device:</td>
						<td>
							<s:select list="devicelist" name="veh.deviceid" id="deviceid" listKey="key" listValue="value" headerKey="" headerValue="Please choose" theme="simple" cssStyle="width:182px;" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Vehicle Type:</td>
						<td>
							<s:select list="typelist" name="veh.typeid" id="typeid" listKey="key" listValue="value" headerKey="" headerValue="Please choose" theme="simple" cssStyle="width:182px;" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Vehicle Brand:</td>
						<td>
							<s:select list="brands" name="veh.brandid" id="brandid" listKey="key" listValue="value" headerKey="" headerValue="Please choose" theme="simple" cssStyle="width:182px;" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Key Code:</td>
						<td class="valid">
							<s:textfield theme="simple" name="veh.keycode" id="keycode" maxlength="20" cssClass="3,1,20" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Description:<br />(300 characters)</td>
						<td height="60px;">
							<s:textarea id="description" name="veh.description" theme="simple" maxlength="300" cssStyle="width:100%; height:100%;" />
					 	</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>