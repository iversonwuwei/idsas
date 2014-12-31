<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<link type="text/css" href="<%=basePath%>scripts/common/dtree/dtree.css" rel="stylesheet" />
		<script type="text/JavaScript" src="<%=basePath%>scripts/common/dtree/input-dtree.js"></script>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/authority/user.js"></script>
	</head>
	<body>
		<custom:navigation father="User Management" model="User list" operate="Add" saveMethod="doSave();" homePath="authority/user" />
		<form action="<%=basePath%>authority/user!save" name="queryResult" method="post">
			<input type="hidden" name="user.userID" id="userID" value="${user.userID}" /> 
			<input type="hidden" name="user.password" id="password" value="${user.password}" /> 
			<input type="hidden" name="orgStr" id="orgstr" value="${orgStr}" />
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="30%"><font color="red">*&nbsp;</font>Account:</td>
						<td width="70%" class="valid">
							<input type="text" id="loginName" name="user.loginName" maxlength="10" class="5,1,10" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>User Code:</td>
						<td class="valid">
							<input type="text" id="userCode" name="user.userCode" maxlength="10" class="3,1,10" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Name:</td>
						<td class="valid">
							<input type="text" id="userName" name="user.userName" maxlength="20" class="5,1,20" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Role:</td>
						<td>
							<s:select list="roleList" listKey="key" listValue="value" name="user.userRole.roleID" id="selectRoleName" theme="simple" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Phone:</td>
						<td class="valid">
							<input type="text" id="mobilephone" name="user.mobilephone" maxlength="30" class="0,1,30" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Email:</td>
						<td>
							<input type="text" id="e_mail" name="user.e_mail" maxlength="50" />
						</td>
					</tr>
					<!-- <tr>
						<td class="text_bg">Mail Password:</td>
						<td style="text-align: left;"  class="valid"><input type="text" id="mailpassword" name="user.mailpassword" maxlength="20" class="5,1,20" /></td>
						<td class="text_bg">Education:</td>
						<td style="text-align: left;"><input type="text" id="education" name="user.education" maxlength="50" class="5,1,50" /></td>
					</tr> -->
					<!-- <tr>
						<td class="text_bg">School:</td>
						<td style="text-align: left;"  class="valid"><input type="text" id="school" name="user.school" maxlength="50" class="5,1,50" /></td>
						<td class="text_bg">Gender:</td>
						<td style="text-align: left;">
							<input type="radio" name="user.gender" value="M" style="width: 20px;"/> Male &nbsp;
							<input type="radio" name="user.gender" value="F" style="width: 20px;"/> Female
						</td>
					</tr> -->
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Department:</td>
						<td>
							<s:include value="/common/button-tree.jsp">
								<s:param name="treeName" value="'orgTree'" />
								<s:param name="treeType" value="'uniqueRadio'" />
								<s:param name="propertys" value="'orgID, orgName, parentID, n, n, url'" />
								<s:param name="dataList" value="%{orgList}" />
								<s:param name="dataStr" value="%{orgStr}" />
							</s:include>
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Visible:</td>
						<td>
							<s:include value="/common/button-tree.jsp">
								<s:param name="treeName" value="'dataTree'" />
								<s:param name="treeType" value="'checkbox'" />
								<s:param name="propertys" value="'orgID, orgName, parentID, n, n, url'" />
								<s:param name="dataList" value="%{orgList}" />
								<s:param name="dataStr" value="%{data_Str}" />
							</s:include>
							<input type="hidden" name="data_Str" id="data_Str" value="${data_Str}"/>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Description:<br />(300 characters)</td>
						<td height="60px;">
							<s:textarea id="description" name="user.description" theme="simple" cssStyle="width:100%; height:100%;" />
					 	</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
	<script type="text/JavaScript" >
		$(function() {
			$.each($(":radio"),function(i, n){
				$(n).bind("click",function(){
				var departmentID = $(n).attr("value");
				uncheckAll(tree['dataTree']);
				$("input[type='checkbox'][value='" + departmentID + "']").click();
				});
			});
			document.getElementById('description').onkeydown = function() {    
				if(this.value.length >= 300 && event.keyCode != 8) {
					event.returnValue = false; 
				}
			}
		});
		
		$("#mainInputTreeDivorgTree img[alt='确定']").bind("click",function(){
			$("#mainInputTreeDivdataTree2 img[alt='确定']").click();
		}); 
		
		function uncheckAll(tree) {
			var box;
			for ( var i = 0; i < tree.aNodes.length; i++) {
				box = tree.co(tree.aNodes[i].id);
				if (null != box) {
					box.checked = false;
				}
			}
		}
	</script>
</html>