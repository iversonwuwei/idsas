<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script src="<%=basePath%>scripts/common/dhtmlxCombo/dhtmlxcombo_3.js" type="text/javascript"></script>
<html>
<head>
	<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/analy/fault.js"></script>
	</head>
	<body>
	<c:if test="${null != specialid }">
	<custom:navigation father="Fault Analyzer" model="Special Instruction" operate="Edit" saveMethod="sub1();" homePath="analy/spe-instruction" />
	</c:if>
	
	<c:if test="${null == specialid }">
	  <custom:navigation father="Fault Analyzer" model="Special Instruction" operate="Add" saveMethod="sub1();" homePath="analy/spe-instruction" />
	</c:if>
		
		<form id="inform"  action="<%=basePath%>analy/spe-instruction!create" name="queryResult" method="post">
			<input type="hidden" name="specialid" id="specialid" value="${id}" /> 
			<input type="hidden" name="success" id="success" value="${success}" /> 
			<input type="hidden" name="engdefinition" id="engdefinition"  /> 
			<input type="hidden" name="vehicleid" id="vehicleid"  /> 
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="18%">Plate Number:</td>
						<td width="82%" style="text-align: left;" class="valid">
						 <custom:selVeh   name="vehicleid1" value="${vehicleid }" />
						 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Definition:</td>
						<td style="text-align: left;" class="valid">
						   <s:select id="code" name="code" list="listfa" listKey="key" listValue="value"    headerKey="" headerValue="Please choose" theme="simple"   cssStyle="width:150px;"  /> 
						</td>
					</tr>
					 

				</table>
			</div>
			
			  <script type="text/JavaScript">
	 
	  
		function sub1(){
		  if (confirm("This operation is very dangerous, Are you sure want to continue")) {
			var b = true;
			var veh = select_vehicle.getSelectedValue();
			 
			var vehiclename=select_vehicle.getComboText();
		 
			if(null == veh || ""==vehiclename){
				alert('Please select Plate Number');
				return false;
			}
			if(null == $("#code").val() ||　"" == $("#code").val()){
				alert("Please select Definition");
				return false;
			}
		
			 $.ajax({
		  			type : "POST",
		  			async : false,
		  			dataType : 'text',
		  			data : {
		  				'specialid' :  $("#specialid").val(),
		  				'vehicleid' :  select_vehicle.getSelectedValue(),
		  				'code' :  $("#code").val()
		  				 
		  			},
		  			url : basePath + "analy/spe-instruction!check",
		  			success : function(data) {
		  				 if('[false]' == data){
		  					 alert("You enter the Plate Number Already exists ");
		  					 b = false;
		  				 }
		  			}
		  		  });	  
			if(b){
				$("#vehicleid").val(select_vehicle.getSelectedValue());
				$("#engdefinition").val($("#code").find("option:selected").text());
				$("#inform").submit();
			}
			return b;
		  }
		}
	   </script>
		</form>
	</body>
</html>