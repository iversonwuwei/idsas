<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/authority/user.js"></script>
		<script type="text/javascript">
		</script>
	</head>
	<body>
	<%-- 	<c:if test="${editble == 1}">
			<custom:navigation father="User Management" model="User List" addPath="authority/user" addMethod="add" />
		</c:if>
		<c:if test="${editble == 0}">
			<custom:navigation father="User Management" model="User Query" addPath="authority/user" />
		</c:if> --%>
		<custom:navigation father="Fault Analyzer" model="Fault Phenomenon" />
		<form name="queryResult" action="<%=basePath%>analy/phenomenon!index" method="post">
			<%-- <s:hidden name="editble" value="%{editble}" /> --%>
			<custom:searchBody row="1">
				<custom:searchItem title="Plate Number" side="first">
					<input type="text" name="pheVo.vehiclename" id="vehiclename" value="${pheVo.vehiclename}" maxlength="15" />
				</custom:searchItem>
				<custom:searchItem title="Driver Name" >
					<input type="text" name="pheVo.drivername" id="drivername" value="${pheVo.drivername}" maxlength="15" />
				</custom:searchItem>
				<custom:searchItem title="Isremove" >
					<s:select list="#{'-1':'All', 'Y':'Yes', 'N':'No'}" name="pheVo.isremove" theme="simple" />
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
					<custom:sort property="licenseplate" page="${page}" width="10%">Plate Number</custom:sort>
					<custom:sort property="drivername" page="${page}" width="10%">Driver Name</custom:sort> 
					<custom:sort property="longitude" page="${page}" width="10%">Longitude</custom:sort>
					<custom:sort property="latitude" page="${page}" width="10%">Latitude</custom:sort>
					<custom:sort property="faulttime" page="${page}" width="10%">Fault Time</custom:sort>
					<custom:sort property="faultcode" page="${page}" width="8%">Fault Code Definition</custom:sort>
					<custom:sort property="isremove" page="${page}" width="10%">Remove</custom:sort>
					<custom:sort property="removetime" page="${page}" width="10%">Remove Time</custom:sort>
					<custom:sort property="remover" page="${page}" width="10%">Remover</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="faultid" index="i" var="u">
					<custom:tableItem>${u[1].licenseplate}</custom:tableItem>
					<custom:tableItem>${u[1].drivername}</custom:tableItem>
					<custom:tableItem>${u[1].longitude}</custom:tableItem>
					<custom:tableItem>${u[1].latitude}</custom:tableItem>
				 	<custom:tableItem>${u[1].faulttime}</custom:tableItem>
					<custom:tableItem>${u[0].engdefinition}</custom:tableItem>
					<custom:tableItem>
						<c:if test="${u[1].isremove == 'N'}">No</c:if>
						<c:if test="${u[1].isremove == 'Y'}">Yes</c:if>
					</custom:tableItem>
					<custom:tableItem>${u[1].removetime}</custom:tableItem>
					<custom:tableItem>${u[1].remover}</custom:tableItem>
					<custom:tableItem>
						<custom:show path="analy/phenomenon" id="${u[1].faultid}"/>
						<c:if test="${u[1].isremove == 'N'}">
							<a href="#" onclick="operItem('<%=basePath%>analy/phenomenon/${u[1].faultid}/cancelCode?vehiclename=${u[1].vehiclename}', true, 'Remove fault code phenomenon?');">
								<img src="<%=basePath%>images/dtree/ico_edit_h.gif" width="16" height="16" border="0" />
							</a>
							<custom:del path="analy/phenomenon" id="${u[1].faultid}" method="delete"/>
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