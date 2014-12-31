<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript">
	 		function doExcel(){
		     	if(0 == "${fn:length(page.result)}"){
		         	alert("No derived data!");
		         	return;
		     	}
     			document.forms['queryResult'].action = basePath + 'authority/role!doExport';
         		document.forms['queryResult'].submit();
         		document.forms['queryResult'].action = basePath + 'authority/role!index';
     		}
		</script>
	</head>
	<body>
		<s:if test="editble==0">
			<custom:navigation father="User Query" model="Role" excelMethod="doExcel()" />
		</s:if>
		<s:else>
			<custom:navigation father="User Management" model="Role" addPath="authority/role" addMethod="editNew" excelMethod="doExcel()" />
	  	</s:else>
		<form name="queryResult" action="<%=basePath%>authority/role!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:searchBody>
				<custom:searchItem title="Role Name" side="first">
					<input type="text" name="userVo.roleName" id="userVo.roleName" value="${userVo.roleName}" maxlength="25" />
				</custom:searchItem>
				<custom:searchItem width="20%">
					<custom:search onclick="sub();" />
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
					<custom:sort property="roleName" page="${page}" width="15%">Role Name</custom:sort>
					<custom:sort property="comname" page="${page}" width="15%">Company</custom:sort>
					<custom:sort property="description" page="${page}" width="15%">Description</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="roleID" index="i" var="v">
					<custom:tableItem>${v.roleName}</custom:tableItem>
					<custom:tableItem>${v.comname}</custom:tableItem>
					<custom:tableItem>
						<div title="${v.description}" style="width:320px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
			        		<label>${v.description}</label>
			        	</div>
					</custom:tableItem>
					<custom:tableItem>
						<custom:show path="authority/role" id="${v.roleID}"/>
					<c:if test="${editble==1&&v.roleID!=1}">
					<c:if test="${mainUser.userRole. aflag==0}">
						<custom:edit path="authority/role" id="${v.roleID}"/>
						<custom:del path="authority/role" id="${v.roleID}" method="delete"/>
					</c:if>
					<c:if test="${mainUser.userRole. aflag==1 && v.aflag==2}">
						<custom:edit path="authority/role" id="${v.roleID}"/>
						<custom:del path="authority/role" id="${v.roleID}" method="delete"/>
					</c:if>
					</c:if>
					</custom:tableItem>
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