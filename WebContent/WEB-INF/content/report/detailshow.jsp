<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
       <script type="text/javascript">
       $(document).ready(function() {
    	   select_vehicle.attachEvent('onChange', getSchedule);
    	   getSchedule();
	        if(0 < "${fn:length(page.result)}"){
	        	var mysuperTable = new superTable("maintable5422",{headerRows :1,fixedCols :3});
	        }
	     });
       function sub2(){
    	   if(select_vehicle.getSelectedValue()==null||select_vehicle.getSelectedValue()==""||select_vehicle.getSelectedValue()==-1){
    		   alert("Please Select Plate Number!");
    		   return;
    	   }
    	   var str1= $("#sel_schedule").find("option:selected").text();
    		$("#sdu").val(str1);
    		beforeSubmit();
    	   document.queryResult.submit();
       }
	        function excel(){
	        	if(0 == "${fn:length(page.result)}"){
	            	alert("No data to export！");
	            	return;
	        	}
	        	document.forms['kindForm'].action='<%=basePath%>report/detailshow!exportDetail';
	            document.forms['kindForm'].submit();
	            document.forms['kindForm'].action='<%=basePath%>report/detailshow!index';
	        }
	        function getSchedule() {
	        	if(select_vehicle.getSelectedValue()==null||select_vehicle.getSelectedValue()==""||select_vehicle.getSelectedValue()==-1){
	        		$("#sel_schedule option").remove();
    				$("#sel_schedule").append("<option value='-1'>Please choose schedule...</option>");
	        	}else{
	        	$.ajax({
	        	    type : "POST",
	        	    async : false,
	        	    dataType : 'json',
	        	    data : {
	        	    	'vo.vehID' : select_vehicle.getSelectedValue()
	        	    },
	        	    url : basePath + "monitor/display!getSchedule",
	        	    success : function(d) {
	        			if("empty" != d.result) {
	        				$("#sel_schedule option").remove();
	        				$("#sel_schedule").append("<option value='-1'>Please choose schedule...</option>");
	        				for ( var i = 0; i < d.length; i++) {
	        					if("${dvo.behValue}"!=""&&d[i].key=="${dvo.behValue}"){
		        					$("#sel_schedule").append("<option value='" + d[i].key + "' selected='selected'>" + d[i].value + "</option>");
	        					}else{
		        					$("#sel_schedule").append("<option value='" + d[i].key + "'>" + d[i].value + "</option>");
	        					}
	        				}
	        			} else {
	        				$("#sel_schedule option").remove();
	        				$("#sel_schedule").append("<option value='-1'>Please choose schedule...</option>");
	        			}
	        	    },
	        	    error : function(d) {
	        			alert("Exception occurs!");
	        		}
	        	});
	        	}
	        }
       </script>
    </head>
	<body>
		<custom:navigation father="Chart Report" model="Report Trip" excelMethod="excel()" />
		<form id="kindForm" name="queryResult" action="<%=basePath%>report/detailshow!index" method="post">
			 <s:hidden name="selectype" value="true"/>
			 <custom:searchBody row="2">
				 <custom:searchItem title="Department" side="first">
					<custom:selCom name="dvo.deptId" value="${dvo.deptId}" />
				</custom:searchItem>
				<custom:searchItem title="Plate Number" side="last">
					<custom:selVeh name="dvo.vehId" value="${dvo.vehId}" />
				</custom:searchItem>
				<custom:searchItem title="Schedule" side="first">
					<select id="sel_schedule" name="dvo.behValue" style="width: 310px;">
							<option value="-1">Please choose schedule...</option>
					</select>
				</custom:searchItem>
				<custom:searchItem title="Type">
					<input type="hidden" id="sdu" name="sdu" value="${sdu}"/>
				    <s:select list="btlist" listKey="key" listValue="value" headerKey="-1" headerValue="ALL" theme="simple" id="behType" name="dvo.behType"></s:select>
				</custom:searchItem>
				<custom:searchItem side="last" width="30%">
					<custom:search onclick="sub2();" />
				</custom:searchItem>
			</custom:searchBody>
		<div class="main" >
				<table id="maintable5422" border="0" class='zhzd' style="width: 1300px">
					<thead>
				       <tr class="Reprot_Head">
        					<td width="50px"><label>No.</label></td>
					<custom:sort property="driver_name" page="${page}" width="60px">Driver</custom:sort>
					<custom:sort property="vehcode" page="${page}" width="100px">Plate Number</custom:sort>
					<custom:sort property="driver_name" page="${page}" width="90px">Department</custom:sort>
					<custom:sort property="orgName" page="${page}" width="70px">Fleet</custom:sort>
					<custom:sort property="turnSpeed" page="${page}" width="70px">Geo</custom:sort>
					<custom:sort property="riqi" page="${page}" width="90px">Date</custom:sort>
					<custom:sort property="timeBegin" page="${page}" width="80px">Start Time</custom:sort>
					<custom:sort property="timeEnd" page="${page}" width="80px">End Time</custom:sort>
					<custom:sort property="timeCont" page="${page}" width="100px">Continuance</custom:sort>
					<custom:sort property="behType" page="${page}" width="90px">Type</custom:sort>
					<custom:sort property="vehSpeed" page="${page}" width="60px">Speed</custom:sort>
					<custom:sort property="turnSpeed" page="${page}" width="90px">Lng</custom:sort>
					<custom:sort property="turnSpeed" page="${page}" width="90px">Lat</custom:sort>
					</tr>
					</thead>
				<custom:tableBody page="${page}" id="notId" index="i" var="v">
					<custom:tableItem>${v.driver_name}</custom:tableItem>
					<custom:tableItem>${v.vehcode}</custom:tableItem>
					<custom:tableItem>${v.dept_name}</custom:tableItem>
					<custom:tableItem>${v.orgName}</custom:tableItem>
					<custom:tableItem>${v.geo_caption}</custom:tableItem>
					<custom:tableItem>${v.riqi}</custom:tableItem>
					<custom:tableItem>${v.timeBegin}</custom:tableItem>
					<custom:tableItem>${v.timeEnd}</custom:tableItem>
					<custom:tableItem><custom:formatTime value="${v.timeCont}"></custom:formatTime></custom:tableItem>
					<custom:tableItem>${v.beh_name}</custom:tableItem>
					<custom:tableItem>${v.vehSpeed}</custom:tableItem>
					<custom:tableItem>${v.longitude}</custom:tableItem>
					<custom:tableItem>${v.latitude}</custom:tableItem>
					</custom:tableBody>
				</table>
			</div>
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