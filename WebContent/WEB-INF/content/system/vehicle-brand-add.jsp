<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/JavaScript">
			$(function(){
				$("#comid").val("${vehbrand.comid}");
				document.getElementById('description').onkeydown = function() {    
					if(this.value.length >= 300 && event.keyCode != 8) {
						event.returnValue = false; 
					}
				}
			});
		
			function doSave() {
				if($.trim($("#comid").val()) == ""){
					alert("Please select a company!");
					return;
				}
				if ('' == $("#vehbrand\\.type").val()) {
					$("#vehbrand\\.type").focus();
					alert("Please enter the Vehicle Brand!");
					return;
				}
				if(($("#vehbrand\\.type").val() != '${vehbrand.name}')|| ($("#comid").val() != '${vehbrand.comid}')) {
					if(check()){
						alert("The Vehicle Brand already exists!");
						return;
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
				document.queryResult.submit();
			}
			
			function check() {
				var repeat;
				$.ajax({
					type : "POST",
					async : false,
					dataType : 'text',
					data : {
						'type' : $("#vehbrand\\.type").val(),
						'id':$("#comid").val()
					},
					url : basePath + "system/vehicle-brand!check",
					success : function(d) {
						if (d != 0) {
							repeat = true;//repeat
						} else {
							repeat = false;
						}
					},
					error : function() {
						repeat = false;
					}
				});
				return repeat;
			}
		</script>
	</head>
	<body>
		<c:if test="${vehbrand.id == null}">
			<custom:navigation father="System Settings" model="Vehicle Brand" operate="Add" saveMethod="doSave();" homePath="system/vehicle-brand" />
		</c:if>
		<c:if test="${vehbrand.id != null}">
			<custom:navigation father="System Settings" model="Vehicle Brand" operate="Edit" saveMethod="doSave();" homePath="system/vehicle-brand" />
		</c:if>
		<form name="queryResult" action="<%=basePath%>system/vehicle-brand!save" method="post">
			<div class="pop_main">
				<input type="hidden" name="vehbrand.id" value="${vehbrand.id}" /> 
				<input type="hidden" name="vehbrand.isdelete" value="F" />
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="30%" class="text_bg"><font color="red">*&nbsp;</font>Company:</td>
						<td width="70%">
							<s:select id="comid" name="vehbrand.comid" list="coms" listKey="key" listValue="value" theme="simple"></s:select>
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Vehicle Brand:</td>
						<td class="valid">
							<input type="text" name="vehbrand.name" id="vehbrand.type" value="${vehbrand.name}" maxlength="30" class="5,1,30" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Description:<br />(300 characters)</td>
						<td height="60px;">
							<s:textarea id="description" name="vehbrand.memo" theme="simple" cssStyle="width:100%; height:100%;" />
					 	</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>