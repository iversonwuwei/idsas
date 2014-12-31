<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script src="<%=basePath%>scripts/common/dhtmlxCombo/dhtmlxcombo_3.js" type="text/javascript"></script>
<html>
<head>
	<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/analy/fault.js"></script>
	</head>
	<body>
	 
	<custom:navigation father="Fault Analyzer" model="Special Instruction" operate="Show"   homePath="analy/spe-instruction" />
		<form id="inform"  action="<%=basePath%>analy/spe-instruction!create" name="queryResult" method="post">
			<input type="hidden" name="engdefinition" id="engdefinition"  /> 
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="18%">Plate Number:</td>
						<td width="82%" style="text-align: left;" class="valid">
						 ${ licenseplate}
						</td>
					</tr>
					<tr>
						<td class="text_bg">Definition:</td>
						<td style="text-align: left;" class="valid">
						  ${engdefinition }
						</td>
					</tr>
					<tr>
						<td class="text_bg">Creator:</td>
						<td style="text-align: left;" class="valid">
						  ${creater }
						</td>
					</tr>
					<tr>
						<td class="text_bg">Create Time:</td>
						<td style="text-align: left;" class="valid">
						  ${creatime }
						</td>
					</tr>
					 

				</table>
			</div>
			 
		</form>
	</body>
</html>