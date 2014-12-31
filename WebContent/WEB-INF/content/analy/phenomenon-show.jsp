<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<body>
		<custom:navigation father="Fault Analyzer" model="Fault Phenomenon" operate="Show" homePath="analy/phenomenon" />
		<div class="pop_main">
			<table cellspacing="0" cellpadding="0">
				<tr>
					<td class="text_bg" width="18%">Vehicle Name:</td>
					<td width="82%" style="text-align: left;" class="valid">${phe.vehiclename}</td>
				</tr>
				<tr>
					<td class="text_bg" >Plate number:</td>
					<td style="text-align: left;" class="valid">${phe.licenseplate}</td>
				</tr>
				<tr>
					<td class="text_bg">Driver Number:</td>
					<td style="text-align: left;"  class="valid">${phe.drivernumber}</td>
				</tr>
				<tr>
					<td class="text_bg">Driver Name:</td>
					<td style="text-align: left;"  class="valid">${phe.drivername}</td>
				</tr>
				<tr>
					<td class="text_bg">Longitude:</td>
					<td style="text-align: left;" class="valid">${phe.longitude}</td>
				</tr>
				<tr>
					<td class="text_bg">Latitude:</td>
					<td style="text-align: left;" class="valid">${phe.latitude}</td>
				</tr>
				<tr>
					<td class="text_bg">Fault Code:</td>
					<td style="text-align: left;" class="valid">${phe.faultcode.engdefinition}</td>
				</tr>
				<tr>
					<td class="text_bg">Fault Time:</td>
					<td style="text-align: left;" class="valid">${phe.faulttime}</td>
				</tr>
				<tr>
					<td class="text_bg">Isremove:</td>
					<td style="text-align: left;" class="valid">
						<c:if test="${phe.isremove == 'N'}">No</c:if>
						<c:if test="${phe.isremove == 'Y'}">Yes</c:if>
					</td>
				</tr>
				<tr>
					<td class="text_bg">Remove Time:</td>
					<td style="text-align: left;" class="valid">${phe.removetime}</td>
				</tr>
				<tr>
					<td class="text_bg">Remover:</td>
					<td style="text-align: left;" class="valid">${phe.remover}</td>
				</tr>
			</table>
		</div>
</body>
</html>