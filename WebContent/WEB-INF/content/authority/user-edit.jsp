<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<link type="text/css" href="<%=basePath%>scripts/common/dtree/dtree.css" rel="stylesheet" />
		<script type="text/JavaScript" src="<%=basePath%>scripts/common/dtree/input-dtree.js"></script>
		<script type="text/javascript">
			//选择所属部门时，自动选择可见机构
			$(function() {
				srole();
				$("#userDepart").val("${userDepart}");
				document.getElementById('description').onkeydown = function() {    
					if(this.value.length >= 300 && event.keyCode != 8) {
						event.returnValue = false; 
					}
				}
			});
			
			$("#mainInputTreeDivdepartmentTree img[alt='Confirm']").bind("click",function() {
				$("#mainInputTreeDivdataTree img[alt='Confirm']").click();
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
			
			function srole(){
				var v=$("#selectRoleName").val();
				$.ajax({
					type: "POST",
				    async: false,
				    dataType:'text',
				    data :{'id' : v},
				    url : basePath + "authority/user!srole",
				    success : function(dd) {
				    	if(dd=='0' ){
							$("#userDepart").attr("disabled","true");
						}else{
							$("#userDepart").removeAttr("disabled");
						}
				    }
				});
			}
			
			function doSave() {
				//$("#orgstr").val(getDate('orgTree'));
				if("" == $.trim($("#loginName").val())) {
					alert("Please enter the account!");
					$("#loginName").focus();
					return;
				}
				if( $("#userDepart").attr("disabled")==null &&($.trim($("#userDepart").val())=="")){
					alert("Please select a company!");
					return;
				}
				/* if("" == $.trim($("#userName").val())) {
					alert("Please enter the Name!");
					$("#userName").focus();
					return;
				} */
				if("" == getDate('dataTree') || null == getDate('dataTree')) {
					alert("Please select the visible!");
					return;
				}
				var t = $("#e_mail").val();
				//对电子邮件的验证
				if ("" != t) {
					var myreg = /^([a-za-z0-9]+[_|_|.]?)*[a-za-z0-9]+@([a-za-z0-9]+[_|_|.]?)*[a-za-z0-9]+.[a-za-z]{2,3}$/;
					if(!myreg.test(t)) {
				       alert('The email is not in the correct format!');
				       $("#e_mail").select();
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
				$.ajax({
					type: "POST",
				    async: false,
				    dataType:'text',
				    data :{
				    	'id' : $("#selectRoleName").val(),
				    	'userID':$("#userDepart").val()
				    },
				    url : basePath + "authority/user!checkrc",
				    success : function(dd) {
						if(dd=='false'){
							alert('Role and the company does not match!');
							return;
						}else{
							$.ajax({
								type: "POST",
							    async: false,
							    dataType:'text',
							    data :{
							    	'userID' : $.trim($("#userID").val()),
							    	'names' : $.trim($("#loginName").val()),
							    	'code' : $.trim($("#userCode").val()),
							    	'userName' : $.trim($("#userName").val())
							    },
							    url : basePath + "authority/user!check",
							    success : function(dd) {
									if("loginNameRep" == dd) {
										alert("The Account already exists!");
										$("#loginName").select();
										return;
									} 
									/* if("userCodeRep" == dd) {
										alert("The User Code already exists!");
										$("#userCode").select();
										return;
									} */
									if("userNameRep" == dd) {
										alert("The Name already exists!");
										$("#userName").select();
										return;
									}
									if("success" == dd){
									//	$('#userDepart').val(getDate('departmentTree'));
										$('#visibleStr').val(getDate('dataTree'));
							    		document.queryResult.submit();
							    	}
							    },
							    error : function(dd) {
									alert("Exception occurs!");
									window.history.back();
								}
							});
						}
				    },
				    error : function(dd) {
						alert("Exception occurs!");
						
					}
				});
			}
		</script>
	</head>
	<body>
		<s:if test="userID == null">
			<custom:navigation father="User Management" model="Account" operate="Add" saveMethod="doSave();" homePath="authority/user" />
		</s:if>
		<s:else>
			<custom:navigation father="User Management" model="Account" operate="Edit" saveMethod="doSave();" homePath="authority/user" />
		</s:else>
		<form action="<%=basePath%>authority/user!create" name="queryResult" method="post">
			<input type="hidden" name="userID" id="userID" value="${userID}" /> 
			<input type="hidden" name="password" id="password" value="${password}" /> 
			<%--
			<input type="hidden" name="userDepart" id="userDepart" value="${userDepart}" />
			 --%>
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="30%"><font color="red">*&nbsp;</font>Account:</td>
						<td width="70%" class="valid">
							<input type="text" id="loginName" name="loginName" value="${loginName}" maxlength="20" class="5,1,20" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Role:</td>
						<td>
							<s:select list="roleList" listKey="key" listValue="value" name="userRoleID" id="selectRoleName" theme="simple" onchange="srole()" />
						</td>
					</tr>
					<%-- <tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>User Code:</td>
						<td class="valid">
							<input type="text" id="userCode" name="userCode" value="${userCode}" maxlength="20" class="3,1,20" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr> --%>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Company:</td>
						<td style="text-align: left;">
							<s:select id="userDepart" name="userDepart" list="coms" listKey="key" listValue="value" theme="simple" />	
						<%--
							<s:include value="/common/button-tree.jsp">
								<s:param name="treeName" value="'departmentTree'" />
								<s:param name="treeType" value="'uniqueRadio'" />
								<s:param name="propertys" value="'orgID, orgName, parentID, n, n, url'" />
								<s:param name="dataList" value="%{departmentList}" />
								<s:param name="dataStr" value="%{userDepart}" />
							</s:include>
						 --%>
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Visible:</td>
						<td style="text-align: left;">
							<s:include value="/common/button-tree.jsp">
								<s:param name="treeName" value="'dataTree'" />
								<s:param name="treeType" value="'checkbox'" />
								<s:param name="propertys" value="'orgID, orgName, parentID, n, n, url'" />
								<s:param name="dataList" value="%{departmentList}" />
								<s:param name="dataStr" value="%{visibleStr}" />
							</s:include>
							<input type="hidden" name="visibleStr" id="visibleStr" value="${visibleStr}"/>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Name:</td>
						<td class="valid">
							<input type="text" id="userName" name="userName" value="${userName}" maxlength="20" class="5,1,20" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Email:</td>
						<td>
							<input type="text" id="e_mail" name="e_mail" value="${e_mail}" maxlength="50" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Phone:</td>
						<td class="valid">
							<input type="text" id="mobilephone" name="mobilephone" value="${mobilephone}" maxlength="30" class="0,1,30" onblur="this.value=$.trim(this.value);"/>
						</td>
					</tr>
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