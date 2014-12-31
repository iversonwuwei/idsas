<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
<body>
	<div class='welcome'>
		<div class="user">
			<img src='<%=basePath%>images/temp_34.gif' width='6' height='29' align='absmiddle' /><img src='<%=basePath%>images/dtree/ico_user.gif' width='16' height='16' align='absmiddle' />&nbsp;&nbsp;Welcome&nbsp;&nbsp;${mainUser.loginName}&nbsp;&nbsp;[${mainUser.userRole.roleName}]&nbsp;&nbsp;Current Position&nbsp;:&nbsp;Profile&nbsp;&gt;&gt;&nbsp;<span style="font-weight: bold">Change Password</span>
		</div>
		<div class="czan" style="width: 300px;">
			<ul>
				<li><a href='#' onclick='save();'> <img src='<%=basePath%>images/dtree/ico_save.gif' border='0' align='absmiddle' title='Save' /> Save</a>
				</li>
				<li><a href='#' onclick='if(confirm("Be sure you want to reset the password?"))resetpw();'> <img src='<%=basePath%>images/dtree/ico_reset.gif' border='0' align='absmiddle' title='Reset' /> Reset</a>
				</li>
			</ul>
		</div>
	</div>
	<%-- <custom:navigation father="Authority" model="Change Password" saveMethod="save();" /> --%>
	<form action="<%=basePath%>authority/user!saveChange" method="post"
		name="userForm">
		<div class="pop_main">
			<table border="0" cellspacing="0" cellpadding="0" style="width: 100%">
				<input type="hidden" id="userID" value="${session.mainUser.userID}" />
				<tr>
					<td class="text_bg" width="13%">Accout:</td>
					<td width="37%" style="text-align: left;">${mainUser.loginName} <input
						name="user.userID" value="${mainUser.userID}" type="hidden" /></td>
				</tr>
				<tr>
					<td class="text_bg" width="13%">Old Password:</td>
					<td width="37%" style="text-align: left;"><input id="password"
						type="password" /></td>
				</tr>
				<tr>
					<td class="text_bg" width="13%">New Password:</td>
					<td width="37%" style="text-align: left;"><input
						id="newPassword" type="password" /></td>
				</tr>
				<tr>
					<td class="text_bg" width="13%">Confirm Password:</td>
					<td width="37%" style="text-align: left;"><input
						name="user.password" id="newPassword2" type="password" /></td>
				</tr>
			</table>
		</div>
	</form>
	<script type="text/JavaScript">
			function save(){
				if("" == $.trim($("#password").val())){
					alert("Please enter the old password!");
					$("#password").focus();
					return;
				}
				if("" == $.trim($("#newPassword").val())){
					alert("Please enter the new password!");
					$("#newPassword").focus();
					return;
				}
				if("" == $.trim($("#newPassword2").val())){
					alert("Please input the confirm new password!");
					$("#newPassword2").focus();
					return;
				}
				if($.trim($("#newPassword").val()) != $.trim($("#newPassword2").val())){
					alert("The two input password does not match!");
					$("#newPassword2").select();
					return;
				}
				$.ajax({
				    type: "POST",
				    async: false,
				    dataType:'text',
				    data :{'password' : $.trim($("#password").val()), 'userID' : $.trim($("#userID").val()), 'newPassword' : $.trim($("#newPassword").val())},
				    url : basePath + "authority/user!saveChange",
				    success : function(dd) {
				    	if(dd == "true"){
				    		alert("Password updated successfully!");
				    		window.parent.location = "<%=basePath%>login";
							return;
				    	}
				    	if(dd == "psdErr"){
				    		alert("The old password input error!");
				    		$("#password").select();
							return;
				    	}
				    	if(dd == "false"){
				    		alert("Save failed!");
							return;
				    	}
				    	
				    },
				    error : function(dd) {
						alert("Data exception!");
						window.history.back();
					}
				});
			}
			function resetpw(){
				$.ajax({
				    type: "POST",
				    async: false,
				    dataType:'text',
				    data :{'userID' : $.trim($("#userID").val())},
				    url : basePath + "authority/user!resetPassword",
				    success : function(dd) {
				    	if(dd == "true"){
				    		alert("Password reset successfully!");
				    		window.parent.location = "<%=basePath%>login";
						return;
					}
					if (dd == "false") {
						alert("Reset failed!");
						return;
					}
				},
				error : function(dd) {
					alert("Data exception!");
					window.history.back();
				}
			});
		}
	</script>
</body>
</html>