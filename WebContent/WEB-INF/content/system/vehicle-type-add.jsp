<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/JavaScript">
			$(function(){
				$("#comid").val("${vehtype.comid}");
				document.getElementById('description').onkeydown = function() {    
					if(this.value.length >= 300 && event.keyCode != 8) {
						event.returnValue = false; 
					}
				}
			});
		
			function doSave() {
				if($.trim($("#comid").val())==""){
					alert("Please select a company!");
					return;
				}
				if ('' == $("#vehtype\\.type").val()) {
					$("#vehtype\\.type").focus();
					alert("Please enter the Vehicle Type!");
					return;
				}
				if(($("#vehtype\\.type").val() != '${vehtype.type}')|| ($("#comid").val() != '${vehtype.comid}')){
					if(check()) {
						alert("The Vehicle Type already exists!");
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
						'type' : $("#vehtype\\.type").val(),
						'id':$("#comid").val()
					},
					url : basePath + "system/vehicle-type!check",
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
		<c:if test="${vehtype.typeid == null}">
			<custom:navigation father="System Settings" model="Vehicle Type" operate="Add" saveMethod="doSave();" homePath="system/vehicle-type" />
		</c:if>
		<c:if test="${vehtype.typeid != null}">
			<custom:navigation father="System Settings" model="Vehicle Type" operate="Edit" saveMethod="doSave();" homePath="system/vehicle-type" />
		</c:if>
		<form name="queryResult" action="<%=basePath%>system/vehicle-type!save" method="post">
			<input type="hidden" name="vehtype.typeid" value="${vehtype.typeid }" /> 
			<input type="hidden" name="vehtype.isdelete" value="F" />
			<div class="pop_main">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="30%" class="text_bg"><font color="red">*&nbsp;</font>Company:</td>
						<td width="70%" style="text-align:left;">
							<s:select id="comid" name="vehtype.comid" list="coms" listKey="key" listValue="value" theme="simple"></s:select>
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Vehicle Type:</td>
						<td class="valid">
							<input type="text" id="vehtype.type" name="vehtype.type" value="${vehtype.type}" maxlength="30" class="5,1,30" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Description:<br />(300 characters)</td>
						<td height="60px;">
							<s:textarea id="description" name="vehtype.description" theme="simple" maxlength="300" cssStyle="width:100%; height:100%;" />
					 	</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>