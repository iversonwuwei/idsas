<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head> 
	</head>
	<body>
		<custom:navigation father="Task Management" model="Schedules-RFID" operate="Show" homePath="task/schedule" />
		<div class="pop_main">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td class="text_bg" width="18%">Plan Driver Name:</td>
					<td width="82%" style="text-align: left;">
						${sch.plandrivername}
					</td>
				</tr>
				<tr>
					<td class="text_bg">Plan Vehicle Name:</td>
					<td style="text-align: left;">
						${sch.planvehiclenumber}
					</td>
				</tr>
				
				<tr>
					<td class="text_bg">Plan Start Time:</td>
					<td style="text-align: left;">
						${sch.planstartime}
					</td>
				</tr>
				<tr>
					<td class="text_bg">Plan End Time:</td>
					<td style="text-align: left;">
						${sch.planendtime}
					</td>
				</tr>
				
				<tr>
					<td class="text_bg">Driver Name:</td>
					<td style="text-align: left;">
						${sch.drivername}
					</td>
				</tr>
				<tr>
					<td class="text_bg">Vehicle Name:</td>
					<td style="text-align: left;">
						${sch.vehiclenumber}
					</td>
				</tr>
				<tr>
					<td class="text_bg">Start Time:</td>
					<td style="text-align: left;">
						${sch.startime}
					</td>
				</tr>
				<tr>
					<td class="text_bg">End Time:</td>
					<td style="text-align: left;">
						${sch.endtime}
					</td>
				</tr>
				<tr>
					<td class="text_bg">Route:</td>
					<td style="text-align: left;">
						${sch.routename}
					</td>
				</tr>
				<tr>
					<td class="text_bg">Duty:</td>
					<td style="text-align: left;">
						&nbsp;${sch.duty}
					</td>
				</tr>
		</table>
	</div>
</body>
</html>