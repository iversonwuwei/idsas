<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/javascript">
			function excel(){
	     		if(0 == "${fn:length(page.result)}") {
	         		alert("No derived data!");
	         		return;
	     		}
	     		document.forms['queryResult'].action = basePath + 'vehicle/driver-list!exportDetail';
	         	document.forms['queryResult'].submit();
	         	document.forms['queryResult'].action = basePath + 'vehicle/driver-list!index';
	     	}
		</script>
	</head>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="Organization Management" model="Driver"  excelMethod="excel()" addPath="vehicle/driver-list"  addMethod="add" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Query" model="Driver List" excelMethod="excel()"  />
		</s:else>
		<form name="queryResult" action="<%=basePath%>vehicle/driver-list!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:searchBody>
				<custom:searchItem title="Driver Name" side="first">
					<custom:selDriver name="drivervo.driverid" value="${drivervo.driverid}"/>
				</custom:searchItem>
				<custom:searchItem title="Department" >
					<custom:selCom name="drivervo.departmentid" value="${drivervo.departmentid }"/>
				</custom:searchItem>
				<custom:searchItem title="Card Number" >
					<s:textfield theme="simple" name="drivervo.drivernumber" maxlength="50" onblur="this.value=$.trim(this.value)"/>
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
					<custom:sort property="drivername" page="${page}" width="10%">Driver Name</custom:sort>
					<custom:sort property="departmentname" page="${page}" width="10%">Department</custom:sort>
					<custom:sort property="drivernumber" page="${page}" width="10%">Card Number</custom:sort>
					<custom:sort property="type" page="${page}" width="10%">Card Type</custom:sort>
					<custom:sort property="gender" page="${page}" width="5%">Gender</custom:sort>
					<custom:sort property="email" page="${page}" width="15%">Email</custom:sort>
					<custom:sort property="phone" page="${page}" width="10%">Phone</custom:sort>
					<custom:sort property="description" page="${page}" width="19%">Description</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="driverid" index="i" var="d">
					<custom:tableItem>${d.drivername}</custom:tableItem>
					<custom:tableItem>${d.departmentname}</custom:tableItem>
					<custom:tableItem>${d.drivernumber}</custom:tableItem>
					<custom:tableItem>
						<c:if test="${d.type==0 || d.type==null }">Normal</c:if>
						<c:if test="${d.type==1 }">Temporary</c:if>
					</custom:tableItem>
					<custom:tableItem>${d.gender}</custom:tableItem>
					<custom:tableItem>${d.email}</custom:tableItem>
					<custom:tableItem>${d.phone}</custom:tableItem>
					<custom:tableItem>
						<div title="${d.description}" style="width:210px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
			        		<label>${d.description}</label>
			        	</div>
					</custom:tableItem>
					<custom:tableItem>
						<custom:show path="vehicle/driver-list" id="${d.driverid}" />
						<s:if test="%{editble == 1}">
							<custom:edit path="vehicle/driver-list" id="${d.driverid}" />
							<custom:del path="vehicle/driver-list" id="${d.driverid}" />
						</s:if>
					</custom:tableItem>
				</custom:tableBody>
			</custom:table>
			<s:include value="/common/page.jsp">
				<s:param name="pageName" value="'page'" />
				<s:param name="formName" value="'queryResult'" />
				<s:param name="linkCount" value="5" />
			</s:include>
		</form>
	</body>
</html>