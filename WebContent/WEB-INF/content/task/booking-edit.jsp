<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
 
	</head>
	<body>
		<custom:navigation father="Task Management" model="Trip Booking" operate="Edit" saveMethod="sub1();" homePath="task/booking" />
		<form  id="booking" action="<%=basePath%>task/booking!save" name="queryResult" method="post">
			<input type="hidden" name="bookingid" id="bookingid" value="${id}" /> 
			<input type="hidden" name="isdelete" id="isdelete" value="${isdelete}" /> 
			<input type="hidden" name="vehicletype" id="vehicletype"   /> 
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="18%">Date:</td>
						<td width="82%" style="text-align: left;" class="valid">
						 <custom:date dateName="bookingdate" dateValue="${bookingdate}" id="bookingdate" ></custom:date>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Vehicle Type:</td>
						<td style="text-align: left;" class="valid">
						   <s:select id="vehicletypeid" name="vehicletypeid" list="listva" listKey="key" listValue="value"    headerKey="-1" headerValue="Please choose" theme="simple"   cssStyle="width:187px;"  /> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Start Time:</td>
						<td style="text-align: left;">
							<label class="input_date">
							<input id="starttime" name="starttime" value="${starttime}" onfocus="WdatePicker({dateFmt:'HH:mm:ss', maxDate:'#F{$dp.$D(\'endtime\')||\'%H:%m:%s\'}'})"/>
							</label>
						</td>
					</tr>
					<tr>
						<td class="text_bg">End Time:</td>
						<td style="text-align: left;">
							<label class="input_date">
							<input id="endtime" name="endtime" value="${endtime}" onfocus="WdatePicker({dateFmt:'HH:mm:ss', maxDate:'%H:%m:%s',minDate:'#F{$dp.$D(\'starttime\')}'})"/>
							</label>
						</td>
					</tr>
			 
					<tr>
						<td class="text_bg">Destination:</td>
						<td style="text-align: left;"  class="valid">
						<input type="text" id="destination" name="destination" value="${destination}" maxlength="30" class="5,1,30" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Route:</td>
						<td style="text-align: left;"  class="valid">
						 <s:select id="route" name="route" list="listfenc" listKey="value" listValue="value"    headerKey="-1" headerValue="Please choose" theme="simple"   cssStyle="width:187px;"  /> 
						</td>
					</tr>
					
						<tr>
						<td class="text_bg">Duty:</td>
						<td style="text-align: left;"  class="valid">
						<input type="text" id="duty" name="duty" value="${duty}" maxlength="30" class="5,1,30" />
						</td>
					</tr>
				</table>
			</div>
		</form>
		  <script type="text/JavaScript">
		    function sub1(){
		    	var b=true;
		    	$.ajax({
		  			type : "POST",
		  			async : false,
		  			dataType : 'json',
		  			data : {
		  				'id' :  $("#bookingid").val(),
		  				'bookingdate': $("#bookingdate").val()
		  				 
		  			},
		  			url : basePath + "task/booking!check",
		  			success : function(data) {
		  				if('false' == data){
		  					 alert("You enter the Date Already exists ");
		  					 b = false;
		  				 }
		  			}
		  		  });
		    	if(!b){
		    		return b;
		    	}
		    	var startime =$("#starttime").val();
		    	var endtime =$("#endtime").val();
		    	var bookingdate =$("#bookingdate").val();
		    	var vehicletypeid =$("#vehicletypeid").val();
		    	var route =$("#route").val();
		    	if(null == bookingdate || ""== bookingdate){
		    		alert('Please choose Date');
		    		return false;
		    	}
		    	if(null == vehicletypeid || -1== vehicletypeid){
		    		alert('Please choose Vehicle Type');
		    		return false;
		    	}
		    	if(null == route || -1 == route){
		    		alert('Please choose Route');
		    		return false;
		    	}
		    	if(null == startime || "" == startime){
		    		alert('Please choose  Start Time');
		    		return false;
		    	}
		    	if(null == endtime || "" == endtime){
		    		alert('Please choose End Time');
		    		return false;
		    	}
		    	 $("#vehicletype").val($("#vehicletypeid").find("option:selected").text());
		    	$("#booking").submit();
		    }
		   </script>
	</body>
</html>