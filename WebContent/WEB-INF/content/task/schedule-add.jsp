<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head> 
		<script src="<%=basePath%>scripts/common/dhtmlxCombo/dhtmlxcombo.js" type="text/javascript"></script>
		<script type="text/javascript">
			function sub1() {
				$("input[name='sch.driverid']").val($($("#td1 input")[1]).val());
				$("input[name='sch.vehicleid']").val($($("#td2 input")[1]).val());
				if (-1 == $("input[name='sch.driverid']").val()) {
					alert("Please choose the Plan Driver Name!");
					return;
				}
				if (-1 == $("input[name='sch.vehicleid']").val()) {
					alert("Please choose the Plan Vehicle Number!");
					return;
				}
				if ("" == $("#startime").val()) {
					alert("Please choose the Plan Start Time!");
					return;
				}
				if ("" == $("#endtime").val()) {
					alert("Please choose the Plan End Time!");
					return;
				}
				if (-1 == $("#routeid").val()) {
					alert("Please choose the Route!");
					return;
				}
				$.ajax( {
					type : "POST",
					url : basePath + "task/schedule!checkoutTime",
					async : false,
                    data : {
                    	startime : $("#startime").val(),
                    	endtime : $("#endtime").val(),
                    	vehicleid : $("input[name='sch.vehicleid']").val(),
                    	scheduleid : $("#scheduleid").val()
                    },
                    success : function(data) {
                        if ('repeat' == data) {
                            alert("This car has been scheduled in this time period!");
                            return;
                        }
                        
                        $.ajax( {
        					type : "POST",
        					url : basePath + "task/schedule!checkDriverTime",
        					async : false,
                            data : {
                            	startime : $("#startime").val(),
                            	endtime : $("#endtime").val(),
                            	driverid : $("input[name='sch.driverid']").val(),
                            	scheduleid : $("#scheduleid").val()
                            },
                            success : function(data) {
                                if ('repeat' == data) {
                                    alert("This Driver has been scheduled in this time period!");
                                    return;
                                }
        						$("#queryResult").submit();
                            }
        				});
                    }
				});
			}
		</script>
	</head>
	<body>
		<c:if test="${null == sch.scheduleid}">
			<custom:navigation father="Task Management" model="Schedules-RFID" operate="Add" saveMethod="sub1();" homePath="task/schedule" />
		</c:if>
		<c:if test="${null != sch.scheduleid}">
			<custom:navigation father="Task Management" model="Schedules-RFID" operate="Edit" saveMethod="sub1();" homePath="task/schedule" />
		</c:if>	
		<form id="queryResult" action="<%=basePath%>task/schedule!save" name="queryResult" method="post">
			<input type="hidden" id="scheduleid" name="sch.scheduleid" value="${sch.scheduleid}"/>
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="18%">Plan Driver Name:</td>
						<td width="82%" style="text-align: left;" id="td1">
							<custom:selDriver name="sch.driverid" value="${sch.driverid}"/>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Plan Vehicle Name:</td>
						<td style="text-align: left;" id="td2">
							<custom:selVehName name="sch.vehicleid" value="${sch.vehicleid}" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Plan Start Time:</td>
						<td style="text-align: left;">
							<label class="input_date">
								<input type="text" style="width: 160px;" name="sch.startime" value="${sch.startime}" id="startime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:&quot;#F{$dp.$D('endtime')}&quot;, readOnly:true});this.blur()" />
							</label>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Plan End Time:</td>
						<td style="text-align: left;">
							<label class="input_date">
								<input type="text" style="width: 160px;" name="sch.endtime" value="${sch.endtime}" id="endtime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:&quot;#F{$dp.$D('startime')}&quot; , readOnly:true});this.blur()" />
							</label>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Route:</td>
						<td style="text-align: left;">
							<s:select list="routeList" name="sch.routeid" id="routeid" theme="simple" listKey="key" listValue="value" headerKey="-1" headerValue="Please choose"></s:select>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Duty:</td>
						<td style="text-align: left;">
							<input type="text" name="sch.duty" value="${sch.duty}" />
						</td>
					</tr>
			</table>
		</div>
	</form>
</body>
	<!-- <script type="text/javascript">
	   var vehicleid = dhtmlXComboFromSelect("vehicleid");
	   vehicleid.enableFilteringMode(true);
	   var driverid = dhtmlXComboFromSelect("driverid");
	   driverid.enableFilteringMode(true);
	</script> -->
</html>