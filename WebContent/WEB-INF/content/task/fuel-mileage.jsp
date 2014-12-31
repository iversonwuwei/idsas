<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
    <head>
       <script type="text/javascript">
	        function excel(){
	        	if(0 == "${fn:length(page.result)}"){
	            	alert("No export data！");
	            	return;
	        	}
	        	document.forms['queryResult'].action='<%=basePath%>task/fuel-mileage!exportDetail';
	            document.forms['queryResult'].submit();
	            document.forms['queryResult'].action='<%=basePath%>task/fuel-mileage!index';
	        }
       </script>
    </head>
	<body>
		<custom:navigation father="Vehicle Report" model="Fuel Mileage" excelMethod="excel()" />
		<form name="queryResult" id="queryResult" action="<%=basePath%>task/fuel-mileage!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:searchBody >
				<custom:searchItem title="Plate Number" side="first">
				    <custom:selVeh name="fmvo.vehicleid" value="${fmvo.vehicleid}" />
				</custom:searchItem>
				<custom:searchItem title="Vehicle Type" >
				    <s:select list="typelist" name="fmvo.typeid" listKey="key" listValue="value" headerKey="-1" headerValue="All" theme="simple"/>
				</custom:searchItem>
				<custom:searchItem title="Date" >
				   <label class="input_date_l">
						<input type="text" style="width: 140px;" name="fmvo.startdate" value="${fmvo.startdate}" id="startMin" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:&quot;#F{$dp.$D('startMax')}&quot;, readOnly:true});this.blur()" /></label><label class="input_date_r"><input type="text" name="fmvo.enddate" value="${fmvo.enddate}" id="startMax" style="width: 140px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:&quot;#F{$dp.$D('startMin')}&quot;, readOnly:true});this.blur();"/>
					</label>
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table width="99%">
				<custom:tableHeader operate="false">
					<custom:sort property="vehiclename" page="${page}" width="15%">Plate Number</custom:sort>
					<custom:sort property="typename" page="${page}" width="15%">Vehicle Type</custom:sort>
					<custom:sort property="startdate" page="${page}" width="15%">Date</custom:sort>
					<custom:sort property="mileage" page="${page}" width="15%">Mileage(KM)</custom:sort>
					<custom:sort property="oil" page="${page}" width="20%">Fuel consumption(L)</custom:sort>
					<custom:sort property="oil" page="${page}" width="20%">Specific Fuel <br />consumption(L/100km)</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="vehicleid" index="i" var="u">
					<custom:tableItem>${u.vehiclename}</custom:tableItem>
					<custom:tableItem>${u.typename}</custom:tableItem>
					<custom:tableItem>${u.riqi}</custom:tableItem>
					<custom:tableItem><fmt:formatNumber value="${u.mileage/1000}" pattern="#.##"/></custom:tableItem>
					<custom:tableItem><fmt:formatNumber value="${u.oilcost/1000}" pattern="#.##"/></custom:tableItem>
					<custom:tableItem><fmt:formatNumber value="${u.oil}" pattern="#.##"/></custom:tableItem>
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