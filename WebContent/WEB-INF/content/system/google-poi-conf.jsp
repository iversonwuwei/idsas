<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<body>
		<custom:navigation father="System Settings" model="POI" addPath="system/google-poi-conf" addMethod="add" />
		<form name="queryResult" action="<%=basePath%>system/google-poi-conf!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:searchBody>
				<custom:searchItem title="POI" side="first">
					<input type="text" name="vo.caption" id="caption" value="${vo.caption}" maxlength="25" />
				</custom:searchItem>
				<custom:searchItem width="20%">
					<custom:search onclick="sub();" />
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
					<custom:sort property="comname" page="${page}" width="15%">Company</custom:sort>
					<custom:sort property="caption" page="${page}" width="15%">POI</custom:sort>
					<custom:sort property="longitude" page="${page}" width="15%">Longitude</custom:sort>
					<custom:sort property="latitude" page="${page}" width="15%">Latitude</custom:sort>
					<custom:sort property="isvisible" page="${page}" width="15%">Visibility</custom:sort>
					<custom:sort property="description" page="${page}" width="15%">Description</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="poiid" index="i" var="v">
					<custom:tableItem>${v.comname}</custom:tableItem>
					<custom:tableItem>${v.caption}</custom:tableItem>
					<custom:tableItem>${v.longitude}</custom:tableItem>
					<custom:tableItem>${v.latitude}</custom:tableItem>
					<custom:tableItem><c:if test="${v.isvisible=='T'}">true</c:if><c:if test="${v.isvisible=='F'}">false</c:if></custom:tableItem>
					<custom:tableItem>
						<div title="${v.description}" style="width:170px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
			        		<label>${v.description}</label>
			        	</div>
					</custom:tableItem>
					<custom:tableItem>
						<custom:show path="system/google-poi-conf" id="${v.poiid}" />
						<custom:edit path="system/google-poi-conf" id="${v.poiid}"/>
						<custom:del path="system/google-poi-conf" id="${v.poiid}" />
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