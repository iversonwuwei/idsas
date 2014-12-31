<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript">
			$(function(){
				document.getElementById('description').onkeydown = function() {    
					if(this.value.length >= 300 && event.keyCode != 8) {
						event.returnValue = false; 
					}
				}
			});
		
			function doSave() {
				if($('#orgName').val() == "") {
					alert("Please enter the Company Name!");
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
				checkDuplicate();
			}

			/**
			 * check if the fleet is duplicate
			 */
			function checkDuplicate() {
				$.ajax({
				    type: "POST",
				    async: false,
				    dataType: 'JSON',
				    data :{
				    	'orgID' : $.trim($("#orgID").val()),
				    	'parentID' : '1',
				    	'orgName' : $.trim($("#orgName").val())
				    },
				    url : basePath + "vehicle/com!checkDuplicate",
				    success : function(d) {
						if("pass" != d.result){
							alert("The company already exists!");
							return;
						} else {
							document.queryResult.submit();
						}
				    },
				    error : function(d) {
						alert("ERROR!");
						window.history.back();
				    }
				});
			}
		</script>
	</head>
	<body>
		<s:if test="%{orgID == null}">
			<custom:navigation father="Organization Management" model="Company" operate="Add" saveMethod="doSave();" homePath="vehicle/com" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Management" model="Company" operate="Edit" saveMethod="doSave();" homePath="vehicle/com" />
		</s:else>
		<form action="<%=basePath%>vehicle/com!create" name="queryResult" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<input type="hidden" name="orgID" id="orgID" value="${orgID}" />
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="30%"><font color="red">*&nbsp;</font>Company Name:</td>
						<td width="70%" class="valid">
							<input type="text" id="orgName" name="orgName" value="${orgName}" maxlength="20" class="5,1,20" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Schedule Option:</td>
						<td>
							<s:select list="#{'0':'Non-RFID','1':'RFID'}" name="lineno" id="lineno" theme="simple" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Description:<br />(300 characters)</td>
						<td height="60px;">
							<s:textarea id="description" name="description" theme="simple" maxlength="300" cssStyle="width:100%; height:100%;" />
					 	</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>