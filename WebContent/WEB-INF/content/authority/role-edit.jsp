<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<%response.setHeader("Pragma","No-cache"); response.setHeader("Cache-Control","No-cache"); response.setDateHeader("Expires",-10);%> 
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>scripts/common/dtree/dtree.css" />
		<script type="text/JavaScript" src="<%=basePath%>scripts/common/dtree/input-dtree.js"></script>
		<script type="text/JavaScript" src="<%=basePath%>scripts/specific/authority/role.js"></script>
		<script type="text/javascript">
			$(function (){
				seflag($("#aflag").val());
				document.getElementById('description').onkeydown = function() {    
					if(this.value.length >= 300 && event.keyCode != 8) {
						event.returnValue = false; 
					}
				}
			});
			
			function checktree(id) {
				var selids = tree['featTree'].getCheckedNodes(true);
				for (var n = 0; n < selids.length; n++) {
					if(selids[n]==id){
						return true;
					}
				}
				return false;
			}
		
			function seflag(v){
				if(v=='0' || v==''){
					$("#comid").attr("disabled","true");
				}else{
					$("#comid").removeAttr("disabled");
				}
			}
			
			function doSave() {
				if("" == $.trim($("#roleName").val())){
					alert("Please enter a Role Name!");
					return;
				}
				if("" == $.trim($("#comid").val()) && $("#aflag").val() != 0){
					alert("Please select a company!");
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
				$("#featStr").val(getDate('featTree'));	//Assignment to feats
				if(null == $("#featStr").val() || "" == $("#featStr").val()){
					alert("Please select the features!");
					return;
				}
				if($("#aflag").val() == 1) {
					if(!checktree(1300)) {
						alert("If the role type is administrator, please select the User Management.");
						return;
					}
					if(!checktree(1302)) {
						alert("If the role type is administrator, please select the User Management >> Role function.");
						return;
					}
					if(!checktree(1301)) {
						alert("If the role type is administrator, please select the User Management >> Account function.");
						return;
					}
					if(checktree(1409)){
						alert("If the role type is administrator, please do not select the Organization Management >> Company function.");
						return;
					}
					if(checktree(1605)){
						alert("If the role type is administrator, please do not select the Organization Query >> Company function.");
						return;
					}
				}
				if($("#aflag").val() == 2) {
					if(checktree(1300)) {
						alert("If the role type is other, please do not select the User Management.");
						return;
					}
					if(checktree(1302)) {
						alert("If the role type is other, please do not select the User Management >> Role function.");
						return;
					}
					if(checktree(1301)) {
						alert("If the role type is other, please do not select the User Management >> Account function.");
						return;
					}
				}
				$.ajax({
					async: false,
				    type: "POST",
				    dataType:'json',
				    data : {
				    	'userVo.roleID' : $("#roleID").val(),
				    	'userVo.roleName' : $.trim($("#roleName").val()),
				    	'id':$("#comid").val(),
				    	'aflag':$("#aflag").val()
				    },
				    url : basePath + "authority/role!checkDuplicate",
				    success : function(data) {
						if("OK" != data.result){
							if($("#aflag").val()=='0'){
								alert("The super administrator type can only have one role, the super administrator role.");
								return;
							}
							
							alert("The Role Name already exists!");
							$("#roleName").select();
							return;
						}
						document.roleForm.submit();
				    },
				    error : function(data) {
						alert("Exception occurs!");
						window.history.back();
					}
				});
			}
		</script>
	</head>
	<body>
		<s:if test="roleID == null">
			<custom:navigation father="User Management" model="Role " operate="Add" saveMethod="doSave();" homePath="authority/role" />
		</s:if>
		<s:else>
			<custom:navigation father="User Management" model="Role" operate="Edit" saveMethod="doSave();" homePath="authority/role" />
		</s:else>
		<form name="roleForm" method="post" action="<%=basePath%>authority/role!create">
			<input type="hidden" id="featStr" name="feat_Add_Str"/>
			<input type="hidden" id="roleID" name="roleID" value="${roleID}"/>
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="30%"><font color="red">*&nbsp;</font>Role Name:</td>
						<td width="70%" class="valid">
							<input id="roleName" type="text" name="roleName" value="${roleName}" maxlength="30" class="5,1,30"  onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Role type:</td>
						<td>
							<c:if test="${mainUser.userRole.aflag == 0}">
								<s:select id="aflag" name="aflag" list="#{'0':'Super Administrator','1':'Administrator', '2':'Other'}"  theme="simple" onchange="seflag(this.value)" />							
							</c:if>
							<c:if test="${mainUser.userRole.aflag == 1}">
								<s:select id="aflag" name="aflag" list="#{ '2':'Other'}"  theme="simple" onchange="seflag(this.value)" />							
							</c:if>
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Company:</td>
						<td>
							<s:select id="comid" name="comid" list="coms" listKey="key" listValue="value" theme="simple"  ></s:select>						
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Features:</td>
						<td>
							<s:include value="/common/button-tree.jsp">
								<s:param name="treeName" value="'featTree'" />
								<s:param name="treeType" value="'checkbox'" />
								<s:param name="propertys" value="'featID, featName, fatherID, n, n, url'" />
								<s:param name="dataList" value="%{featList}" />
								<s:param name="dataStr" value="%{feat_Add_Str}" />
								<s:param name="isAdd" value="%{id}" />
							</s:include>
						</td>
					</tr>
					<s:hidden name="inSystem" value="0"/>
					<%--
					
					<tr>
						<td class="text_bg">Camera Permissions:</td>
						<td>
							<s:select id="camPerm" name="inSystem" list="#{'1':'operator', '2':'viewer'}" headerKey="0" headerValue="administrator" theme="simple" />
						</td>
					</tr>
					 --%>
					<tr>
						<td class="text_bg">Description:<br />(300 characters)</td>
						<td height="60px;">
							<s:textarea id="description" name="description" theme="simple" cssStyle="width:100%; height:100%;" />
					 	</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>