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
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/specific/system/google-poi-conf.js"></script>
		<script charset="UTF-8" type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=drawing&key=AIzaSyAs2HwT6PEJzn2P8WYiSScJpq0oigo_pGA&sensor=false"></script>
		<script type="text/javascript">
		$(function() {
			//载入地图
			initMap();
			//添加已有标记点,状态:非选中
			
			iconx = $("#icon").val();
			if(iconx == null||iconx==""){
				//新增时默认选中第一个图片
				iconx="tu_03";
				$("#tu_03").attr("src",basePath+"images/tu_03_.gif");
			} else {
				//编辑时初始化标记点
				selectIcon(iconx);
			}
			
				if($("#isvisible").val()=='T'){
					$("#cbox").attr('checked',true);
				}
			
		});
		
		function longla(){
			 
			if(iconx == null||iconx==""){
				//新增时默认选中第一个图片
				iconx="tu_03";
				$("#tu_03").attr("src",basePath+"images/tu_03_.gif");
			} else {
				//编辑时初始化标记点
				selectIcon(iconx);
			}
			 if(firstload){
			     deleteOverlays();
			 }
			 firstload=true;
			 addPoiMarkers($("#longitude").val(), $("#latitude").val(), "123", iconx, 'newMarker');
			// map.setCenter(new google.maps.LatLng($("#latitude").val(), $("#longitude").val()))设置中心点，现在google地图不好用 暂时测试不了 所以注释掉
			  
		}
		</script>
	</head>
	<body>
			<custom:navigation father="System Settings" model="POI" operate="Show"  homePath="system/google-poi-conf" />
		<form name="poiForm" method="post" action="<%=basePath%>system/google-poi-conf!save">
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
					    <td rowspan="13" width="55%" height="487px"><div id="basicMap" style="width:100%; height:100%;"></div></td>
						<td class="text_bg" width="10%">POI:</td>
						<td width="35%" style="text-align:left;" class="valid">${poi.caption}
							<input id="poiid" type="hidden" name="poi.poiid" value="${poi.poiid}"/>
							<input id="caption" type="hidden" name="poi.caption" value="${poi.caption}" maxlength="25" class="5,1,25"/>
						</td>
					</tr>
					<tr>
						<td class="text_bg" width="10%">Company:</td>
						<td width="35%" style="text-align:left;">
						${poi.comname }
						</td>
					</tr>
					<tr>
						<td class="text_bg" width="10%">Longitude:</td>
						<td width="35%" style="text-align:left;">
						${poi.longitude}
							<input id="longitude" type="hidden" name="poi.longitude" value="${poi.longitude}" onblur="longla();"    onkeyup="if(this.value==this.value2)return;if(this.value.search(/^\d*(?:\.\d{0,6})?$/)==-1)this.value=(this.value2)?this.value2:'';else this.value2=this.value;if(this.value2.length>10){this.value=''};" />
						</td>
					</tr>
					<tr>
						<td class="text_bg" width="10%">Latitude:</td>
						<td width="35%" style="text-align:left;">
						${poi.latitude}
							<input id="latitude" type="hidden" name="poi.latitude" value="${poi.latitude}"  onblur="longla();"   onkeyup="if(this.value==this.value2)return;if(this.value.search(/^\d*(?:\.\d{0,6})?$/)==-1)this.value=(this.value2)?this.value2:'';else this.value2=this.value;if(this.value2.length>10){this.value=''};"  />
						</td>
					</tr>
					<tr>
						<td class="text_bg" width="10%">Description:</td>
						<td width="35%" style="text-align:left;" class="valid">
						${poi.description }
						</td>
					</tr>
					<tr>
						<td class="text_bg" width="10%">Visibility:</td>
						<td width="35%">
							<label><input id="cbox" type="checkbox" disabled="disabled" readonly="readonly" onclick="setvis()" style="width:16px"/></label>
							<input id="isvisible" type="hidden" name="poi.isvisible" value="${poi.isvisible}"/>
						</td>
					</tr>
					<tr>
						<td class="text_bg" width="10%">Choose Icon:</td>
						<td width="35%" style="text-align:left;height: 37px" class="valid">&nbsp;
						<img id="tu_09" src="<%=basePath%>images/${poi.icon}.gif" />
							<input id="icon" type="hidden" name="poi.icon" value="${poi.icon}" />
						</td>
					</tr>
					<tr>
						<td colspan="2" width="10%" height="295px"></td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>