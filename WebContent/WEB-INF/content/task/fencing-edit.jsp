<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>

 
<script src="<%=basePath%>scripts/common/dhtmlxCombo/dhtmlxcombo_3.js" type="text/javascript"></script>
<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	    <style type="text/css">
		      html { height: 100% }
		      body { height: 100%; margin: 0; padding: 0 }
		      #map_canvas { height: 100% }
	    </style>
    	<!-- <script src="http://maps.google.com/maps/api/js?v=3&amp;sensor=false"></script> -->
    	<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/OpenLayers-2.12/OpenLayers.js"></script>
    	<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/olayers.js"></script>
	</head>
	<custom:navigation father="Task Management" model="Geo Fencing" operate="Edit" saveMethod="sub2();" homePath="task/fencing" />
	
	<form id="equip_form" action="<%=basePath%>task/fencing!create1" target="" method="post">  
	 <input type="hidden" size="300" name="fenname" id="fenname" />
	  <input type="hidden"  name="geofencingid" id="geofencingid" value="${id}"/>
	<body  >
	<div class="pop_main">
	<table>  <tr><td rowspan="15" height="400px" width="700px">
	
     	<div id="basicMap" style="width:100%; height:100%"></div>
		
		<br />
	</td> 
	<td class="text_bg" >Caption:</td>
	<td  ><input type="text" name="caption" id="caption" value="${caption }" style="width: 150px"></td>
	</tr>
	<tr > <td class="text_bg" style="width: 20%">Plate Number:</td><td   >
	 <s:select id="vehicleid" name="vehicleid" list="list" listKey="key" listValue="value"    headerKey="-1" headerValue="Please choose" theme="simple"   cssStyle="width:150px;"  /> 
	   
	  </td> </tr>
	<tr><td colspan="2">
		<div style="margin-left:100px;"  class="button_input"> 
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button"  name="draw"  onclick="draw1()"  value="Draw"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button"  name="delete" onclick="remov();" value="Delete"/>
		</div>
		</td>
	 </tr>
	 <tr height="300"><td colspan="2"></td>
	 </tr>
	</table> 
		</div>
 
	</body>
	</form>
	   <script type="text/JavaScript">
	   $(function(){
		   increate();
		   catchMaterials('');
		});
	   var vehicleid = dhtmlXComboFromSelect_3("vehicleid");//姓名
	   vehicleid.enableFilteringMode(true);
	   
	   function catchMaterials(text) {
			var v = new Array();
			$.ajax({
				url :  basePath +"task/fencing!catchArch",
				type : "POST",
				async : false,
				data : {
					"vehname" : text,
					"id":$("#geofencingid").val()
				},
				dataType : "json",
				success : function(data) {
					if(0 != data.length){
						for(var i = 0; i < data.length; i++) {
							v[i] = {value : data[i].key, text : data[i].value};
						}
						vehicleid.optionsArr = new Array();
						vehicleid.addOption(v);
					}
				},
				error : function(json) {
					window.history.back();
				}
			});
			v = null;
	    }
		
	   
	      function sub2(){
	    	  if(null == $("#caption").val() || "" == $("#caption").val()){
	    		  alert("Please enter the Caption");
	    		  return false;
	    	  }
	    	  var veh = vehicleid.getSelectedValue();
			  var vehanem=($("input[name='vehicleid']").val());
	    	  var b=true;
	    	  $.ajax({
	  			type : "POST",
	  			async : false,
	  			dataType : 'text',
	  			data : {
	  				'caption' :  $("#caption").val(),
	  				'id' :  $("#geofencingid").val()
	  			},
	  			url : basePath + "task/fencing!check2",
	  			success : function(data) {
	  				 
	  				 if('[false]' ==data){
	  					 alert("You enter the Caption Already exists ");
	  					 b = false;
	  				 }
	  			}
	  		  });
	    	  if(null != veh || "" != vehanem){
	    		  $.ajax({
			  			type : "POST",
			  			async : false,
			  			dataType : 'text',
			  			data : {
			  				'vehicleid' :  vehicleid.getSelectedValue(),
			  				'id' :  $("#geofencingid").val()
			  			},
			  			url : basePath + "task/fencing!getveh1",
			  			success : function(data) {
			  				 if('[false]' == data){
			  					 alert("You enter the Plate Number Already exists ");
			  					 b = false;
			  				 }
			  			}
			  		  });	  
			  }
	    	  
	    	
	    	  if(null == $("#fenname").val() || "" == $("#fenname").val()){
	    		  alert("Please draw the electronic map");
	    		  return false;
	    	  }
    	 
			 if(null != listlength && (listlength.length>0 && listlength.length <= 3)){
				 alert("Graph structure does not change!");
				 return false;
			 }
				
	    	  if(b){
	    		  $("#equip_form").submit();
	    	  } 
	    	  return b;
	    	  
	      }
	      
	      
	   </script>
	 
</html>