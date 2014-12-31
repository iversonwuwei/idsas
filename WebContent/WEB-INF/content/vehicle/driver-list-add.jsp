<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/JavaScript">
			$(function(){
				document.getElementById('description').onkeydown = function() {    
					if(this.value.length >= 300 && event.keyCode != 8) {
						event.returnValue = false; 
					}
				}
			});
		
			var xxxaaa = true;
			
			function carder(id){
				$("#drivernumber").val($.trim(id).split("x")[0]);
			};
			
			function doSave() {
				if ('' == $("#driver\\.drivername").val()) {
					$("#driver\\.drivername").focus();
					alert("Please enter the Driver Name!");
					return;
				}
				/* if ('' == $("#drivernumber").val()) {
					$("#drivernumber").focus();
					alert("Card Number should not be empty!");
					return;
				} */
				var departmentID = select_company.getSelectedValue();
				if(departmentID == null || departmentID == "-1" || departmentID == "") {
					alert("Place select a department!");
					return;
				}
				if("" != $("#driver\\.email").val() && !emailReg.test($("#driver\\.email").val())){
					alert('The email is not in the correct format!');
				 	$("#driver\\.email").select()
				 	return;
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
				if($("#driver\\.drivername").val() != '${driver.drivername}') {
			 		if (_check()) {
			 			alert("Driver Name already exists!");
				 		$("#driver\\.drivername").select();
				 		return;
					 }
			 	}
				// drivernumber repeat check
				if ($("#drivernumber").val() != '${driver.drivernumber}') {
					if (_checknumber()) {
						alert("Card Number already exists!");
						$("#drivernumber").select();
				 		return;
					}
			 	}
				beforeSubmit();
				document.queryResult.submit();
			}
		
			function _check() {
				var repeat = false;
				$.ajax({
					type : "POST",
					async : false,
					dataType : 'text',
					data : {
						'drivername' : $("#driver\\.drivername").val()
					},
					url : basePath + "vehicle/driver-list!check",
					success : function(d) {
						if (d != 0) {
							repeat = true;//repeat
						}
					}
				});
				return repeat;
			}
			
			function _checknumber() {// driverNumber Repeat Check
				var repeat = false;
				$.ajax({
					type : "POST",
					async : false,
					dataType : 'text',
					data : {
						'drivernumber' : $("#drivernumber").val()
					},
					url : basePath + "vehicle/driver-list!checkNumber",
					success : function(d) {
						if (d != 0) {
							repeat = true;//repeat
						}
					}
				});
				return repeat;
			}
		</script>
	</head>
	<body>
		<c:if test="${driver.driverid == null}">
			<custom:navigation father="Organization Management" model="Driver" operate="Add" saveMethod="doSave();" homePath="vehicle/driver-list" />
		</c:if>
		<c:if test="${driver.driverid != null}">
			<custom:navigation father="Organization Management" model="Driver" operate="Edit" saveMethod="doSave();" homePath="vehicle/driver-list" />
		</c:if>
		<form name="queryResult" action="<%=basePath%>vehicle/driver-list!save" method="post">
			<input name="driver.driverid" value="${driver.driverid}" type="hidden" /> 
			<input name="driver.isdelete" value="F" type="hidden" />
			<div class="pop_main">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="30%" class="text_bg"><font color="red">*&nbsp;</font>Name:</td>
						<td width="70%" class="valid">
							<s:textfield theme="simple" name="driver.drivername" id="driver.drivername" maxlength="30" cssClass="5,1,30" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Department:</td>
						<td>
							<custom:selCom name="driver.departmentid" value="${driver.departmentid}" width="180" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Card Number:</td>
						<td class="valid">
							<input type="text" id="drivernumber" name="driver.drivernumber" value="${driver.drivernumber}" maxlength="20" class="1,1,20" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Card type:</td>
						<td>
							<s:select list="#{'0':'Normal','1':'Temporary'}" name="driver.type" theme="simple" />
						</td>
					</tr>
					<tr>
						<td width="18%" class="text_bg">Gender:</td>
						<td>
							<s:radio list="#{'M':'Male','F':'Female'}" name="driver.gender" theme="simple" cssStyle="width:16px;" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Email:</td>
						<td>
							<s:textfield theme="simple" name="driver.email" id="driver.email" maxlength="30" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Phone:</td>
						<td>
							<s:textfield theme="simple" name="driver.phone" id="driver.phone" maxlength="16" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Description:<br />(300 characters)</td>
						<td height="60px;">
							<s:textarea id="description" name="driver.description" theme="simple" maxlength="300" cssStyle="width:100%; height:100%;" />
					 	</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>