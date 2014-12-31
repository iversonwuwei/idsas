<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<body>
		<custom:navigation father="Task Management" model="Trip Booking" addPath="task/booking" addMethod="add" />
		<form name="queryResult" action="<%=basePath%>task/booking!index" method="post">
			 <custom:searchBody row="2">
				<custom:searchItem title="Date" side="first">
					 <custom:date dateName="bkVo.bookingdate" dateValue="${bkVo.bookingdate}" id="bookingdate" ></custom:date>
				</custom:searchItem>
					<custom:searchItem title="Duty"  >
					<input type="text" name="bkVo.duty" id="duty" value="${bkVo.duty}" maxlength="15" />
				</custom:searchItem>
				<custom:searchItem title="Destination" side="last">
					  <input type="text" name="bkVo.destination" value="${bkVo.destination }" >
				</custom:searchItem>
				<custom:searchItem title="Route" side="first">
					<s:select list="routeList" name="bkVo.route" id="route" listKey="value" listValue="value" headerKey="-1" headerValue="All" theme="simple"></s:select>
				</custom:searchItem>
				<custom:searchItem title="Vehicle Type">
					 <s:select id="vehicletypeid" name="bkVo.vehicleid" list="listva" listKey="key" listValue="value"    headerKey="-1" headerValue="All" theme="simple"   cssStyle="width:103px;"  /> 
				</custom:searchItem>
				<custom:searchItem title=""  >
				
				</custom:searchItem>
				<custom:searchItem side="last">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
					<custom:sort property="bookingdate" page="${page}" width="10%">Date</custom:sort>
					<custom:sort property="starttime" page="${page}" width="11%">Start Time</custom:sort>
					<custom:sort property="endtime" page="${page}" width="11%">End Time</custom:sort>
					<custom:sort property="vehicletype" page="${page}" width="11%">Vehicle Type</custom:sort>
					<custom:sort property="destination" page="${page}" width="11%">Destination</custom:sort>
					<custom:sort property="route" page="${page}" width="10%">Route</custom:sort>
					<custom:sort property="duty" page="${page}" width="10%">Duty</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="bookingid" index="i" var="u">
					<custom:tableItem>${u.bookingdate}</custom:tableItem>
					<custom:tableItem>${u.starttime}</custom:tableItem>
					<custom:tableItem>${u.endtime}</custom:tableItem>
					<custom:tableItem>${u.vehicletype}</custom:tableItem>
					<custom:tableItem>${u.destination}</custom:tableItem>
					<custom:tableItem>${u.route}</custom:tableItem>
					<custom:tableItem>${u.duty}</custom:tableItem>
					<custom:tableItem>
						<custom:edit path="task/booking" id="${u.bookingid}"/>
						<custom:del path="task/booking" id="${u.bookingid}"/>
						<a href="<%=request.getContextPath()%>/task/booking!bedit?id=${u.bookingid}"><img title="Add" src="<%=request.getContextPath() %>/images/booking.gif" complete="complete"/></a>
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