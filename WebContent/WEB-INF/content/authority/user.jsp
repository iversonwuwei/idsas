<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/authority/user.js"></script>
		<script type="text/javascript">
			function excel(){
	     		if(0 == "${fn:length(page.result)}"){
		         	alert("No derived data!");
		         	return;
		     	}
	     		document.forms['queryResult'].action='<%=basePath%>authority/user!exportDetail';
	         	document.forms['queryResult'].submit();
	         	document.forms['queryResult'].action='<%=basePath%>authority/user!index';
	     	}
		</script>
	</head>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="User Management" model="Account" addPath="authority/user" addMethod="editNew" excelMethod="excel()" />
		</s:if>
		<s:else>
			<custom:navigation father="User Query" model="Account"/>
		</s:else>
		<form name="queryResult" action="<%=basePath%>authority/user!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:searchBody row="1">
				<custom:searchItem title="Account" side="first">
					<input type="text" name="userVo.loginName" id="loginName" value="${userVo.loginName}" maxlength="15" />
				</custom:searchItem>
				<custom:searchItem title="Name" >
					<input type="text" name="userVo.userName" id="userName" value="${userVo.userName}" maxlength="15" />
				</custom:searchItem>
				<custom:searchItem title="Phone" >
					<input type="text" name="userVo.phone" id="phone" value="${userVo.phone}" maxlength="15" />
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true" width="2%">
					<custom:sort property="loginName" page="${page}" width="10%">Account</custom:sort>
					<custom:sort property="userRole.roleName" page="${page}" width="10%">Role</custom:sort>
					<custom:sort property="userOrg.orgName" page="${page}" width="10%">Company</custom:sort>
					<custom:sort property="userName" page="${page}" width="10%">Name</custom:sort>
					<custom:sort property="e_mail" page="${page}" width="10%">Email</custom:sort>
					<custom:sort property="mobilephone" page="${page}" width="15%">Phone</custom:sort>
					<custom:sort property="description" page="${page}" width="15%">description</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="userID" index="i" var="u">
					<custom:tableItem>${u[2].loginName}</custom:tableItem>
					<custom:tableItem>${u[1].roleName}</custom:tableItem>
					<custom:tableItem>${u[0].orgName}</custom:tableItem>
					<custom:tableItem>${u[2].userName}</custom:tableItem>
					<custom:tableItem>${u[2].e_mail}</custom:tableItem>
					<custom:tableItem>${u[2].mobilephone}</custom:tableItem>
					<custom:tableItem>
						<div title="${u[2].description}" style="width:200px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
			        		<label>${u[2].description}</label>
			        	</div>
					</custom:tableItem>
					<c:if test="${editble == 1}">
						<custom:tableItem>
							<custom:show path="authority/user" id="${u[2].userID}"/>
							<custom:edit path="authority/user" id="${u[2].userID}"/>
							<custom:del path="authority/user" id="${u[2].userID}" method="delete"/>
						</custom:tableItem>	
					</c:if>
					<c:if test="${editble == 0}">
						<custom:tableItem>
							<custom:show path="authority/user" id="${u[2].userID}"/>
						</custom:tableItem>
					</c:if>
				</custom:tableBody>
			</custom:table>
			<s:include value="/common/page.jsp">
				<s:param name="pageName" value="'page'"/>
				<s:param name="formName" value="'queryResult'"/>
				<s:param name="linkCount" value="5"/>
		    </s:include>
		</form>
	</body>
</html>