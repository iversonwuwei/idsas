<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
       <script type="text/javascript">
       $(document).ready(function() {
	        if(0 < "${fn:length(page.result)}"){
	        	var mysuperTable = new superTable("maintable5422",{headerRows :1});
	        }
	     });
	        function excel(){
	        	if(0 == "${fn:length(page.result)}"){
	            	alert("No  data!");
	            	return;
	        	}
	        	document.forms['kindForm'].action='<%=basePath%>query/track!exportDetail';
	            document.forms['kindForm'].submit();
	            document.forms['kindForm'].action='<%=basePath%>query/track!index';
	        }
	        function sub(){
	        	if(select_vehicle.getSelectedValue()==null || select_vehicle.getSelectedValue()==-1 || select_vehicle.getSelectedValue()==""){
	        		alert("Please select Plate Number");
	        		return ;
	        	}
	        	if($("#startMin").val()==null ||$("#startMin").val()=="" ){
	        		alert("Please enter Date");
	        		return;
	        	}
	        	if($("#startMax").val()==null ||$("#startMax").val()=="" ){
	        		alert("Please enter Date");
	        		return;
	        	}
	        
	        	beforeSubmit();
	        	document.queryResult.submit();
	        }
       </script>
    </head>
	<body>
		<custom:navigation father="Vehicle Report" model="Tracking Report" excelMethod="excel()"/>
		<form id="kindForm" name="queryResult" action="<%=basePath%>query/track!index" method="post">
		 <custom:searchBody>
				<custom:searchItem title="Fleet" side="first">
					<custom:selTeam name="trvo.fleetid" value="${trvo.fleetid}" />
				</custom:searchItem>
			<custom:searchItem title="Plate Number">
				<custom:selVeh name="trvo.busid" value="${trvo.busid}" />
			</custom:searchItem>
				<custom:searchItem title="Date">

<label class="input_date_l">
						<input type="text" style="width: 140px;" name="trvo.datemin" value="${trvo.datemin}" id="startMin" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:&quot;#F{$dp.$D('startMax');}&quot;, minDate:&quot;#F{$dp.$D('startMax',{H:-24});}&quot;,readOnly:true});this.blur()" /></label><label class="input_date_r"><input type="text" name="trvo.datemax" value="${trvo.datemax}" id="startMax" style="width: 140px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:&quot;#F{$dp.$D('startMin');}&quot;,maxDate:&quot;#F{$dp.$D('startMin',{H:24});}&quot;, readOnly:true});this.blur();"/>
					</label>				
					
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();" />
				</custom:searchItem>
			</custom:searchBody>
		<div class="main" >
			<table cellspacing="0" cellpadding="0" class="zhzd" id="maintable5422" style="margin: 0px;width: 100%">
			<thead>
			<tr class="Reprot_Head">
			
			<td width="4%" rowspan="1">
			<label>No.</label></td>				
			<td width="9%" rowspan="1" colspan="1" onclick="resort('o_busname', 1 );" style="cursor:pointer">Busname</td>
					<td width="10%" rowspan="1" colspan="1" onclick="resort('o_date', 1 );" style="cursor:pointer">Date</td>
					<td width="10%" rowspan="1" colspan="1" onclick="resort('o_time', 1 );" style="cursor:pointer">Time</td>
					<td width="10%" rowspan="1" colspan="1" onclick="resort('o_longitude', 1 );" style="cursor:pointer">Longitude</td>
					<td width="10%" rowspan="1" colspan="1" onclick="resort('o_latitude', 1 );" style="cursor:pointer">Latitude</td>
					<td width="8%" rowspan="1" colspan="1" onclick="resort('o_speed', 1 );" style="cursor:pointer">Speed(km/h)</td>
					<td width="8%" rowspan="1" colspan="1" onclick="resort('o_direction', 1 );" style="cursor:pointer">Direction</td>
				</tr>
				</thead>
				<tbody>
				   <s:iterator value="page.result" var="b" status="i">
				<tr class="Reprot_td" >
				<td>${i.index + 1 + (page.currentPage - 1) * page.pageSize}</td>
				<td>${b.o_busname}</td>
				<td>${b.o_date}</td>
				<td>${b.o_time}</td>
				<td>${b.o_longitude}</td>
				<td>${b.o_latitude}</td>
				<td>${b.o_speed}</td>
				<td>
				<c:if test="${b.o_direction>=0 && b.o_direction<=22}">North	</c:if>
					<c:if test="${b.o_direction>=338 && b.o_direction<=360}">North	</c:if>
						<c:if test="${b.o_direction>=23 && b.o_direction<=67}">Northeast</c:if>
							<c:if test="${b.o_direction>=68 && b.o_direction<=112}">East</c:if>
								<c:if test="${b.o_direction>=113 && b.o_direction<=157}">Southeast</c:if>
									<c:if test="${b.o_direction>=158 && b.o_direction<=202}">South</c:if>
										<c:if test="${b.o_direction>=203&& b.o_direction<=247}">Southwest</c:if>
											<c:if test="${b.o_direction>=248 && b.o_direction<=292}">West</c:if>
											<c:if test="${b.o_direction>=293 && b.o_direction<=337}">Northwest</c:if>
				</td>
				</tr>
				
				</s:iterator>
				</tbody>
				
				
				</table>
				</div>
			<%--
			
			<custom:table id="maintable5422" width="1310px">
				<custom:tableHeader operate="false" width="50px">
					<custom:sort property="o_busname" page="${page}" width="60px">Busname</custom:sort>
					<custom:sort property="o_date" page="${page}" width="100px">Date</custom:sort>
					<custom:sort property="o_time" page="${page}" width="100px">Time</custom:sort>
					<custom:sort property="o_longitude" page="${page}" width="90px">Longitude</custom:sort>
					<custom:sort property="o_latitude" page="${page}" width="70px">Latitude</custom:sort>
					<custom:sort property="o_speed" page="${page}" width="70px">Speed</custom:sort>
					<custom:sort property="o_direction" page="${page}" width="90px">Direction</custom:sort>
					
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="notId" index="i" var="v">
					<custom:tableItem>${v.o_busname}</custom:tableItem>
					<custom:tableItem>${v.o_date}</custom:tableItem>
					<custom:tableItem>${v.o_time}</custom:tableItem>
					<custom:tableItem>${v.o_longitude}</custom:tableItem>
					<custom:tableItem>${v.o_latitude}</custom:tableItem>
					<custom:tableItem>${v.o_speed}</custom:tableItem>
					<custom:tableItem>${v.o_direction}</custom:tableItem>
					
				</custom:tableBody>
			</custom:table> 
			 --%>
			
			
				
	<div class="page">
  				<s:include value="/common/page.jsp">
					<s:param name="pageName" value="'page'"/>
					<s:param name="formName" value="'queryResult'"/>
					<s:param name="linkCount" value="5"/>
				</s:include>
			</div>
		</form>
	</body>
</html>