<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	    <style type="text/css">
		      html { height: 100% }
		      body { height: 100%; margin: 0; padding: 0 }
		      #map_canvas { height: 100% }
	    </style>
    	<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/OpenLayers-2.12/OpenLayers.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/specific/system/poi-conf.js"></script>
		<script type="text/javascript">
			$(function() {
				initMap();
				var iconName = $("#icon").val();
				var lon = "${longitude}";
				var lat = "${latitude}";
				if(lon != "" && lon != null) {
					var lonlat = new OpenLayers.LonLat(lon, lat).transform(fromProjection, OSM_toProjection);
					addMarker(markerID, lonlat, "${caption}", iconName, true);
					map.setCenter(lonlat);	//中心点
				}
				if($("#isvisible").val() == 'T'){
					$("#cbox").attr('checked',true);
				}
			});
		</script>
	</head>
	<body>
	<custom:navigation father="System Settings" model="POI" operate="Show"  homePath="system/poi-conf" />
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
					    <td rowspan="12" width="60%" height="487px">
					    	<div id="basicMap" style="width:100%; height:100%;"></div>
					    </td>
						<td class="text_bg" width="10%">POI:</td>
						<td width="30%">${caption}</td>
					</tr>
					<tr>
						<td class="text_bg">Company:</td>
						<td>${comname}</td>
					</tr>
					<tr>
						<td class="text_bg">Longitude:</td>
						<td>${longitude}</td>
					</tr>
					<tr>
						<td class="text_bg">Latitude:</td>
						<td>${latitude}</td>
					</tr>
					<tr>
						<td class="text_bg" width="10%">Visibility:</td>
						<td width="35%">&nbsp;&nbsp;
							<label><input id="cbox" readonly="readonly" disabled="disabled" type="checkbox" onclick="setvis()" style="width:16px"/></label>
							<input id="isvisible" type="hidden" name="isvisible" value="${isvisible}"/>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Choose Icon:</td>
						<td style="height: 37px;">&nbsp;
						<c:if test="${icon == 'tu_03'}">
							<img id="tu_09" src="<%=basePath%>images/${icon}_.png" title="Head Office" />
						</c:if>
						<c:if test="${icon == 'tu_05'}">
							<img id="tu_09" src="<%=basePath%>images/${icon}_.png" title="Parking Lot" />
						</c:if>
						<c:if test="${icon == 'tu_07'}">
							<img id="tu_09" src="<%=basePath%>images/${icon}_.png" title="Branch Office" />
						</c:if>
						<input id="icon" type="hidden" name="icon" value="${icon}" />
						</td>
					</tr>
					<tr>
						<td class="text_bg" height="295px">Description:</td>
						<td height="295px">
							<textarea style="width: 100%;height: 100%" disabled="disabled">${description}</textarea>
						</td>
					</tr>
				</table>
			</div>
	</body>
</html>