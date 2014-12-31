<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript">
			function doExport() {
				if(0 == "${fn:length(page.result)}"){
         			alert("No derived data!");
         			return;
     			}
				document.queryResult.action = basePath + 'vehicle/police!doExport';
				document.queryResult.submit();
				document.queryResult.action = basePath + 'vehicle/police!index';
			}
		</script>
	</head>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="Organization Management" model="Police" addPath="vehicle/police" addMethod="editNew" excelMethod="doExport();" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Query" model="Police" excelMethod="doExport();" />
		</s:else>
		<form name="queryResult" action="<%=basePath%>vehicle/police!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:searchBody>
				<custom:searchItem title="Fleet" side="first">
					<custom:selTeam name="vo.fleetID" value="${vo.fleetID}" />
				</custom:searchItem>
				<custom:searchItem title="Police Name">
					<s:textfield id="policeName" name="vo.policeName" value="%{vo.policeName}" theme="simple" />
				</custom:searchItem>
				<custom:searchItem title="Device">
					<s:textfield id="deviceName" name="vo.deviceName" value="%{vo.deviceName}" theme="simple"/>
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader>
					<custom:sort property="policeName" page="${page}" width="10%">Police Name</custom:sort>
					<custom:sort property="fleetName" page="${page}" width="10%">Fleet</custom:sort>
					<custom:sort property="deptName" page="${page}" width="10%">Department</custom:sort>
					<custom:sort property="deviceName" page="${page}" width="10%">Device</custom:sort>
					<custom:sort property="gender" page="${page}" width="5%">Gender</custom:sort>
					<custom:sort property="email" page="${page}" width="13%">Email</custom:sort>
					<custom:sort property="phone" page="${page}" width="11%">Phone</custom:sort>
					<custom:sort property="description" page="${page}" width="20%">Description</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="policeID" index="i" var="v">
					<custom:tableItem>${v.policeName}</custom:tableItem>
					<custom:tableItem>${v.fleetName}</custom:tableItem>
					<custom:tableItem>${v.deptName}</custom:tableItem>
					<custom:tableItem>${v.deviceName}</custom:tableItem>
					<custom:tableItem list="{0:'hidden', 1:'male', 2:'female'}">${v.gender}</custom:tableItem>
					<custom:tableItem>${v.email}</custom:tableItem>
					<custom:tableItem>${v.phone}</custom:tableItem>
					<custom:tableItem>
						<div title="${v.description}" style="width:240px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
			        		<label>${v.description}</label>
			        	</div>
					</custom:tableItem>
					<s:if test="%{editble == 1}">
					<custom:tableItem>
						<custom:show path="vehicle/police" id="${v.policeID}"/>
						<custom:edit path="vehicle/police" id="${v.policeID}"/>
						<custom:del path="vehicle/police" id="${v.policeID}" method="destory" />
					</custom:tableItem>
					</s:if>
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