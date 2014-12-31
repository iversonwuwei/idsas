<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script src="<%=basePath%>scripts/common/dhtmlxCombo/dhtmlxcombo_3.js" type="text/javascript"></script>
<script src="<%=basePath%>scripts/common/dhtmlxCombo/dhtmlxcombo_2.js" type="text/javascript"></script>
<html>
<head>
 
	</head>
	<body>
		<custom:navigation father="Task Management" model="Trip Booking" operate="Edit"  homePath="task/booking" />
		<form  id="booking" action="<%=basePath%>task/booking!create1" name="queryResult" method="post">
		  <input type="hidden" name="bookingid" id="bookingid" value="${bookingid }">
			  <custom:searchBody row="1">
				<custom:searchItem title="Plate Number" side="first" width="10%">
				 	 <s:select id="vehicleid" name="vehicleid" list="list" listKey="key" listValue="value"    headerKey="-1" headerValue="" theme="simple"   cssStyle="width:150px;"  /> 
				</custom:searchItem>
				<custom:searchItem title="Driver Name" width="10%">
					 <s:select id="driverid" name="driverid" list="listdriver" listKey="key" listValue="value"    headerKey="-1" headerValue="" theme="simple"   cssStyle="width:150px;"  /> 
				</custom:searchItem>
				<custom:searchItem side="last" width="30%">
				<div style="margin-left:100px;"  class="button_input"> 
					<input type="button" value="Save" onclick="sub1()">
					</div>
				</custom:searchItem>
			</custom:searchBody>
			
			 <custom:table id="main1">
				<custom:tableHeader operate="true">
				     <th>Vehicle Type</th>
				     <th>Vehicle Name</th>
				     <th>Plate Number</th>
			         <th>Driver Name</th>
			         <th>Driver Number</th>
				</custom:tableHeader>
				
				<tbody>
				<s:iterator value="listbookdeta" var="u" status="r">
				
				 <tr id="book_${u.detailid}">
				     <td align="center">${r.index+1}</td>
				    <td  align="center">${vehicletype}</td>
					<td  align="center">${u.vehiclename}</td>
					<td  align="center">${u.licenseplate}</td>
					<td  align="center">${u.drivername}</td>
					<td  align="center">${u.drivernumber}</td>
					<td  align="center">
					<a href="#" onclick="delete1(${u.detailid})">
					<img title="Delete" src="<%=request.getContextPath() %>/images/dtree/ico_delete.gif" complete="complete"/>
					 </a>
					</td>
				</tr>
				
				</s:iterator>
				 </tbody>
			</custom:table>
		</form>
		  <script type="text/JavaScript">
		  $(function(){
			   catchMaterials('');
			   catchPositions('');
			});
		   var vehicleid = dhtmlXComboFromSelect_3("vehicleid");//姓名
		   vehicleid.enableFilteringMode(true);
		   
		   var driverid = dhtmlXComboFromSelect_2("driverid");//姓名
		   driverid.enableFilteringMode(true);
		   
		   function catchMaterials(text) {
				var v = new Array();
				$.ajax({
					url :  basePath +"task/booking!catchArch1",
					type : "POST",
					async : false,
					data : {
						"vehname" : text
					},
					dataType : "json",
					success : function(data) {
						if(0 != data.length){
							for(var i = 0; i < data.length; i++) {
								v[i] = {value : data[i].key, text : data[i].value};
							}
							vehicleid.optionsArr = new Array();
							vehicleid.addOption(v);
						}  else {
							vehicleid.clearAll();
						} 
					},
					error : function(json) {
						window.history.back();
					}
				});
				v = null;
		    }
		   
		   function catchPositions(text) {
				var v = new Array();
				$.ajax({
					url :  basePath +"task/booking!catchArch",
					type : "POST",
					async : false,
					data : {
						"dname" : text
					},
					dataType : "json",
					success : function(data) {
						 
						if(0 != data.length){
							for(var i = 0; i < data.length; i++) {
								v[i] = {value : data[i].key, text : data[i].value};
							}
							driverid.optionsArr = new Array();
							driverid.addOption(v);
						} else {
							driverid.clearAll();
						} 
					},
					error : function(json) {
						window.history.back();
					}
				});
				v = null;
		    }
		   function delete1(id){
			   $.ajax({
		  			type : "POST",
		  			async : false,
		  			dataType : 'json',
		  			data : {
		  				'id' :  id
		  				 
		  			},
		  			url : basePath + "task/booking!deletebook",
		  			success : function(data) {
		  			}
		  		  });
			   var bookstr="#book_"+id;
			   $(bookstr).remove();
			   catchMaterials('');
			   catchPositions('');
		   }
		   function  sub1(){
		    if (confirm("You sure you want to save")) {
			   var veh = vehicleid.getSelectedValue();
			   var vehanem=($("input[name='vehicleid']").val());
			   var dri = driverid.getSelectedValue();
			   var drianem=($("input[name='driverid']").val());
			   if(null == veh || ""==vehanem){
				   alert("Please choose Plate Number");
				   return false;
			   }
			   if(null == dri || ""==drianem){
				   alert("Please choose Driver Name");
				   return false;
			   }
			   $.ajax({
		  			type : "POST",
		  			async : false,
		  			dataType : 'json',
		  			data : {
		  				'vehicleid' :  vehicleid.getSelectedValue(),
		  				'driverid' : driverid.getSelectedValue(),
		  				'bookingid' : $("#bookingid").val()
		  			},
		  			url : basePath + "task/booking!create1",
		  			success : function(data) {
		  				for(var i=0; i < data.length; i++) {
		  					$("#main1 tbody").append('<tr id=book_'+data[i].detailid+'> <td align=\'center\'>'+($("#main1 tbody tr").size()+1)+'</td><td align=\'center\'>'+'${vehicletype}'+'</td><td align=\'center\'>'+data[i].vehiclename+'</td><td align=\'center\'>'+data[i].licenseplate+'</td><td align=\'center\'>'+data[i].drivername+'</td><td align=\'center\'>'+data[i].drivernumber+'</td><td align=\'center\'><a href=\'#\' onclick=\'delete1('+data[i].detailid+')\' ><img title="Delete" src=\'<%=request.getContextPath() %>/images/dtree/ico_delete.gif\' complete=\'complete\'/></a></td></tr>');
		  				}
		  			}
		  		  });
			   vehicleid.setComboValue('');
			   vehicleid.setComboText('')
			   driverid.setComboValue('');
			   driverid.setComboText('')
			   catchMaterials('');
			   catchPositions('');
			   }
		   }
		   </script>
	</body>
</html>