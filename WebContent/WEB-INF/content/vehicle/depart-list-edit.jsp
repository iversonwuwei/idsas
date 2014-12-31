<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript">
			$(function(){
				$("#parentID").val("${parentID}");
				document.getElementById('description').onkeydown = function() {    
					if(this.value.length >= 300 && event.keyCode != 8) {
						event.returnValue = false; 
					}
				}
			});
		
			function doSave() {
				if($.trim($("#parentID").val()) == ""){
					alert("Please select a company!");
					return;
				}
				if($('#orgName').val() == "") {
					alert("Please enter the Department Name!");
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
				    	'parentID' : $.trim($("#parentID").val()),
				    	'orgName' : $.trim($("#orgName").val())
				    },
				    url : basePath + "vehicle/depart-list!checkDuplicate",
				    success : function(d) {
						if("pass" != d.result){
							alert("The department already exists!");
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
			<custom:navigation father="Organization Management" model="Department" operate="Add" saveMethod="doSave();" homePath="vehicle/depart-list" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Management" model="Department" operate="Edit" saveMethod="doSave();" homePath="vehicle/depart-list" />
		</s:else>
		<form action="<%=basePath%>vehicle/depart-list!create" name="queryResult" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<input type="hidden" name="orgID" id="orgID" value="${orgID}" />
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="30%"><font color="red">*&nbsp;</font>Department Name:</td>
						<td width="70%" class="valid">
							<input type="text" id="orgName" name="orgName" value="${orgName}" style="width: 200px;" maxlength="20" class="5,1,20" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Company:</td>
						<td>
							<s:select id="parentID" name="parentID" list="coms" listKey="key" listValue="value" theme="simple" cssStyle="width:200px;" />
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