<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
	
		
	</head>
	<body>
	
	
	<!-- 透明层 -->

		<c:if test="${editble == 1}">
			<custom:navigation father="Task Management" model="Driver changed Query" />
		</c:if>
	<%-- 	<c:if test="${editble == 0}">
			<custom:navigation father="User Management" model="User Query" addPath="authority/user" />
		</c:if> --%>
		<form name="queryResult" id="queryResult" action="<%=basePath%>task/schedule!rindex" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:searchBody row="2">
				<custom:searchItem title="Driver" side="first">
					<input type="text" name="schVo.driverid" value="${schVo.driverid}"/>
				</custom:searchItem>
				<custom:searchItem title="Return Driver" >
					<input type="text" name="schVo.redriverid" value="${schVo.redriverid}"/>
				</custom:searchItem>
				<custom:searchItem title="Vehicle Name" >
					<custom:selVehName name="schVo.vehicleid" value="${schVo.vehicleid}" />
				</custom:searchItem>
				<custom:searchItem title="Route" side="last">
					<s:select list="routeList" name="schVo.routeid" id="routeid" listKey="key" listValue="value" headerKey="-1" headerValue="All" theme="simple"></s:select>
				</custom:searchItem>
				<tr>
				<td class="text_right">Time:</td>
				<td colspan="2"><label class="input_date_l">
						<input type="text" style="width: 140px;" name="schVo.timeMin" value="${schVo.timeMin}" id="startMin" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:&quot;#F{$dp.$D('startMax')}&quot;, readOnly:true});this.blur()" /></label><label class="input_date_r"><input type="text" name="schVo.timeMax" value="${schVo.timeMax}" id="startMax" style="width: 140px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:&quot;#F{$dp.$D('startMin')}&quot;, readOnly:true});this.blur();"/>
					</label></td>
				
			
				
				<custom:searchItem side="last">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="false">
					
					
					<custom:sort property="routename" page="${page}" width="10%">Route</custom:sort>
					<custom:sort property="drivername" page="${page}" width="10%">Driver</custom:sort>
					<custom:sort property="redrivername" page="${page}" width="10%">Return Driver</custom:sort>
					<custom:sort property="vehiclenumber" page="${page}" width="10%">Vehicle Name</custom:sort>
					<custom:sort property="startime" page="${page}" width="13%">Start Time</custom:sort>
					<custom:sort property="endtime" page="${page}" width="13%">End Time</custom:sort>
					
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="scheduleid" index="i" var="u">
				<custom:tableItem>${u.routename}</custom:tableItem>
					<custom:tableItem>${u.drivername}</custom:tableItem>
					<custom:tableItem>${u.redrivername}</custom:tableItem>
					<custom:tableItem>${u.vehiclenumber}</custom:tableItem>
					<custom:tableItem>${u.startime}</custom:tableItem>
					<custom:tableItem>${u.endtime}</custom:tableItem>
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